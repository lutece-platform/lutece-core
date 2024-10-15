<%@ page errorPage="../ErrorPage.jsp" %>

${ pageContext.response.sendRedirect( adminUserJspBean.reactivateAccount( pageContext.request )) }
