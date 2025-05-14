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
package fr.paris.lutece.portal.web.util;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 * @param <S>
 * @param <T>
 */
public interface IPager<S, T>
{
    /**
     * Gets the name
     * 
     * @return The name of the pager
     */
    String getName( );

    /**
     * Returns the paged list model based on the List provided through initList method.
     * 
     * @param <T>
     * @param request
     * @param locale
     * @return
     */
    <T> Map<String, Object> getPaginatedListModel( HttpServletRequest request, Locale locale );

    /**
     * Returns the paged list model based on the List provided through initIdList method and the delegate function to load the objects with their ids.
     * 
     * @param <T>
     * @param request
     * @param delegate
     * @param locale
     * @return
     */
    <T> Map<String, Object> getPaginatedListModel( HttpServletRequest request, Function<List<S>, List<T>> delegate, Locale locale );

    /**
     * Set the list of IDs <S>. This list will be used to load the objects <T> through getPaginatedListModel method with a delegate on the concrete method that
     * will load the complete objects.
     * 
     * @param listIds
     */
    void setIdList( List<S> listIds );

    /**
     * Set the list of items. In that case, no delegate will be involved during the retrieval of a page. This method should be used when the pager is of type
     * IPager<S, Void>.
     * 
     * @param listItems
     */
    void setList( List<S> listItems );

    /**
     * Allows to update the base url of the pager in case this url should be dynamic.
     * 
     * @param strBaseUrl
     */
    void setBaseUrl( String strBaseUrl );
}
