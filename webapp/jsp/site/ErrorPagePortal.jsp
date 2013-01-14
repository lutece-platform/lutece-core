<%@ page isErrorPage="true" %>
<%@ page import="fr.paris.lutece.portal.service.util.*" %>
<%@page import="fr.paris.lutece.portal.service.i18n.I18nService" %>
<%!
	private final static String PROPERTY_DEBUG = "error.page.debug";
	private final static String PROPERTY_DEBUG_DEFAULT = "true";
	private final static String PROPERTY_MESSAGE = "portal.util.error.page.message";
	private final static String PROPERTY_CONTACT_ADMIN = "portal.util.error.page.message.contact_admin";
	private final static String PROPERTY_MESSAGE_SORRY = "portal.util.error.page.message.sorry";
	private final static String PROPERTY_MESSAGE_ERROR = "portal.util.error.page.message_error";
	private final static String PROPERTY_MESSAGE_STACK = "portal.util.error.page.message.stack_trace";
	private final static String PROPERTY_HOME = "portal.site.page_home.label";
	private final static String PROPERTY_SITE_MAP = "portal.site.site_map.pathLabel";
    private final static String PROPERTY_XPAGE_CONTACT = "portal.site.page_menu_tools.xpage.contact";
	private final static String PROPERTY_CREDITS = "portal.site.portal_footer.labelCredits";
	private final static String PROPERTY_WINDOW = "portal.site.portal_footer.newWindow";
            
%>
<html>
<head>
<jsp:include page="PortalHeader.jsp" />
<base href="<%= AppPathService.getBaseUrl( request ) %>/" />
<!-- Set the viewport width to device width for mobile -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Le styles -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="css/page_template_styles.css" type="text/css" media="screen, projection" />
<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<link href="css/bootstrap-responsive.min.css" rel="stylesheet">
</head>
<body>
 <!-- header -->
<header role="banner">
<div class="navbar navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <a class="brand" href="jsp/site/Portal.jsp"><img src="images/logo-header.png" title="Home" alt="Home" /></a>
        </div>
    </div>
</div>
</header>
<!-- end header -->
<div class="container">
<!-- container -->
	<div class="row">
		<div class="span12">
			<br>
			<br>
			<br>
			<div class="alert alert-error">
				<br>
				<br>
				<%
				if( AppPropertiesService.getProperty( PROPERTY_DEBUG , PROPERTY_DEBUG_DEFAULT ).equalsIgnoreCase( "true" )){
				%>
				<p class="lead">
					<i class="icon-user"></i>&nbsp;
					<%= I18nService.getLocalizedString(PROPERTY_MESSAGE_SORRY, request.getLocale() ) %>
				</p>
				<p>
					<%= I18nService.getLocalizedString(PROPERTY_CONTACT_ADMIN, request.getLocale() ) %>
				</p>
				<%
				String strErrorMessage = (exception.getMessage() != null ) ? exception.getMessage() : exception.toString();
				%>
				<p class="lead"><%= I18nService.getLocalizedString(PROPERTY_MESSAGE_ERROR, request.getLocale() ) %> <%= strErrorMessage %></p>
				<p id="stack_msg">
					<a class="btn btn-danger" id="btnStack">
						<%= I18nService.getLocalizedString(PROPERTY_MESSAGE_STACK, request.getLocale() ) %>
					</a>
				</p>
				<pre id="stack">
				<%
					java.io.CharArrayWriter cw = new java.io.CharArrayWriter();
					java.io.PrintWriter pw = new java.io.PrintWriter(cw,true);
					exception.printStackTrace(pw);
					out.println(cw.toString());
				%>
				</pre>
				<%  
					AppLogService.error( exception.getMessage(  ), exception ); 
					} else 	{ 
				%>
				<p class="lead">
					<i class="icon-warning-sign icon-white"></i>&nbsp;<%= I18nService.getLocalizedString(PROPERTY_MESSAGE, request.getLocale() ) %>
				</p>
				<p>
					<a href="jsp/site/Portal.jsp?page=contact" class="btn btn-danger">
						<i class="icon-user icon-white"></i>&nbsp;Accéder à la page contact 
					</a>
					<a href="jsp/site/Portal.jsp" class="btn btn-info">
						<i class="icon-home icon-white"></i>&nbsp;Accéder à l'accueil
					</a>
				</p>
				<%  
					}
				%>				
				<br>
				<br>
				<!-- container -->
			</div>
		</div>
	</div>
<footer>
<div class="navbar navbar-fixed-bottom navbar-inverse">
    <div class="navbar-inner">
        <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
		  &nbsp;
        </a>
        <a class="brand" href="http://fr.lutece.paris.fr" title="#i18n{portal.site.portal_footer.labelPortal}">
			<img src="images/poweredby.jpg" width="55" height="22" alt="#i18n{portal.site.portal_footer.labelMadeBy}" />
		</a>
        <div class="nav-collapse">
		 <ul class="nav">
            <li><a href="jsp/site/Portal.jsp"><%= I18nService.getLocalizedString(PROPERTY_HOME, request.getLocale() ) %></a></li>
            <li><a href="jsp/site/Portal.jsp?page=map" accesskey="3" ><%= I18nService.getLocalizedString(PROPERTY_SITE_MAP, request.getLocale() ) %></a></li>
            <li class="active"><a href="jsp/site/Portal.jsp?page=contact" accesskey="7" ><%= I18nService.getLocalizedString(PROPERTY_XPAGE_CONTACT, request.getLocale() ) %></a></li>
            <li><a href="jsp/site/PopupCredits.jsp" title="[<%= I18nService.getLocalizedString(PROPERTY_WINDOW, request.getLocale() ) %>] <%= I18nService.getLocalizedString(PROPERTY_CREDITS, request.getLocale() ) %>" target="info_comp"><%= I18nService.getLocalizedString(PROPERTY_CREDITS, request.getLocale() ) %></a></li>
          </ul>
		<ul class="nav pull-right">
            <li><a id="footer-rss" href="http://fr.lutece.paris.fr/fr/plugins/rss/lutece.xml" title="RSS">
				<img src="images/feed.png" alt="RSS" title="RSS" />
			</a>
			</li>
           </ul>
        </div><!-- /.nav-collapse -->
    </div><!-- /navbar-inner -->
  </div>
</footer>
<!-- Le javascript
    ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="js/jquery/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script>
$(document).ready(function(e){
	$('#stack').toggle();
	$("#btnStack").click( function(e){
		$('#stack').toggle();
	});
});
</script>
</body>
</html>
