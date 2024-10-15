<%@ page errorPage="ErrorPage.jsp" %>
<jsp:include page="AdminHeaderSessionLess.jsp" />

${ adminLoginJspBean.getForgotPassword( pageContext.request ) }
