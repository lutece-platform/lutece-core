<%@ page errorPage="../ErrorPage.jsp" %>
${ pluginJspBean.init( pageContext.request, PluginJspBean.RIGHT_MANAGE_PLUGINS ) }
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.system.PluginJspBean"%>

${ pluginJspBean.getPluginDescription( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>