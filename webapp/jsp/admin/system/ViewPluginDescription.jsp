<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.system.PluginJspBean"%>

${ pluginJspBean.init( pageContext.request, PluginJspBean.RIGHT_MANAGE_PLUGINS ) }
${ pluginJspBean.getPluginDescription( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>