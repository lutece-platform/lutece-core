<%@ page errorPage="ErrorPage.jsp" %>
<jsp:include page="AdminHeaderSessionLess.jsp" />

${ adminMessageJspBean.getMessage( pageContext.request ) }

<%@ include file="AdminFooter.jsp" %>