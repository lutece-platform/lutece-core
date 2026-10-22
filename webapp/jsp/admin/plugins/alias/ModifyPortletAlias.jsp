<%@ page errorPage="../../ErrorPage.jsp" %>
${ aliasPortletJspBean.init( pageContext.request, AliasPortletJspBean.RIGHT_MANAGE_ADMIN_SITE ) }
<jsp:include page="../../PortletAdminHeader.jsp" />

<%@page import="fr.paris.lutece.portal.web.portlet.AliasPortletJspBean"%>

${ aliasPortletJspBean.getModify( pageContext.request ) }
