package fr.paris.lutece.portal.web.download;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.portal.service.security.SecurityService;

public class DownloadServletFo extends AbstractDownloadServlet
{

    private static final long serialVersionUID = 8953542750378204565L;

    @Override
    protected User getUser( HttpServletRequest request )
    {
        if ( !SecurityService.isAuthenticationEnable( ) )
        {
            return null;
        }
        return SecurityService.getInstance( ).getRegisteredUser( request );
    }

    @Override
    protected boolean isFromBo( )
    {
        return false;
    }

}
