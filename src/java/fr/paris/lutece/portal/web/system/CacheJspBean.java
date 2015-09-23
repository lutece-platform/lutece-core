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
package fr.paris.lutece.portal.web.system;

import fr.paris.lutece.portal.service.cache.CacheService;
import fr.paris.lutece.portal.service.cache.CacheableService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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
    private static final long serialVersionUID = 7010476999488231065L;

    // Markers
    private static final String MARK_SERVICES_LIST = "services_list";

    // Template Files path
    private static final String TEMPLATE_MANAGE_CACHES = "admin/system/manage_caches.html";
    private static final String TEMPLATE_CACHE_INFOS = "admin/system/cache_infos.html";
    private static final String PARAMETER_ID_CACHE = "id_cache";

    /**
     * Returns the page to manage caches
     * @param request The HttpServletRequest
     * @return The HTML code.
     */
    public String getManageCaches( HttpServletRequest request )
    {
        HashMap<String, Collection<CacheableService>> model = new HashMap<String, Collection<CacheableService>>(  );
        model.put( MARK_SERVICES_LIST, CacheService.getCacheableServicesList(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_CACHES, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process cache resetting
     *
     * @param request The HTTP request
     * @return The URL to display when the process is done.
     */
    public static String doResetCaches( HttpServletRequest request )
    {
        String strCacheIndex = request.getParameter( PARAMETER_ID_CACHE );

        if ( strCacheIndex != null )
        {
            int nCacheIndex = Integer.parseInt( strCacheIndex );
            CacheableService cs = CacheService.getCacheableServicesList(  ).get( nCacheIndex );
            cs.resetCache(  );
        }
        else
        {
            CacheService.resetCaches(  );
            AppTemplateService.resetCache(  );
            I18nService.resetCache(  );
        }

        return JSP_MANAGE_CACHES;
    }

    /**
     * Reload all properties files of the application
     *
     * @return The URL to display when the process is done.
     */
    public String doReloadProperties(  )
    {
        AppPropertiesService.reloadAll(  );

        return JSP_MANAGE_CACHES;
    }

    /**
     * Gets cache infos for all caches
     * @param request The HTTP request
     * @return HTML formated cache infos
     */
    public String getCacheInfos( HttpServletRequest request )
    {
        List<CacheableService> list;
        String strCacheIndex = request.getParameter( PARAMETER_ID_CACHE );

        if ( strCacheIndex != null )
        {
            int nCacheIndex = Integer.parseInt( strCacheIndex );
            CacheableService cs = CacheService.getCacheableServicesList(  ).get( nCacheIndex );
            list = new ArrayList<CacheableService>(  );
            list.add( cs );
        }
        else
        {
            list = CacheService.getCacheableServicesList(  );
        }

        HashMap<String, Collection<CacheableService>> model = new HashMap<String, Collection<CacheableService>>(  );
        model.put( MARK_SERVICES_LIST, list );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CACHE_INFOS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process cache toggle on/off
     *
     * @param request The HTTP request
     * @return The URL to display when the process is done.
     */
    public static String doToggleCache( HttpServletRequest request )
    {
        String strCacheIndex = request.getParameter( PARAMETER_ID_CACHE );

        if ( strCacheIndex != null )
        {
            int nCacheIndex = Integer.parseInt( strCacheIndex );
            CacheableService cs = CacheService.getCacheableServicesList(  ).get( nCacheIndex );
            cs.enableCache( !cs.isCacheEnable(  ) );
        }

        return JSP_MANAGE_CACHES;
    }
}
