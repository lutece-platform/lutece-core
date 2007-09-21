<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="mode" scope="session" class="fr.paris.lutece.portal.web.style.ModesJspBean" />

<% mode.init( request , mode.RIGHT_MANAGE_MODES ); %>
<%= mode.getManageModes( request ) %>

<%@ include file="../AdminFooter.jsp" %>