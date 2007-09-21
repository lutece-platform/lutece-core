<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="role" scope="session" class="fr.paris.lutece.portal.web.role.RoleJspBean" />

<% role.init( request, role.RIGHT_ROLES_MANAGEMENT ); %>
<%= role.getCreatePageRole( request ) %>

<%@ include file="../AdminFooter.jsp" %>