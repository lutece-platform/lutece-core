<%@ page errorPage="../ErrorPage.jsp" %>
${ cacheJspBean.init( pageContext.request, CacheJspBean.RIGHT_CACHE_MANAGEMENT ) }
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.system.CacheJspBean"%>

${ cacheJspBean.getManageCaches( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>