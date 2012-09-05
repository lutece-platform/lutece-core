<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:useBean id="appXslExport" scope="session" class="fr.paris.lutece.portal.web.xsl.XslExportJspBean" />
<% 
	appXslExport.init( request,fr.paris.lutece.portal.web.xsl.XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT);
   	response.sendRedirect( appXslExport.getConfirmRemoveXslExport(request) );
%>
