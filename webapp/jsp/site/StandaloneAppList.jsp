<%@ page errorPage="ErrorPagePortal.jsp" %>

<jsp:useBean id="standalone" scope="page" class="fr.paris.lutece.portal.web.StandaloneAppJspBean" />
<jsp:useBean id="portal" scope="page" class="fr.paris.lutece.portal.web.PortalJspBean" />

<%= standalone.getPluginList( request ) %>
