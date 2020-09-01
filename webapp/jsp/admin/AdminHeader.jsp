<%@ page import="fr.paris.lutece.portal.service.util.AppPathService" %>
<%@ page import="fr.paris.lutece.portal.service.portal.PortalService" %>
<%@ page import="fr.paris.lutece.portal.service.admin.AdminThemeService" %>
<%@ page import="fr.paris.lutece.portal.service.admin.AdminUserService" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page buffer="1024kb" %>
<%@ page autoFlush="false" %>
<%
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", 0);
%>
<jsp:useBean id="adminMenu" scope="session" class="fr.paris.lutece.portal.web.admin.AdminMenuJspBean" />
<!DOCTYPE html>
<html lang="<%= AdminUserService.getAdminUser( request ).getLocale().getLanguage() %>" >
<head>
<base href="<%= AppPathService.getBaseUrl( request ) %>">
<title><%= PortalService.getSiteName(  ) %> - Administration</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" >
<meta http-equiv="X-UA-Compatible" content="IE=edge" >
<!-- Set the viewport width to device width for mobile -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<!-- Styles -->
<%= adminMenu.getAdminStyleSheets() %>
<%
	if ( AdminThemeService.isModeAccessible( request ) ){
%>
    <link rel="stylesheet" type="text/css" href="css/admin/portal_admin_accessibility.css" media="screen, projection" >
<%
	}
%>
<!-- Le fav and touch icons -->
<link rel="shortcut icon" href="<%= AppPathService.getBaseUrl( request ) %>favicon.ico">
<%-- Display the admin menu --%>
<%= adminMenu.getAdminMenuHeader( request ) %>
<%-- LUTECE-2310 Remove jQuery Lib - and move /head to adminHeader macro --%>