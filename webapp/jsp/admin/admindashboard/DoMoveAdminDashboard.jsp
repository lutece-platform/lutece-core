<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.dashboard.AdminDashboardJspBean"%>
<jsp:useBean id="adminDashboard" scope="request" class="fr.paris.lutece.portal.web.dashboard.AdminDashboardJspBean" />

<% adminDashboard.init( request, AdminDashboardJspBean.RIGHT_MANAGE_ADMINDASHBOARD ) ; %>
<% response.sendRedirect( adminDashboard.doMoveAdminDashboard( request ) ); %>
