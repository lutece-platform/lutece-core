<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.system.DaemonsJspBean"%>

${ daemonsJspBean.init( pageContext.request, DaemonsJspBean.RIGHT_DAEMONS_MANAGEMENT ) }
${ daemonsJspBean.getManageDaemons( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>