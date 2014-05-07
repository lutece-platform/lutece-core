<%@ page import="fr.paris.lutece.portal.service.util.AppPathService" %>
<%@ page import="fr.paris.lutece.portal.service.portal.PortalService" %>
<%@ page import="fr.paris.lutece.portal.service.admin.AdminThemeService" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page buffer="1024kb" %>
<%@ page autoFlush="false" %>
<jsp:useBean id="adminMenu" scope="session" class="fr.paris.lutece.portal.web.admin.AdminMenuJspBean" />
<!DOCTYPE html>
<html lang="fr">
<head>
<base href="<%= AppPathService.getBaseUrl( request ) %>"></base>
<title><%= PortalService.getSiteName(  ) %> - Administration</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" >
<meta http-equiv="Expires" content="Mon, 26 Jul 1997 05:00:00 GMT" >
<meta http-equiv="Pragma" content="no-cache" > 
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" >
<meta http-equiv="Cache-Control" content="post-check=0, pre-check=0" >
<meta http-equiv="X-UA-Compatible" content="IE=edge" >
<!-- Set the viewport width to device width for mobile -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Le styles -->
<link href="css/admin/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/admin/print_admin.css" media="print" >
<link href="css/admin/colorpicker.css" rel="stylesheet">
<link href="css/admin/datepicker.css" rel="stylesheet" >
<link rel="stylesheet" href="js/jquery/plugins/ui/css/jquery-ui-1.10.0.custom.css" >
<link href="css/admin/font-awesome.min.css"  rel="stylesheet" >
<link href="css/admin/portal_admin.css" rel="stylesheet" >
<link href="css/bootstrap2-compatibility.css" rel="stylesheet">
<link href="css/admin/bootstrap-theme.min.css" rel="stylesheet">
<%
	if ( AdminThemeService.isModeAccessible( request ) ){
%>
<link rel="stylesheet" type="text/css" href="css/admin/portal_admin_accessibility.css" media="screen, projection" >
<%
	}
%>
<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
<link rel="stylesheet" href="js/jquery/plugins/ui/css/jquery.ui.1.10.0.ie.css" >
<script src="https://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<!-- Le fav and touch icons -->
<link rel="shortcut icon" href="<%= AppPathService.getBaseUrl( request ) %>favicon.ico">
<!-- 
<link rel="apple-touch-icon-precomposed" sizes="144x144" href="apple-touch-icon-144-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="114x114" href="apple-touch-icon-114-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="72x72" href="apple-touch-icon-72-precomposed.png">
<link rel="apple-touch-icon-precomposed" href="apple-touch-icon-57-precomposed.png">
-->
<script src="js/jquery/jquery.min.js"></script>
</head>
<body>
<%-- Display the admin menu --%>
<%= adminMenu.getAdminMenuHeader( request ) %>