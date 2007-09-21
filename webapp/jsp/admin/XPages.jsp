<%@ page errorPage="ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.service.message.SiteMessageException"%>
<jsp:useBean id="portal" scope="session" class="fr.paris.lutece.portal.web.PortalJspBean"/>



<%
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
%>