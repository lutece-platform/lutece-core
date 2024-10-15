<%@ page errorPage="ErrorPage.jsp" %>
<jsp:include page="AdminHeaderSessionLess.jsp" />

${ adminLoginJspBean.getForgotLogin( pageContext.request ) }
