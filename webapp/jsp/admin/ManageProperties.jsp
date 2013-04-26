<%@ page errorPage="ErrorPage.jsp" %>
<jsp:include page="AdminHeader.jsp" />

<jsp:useBean id="system" scope="session" class="fr.paris.lutece.portal.web.system.SystemJspBean" />

<% system.init( request,  system.RIGHT_PROPERTIES_MANAGEMENT ); %>
<%= system.getManageProperties ( request )%>

<%@ include file="AdminFooter.jsp" %>