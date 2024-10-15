<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

${ adminUserJspBean.init( pageContext.request, "CORE_USERS_MANAGEMENT" ) }
${ adminUserJspBean.getModifyAdminUserWorkgroups( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>