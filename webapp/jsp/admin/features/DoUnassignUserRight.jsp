<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.features.RightJspBean"%>

${ rightJspBean.init( pageContext.request, RightJspBean.RIGHT_MANAGE_RIGHTS ) }
${ pageContext.response.sendRedirect( rightJspBean.doUnAssignUser( pageContext.request )) }
