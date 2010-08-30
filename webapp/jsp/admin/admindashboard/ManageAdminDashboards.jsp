<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.dashboard.AdminDashboardJspBean"%>
<jsp:useBean id="dashboard" scope="request" class="fr.paris.lutece.portal.web.dashboard.AdminDashboardJspBean" />

<% dashboard.init( request, AdminDashboardJspBean.RIGHT_MANAGE_ADMINDASHBOARD ) ; %>
<%= dashboard.getManageDashboards( request ) %>

<%@ include file="../AdminFooter.jsp" %>
