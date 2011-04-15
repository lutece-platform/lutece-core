<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="appSearch" scope="session" class="fr.paris.lutece.portal.web.search.SearchJspBean" />

<% appSearch.init( request, "CORE_SEARCH_MANAGEMENT" ) ; %>
<%= appSearch.getManageAdvancedParameters( request ) %>

<%@ include file="../AdminFooter.jsp" %>