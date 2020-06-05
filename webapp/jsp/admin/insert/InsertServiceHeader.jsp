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
<link href="css/admin/bootstrap.min.css" rel="stylesheet">
<link href="css/admin/bootstrap-theme.min.css" rel="stylesheet">
<link href="css/admin/font-awesome.min.css"  rel="stylesheet" >
<link href="css/admin/print_admin.css" rel="stylesheet" type="text/css" media="print" >
<link href="css/admin/colorpicker.css" rel="stylesheet">
<link href="css/admin/datepicker.css" rel="stylesheet" >
<link href="css/admin/portal_admin.css" rel="stylesheet" >
<link href="js/jquery/plugins/ui/css/jquery-ui-1.10.0.custom.css" rel="stylesheet" >
<style>body{padding-top:0;}</style>
<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
<link rel="stylesheet" href="js/jquery/plugins/ui/css/jquery.ui.1.10.0.ie.css" >
<script src="https://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
<link rel="shortcut icon" href="favicon.ico">
<script src="js/jquery/jquery.min.js"></script>
</head>
<body>
<div class="container">
	<div class="row">
		<div class="col-xs-offset-10 col-xs-2 col-md-offset-10 col-md-2"> 
			<button type="button" class="btn btn-default btn-sm" onclick="javascript:window.history.back();" title="<%= I18nService.getLocalizedString( "portal.util.labelBack" , AdminUserService.getLocale( request )) %>">
				<i class="fa fa-arrow-circle-o-left"></i>&nbsp;<%= I18nService.getLocalizedString( "portal.util.labelBack" , AdminUserService.getLocale( request )) %>
			</button>
		</div>
	</div>
