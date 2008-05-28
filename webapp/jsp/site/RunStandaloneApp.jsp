<%@ page errorPage="ErrorPagePortal.jsp"%>

<%@page import="fr.paris.lutece.portal.service.message.SiteMessageException"%>
<%@page import="fr.paris.lutece.portal.service.util.AppPathService"%>
<jsp:include page="PortalHeader.jsp" />

<jsp:useBean id="standalone" scope="page" class="fr.paris.lutece.portal.web.StandaloneAppJspBean" />

<%
	try
	{
		String strContent = standalone.getContent( request );
		out.print( strContent );
		out.flush();
	}
	catch (SiteMessageException lsme)
	{
		response.sendRedirect( AppPathService.getSiteMessageUrl( request ) );
	}
%>

