<%@ page errorPage="../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.portal.web.search.SearchJspBean"%>

${ searchJspBean.init( pageContext.request, SearchJspBean.RIGHT_SEARCH_MANAGEMENT ) }
${ pageContext.response.sendRedirect( searchJspBean.doModifyAdvancedParameters( pageContext.request )) }
