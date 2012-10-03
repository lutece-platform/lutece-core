<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:useBean id="editorChoice" scope="session" class="fr.paris.lutece.portal.web.globalmanagement.EditorChoiceLutecePanelJspBean" />

<%
	editorChoice.init( request, "CORE_GLOBAL_MANAGEMENT" ) ;
	response.sendRedirect( editorChoice.doUpdateFrontOfficeEditor( request ) );
%>