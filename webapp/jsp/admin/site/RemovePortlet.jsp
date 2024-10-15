<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.admin.AdminPageJspBean"%>

${ adminPagePortletJspBean.init( pageContext.request, AdminPageJspBean.RIGHT_MANAGE_ADMIN_SITE ) }
${ pageContext.response.sendRedirect( adminPagePortletJspBean.getRemovePortlet( pageContext.request )) }
