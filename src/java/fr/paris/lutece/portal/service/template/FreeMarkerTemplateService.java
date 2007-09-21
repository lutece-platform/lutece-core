/*
 * Copyright (c) 2002-2007, Mairie de Paris
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
package fr.paris.lutece.portal.service.template;

import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.html.HtmlTemplate;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * Template service based on the Freemarker template engine
 */
public class FreeMarkerTemplateService
{
    private static final String PATH_AUTO_INCLUDE_COMMONS = "*/commons.html";
    private static final String NUMBER_FORMAT_PATTERN = "0.######";
    private static Map<String, Configuration> _mapConfigurations = new HashMap<String, Configuration>(  );
    private static String _strDefaultPath;

    /** Creates a new instance of FreeMarkerTemplateService */
    private FreeMarkerTemplateService(  )
    {
    }

    /**
     * Initializes the service with the templates's path
     * @param strTemplatePath The template path
     */
    public static void init( String strTemplatePath )
    {
        _strDefaultPath = strTemplatePath;
    }

    /**
     * Load a template
     * @param strPath the root path
     * @param strTemplate the path of the template from the root path
     * @return the html template
     */
    public static HtmlTemplate loadTemplate( String strPath, String strTemplate )
    {
        return loadTemplate( strPath, strTemplate, null, null );
    }

    /**
     * Load a template and process a model
     * @param strPath the root path
     * @param strTemplate the path of the template from the root path
     * @param rootMap the model root
     * @return the processed html template
     */
    public static HtmlTemplate loadTemplate( String strPath, String strTemplate, Locale locale, Object rootMap )
    {
        HtmlTemplate template = null;

        try
        {
            Configuration cfg = (Configuration) _mapConfigurations.get( strPath );

            if ( cfg == null )
            {
                cfg = new Configuration(  );

                // set the root directory for template loading
                File directory = new File( AppPathService.getAbsolutePathFromRelativePath( strPath ) );
                cfg.setDirectoryForTemplateLoading( directory );

                if ( ( strPath != null ) && ( strPath.equals( _strDefaultPath ) ) )
                {
                    // add the macros auto inclusion
                    cfg.addAutoInclude( PATH_AUTO_INCLUDE_COMMONS );
                }

                // disable the localized look-up process to find a template
                cfg.setLocalizedLookup( false );

                // keep control localized number formating (can cause pb on ids, and we don't want to use the ?c directive all the time)
                cfg.setNumberFormat( NUMBER_FORMAT_PATTERN );

                _mapConfigurations.put( strPath, cfg );
            }

            Template ftl;

            if ( locale == null )
            {
                ftl = cfg.getTemplate( strTemplate );
            }
            else
            {
                ftl = cfg.getTemplate( strTemplate, locale );
            }

            StringWriter writer = new StringWriter( 1024 );
            ftl.process( rootMap, writer );
            template = new HtmlTemplate( writer.toString(  ) );
        }
        catch ( IOException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
        catch ( TemplateException e )
        {
            throw new AppException( e.getMessage(  ) );
        }

        return template;
    }

    /**
     * Reset the cache
     *
     */
    public static void resetCache(  )
    {
        for ( Configuration cfg : _mapConfigurations.values(  ) )
        {
            cfg.clearTemplateCache(  );
        }
    }
}
