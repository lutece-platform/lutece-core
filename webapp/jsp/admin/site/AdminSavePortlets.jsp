<%@ page errorPage="../ErrorPage.jsp" %>
<%@ page import="java.util.Enumeration" %>


<jsp:useBean id="admin" scope="session" class="fr.paris.lutece.portal.web.admin.AdminPageJspBean" />

  
<% 
admin.init( request , admin.RIGHT_MANAGE_ADMIN_SITE ); 
admin.savePortlets( request );  %>