package fr.paris.lutece.portal.web.admin;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.user.AdminUser;
import jakarta.servlet.http.HttpServletRequest;

public class AdminUserUtils
{

    private static final String ATTRIBUTE_ADMIN_USER = "lutece_admin_user";
    /**
     * Register an admin user with a given right
     * @param request the request to register the user into
     * @param user The user
     * @param strRight The right
     */
    public static void registerAdminUserWithRigth( HttpServletRequest request, AdminUser user, String strRight )
    {
        Map<String, Right> mapRights = new HashMap<String, Right>( );
        Right right = new Right( );
        right.setId( strRight );
        mapRights.put( strRight, right );
        user.setRights( mapRights );

        // TODO set locale user
        user.setLocale( new Locale( "fr", "FR", "" ) );
        registerAdminUser( request, user );
    }
    
    /**
     * Register an admin user
     * @param request the request to register the user into
     * @param user The user
     */
    public static void registerAdminUser( HttpServletRequest request, AdminUser user )
    {
        request.getSession( true ).setAttribute( ATTRIBUTE_ADMIN_USER, user );
    }
    
}
