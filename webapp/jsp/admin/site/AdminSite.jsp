<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.admin.AdminPageJspBean"%>

${ adminPageJspBean.init( pageContext.request, AdminPageJspBean.RIGHT_MANAGE_ADMIN_SITE ) }

<jsp:include page="AdminPage.jsp" />

<%@ include file="../AdminFooter.jsp"%>

<%@page import="fr.paris.lutece.portal.web.admin.AdminMapJspBean"%>

${ adminMapJspBean.init( pageContext.request, AdminMapJspBean.RIGHT_MANAGE_ADMIN_SITE ) }
${ adminMapJspBean.getMap( pageContext.request ) }
