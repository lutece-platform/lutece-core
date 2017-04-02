package fr.paris.lutece.portal.web.includes;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginEvent;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.test.LuteceTestCase;

public class LinksIncludeCacheServiceTest extends LuteceTestCase
{
    private LinksIncludeCacheService service;
    private boolean bOrigCacheStatus;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        service = SpringContextService.getBean( LinksIncludeCacheService.SERVICE_NAME );
        bOrigCacheStatus = service.isCacheEnable( );
        service.enableCache( true );
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        service.enableCache( bOrigCacheStatus );
        super.tearDown( );
    }

    public void testGetCacheKey( )
    {
        Set<String> keys = new HashSet<>( );
        for ( int nMode : new int[ ] { 0, 1 } )
        {
            for ( String strPage : new String[ ] { null, "test", "test2" } )
            {
                for ( Locale locale : new Locale[ ] { null, Locale.FRANCE, Locale.FRENCH } )
                {
                    String key = service.getCacheKey( nMode, strPage, locale );
                    assertTrue( "key " + key + " was already generated", keys.add( key ) );
                }
            }
        }
    }

    public void testProcessPluginEvent( )
    {
        service.putInCache( "key", "value" );
        assertEquals( "value", service.getFromCache( "key" ) );
        service.processPluginEvent( null );
        assertNull( service.getFromCache( "key" ) );
        for ( Plugin plugin : new Plugin[ ] { null, PluginService.getCore( ) } )
        {
            for ( int eventType : new int[ ] { PluginEvent.PLUGIN_INSTALLED, PluginEvent.PLUGIN_POOL_CHANGED,
                    PluginEvent.PLUGIN_UNINSTALLED } )
            {
                service.putInCache( "key", "value" );
                assertEquals( "value", service.getFromCache( "key" ) );
                PluginEvent event = new PluginEvent( plugin, eventType );
                service.processPluginEvent( event );
                assertNull( service.getFromCache( "key" ) );
            }
        }
    }

}
