<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="role" scope="session" class="fr.paris.lutece.portal.web.role.RoleJspBean" />

<%
	role.init( request, role.RIGHT_ROLES_MANAGEMENT );
    response.sendRedirect( role.doModifyPageRole( request ) );
%>
