<%@ page errorPage="../ErrorPage.jsp" %>
${ levelsJspBean.init( pageContext.request, LevelsJspBean.RIGHT_MANAGE_LEVELS ) }
${ pageContext.setAttribute( 'pluginActionResult', levelsJspBean.getModifyLevel( pageContext.request ) ) }
${ empty pageContext.getAttribute( 'pluginActionResult' ).htmlContent ? pageContext.response.sendRedirect( pageContext.getAttribute( 'pluginActionResult' ).redirect ) : '' }
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.features.LevelsJspBean"%>

${ pageContext.getAttribute( 'pluginActionResult' ).htmlContent }

<%@ include file="../AdminFooter.jsp" %>