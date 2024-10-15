<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.user.AdminUserJspBean"%>

${ adminUserJspBean.init( pageContext.request, AdminUserJspBean.RIGHT_USERS_MANAGEMENT ) }

${ pageContext.setAttribute( 'pluginActionResult', adminUserJspBean.doImportUsersFromFile( pageContext.request ) ) }
${ empty pageContext.getAttribute( 'pluginActionResult' ).htmlContent ? pageContext.response.sendRedirect( pageContext.getAttribute( 'pluginActionResult' ).redirect ) : '' }

<jsp:include page="../AdminHeader.jsp" flush="true" />

${ pageContext.getAttribute( 'pluginActionResult' ).htmlContent }

<%@ include file="../AdminFooter.jsp" %>
