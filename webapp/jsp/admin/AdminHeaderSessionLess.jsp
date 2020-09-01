<%@ page errorPage="ErrorPage.jsp" %>
<%@ page import="fr.paris.lutece.portal.service.util.AppPathService" %>
<%@ page import="fr.paris.lutece.portal.service.portal.PortalService" %>
<%@ page import="fr.paris.lutece.portal.web.l10n.LocaleService" %>
<%@ page pageEncoding="UTF-8" %>
<%	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
<%
	// Calcul de l'url de base
	String strBase = AppPathService.getBaseUrl( request ) ;
%>
<!DOCTYPE html>
<html lang="<%= LocaleService.getDefault().getLanguage() %>">
<head>
<base href="<%= strBase %>">
<title><%= PortalService.getSiteName(  ) %> - Administration</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<!-- Set the viewport width to device width for mobile -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Le styles -->
<%= PortalService.getAdminCssLinks() %>
<style>
#login-box{-webkit-box-shadow: 0px 1px 5px 1px rgba(50, 50, 50, 0.75);-moz-box-shadow:0px 1px 5px 1px rgba(50, 50, 50, 0.75);box-shadow:0px 1px 5px 1px rgba(50, 50, 50, 0.75);}
</style>
<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
	<script src="https://html5shim.googlecode.com/svn/trunk/html5.js"></script>
	<script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
<!-- Le fav and touch icons -->
<link rel="shortcut icon" href="<%= strBase %>favicon.ico">