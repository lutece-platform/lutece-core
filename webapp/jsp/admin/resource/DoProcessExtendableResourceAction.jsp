<%@page import="fr.paris.lutece.portal.web.pluginaction.IPluginActionResult"%>

<jsp:useBean id="extendableResourceAction" scope="session" class="fr.paris.lutece.portal.web.resource.ExtendableResourceJspBean" />

<% 
	IPluginActionResult result = extendableResourceAction.doProcessExtendableResourceAction( request, response );
	if ( result.getRedirect(  ) != null )
	{
		response.sendRedirect( result.getRedirect(  ) );
	}
	else if ( result.getHtmlContent(  ) != null )
	{
%>
		<%@ page errorPage="../ErrorPage.jsp" %>
		<jsp:include page="../AdminHeader.jsp" />

		<%= result.getHtmlContent(  ) %>

		<%@ include file="../AdminFooter.jsp" %>
<%
	}
%>
