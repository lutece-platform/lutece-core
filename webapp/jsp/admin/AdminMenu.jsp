<%@ page errorPage="ErrorPage.jsp" %>
<jsp:include page="AdminHeader.jsp" />

${ adminMenuJspBean.getAdminMenuUser( pageContext.request ) }

<%@ include file="AdminFooter.jsp" %>
