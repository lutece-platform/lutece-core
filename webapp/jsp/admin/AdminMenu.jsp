<%@ page errorPage="ErrorPage.jsp" %>
<jsp:include page="AdminHeader.jsp" />

<jsp:useBean id="adminMenu" scope="session" class="fr.paris.lutece.portal.web.admin.AdminMenuJspBean" />

<%= adminMenu.getAdminMenuUser( request ) %>

<%@ include file="AdminFooter.jsp" %>
