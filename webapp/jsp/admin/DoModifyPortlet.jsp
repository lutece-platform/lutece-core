<%-- This JSP is displayed in a IFrame. It doesn't need the admin header and footer--%>
<%@ page errorPage="ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.admin.AdminPageJspBean"%>

${ adminPageJspBean.init( pageContext.request, AdminPageJspBean.RIGHT_MANAGE_ADMIN_SITE ) }
${ pageContext.forward( adminPagePortletJspBean.doModifyPortlet( pageContext.request ) ) }
