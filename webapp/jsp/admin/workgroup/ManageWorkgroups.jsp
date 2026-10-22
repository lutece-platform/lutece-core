<%@ page errorPage="../ErrorPage.jsp" %>
${ adminWorkgroupJspBean.init( pageContext.request, AdminWorkgroupJspBean.RIGHT_MANAGE_WORKGROUPS ) }
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.workgroup.AdminWorkgroupJspBean"%>

${ adminWorkgroupJspBean.getManageWorkgroups( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>