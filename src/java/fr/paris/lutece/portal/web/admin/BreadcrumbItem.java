/*
 * Copyright (c) 2002-2025, City of Paris
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
package fr.paris.lutece.portal.web.admin;

import java.io.Serializable;

/**
 * An item of the breadcrumbs displayed in the header of an admin page, between the feature title and the page title
 */
public class BreadcrumbItem implements Serializable
{
    private static final long serialVersionUID = 4263712365419270451L;

    private final String _strTitle;
    private final String _strUrl;

    /**
     * Constructor
     *
     * @param strTitle
     *            the title of the item. It is displayed as is, like the page title : a value entered by a user must be escaped by the caller
     * @param strUrl
     *            the URL of the item, or null if the item is not a link
     */
    public BreadcrumbItem( String strTitle, String strUrl )
    {
        _strTitle = strTitle;
        _strUrl = strUrl;
    }

    /**
     * Returns the title of the item
     *
     * @return the title
     */
    public String getTitle( )
    {
        return _strTitle;
    }

    /**
     * Returns the URL of the item
     *
     * @return the URL, or null if the item is not a link
     */
    public String getUrl( )
    {
        return _strUrl;
    }
}
