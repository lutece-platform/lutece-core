<%@ page errorPage="../ErrorPage.jsp" %>
${ rightJspBean.init( pageContext.request, RightJspBean.RIGHT_MANAGE_RIGHTS ) }
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.features.RightJspBean"%>

${ rightJspBean.getAssignUsers( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>