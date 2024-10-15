<%@ page errorPage="ErrorPage.jsp" %>
<jsp:include page="AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.system.SystemJspBean"%>

${ systemJspBean.init( pageContext.request, SystemJspBean.RIGHT_PROPERTIES_MANAGEMENT ) }
${ systemJspBean.getManageProperties( pageContext.request ) }

<%@ include file="AdminFooter.jsp" %>