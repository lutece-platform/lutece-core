<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="level" scope="session" class="fr.paris.lutece.portal.web.features.LevelsJspBean" />

<% level.init( request , level.RIGHT_MANAGE_LEVELS ); %>
<%= level.getCreateLevel( request )%>

<%@ include file="../AdminFooter.jsp" %>