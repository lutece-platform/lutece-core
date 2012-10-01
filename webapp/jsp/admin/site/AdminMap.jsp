<%@ page errorPage="../ErrorPage.jsp" %>
<%@ page import="java.util.Enumeration" %>

<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="adminMap" scope="session" class="fr.paris.lutece.portal.web.admin.AdminMapJspBean" />


<% adminMap.init( request , adminMap.RIGHT_MANAGE_ADMIN_SITE ); %>
 
<%= adminMap.getMap( request ) %>

  
<%
	String strParams = "";
	String strSeparator = "?";
	Enumeration paramNames = request.getParameterNames();
	int i = 0;
	while( paramNames.hasMoreElements() )
	{
            if( i != 0 )
            {
                strSeparator = "&#38;";
            }
            String strParamName = (String) paramNames.nextElement();
            String strParamValue = request.getParameter( strParamName );
            strParams += strSeparator + strParamName + "=" + strParamValue;

     }   
%>

<%@ include file="../AdminFooter.jsp"%>
