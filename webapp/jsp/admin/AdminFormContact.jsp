<%@ page errorPage="ErrorPage.jsp" %>
<jsp:include page="AdminHeaderSessionLess.jsp" />

${ adminLoginJspBean.getFormContact( pageContext.request ) }

<%@ include file="AdminFooter.jsp" %>
