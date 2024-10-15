<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.features.FeaturesGroupJspBean"%>

${ featuresGroupJspBean.init( pageContext.request, FeaturesGroupJspBean.RIGHT_FEATURES_MANAGEMENT ) }
${ pageContext.response.sendRedirect( featuresGroupJspBean.getRemoveGroup( pageContext.request )) }
