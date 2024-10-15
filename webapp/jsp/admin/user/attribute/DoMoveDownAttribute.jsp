<%@ page errorPage="../../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.user.AdminUserJspBean"%>

${ attributeJspBean.init( pageContext.request, AdminUserJspBean.RIGHT_USERS_MANAGEMENT ) }
${ pageContext.response.sendRedirect( attributeJspBean.doMoveDownAttribute( pageContext.request )) }
