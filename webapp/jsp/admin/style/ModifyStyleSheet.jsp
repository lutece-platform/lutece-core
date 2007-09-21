<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="stylesheet" scope="session" class="fr.paris.lutece.portal.web.stylesheet.StyleSheetJspBean" />

<% stylesheet.init(request, stylesheet.RIGHT_MANAGE_STYLESHEET ) ; %>
<%= stylesheet.getModifyStyleSheet ( request )%>

<%@ include file="../AdminFooter.jsp" %>