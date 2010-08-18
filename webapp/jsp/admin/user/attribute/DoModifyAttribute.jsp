<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="attribute" scope="session" class="fr.paris.lutece.portal.web.user.attribute.AttributeJspBean" />

<%
	attribute.init( request, "CORE_USERS_MANAGEMENT" ) ; 
	response.sendRedirect( attribute.doModifyAttribute( request ) );  
%>

