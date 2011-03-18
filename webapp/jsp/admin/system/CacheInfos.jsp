<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="cache" scope="session" class="fr.paris.lutece.portal.web.system.CacheJspBean" />

<% cache.init( request , cache.RIGHT_CACHE_MANAGEMENT ); %>
<%= cache.getCacheInfos( request ) %>

<%@ include file="../AdminFooter.jsp" %>