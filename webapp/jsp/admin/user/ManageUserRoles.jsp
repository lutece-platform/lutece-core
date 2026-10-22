<%@ page errorPage="../ErrorPage.jsp" %>
${ adminUserJspBean.init( pageContext.request, AdminUserJspBean.RIGHT_USERS_MANAGEMENT ) }
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.user.AdminUserJspBean"%>

${ adminUserJspBean.getManageAdminUserRoles( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>