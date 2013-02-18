<%@page import="fr.paris.lutece.portal.web.pluginaction.DefaultPluginActionResult"%>
<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="appUser" scope="session" class="fr.paris.lutece.portal.web.user.AdminUserJspBean" />

<%
	appUser.init( request, "CORE_USERS_MANAGEMENT" ) ;
	DefaultPluginActionResult result = appUser.doExportUsers( request, response );  
	if( result != null && result.getRedirect( ) != null && !"".equals( result.getRedirect( ) ) && ( result.getHtmlContent( ) == null || "".equals( result.getHtmlContent( ) ) ) )
	{
	    response.sendRedirect( result.getRedirect( ) );
	}
	else if ( result != null && result.getHtmlContent( ) != null && "".equals( result.getHtmlContent( ) ) )
	{
%>
<jsp:include page="../AdminHeader.jsp"  flush="true" />

<%= result.getHtmlContent( ) %>

<%@ include file="../AdminFooter.jsp" %>
<%
	}
%>