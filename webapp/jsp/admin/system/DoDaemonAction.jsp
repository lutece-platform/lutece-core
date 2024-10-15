<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.system.DaemonsJspBean"%>

${ daemonsJspBean.init( pageContext.request, DaemonsJspBean.RIGHT_DAEMONS_MANAGEMENT ) }
${ pageContext.response.sendRedirect( daemonsJspBean.doDaemonAction( pageContext.request )) }
