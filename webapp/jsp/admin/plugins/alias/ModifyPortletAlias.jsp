<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../PortletAdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.portlet.AliasPortletJspBean"%>

${ aliasPortletJspBean.init( pageContext.request, AliasPortletJspBean.RIGHT_MANAGE_ADMIN_SITE ) }
${ aliasPortletJspBean.getModify( pageContext.request ) }
