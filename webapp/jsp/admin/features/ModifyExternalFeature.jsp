<%@ page errorPage="../ErrorPage.jsp" %>
${ externalFeaturesJspBean.init( pageContext.request, ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT ) }
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.features.ExternalFeaturesJspBean"%>

${ externalFeaturesJspBean.getModifyExternalFeature( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>