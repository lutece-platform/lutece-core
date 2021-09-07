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
<link rel="shortcut icon" href="favicon.ico">
</head>
<body class="portlet">
<div id="wrapper">
<section class="vh-100">