<%@ page errorPage="ErrorPage.jsp" %>
<%@ page import="fr.paris.lutece.portal.service.util.AppPathService" %>
<%@ page import="fr.paris.lutece.portal.service.portal.PortalService" %>
<%@ page import="fr.paris.lutece.portal.web.l10n.LocaleService" %>
<%@ page pageEncoding="UTF-8" %>
<%	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
<%
	// Calcul de l'url de base
	String strBase = AppPathService.getBaseUrl( request ) ;
%>
<!DOCTYPE html>
<html lang="<%= LocaleService.getDefault().getLanguage() %>" dir="ltr">
<head>
<base href="<%= strBase %>">
<title><%= PortalService.getSiteName(  ) %> - Administration</title>
<meta charset="utf-8" />
<!-- Set the viewport width to device width for mobile -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Le styles -->
<%= PortalService.getAdminCssLinks() %>
<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
<script src="https://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
<!-- Le fav and touch icons -->
<link rel="shortcut icon" href="<%= strBase %>favicon.ico">
<!-- Apply the user's stored theme mode synchronously, before first paint, so logout/message pages keep the admin theme -->
<script>
// The logout Clear-Site-Data header wipes localStorage : re-seed the non-sensitive UI preferences carried
// through the redirect as query params (whitelisted), so the user's theme/read direction survive logout.
const urlParams = new URLSearchParams( window.location.search );
const paramTheme = urlParams.get('lutece-tabler-theme');
if( paramTheme === 'dark' || paramTheme === 'light' ){
	localStorage.setItem('lutece-tabler-theme', paramTheme);
}
if( urlParams.get('lutece-bo-readmode') === 'rtl' ){
	localStorage.setItem('lutece-bo-readmode', 'rtl');
}
const localTheme = localStorage.getItem('lutece-tabler-theme');
if( localTheme !== null ){
	document.documentElement.dataset.bsTheme = localTheme;
}
// Read direction : apply the user's stored value on <html> before first paint (persists over login/logout)
if( localStorage.getItem('lutece-bo-readmode') === 'rtl' ){
	document.documentElement.setAttribute('dir','rtl');
}
</script>