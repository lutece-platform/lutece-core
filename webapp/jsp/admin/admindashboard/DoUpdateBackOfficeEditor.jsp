<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="editorChoice" scope="session" class="fr.paris.lutece.portal.web.editor.EditorChoiceLutecePanelJspBean" />

<%
	editorChoice.init( request, editorChoice.RIGHT_EDITORS_MANAGEMENT ) ;
	response.sendRedirect( editorChoice.doUpdateBackOfficeEditor( request ) );
%>