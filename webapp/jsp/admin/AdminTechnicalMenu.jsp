<%@ page errorPage="ErrorPage.jsp" %>
<jsp:include page="AdminHeader.jsp" />

${ adminDashboardJspBean.getAdminDashboards( pageContext.request ) }

<%@ include file="AdminFooter.jsp" %>