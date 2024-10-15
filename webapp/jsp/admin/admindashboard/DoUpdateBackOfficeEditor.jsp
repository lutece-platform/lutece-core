<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.editor.EditorChoiceLutecePanelJspBean"%>

${ editorChoiceLutecePanelJspBean.init( pageContext.request, EditorChoiceLutecePanelJspBean.RIGHT_EDITORS_MANAGEMENT ) }
${ pageContext.response.sendRedirect( editorChoiceLutecePanelJspBean.doUpdateBackOfficeEditor( pageContext.request )) }
