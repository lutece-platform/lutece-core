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
package fr.paris.lutece.portal.service.content;

import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.security.UserNotSignedException;

import javax.servlet.http.HttpServletRequest;


/**
 * Interface for pages provider services.
 */
public abstract class ContentService extends AbstractCacheableService
{
    private String _strPluginName;

    /**
     * Returns the HTML (or XML) code for a page for a given mode. The Service should use request parameters to
     * identify the page content to provide.
     *
     * @param request The HTTP request containing content parameters
     * @param nMode The current mode.
     * @return The HTML (or XML) code of the page.
     * @throws UserNotSignedException the UserNotSignedException
     * @throws SiteMessageException occurs when a site message need to be displayed
     */
    public abstract String getPage( HttpServletRequest request, int nMode )
        throws UserNotSignedException, SiteMessageException;

    /**
     * Analyzes request's parameters to see if the request should be handled by the current Content Service
     *
     * @param request The HTTP request
     * @return true if this ContentService should handle this request
     */
    public abstract boolean isInvoked( HttpServletRequest request );

    /**
     * Return the name of the plugin
     *
     * @return The name of the plugin
     */
    public String getPluginName(  )
    {
        return _strPluginName;
    }

    /**
     * Set the plugin name of the content service
     *
     * @param strPluginName the plugin name
     */
    public void setPluginName( String strPluginName )
    {
        _strPluginName = strPluginName;
    }
}
