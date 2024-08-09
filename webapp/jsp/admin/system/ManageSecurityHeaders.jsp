<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="securityHeader" scope="session" class="fr.paris.lutece.portal.web.system.SecurityHeaderJspBean" />

<% securityHeader.init( request , securityHeader.RIGHT_SECURITY_HEADER_MANAGEMENT ); %>
<%= securityHeader.getManageSecurityHeaders( request ) %>

<%@ include file="../AdminFooter.jsp" %>