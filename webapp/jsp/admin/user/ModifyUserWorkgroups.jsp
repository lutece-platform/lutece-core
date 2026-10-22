<%@ page errorPage="../ErrorPage.jsp" %>
${ adminUserJspBean.init( pageContext.request, "CORE_USERS_MANAGEMENT" ) }
<jsp:include page="../AdminHeader.jsp" />

${ adminUserJspBean.getModifyAdminUserWorkgroups( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>