<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="features" scope="session" class="fr.paris.lutece.portal.web.features.FeaturesGroupJspBean" />

<% 
        features.init( request , features.RIGHT_FEATURES_MANAGEMENT ); 
        response.sendRedirect( features.doRemoveGroup( request ) );
%>

