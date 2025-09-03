<%@ page errorPage="ErrorPage.jsp" %>
<%@ page import="fr.paris.lutece.portal.service.util.AppPathService" %>
<%@ page import="fr.paris.lutece.portal.web.l10n.LocaleService" %>

<!DOCTYPE html>
<html lang="<%= LocaleService.getDefault().getLanguage() %>" dir="ltr" class="loading">
<head>
<base href="<%= AppPathService.getBaseUrl( request ) %>">
<title>Administration</title>
<meta charset="utf-8" />
<!-- Set the viewport width to device width for mobile -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="shortcut icon" href="favicon.ico">
</head>
<body class="portlet">
<div id="wrapper">
<section class="vh-100">