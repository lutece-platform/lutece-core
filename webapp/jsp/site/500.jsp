<%@ page errorPage="ErrorPagePortal.jsp" %>

<jsp:include page="PortalHeader.jsp" />

<jsp:useBean id="portal" scope="page" class="fr.paris.lutece.portal.web.PortalJspBean" />

<%= portal.getFatalErrorPage( request , "Erreur fatale contacter votre administrateur" ) %>


