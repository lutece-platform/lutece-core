<%@ page errorPage="../ErrorPage.jsp" contentType="application/json; charset=UTF-8" trimDirectiveWhitespaces="true" %>

<%@page import="fr.paris.lutece.portal.web.admin.AdminPageJspBean"%>

${ adminPagePortletJspBean.init( pageContext.request, AdminPageJspBean.RIGHT_MANAGE_ADMIN_SITE ) }
${ adminPagePortletJspBean.doModifyPortletStatusJson( pageContext.request ) }
