<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="plugin" scope="session" class="fr.paris.lutece.portal.web.system.PluginJspBean" />

<% plugin.init( request , plugin.RIGHT_MANAGE_PLUGINS ); %>
<%= plugin.getPluginDescription( request ) %>

<%@ include file="../AdminFooter.jsp" %>