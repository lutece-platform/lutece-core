<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.dashboard.DashboardJspBean"%>
<jsp:useBean id="dashboard" scope="request" class="fr.paris.lutece.portal.web.dashboard.DashboardJspBean" />

<% dashboard.init( request, DashboardJspBean.RIGHT_MANAGE_DASHBOARD ) ; %>
<%= dashboard.getManageDashboards( request ) %>

<%@ include file="../AdminFooter.jsp" %>
