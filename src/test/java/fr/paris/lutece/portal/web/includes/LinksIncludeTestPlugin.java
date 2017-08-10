/*
 * Copyright (c) 2002-2017, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
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
