package fr.paris.lutece.portal.service.security;

import fr.paris.lutece.portal.service.cache.AbstractCacheableService;


/**
 * Cache service for LuteceUserService
 */
public final class LuteceUserCacheService extends AbstractCacheableService
{
    private static final String CACHE_SERVICE_NAME = "LuteceUserCacheService";

    private static volatile LuteceUserCacheService _instance;

    /**
     * Private constructor
     */
    private LuteceUserCacheService( )
    {
        initCache( );
    }

    /**
     * Get the instance of the cache service
     * @return The instance of the cache service
     */
    public static LuteceUserCacheService getInstance( )
    {
        if ( _instance == null )
        {
            synchronized ( LuteceUserCacheService.class )
            {
                // Ignore double null check error : attribute is volatile
                if ( _instance == null )
                {
                    _instance = new LuteceUserCacheService( );
                }
            }
        }
        return _instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName( )
    {
        return CACHE_SERVICE_NAME;
    }

}
