package fr.paris.lutece.portal.service.cache;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.service.page.PageEvent;
import fr.paris.lutece.portal.service.page.PageService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.test.LuteceTestCase;

public class PathCacheServiceEnabledTest extends LuteceTestCase
{

    private static final String BEAN_PAGE_SERVICE = "pageService";

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
                aService.enableCache( true );
                aService.resetCache( );
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
                aService.resetCache( );
                aService.enableCache( bEnabled );
                break;
            }
        }
        service = null;
        super.tearDown( );
    }

    public void testGetKey( )
    {
        Set<String> keys = new HashSet<>( );
        for ( String xpageName : new String[] { "name", "name2" } )
        {
            for ( int mode : new int[] { 0, 1, 2} )
            {
                String key = service.getKey( xpageName, mode, null );
                checkKey( keys, key );
                MockHttpServletRequest request = new MockHttpServletRequest( );
                request.addPreferredLocale( Locale.FRANCE );
                key = service.getKey( xpageName, mode, request );
                checkKey( keys, key );
                request = new MockHttpServletRequest( );
                request.addPreferredLocale( Locale.ITALY );
                key = service.getKey( xpageName, mode, request );
                checkKey( keys, key );
                request = new MockHttpServletRequest( );
                request.addParameter( Parameters.PAGE_ID, "1" );
                key = service.getKey( xpageName, mode, request );
                checkKey( keys, key );
                request = new MockHttpServletRequest( );
                request.addParameter( Parameters.PAGE_ID, "2" );
                key = service.getKey( xpageName, mode, request );
                checkKey( keys, key );
                request = new MockHttpServletRequest( );
                request.addParameter( Parameters.PORTLET_ID, "1" );
                key = service.getKey( xpageName, mode, request );
                checkKey( keys, key );
                request = new MockHttpServletRequest( );
                request.addParameter( Parameters.PORTLET_ID, "2" );
                key = service.getKey( xpageName, mode, request );
                checkKey( keys, key );
            }
        }
    }

    public void testGetKeyWithTitleUrls( )
    {
        Set<String> keys = new HashSet<>( );
        for ( String xpageName : new String[] { "name", "name2" } )
        {
            for ( int mode : new int[] { 0, 1, 2} )
            {
                for ( String titleUrls : new String[] {null, "title1", "title2" } )
                {
                    String key = service.getKey( xpageName, mode, titleUrls, null );
                    checkKey( keys, key );
                    MockHttpServletRequest request = new MockHttpServletRequest( );
                    request.addPreferredLocale( Locale.FRANCE );
                    key = service.getKey( xpageName, mode, titleUrls, request );
                    checkKey( keys, key );
                    request = new MockHttpServletRequest( );
                    request.addPreferredLocale( Locale.ITALY );
                    key = service.getKey( xpageName, mode, titleUrls, request );
                    checkKey( keys, key );
                    request = new MockHttpServletRequest( );
                    request.addParameter( Parameters.PAGE_ID, "1" );
                    key = service.getKey( xpageName, mode, titleUrls, request );
                    checkKey( keys, key );
                    request = new MockHttpServletRequest( );
                    request.addParameter( Parameters.PAGE_ID, "2" );
                    key = service.getKey( xpageName, mode, titleUrls, request );
                    checkKey( keys, key );
                    request = new MockHttpServletRequest( );
                    request.addParameter( Parameters.PORTLET_ID, "1" );
                    key = service.getKey( xpageName, mode, titleUrls, request );
                    checkKey( keys, key );
                    request = new MockHttpServletRequest( );
                    request.addParameter( Parameters.PORTLET_ID, "2" );
                    key = service.getKey( xpageName, mode, titleUrls, request );
                    checkKey( keys, key );
                }
            }
        }
    }

    public void testPutAndGetFromCache( )
    {
        service.putInCache( null, "junit" );
        String key = service.getKey( "junit", 0, null );
        service.putInCache( key, "junit" );
        assertEquals( "junit", service.getFromCache( key ) );
        assertNull( service.getFromCache( null ) );
        assertNull( service.getFromCache( "NotInCache" ) );
    }

    public void testProcessPageEvent( )
    {
        String key = service.getKey( "junit", 0, null );
        service.putInCache( key, "junit" );
        PageEvent event = new PageEvent( new Page( ), PageEvent.PAGE_CREATED );
        ( ( PathCacheService ) service ).processPageEvent( event );
        assertEquals( "junit", service.getFromCache( key ) );
        for ( int nEventType : new int[] { PageEvent.PAGE_CONTENT_MODIFIED, PageEvent.PAGE_DELETED, PageEvent.PAGE_MOVED, PageEvent.PAGE_STATE_CHANGED } )
        {
            service.putInCache( key, "junit" );
            event = new PageEvent( new Page( ), nEventType );
            ( ( PathCacheService ) service ).processPageEvent( event );
            assertNull( service.getFromCache( key ) );
        }
    }

    public void testRegisteredPageEventListener( )
    {
        String key = service.getKey( "junit", 0, null );
        service.putInCache( key, "junit" );
        PageService pageService = SpringContextService.getBean( BEAN_PAGE_SERVICE );
        Page page = new Page( );
        page.setName( getRandomName( ) );
        page.setDescription( page.getName( ) );
        page.setParentPageId( PortalService.getRootPageId( ) );
        pageService.createPage( page );
        try
        {
            // FIXME : change when LUTECE-1834 Adding or removing a page blows up all caches
            // is fixed
            //assertEquals( "junit", service.getFromCache( key ) );
            assertNull( service.getFromCache( key ) );
            service.putInCache( key, "junit" );
            page.setDescription( page.getName( ) + page.getName( ) );
            pageService.updatePage( page );
            assertNull( service.getFromCache( key ) );
            service.putInCache( key, "junit" );
        } finally {
            pageService.removePage( page.getId( ) );
            assertNull( service.getFromCache( key ) );
        }
    }

    private String getRandomName( )
    {
        Random rand = new SecureRandom( );
        BigInteger bigInt = new BigInteger( 128, rand );
        return "junit" + bigInt.toString( 36 );
    }

    private void checkKey( Set<String> keys, String key )
    {
        assertNotNull( key );
        assertFalse( keys.contains( key ) );
        keys.add( key );
    }
}
