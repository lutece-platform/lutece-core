package fr.paris.lutece.portal.web.xpages;

import fr.paris.lutece.portal.service.cache.AbstractCacheableService;


/**
 * SiteMapCacheService
 */
public class SiteMapCacheService extends AbstractCacheableService
{
    private static final String SERVICE_NAME = "SiteMapService";

    private static SiteMapCacheService _instance = new SiteMapCacheService( );

    /**
     * Private constructor
     */
    private SiteMapCacheService( )
    {
        initCache( );
    }

    /**
     * Get the instance of the cache service
     * @return The instance of the cache service
     */
    public static SiteMapCacheService getInstance( )
    {
        return _instance;
    }

    /**
     * Returns the service name
     * @return The service name
     */
    @Override
    public String getName( )
    {
        return SERVICE_NAME;
    }

}
