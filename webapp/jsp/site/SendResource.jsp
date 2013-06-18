<%@ page errorPage="ErrorPagePortal.jsp" %>

<%@page import="fr.paris.lutece.portal.service.message.SiteMessageException"%>
<%@page import="fr.paris.lutece.portal.service.page.PageNotFoundException"%>
<%@page import="fr.paris.lutece.portal.service.util.AppPathService"%>
<%@page import="fr.paris.lutece.portal.web.PortalJspBean"%>

<jsp:include page="PortalHeader.jsp" />

<%
	try
	{
		String strContent = PortalJspBean.sendResource( request );
		out.print( strContent );
		out.flush();
	}
	catch ( PageNotFoundException pnfe )
	{
		response.sendError( response.SC_NOT_FOUND );
	}
	catch( SiteMessageException lme )
	{
		response.sendRedirect( AppPathService.getSiteMessageUrl( request ) );
	}
%>

