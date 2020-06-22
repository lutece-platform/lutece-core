<%@page import="fr.paris.lutece.util.sql.TransactionManager"%>
<%@page import="fr.paris.lutece.portal.service.admin.PasswordResetException"%>
<%@ page isErrorPage="true" %>
<%@ page import="fr.paris.lutece.portal.web.constants.Messages" %>
<%@ page import="fr.paris.lutece.portal.service.util.*" %>
<%@ page import="fr.paris.lutece.portal.service.message.AdminMessageService" %>
<%@ page import="fr.paris.lutece.portal.service.message.AdminMessage" %>
<%@ page import="fr.paris.lutece.portal.service.i18n.I18nService" %>



<%@ page buffer="1024kb"%>
<%@ page autoFlush="false"%>

<%!
	private final static String JSP_URL_MODIFY_DEFAULT_USER_PASSWORD = "jsp/admin/user/ModifyDefaultUserPassword.jsp";

	private final static String PROPERTY_DEBUG = "error.page.debug";
	private final static String PROPERTY_DEBUG_DEFAULT = "true";
	private final static String PROPERTY_MESSAGE = "portal.util.error.page.message";
	private final static String PROPERTY_DETAIL = "portal.util.error.page.message.details";
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

<%

	TransactionManager.rollBackEveryTransaction( exception );
    if( exception instanceof fr.paris.lutece.portal.service.admin.AccessDeniedException )
    {
    	if ( exception.getMessage(  ) != null )
    	{
    		AppLogService.error( "AccessDeniedException : " + exception.getMessage() );
    	}
        response.sendRedirect( AdminMessageService.getMessageUrl( request , Messages.USER_ACCESS_DENIED , AdminMessage.TYPE_STOP ) );
    }
    else if ( exception instanceof fr.paris.lutece.portal.service.admin.PasswordResetException )
    {
        String strRedirectUrl = AdminMessageService.getMessageUrl( request,
                Messages.MESSAGE_USER_MUST_CHANGE_PASSWORD, JSP_URL_MODIFY_DEFAULT_USER_PASSWORD,
                AdminMessage.TYPE_ERROR );
				response.sendRedirect( strRedirectUrl );
        //response.sendRedirect( AppPathService.getBaseUrl( request ) + JSP_URL_MODIFY_DEFAULT_USER_PASSWORD );
    }
    else
    {
%>

<!DOCTYPE html>
<html>
<head>
<base href="<%= AppPathService.getBaseUrl( request ) %>/" />
<!-- Set the viewport width to device width for mobile -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Le styles -->
<link href="css/admin/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="css/admin/portal_admin.css" />
<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<link href="css/admin/bootstrap-responsive.min.css" rel="stylesheet">
</head>
<body>
<header>
    <div class="navbar navbar-inverse navbar-fixed-top">
        <div class="navbar-inner">
            <a href="jsp/site/Portal.jsp" title="#i18n{portal.users.admin_header.title.viewSite} ${site_name}" target="_blank" class="brand">
                &nbsp;<img class="logo" src="images/logo-header.png" title="#i18n{portal.users.admin_header.title.viewSite} ${site_name}"  alt="${site_name}" />
                
            </a>
            
        </div> <!-- /navbar-inner -->
    </div>
    <h1 class="header"><a name="top">&nbsp;</a></h1>
</header>
<div class="container-fluid">
<!-- container -->
	<div class="row-fluid">
		<div class="span12">
			<br>
			<br>
			<br>
			<div class="alert alert-error">
			<h3>Internal error</h3>
			<%
			if ( exception instanceof AppException )
			{
			    //AppException calls AppLogService.error( message, this ) in the
			    //constructor, so don't call it here again Call toString to have
			    //the Class and the message to be able to indentify the correct
			    //stacktrace in the preceding logs.
			    AppLogService.error( "Error 500 : Caused by previous Critical AppException" );
			}
			else
			{
			    AppLogService.error( exception.getMessage(  ), exception );
			}
			if( AppPropertiesService.getProperty( PROPERTY_DEBUG , PROPERTY_DEBUG_DEFAULT ).equalsIgnoreCase( "true" ))
			{
			%>
			<p class="lead">
				<i class="icon-ban-circle icon-white"></i>&nbsp;
				<%
				String strErrorMessage = (exception.getMessage() != null ) ? exception.getMessage() : exception.toString();
				%>
			</p>
			<p>
				<a class="btn btn-danger" id="btnStack">
				<%= I18nService.getLocalizedString(PROPERTY_DETAIL, request.getLocale() ) %>
				</a>
			</p>
			<pre id="stackMsg">
				<%
				java.io.CharArrayWriter cw = new java.io.CharArrayWriter();
				java.io.PrintWriter pw = new java.io.PrintWriter(cw,true);
				exception.printStackTrace(pw);
				out.println(cw.toString());
				%>
			</pre>
			<p>
				<a class="btn btn-danger" href="jsp/site/Portal.jsp">
					<i class="icon-home icon-white"></i>&nbsp;Accueil du site
				</a>
			</p>
			<%
				
				} else {
			%>
			<p>
				<i class="icon-warning-sign"></i>
				&nbsp;<%= I18nService.getLocalizedString(PROPERTY_MESSAGE, request.getLocale() ) %>
			</p>
			<p>
				<a class="btn btn-danger" href="<%= AppPathService.getAdminMenuUrl() %>">
					<i class="icon-home icon-white"></i>&nbsp;Accueil de l'administration du site
				</a>
			</p>
			<%
				}
				} // end else access denied
			%>
			</div>
		</div>
	</div>
<!-- end container -->
</div>
<!-- footer menu -->
<footer>
<div id="footer" class="navbar navbar-fixed-bottom  hidden-phone">
	<div class="pull-right">
		<a href="http://fr.lutece.paris.fr" target="lutece" title="#i18n{portal.site.portal_footer.labelPortal}">
			<img src="images/poweredby.png" alt="#i18n{portal.site.portal_footer.labelMadeBy}" />
			<small>version ${version}</small>
		</a>
	</div>
</div>
</footer>
<!-- Included JS Files -->
<!-- Le javascript
    ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="js/jquery/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script>
$(document).ready(function(e){
	$('#stackMsg').toggle();
	$("#btnStack").click( function(e){
		$('#stackMsg').toggle();
	});
});
</script>

</body>
</html>