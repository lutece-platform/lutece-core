<%@ page errorPage="../ErrorPage.jsp" %>
${ pageTemplatesJspBean.init( pageContext.request, PageTemplatesJspBean.RIGHT_MANAGE_PAGE_TEMPLATES ) }
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.style.PageTemplatesJspBean"%>

${ pageTemplatesJspBean.getModifyPageTemplate( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>
