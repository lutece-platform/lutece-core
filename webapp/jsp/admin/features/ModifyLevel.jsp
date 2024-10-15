<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.features.LevelsJspBean"%>

${ levelsJspBean.init( pageContext.request, LevelsJspBean.RIGHT_MANAGE_LEVELS ) }
${ levelsJspBean.getModifyLevel( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>