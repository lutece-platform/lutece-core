<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.features.FeaturesGroupJspBean"%>

${ featuresGroupJspBean.init( pageContext.request, FeaturesGroupJspBean.RIGHT_FEATURES_MANAGEMENT ) }
${ featuresGroupJspBean.getModifyGroup( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>