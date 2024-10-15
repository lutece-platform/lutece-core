<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.system.SystemJspBean"%>

${ systemJspBean.init( pageContext.request, SystemJspBean.RIGHT_LOGS_VISUALISATION ) }
${ systemJspBean.getManageFilesSystem( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>
