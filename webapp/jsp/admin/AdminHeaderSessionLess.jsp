<%@ page errorPage="ErrorPage.jsp" %>
<%@ page import="fr.paris.lutece.portal.service.util.AppPathService" %>
<%@ page import="fr.paris.lutece.portal.service.portal.PortalService" %>
<%@ page pageEncoding="UTF-8" %>

<%
	// Calcul de l'url de base
	String strBase = AppPathService.getBaseUrl( request ) ;
%>
<!DOCTYPE html>
<html lang="fr">
<head>
<base href="<%= strBase %>"></base>
<title><%= PortalService.getSiteName(  ) %> - Administration</title>
<%	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<!-- Set the viewport width to device width for mobile -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Le styles -->
<link href="css/admin/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="css/admin/portal_admin.css" />
<link href="css/admin/bootstrap-responsive.min.css" rel="stylesheet">
<!-- link href="css/admin/datepicker.css" rel="stylesheet" -->
<link rel="stylesheet" href="js/jquery/plugins/ui/css/jquery-ui-1.10.0.custom.css" />
<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
<link rel="stylesheet" href="js/jquery/plugins/ui/css/jquery.ui.1.10.0.ie.css" />
<script src="https://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<!-- Le fav and touch icons -->
<link rel="shortcut icon" href="<%= strBase %>favicon.ico">
<!-- 
<link rel="apple-touch-icon-precomposed" sizes="144x144" href="apple-touch-icon-144-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="114x114" href="apple-touch-icon-114-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="72x72" href="apple-touch-icon-72-precomposed.png">
<link rel="apple-touch-icon-precomposed" href="apple-touch-icon-57-precomposed.png">
-->
<script src="js/jquery/jquery.min.js"></script>
</head>
<body>