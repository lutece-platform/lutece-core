<%@ page errorPage="ErrorPage.jsp" %>

${ pageContext.response.sendRedirect( adminMenuJspBean.doModifyHomeShortcuts( pageContext.request )) }

<%@ include file="AdminFooter.jsp"%>