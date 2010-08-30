<%@ page errorPage="ErrorPage.jsp" %>
<jsp:include page="AdminHeader.jsp" />

<jsp:useBean id="dashboard" scope="request" class="fr.paris.lutece.portal.web.dashboard.AdminDashboardJspBean" />

<%= dashboard.getAdminDashboards( request ) %>

<%@ include file="AdminFooter.jsp" %>