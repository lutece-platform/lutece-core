<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="rbac" scope="session" class="fr.paris.lutece.portal.web.rbac.RoleManagementJspBean" />

<% rbac.init( request, rbac.RIGHT_MANAGE_ROLES ) ; %>
<%= rbac.getAssignUsers( request ) %>

<%@ include file="../AdminFooter.jsp" %>