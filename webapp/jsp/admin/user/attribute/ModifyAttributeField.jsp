<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.user.AdminUserJspBean"%>

${ attributeFieldJspBean.init( pageContext.request, AdminUserJspBean.RIGHT_USERS_MANAGEMENT ) }
${ attributeFieldJspBean.getModifyAttributeField( pageContext.request ) }

<%@ include file="../../AdminFooter.jsp" %>
