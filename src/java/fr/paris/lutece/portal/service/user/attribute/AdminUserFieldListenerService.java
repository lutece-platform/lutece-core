package fr.paris.lutece.portal.service.user.attribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.attribute.AdminUserFieldListener;

/**
 * 
 * AdminUserFieldListenerService
 *
 */
public class AdminUserFieldListenerService 
{
	private List<AdminUserFieldListener> _listRegisteredListeners = new ArrayList<AdminUserFieldListener>(  );
	
    /**
     * Register a new Removal listener
     * @param listener The listener to register
     */
    public void registerListener( AdminUserFieldListener listener )
    {
        _listRegisteredListeners.add( listener );
    }
    
    /**
     * Create user fields
     * @param user AdminUser
     * @param request HttpServletRequest
     * @param locale locale
     */
    public void doCreateUserFields( AdminUser user, HttpServletRequest request, Locale locale )
    {
    	for ( AdminUserFieldListener listener : _listRegisteredListeners )
    	{
    		listener.doCreateUserFields( user, request, locale );
    	}
    }
    
    /**
     * Modify user fields
     * @param user AdminUser
     * @param request HttpServletRequest
     * @param locale locale
     * @param currentUser current user
     */
    public void doModifyUserFields( AdminUser user, HttpServletRequest request, Locale locale, AdminUser currentUser )
    {
    	for ( AdminUserFieldListener listener : _listRegisteredListeners )
    	{
    		listener.doModifyUserFields( user, request, locale, currentUser );
    	}
    }
    
    /**
     * Remove user fields
     * @param user Adminuser
	 * @param request HttpServletRequest
	 * @param locale locale
     */
    public void doRemoveUserFields( AdminUser user, HttpServletRequest request, Locale locale )
    {
    	for ( AdminUserFieldListener listener : _listRegisteredListeners )
    	{
    		listener.doRemoveUserFields( user, request, locale );
    	}
    }
}
