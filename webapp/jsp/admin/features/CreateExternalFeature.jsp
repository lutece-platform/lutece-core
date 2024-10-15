<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.features.ExternalFeaturesJspBean"%>

${ externalFeaturesJspBean.init( pageContext.request, ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT ) }
${ externalFeaturesJspBean.getCreateExternalFeature( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>