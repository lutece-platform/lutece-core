<%@ page errorPage="ErrorPage.jsp" %>
<jsp:include page="AdminHeaderSessionLess.jsp" />

${ adminLoginJspBean.getLogin( pageContext.request, pageContext.response ) }
