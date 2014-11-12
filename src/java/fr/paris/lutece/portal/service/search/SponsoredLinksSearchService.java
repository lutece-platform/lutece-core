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
package fr.paris.lutece.portal.service.search;

import fr.paris.lutece.portal.service.spring.SpringContextService;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.util.Locale;


/**
 * Default sponsored links search service implementation
 */
public class SponsoredLinksSearchService implements ISponsoredLinksSearchService
{
    private static final String EMPTY_STRING = "";
    private static final String SPRING_CONTEXT_NAME = "sponsoredlinks";
    private boolean _bAvailable;
    private ISponsoredLinksService _sponsoredLinksService;

    /**
     * Default constructor.
     *
     * Gets the sponsoredLinksService from the sponsoredlinks plugin
     * If the service is missing, sets available to false
     */
    public SponsoredLinksSearchService(  )
    {
        try
        {
            //first check if the sponsoredlinks service bean is available
            _sponsoredLinksService = SpringContextService.getBean( "sponsoredlinks.sponsoredLinksService" );
            _bAvailable = _sponsoredLinksService != null;
        }
        catch ( BeanDefinitionStoreException e )
        {
            _bAvailable = false;
        }
        catch ( NoSuchBeanDefinitionException e )
        {
            _bAvailable = false;
        }
        catch ( CannotLoadBeanClassException e )
        {
            _bAvailable = false;
        }
    }

    /**
    * {@inheritDoc}
    */
    public String getHtmlCode( String strRequest, Locale locale )
    {
        if ( _bAvailable )
        {
            return _sponsoredLinksService.getHtmlCode( strRequest, locale );
        }
        else
        {
            return EMPTY_STRING;
        }
    }

    /**
    * {@inheritDoc}
    */
    public boolean isAvailable(  )
    {
        return _bAvailable;
    }
}
