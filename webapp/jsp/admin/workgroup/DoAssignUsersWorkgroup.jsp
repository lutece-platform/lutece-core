<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="workgroup" scope="session" class="fr.paris.lutece.portal.web.workgroup.AdminWorkgroupJspBean" />

<% 
	workgroup.init( request, "CORE_WORKGROUPS_MANAGEMENT" ) ; 
	response.sendRedirect( workgroup.doAssignUsers( request ) );
%>


