package fr.paris.lutece.portal.web.download;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.service.download.AbstractFileDownloadProvider;
import fr.paris.lutece.portal.service.download.IFileDownloadProvider;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.PortalJspBean;

public abstract class AbstractDownloadServlet extends HttpServlet
{
    private static final long serialVersionUID = 6622358100579620819L;
    private static final String MESSAGE_UNKNOWN_PROVIDER = "portal.file.download.provider.unknown";
    private static final String MESSAGE_UNKNOWN_FILE = "portal.file.download.file.unknown";
    
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        File file = null;
        try
        {
            IFileDownloadProvider provider = AbstractFileDownloadProvider.findProvider( request.getParameter( AbstractFileDownloadProvider.PARAM_PROVIDER ) );
            if ( provider == null )
            {
                SiteMessageService.setMessage( request, MESSAGE_UNKNOWN_PROVIDER );
            }
            file = provider.getFile( getUser( request ), request, isFromBo( ) );
            if ( file == null || file.getPhysicalFile( ) == null )
            {
                SiteMessageService.setMessage( request, MESSAGE_UNKNOWN_FILE );
            }
        }
        catch( SiteMessageException e )
        {
            response.sendRedirect( AppPathService.getSiteMessageUrl( request ) );
        }
        catch( UserNotSignedException e )
        {
            response.sendRedirect( response.encodeRedirectURL( PortalJspBean.redirectLogin( request ) ) );
        }
        
        if ( file != null )
        {
            try ( OutputStream outputStream = response.getOutputStream( ) )
            {
                response.setContentType( file.getMimeType( ) );
                response.setHeader( "Content-Disposition", "attachment; filename=\"" + file.getTitle( ) + "\";" );
                outputStream.write( file.getPhysicalFile( ).getValue( ) );
            }
        }
    }
    
    protected abstract User getUser( HttpServletRequest request );
    
    protected abstract boolean isFromBo( );
}
