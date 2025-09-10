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

    private final static String PROPERTY_ADMIN_HOME_LABEL = "portal.util.error.page.buttonAdminHomeLabel";
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
	private final static String PROPERTY_LABELMADEBY = "portal.site.portal_footer.labelMadeBy";
	private final static String PROPERTY_LABELPORTAL = "portal.site.portal_footer.labelPortal";

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
<script>
if( document.getElementById('navbar-menu') != null ){
	document.getElementById('navbar-menu').remove()
	document.getElementById('main-nav').remove()
	document.documentElement.classList.remove('loading');
} else {
	document.body.classList.add('loaded');
}
</script>
<div class="container">
<!-- container -->
	<div class="row">
		<div class="col">
			<div class="card mt-3" style="height: 80vh;">
				<div class="card-body d-flex align-items-center justify-content-center h-100">
					<div class="alert alert-danger" role="alert">
                      <div class="alert-icon">
                        <!-- Download SVG icon from http://tabler.io/icons/icon/alert-circle -->
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="icon alert-icon icon-2">
                          <path d="M3 12a9 9 0 1 0 18 0a9 9 0 0 0 -18 0"></path>
                          <path d="M12 8v4"></path>
                          <path d="M12 16h.01"></path>
                        </svg>
                      </div>
                      <div>
                        <h3 class="alert-heading">Internal error</h4>
                        <div class="alert-description">
                        <%
							if ( exception instanceof AppException )
							{
								//AppException calls AppLogService.error( message, this ) in the
								//constructor, so don't call it here again Call toString to have
								//the Class and the message to be able to identify the correct
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
							<p class="fw-bold fs-3">
								<%
								String strErrorMessage = (exception.getMessage() != null ) ? exception.getMessage() : exception.toString();
								%>
							</p>
							<details>
  								<summary>
								<%= I18nService.getLocalizedString(PROPERTY_DETAIL, request.getLocale() ) %>
								</summary>
								<pre>
									<code>
<%
java.io.CharArrayWriter cw = new java.io.CharArrayWriter();
java.io.PrintWriter pw = new java.io.PrintWriter(cw,true);
exception.printStackTrace(pw);
out.println(cw.toString());
%>
									</code>
								</pre>
								<p class="text-center">
									<a class="btn btn-outline-primary" href="jsp/site/Portal.jsp">
										<%= I18nService.getLocalizedString(PROPERTY_HOME, request.getLocale() ) %>
									</a>
								</p>
							</details>
							<%
								
								} else {
							%>
							<p><%= I18nService.getLocalizedString(PROPERTY_MESSAGE, request.getLocale() ) %></p>
							<p class="text-center">
								<a class="btn btn-outline-primary" href="<%= AppPathService.getAdminMenuUrl() %>">
									<%= I18nService.getLocalizedString(PROPERTY_ADMIN_HOME_LABEL, request.getLocale() ) %>
								</a>
							</p>
						<%
							}
						} // end else access denied
						%>
                        </div>
                      </div>
                    </div>
				</div>
			</div>
		</div>
	</div>
</div>
<!--  BEGIN FOOTER  -->
<footer class="footer footer-transparent d-print-none">
    <div class="container-xl">
        <div class="row text-center align-items-center flex-row-reverse">
            <div class="col-lg-auto ms-lg-auto">
            <ul class="list-inline list-inline-dots mb-0">
                <li class="list-inline-item"><a href="https://lutece.paris.fr/support/jsp/site/Portal.jsp?page=wiki" class="nav-link">Documentation</a></li>
                <li class="list-inline-item"><a href="https://github.com/lutece-platform/" target="_blank" class="link-secondary" rel="noopener">Source code</a></li>
            </ul>
            </div>
            <div class="col-12 col-lg-auto mt-3 mt-lg-0">
            <ul class="list-inline list-inline-dots mb-0">
                <li class="list-inline-item">
                    <a class="nav-link d-flex align-items-center" href="https://lutece.paris.fr" target="lutece" title="<%= I18nService.getLocalizedString(PROPERTY_LABELPORTAL, request.getLocale() ) %>">
                        <span class="me-2"></span>
                        <img src="themes/admin/shared/images/poweredby.svg" style="height:15px" class="img-fluid theme-invert" alt="<%= I18nService.getLocalizedString(PROPERTY_LABELMADEBY, request.getLocale() ) %>">
                        <span class="visually-hidden">LUTECE</span>
                        <!-- <span class="text-muted ms-2" rel="noopener">Version</span> -->
                    </a>
                </li>
            </ul>
            </div>
        </div>
    </div>
</footer>
<!--  END FOOTER  -->
<!-- Included JS Files -->
<!-- Le javascript
    ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script>
document.addEventListener('DOMContentLoaded', function() {
	
	const stackMsg = document.getElementById('stackMsg');
	const btnStack = document.getElementById('btnStack');
	
	// Initially hide the stack trace
	stackMsg.style.display = 'none';
	
	// Toggle stack trace visibility on button click
	btnStack.addEventListener('click', function(e) {
		e.preventDefault();
		if (stackMsg.style.display === 'none') {
			stackMsg.style.display = 'block';
		} else {
			stackMsg.style.display = 'none';
		}
	});
	
});
</script>
</body>
</html>