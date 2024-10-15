<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.user.AdminUserJspBean"%>

${ adminUserJspBean.init( pageContext.request, AdminUserJspBean.RIGHT_USERS_MANAGEMENT ) }
${ pageContext.response.sendRedirect( adminUserJspBean.doModifyAdminUserPassword( pageContext.request )) }
