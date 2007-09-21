<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="rbac" scope="session" class="fr.paris.lutece.portal.web.rbac.RoleManagementJspBean" />

<%
    rbac.init( request, rbac.RIGHT_MANAGE_ROLES ) ;
    response.sendRedirect( rbac.doSelectResources(request ));
%>