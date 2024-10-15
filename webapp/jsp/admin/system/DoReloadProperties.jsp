<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.system.CacheJspBean"%>

${ cacheJspBean.init( pageContext.request, CacheJspBean.RIGHT_CACHE_MANAGEMENT ) }
${ pageContext.response.sendRedirect( cacheJspBean.doReloadProperties( pageContext.request )) }
