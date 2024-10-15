<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.role.RoleJspBean"%>

${ roleJspBean.init( pageContext.request, RoleJspBean.RIGHT_ROLES_MANAGEMENT ) }
${ roleJspBean.getModifyPageRole( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>