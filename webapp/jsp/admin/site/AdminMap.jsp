<%@ page errorPage="../ErrorPage.jsp" %>
<%@ page import="java.util.Enumeration" %>

<%@page import="fr.paris.lutece.portal.web.admin.AdminMapJspBean"%>

${ adminMapJspBean.init( pageContext.request, AdminMapJspBean.RIGHT_MANAGE_ADMIN_SITE ) }
${ adminMapJspBean.getMap( pageContext.request ) }
