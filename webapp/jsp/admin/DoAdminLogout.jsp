<%@ page errorPage="ErrorPage.jsp" %>
<jsp:include page="AdminHeaderSessionLess.jsp" />

${ pageContext.response.sendRedirect( adminLoginJspBean.doLogout( pageContext.request )) }

<%@ include file="AdminFooter.jsp" %>
