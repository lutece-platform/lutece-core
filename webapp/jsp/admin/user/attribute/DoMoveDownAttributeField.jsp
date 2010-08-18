<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="attributeField" scope="session" class="fr.paris.lutece.portal.web.user.attribute.AttributeFieldJspBean" />

<%
	attributeField.init( request, "CORE_USERS_MANAGEMENT" ) ; 
	response.sendRedirect( attributeField.doMoveDownAttributeField( request ) );  
%>

