<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.workgroup.AdminWorkgroupJspBean"%>

${ adminWorkgroupJspBean.init( pageContext.request, AdminWorkgroupJspBean.RIGHT_MANAGE_WORKGROUPS ) }
${ adminWorkgroupJspBean.getCreateWorkgroup( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>
