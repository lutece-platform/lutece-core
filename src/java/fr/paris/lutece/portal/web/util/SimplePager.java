package fr.paris.lutece.portal.web.util;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.util.html.AbstractPaginator;
import fr.paris.lutece.util.html.IPaginator;
import fr.paris.lutece.util.url.UrlItem;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Simple implementation of the IPager
 * 
 * @param <S>
 * @param <T>
 */
public class SimplePager<S, T> implements IPager<S, T>, Serializable
{
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";

    private final String _strName;
    private String _strListBookmark;
    private String _strBaseUrl;
    private String _strCurrentPageIndex;
    private int _nDefaultItemsPerPage;
    private int _nItemsPerPage;
    private List<S> _listId;
    private IPaginator<S> paginator;
    private Function<List<S>, List<T>> _delegate ;
    

    public String getName( )
    {
        return _strName;
    }

    public SimplePager( String strName, String strListBookmark, String strBaseUrl, int nDefaultItemsPerPage )
    {
        _strName = strName;
        _strListBookmark = strListBookmark;
        _strBaseUrl = strBaseUrl;
        _nDefaultItemsPerPage = nDefaultItemsPerPage;
    }
	
	@Override
	public IPager<S, T> withBaseUrl(String baseUrl) {
		 this._strBaseUrl = baseUrl;
         return this;
	}

	@Override
	public IPager<S, T> withItemsPerPage(int defaultItemsPerPage) {
		if (defaultItemsPerPage <= 0) {
            throw new IllegalArgumentException("Default items per page must be positive");
        }
		this._nDefaultItemsPerPage = defaultItemsPerPage;
         return this;
	}

	@Override
	public IPager<S, T> withIdList(List<S> idList) {
		 this._listId = idList;
         this._strCurrentPageIndex = null;
         return this;
	}
	@Override
	public IPager<S, T> withListItem(List<S> list) {
		 this._listId = list;
         this._strCurrentPageIndex = null;
         return this;
	}

	@Override
	public IPaginator<S> populateModels(HttpServletRequest request, Models models, Locale locale) {
        return populateModels(request, models, null, locale);
	}

	@Override
	public IPaginator<S> populateModels(HttpServletRequest request, Models models, Function<List<S>, List<T>> delegate,
			Locale locale) {

        Objects.requireNonNull(request, "HttpServletRequest cannot be null");
        Objects.requireNonNull(models, "Models cannot be null");
        if (_listId == null || _listId.isEmpty()) {
            models.put(_strListBookmark, Collections.emptyList())
                  .put(MARK_PAGINATOR, null)
                  .put(MARK_NB_ITEMS_PER_PAGE, "0");
            return null;
        }

		_strCurrentPageIndex = AbstractPaginator.getPageIndex(request, AbstractPaginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex);
		_nItemsPerPage = AbstractPaginator.getItemsPerPage(request, AbstractPaginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage, _nDefaultItemsPerPage);

         String strUrl = buildUrl();
         paginator = new LocalizedPaginator<S>( 
        		 _listId,
        		 _nItemsPerPage,
        		 strUrl, 
        		 AbstractPaginator.PARAMETER_PAGE_INDEX,
                 _strCurrentPageIndex, 
                 locale );
         models.put(MARK_NB_ITEMS_PER_PAGE, String.valueOf(_nItemsPerPage))
               .put(MARK_PAGINATOR, paginator);

         _delegate = delegate;
         List<?> resultList = delegate != null ?
             delegate.apply(paginator.getPageItems()) :
             paginator.getPageItems();

         models.put(_strListBookmark, resultList);
        
         return paginator;
	}
	
	public List<?> getItemsPerPage( int nPage){
		if( paginator == null ) {
			return Collections.emptyList( );
		}
		List<?> resultList = _delegate != null ?
	             _delegate.apply(paginator.getPageItems(nPage)) :
	             paginator.getPageItems(nPage);
		
		return resultList;
	}
	/**
	 * Builds the optimized URL for the pager.
	 * 
	 * @return The optimized URL as a String
	 */
    private String buildUrl() {
        if (_strBaseUrl == null) {
            return "";
        }
        return _strBaseUrl.contains("?") ? _strBaseUrl : new UrlItem(_strBaseUrl).getUrl();
    }

	@Override
	public Optional<IPaginator<S>> getPaginator() {
		
		return Optional.ofNullable(paginator);
	}
}
