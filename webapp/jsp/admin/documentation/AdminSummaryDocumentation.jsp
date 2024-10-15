<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

${ adminDocumentationJspBean.getSummaryDocumentation( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>