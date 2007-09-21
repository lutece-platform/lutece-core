<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="style" scope="session" class="fr.paris.lutece.portal.web.style.StylesJspBean" />

<% style.init(request, style.RIGHT_MANAGE_STYLE ); %>
<%= style.getStylesManagement ( request )%>

<%@ include file="../AdminFooter.jsp" %>