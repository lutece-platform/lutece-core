<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.xsl.XslExportJspBean"%>

${ xslExportJspBean.init( pageContext.request, XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT ) }
${ pageContext.response.sendRedirect( xslExportJspBean.doRemoveXslExport( pageContext.request )) }
