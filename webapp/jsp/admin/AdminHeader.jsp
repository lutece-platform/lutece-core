<%@ page errorPage="ErrorPage.jsp" %>
<%@ page import="fr.paris.lutece.portal.service.util.AppPathService" %>
<%@ page pageEncoding="UTF-8" %>

<jsp:useBean id="adminMenu" scope="session" class="fr.paris.lutece.portal.web.admin.AdminMenuJspBean" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">


<html xmlns="http://www.w3.org/1999/xhtml" lang="fr" xml:lang="fr">

<head>
<title>LUTECE - Administration</title>
<base href="<%= AppPathService.getBaseUrl( request ) %>" />
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />

<%
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>

<link rel="stylesheet" type="text/css" href="css/portal_admin.css" title="lutece_admin" />

<!--[if IE 6]>
		<link rel="stylesheet" type="text/css" href="css/ie/portal_admin_ie6.css" title="lutece_admin_ie6" />
<![endif]-->

<!--[if IE 7]>
		<link rel="stylesheet" type="text/css" href="css/ie/portal_admin_ie7.css" title="lutece_admin_ie7" />
	<![endif]-->


<script type="text/javascript" src="js/admin_map.js"></script>
<script type="text/javascript" src="js/menu.js" ></script>
<script type="text/javascript" src="js/tools.js"></script>
<script type="text/javascript" src="js/selectbox.js"></script>

<script src="js/jquery/jquery-1.3.2.min.js" type="text/javascript"></script>
<script src="js/jquery/plugins/treeview/jquery.cookie.js" type="text/javascript"></script>
<script src="js/jquery/plugins/treeview/jquery.treeview.pack.js" type="text/javascript"></script>
<script src="js/jquery/plugins/ui/datepicker/ui.datepicker.js" type="text/javascript"></script>
<script src="js/jquery/plugins/ui/datepicker/ui.datepicker-fr.js" type="text/javascript"></script>
<script src="js/jquery/plugins/jpassword/jquery.jpassword.pack.js" type="text/javascript"></script>

<script type="text/javascript">
function init()
{
	// The class id of the ul tag of the CSS menu
	setHover( 'admin-header-menu');
	window.focus();

}
</script>


</head>
<body onload="init()">

<%-- Display the admin menu --%>

<%= adminMenu.getAdminMenuHeader( request ) %>
