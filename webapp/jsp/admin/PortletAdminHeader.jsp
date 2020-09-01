<%@ page errorPage="ErrorPage.jsp" %>
<%@ page import="fr.paris.lutece.portal.service.util.AppPathService" %>
<%@ page import="fr.paris.lutece.portal.web.l10n.LocaleService" %>

<!DOCTYPE html>
<html lang="<%= LocaleService.getDefault().getLanguage() %>">
<head>
<base href="<%= AppPathService.getBaseUrl( request ) %>">
<title>Administration</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<!-- Set the viewport width to device width for mobile -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Le styles -->
<link href="css/admin/bootstrap.min.css" rel="stylesheet">
<link href="css/admin/bootstrap-theme.min.css" rel="stylesheet">
<link href="css/admin/font-awesome.min.css"  rel="stylesheet" >
<link href="css/admin/ionicons.min.css"  rel="stylesheet" >
<link href="css/admin/AdminLTE.css" rel="stylesheet" >
<link href="css/admin/portal_admin.css" rel="stylesheet" >
<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
<script src="https://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
<link rel="shortcut icon" href="favicon.ico">
<link rel="stylesheet" href="css/admin/portal_admin.css">
<script src="js/admin/jquery/jquery-3.5.1.min.js"></script>
<style>body{padding-top:0;}</style>
</head>
<body class="portlet">
<div id="wrapper">
<div id="admin-wrapper">
<section>
