<%@ page errorPage="ErrorPage.jsp" %>
<jsp:include page="AdminHeaderSessionLess.jsp" />

${ adminLoginJspBean.getResetPassword( pageContext.request ) }
