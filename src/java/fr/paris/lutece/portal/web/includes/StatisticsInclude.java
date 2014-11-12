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
package fr.paris.lutece.portal.web.includes;

import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.includes.PageInclude;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This Page Include
 */
public class StatisticsInclude implements PageInclude
{
    private static final String PROPERTY_STATISTICAL_SITE_ID = "lutece.statistical.site.id";
    private static final String PROPERTY_STATISTICAL_INCLUDE_ENABLE = "lutece.statistical.include.enable";
    private static final String PROPERTY_STATISTICAL_INCLUDE_TEMPLATE = "lutece.statistical.include.template";
    private static final String PROPERTY_STATISTICAL_INCLUDE_HEAD_TEMPLATE = "lutece.statistical.include.template.head";
    private static final String PROPERTY_STATISTICAL_SERVER_URL = "lutece.statistical.server.url";
    private static final String MARK_STATISTICAL_INCLUDE = "statistical_include";
    private static final String MARK_STATISTICAL_INCLUDE_HEAD = "statistical_include_head";
    private static final String MARK_STATISTICAL_SITE_ID = "statistical_site_id";
    private static final String MARK_STATISTICAL_SERVER_URL = "statistical_server_url";

    /**
     * Substitue specific Freemarker markers in the page template.
     * @param rootModel the HashMap containing markers to substitute
     * @param data A PageData object containing applications data
     * @param nMode The current mode
     * @param request The HTTP request
     */
    public void fillTemplate( Map<String, Object> rootModel, PageData data, int nMode, HttpServletRequest request )
    {
        String strStatisticalIncludeHead;
        String strStatisticalInclude;

        if ( request != null )
        {
            String strStatisticalSiteId = AppPropertiesService.getProperty( PROPERTY_STATISTICAL_SITE_ID, "1" );
            String strStatisticalIncludeEnable = AppPropertiesService.getProperty( PROPERTY_STATISTICAL_INCLUDE_ENABLE );
            String strStatisticalServerUrl = AppPropertiesService.getProperty( PROPERTY_STATISTICAL_SERVER_URL );

            if ( ( strStatisticalIncludeEnable != null ) && ( strStatisticalIncludeEnable.equalsIgnoreCase( "true" ) ) )
            {
                String strStatisticalIncludeTemplateHead = AppPropertiesService.getProperty( PROPERTY_STATISTICAL_INCLUDE_HEAD_TEMPLATE );

                Map<String, String> model = new HashMap<String, String>(  );
                model.put( MARK_STATISTICAL_SITE_ID, strStatisticalSiteId );
                model.put( MARK_STATISTICAL_SERVER_URL, strStatisticalServerUrl );

                HtmlTemplate tHead = AppTemplateService.getTemplate( strStatisticalIncludeTemplateHead,
                        request.getLocale(  ), model );
                strStatisticalIncludeHead = tHead.getHtml(  );

                String strStatisticalIncludeTemplate = AppPropertiesService.getProperty( PROPERTY_STATISTICAL_INCLUDE_TEMPLATE );
                HtmlTemplate t = AppTemplateService.getTemplate( strStatisticalIncludeTemplate, request.getLocale(  ),
                        model );
                strStatisticalInclude = t.getHtml(  );
            }
            else
            {
                // markers are defaulted to empty string to avoid freemarker errors
                strStatisticalIncludeHead = "";
                strStatisticalInclude = "";
            }
        }
        else
        {
            // markers are defaulted to empty string to avoid freemarker errors
            strStatisticalIncludeHead = "";
            strStatisticalInclude = "";
        }

        rootModel.put( MARK_STATISTICAL_INCLUDE_HEAD, strStatisticalIncludeHead );
        rootModel.put( MARK_STATISTICAL_INCLUDE, strStatisticalInclude );
    }
}
