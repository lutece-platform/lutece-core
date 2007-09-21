<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="mode" scope="session" class="fr.paris.lutece.portal.web.style.ModesJspBean" />

<%
    mode.init( request , mode.RIGHT_MANAGE_MODES );
    response.sendRedirect( mode.doModifyMode( request ) );
%>

