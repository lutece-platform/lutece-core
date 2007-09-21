<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="indexing" scope="session" class="fr.paris.lutece.portal.web.search.SearchIndexationJspBean" />

<% indexing.init( request , indexing.RIGHT_INDEXER ); %>
<%= indexing.getIndexingProperties( request ) %>

<%@ include file="../AdminFooter.jsp" %>
