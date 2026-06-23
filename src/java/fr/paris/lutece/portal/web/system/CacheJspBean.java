/*
 * Copyright (c) 2002-2022, City of Paris
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
package fr.paris.lutece.portal.web.system;

import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.cache.CacheService;
import fr.paris.lutece.portal.service.cache.CacheableService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage the caches through the admin MVC front-controller.
 */
@RequestScoped
@Named
@Controller( name = "cache", right = "CORE_CACHE_MANAGEMENT" )
public class CacheJspBean extends MVCAdminJspBean
{
    // Right
    public static final String RIGHT_CACHE_MANAGEMENT = "CORE_CACHE_MANAGEMENT";

    private static final long serialVersionUID = 7010476999488231065L;

    // Templates
    private static final String TEMPLATE_MANAGE_CACHES = "admin/system/manage_caches.html";
    private static final String TEMPLATE_CACHE_INFOS = "admin/system/cache_infos.html";

    // Views
    private static final String VIEW_MANAGE_CACHES = "manageCaches";
    private static final String VIEW_CACHE_INFOS = "cacheInfos";
    private static final String VIEW_CONFIRM_TOGGLE_CACHE = "confirmToggleCache";

    // Actions
    private static final String ACTION_RESET_CACHES = "resetCaches";
    private static final String ACTION_TOGGLE_CACHE = "toggleCache";

    // Markers
    private static final String MARK_SERVICES_LIST = "services_list";

    // Parameters
    private static final String PARAMETER_ID_CACHE = "id_cache";

    // Messages
    private static final String PROPERTY_MESSAGE_CONFIRM_TOOGLE_CACHE = "portal.system.message.confirmToggleCache";
    private static final String PROPERTY_MESSAGE_CONFIRM_TOOGLE_CACHE_TITLE = "portal.system.message.confirmToggleCacheTitle";
    private static final String PROPERTY_MESSAGE_INVALID_CACHE_ID = "portal.system.message.invalidCacheId";

    @Inject
    private Models _models;

    /**
     * Returns the page to manage caches.
     *
     * @param request
     *            The HttpServletRequest
     * @return The HTML code of the manage caches page
     */
    @View( value = VIEW_MANAGE_CACHES, defaultView = true )
    public String getManageCaches( HttpServletRequest request )
    {
        _models.put( MARK_SERVICES_LIST, CacheService.getCacheableServicesList( ) );
        _models.put( SecurityTokenService.MARK_TOKEN, getSecurityTokenService( ).getToken( request, ACTION_RESET_CACHES ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_CACHES, getLocale( ), _models );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Returns the cache infos page for one cache or for all caches.
     *
     * @param request
     *            The HttpServletRequest
     * @return The HTML code of the cache infos page
     */
    @View( VIEW_CACHE_INFOS )
    public String getCacheInfos( HttpServletRequest request )
    {
        List<CacheableService<?, ?>> list;
        String strCacheIndex = request.getParameter( PARAMETER_ID_CACHE );

        if ( strCacheIndex != null )
        {
            int nCacheIndex = Integer.parseInt( strCacheIndex );
            CacheableService<?, ?> cs = CacheService.getCacheableServicesList( ).get( nCacheIndex );
            list = new ArrayList<>( );
            list.add( cs );
        }
        else
        {
            list = CacheService.getCacheableServicesList( );
        }

        _models.put( MARK_SERVICES_LIST, list );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CACHE_INFOS, getLocale( ), _models );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Returns the confirmation page before changing the activation of a cache.
     *
     * @param request
     *            The Http Request
     * @return the confirmation message URL, the redirection being committed by the framework
     */
    @View( VIEW_CONFIRM_TOGGLE_CACHE )
    public String getConfirmToggleCache( HttpServletRequest request )
    {
        String strCacheIndex = request.getParameter( PARAMETER_ID_CACHE );

        if ( strCacheIndex != null )
        {
            int nCacheIndex = Integer.parseInt( strCacheIndex );
            CacheableService<?, ?> cs = CacheService.getCacheableServicesList( ).get( nCacheIndex );

            if ( cs != null )
            {
                Object [ ] messageArgs = {
                        cs.getName( )
                };

                Map<String, Object> parameters = new HashMap<>( );
                parameters.put( PARAMETER_ID_CACHE, strCacheIndex );
                parameters.put( SecurityTokenService.PARAMETER_TOKEN, getSecurityTokenService( ).getToken( request, ACTION_TOGGLE_CACHE ) );

                return redirect( request, AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_CONFIRM_TOOGLE_CACHE, messageArgs,
                        PROPERTY_MESSAGE_CONFIRM_TOOGLE_CACHE_TITLE, getActionUrl( ACTION_TOGGLE_CACHE ), "", AdminMessage.TYPE_CONFIRMATION, parameters ) );
            }
        }

        return redirect( request, AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_INVALID_CACHE_ID, AdminMessage.TYPE_ERROR ) );
    }

    /**
     * Processes the cache reset for one cache or for all caches.
     *
     * @param request
     *            The HTTP request
     * @return the redirection to the manage caches view
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Action( ACTION_RESET_CACHES )
    public String doResetCaches( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !getSecurityTokenService( ).validate( request, ACTION_RESET_CACHES ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }

        String strCacheIndex = request.getParameter( PARAMETER_ID_CACHE );

        if ( strCacheIndex != null )
        {
            int nCacheIndex = Integer.parseInt( strCacheIndex );
            CacheableService<?, ?> cs = CacheService.getCacheableServicesList( ).get( nCacheIndex );
            cs.resetCache( );
        }
        else
        {
            CacheService.resetCaches( );
            AppTemplateService.resetCache( );
            I18nService.resetCache( );
        }

        return redirectView( request, VIEW_MANAGE_CACHES );
    }

    /**
     * Processes the cache toggle on/off.
     *
     * @param request
     *            The HTTP request
     * @return the redirection to the manage caches view
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Action( ACTION_TOGGLE_CACHE )
    public String doToggleCache( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !getSecurityTokenService( ).validate( request, ACTION_TOGGLE_CACHE ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }

        String strCacheIndex = request.getParameter( PARAMETER_ID_CACHE );

        if ( strCacheIndex != null )
        {
            int nCacheIndex = Integer.parseInt( strCacheIndex );
            CacheableService<?, ?> cs = CacheService.getCacheableServicesList( ).get( nCacheIndex );
            cs.enableCache( !cs.isCacheEnable( ) );
        }

        return redirectView( request, VIEW_MANAGE_CACHES );
    }

}
