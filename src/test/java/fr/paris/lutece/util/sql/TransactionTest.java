/*
 * Copyright (c) 2002-2017, Mairie de Paris
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
package fr.paris.lutece.util.sql;

import fr.paris.lutece.test.LuteceTestCase;

import java.sql.SQLException;

/**
 * Transaction Test
 */
public class TransactionTest extends LuteceTestCase
{
    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS test_transaction";
    private static final String SQL_CREATE_TABLE = "CREATE TABLE test_transaction ( id integer )";
    private static final String SQL_INSERT = "INSERT INTO test_transaction VALUES ( ? )";

    public void testCommit( )
    {
        System.out.println( "commit" );

        Transaction transaction = new Transaction( );

        try
        {
            transaction.prepareStatement( SQL_DROP_TABLE );
            transaction.executeStatement( );
            transaction.prepareStatement( SQL_CREATE_TABLE );
            transaction.executeStatement( );

            for ( int i = 0; i < 3; i++ )
            {
                transaction.prepareStatement( SQL_INSERT );
                transaction.getStatement( ).setInt( 1, i );
                transaction.executeStatement( );
            }

            transaction.commit( );
        }
        catch( SQLException ex )
        {
            transaction.rollback( ex );
        }

        assertTrue( transaction.getStatus( ) == Transaction.COMMITTED );
    }

    public void testRollback( )
    {
        System.out.println( "rollback" );

        Transaction transaction = new Transaction( );

        try
        {
            for ( int i = 3; i < 6; i++ )
            {
                transaction.prepareStatement( SQL_INSERT );
                transaction.getStatement( ).setInt( 1, i );
                transaction.executeStatement( );
            }

            transaction.rollback( );
        }
        catch( SQLException ex )
        {
            transaction.rollback( ex );
        }

        assertTrue( transaction.getStatus( ) == Transaction.ROLLEDBACK );
    }
}
