<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.rbac.RoleManagementJspBean"%>

${ roleManagementJspBean.init( pageContext.request, RoleManagementJspBean.RIGHT_MANAGE_ROLES ) }
${ pageContext.response.sendRedirect( roleManagementJspBean.doModifyRole( pageContext.request )) }
