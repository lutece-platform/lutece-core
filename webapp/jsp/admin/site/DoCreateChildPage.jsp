<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="admin" scope="session" class="fr.paris.lutece.portal.web.admin.AdminPageJspBean" />

<% 
    admin.init( request , admin.RIGHT_MANAGE_ADMIN_SITE ); 
    response.sendRedirect( admin.doCreateChildPage( request ) );
%>







