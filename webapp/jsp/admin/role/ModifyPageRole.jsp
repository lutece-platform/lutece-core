<%@ page errorPage="../ErrorPage.jsp" %>
${ roleJspBean.init( pageContext.request, RoleJspBean.RIGHT_ROLES_MANAGEMENT ) }
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.role.RoleJspBean"%>

${ roleJspBean.getModifyPageRole( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>