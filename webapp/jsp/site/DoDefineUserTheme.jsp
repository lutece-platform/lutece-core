<%@ page errorPage="ErrorPagePortal.jsp" %>

<jsp:useBean id="theme" scope="session" class="fr.paris.lutece.portal.web.style.ThemesJspBean" />

<%
    response.sendRedirect( theme.doModifyUserTheme( request , response ) );
%>

