<%@ page errorPage="../ErrorPage.jsp" %>
<%@ page import="fr.paris.lutece.portal.web.LocalVariables" %>

<%@ page pageEncoding="UTF-8" %>

<jsp:useBean id="admin" scope="session" class="fr.paris.lutece.portal.web.admin.AdminPageJspBean" />

<% admin.init( request , admin.RIGHT_MANAGE_ADMIN_SITE ); %>
<%
    try
    {
    	LocalVariables.setLocal( config, request, response );
%>
  	<%= admin.getAdminPagePreview( request ) %>
<%
    }
    finally
    {
        LocalVariables.setLocal( null, null, null );
    }
%>

