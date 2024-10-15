<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp"  flush="true" />

<%@page import="fr.paris.lutece.portal.web.user.AdminUserJspBean"%>

${ adminUserJspBean.init( pageContext.request, AdminUserJspBean.RIGHT_USERS_MANAGEMENT ) }
${ adminUserJspBean.getManageAdminUsers( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>
