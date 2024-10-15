<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.stylesheet.StyleSheetJspBean"%>

${ styleSheetJspBean.init( pageContext.request, StyleSheetJspBean.RIGHT_MANAGE_STYLESHEET ) }
${ styleSheetJspBean.getManageStyleSheet( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>