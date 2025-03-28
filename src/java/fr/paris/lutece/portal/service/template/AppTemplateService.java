/*
 * Copyright (c) 2002-2025, City of Paris
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

import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.i18n.I18nTemplateMethod;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.Locale;
import java.util.Map;

/**
 * This Service is used to retreive HTML templates, stored as files in the WEB-INF/templates directory of the webapp, to build the user interface. It provides a
 * cache feature to prevent from loading file each time it is asked.
 */
public final class AppTemplateService
{
    // Variables
    private static String _strTemplateDefaultPath;
    private static IFreeMarkerTemplateService _freeMarkerTemplateService;

    /**
     * Protected constructor
     */
    private AppTemplateService( )
    {
    }

    /**
     * Initializes the service with the templates's path
     * 
     * @param strTemplatePath
     *            The template path
     */
    public static void init( String strTemplatePath )
    {
        _strTemplateDefaultPath = strTemplatePath;
        getFreeMarkerTemplateService( ).setSharedVariable( "i18n", new I18nTemplateMethod( ) );
    }

    /**
     * Initializes auto-includes and auto-imports for plugins.
     */
    public static void initMacros( )
    {
        // register core (commons declared in core.xml)
        Plugin corePlugin = PluginService.getCore( );
        addPluginAutoIncludes( corePlugin );
        addPluginAutoImports( corePlugin );

        // register plugins
        for ( Plugin plugin : PluginService.getPluginList( ) )
        {
            addPluginAutoIncludes( plugin );
            addPluginAutoImports( plugin );
        }

        // activate current commons stored in the datastore
        CommonsService.activateCommons( CommonsService.getCurrentCommonsKey( ) );
    }

    /**
     * Adds the plugin auto-includes.
     *
     * @param plugin
     *            the plugin
     */
    private static void addPluginAutoIncludes( Plugin plugin )
    {
        for ( String strFileName : plugin.getFreeMarkerAutoIncludes( ) )
        {
            AppLogService.info( "New freemarker autoinclude : {} from {}", strFileName, plugin.getName( ) );
            getFreeMarkerTemplateService( ).addPluginAutoInclude( strFileName );
        }
    }

    /**
     * Adds the plugin auto-imports.
     *
     * @param plugin
     *            the plugin
     */
    private static void addPluginAutoImports( Plugin plugin )
    {
        for ( Map.Entry<String, String> importEntry : plugin.getFreeMarkerAutoImports( ).entrySet( ) )
        {
            AppLogService.info( "New freemarker autoimport : {} as {} from {}", importEntry.getValue( ), importEntry.getKey( ), plugin.getName( ) );
            getFreeMarkerTemplateService( ).addPluginAutoImport( importEntry.getKey( ), importEntry.getValue( ) );
        }
    }

    /**
     * Reset the cache
     */
    public static void resetCache( )
    {
        getFreeMarkerTemplateService( ).resetCache( );
    }

    /**
     * Resets the configuration cache
     */
    public static void resetConfiguration( )
    {
        getFreeMarkerTemplateService( ).resetConfiguration( );
    }

    /**
     * Returns a reference on a template object (load the template or get it from the cache if present.)
     *
     * @param strTemplate
     *            The name of the template
     * @return The template object.
     */
    public static HtmlTemplate getTemplate( String strTemplate )
    {
        return getTemplate( strTemplate, _strTemplateDefaultPath );
    }

    /**
     * Returns a reference on a template object (load the template or get it from the cache if present.)
     *
     * @param strTemplate
     *            The name of the template
     * @param strPath
     *            The specific path to load the template
     * @return The template object.
     * @since 1.3.1
     */
    public static HtmlTemplate getTemplate( String strTemplate, String strPath )
    {
        return getTemplate( strTemplate, strPath, null, null );
    }

    // //////////////////////////////////////////////////////////////////////////
    // v1.5

    /**
     * Returns a reference on a template object (load the template or get it from the cache if present.)
     *
     * @param strTemplate
     *            The name of the template
     * @param locale
     *            The current locale to localize the template
     * @return The template object.
     * @since 1.5
     */
    public static HtmlTemplate getTemplate( String strTemplate, Locale locale )
    {
        return getTemplate( strTemplate, _strTemplateDefaultPath, locale, null );
    }

    /**
     * Returns a reference on a template object (load the template or get it from the cache if present.)
     * 
     * @param strTemplate
     *            The name of the template
     * @param locale
     *            The current locale to localize the template
     * @param model
     *            the model to use for loading
     * @return The template object.
     * @since 1.5
     */
    public static HtmlTemplate getTemplate( String strTemplate, Locale locale, Object model )
    {
        HtmlTemplate template;

        // Load the template from the file
        template = getTemplate( strTemplate, _strTemplateDefaultPath, locale, model );

        return template;
    }

    /**
     * Returns a reference on a template object (load the template or get it from the cache if present.)
     *
     * @param strTemplate
     *            The name of the template
     * @param strPath
     *            The specific path to load the template
     * @param locale
     *            The current locale to localize the template
     * @param model
     *            the model to use for loading
     * @return The template object.
     * @since 1.5
     */
    public static HtmlTemplate getTemplate( String strTemplate, String strPath, Locale locale, Object model )
    {
        HtmlTemplate template;

        // Load the template from the file
        template = loadTemplate( strPath, strTemplate, locale, model );

        return template;
    }

    /**
     * Returns a reference on a template object  using the template data passed in parameter or getting from cache if present
     *
     *
     * @param strFreemarkerTemplateData
     *            The content of the template
     * @param locale
     *            The current {@link Locale} to localize the template
     * @param model
     *            The model
     * @return The template object
     * @since 1.5
     */
    public static HtmlTemplate getTemplateFromStringFtl( String strFreemarkerTemplateData, Locale locale, Object model )
    {
        HtmlTemplate template = getFreeMarkerTemplateService( ).loadTemplateFromStringFtl( strFreemarkerTemplateData, locale, model );

        if ( locale != null )
        {
            String strLocalized = I18nService.localize( template.getHtml( ), locale );
            template = new HtmlTemplate( strLocalized );
        }
        return template;
    }
    
    
    /**
     * Returns a reference on a template object using the template data passed in parameter or getting from cache if present
     *
     * @param strFreemarkerTemplateName The name of the template ( Must be a Fully qualified name for example skin.plugins.myplugin.manage_my_objects )
     * @param strFreemarkerTemplateData
     *            The content of the template
     * @param locale
     *            The current {@link Locale} to localize the template
     * @param model
     *            The model
     * @param bResetCache true if the template stored in cache must be updated by the content of the strFreemarkerTemplateDa            
     * @return The template object
     * @since 7.0.5
     */
    public static HtmlTemplate getTemplateFromStringFtl( String strFreemarkerTemplateName,String strFreemarkerTemplateData, Locale locale, Object model,boolean bResetCache )
    {
        HtmlTemplate template = getFreeMarkerTemplateService( ).loadTemplateFromStringFtl( strFreemarkerTemplateName,strFreemarkerTemplateData, locale, model,bResetCache );

        if ( locale != null )
        {
            String strLocalized = I18nService.localize( template.getHtml( ), locale );
            template = new HtmlTemplate( strLocalized );
        }
        return template;
    }

    /**
     * Load the template from the file
     * 
     * @param strTemplate
     *            The name of the template
     * @param strPath
     *            The specific path to load the template
     * @param locale
     *            The current locale to localize the template
     * @param model
     *            the model to use for loading
     * @return The loaded template
     */
    private static HtmlTemplate loadTemplate( String strPath, String strTemplate, Locale locale, Object model )
    {
        HtmlTemplate template;
        template = getFreeMarkerTemplateService( ).loadTemplate( strPath, strTemplate, locale, model );

        if ( locale != null )
        {
            String strLocalized = I18nService.localize( template.getHtml( ), locale );
            template = new HtmlTemplate( strLocalized );
        }

        template = new HtmlTemplate( DatastoreService.replaceKeys( template.getHtml( ) ) );

        return template;
    }

    /**
     * Get the instance of free marker template service
     * 
     * @return the instance of free marker template service
     */
    private static IFreeMarkerTemplateService getFreeMarkerTemplateService( )
    {
        if ( _freeMarkerTemplateService == null )
        {
            _freeMarkerTemplateService = FreeMarkerTemplateService.getInstance( );
            _freeMarkerTemplateService.init( _strTemplateDefaultPath );
        }

        return _freeMarkerTemplateService;
    }
}
