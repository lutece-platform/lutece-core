package fr.paris.lutece.portal.service.security;

import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;


/**
 * Service to access user management functionalities. This class provide
 * 
 */
public final class LuteceUserService
{
    /**
     * Private constructor
     */
    private LuteceUserService( )
    {
        // Do nothing
    }

    /**
     * Get the LuteceUser with the given name
     * @param strName The name of the LuteceUser to get
     * @return The LuteceUser, or null if no LuteceUser has the given name
     */
    public static LuteceUser getLuteceUserFromName( String strName )
    {
        //TODO : add separation between user management implementations
        LuteceUser user = (LuteceUser) LuteceUserCacheService.getInstance( ).getFromCache( strName );
        if ( user != null )
        {
            try
            {
                return (LuteceUser) user.clone( );
            }
            catch ( CloneNotSupportedException e )
            {
                AppLogService.error( e.getMessage( ), e );
            }
        }
        for ( ILuteceUserProviderService luteceUserProviderService : SpringContextService
                .getBeansOfType( ILuteceUserProviderService.class ) )
        {
            user = luteceUserProviderService.getLuteceUserFromName( strName );
            if ( user != null )
            {
                if ( luteceUserProviderService.canUsersBeCached( ) )
                {
                    try
                    {
                        LuteceUserCacheService.getInstance( ).putInCache( strName, user.clone( ) );
                    }
                    catch ( CloneNotSupportedException e )
                    {
                        AppLogService.error( e.getMessage( ), e );
                    }
                }
                return user;
            }
        }
        return null;
    }

    /**
     * Declares that a user was modified. This method must be used every time a
     * user is updated to keep the cache up to date. Note that if the user name
     * of a user is changed, this method does not need to be called
     * @param strUserName The name of the updated user.
     */
    public static void userAttributesChanged( String strUserName )
    {
        LuteceUserCacheService.getInstance( ).removeKey( strUserName );
    }
}
