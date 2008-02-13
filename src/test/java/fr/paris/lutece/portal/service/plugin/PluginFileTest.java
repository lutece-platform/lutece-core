/*
 * PluginFileTest.java
 * JUnit based test
 *
 * Created on 7 mai 2006, 19:55
 */
package fr.paris.lutece.portal.service.plugin;

import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.test.LuteceTestCase;

import junit.framework.*;


/**
 *
 * @author Pierre
 */
public class PluginFileTest extends LuteceTestCase
{
    public PluginFileTest( String testName )
    {
        super( testName );
    }

    protected void setUp(  ) throws Exception
    {
    }

    protected void tearDown(  ) throws Exception
    {
    }

    public static Test suite(  )
    {
        TestSuite suite = new TestSuite( PluginFileTest.class );

        return suite;
    }

    /**
     * Test of getName method, of class fr.paris.lutece.portal.service.PluginFile.
     */
    public void testLoad(  ) throws LuteceInitException
    {
        System.out.println( "load" );

        PluginFile instance = new PluginFile(  );

        //TODO phath en dur...
        String strFilename = getResourcesDir(  ) + "../test-classes/plugin-test.xml";
        instance.load( strFilename );
    }
}
