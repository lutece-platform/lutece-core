<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.features.ExternalFeaturesJspBean"%>

${ externalFeaturesJspBean.init( pageContext.request, ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT ) }
${ pageContext.setAttribute( 'pluginActionResult', externalFeaturesJspBean.getModifyExternalFeature( pageContext.request ) ) }
${ empty pageContext.getAttribute( 'pluginActionResult' ).htmlContent ? pageContext.response.sendRedirect( pageContext.getAttribute( 'pluginActionResult' ).redirect ) : '' }

<jsp:include page="../AdminHeader.jsp" />

${ pageContext.getAttribute( 'pluginActionResult' ).htmlContent }

<%@ include file="../AdminFooter.jsp" %>