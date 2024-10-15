<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.stylesheet.StyleSheetJspBean"%>

${ styleSheetJspBean.init( pageContext.request, StyleSheetJspBean.RIGHT_MANAGE_STYLESHEET ) }
${ pageContext.response.sendRedirect( styleSheetJspBean.getRemoveStyleSheet( pageContext.request )) }
