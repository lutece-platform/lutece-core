<%@ page isErrorPage="true" %>
<%@ page import="fr.paris.lutece.portal.web.constants.Messages" %>
<%@ page import="fr.paris.lutece.portal.service.util.*" %>
<%@ page import="fr.paris.lutece.portal.service.message.AdminMessageService" %>
<%@ page import="fr.paris.lutece.portal.service.message.AdminMessage" %>

<%@ page buffer="1024kb"%>
<%@ page autoFlush="false"%>

<%!
	private final static String PROPERTY_DEBUG = "error.page.debug";
	private final static String PROPERTY_DEBUG_DEFAULT = "true";
	private final static String PROPERTY_MESSAGE = "error.page.message";
%>

<%
    if( exception instanceof fr.paris.lutece.portal.service.admin.AccessDeniedException )
    {
        response.sendRedirect( AdminMessageService.getMessageUrl( request , Messages.USER_ACCESS_DENIED , AdminMessage.TYPE_STOP ) );
    }
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
  <table border="0" width="200" cellpadding="5" cellspacing="5" >
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

<!-- 		
		<form action="mailto:thibaut.lassalle@paris.fr" name="bugForm">
			<input type="hidden" name="stack" />
			<input type="submit" value="Report the bug" />
		</form>
		<script>
			document.forms["bugForm"].stack.value = document.getElementById("stackMsg").innerHTML;
		</script>
 -->
 		
		 </td>
        </tr>

<%
	}
	else
	{
%>
		<tr>
         <td align="center" width="100%">
			<img src="images/admin/skin/messages/warning.png" />
        </td>
		</tr>
		<tr>
		 <td>
		   <%= AppPropertiesService.getProperty( PROPERTY_MESSAGE ) %>
         </td>
        </tr>
<%
	}
%>
    </table>
</div>
</div>
</td>
</tr>
</table>
</center>
