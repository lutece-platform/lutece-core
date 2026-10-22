<%@ page errorPage="../../ErrorPage.jsp" %>
${ attributeJspBean.init( pageContext.request, AdminUserJspBean.RIGHT_USERS_MANAGEMENT ) }
<jsp:include page="../../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.user.AdminUserJspBean"%>

${ attributeJspBean.getModifyAttribute( pageContext.request ) }

<%@ include file="../../AdminFooter.jsp" %>
