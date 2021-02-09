/*
 * Copyright (c) 2002-2021, City of Paris
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
package fr.paris.lutece.portal.service.html;

import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * This class provides management methods for cleaner
 */
public final class HtmlCleanerService
{
    /** html Cleaner */
    private static IHtmlCleaner _htmlCleaner = SpringContextService.getBean( "htmlCleaner" );
    private static boolean _bInit;

    /**
     * Constructor. Creates a new HtmlCleanerService object.
     */
    private HtmlCleanerService( )
    {
    }

    /**
     * Clean HTML code and converts it to XHTML
     *
     * @param strSource
     *            The input string to clean
     * @return The cleaned string
     */
    public static synchronized String clean( String strSource )
    {
        init( );

        return _htmlCleaner.clean( strSource );
    }

    /**
     * Clean HTML code and converts it to text
     *
     * @param strSource
     *            The input string to clean
     * @return The cleaned string
     */
    public static synchronized String text( String strSource )
    {
        init( );

        return _htmlCleaner.text( strSource );
    }

    private static void init( )
    {
        // init htmlCleaner
        if ( !_bInit )
        {
            _htmlCleaner.init( );
            _bInit = true;
        }
    }
}
