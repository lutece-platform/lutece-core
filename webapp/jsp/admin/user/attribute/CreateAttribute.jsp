<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.user.AdminUserJspBean"%>

${ attributeJspBean.init( pageContext.request, AdminUserJspBean.RIGHT_USERS_MANAGEMENT ) }
${ attributeJspBean.getCreateAttribute( pageContext.request ) }

<%@ include file="../../AdminFooter.jsp" %>
