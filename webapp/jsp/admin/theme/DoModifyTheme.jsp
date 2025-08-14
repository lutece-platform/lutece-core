<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="themeTheme" scope="session" class="fr.paris.lutece.portal.web.theme.ThemeJspBean" />

<%
	themeTheme.init( request, fr.paris.lutece.portal.web.theme.ThemeJspBean.RIGHT_MANAGE_THEMES );
    response.sendRedirect( themeTheme.doModifyTheme( request ) );
%>

