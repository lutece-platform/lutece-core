/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
package fr.paris.lutece.portal.web.resource;

import fr.paris.lutece.portal.service.resource.IResourceProvider;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * ResourceComponentManager
 *
 */
public final class ResourceComponentManager
{
	/**
     * Empty constructor
     */
    private ResourceComponentManager(  )
    {
    }

    /**
     * Builds the xml add on.
     *
     * @param strResourceType the str resource type
     * @param strXml the str xml
     * @param mapParameters the map parameters
     */
    public static void buildXmlAddOn( String strResourceType, StringBuffer strXml, Map<String, String[]> mapParameters )
    {
        for ( IResourceComponent component : SpringContextService.getBeansOfType( IResourceComponent.class ) )
        {
        	if ( component.isInvoked( strResourceType ) )
        	{
        		component.buildXmlAddOn( strResourceType, strXml, mapParameters );
        	}
        }
    }

    /**
     * Builds the page add on.
     *
     * @param strResourceType the str resource type
     * @param model the model
     * @param mapParameters the map parameters
     * @param request the request
     */
    public static void buildPageAddOn( String strResourceType, Map<String, Object> model, Map<String, String[]> mapParameters, HttpServletRequest request )
    {
        for ( IResourceComponent component : SpringContextService.getBeansOfType( IResourceComponent.class ) )
        {
        	if ( component.isInvoked( strResourceType ) )
        	{
        		component.buildPageAddOn( strResourceType, model, mapParameters, request );
        	}
        }
    }

    /**
     * Do process.
     *
     * @param strResourceType the str resource type
     * @param request the request
     * @param mapParameters the map parameters
     * @param nMode the n mode
     */
    public static void doProcess( String strResourceType, HttpServletRequest request, Map<String, String[]> mapParameters, int nMode )
    {
    	for ( IResourceProvider provider : SpringContextService.getBeansOfType( IResourceProvider.class ) )
        {
        	if ( provider.isInvoked( strResourceType ) )
        	{
        		provider.doProcess( strResourceType, request, mapParameters, nMode );
        	}
        }
    }
}
