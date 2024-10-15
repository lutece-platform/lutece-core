<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.features.ExternalFeaturesJspBean"%>

${ externalFeaturesJspBean.init( pageContext.request, ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT ) }
${ pageContext.response.sendRedirect( externalFeaturesJspBean.doRemoveExternalFeature( pageContext.request )) }
