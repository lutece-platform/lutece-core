<%@ page errorPage="ErrorPagePortal.jsp" %><%@
    page import="fr.paris.lutece.portal.service.security.UserNotSignedException,
                 fr.paris.lutece.portal.service.security.SecurityService,
                 java.util.Enumeration,
                 java.lang.StringBuffer,
                 fr.paris.lutece.portal.web.LocalVariables,
                 fr.paris.lutece.portal.service.message.SiteMessageException,
                 fr.paris.lutece.portal.service.page.PageNotFoundException,
                 fr.paris.lutece.portal.service.util.AppPathService,
                 fr.paris.lutece.portal.web.PortalJspBean"%><%@
    page pageEncoding="UTF-8"
%><jsp:include page="PortalHeader.jsp"
/><jsp:useBean id="portal" scope="page" class="fr.paris.lutece.portal.web.PortalJspBean" /><%

	// Required by JSR168 portlets (added in v1.2)
	LocalVariables.setLocal( config, request, response );


	try
	{
		String strContent = portal.getContent( request );

		out.print( strContent );
		out.flush();
	}
	catch ( PageNotFoundException pnfe )
	{
		response.sendError(response.SC_NOT_FOUND);
	}
	catch( SiteMessageException lme )
	{
		response.sendRedirect( AppPathService.getSiteMessageUrl( request ) );
	}
	catch( UserNotSignedException e )
	{
		// This exception means that a content service needs the
		// user to be authenticated (Added in v1.1)
		// The user should be redirected to the Lutece login page

		if( SecurityService.getInstance(  ).isExternalAuthentication(  ) && 
				!SecurityService.getInstance(  ).isMultiAuthenticationSupported(  ) )
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
	finally
        {
            LocalVariables.setLocal( null, null, null );
        }

%>