<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="aliasPortlet" scope="session" class="fr.paris.lutece.portal.web.portlet.AliasPortletJspBean" />

        
<%
    aliasPortlet.init( request, aliasPortlet.RIGHT_MANAGE_ADMIN_SITE );
    response.sendRedirect( aliasPortlet.doModify( request ) );
%>

