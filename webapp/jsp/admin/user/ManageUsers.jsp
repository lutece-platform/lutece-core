<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp"  flush="true" />

<jsp:useBean id="appUser" scope="session" class="fr.paris.lutece.portal.web.user.AdminUserJspBean" />

<% appUser.init( request, "CORE_USERS_MANAGEMENT" ) ; %>
<%= appUser.getManageAdminUsers( request ) %>

<%@ include file="../AdminFooter.jsp" %>