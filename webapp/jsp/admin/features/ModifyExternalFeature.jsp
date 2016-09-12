<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="externalFeatures" scope="session" class="fr.paris.lutece.portal.web.features.ExternalFeaturesJspBean" />

<% externalFeatures.init( request , externalFeatures.RIGHT_EXTERNAL_FEATURES_MANAGEMENT ); %>
<%= externalFeatures.getModifyExternalFeature ( request ) %>

<%@ include file="../AdminFooter.jsp" %>