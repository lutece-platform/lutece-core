<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.xsl.XslExportJspBean"%>

${ xslExportJspBean.init( pageContext.request, XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT ) }
${ xslExportJspBean.getModifyXslExport( pageContext.request ) }

<%@ include file="../AdminFooter.jsp" %>