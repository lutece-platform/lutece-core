<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.dashboard.AdminDashboardJspBean"%>

${ adminDashboardJspBean.init( pageContext.request, AdminDashboardJspBean.RIGHT_MANAGE_ADMINDASHBOARD ) }
${ pageContext.response.sendRedirect( adminDashboardJspBean.doMoveAdminDashboard( pageContext.request )) }
