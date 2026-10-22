<%@ page errorPage="../../ErrorPage.jsp" %>
${ attributeFieldJspBean.init( pageContext.request, AdminUserJspBean.RIGHT_USERS_MANAGEMENT ) }
<jsp:include page="../../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.user.AdminUserJspBean"%>

${ attributeFieldJspBean.getModifyAttributeField( pageContext.request ) }

<%@ include file="../../AdminFooter.jsp" %>
