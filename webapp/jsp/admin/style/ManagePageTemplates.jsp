<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.style.PageTemplatesJspBean"%>

${ pageTemplatesJspBean.init( pageContext.request, PageTemplatesJspBean.RIGHT_MANAGE_PAGE_TEMPLATES ) }
${ pageTemplatesJspBean.getManagePageTemplate( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>