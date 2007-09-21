<%@ page errorPage="ErrorPage.jsp" %>

<jsp:useBean id="system" scope="session" class="fr.paris.lutece.portal.web.system.SystemJspBean" />

<%
    system.init( request,  system.RIGHT_PROPERTIES_MANAGEMENT ); 
    response.sendRedirect( system.doModifyProperties( request , getServletContext() ));
%>

