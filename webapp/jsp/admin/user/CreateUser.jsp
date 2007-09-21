<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="appUser" scope="session" class="fr.paris.lutece.portal.web.user.AdminUserJspBean" />

<% appUser.init( request, "CORE_USERS_MANAGEMENT" ) ; %>
<%= appUser.getCreateAdminUser ( request ) %>

<%@ include file="../AdminFooter.jsp" %>
