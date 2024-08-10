<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="securityheader" scope="session" class="fr.paris.lutece.portal.web.system.SecurityHeaderJspBean" />

<% securityheader.init( request, securityheader.RIGHT_SECURITY_HEADER_MANAGEMENT ) ; %>
<%= securityheader.getModifySecurityHeader( request ) %>

<%@ include file="../AdminFooter.jsp" %>