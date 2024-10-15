<%@ page errorPage="../../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.portlet.AliasPortletJspBean"%>

${ aliasPortletJspBean.init( pageContext.request, AliasPortletJspBean.RIGHT_MANAGE_ADMIN_SITE ) }
${ pageContext.response.sendRedirect( aliasPortletJspBean.doModify( pageContext.request )) }
