<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="features" scope="session" class="fr.paris.lutece.portal.web.features.FeaturesGroupJspBean" />

<% features.init( request , features.RIGHT_FEATURES_MANAGEMENT ); %>
<%= features.getModifyGroup ( request ) %>

<%@ include file="../AdminFooter.jsp" %>