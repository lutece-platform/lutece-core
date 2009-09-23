/*
 * Copyright (c) 2002-2009, Mairie de Paris
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
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.html.HtmlTemplate;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;

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
public final class FreeMarkerTemplateService
{
    private static final String PROPERTY_TEMPLATE_UPDATE_DELAY = "service.freemarker.templateUpdateDelay";
    private static final int TEMPLATE_UPDATE_DELAY = AppPropertiesService.getPropertyInt( PROPERTY_TEMPLATE_UPDATE_DELAY,
            5 );
    private static final String STRING_TEMPLATE_LOADER_NAME = "stringTemplate";
    private static final String PATH_AUTO_INCLUDE_COMMONS = "*/commons.html";
    private static final String NUMBER_FORMAT_PATTERN = "0.######";
    private static final String SETTING_DATE_FORMAT = "date_format";
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
     * @param locale The locale
     * @param rootMap the model root
     * @return the processed html template
     */
    public static HtmlTemplate loadTemplate( String strPath, String strTemplate, Locale locale, Object rootMap )
    {
        Configuration cfg = null;

        try
        {
            cfg = (Configuration) _mapConfigurations.get( strPath );

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

                //Used to set the default format to display a date and datetime
                cfg.setSetting( SETTING_DATE_FORMAT, DateUtil.getDefaultPattern( locale ) );

                //WARNING : the Datetime format is defined as the date format, i.e. the hours and minutes will not be displayed
                //        	cfg.setSetting( SETTING_DATETIME_FORMAT, DateUtil.getDefaultPattern( locale ) );

                // Time in seconds that must elapse before checking whether there is a newer version of a template file
                cfg.setTemplateUpdateDelay( TEMPLATE_UPDATE_DELAY );
            }
        }
        catch ( IOException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
        catch ( TemplateException e )
        {
            throw new AppException( e.getMessage(  ) );
        }

        return processTemplate( cfg, strTemplate, rootMap, locale );
    }

    /**
     * Load a template from a String and process a model
     * WARNING : This method must not be used in front office (no cache management available).
     *
     * <br /><b>Deprecated</b> Using Freemarker without cache is huge CPU consuming
     *
     * @param strTemplateData The template as a string
     * @param locale The {@link Locale}
     * @param rootMap the model root
     * @return the processed html template
     */
    @Deprecated
    public static HtmlTemplate loadTemplate( String strTemplateData, Locale locale, Object rootMap )
    {
        Configuration cfg = null;

        try
        {
            cfg = new Configuration(  );

            // set the root directory for template loading
            File directory = new File( AppPathService.getAbsolutePathFromRelativePath( _strDefaultPath ) );
            FileTemplateLoader ftl1 = new FileTemplateLoader( directory );
            StringTemplateLoader stringLoader = new StringTemplateLoader(  );
            stringLoader.putTemplate( STRING_TEMPLATE_LOADER_NAME, strTemplateData );

            TemplateLoader[] loaders = new TemplateLoader[] { ftl1, stringLoader };
            MultiTemplateLoader mtl = new MultiTemplateLoader( loaders );

            cfg.setTemplateLoader( mtl );

            // add the macros auto inclusion
            cfg.addAutoInclude( PATH_AUTO_INCLUDE_COMMONS );

            // disable the localized look-up process to find a template
            cfg.setLocalizedLookup( false );

            // keep control localized number formating (can cause pb on ids, and we don't want to use the ?c directive all the time)
            cfg.setNumberFormat( NUMBER_FORMAT_PATTERN );

            //Used to set the default format to display a date and datetime
            cfg.setSetting( SETTING_DATE_FORMAT, DateUtil.getDefaultPattern( locale ) );

            // Time in seconds that must elapse before checking whether there is a newer version of a template file
            cfg.setTemplateUpdateDelay( TEMPLATE_UPDATE_DELAY );
        }
        catch ( IOException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
        catch ( TemplateException e )
        {
            throw new AppException( e.getMessage(  ) );
        }

        return processTemplate( cfg, STRING_TEMPLATE_LOADER_NAME, rootMap, locale );
    }

    /**
     * Process the template transformation and return the {@link HtmlTemplate}
     * @param cfg The Freemarker configuration to use
     * @param strTemplate The template name to call
     * @param rootMap The HashMap model
     * @param locale The {@link Locale}
     * @return The {@link HtmlTemplate}
     */
    private static HtmlTemplate processTemplate( Configuration cfg, String strTemplate, Object rootMap, Locale locale )
    {
        HtmlTemplate template = null;

        try
        {
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
            //Used to set the default format to display a date and datetime
            ftl.setDateFormat( DateUtil.getDefaultPattern( locale ) );
            
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
