<%@ page errorPage="ErrorPage.jsp" %>

${ pageContext.response.sendRedirect( adminMenuJspBean.doModifyAccessibilityMode( pageContext.request )) }

<%@ include file="AdminFooter.jsp"%>