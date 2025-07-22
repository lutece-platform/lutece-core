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
package fr.paris.lutece.portal.business.style;

import jakarta.enterprise.inject.spi.CDI;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.portal.service.style.IStyleService;

/**
 * This class provides instances management methods (create, find, ...) for PageTemplate objects
 */
@Deprecated( since = "8.0", forRemoval = true )
public final class PageTemplateHome
{
    // Static variable pointed at the DAO instance
    private static IPageTemplateRepository _repository = CDI.current( ).select( IPageTemplateRepository.class ).get( );

    /**
     * Creates a new PageTemplateHome object.
     */
    private PageTemplateHome( )
    {
    }

    /**
     * Return the list of all the page templates
     *
     * @deprecated From the core use {@code @Inject} to obtain the {@link IPageTemplateRepository} instance and access the method
     *             {@link IPageTemplateRepository#findAll()}. 
     *             From a plugin use {@code @Inject} to obtain the {@link IStyleService} instance and access the
     *             method {@link IStyleService#findPageTemplates()}. 
     *             This method will be removed in future versions.
     * 
     * @return A collection of page templates objects
     */
    @Deprecated( since = "8.0", forRemoval = true )
    public static List<PageTemplate> getPageTemplatesList( )
    {
        return new ArrayList<PageTemplate>( _repository.findAll( ) );
    }

}
