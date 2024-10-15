<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.system.CacheJspBean"%>

${ cacheJspBean.init( pageContext.request, CacheJspBean.RIGHT_CACHE_MANAGEMENT ) }
${ cacheJspBean.getCacheInfos( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>