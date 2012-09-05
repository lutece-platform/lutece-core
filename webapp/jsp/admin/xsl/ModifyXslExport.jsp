<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />
<jsp:useBean id="appXslExport" scope="session" class="fr.paris.lutece.portal.web.xsl.XslExportJspBean" />
<% 
	appXslExport.init( request,fr.paris.lutece.portal.web.xsl.XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT);
%>
<%= appXslExport.getModifyXslExport( request ) %>
<%@ include file="../AdminFooter.jsp" %>