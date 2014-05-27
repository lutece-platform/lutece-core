<%@page import="fr.paris.lutece.portal.service.util.AppPathService" %>
<%@page import="fr.paris.lutece.portal.service.util.AppLogService" %>

<jsp:useBean id="portal" scope="page" class="fr.paris.lutece.portal.web.PortalJspBean" />

<% 
try
{
    request.getRequestDispatcher( AppPathService.getRootForwardUrl()).forward(request, response);  
}
catch(Exception e )
{
        AppLogService.error( "Error index.jsp forward" + e.getMessage() , e );
	out.print( portal.getError500Page( request , e.getMessage() ) );
}
	
%>
