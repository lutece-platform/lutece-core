<%@ page errorPage="ErrorPagePortal.jsp" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="java.lang.StringBuffer" %>
<%@ page import="fr.paris.lutece.portal.web.LocalVariables" %>
<%@page import="fr.paris.lutece.portal.service.page.PageNotFoundException"%>
<%@page import="fr.paris.lutece.portal.service.util.AppPathService"%>
<%@page import="fr.paris.lutece.portal.web.PortalJspBean"%>

<%@ page pageEncoding="UTF-8" %>

<jsp:include page="PortalHeader.jsp" />

<jsp:useBean id="portal" scope="page" class="fr.paris.lutece.portal.web.PortalJspBean" />

<%
	// Required by JSR168 portlets (added in v1.2)
	LocalVariables.setLocal( config, request, response );


	try
	{
		String strContent = portal.getSiteMessageContent(request);
		if(strContent ==  null)
		{
			response.sendRedirect(AppPathService.getBaseUrl(request) + AppPathService.getPortalUrl() );
			
		}
		out.print( strContent );
		out.flush();
	}
	catch ( PageNotFoundException pnfe )
	{
		response.sendError(response.SC_NOT_FOUND);
	}
	finally
	{
		LocalVariables.setLocal( null, null, null );
	}
%>
