<%@ page errorPage="../ErrorPage.jsp" %>
${ adminUserJspBean.init( pageContext.request, AdminUserJspBean.RIGHT_USERS_MANAGEMENT ) }
<jsp:include page="../AdminHeader.jsp"  flush="true" />

<%@page import="fr.paris.lutece.portal.web.user.AdminUserJspBean"%>

${ adminUserJspBean.getManageAdminUsers( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>
