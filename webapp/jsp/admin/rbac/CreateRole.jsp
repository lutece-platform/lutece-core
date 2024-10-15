<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.rbac.RoleManagementJspBean"%>

${ roleManagementJspBean.init( pageContext.request, RoleManagementJspBean.RIGHT_MANAGE_ROLES ) }
${ roleManagementJspBean.getCreateRole( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>