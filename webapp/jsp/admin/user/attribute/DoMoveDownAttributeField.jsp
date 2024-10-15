<%@ page errorPage="../../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.user.AdminUserJspBean"%>

${ attributeFieldJspBean.init( pageContext.request, AdminUserJspBean.RIGHT_USERS_MANAGEMENT ) }
${ pageContext.response.sendRedirect( attributeFieldJspBean.doMoveDownAttributeField( pageContext.request )) }
