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
package fr.paris.lutece.portal.web.system;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.core.LoggerContext;

import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.datastore.LocalizedData;
import fr.paris.lutece.portal.service.datastore.LocalizedDataGroup;
import fr.paris.lutece.portal.service.file.FileService;
import fr.paris.lutece.portal.service.file.IFileStoreServiceProvider;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.site.properties.SitePropertiesService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.util.LoggerInfo;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.http.SecurityUtil;
import fr.paris.lutece.util.string.StringUtil;

/**
 * This class provides the user interface to manage system features ( manage logs, view system files, ... ).
 */
public class SystemJspBean extends AdminFeaturesPageJspBean
{
    // Right
    public static final String RIGHT_PROPERTIES_MANAGEMENT = "CORE_PROPERTIES_MANAGEMENT";

    // Jsp definition
    public static final String JSP_MANAGE_PROPERTIES = "ManageProperties.jsp";

    /** serial id */
    private static final long serialVersionUID = 3770485521087669430L;

    // Markers
    private static final String MARK_PROPERTIES_GROUPS_LIST = "groups_list";
    private static final String MARK_BYPASS_XSS_KEYS = "bypass_xss_keys";

    // Template 
    private static final String TEMPLATE_MODIFY_PROPERTIES = "admin/system/modify_properties.html";

    // Properties file definition
    private static final String MARK_WEBAPP_URL = "webapp_url";
    private static final String MARK_LOCALE = "locale";

    // Site properties keys requiring XSS bypass decoding
    private static final Set<String> BYPASS_XSS_KEYS = Set.of(
        "portal.site.site_property.home_url",
        "portal.site.site_property.admin_home_url"
    );

    // Property for extending the bypass whitelist
    private static final String PROPERTY_XSS_BYPASS_ADDITIONAL_KEYS = "portal.site.site_property.xss.bypass.keys";
    private static final String SITE_PROPERTY_PREFIX = "portal.site.site_property.";

    // Color validation
    private static final String MESSAGE_INVALID_COLOR_FORMAT = "portal.system.message.invalidColorFormat";
    private static final Pattern COLOR_PATTERN = Pattern.compile( "^(#[0-9a-fA-F]{3,8}|rgba?\\([^)]+\\))?$" );
    private static final Set<String> COLOR_KEYS = Set.of(
        "portal.site.site_property.banner.title.color",
        "portal.site.site_property.banner.title.bgcolor",
        "portal.theme.site_property.banner.title.color",
        "portal.theme.site_property.banner.title.bgcolor"
    );

    
    /**
     * Returns the form to update site properties in DataStore
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    public String getManageProperties( HttpServletRequest request )
    {
        List<LocalizedDataGroup> groups = SitePropertiesService.getGroups( getLocale( ) );
        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_PROPERTIES_GROUPS_LIST, groups );
        model.put( MARK_BYPASS_XSS_KEYS, getEffectiveBypassKeys( groups ) );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale( ).getLanguage( ) );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_MODIFY_PROPERTIES ) );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MODIFY_PROPERTIES, getLocale( ), model );

        return getAdminPage( templateList.getHtml( ) );
    }

    /**
     * Process the update of site properties in DataStore
     *
     * @param request
     *            The Http request
     * @param context
     *            The context
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public static String doModifyProperties( HttpServletRequest request, ServletContext context ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, TEMPLATE_MODIFY_PROPERTIES ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
        List<LocalizedDataGroup> groups = SitePropertiesService.getGroups( AdminUserService.getAdminUser( request ).getLocale( ) );
        Set<String> effectiveBypassKeys = getEffectiveBypassKeys( groups );

        for ( LocalizedDataGroup group : groups )
        {
            List<LocalizedData> datas = group.getLocalizedDataList( );

            for ( LocalizedData data : datas )
            {
                String strValue = request.getParameter( data.getKey( ) );

                if ( strValue != null )
                {
                    String strKey = data.getKey( );

                    if ( effectiveBypassKeys.contains( strKey ) )
                    {
                        strValue = StringUtil.decodeXssBypass( strValue );
                    }

                    if ( COLOR_KEYS.contains( strKey ) && strValue != null && !strValue.isEmpty( )
                            && !COLOR_PATTERN.matcher( strValue ).matches( ) )
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_INVALID_COLOR_FORMAT, AdminMessage.TYPE_STOP );
                    }

                    if ( strValue != null && !strValue.equals( data.getValue( ) ) )
                    {
                        DatastoreService.setDataValue( strKey, strValue );
                    }
                }
            }
        }

        // if the operation occurred well, redirects towards the view of the Properties
        return JSP_MANAGE_PROPERTIES;
    }

    /**
     * Builds the effective set of site property keys that require XSS bypass decoding.
     * Merges the hardcoded keys with additional keys declared via the
     * {@value #PROPERTY_XSS_BYPASS_ADDITIONAL_KEYS} property. Additional keys are validated
     * against the declared site properties: keys with an invalid prefix or that do not match
     * any known site property are logged as errors and ignored.
     *
     * @param groups
     *            the list of site property groups used to validate additional keys
     * @return the merged set of bypass keys
     */
    private static Set<String> getEffectiveBypassKeys( List<LocalizedDataGroup> groups )
    {
        String strAdditionalKeys = AppPropertiesService.getProperty( PROPERTY_XSS_BYPASS_ADDITIONAL_KEYS, "" );

        if ( strAdditionalKeys.isEmpty( ) )
        {
            return BYPASS_XSS_KEYS;
        }

        Set<String> validSiteKeys = new HashSet<>( );
        for ( LocalizedDataGroup group : groups )
        {
            for ( LocalizedData data : group.getLocalizedDataList( ) )
            {
                validSiteKeys.add( data.getKey( ) );
            }
        }

        Set<String> effectiveKeys = new HashSet<>( BYPASS_XSS_KEYS );

        for ( String strKey : strAdditionalKeys.split( "," ) )
        {
            String strTrimmedKey = strKey.trim( );

            if ( strTrimmedKey.isEmpty( ) )
            {
                continue;
            }

            if ( !strTrimmedKey.startsWith( SITE_PROPERTY_PREFIX ) )
            {
                AppLogService.error( "XSS bypass key '{}' does not start with required prefix '{}' — ignoring", strTrimmedKey, SITE_PROPERTY_PREFIX );
                continue;
            }

            if ( !validSiteKeys.contains( strTrimmedKey ) )
            {
                AppLogService.error( "XSS bypass key '{}' does not match any declared site property — ignoring", strTrimmedKey );
                continue;
            }

            effectiveKeys.add( strTrimmedKey );
        }

        return effectiveKeys;
    }
}
