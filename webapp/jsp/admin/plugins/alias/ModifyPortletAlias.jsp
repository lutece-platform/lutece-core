<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../PortletAdminHeader.jsp" />

<jsp:useBean id="aliasPortlet" scope="session" class="fr.paris.lutece.portal.web.portlet.AliasPortletJspBean" />


<% aliasPortlet.init( request, aliasPortlet.RIGHT_MANAGE_ADMIN_SITE ); %>
<%= aliasPortlet.getModify ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>