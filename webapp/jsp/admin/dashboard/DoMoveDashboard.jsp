<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.dashboard.DashboardJspBean"%>

${ dashboardJspBean.init( pageContext.request, DashboardJspBean.RIGHT_MANAGE_DASHBOARD ) }
${ pageContext.response.sendRedirect( dashboardJspBean.doMoveDashboard( pageContext.request )) }
