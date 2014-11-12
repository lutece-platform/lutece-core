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
package fr.paris.lutece.portal.web.util;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.util.html.DelegatePaginator;

import java.util.List;
import java.util.Locale;


/**
 * Localized Delegate Paginator.
 *
 * @param <E> the element type
 * @see LocalizedPaginator
 */
public class LocalizedDelegatePaginator<E> extends DelegatePaginator<E>
{
    private static final long serialVersionUID = -2433297121331132765L;
    private static final String KEY_FIRST = "portal.util.labelFirst";
    private static final String KEY_PREVIOUS = "portal.util.labelPrevious";
    private static final String KEY_NEXT = "portal.util.labelNext";
    private static final String KEY_LAST = "portal.util.labelLast";
    private static final String KEY_ITEM_COUNT = "portal.util.labelItemCount";
    private static final String KEY_ITEM_COUNT_PER_PAGE = "portal.util.labelItemCountPerPage";
    private Locale _locale;

    /**
     * Instantiates a new localized delegate paginator.
     *
     * @param list The collection to paginate
     * @param nItemPerPage Number of items to display per page
     * @param strBaseUrl The base Url for build links on each page link
     * @param strPageIndexParameterName The parameter name for the page index
     * @param strPageIndex The current page index
     * @param nItemsCount total items
     * @param locale The Locale
     */
    public LocalizedDelegatePaginator( List<E> list, int nItemPerPage, String strBaseUrl,
        String strPageIndexParameterName, String strPageIndex, int nItemsCount, Locale locale )
    {
        super( list, nItemPerPage, strBaseUrl, strPageIndexParameterName, strPageIndex, nItemsCount );
        _locale = locale;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabelFirst(  )
    {
        return I18nService.getLocalizedString( KEY_FIRST, _locale );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getLabelPrevious(  )
    {
        return I18nService.getLocalizedString( KEY_PREVIOUS, _locale );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getLabelNext(  )
    {
        return I18nService.getLocalizedString( KEY_NEXT, _locale );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getLabelLast(  )
    {
        return I18nService.getLocalizedString( KEY_LAST, _locale );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getLabelItemCount(  )
    {
        return I18nService.getLocalizedString( KEY_ITEM_COUNT, _locale );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getLabelItemCountPerPage(  )
    {
        return I18nService.getLocalizedString( KEY_ITEM_COUNT_PER_PAGE, _locale );
    }
}
