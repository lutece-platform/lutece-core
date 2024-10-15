<%@page import="fr.paris.lutece.portal.service.util.AppPathService" %>
<%@page import="fr.paris.lutece.portal.service.util.AppLogService" %>

<% 
	try
	{
%>
      ${ pageContext.forward( AppPathService.getRootForwardUrl() ) }
<%
	}
	catch(Exception e )
	{
	    AppLogService.error( "Error index.jsp forward" + e.getMessage() , e );
	    pageContext.setAttribute ("err500", e);
%>
	${ portalJspBean.getError500Page( pageContext.request, pageContext.getAttribute('err500') ) }	    
<%
	}
%>
