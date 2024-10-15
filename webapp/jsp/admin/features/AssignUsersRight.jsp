<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.features.RightJspBean"%>

${ rightJspBean.init( pageContext.request, RightJspBean.RIGHT_MANAGE_RIGHTS ) }
${ rightJspBean.getAssignUsers( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>