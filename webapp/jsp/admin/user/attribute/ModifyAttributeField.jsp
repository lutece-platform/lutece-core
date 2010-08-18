<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="attributeField" scope="session" class="fr.paris.lutece.portal.web.user.attribute.AttributeFieldJspBean" />

<% attributeField.init( request, "CORE_USERS_MANAGEMENT" ) ; %>
<%= attributeField.getModifyAttributeField ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>
