<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="system" scope="session" class="fr.paris.lutece.portal.web.system.SystemJspBean" />

<%
    system.init( request, system.RIGHT_CACHE_MANAGEMENT );
    response.sendRedirect( system.doResetCaches());
%>

