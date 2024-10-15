<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeaderSessionLess.jsp" />

${ empty adminDocumentationJspBean.getDocumentation( pageContext.request ) ? pageContext.response.sendRedirect( adminDocumentationJspBean.doAdminMessage( pageContext.request )) : adminDocumentationJspBean.getDocumentation( pageContext.request )}

<%@ include file="../AdminFooter.jsp" %>