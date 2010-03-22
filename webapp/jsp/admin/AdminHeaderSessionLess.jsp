<%@ page errorPage="ErrorPage.jsp" %>
<%@ page import="fr.paris.lutece.portal.service.util.AppPathService" %>
<%@ page pageEncoding="UTF-8" %>

<%
	// Calcul de l'url de base
	String strBase = AppPathService.getBaseUrl( request ) ;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>LUTECE - Administration</title>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<base href="<%= strBase %>"></base>
	<%
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
	%>
	<link rel="stylesheet" type="text/css" href="css/portal_admin.css" title="lutece_admin" />
	
	<!--[if IE 6]>
		<link rel="stylesheet" type="text/css" href="css/ie/portal_admin_ie6.css" 						title="lutece_admin_ie6" />
	<![endif]-->

	<!--[if IE 7]>
		<link rel="stylesheet" type="text/css" href="css/ie/portal_admin_ie7.css" 			title="lutece_admin_ie7" />
	<![endif]-->

	
</head>
<body>