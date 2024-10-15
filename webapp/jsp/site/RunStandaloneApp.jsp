<%@ page errorPage="ErrorPagePortal.jsp"%>
<%@page import="fr.paris.lutece.portal.service.message.SiteMessageException"%>
<%@page import="fr.paris.lutece.portal.service.util.AppPathService"%>
<%@page import="fr.paris.lutece.portal.service.page.PageNotFoundException"%>
<%@page import="jakarta.el.ELException" %>
<jsp:include page="PortalHeader.jsp" />

<%
	try
	{
%>
	${ standaloneAppJspBean.getContent( pageContext.request ) }
	${ pageContext.response.flushBuffer( ) }
<%
	}
	catch ( ELException el) 
	{
	    if ( PageNotFoundException.class.getCanonicalName(  ).equals( el.getCause( ).getClass( ).getCanonicalName( ) ) )
        {
	        response.sendError( HttpServletResponse.SC_NOT_FOUND );
        } 
	    else if ( SiteMessageException.class.getCanonicalName(  ).equals( el.getCause( ).getClass( ).getCanonicalName( ) ) )
        {
            response.sendRedirect( AppPathService.getSiteMessageUrl( request ) );
        }
        else
        {
    		throw el;
        }
	}
%>
