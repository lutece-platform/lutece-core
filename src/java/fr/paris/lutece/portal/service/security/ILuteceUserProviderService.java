package fr.paris.lutece.portal.service.security;

/**
 * Interface for service that provide LuteceUser. There should be an
 * implementation of this interface for each implementation of user management.
 * Implementations are retrieved by Spring and thus must be declared as beans.
 */
public interface ILuteceUserProviderService
{
    /**
     * Get the instance of a lutece user from its name
     * @param strName The name of the user to get
     * @return The LuteceUser, or null if no user has the given name
     */
    LuteceUser getLuteceUserFromName( String strName );

    /**
     * Check if this service allow users it provide to be stored in cache
     * @return True if users can be stored in cache, false otherwise
     */
    boolean canUsersBeCached( );
}
