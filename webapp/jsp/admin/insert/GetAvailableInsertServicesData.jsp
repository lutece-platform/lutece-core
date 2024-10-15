<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.insert.InsertServiceSelectorJspBean"%>

${ insertServiceSelectorJspBean.init( pageContext.request, InsertServiceSelectorJspBean.RIGHT_MANAGE_LINK_SERVICE ) }
${ insertServiceSelectorJspBean.getServicesListPage( pageContext.request ) }
