<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

${ adminMenuJspBean.getModifyDefaultAdminUserPassword( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>
