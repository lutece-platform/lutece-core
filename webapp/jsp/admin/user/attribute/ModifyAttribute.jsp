<%@ page errorPage="../../ErrorPage.jsp" %>
${ attributeJspBean.init( pageContext.request, AdminUserJspBean.RIGHT_USERS_MANAGEMENT ) }
${ pageContext.setAttribute( 'pluginActionResult', attributeJspBean.getModifyAttribute( pageContext.request ) ) }
${ empty pageContext.getAttribute( 'pluginActionResult' ).htmlContent ? pageContext.response.sendRedirect( pageContext.getAttribute( 'pluginActionResult' ).redirect ) : '' }
<jsp:include page="../../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.user.AdminUserJspBean"%>

${ pageContext.getAttribute( 'pluginActionResult' ).htmlContent }

<%@ include file="../../AdminFooter.jsp" %>
