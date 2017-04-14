package fr.paris.lutece.util.sql;

import java.security.SecureRandom;
import java.sql.Statement;

import fr.paris.lutece.portal.service.database.AppConnectionService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginDefaultImplementation;
import fr.paris.lutece.test.LuteceTestCase;

public class DAOUtilTest extends LuteceTestCase
{
    //Any table would be ok here
    private static final String SQL_INSERT = " INSERT INTO core_datastore ( entity_key, entity_value ) VALUES ( ? , ? ) ";
    private static final String SQL_DELETE = " DELETE FROM core_datastore where entity_key = ? ";
    private static final String TESTKEY = "daoutiltestkey" ;
    private static final String TESTVALUE = "daoutiltestvalue" ;

    public void testDAOUtil_str( ) {
        DAOUtil daoUtil = new DAOUtil( SQL_INSERT );
        doTest( daoUtil, false );
    }

    public void testDAOUtil_str_int( ) {
        DAOUtil daoUtil = new DAOUtil( SQL_INSERT, Statement.RETURN_GENERATED_KEYS );
        doTest( daoUtil, true );
    }

    public void testDAOUtil_str_plugin( ) {
        Plugin p = new PluginDefaultImplementation( );
        p.setName( "core" ) ; // DAOUtil.DEFAULT_MODULE_NAME
        p.setConnectionService( AppConnectionService.getDefaultConnectionService( ) );

        DAOUtil daoUtil = new DAOUtil( SQL_INSERT, p );
        doTest( daoUtil, false );
    }

    public void testDAOUtil_str_int_plugin( ) {
        Plugin p = new PluginDefaultImplementation( );
        p.setName( "core" ) ; // DAOUtil.DEFAULT_MODULE_NAME
        p.setConnectionService( AppConnectionService.getDefaultConnectionService( ) );

        DAOUtil daoUtil = new DAOUtil( SQL_INSERT, Statement.RETURN_GENERATED_KEYS, p );
        doTest( daoUtil, true );
    }

    private void doTest( DAOUtil daoUtil, boolean hasGeneratedKey ) {
        String key = TESTKEY + new SecureRandom( ).nextLong( );
        try
        {
            daoUtil.setString( 1, key  );
            daoUtil.setString( 2, TESTVALUE );
            daoUtil.executeUpdate( );
            if ( hasGeneratedKey ) {
                //For now let's just test that there is no generated key
                //we don't have a table in the core that is created
                //with a generated key correctly in the different databases
                //With mysql and hsql, the result set is empty.
                //With postgresql, the result set has all the inserted values.
                assertNotNull( "There should be a generatedkey resultset", daoUtil.getGeneratedKeysResultSet( ) );
            }
        }
        finally
        {
            daoUtil.free( );
        }

        DAOUtil daoUtildelete = new DAOUtil( SQL_DELETE );
        try
        {
            daoUtildelete.setString( 1, key );
            daoUtildelete.executeUpdate( );
        }
        finally
        {
            daoUtildelete.free( );
        }
    }

    public void testDAOUtil_str_FAIL_NO_GENERATED_KEYS( ) {
        String key = TESTKEY + new SecureRandom( ).nextLong( );
        DAOUtil daoUtil = new DAOUtil( SQL_INSERT, Statement.NO_GENERATED_KEYS );
        try
        {
            daoUtil.setString( 1, key );
            daoUtil.setString( 2, TESTVALUE );
            daoUtil.executeUpdate( );
        }
        finally
        {
            daoUtil.free( );
        }

        DAOUtil daoUtildelete = new DAOUtil( SQL_DELETE );
        try
        {
            daoUtildelete.setString( 1, key );
            daoUtildelete.executeUpdate( );
        }
        finally
        {
            daoUtildelete.free( );
        }
    }

}
