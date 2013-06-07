<!DOCTYPE html>
<html lang="fr">
<head>
<jsp:include page="PortalHeader.jsp" />
<%@page import="fr.paris.lutece.portal.service.util.AppPathService"%>
<%@page import="fr.paris.lutece.portal.service.i18n.I18nService"%>
<title><%= I18nService.getLocalizedString("portal.util.error404.pageTitle", request.getLocale(  ) ) %></title>
<base href="<%= AppPathService.getBaseUrl( request ) %>" />
<meta charset="UTF-8" />
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
<meta http-equiv="Content-Language" content="fr" />
<!-- Set the viewport width to device width for mobile -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Le styles -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="css/page_template_styles.css" type="text/css" media="screen, projection" />
<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<link href="css/bootstrap-responsive.min.css" rel="stylesheet">
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
<style>
  .center {text-align: center; margin-left: auto; margin-right: auto; margin-bottom:2em; margin-top: auto;}
</style>
</head>
<body>
<div class="container">
<!-- header -->
<header role="banner">
<div class="navbar navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
        <a class="brand" href="jsp/site/Portal.jsp"><img src="images/logo-header.png" title="Home" alt="Home"/></a>
		<div class="nav-collapse">&nbsp;</div>
        </div>
    </div>
</div>
</header>
<!-- end header -->
<div class="row">
		<div class="span12">
			<div class="hero-unit center">
				<h1>
					<%= I18nService.getLocalizedString("portal.util.error404.pageTitle", request.getLocale(  ) ) %>
					<small><font face="Tahoma" color="red">Error 404</font></small>
				 </h1>
				<br />
				<p>
					<%= I18nService.getLocalizedString("portal.util.error404.title", request.getLocale(  ) ) %>
					<%= I18nService.getLocalizedString("portal.util.error404.text", request.getLocale(  ) ) %>. 
					
				</p>
				<p>
					<strong><%= I18nService.getLocalizedString("portal.util.error404.link", request.getLocale(  ) ) %></strong>
				</p>
				<a href="jsp/site/Portal.jsp" class="btn btn-large btn-info">
					<i class="icon-home icon-white"></i>&nbsp;<%= I18nService.getLocalizedString("portal.util.error404.link2", request.getLocale(  ) ) %>
				</a>
			</div>
		</div>
	</div>
</div>
<!-- footer -->
<footer>
	<div class="navbar navbar-inverse">
		<div class="navbar-inner">
		  <div class="container" style="width: auto;">
			<a class="brand" href="http://fr.lutece.paris.fr" title="<%= I18nService.getLocalizedString("portal.site.portal_footer.labelPortal", request.getLocale(  ) ) %>">
				<img src="images/poweredby.jpg" width="55" height="22" alt="<%= I18nService.getLocalizedString("portal.site.portal_footer.labelMadeBy", request.getLocale(  ) ) %>"/>
			</a>
			
		  </div>
		</div><!-- /navbar-inner -->
	  </div>
</footer>
<!-- end footer -->
<!-- Included JS Files -->
<script src="js/jquery/jquery.min.js"></script>
<!-- Le javascript
    ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="js/bootstrap.min.js"></script>
<script>

</script>
</body>
</html>