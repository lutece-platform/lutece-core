<jsp:useBean id="themeTheme" scope="session" class="fr.paris.lutece.portal.web.theme.ThemeJspBean" />
<% String strContent = themeTheme.processController ( request , response ); %>

<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../AdminFooter.jsp" %>
