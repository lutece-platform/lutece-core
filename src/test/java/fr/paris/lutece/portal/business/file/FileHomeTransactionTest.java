/*
 * Copyright (c) 2002-2022, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.portal.business.file;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Locale;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFileHome;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.sql.TransactionManager;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.transaction.UserTransaction;

/**
 * Integration tests for the transaction behavior of {@link FileHome}.
 *
 * {@code FileHome.create/update/remove} manage their own transaction only when they are the top-level owner. When a caller already runs a transaction — a
 * managed one (any {@code ITransactionSynchronizationManager}) or a Lutece one ({@code TransactionManager}) on the {@code portal} pool — the file operations
 * must join it and never commit on their own, so that a rollback of the caller also discards the file rows.
 *
 * The Lutece enclosing case and the standalone case are exercised here against a real database. The managed (CDI/JTA) case mirrors the same delegation but
 * requires an active JTA transaction to exercise its detection branch, so it belongs to a JTA-enabled integration environment.
 */
public class FileHomeTransactionTest extends LuteceTestCase
{
    /**
     * A caller opens a Lutece transaction on the {@code portal} pool (as {@code FormService.removeForm} does through {@code LuteceTransactionManager}) and rolls
     * it back. The file created in between must be gone : {@code FileHome.create} must have joined the caller's transaction rather than committing on its own.
     */
    @Test
    public void testCreateJoinsLuteceTransactionAndRollsBack( )
    {
        int nIdFile;
        int nIdPhysicalFile;

        TransactionManager.beginTransaction( null );

        try
        {
            nIdFile = FileHome.create( newFileWithContent( ) );
            nIdPhysicalFile = FileHome.findByPrimaryKey( nIdFile ).getPhysicalFile( ).getIdPhysicalFile( );

            assertNotNull( FileHome.findByPrimaryKey( nIdFile ), "The file must be visible inside the enclosing transaction" );
        }
        catch( RuntimeException e )
        {
            TransactionManager.rollBack( null, e );
            throw e;
        }

        // The caller aborts.
        TransactionManager.rollBack( null );

        assertNull( FileHome.findByPrimaryKey( nIdFile ), "The file row must be rolled back with the caller's Lutece transaction" );
        assertNull( PhysicalFileHome.findByPrimaryKey( nIdPhysicalFile ), "The physical file row must be rolled back too" );
    }

    /**
     * Managed (CDI/JTA) equivalent of the case that proved the regression under Spring in the v7 line : a caller runs under a JTA transaction (as a
     * container-managed {@code @Transactional} does, e.g. workflow-upload), so {@code FileHome.create} must join it through the
     * {@code ITransactionSynchronizationManager} branch of the guard, and the caller's rollback must discard the file rows.
     *
     * Disabled : the unit-test harness has no JTA provider, so no managed transaction can be started ({@code UserTransaction} cannot be resolved). This case
     * belongs to a JTA-enabled integration environment. The delegation logic it would exercise is identical to the Lutece case above.
     */
    @Test
    @Disabled( "requires an active JTA transaction : no JTA provider in the unit-test harness" )
    public void testCreateJoinsManagedTransactionAndRollsBack( ) throws Exception
    {
        UserTransaction userTransaction = CDI.current( ).select( UserTransaction.class ).get( );

        int nIdFile;
        int nIdPhysicalFile;

        userTransaction.begin( );

        try
        {
            nIdFile = FileHome.create( newFileWithContent( ) );
            nIdPhysicalFile = FileHome.findByPrimaryKey( nIdFile ).getPhysicalFile( ).getIdPhysicalFile( );

            assertNotNull( FileHome.findByPrimaryKey( nIdFile ), "The file must be visible inside the enclosing managed transaction" );
        }
        catch( RuntimeException e )
        {
            userTransaction.rollback( );
            throw e;
        }

        // The caller aborts.
        userTransaction.rollback( );

        assertNull( FileHome.findByPrimaryKey( nIdFile ), "The file row must be rolled back with the caller's managed transaction" );
        assertNull( PhysicalFileHome.findByPrimaryKey( nIdPhysicalFile ), "The physical file row must be rolled back too" );
    }

    /**
     * Control : with no enclosing transaction, {@code FileHome} owns its transaction and commits, so the file is persisted (and can be removed afterwards). This
     * guards against a regression where the top-level detection would wrongly delegate and never commit.
     */
    @Test
    public void testCreateStandaloneCommits( )
    {
        int nIdFile = FileHome.create( newFileWithContent( ) );

        try
        {
            assertNotNull( FileHome.findByPrimaryKey( nIdFile ), "Without an enclosing transaction the file must be committed and retrievable" );
        }
        finally
        {
            FileHome.remove( nIdFile );
        }

        assertNull( FileHome.findByPrimaryKey( nIdFile ), "The file must be removed after cleanup" );
    }

    /**
     * Builds a valid file carrying its content. The physical file identifier is left unset on purpose : {@code FileHome.create} generates it.
     *
     * @return a file with a non-null physical file content
     */
    private File newFileWithContent( )
    {
        File file = new File( );
        file.setTitle( "filehome-transaction-test" );
        file.setDateCreation( DateUtil.formatTimestamp( "1/1/2000", Locale.FRANCE ) );
        file.setExtension( "txt" );
        file.setMimeType( "text/plain" );
        file.setSize( 12 );

        PhysicalFile physicalFile = new PhysicalFile( );
        physicalFile.setValue( "some content".getBytes( ) );
        file.setPhysicalFile( physicalFile );

        return file;
    }
}
