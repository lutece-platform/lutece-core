<%@ page errorPage="../ErrorPage.jsp" %>
${ adminPageJspBean.init( pageContext.request, AdminPageJspBean.RIGHT_MANAGE_ADMIN_SITE ) }
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.admin.AdminPageJspBean"%>


<jsp:include page="AdminPage.jsp" />

<%@ include file="../AdminFooter.jsp"%>

