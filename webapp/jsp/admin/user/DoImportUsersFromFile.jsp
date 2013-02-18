<%@page import="fr.paris.lutece.portal.web.pluginaction.DefaultPluginActionResult"%>
<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="appUser" scope="session" class="fr.paris.lutece.portal.web.user.AdminUserJspBean" />

<%
	appUser.init( request, "CORE_USERS_MANAGEMENT" ) ;
	DefaultPluginActionResult result = appUser.doImportUsersFromFile( request );  
	if( result.getHtmlContent( ) == null || "".equals( result.getHtmlContent( ) ) )
	{
	    response.sendRedirect( result.getRedirect( ) );
	}
%>
<jsp:include page="../AdminHeader.jsp"  flush="true" />

<%= result.getHtmlContent( ) %>

<%@ include file="../AdminFooter.jsp" %>
