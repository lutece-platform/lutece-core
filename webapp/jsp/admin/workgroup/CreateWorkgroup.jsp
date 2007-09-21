<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="workgroup" scope="session" class="fr.paris.lutece.portal.web.workgroup.AdminWorkgroupJspBean" />

<% workgroup.init( request, "CORE_WORKGROUPS_MANAGEMENT" ) ; %>
<%= workgroup.getCreateWorkgroup( request ) %>

<%@ include file="../AdminFooter.jsp" %>
