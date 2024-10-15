<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.features.FeaturesGroupJspBean"%>

${ featuresGroupJspBean.init( pageContext.request, FeaturesGroupJspBean.RIGHT_FEATURES_MANAGEMENT ) }
${ featuresGroupJspBean.getCreateGroup( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>