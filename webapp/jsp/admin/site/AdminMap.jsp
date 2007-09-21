<%@ page errorPage="../ErrorPage.jsp" %>
<%@ page import="java.util.Enumeration" %>

<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="adminMap" scope="session" class="fr.paris.lutece.portal.web.admin.AdminMapJspBean" />


<% adminMap.init( request , adminMap.RIGHT_MANAGE_ADMIN_SITE ); %>
 
   <div id="admin-site">
           <%= adminMap.getMap( request ) %>

    <div id="admin-site-preview" class="preview">
  
<%
	String strParams = "";
	String strSeparator = "?";
	Enumeration paramNames = request.getParameterNames();
	int i = 0;
	while( paramNames.hasMoreElements() )
	{
            if( i != 0 )
            {
                strSeparator = "&";
            }
            String strParamName = (String) paramNames.nextElement();
            String strParamValue = request.getParameter( strParamName );
            strParams += strSeparator + strParamName + "=" + strParamValue;
	}
%>
	<iframe name="preview" src="jsp/admin/site/AdminPagePreview.jsp<%= strParams %>" width="100%" height="750">
	</iframe> 
    </div>
</div>


<%@ include file="../AdminFooter.jsp"%>
