package fr.paris.lutece.portal.web.includes;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.paris.lutece.portal.service.plugin.PluginDefaultImplementation;

public final class LinksIncludeTestPlugin extends PluginDefaultImplementation
{

    private Map<Integer, List<String>> _listCssStyleSheets = new HashMap<>( );
    private Map<Integer, List<String>> _listJavascriptFiles = new HashMap<>( );
    private boolean _cssStyleSheetsScopePortal;
    private boolean _cssStylesheetsScopeXPage;
    private boolean _javascriptFilesScopePortal;
    private boolean _javascriptFilesScopeXPage;

    @Override
    public List<String> getCssStyleSheets( )
    {
        List<String> res = _listCssStyleSheets.get( null );

        if ( res == null )
        {
            return Collections.emptyList( );
        }

        return res;
    }

    @Override
    public List<String> getCssStyleSheets( int mode )
    {
        List<String> res = _listCssStyleSheets.get( mode );

        if ( res == null )
        {
            return Collections.emptyList( );
        }

        return res;
    }

    @Override
    public List<String> getJavascriptFiles( )
    {
        List<String> res = _listJavascriptFiles.get( null );

        if ( res == null )
        {
            return Collections.emptyList( );
        }

        return res;
    }

    @Override
    public List<String> getJavascriptFiles( int mode )
    {
        List<String> res = _listJavascriptFiles.get( mode );

        if ( res == null )
        {
            return Collections.emptyList( );
        }

        return res;
    }

    public void setCssStyleSheetsScopePortal( boolean bScope )
    {
        _cssStyleSheetsScopePortal = bScope;
    }

    @Override
    public boolean isCssStylesheetsScopePortal( )
    {
        return _cssStyleSheetsScopePortal;
    }

    public void setCssStylesheetsScopeXPage( boolean bScope )
    {
        _cssStylesheetsScopeXPage = bScope;
    }

    @Override
    public boolean isCssStylesheetsScopeXPage( )
    {
        return _cssStylesheetsScopeXPage;
    }

    public void setJavascriptFilesScopePortal( boolean bScope )
    {
        _javascriptFilesScopePortal = bScope;
    }

    @Override
    public boolean isJavascriptFilesScopePortal( )
    {
        return _javascriptFilesScopePortal;
    }

    public void setJavascriptFilesScopeXPage( boolean bScope )
    {
        _javascriptFilesScopeXPage = bScope;
    }

    @Override
    public boolean isJavascriptFilesScopeXPage( )
    {
        return _javascriptFilesScopeXPage;
    }

    public void setCssStyleSheets( int mode, List<String> styleSheets )
    {
        _listCssStyleSheets.put( mode, styleSheets );
    }

    public void setCssStyleSheets( List<String> styleSheets )
    {
        _listCssStyleSheets.put( null, styleSheets );
    }

    public void setJavascriptFiles( int mode, List<String> javascriptFiles )
    {
        _listJavascriptFiles.put( mode, javascriptFiles );
    }

    public void setJavascriptFiles( List<String> javascriptFiles )
    {
        _listJavascriptFiles.put( null, javascriptFiles );
    }

}
