<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.style.StylesJspBean"%>

${ stylesJspBean.init( pageContext.request, StylesJspBean.RIGHT_MANAGE_STYLE ) }
${ stylesJspBean.getModifyStyle( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>