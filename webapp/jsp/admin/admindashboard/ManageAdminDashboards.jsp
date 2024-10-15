<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.dashboard.AdminDashboardJspBean"%>

${ adminDashboardJspBean.init( pageContext.request, AdminDashboardJspBean.RIGHT_MANAGE_ADMINDASHBOARD ) }
${ adminDashboardJspBean.getManageDashboards( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>
