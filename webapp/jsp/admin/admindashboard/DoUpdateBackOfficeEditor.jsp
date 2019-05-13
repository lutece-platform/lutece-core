<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="editorChoice" scope="session" class="fr.paris.lutece.portal.web.editor.EditorChoiceLutecePanelJspBean" />

<%
	editorChoice.init( request, "CORE_ADMIN_SITE" ) ;
	response.sendRedirect( editorChoice.doUpdateBackOfficeEditor( request ) );
%>