<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="securityHeaderConfig" scope="session" class="fr.paris.lutece.portal.web.system.SecurityHeaderConfigJspBean" />

<% securityHeaderConfig.init( request, securityHeaderConfig.RIGHT_SECURITY_HEADER_MANAGEMENT ) ; %>
<%= securityHeaderConfig.getCreateSecurityHeaderConfigItem( request ) %>

<%@ include file="../AdminFooter.jsp" %>