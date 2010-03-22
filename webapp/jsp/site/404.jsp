<jsp:include page="PortalHeader.jsp" />
<%@page import="fr.paris.lutece.portal.service.util.AppPathService"%>

<%@page import="fr.paris.lutece.portal.service.i18n.I18nService"%><html>
<head>
	<title><%= I18nService.getLocalizedString("portal.util.error404.pageTitle", request.getLocale(  ) ) %></title>
	<base href="<%= AppPathService.getBaseUrl( request ) %>" />
	<link rel="stylesheet" href="css/blueprint/screen.css" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="css/blueprint/print.css" type="text/css" media="print" />
	<!--[if IE]><link rel="stylesheet" href="css/blueprint/ie.css" type="text/css" media="screen, projection" /><![endif]-->
	<link rel="stylesheet" href="css/commons.css" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="css/page_template_styles.css" type="text/css" media="screen, projection" />
</head>
<body>
<div class="container">
<div id="banner" class="span-24 append-bottom last -lutece-border-radius-top">
	<div id="logo" class="span-4 prepend-1 prepend-top">
		<a href="jsp/site/Portal.jsp"><img class="png" src="images/logo.png" title="Home" alt="Home"/></a>
	</div>
</div>
<div id="page" class="span-24 last">
<div id="one-column-col1" class="span-22 prepend-1 append-1 last"><div class="portlet -lutece-border-radius append-bottom"><div class="portlet-content"><div class="-lutece-center -lutece-800px-width">&#160;
<div class="portlet-header"><%= I18nService.getLocalizedString("portal.util.error404.title", request.getLocale(  ) ) %></div>
<div class="portlet-content">
<p>
<%= I18nService.getLocalizedString("portal.util.error404.link", request.getLocale(  ) ) %> <a href="jsp/site/Portal.jsp"><%= I18nService.getLocalizedString("portal.util.error404.link2", request.getLocale(  ) ) %></a>.
</p>
</div>
</div></div>
</div>

</div>
</div>
</div>
</body>
</html>