<%@ page errorPage="../ErrorPage.jsp" %>
<%@ page import="fr.paris.lutece.portal.service.util.AppPathService" %>
<%@ page import="fr.paris.lutece.portal.service.i18n.I18nService" %>
<%@ page import="fr.paris.lutece.portal.service.admin.AdminUserService" %>
<!DOCTYPE html>
<html lang="AdminUserService.getLocale( request ).getLanguage()">
<head>
<title><%= I18nService.getLocalizedString( "portal.insert.insertService.windowTitle" , AdminUserService.getLocale( request )) %></title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<base href="<%= AppPathService.getBaseUrl( request ) %>"></base>
<%
  response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
  response.setHeader("Pragma","no-cache"); //HTTP 1.0
  response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<!-- Set the viewport width to device width for mobile -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Le styles -->
<link href="themes/admin/shared/css/vendor/bootstrap/bootstrap.min.css" rel="stylesheet">
<link href="themes/admin/shared/css/vendor/tabler/tabler-icons.min.css" rel="stylesheet">
<link href="themes/admin/shared/css/portal_admin.min.css" rel="stylesheet" >
<link rel="shortcut icon" href="favicon.ico">
</head>
<body>
<div class="container">