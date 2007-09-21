<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="workGroup" scope="session" class="fr.paris.lutece.portal.web.workgroup.AdminWorkgroupJspBean" />

<% workGroup.init( request, "CORE_WORKGROUPS_MANAGEMENT" ) ; %>
<%= workGroup.getAssignUsers( request ) %>

<%@ include file="../AdminFooter.jsp" %>