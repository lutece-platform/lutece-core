<%@ page import="fr.paris.lutece.portal.service.util.AppPathService" %>
<%@ page import="fr.paris.lutece.portal.service.portal.PortalService" %>
<%@ page import="fr.paris.lutece.portal.service.admin.AdminThemeService" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page buffer="1024kb" %>
<%@ page autoFlush="false" %>
<jsp:useBean id="adminMenu" scope="session" class="fr.paris.lutece.portal.web.admin.AdminMenuJspBean" />
<!DOCTYPE html>
<html lang="fr">
<head>
<title><%= PortalService.getLuteceFavourite(  ) %> - Administration</title>
<base href="<%= AppPathService.getBaseUrl( request ) %>"></base>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<!-- Set the viewport width to device width for mobile -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Le styles -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<!-- style type="text/css">
body {padding-top: 60px;padding-bottom: 40px;}
.sidebar-nav {padding: 9px 0; }
</style -->
<link href="css/bootstrap-responsive.min.css" rel="stylesheet">
<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<!-- Le fav and touch icons -->
<link rel="shortcut icon" href="favicon.ico">
<!-- 
<link rel="apple-touch-icon-precomposed" sizes="144x144" href="apple-touch-icon-144-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="114x114" href="apple-touch-icon-114-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="72x72" href="apple-touch-icon-72-precomposed.png">
<link rel="apple-touch-icon-precomposed" href="apple-touch-icon-57-precomposed.png">
-->
<link rel="stylesheet" href="css/portal_admin.css" />
<%
	if ( AdminThemeService.isModeAccessible( request ) ){
%>
<link rel="stylesheet" type="text/css" href="css/portal_admin_accessibility.css" media="screen, projection" />
<%
	}
%>
<link rel="stylesheet" type="text/css" href="css/print_admin.css" media="print" />
<!--[if IE 6]>
<!-- Hack for menu 
<script type="text/javascript">
startList = function() {
if (document.all && document.getElementById) {
   var navRoot = document.getElementById("menu-main");
    for (i=0; i<navRoot.childNodes.length; i++) {
                    var node = navRoot.childNodes[i];
                    if (node.nodeName=="LI") {
                        node.onmouseover=function() {
                            this.className+=" over";
                        }
                        node.onmouseout=function() {
                            this.className=this.className.replace(" over", "");
                        }
                    }
                }
            }
        }
        window.onload=startList;
   </script>
-->
<![endif]-->
</head>
<body>
<%-- Display the admin menu --%>
<%= adminMenu.getAdminMenuHeader( request ) %>