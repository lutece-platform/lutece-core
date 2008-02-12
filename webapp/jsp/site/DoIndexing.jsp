<jsp:useBean id="indexing" scope="session" class="fr.paris.lutece.portal.web.search.SearchIndexationJspBean" />


<%= indexing.doIndexing( request ) %>
