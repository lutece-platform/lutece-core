<%@ page errorPage="../ErrorPage.jsp" %>

${ pageContext.response.sendRedirect( adminMenuJspBean.doModifyDefaultAdminUserPassword( pageContext.request )) }
