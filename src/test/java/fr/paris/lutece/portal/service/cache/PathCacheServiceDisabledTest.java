package fr.paris.lutece.portal.service.cache;

import java.util.List;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.service.page.PageEvent;
import fr.paris.lutece.test.LuteceTestCase;

public class PathCacheServiceDisabledTest extends LuteceTestCase
{
    IPathCacheService service;
    boolean bEnabled;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        service = null;
        List<CacheableService> serviceList = CacheService.getCacheableServicesList( );
        for ( CacheableService aService : serviceList )
        {
            if ( aService instanceof IPathCacheService )
            {
                service = ( IPathCacheService ) aService;
                bEnabled = aService.isCacheEnable( );
                aService.enableCache( false );
                break;
            }
        }
        assertNotNull( service );
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        List<CacheableService> serviceList = CacheService.getCacheableServicesList( );
        for ( CacheableService aService : serviceList )
        {
            if ( aService == service )
            {
                aService.enableCache( bEnabled );
                break;
            }
        }
        service = null;
        super.tearDown( );
    }

    public void testGetKey( )
    {
        assertNull( service.getKey( "junit", 0, new MockHttpServletRequest( ) ) );
        assertNull( service.getKey( "junit", 0, "junit", new MockHttpServletRequest( ) ) );
    }

    public void testPutAndGetFromCache( )
    {
        service.putInCache( null, "junit" );
        String key = service.getKey( "junit", 0, null );
        service.putInCache( key, "junit" );
        assertNull( service.getFromCache( key ) );
        assertNull( service.getFromCache( null ) );
        assertNull( service.getFromCache( "NotInCache" ) );
    }

    public void testProcessPageEvent( )
    {
        String key = service.getKey( "junit", 0, null );
        for ( int nEventType : new int[] { PageEvent.PAGE_CONTENT_MODIFIED, PageEvent.PAGE_CREATED, PageEvent.PAGE_DELETED, PageEvent.PAGE_MOVED, PageEvent.PAGE_STATE_CHANGED } )
        {
            service.putInCache( key, "junit" );
            PageEvent event = new PageEvent( new Page( ), nEventType );
            ( ( PathCacheService ) service ).processPageEvent( event );
            assertNull( service.getFromCache( key ) );
        }
    }

}
