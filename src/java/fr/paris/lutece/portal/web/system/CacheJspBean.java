/*
 * Copyright (c) 2002-2021, City of Paris
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
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage system features ( manage logs, view system files, ... ).
 */
public class CacheJspBean extends AdminFeaturesPageJspBean
{
    // Right
    public static final String RIGHT_CACHE_MANAGEMENT = "CORE_CACHE_MANAGEMENT";

    // Jsp definition
    public static final String JSP_MANAGE_CACHES = "ManageCaches.jsp";

    private static final String JSP_TOGGLE_CACHE = "jsp/admin/system/DoToggleCache.jsp";
    private static final String PROPERTY_MESSAGE_CONFIRM_TOOGLE_CACHE = "portal.system.message.confirmToggleCache";
    private static final String PROPERTY_MESSAGE_CONFIRM_TOOGLE_CACHE_TITLE = "portal.system.message.confirmToggleCacheTitle";
    private static final String PROPERTY_MESSAGE_INVALID_CACHE_ID = "portal.system.message.invalidCacheId";

    private static final long serialVersionUID = 7010476999488231065L;

    // Markers
    private static final String MARK_SERVICES_LIST = "services_list";

    // Template Files path
    private static final String TEMPLATE_MANAGE_CACHES = "admin/system/manage_caches.html";
    private static final String TEMPLATE_CACHE_INFOS = "admin/system/cache_infos.html";
    private static final String PARAMETER_ID_CACHE = "id_cache";

    /**
     * Returns the page to manage caches
     * 
     * @param request
     *            The HttpServletRequest
     * @return The HTML code.
     */
    public String getManageCaches( HttpServletRequest request )
    {
        HashMap<String, Object> model = new HashMap<>( );
        model.put( MARK_SERVICES_LIST, CacheService.getCacheableServicesList( ) );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_MANAGE_CACHES ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_CACHES, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Process cache resetting
     *
     * @param request
     *            The HTTP request
     * @return The URL to display when the process is done.
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public static String doResetCaches( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, TEMPLATE_MANAGE_CACHES ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
        String strCacheIndex = request.getParameter( PARAMETER_ID_CACHE );

        if ( strCacheIndex != null )
        {
            int nCacheIndex = Integer.parseInt( strCacheIndex );
            CacheableService cs = CacheService.getCacheableServicesList( ).get( nCacheIndex );
            cs.resetCache( );
        }
        else
        {
            CacheService.resetCaches( );
            AppTemplateService.resetCache( );
            I18nService.resetCache( );
        }

        return JSP_MANAGE_CACHES;
    }

    /**
     * Reload all properties files of the application
     *
     * @param request
     *            The HTTP request
     * @return The URL to display when the process is done.
     * @throws AccessDeniedException
     */
    public String doReloadProperties( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, TEMPLATE_MANAGE_CACHES ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
        AppPropertiesService.reloadAll( );

        return JSP_MANAGE_CACHES;
    }

    /**
     * Gets cache infos for all caches
     * 
     * @param request
     *            The HTTP request
     * @return HTML formated cache infos
     */
    public String getCacheInfos( HttpServletRequest request )
    {
        List<CacheableService> list;
        String strCacheIndex = request.getParameter( PARAMETER_ID_CACHE );

        if ( strCacheIndex != null )
        {
            int nCacheIndex = Integer.parseInt( strCacheIndex );
            CacheableService cs = CacheService.getCacheableServicesList( ).get( nCacheIndex );
            list = new ArrayList<>( );
            list.add( cs );
        }
        else
        {
            list = CacheService.getCacheableServicesList( );
        }

        HashMap<String, Collection<CacheableService>> model = new HashMap<>( );
        model.put( MARK_SERVICES_LIST, list );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CACHE_INFOS, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Returns the page of confirmation for changing the cache activation
     *
     * @param request
     *            The Http Request
     * @return the HTML page
     */
    public String getConfirmToggleCache( HttpServletRequest request )
    {
        String strCacheIndex = request.getParameter( PARAMETER_ID_CACHE );
        if ( strCacheIndex != null )
        {
            int nCacheIndex = Integer.parseInt( strCacheIndex );
            CacheableService cs = CacheService.getCacheableServicesList( ).get( nCacheIndex );
            if ( cs != null )
            {
                Object [ ] messageArgs = {
                        cs.getName( )
                };

                Map<String, Object> parameters = new HashMap<>( );
                parameters.put( PARAMETER_ID_CACHE, strCacheIndex );
                parameters.put( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, JSP_TOGGLE_CACHE ) );
                return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_CONFIRM_TOOGLE_CACHE, messageArgs,
                        PROPERTY_MESSAGE_CONFIRM_TOOGLE_CACHE_TITLE, JSP_TOGGLE_CACHE, "", AdminMessage.TYPE_CONFIRMATION, parameters );
            }
        }
        return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_INVALID_CACHE_ID, JSP_MANAGE_CACHES, AdminMessage.TYPE_ERROR );
    }

    /**
     * Process cache toggle on/off
     *
     * @param request
     *            The HTTP request
     * @return The URL to display when the process is done.
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public static String doToggleCache( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, JSP_TOGGLE_CACHE ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
        String strCacheIndex = request.getParameter( PARAMETER_ID_CACHE );

        if ( strCacheIndex != null )
        {
            int nCacheIndex = Integer.parseInt( strCacheIndex );
            CacheableService cs = CacheService.getCacheableServicesList( ).get( nCacheIndex );
            cs.enableCache( !cs.isCacheEnable( ) );
        }

        return JSP_MANAGE_CACHES;
    }

}
