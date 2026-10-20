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
package fr.paris.lutece.portal.web.insert;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.portal.service.html.EncodingService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.insert.InsertResourceIdService;
import fr.paris.lutece.portal.service.insert.InsertService;
import fr.paris.lutece.portal.service.insert.InsertServiceManager;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

import org.apache.commons.lang3.StringUtils;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Provides the generic interface to choose a particular link service
 */
@RequestScoped
@Named
public class InsertServiceSelectorJspBean extends AdminFeaturesPageJspBean
{
    /**
     * Right to manage this feature
     */
    public static final String RIGHT_MANAGE_LINK_SERVICE = "CORE_LINK_SERVICE_MANAGEMENT";

    /**
     * Session key to set the content to insert
     */
    public static final String SESSION_INSERT = "portal.session.insertServiceSelector.contentToInsert";
    private static final long serialVersionUID = 3395846045509139922L;

    // Constants
    private static final String TEMPLATE_INSERT_TYPE_PAGE = "admin/insert/page_insertservice.html";
    private static final String TEMPLATE_INSERT_TYPE_PAGE_DATA = "admin/insert/page_insertservice_data.html";
    private static final String TEMPLATE_INSERT_INTO_ELEMENT = "admin/insert/insert_into_element.html";
    private static final String TEMPLATE_INSERT_INTO_ELEMENT2 = "admin/insert/insert_into_element2.html";
    private static final String MSG_NO_SERVICE_AVAILABLE = "portal.insert.message.noServiceAvailable";

    /** name of the links type parameter */
    private static final String PARAMETER_INSERT_SERVICE_TYPE = "insert_service_type";
    private static final String PARAMETER_MODE = "mode";
    private static final String PARAMETER_INPUT = "input";
    private static final String PARAMETER_INSERT = "insert";
    private static final String PARAMETER_SELECTED_TEXT = "selected_text";
    private static final String MARK_INSERT_SERVICES_LIST = "insert_services_list";
    private static final String MARK_INSERT_SERVICE_GROUPS = "insert_service_groups";
    private static final String MARK_GROUP_PLUGIN_NAME = "plugin_name";
    private static final String MARK_GROUP_PLUGIN = "plugin";
    private static final String MARK_GROUP_PLUGIN_DISPLAY_NAME = "plugin_display_name";
    private static final String SUFFIX_DESCRIPTION_KEY = ".description";
    private static final String SUFFIX_NAME_KEY = ".name";
    private static final String PREFIX_MODULE_KEY = "module.";
    private static final String MARK_GROUP_SERVICES = "services";
    private static final String MARK_SELECTED_TEXT = "selected_text";
    private static final String MARK_INPUT = "input";
    private static final String MARK_INSERT = "insert";

    /**
     * build the insert service selection page
     *
     * @param request
     *            HTTP request
     * @return HTML code of the page as String
     */
    public String getServicesListPage( HttpServletRequest request )
    {
        String strInput = request.getParameter( PARAMETER_INPUT );
        String strText = request.getParameter( PARAMETER_SELECTED_TEXT );
        String strMode = request.getParameter( PARAMETER_MODE );

        // Encode the HTML code to insert
        strText = EncodingService.encodeUrl( strText );

        Collection<InsertService> listServices = InsertServiceManager.getInsertServicesList( );
        User user = getUser( );
        
        Collection<InsertService> listAuthorizedServices = RBACService.getAuthorizedCollection( listServices, InsertResourceIdService.PERMISSION_USE, user );

        // building from a template
        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_INSERT_SERVICES_LIST, listAuthorizedServices );
        model.put( MARK_INSERT_SERVICE_GROUPS, getServiceGroups( listAuthorizedServices, getLocale( ) ) );
        model.put( MARK_SELECTED_TEXT, strText );
        model.put( MARK_INPUT, strInput );
        
        HtmlTemplate t;
        
        if ("2".equals(strMode))
        {
            t = AppTemplateService.getTemplate( TEMPLATE_INSERT_TYPE_PAGE_DATA, getLocale( ), model );
        }
        else
        {
            t = AppTemplateService.getTemplate( TEMPLATE_INSERT_TYPE_PAGE, getLocale( ), model );
        }
        return t.getHtml( );
    }

    /**
     * Groups the insert services by plugin, in the order of the services
     *
     * @param listServices
     *            the insert services
     * @param locale
     *            the locale of the display names of the plugins
     * @return for each plugin: plugin_name, plugin_display_name, plugin (null when the plugin is not found) and services
     */
    private static List<Map<String, Object>> getServiceGroups( Collection<InsertService> listServices, Locale locale )
    {
        Map<String, List<InsertService>> mapServicesByPlugin = new LinkedHashMap<>( );

        for ( InsertService service : listServices )
        {
            mapServicesByPlugin.computeIfAbsent( service.getPluginName( ), strPluginName -> new ArrayList<>( ) ).add( service );
        }

        List<Map<String, Object>> listGroups = new ArrayList<>( );

        for ( Map.Entry<String, List<InsertService>> entry : mapServicesByPlugin.entrySet( ) )
        {
            Map<String, Object> group = new HashMap<>( );
            Plugin plugin = PluginService.getPlugin( entry.getKey( ) );
            group.put( MARK_GROUP_PLUGIN_NAME, entry.getKey( ) );
            group.put( MARK_GROUP_PLUGIN_DISPLAY_NAME, getPluginDisplayName( entry.getKey( ), plugin, locale ) );
            group.put( MARK_GROUP_PLUGIN, plugin );
            group.put( MARK_GROUP_SERVICES, entry.getValue( ) );
            listGroups.add( group );
        }

        return listGroups;
    }

    /**
     * The display name of a plugin: the key of its description with name in place of description (blog.plugin.name, module.x.y.module.name), or its
     * technical name, capitalized, when it has none
     *
     * @param strPluginName
     *            the name of the plugin
     * @param plugin
     *            the plugin, null when not found
     * @param locale
     *            the locale
     * @return the display name
     */
    private static String getPluginDisplayName( String strPluginName, Plugin plugin, Locale locale )
    {
        String strDescriptionKey = ( plugin != null ) ? plugin.getDescription( ) : null;

        if ( StringUtils.endsWith( strDescriptionKey, SUFFIX_DESCRIPTION_KEY ) )
        {
            String strNameKey = StringUtils.removeEnd( strDescriptionKey, SUFFIX_DESCRIPTION_KEY ) + SUFFIX_NAME_KEY;
            String strName = null;

            if ( strNameKey.startsWith( PREFIX_MODULE_KEY ) )
            {
                // the bundle of a module cannot be listed: a missing key logs a warning
                strName = I18nService.getLocalizedString( strNameKey, locale );
            }
            else if ( hasPluginKey( strPluginName, strNameKey.substring( strNameKey.indexOf( '.' ) + 1 ) ) )
            {
                strName = I18nService.getLocalizedString( strNameKey, locale );
            }

            if ( StringUtils.isNotBlank( strName ) )
            {
                return strName;
            }
        }

        return StringUtils.capitalize( strPluginName.replace( '-', ' ' ) );
    }

    /**
     * Tells whether the bundle of a plugin holds a key, without the warning of a missing key
     *
     * @param strPluginName
     *            the name of the plugin
     * @param strKey
     *            the key, without the name of the plugin
     * @return true if the bundle holds it
     */
    private static boolean hasPluginKey( String strPluginName, String strKey )
    {
        try
        {
            return Collections.list( I18nService.getPluginBundleKeys( strPluginName ) ).contains( strKey );
        }
        catch( MissingResourceException e )
        {
            return false;
        }
    }

    /**
     * Call the selected service selection UI
     *
     * @param request
     *            HTTP request
     * @return the HTML code of the selection page for selected service
     */
    public String displayService( HttpServletRequest request )
    {
        String strInsertService = request.getParameter( PARAMETER_INSERT_SERVICE_TYPE );

        if ( ( strInsertService != null ) && !strInsertService.equals( "" ) )
        {
            InsertService is = InsertServiceManager.getInsertService( strInsertService );

            return is.getSelectorUI( request );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, MSG_NO_SERVICE_AVAILABLE, AdminMessage.TYPE_STOP );
        }
    }

    /**
     * Build the JSP that provides the HTML code insertion into the Editor via a javascript call
     *
     * @param request
     *            HTTP request
     * @return the HTML code of the page that insert code into the editor
     */
    public String doInsertIntoElement( HttpServletRequest request )
    {
        String strMode = request.getParameter( PARAMETER_MODE );
        String strInput = request.getParameter( PARAMETER_INPUT );
        String strInsert = (String) request.getSession( ).getAttribute( SESSION_INSERT );

        if ( strInsert == null )
        {
            strInsert = request.getParameter( PARAMETER_INSERT );
        }

        request.getSession( ).removeAttribute( SESSION_INSERT );

        Map<String, String> model = new HashMap<>( );
        model.put( MARK_INPUT, strInput );
        model.put( MARK_INSERT, strInsert );

        HtmlTemplate template;

        if ( strMode.compareTo( "2" ) == 0 )
        {
            template = AppTemplateService.getTemplate( TEMPLATE_INSERT_INTO_ELEMENT2, getLocale( ), model );
        }
        else
        {
            template = AppTemplateService.getTemplate( TEMPLATE_INSERT_INTO_ELEMENT, getLocale( ), model );
        }

        return template.getHtml( );
    }
}
