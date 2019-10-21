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

import java.security.SecureRandom;
import java.sql.Statement;

import fr.paris.lutece.portal.service.database.AppConnectionService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginDefaultImplementation;
import fr.paris.lutece.test.LuteceTestCase;

public class DAOUtilTest extends LuteceTestCase
{
    // Any table would be ok here
    private static final String SQL_INSERT = " INSERT INTO core_datastore ( entity_key, entity_value ) VALUES ( ? , ? ) ";
    private static final String SQL_DELETE = " DELETE FROM core_datastore where entity_key = ? ";
    private static final String TESTKEY = "daoutiltestkey";
    private static final String TESTVALUE = "daoutiltestvalue";

    public void testDAOUtil_str( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_INSERT );
        doTest( daoUtil, false );
    }

    public void testDAOUtil_str_int( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_INSERT, Statement.RETURN_GENERATED_KEYS );
        doTest( daoUtil, true );
    }

    public void testDAOUtil_str_plugin( )
    {
        Plugin p = new PluginDefaultImplementation( );
        p.setName( "core" ); // DAOUtil.DEFAULT_MODULE_NAME
        p.setConnectionService( AppConnectionService.getDefaultConnectionService( ) );

        DAOUtil daoUtil = new DAOUtil( SQL_INSERT, p );
        doTest( daoUtil, false );
    }

    public void testDAOUtil_str_int_plugin( )
    {
        Plugin p = new PluginDefaultImplementation( );
        p.setName( "core" ); // DAOUtil.DEFAULT_MODULE_NAME
        p.setConnectionService( AppConnectionService.getDefaultConnectionService( ) );

        DAOUtil daoUtil = new DAOUtil( SQL_INSERT, Statement.RETURN_GENERATED_KEYS, p );
        doTest( daoUtil, true );
    }

    private void doTest( DAOUtil daoUtil, boolean hasGeneratedKey )
    {
        String key = TESTKEY + new SecureRandom( ).nextLong( );
        try
        {
            daoUtil.setString( 1, key );
            daoUtil.setString( 2, TESTVALUE );
            daoUtil.executeUpdate( );
            if ( hasGeneratedKey )
            {
                // For now let's just test that there is no generated key
                // we don't have a table in the core that is created
                // with a generated key correctly in the different databases
                // With mysql and hsql, the result set is empty.
                // With postgresql, the result set has all the inserted values.
                assertNotNull( "There should be a generatedkey resultset", daoUtil.getGeneratedKeysResultSet( ) );
            }
        }
        finally
        {
            daoUtil.free( );
        }

        try ( DAOUtil daoUtildelete = new DAOUtil( SQL_DELETE ) )
        {
            daoUtildelete.setString( 1, key );
            daoUtildelete.executeUpdate( );
        }
        catch (Exception e) {
            fail( );
        }
    }

    public void testDAOUtil_str_FAIL_NO_GENERATED_KEYS( )
    {
        String key = TESTKEY + new SecureRandom( ).nextLong( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_INSERT, Statement.NO_GENERATED_KEYS ) )
        {
            daoUtil.setString( 1, key );
            daoUtil.setString( 2, TESTVALUE );
            daoUtil.executeUpdate( );
        }
        catch (Exception e) {
            fail( );
        }

        try ( DAOUtil daoUtildelete = new DAOUtil( SQL_DELETE ) )
        {
            daoUtildelete.setString( 1, key );
            daoUtildelete.executeUpdate( );
        }
        catch (Exception e) {
            fail( );
        }
    }

}
