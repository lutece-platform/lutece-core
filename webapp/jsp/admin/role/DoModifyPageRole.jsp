<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.role.RoleJspBean"%>

${ roleJspBean.init( pageContext.request, RoleJspBean.RIGHT_ROLES_MANAGEMENT ) }
${ pageContext.response.sendRedirect( roleJspBean.doModifyPageRole( pageContext.request )) }
