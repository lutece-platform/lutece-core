<%@ page errorPage="../ErrorPage.jsp" %>
${ featuresGroupJspBean.init( pageContext.request, FeaturesGroupJspBean.RIGHT_FEATURES_MANAGEMENT ) }
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.features.FeaturesGroupJspBean"%>

${ featuresGroupJspBean.getCreateGroup( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>