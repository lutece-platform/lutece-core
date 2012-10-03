<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp"  flush="true" />

<jsp:useBean id="globalManagement" scope="session" class="fr.paris.lutece.portal.web.globalmanagement.GlobalManagementJspBean" />

<% globalManagement.init( request, "CORE_GLOBAL_MANAGEMENT" ) ; %>
<%= globalManagement.getGlobalManagement( request ) %>

<%@ include file="../AdminFooter.jsp" %>