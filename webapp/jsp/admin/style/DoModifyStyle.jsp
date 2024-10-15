<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.style.StylesJspBean"%>

${ stylesJspBean.init( pageContext.request, StylesJspBean.RIGHT_MANAGE_STYLE ) }
${ pageContext.response.sendRedirect( stylesJspBean.doModifyStyle( pageContext.request )) }
