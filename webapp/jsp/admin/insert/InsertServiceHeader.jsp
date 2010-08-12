<%@ page errorPage="../ErrorPage.jsp" %>
<%@ page import="fr.paris.lutece.portal.service.util.AppPathService" %>
<%@ page import="fr.paris.lutece.portal.service.i18n.I18nService" %>
<%@ page import="fr.paris.lutece.portal.service.admin.AdminUserService" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">


<html xmlns="http://www.w3.org/1999/xhtml" lang="fr" xml:lang="fr" class="insert-service">

<head>
    <title><%= I18nService.getLocalizedString( "portal.insert.insertService.windowTitle" , AdminUserService.getLocale( request )) %></title>
    <base href="<%= AppPathService.getBaseUrl( request ) %>" />

    <%
        response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
        response.setHeader("Pragma","no-cache"); //HTTP 1.0
        response.setDateHeader("Expires", 0); //prevents caching at the proxy server
    %>
    <link rel="stylesheet" type="text/css" href="css/portal_admin.css" title="lutece_admin" />

</head>
<body class="insert-service">
<div id="wrapper">
<input type="button" class="button-right" onclick="javascript:history.go(-1)" value="Retour">
