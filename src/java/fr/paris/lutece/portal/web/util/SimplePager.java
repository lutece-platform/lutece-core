package fr.paris.lutece.portal.web.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import fr.paris.lutece.util.html.AbstractPaginator;
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
    public <T> Map<String, Object> getPaginatedListModel( HttpServletRequest request, Locale locale )
    {
        return getPaginatedListModel( request, null, locale );
    }

    @Override
    public <T> Map<String, Object> getPaginatedListModel( HttpServletRequest request, Function<List<S>, List<T>> delegate, Locale locale )
    {
        _strCurrentPageIndex = AbstractPaginator.getPageIndex( request, AbstractPaginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nItemsPerPage = AbstractPaginator.getItemsPerPage( request, AbstractPaginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage, _nDefaultItemsPerPage );

        UrlItem url = new UrlItem( _strBaseUrl );
        String strUrl = url.getUrl( );

        LocalizedPaginator<S> paginator = new LocalizedPaginator<S>( _listId, _nItemsPerPage, strUrl, AbstractPaginator.PARAMETER_PAGE_INDEX,
                _strCurrentPageIndex, locale );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_NB_ITEMS_PER_PAGE, String.valueOf( _nItemsPerPage ) );
        model.put( MARK_PAGINATOR, paginator );
        if ( null != delegate )
        {
            model.put( _strListBookmark, delegate.apply( paginator.getPageItems( ) ) );
        }
        else
        {
            model.put( _strListBookmark, paginator.getPageItems( ) );
        }
        return model;
    }

    @Override
    public void setIdList( List<S> listItems )
    {
        _listId = listItems;
        _strCurrentPageIndex = null;
    }

    @Override
    public void setList( List<S> listItems )
    {
        _listId = listItems;
        _strCurrentPageIndex = null;
    }

    @Override
    public void setBaseUrl( String strBaseUrl )
    {
        _strBaseUrl = strBaseUrl;
    }

}
