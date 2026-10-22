<%@ page errorPage="ErrorPage.jsp" %>
${ systemJspBean.init( pageContext.request, SystemJspBean.RIGHT_PROPERTIES_MANAGEMENT ) }
<jsp:include page="AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.system.SystemJspBean"%>

${ systemJspBean.getManageProperties( pageContext.request ) }

<%@ include file="AdminFooter.jsp" %>