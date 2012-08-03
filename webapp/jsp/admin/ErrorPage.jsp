<%@page import="fr.paris.lutece.portal.service.admin.PasswordResetException"%>
<%@ page isErrorPage="true" %>
<%@ page import="fr.paris.lutece.portal.web.constants.Messages" %>
<%@ page import="fr.paris.lutece.portal.service.util.*" %>
<%@ page import="fr.paris.lutece.portal.service.message.AdminMessageService" %>
<%@ page import="fr.paris.lutece.portal.service.message.AdminMessage" %>
<%@page import="fr.paris.lutece.portal.service.i18n.I18nService" %>

<%@ page buffer="1024kb"%>
<%@ page autoFlush="false"%>

<%!
	private final static String JSP_URL_MODIFY_DEFAULT_USER_PASSWORD = "jsp/admin/user/ModifyDefaultUserPassword.jsp";

	private final static String PROPERTY_DEBUG = "error.page.debug";
	private final static String PROPERTY_DEBUG_DEFAULT = "true";
	private final static String PROPERTY_MESSAGE = "portal.util.error.page.message";
%>

<%
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


<html>
<head>
<base href="<%= AppPathService.getBaseUrl( request ) %>/" />
<link rel="stylesheet"  href="css/page_template_structure.css" type="text/css"  media="screen" />
<link rel="stylesheet"  href="css/page_template_styles.css" type="text/css"  media="screen" />
</head>
<body topmargin="0" leftmargin="0">

<center>
<table width="600">
 <tr>
  <td>
<div class="portlet">
<h3 class="portlet-header">Internal error</h3>
<div class="portlet-content">
  <table border="0" cellpadding="5" cellspacing="5" width="100%" >
<%
	if( AppPropertiesService.getProperty( PROPERTY_DEBUG , PROPERTY_DEBUG_DEFAULT ).equalsIgnoreCase( "true" ))
	{
%>
<%
		String strErrorMessage = (exception.getMessage() != null ) ? exception.getMessage() : exception.toString();
%>
		<tr>
         <td>

		<br />
		<pre id="stackMsg" style="display:none">
		<%
		java.io.CharArrayWriter cw = new java.io.CharArrayWriter();
		java.io.PrintWriter pw = new java.io.PrintWriter(cw,true);
		exception.printStackTrace(pw);
		out.println(cw.toString());
		%>
		</pre>
		<a onclick="document.getElementById('stackMsg').style.display='block'">Get more details.</a>
		 </td>
        </tr>

<%
	AppLogService.error( exception.getMessage(  ), exception );
	}
	else
	{
%>
		<tr>
         <td align="center">
			<img src="images/admin/skin/messages/warning.png" />
        </td>
		</tr>
		<tr>
		 <td>
		   <%= I18nService.getLocalizedString(PROPERTY_MESSAGE, request.getLocale() ) %>
         </td>
        </tr>
<%
	}
	AppLogService.error( exception.getMessage(  ), exception );
%>
    </table>
</div>
</div>
</td>
</tr>
</table>
</center>
<%
	} // end else access denied
%>
