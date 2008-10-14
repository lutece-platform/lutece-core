<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="featuresGroup" scope="session" class="fr.paris.lutece.portal.web.features.FeaturesGroupJspBean" />

<% 
		featuresGroup.init( request , featuresGroup.RIGHT_FEATURES_MANAGEMENT ); 
        response.sendRedirect( featuresGroup.doDispatchFeatureGroup( request ) );
%>

