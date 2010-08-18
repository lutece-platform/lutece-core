<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="attribute" scope="session" class="fr.paris.lutece.portal.web.user.attribute.AttributeJspBean" />

<% attribute.init( request, "CORE_USERS_MANAGEMENT" ) ; %>
<%= attribute.getCreateAttribute ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>
