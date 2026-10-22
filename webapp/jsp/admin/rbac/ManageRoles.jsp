<%@ page errorPage="../ErrorPage.jsp" %>
${ roleManagementJspBean.init( pageContext.request, RoleManagementJspBean.RIGHT_MANAGE_ROLES ) }
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.rbac.RoleManagementJspBean"%>

${ roleManagementJspBean.getManageRoles( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>