<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.system.SecurityHeaderJspBean"%>

${ securityHeaderJspBean.init( pageContext.request, SecurityHeaderJspBean.RIGHT_SECURITY_HEADER_MANAGEMENT ) }
${ pageContext.response.sendRedirect( securityHeaderJspBean.doCreateSecurityHeader( pageContext.request )) }
