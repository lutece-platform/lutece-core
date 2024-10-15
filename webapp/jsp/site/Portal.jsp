<%@ page errorPage="ErrorPagePortal.jsp" %>
<%@ page import="fr.paris.lutece.portal.service.security.UserNotSignedException,
                 fr.paris.lutece.portal.service.security.SecurityService,
                 fr.paris.lutece.portal.web.LocalVariables,
                 fr.paris.lutece.portal.service.message.SiteMessageException,
                 fr.paris.lutece.portal.service.page.PageNotFoundException,
                 fr.paris.lutece.portal.service.util.AppPathService,
                 fr.paris.lutece.portal.web.PortalJspBean,
				 jakarta.el.ELException"%>
<%@ page pageEncoding="UTF-8"%>
<jsp:include page="PortalHeader.jsp"/>

<%
	// Required by JSR168 portlets (added in v1.2)
	LocalVariables.setLocal( config, request, response );

	try
	{
%>		
		${ portalJspBean.getContent( pageContext.request ) }
		${ pageContext.response.flushBuffer( ) }
<%
	}
	catch ( ELException el )
	{
	    if ( PageNotFoundException.class.getCanonicalName(  ).equals( el.getCause( ).getClass( ).getCanonicalName( ) ) )
        {
	        response.sendError( HttpServletResponse.SC_NOT_FOUND );
        } 
	    else if ( SiteMessageException.class.getCanonicalName(  ).equals( el.getCause( ).getClass( ).getCanonicalName( ) ) )
        {
            response.sendRedirect( AppPathService.getSiteMessageUrl( request ) );
        }
	    else if ( UserNotSignedException.class.getCanonicalName(  ).equals( el.getCause( ).getClass( ).getCanonicalName( ) ) )
        {
	     	// This exception means that a content service needs the
	        // user to be authenticated (Added in v1.1)
	        // The user should be redirected to the Lutece login page

	        if ( SecurityService.getInstance( ).isExternalAuthentication( ) &&
	        	!SecurityService.getInstance( ).isMultiAuthenticationSupported( ) )
	        {
%>
				<center>
				<br />
				<br />
				<h3>
				Error : This page requires an authenticated user identified by an external service
				but no user is available.
				</h3>
				</center>
<%
			}
			else
			{
				response.sendRedirect( PortalJspBean.redirectLogin( request ));
			}
        }
        else
        {
    		throw el;
        }
	}
	finally
    {
        LocalVariables.setLocal( null, null, null );
    }
%>
