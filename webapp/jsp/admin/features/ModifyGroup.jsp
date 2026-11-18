<%@ page errorPage="../ErrorPage.jsp" %>
${ featuresGroupJspBean.init( pageContext.request, FeaturesGroupJspBean.RIGHT_FEATURES_MANAGEMENT ) }
${ pageContext.setAttribute( 'pluginActionResult', featuresGroupJspBean.getModifyGroup( pageContext.request ) ) }
${ empty pageContext.getAttribute( 'pluginActionResult' ).htmlContent ? pageContext.response.sendRedirect( pageContext.getAttribute( 'pluginActionResult' ).redirect ) : '' }
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.features.FeaturesGroupJspBean"%>

${ pageContext.getAttribute( 'pluginActionResult' ).htmlContent }

<%@ include file="../AdminFooter.jsp" %>