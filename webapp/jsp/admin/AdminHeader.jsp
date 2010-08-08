<%@ page errorPage="ErrorPage.jsp" %>
<%@ page import="fr.paris.lutece.portal.service.util.AppPathService" %>
<%@ page pageEncoding="UTF-8" %>

<jsp:useBean id="adminMenu" scope="session" class="fr.paris.lutece.portal.web.admin.AdminMenuJspBean" />
<!DOCTYPE html >
<html lang="fr" xml:lang="fr">
<head>
<title>LUTECE - Administration</title>
<base href="<%= AppPathService.getBaseUrl( request ) %>"></base>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />

<link rel="stylesheet" type="text/css" href="css/portal_admin.css" title="lutece_admin" />
<link rel="stylesheet" type="text/css" href="css/print_admin.css" media="print" />
<link rel="stylesheet" type="text/css" href="js/jquery/plugins/autocomplete/jquery.autocomplete.css" />

<!--[if IE 6]>
		<link rel="stylesheet" type="text/css" href="css/ie/portal_admin_ie6.css" title="lutece_admin_ie6" />
<![endif]-->
<!--[if IE 7]>
		<link rel="stylesheet" type="text/css" href="css/ie/portal_admin_ie7.css" title="lutece_admin_ie7" />
<![endif]-->
<script type="text/javascript" src="js/admin_map.js"></script>
<script type="text/javascript" src="js/tools.js"></script>
<script type="text/javascript" src="js/selectbox.js"></script>
<script src="js/jquery/jquery-1.3.2.min.js" type="text/javascript"></script>
<script src="js/jquery/plugins/treeview/jquery.cookie.js" type="text/javascript"></script>
<script src="js/jquery/plugins/treeview/jquery.treeview.pack.js" type="text/javascript"></script>
<script src="js/jquery/plugins/ui/datepicker/ui.datepicker.js" type="text/javascript"></script>
<script src="js/jquery/plugins/ui/datepicker/ui.datepicker-fr.js" type="text/javascript"></script>
<script src="js/jquery/plugins/jpassword/jquery.jpassword.pack.js" type="text/javascript"></script>
<script src="js/jquery/plugins/autocomplete/jquery.autocomplete.pack.js" type="text/javascript"></script>
<script src="js/jquery/plugins/generatepassword/jquery.password.min.js" type="text/javascript"></script>
<script src="js/jquery/plugins/superfish/superfish.js" type="text/javascript" ></script>
<script type="text/javascript">
$(document).ready(function(){
$("ul.admin-header-menu").superfish({pathClass:  'admin-header-first' });
});
</script>
</head>
<body id="#top">
<%-- Display the admin menu --%>
<%= adminMenu.getAdminMenuHeader( request ) %>