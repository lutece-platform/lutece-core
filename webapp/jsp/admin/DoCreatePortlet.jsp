<%@ page errorPage="ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.admin.AdminPageJspBean"%>

${ adminPageJspBean.init( pageContext.request, AdminPageJspBean.RIGHT_MANAGE_ADMIN_SITE ) }
${ pageContext.forward( adminPagePortletJspBean.doCreatePortlet( pageContext.request ) ) }
