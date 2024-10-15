<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.system.SecurityHeaderJspBean"%>

${ securityHeaderJspBean.init( pageContext.request, SecurityHeaderJspBean.RIGHT_SECURITY_HEADER_MANAGEMENT ) }
${ securityHeaderJspBean.getModifySecurityHeader( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>