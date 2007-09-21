package fr.paris.lutece.portal.service.search;

import fr.paris.lutece.portal.service.util.AppLogService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;

/**
 * LuceneSearchEngine
 */
public class LuceneSearchEngine implements SearchEngine
{
    /**
     * Return search results
     * @param strQuery The search query
     * @return Results as a collection of SearchResult
     */
    public List<SearchResult> getSearchResults( String strQuery )
    {
        ArrayList<SearchItem> listResults = new ArrayList<SearchItem>(  );
        Searcher searcher = null;

        try
        {
            searcher = new IndexSearcher( IndexationService.getIndex(  ) );

            Query query = null;
            QueryParser parser = new QueryParser( SearchItem.FIELD_CONTENTS, IndexationService.getAnalyser(  ) );
            query = parser.parse( ( strQuery != null ) ? strQuery : "" );

            // Get results documents
            Hits hits = searcher.search( query );
            Iterator i = hits.iterator(  );

            while ( i.hasNext(  ) )
            {
                Hit hit = (Hit) i.next(  );
                Document document = hit.getDocument(  );
                SearchItem si = new SearchItem( document );
                listResults.add( si );
            }

            searcher.close(  );
        }
        catch ( Exception e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }

        return convertList( listResults );
    }
    
    
    private List<SearchResult> convertList(List<SearchItem> listSource )
    {
        List<SearchResult> listDest = new ArrayList<SearchResult>();
        for( SearchItem item : listSource )
        {
            SearchResult result = new SearchResult();
            result.setId( item.getId());
            result.setDate( item.getDate());
            result.setUrl( item.getUrl());
            result.setTitle( item.getTitle());
            result.setSummary( item.getSummary());
            result.setType( item.getType());
            listDest.add( result );
        }
        return listDest;
    }
    
}
