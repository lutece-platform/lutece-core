<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="appSearch" scope="session" class="fr.paris.lutece.portal.web.search.SearchJspBean" />

<%
	appSearch.init( request, "CORE_SEARCH_MANAGEMENT" ) ; 
	response.sendRedirect( appSearch.doModifyAdvancedParameters( request ) );  
%>

