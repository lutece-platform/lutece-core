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
package fr.paris.lutece.portal.service.resource;

import java.util.Locale;


/**
 *
 * This interface is used to fetch the {@link IExtendableResource}.
 * The implementations are developed in each plugins that have a
 * {@link IExtendableResource}.
 *
 */
public interface IExtendableResourceService
{
    /**
     * Checks if is invoked.
     *
     * @param strResourceType the str resource type
     * @return true, if is invoked
     */
    boolean isInvoked( String strResourceType );

    /**
     * Gets the resource.
     *
     * @param strIdResource the str id resource
     * @param strResourceType the str resource type
     * @return the resource
     */
    IExtendableResource getResource( String strIdResource, String strResourceType );

    /**
     * Gets the resource type.
     * @return the resource type.
     */
    String getResourceType(  );

    /**
     * Gets the description of the resource type.
     * @param locale The locale to use
     * @return the description of the resource type.
     */
    String getResourceTypeDescription( Locale locale );

    /**
     * Get the main URL to access a resource
     * @param strIdResource The id of the resource
     * @param strResourceType The type of a resource
     * @return The main URL to access a resource, or null if the resource has no
     *         main URL
     */
    String getResourceUrl( String strIdResource, String strResourceType );
}
