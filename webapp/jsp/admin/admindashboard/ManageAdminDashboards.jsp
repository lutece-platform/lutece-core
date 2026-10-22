<%@ page errorPage="../ErrorPage.jsp" %>
${ adminDashboardJspBean.init( pageContext.request, AdminDashboardJspBean.RIGHT_MANAGE_ADMINDASHBOARD ) }
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.dashboard.AdminDashboardJspBean"%>

${ adminDashboardJspBean.getManageDashboards( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>
