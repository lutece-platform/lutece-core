<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.features.LevelsJspBean"%>

${ levelsJspBean.init( pageContext.request, LevelsJspBean.RIGHT_MANAGE_LEVELS ) }
${ pageContext.response.sendRedirect( levelsJspBean.doModifyLevel( pageContext.request )) }
