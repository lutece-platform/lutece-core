<%@ page isErrorPage="true" %>
<%@ page import="fr.paris.lutece.portal.service.util.*" %>
<%!
	private final static String PROPERTY_DEBUG = "error.page.debug";
	private final static String PROPERTY_DEBUG_DEFAULT = "true";
	private final static String PROPERTY_MESSAGE = "error.page.message";
	private final static String PROPERTY_MESSAGE_DEFAULT = "Veuillez contacter l'administrateur de l'application";
%>

<jsp:include page="PortalHeader.jsp" />

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
 <h3 class="portlet-header">Une erreur s'est produite dans l'application</h3>
<table border="0" width="200" cellpadding="5" cellspacing="5" >
<%
	if( AppPropertiesService.getProperty( PROPERTY_DEBUG , PROPERTY_DEBUG_DEFAULT ).equalsIgnoreCase( "true" ))
	{
%>
		<tr>
         <td align="center" width="100%">
			<img src="images/admin/skin/error.png" />
        </td>
		</tr>
		<tr>
			<td align="center">
                            <h3> Le problème a été transmis à l'administrateur technique du Site<br />
                                    Nous vous prions de nous excuser pour cet incident</h3>
			</td>
		</tr>
		<tr>
         <td>
		<img src="images/admin/skin/user.png" />
        </td>
		</tr>
		<tr>
         <td>
<%
		String strErrorMessage = (exception.getMessage() != null ) ? exception.getMessage() : exception.toString();
%>
		Le message d'erreur est : <%= strErrorMessage %>
		 </td>
        </tr>
		<tr>
         <td>
		Le tracé de pile est : <br />
		<PRE><FONT COLOR="#1C2861"><%
		java.io.CharArrayWriter cw = new java.io.CharArrayWriter();
		java.io.PrintWriter pw = new java.io.PrintWriter(cw,true);
		exception.printStackTrace(pw);
		out.println(cw.toString());
		%></FONT></PRE>
		 </td>
        </tr>

<%
	}
	else
	{
%>
		<tr>
         <td align="center" width="100%">
			<img src="images/admin/skin/warning.png" />
        </td>
		</tr>
		<tr>
		 <td>
		   <%= AppPropertiesService.getProperty( PROPERTY_MESSAGE , PROPERTY_MESSAGE_DEFAULT ) %>
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

<%@ include file="PortalFooter.jsp" %>
