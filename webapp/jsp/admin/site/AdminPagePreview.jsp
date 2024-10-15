<%@ page pageEncoding="UTF-8" %>
<%@ page errorPage="../ErrorPage.jsp" %>

<%@ page import="fr.paris.lutece.portal.web.LocalVariables" %>
<%@page import="fr.paris.lutece.portal.web.admin.AdminPageJspBean"%>

${ adminPageJspBean.init( pageContext.request, AdminPageJspBean.RIGHT_MANAGE_ADMIN_SITE ) }

<%
    try
    {
    	LocalVariables.setLocal( config, request, response );
%>
  	${ adminPageJspBean.getAdminPagePreview( pageContext.request ) }
<%
    }
    finally
    {
        LocalVariables.setLocal( null, null, null );
    }
%>
