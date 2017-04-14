<%@ page errorPage="ErrorPage.jsp" %>

<%@ page import="fr.paris.lutece.portal.web.LocalVariables" %>
<%@page import="fr.paris.lutece.portal.service.message.SiteMessageException"%>
<jsp:useBean id="portal" scope="session" class="fr.paris.lutece.portal.web.PortalJspBean"/>



<%
	// Required by JSR168 portlets (added in v1.2)
	LocalVariables.setLocal( config, request, response );

	try
	{
		String strContent = portal.getContent( request , 1 );
		out.print( strContent );
		out.flush();
	}
	catch (SiteMessageException lsme)
	{
		response.sendRedirect( request.getRequestURI(  ) );
	}
	finally
	{
		LocalVariables.setLocal( null, null, null );
	}
%>
