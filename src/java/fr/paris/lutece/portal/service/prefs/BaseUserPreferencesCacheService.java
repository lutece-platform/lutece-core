package fr.paris.lutece.portal.service.prefs;

import fr.paris.lutece.portal.service.cache.AbstractCacheableService;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * Cache service for {@link BaseUserPreferencesServiceImpl}
 */
public class BaseUserPreferencesCacheService extends AbstractCacheableService
{
    private static final String CACHE_SERVICE_NAME = "BaseUserPreferencesCacheService";

    private static final String CONSTANT_UNDERSCORE = "_";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName( )
    {
        return CACHE_SERVICE_NAME;
    }

    /**
     * Get the key in the cache of a preference of a user
     * @param strUserId The id of the user
     * @param strKey The preference key
     * @return The key in the cache of the preference of the user
     */
    public String getCacheKey( String strUserId, String strKey )
    {
        return strUserId + CONSTANT_UNDERSCORE + strKey;
    }

    /**
     * Remove every values stored in cache for a given user
     * @param strUserId The user id to remove from cache
     */
    public void removeCacheValuesOfUser( String strUserId )
    {
        if ( StringUtils.isNotEmpty( strUserId ) )
        {
            String strPrefix = strUserId + CONSTANT_UNDERSCORE;
            List<String> listKeysToRemove = new ArrayList<String>( );
            for ( String strKey : getKeys( ) )
            {
                if ( strKey.startsWith( strPrefix ) )
                {
                    listKeysToRemove.add( strKey );
                }
            }
            for ( String strKey : listKeysToRemove )
            {
                removeKey( strKey );
            }
        }
    }
}
