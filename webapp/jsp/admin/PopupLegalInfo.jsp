<%@ page errorPage="ErrorPagePortal.jsp" %>

<jsp:useBean id="portal" scope="page" class="fr.paris.lutece.portal.web.PortalJspBean" />

<%= portal.getLegalInfos( request ) %>

