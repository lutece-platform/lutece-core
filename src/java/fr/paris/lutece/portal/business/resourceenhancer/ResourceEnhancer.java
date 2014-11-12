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
package fr.paris.lutece.portal.business.resourceenhancer;

import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * ResourceEnhancer : handles {@link IResourceDisplayManager} and {@link IResourceManager}
 */
public final class ResourceEnhancer
{
    /**
     * Empty constructor
     */
    private ResourceEnhancer(  )
    {
        // nothing
    }

    // IResourceDisplayManager facade

    /**
     * Add to the the XML String additional datas
     * @param strXml The xml string use by stylesheet
     * @param strResourceType the resource type
     * @param nResourceId The resource Id
     */
    public static void getXmlAddOn( StringBuffer strXml, String strResourceType, int nResourceId )
    {
        List<IResourceDisplayManager> managers = SpringContextService.getBeansOfType( IResourceDisplayManager.class );

        for ( IResourceDisplayManager manager : managers )
        {
            manager.getXmlAddOn( strXml, strResourceType, nResourceId );
        }
    }

    /**
     * Add datas to the model use by document template
     *
     * @param model The model use by document template
     * @param strResourceType the ressource Type
     * @param nIdResource The resource id
     * @param strPortletId The portlet ID
     * @param request The HTTP Request
     */
    public static void buildPageAddOn( Map<String, Object> model, String strResourceType, int nIdResource,
        String strPortletId, HttpServletRequest request )
    {
        List<IResourceDisplayManager> managers = SpringContextService.getBeansOfType( IResourceDisplayManager.class );

        for ( IResourceDisplayManager manager : managers )
        {
            manager.buildPageAddOn( model, strResourceType, nIdResource, strPortletId, request );
        }
    }

    // IResourceManage facade

    /**
     * Add datas to the create document model use in the template
     * @param model the map use in the template
     */
    public static void getCreateResourceModelAddOn( Map<String, Object> model )
    {
        List<IResourceManager> managers = SpringContextService.getBeansOfType( IResourceManager.class );

        for ( IResourceManager manager : managers )
        {
            manager.getCreateResourceModelAddOn( model );
        }
    }

    /**
     * Perform actions associated to the document creation
     * @param request The HTTP request
     * @param strResourceType the resource type
     * @param nResourceId the resource id
     */
    public static void doCreateResourceAddOn( HttpServletRequest request, String strResourceType, int nResourceId )
    {
        List<IResourceManager> managers = SpringContextService.getBeansOfType( IResourceManager.class );

        for ( IResourceManager manager : managers )
        {
            manager.doCreateResourceAddOn( request, strResourceType, nResourceId );
        }
    }

    /**
     * Add datas to the modify document model use in the template
     * @param model the map use in the template
     * @param strResourceType the resource type
     * @param nResourceId the resource id
     */
    public static void getModifyResourceModelAddOn( Map<String, Object> model, String strResourceType, int nResourceId )
    {
        List<IResourceManager> managers = SpringContextService.getBeansOfType( IResourceManager.class );

        for ( IResourceManager manager : managers )
        {
            manager.getModifyResourceModelAddOn( model, strResourceType, nResourceId );
        }
    }

    /**
     * Perform actions associated to the document modification
     * @param request The HTTP request
     * @param strResourceType the resource type
     * @param nResourceId the resource id
     */
    public static void doModifyResourceAddOn( HttpServletRequest request, String strResourceType, int nResourceId )
    {
        List<IResourceManager> managers = SpringContextService.getBeansOfType( IResourceManager.class );

        for ( IResourceManager manager : managers )
        {
            manager.doModifyResourceAddOn( request, strResourceType, nResourceId );
        }
    }

    /**
     * Perform actions associated to the document deletion
     * @param request The HTTP request
     * @param strResourceType the resource type
     * @param nResourceId the resource id
     */
    public static void doDeleteResourceAddOn( HttpServletRequest request, String strResourceType, int nResourceId )
    {
        List<IResourceManager> managers = SpringContextService.getBeansOfType( IResourceManager.class );

        for ( IResourceManager manager : managers )
        {
            manager.doDeleteResourceAddOn( request, strResourceType, nResourceId );
        }
    }

    /**
     * Perform actions associated to the document download
     * @param request The HTTP request
     * @param strResourceType the resource type
     * @param nResourceId the resource id
     */
    public static void doDownloadResourceAddOn( HttpServletRequest request, String strResourceType, int nResourceId )
    {
        List<IResourceManager> managers = SpringContextService.getBeansOfType( IResourceManager.class );

        for ( IResourceManager manager : managers )
        {
            manager.doDownloadResourceAddOn( request, strResourceType, nResourceId );
        }
    }
}
