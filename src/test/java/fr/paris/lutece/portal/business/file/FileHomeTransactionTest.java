/*
 * Copyright (c) 2002-2025, City of Paris
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

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFileHome;
import fr.paris.lutece.portal.service.database.AppConnectionService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.sql.TransactionManager;

/**
 * Integration tests for the transaction behavior of {@link FileHome}.
 *
 * {@code FileHome.create/update/remove} manage their own transaction only when they are the top-level owner. When a caller already runs a transaction — a
 * Spring-managed one or a Lutece one on the {@code portal} pool — the file operations must join it and never commit on their own, so that a rollback of the
 * caller also discards the file rows. These tests prove exactly that : without the guard, {@code FileHome} would commit the file rows on a separate connection
 * and they would survive the caller's rollback.
 */
public class FileHomeTransactionTest extends LuteceTestCase
{
    /**
     * A caller opens a Spring transaction (as {@code UploadHistoryService.create} does with {@code workflow.transactionManager}, a
     * {@code DataSourceTransactionManager}) and rolls it back. The file created in between must be gone : {@code FileHome.create} must have joined the Spring
     * transaction rather than committing on its own Lutece transaction.
     */
    public void testCreateJoinsSpringTransactionAndRollsBack( ) throws UnsupportedEncodingException
    {
        DataSource dataSource = AppConnectionService.getPoolManager( ).getDataSource( AppConnectionService.DEFAULT_POOL_NAME );
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager( dataSource );
        TransactionStatus status = transactionManager.getTransaction( new DefaultTransactionDefinition( ) );

        int nIdFile;
        int nIdPhysicalFile;

        try
        {
            assertTrue( "A Spring transaction synchronization must be active", TransactionSynchronizationManager.isSynchronizationActive( ) );

            nIdFile = FileHome.create( newFileWithContent( ) );
            nIdPhysicalFile = FileHome.findByPrimaryKey( nIdFile ).getPhysicalFile( ).getIdPhysicalFile( );

            assertNotNull( "The file must be visible inside the enclosing transaction", FileHome.findByPrimaryKey( nIdFile ) );
        }
        catch( RuntimeException e )
        {
            transactionManager.rollback( status );
            throw e;
        }

        // The caller aborts. FileHome must not have committed the rows on its own connection.
        transactionManager.rollback( status );

        assertNull( "The file row must be rolled back with the caller's Spring transaction", FileHome.findByPrimaryKey( nIdFile ) );
        assertNull( "The physical file row must be rolled back too", PhysicalFileHome.findByPrimaryKey( nIdPhysicalFile ) );
    }

    /**
     * Same scenario with a Lutece transaction (as {@code FormService.removeForm} does with {@code forms.transactionManager}, a {@code LuteceTransactionManager}
     * on the {@code portal} pool). The rollback of the caller must also discard the file rows.
     */
    public void testCreateJoinsLuteceTransactionAndRollsBack( ) throws UnsupportedEncodingException
    {
        int nIdFile;
        int nIdPhysicalFile;

        TransactionManager.beginTransaction( null );

        try
        {
            nIdFile = FileHome.create( newFileWithContent( ) );
            nIdPhysicalFile = FileHome.findByPrimaryKey( nIdFile ).getPhysicalFile( ).getIdPhysicalFile( );

            assertNotNull( "The file must be visible inside the enclosing transaction", FileHome.findByPrimaryKey( nIdFile ) );
        }
        catch( RuntimeException e )
        {
            TransactionManager.rollBack( null, e );
            throw e;
        }

        // The caller aborts.
        TransactionManager.rollBack( null );

        assertNull( "The file row must be rolled back with the caller's Lutece transaction", FileHome.findByPrimaryKey( nIdFile ) );
        assertNull( "The physical file row must be rolled back too", PhysicalFileHome.findByPrimaryKey( nIdPhysicalFile ) );
    }

    /**
     * Control : with no enclosing transaction, {@code FileHome} owns its transaction and commits, so the file is persisted (and can be removed afterwards). This
     * guards against a regression where the top-level detection would wrongly delegate and never commit.
     */
    public void testCreateStandaloneCommits( ) throws UnsupportedEncodingException
    {
        int nIdFile = FileHome.create( newFileWithContent( ) );

        try
        {
            assertNotNull( "Without an enclosing transaction the file must be committed and retrievable", FileHome.findByPrimaryKey( nIdFile ) );
        }
        finally
        {
            FileHome.remove( nIdFile );
        }

        assertNull( "The file must be removed after cleanup", FileHome.findByPrimaryKey( nIdFile ) );
    }

    /**
     * Builds a valid file carrying its content. The physical file identifier is left unset on purpose : {@code FileHome.create} generates it.
     *
     * @return a file with a non-null physical file content
     */
    private File newFileWithContent( ) throws UnsupportedEncodingException
    {
        File file = new File( );
        file.setTitle( "filehome-transaction-test" );
        file.setDateCreation( DateUtil.formatTimestamp( "1/1/2000", Locale.FRANCE ) );
        file.setExtension( "txt" );
        file.setMimeType( "text/plain" );
        file.setSize( 12 );

        PhysicalFile physicalFile = new PhysicalFile( );
        physicalFile.setValue( "some content".getBytes( "UTF-8" ) );
        file.setPhysicalFile( physicalFile );

        return file;
    }
}
