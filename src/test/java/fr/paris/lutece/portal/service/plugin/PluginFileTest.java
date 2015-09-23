/*
 * PluginFileTest.java
 * JUnit based test
 *
 * Created on 7 mai 2006, 19:55
 */
package fr.paris.lutece.portal.service.plugin;

import fr.paris.lutece.portal.service.includes.PageIncludeEntry;
import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.test.LuteceTestCase;

import junit.framework.*;

import java.util.List;


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

        //TODO path en dur...
        String strFilename = getResourcesDir(  ) + "../test-classes/plugin-test.xml";
        instance.load( strFilename );

        assertNotNull( instance.getParams(  ) );
        assertEquals( AppPropertiesService.getProperty( "lutece.encoding" ),
            instance.getParams(  ).get( "test_properties" ) );

        List<PageIncludeEntry> includes = instance.getPageIncludes(  );
        assertEquals( 3, includes.size(  ) );

        for ( PageIncludeEntry anInclude : includes )
        {
            if ( anInclude.getId(  ).contains( "disabled" ) )
            {
                assertFalse( anInclude.isEnabled(  ) );
            }
            else
            {
                assertTrue( anInclude.isEnabled(  ) );
            }
        }
    }
}
