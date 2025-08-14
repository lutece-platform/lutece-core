<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="themeTheme" scope="session" class="fr.paris.lutece.portal.web.theme.ThemeJspBean" />

<% themeTheme.init( request, fr.paris.lutece.portal.web.theme.ThemeJspBean.RIGHT_MANAGE_THEMES ); %>
<%= themeTheme.getModifyTheme( request ) %>

<%@ include file="../AdminFooter.jsp" %>