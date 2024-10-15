<%@ page errorPage="ErrorPage.jsp" %>
<jsp:include page="AdminHeaderSessionLess.jsp" />

${ pageContext.response.sendRedirect( adminLoginJspBean.doResetPassword( pageContext.request )) }
