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
import java.util.Optional;
import java.util.function.Function;

import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.util.function.SerializableFunction;
import fr.paris.lutece.util.html.IPaginator;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Interface representing a pager for managing pagination of items.
 *
 * @param <S> The type of source objects (e.g., IDs or items).
 * @param <T> The type of target objects (e.g., fully loaded objects).
 */
public interface IPager<S, T>
{
    /**
	 * Allows to update the base url of the pager in case this url should be dynamic.
	 * 
	 * @return The IPager instance with the updated base URL
	 */
    IPager<S, T> withBaseUrl(String baseUrl);
    /**
    * Sets the default number of items per page.
    *
    * @param defaultItemsPerPage The default number of items per page.
    * @return The updated IPager instance.
    */
    IPager<S, T> withItemsPerPage(int defaultItemsPerPage);
    /**
     * Sets the list of IDs of type <S>. This list will be used to load the objects of type <T>
     * through the `getPaginatedListModel` method with a delegate function.
     *
     * @param idList The list of IDs to set.
     * @return The updated IPager instance.
     */
    IPager<S, T> withIdList(List<S> idList);
    /**
     * Sets the list of items directly. This method should be used when the pager is of type
     * `IPager<S, Void>` and no delegate function is involved during page retrieval.
     *
     * @param listItem The list of items to set.
     * @return The updated IPager instance.
     */
    IPager<S, T> withListItem(List<S> listItem);
 
    /**
     * Populates the given Models object with pagination data.
     *
     * @param request The HTTP request object.
     * @param models The Models object to populate.
     * @param locale The locale for internationalization.
     */
    IPaginator<S> populateModels(HttpServletRequest request, Models models, Locale locale);

    /**
     * Populates the given Models object with pagination data using a delegate function.
     *
     * @param request The HTTP request object.
     * @param models The Models object to populate.
     * @param delegate A function that takes a list of source objects and returns a list of target objects.
     * @param locale The locale for internationalization.
     */
    IPaginator<S> populateModels(HttpServletRequest request, Models models, SerializableFunction<List<S>, List<T>> delegate, Locale locale);

    
    /**
     * Retrieves the optional paginator instance associated with this pager.
     *
     * @return An {@link Optional} containing the {@link IPaginator} instance if available, or an empty {@link Optional} if not.
     */
    Optional<IPaginator<S>> getPaginator();
}