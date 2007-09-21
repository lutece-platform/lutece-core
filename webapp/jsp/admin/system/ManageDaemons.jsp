<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="daemons" scope="session" class="fr.paris.lutece.portal.web.system.DaemonsJspBean" />

<% daemons.init( request , daemons.RIGHT_DAEMONS_MANAGEMENT ); %>
<%= daemons.getManageDaemons( request ) %>

<%@ include file="../AdminFooter.jsp" %>