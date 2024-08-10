<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="securityheader" scope="session" class="fr.paris.lutece.portal.web.system.SecurityHeaderJspBean" />

<%
        securityheader.init( request, securityheader.RIGHT_SECURITY_HEADER_MANAGEMENT );
        response.sendRedirect( securityheader.doRemoveSecurityHeader( request ) );
%>
