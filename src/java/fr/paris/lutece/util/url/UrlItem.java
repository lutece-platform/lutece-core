/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.util.url;

import org.apache.commons.lang.StringUtils;

// Java Util
import java.util.ArrayList;
import java.util.List;


/**
 * This class provides utility methods for the generation of Url String
 */
public class UrlItem
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String ANCHOR_DELIMITER = "#";

    /** the root of the url. */
    private String _strRoot;

    /** the list of parameters. */
    private List<UrlParameterItem> _listParameters = new ArrayList<UrlParameterItem>(  );
    private String _strAnchor;

    /**
     * Constructs an url with no parameters.
     * @param strRoot The url's root.
     */
    public UrlItem( String strRoot )
    {
        _strRoot = strRoot;
    }

    /**
     * Add a Parameter to the url.
     * @param strName The name of the parameter.
     * @param strValue The value of the parameter.
     */
    public void addParameter( String strName, String strValue )
    {
        _listParameters.add( new UrlParameterItem( strName, strValue ) );
    }

    /**
     * Add a Parameter to the url.
     * @param strName The name of the parameter.
     * @param nValue The value of the parameter.
     */
    public void addParameter( String strName, int nValue )
    {
        _listParameters.add( new UrlParameterItem( strName, nValue ) );
    }

    /**
     * Return the url string.
     * @return String The url string.
     */
    public String getUrl(  )
    {
        StringBuilder urlCode = new StringBuilder( _strRoot );

        boolean bFirst = ( _strRoot.indexOf( '?' ) == -1 );

        for ( UrlParameterItem parameter : _listParameters )
        {
            // Add a ? or & to the root url if it does already contains one
            urlCode.append( parameter.getCode( bFirst ) );
            bFirst = false;
        }

        if ( ( getAnchor(  ) != null ) && !getAnchor(  ).equals( StringUtils.EMPTY ) )
        {
            urlCode.append( ANCHOR_DELIMITER );
            urlCode.append( getAnchor(  ) );
        }

        return urlCode.toString(  );
    }

    /**
     * Return the url string.
     * @return String The url string with entity code.
     */
    public String getUrlWithEntity(  )
    {
        StringBuilder urlCode = new StringBuilder( _strRoot );

        boolean bFirst = ( _strRoot.indexOf( '?' ) == -1 );

        for ( UrlParameterItem parameter : _listParameters )
        {
            // Add a ? or & to the root url if it does already contains one
            urlCode.append( parameter.getCodeEntity( bFirst ) );
            bFirst = false;
        }

        if ( StringUtils.isNotEmpty( getAnchor(  ) ) )
        {
            urlCode.append( ANCHOR_DELIMITER + getAnchor(  ) );
        }

        return urlCode.toString(  );
    }

    /**
     * Get the anchor
     * @return the _srtAnchor
     */
    public String getAnchor(  )
    {
        return _strAnchor;
    }

    /**
     * Set the anchor
     * @param strAnchorName the _srtAnchor to set
     */
    public void setAnchor( String strAnchorName )
    {
        _strAnchor = strAnchorName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString(  )
    {
        return getUrl(  );
    }
}
