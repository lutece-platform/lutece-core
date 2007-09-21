<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="group" scope="session" class="fr.paris.lutece.portal.web.group.GroupJspBean" />

<%
	group.init( request, group.RIGHT_GROUPS_MANAGEMENT );
   	response.sendRedirect( group.doAssignRoleGroup( request ) ); 
%>
