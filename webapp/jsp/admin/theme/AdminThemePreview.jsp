<%@page import="fr.paris.lutece.portal.web.admin.AdminPageJspBean"%>

<% adminPageJspBean.init( request , fr.paris.lutece.portal.web.theme.ThemeJspBean.RIGHT_MANAGE_THEMES ); %>
<%
    try
    {
    	LocalVariables.setLocal( config, request, response );
%>
  	<%= adminPageJspBean.getAdminPagePreview( request ) %>
<%
    }
    finally
    {
        LocalVariables.setLocal( null, null, null );
    }
%>

