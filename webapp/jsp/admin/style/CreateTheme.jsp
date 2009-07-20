<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="theme" scope="session" class="fr.paris.lutece.portal.web.style.ThemesJspBean" />

<% theme.init( request , fr.paris.lutece.portal.web.style.ThemesJspBean.RIGHT_MANAGE_THEMES ); %>
<%= theme.getCreateTheme( request ) %>

<%@ include file="../AdminFooter.jsp" %>