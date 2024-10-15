<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.workgroup.AdminWorkgroupJspBean"%>

${ adminWorkgroupJspBean.init( pageContext.request, AdminWorkgroupJspBean.RIGHT_MANAGE_WORKGROUPS ) }
${ pageContext.response.sendRedirect( adminWorkgroupJspBean.doCreateWorkgroup( pageContext.request )) }
