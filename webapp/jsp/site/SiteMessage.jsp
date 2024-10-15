<%@ page errorPage="ErrorPagePortal.jsp" %>
<%@ page import="fr.paris.lutece.portal.web.LocalVariables" %>
<%@page import="fr.paris.lutece.portal.service.page.PageNotFoundException"%>
<%@page import="fr.paris.lutece.portal.service.util.AppPathService"%>
<%@page import="jakarta.el.ELException" %>

<%@ page pageEncoding="UTF-8" %>

<jsp:include page="PortalHeader.jsp" />

<%
	// Required by JSR168 portlets (added in v1.2)
	LocalVariables.setLocal( config, request, response );

	try
	{
%>
	${ pageContext.setAttribute( 'strContent', portalJspBean.getSiteMessageContent( pageContext.request ) ) }
	${ empty pageContext.getAttribute ( 'strContent' ) ? pageContext.response.sendRedirect( AppPathService.getBaseUrl( pageContext.request ) += AppPathService.getPortalUrl() ) : '' }
	${ pageContext.getAttribute ( 'strContent' ) }
	${ pageContext.response.flushBuffer( ) }
<%
}
	catch ( ELException el )
	{
	    if ( PageNotFoundException.class.getCanonicalName(  ).equals( el.getCause( ).getClass( ).getCanonicalName( ) ) )
        {
	        response.sendError( HttpServletResponse.SC_NOT_FOUND );
        }
        else
        {
    		throw el;
        }
	}
	finally
	{
		LocalVariables.setLocal( null, null, null );
	}
%>