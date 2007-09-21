<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="daemons" scope="session" class="fr.paris.lutece.portal.web.system.DaemonsJspBean" />

<%
    daemons.init( request , daemons.RIGHT_DAEMONS_MANAGEMENT );
    response.sendRedirect( daemons.doDaemonAction( request ) );
%>


