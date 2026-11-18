<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.user.AdminUserJspBean"%>

${ adminUserJspBean.init( pageContext.request, AdminUserJspBean.RIGHT_USERS_MANAGEMENT ) }

${ pageContext.setAttribute( 'pluginActionResult', adminUserJspBean.doExportUsers( pageContext.request, pageContext.response ) ) }
<%
    // The users file has been written in the response: nothing else must be added to it (no admin page)
    if ( pageContext.getAttribute( "pluginActionResult" ) == null )
    {
        out.clearBuffer( );
        return;
    }
%>
${ not empty pageContext.getAttribute( 'pluginActionResult' ).redirect ? pageContext.response.sendRedirect( pageContext.getAttribute( 'pluginActionResult' ).redirect ) : '' }

<jsp:include page="../AdminHeader.jsp" flush="true" />

${ pageContext.getAttribute( 'pluginActionResult' ).htmlContent }

<%@ include file="../AdminFooter.jsp" %>
