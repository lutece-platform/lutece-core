<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="plugin" scope="session" class="fr.paris.lutece.portal.web.system.PluginJspBean" />

<% 
    plugin.init( request , plugin.RIGHT_MANAGE_PLUGINS ); 
    response.sendRedirect( plugin.doModifyPluginPool( request ) );
%>


