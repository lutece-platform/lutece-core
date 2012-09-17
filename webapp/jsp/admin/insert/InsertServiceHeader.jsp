<%@ page errorPage="../ErrorPage.jsp" %>
<%@ page import="fr.paris.lutece.portal.service.util.AppPathService" %>
<%@ page import="fr.paris.lutece.portal.service.i18n.I18nService" %>
<%@ page import="fr.paris.lutece.portal.service.admin.AdminUserService" %>
<!DOCTYPE html>
<html lang="fr">
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
<link href="css/admin/bootstrap.min.css" rel="stylesheet">
<!-- style type="text/css">
body {padding-top: 60px;padding-bottom: 40px;}
.sidebar-nav {padding: 9px 0; }
</style -->
<link href="css/admin/bootstrap-responsive.min.css" rel="stylesheet">
<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<!-- Le fav and touch icons -->
<link rel="shortcut icon" href="favicon.ico">
<!-- 
<link rel="apple-touch-icon-precomposed" sizes="144x144" href="apple-touch-icon-144-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="114x114" href="apple-touch-icon-114-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="72x72" href="apple-touch-icon-72-precomposed.png">
<link rel="apple-touch-icon-precomposed" href="apple-touch-icon-57-precomposed.png">
-->
<link rel="stylesheet" href="css/admin/portal_admin.css" />
</head>
<body>
<button type="button" class="btn btn-remove-circle pull-right" onclick="javascript:window.history.back();" title="Retour"><i class="icon-remove-circle"></i>&nbsp;Retour</button>
