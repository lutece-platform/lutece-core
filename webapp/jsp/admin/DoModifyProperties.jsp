<%@ page errorPage="ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.system.SystemJspBean"%>

${ systemJspBean.init( pageContext.request, SystemJspBean.RIGHT_PROPERTIES_MANAGEMENT ) }
${ pageContext.response.sendRedirect( systemJspBean.doModifyProperties( pageContext.request, pageContext.servletContext )) }
