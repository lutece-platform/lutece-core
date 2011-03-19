<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="cache" scope="session" class="fr.paris.lutece.portal.web.system.CacheJspBean" />

<%
    cache.init( request, cache.RIGHT_CACHE_MANAGEMENT );
    response.sendRedirect( cache.doToggleCache( request ));
%>

