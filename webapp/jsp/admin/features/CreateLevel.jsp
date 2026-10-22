<%@ page errorPage="../ErrorPage.jsp" %>
${ levelsJspBean.init( pageContext.request, LevelsJspBean.RIGHT_MANAGE_LEVELS ) }
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.features.LevelsJspBean"%>

${ levelsJspBean.getCreateLevel( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>