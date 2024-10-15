<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.system.PluginJspBean"%>

${ pluginJspBean.init( pageContext.request, PluginJspBean.RIGHT_MANAGE_PLUGINS ) }
${ pageContext.response.sendRedirect( pluginJspBean.getConfirmUninstallPlugin( pageContext.request )) }
