<%@ page errorPage="../ErrorPage.jsp" %>
${ daemonsJspBean.init( pageContext.request, DaemonsJspBean.RIGHT_DAEMONS_MANAGEMENT ) }
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.system.DaemonsJspBean"%>

${ daemonsJspBean.getManageDaemons( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>