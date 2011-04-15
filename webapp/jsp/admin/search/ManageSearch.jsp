<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp"  flush="true" />

<jsp:useBean id="appSearch" scope="session" class="fr.paris.lutece.portal.web.search.SearchJspBean" />

<% appSearch.init( request, "CORE_SEARCH_MANAGEMENT" ) ; %>
<%= appSearch.getManageSearch( request ) %>

<%@ include file="../AdminFooter.jsp" %>