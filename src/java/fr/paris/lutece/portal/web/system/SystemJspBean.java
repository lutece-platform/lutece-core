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


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.datastore.LocalizedData;
import fr.paris.lutece.portal.service.datastore.LocalizedDataGroup;
import fr.paris.lutece.portal.service.security.ISecurityTokenService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.site.properties.SitePropertiesService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * This class provides the user interface to manage system features ( manage logs, view system files, ... ).
 */
@SessionScoped
@Named
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

    // Template 
    private static final String TEMPLATE_MODIFY_PROPERTIES = "admin/system/modify_properties.html";

    // Properties file definition
    private static final String MARK_WEBAPP_URL = "webapp_url";
    private static final String MARK_LOCALE = "locale";

    
    
    /**
     * Returns the form to update site properties in DataStore
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    public String getManageProperties( HttpServletRequest request )
    {   	
    	Map<String, Object> model = new HashMap<>( );
        model.put( MARK_PROPERTIES_GROUPS_LIST, SitePropertiesService.getGroups( getLocale( ) ) );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale( ).getLanguage( ) );
        model.put( SecurityTokenService.MARK_TOKEN, getSecurityTokenService( ).getToken( request, TEMPLATE_MODIFY_PROPERTIES ) );

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
    	ISecurityTokenService securityTokenService = CDI.current( ).select( ISecurityTokenService.class ).get( );
        if ( !securityTokenService.validate( request, TEMPLATE_MODIFY_PROPERTIES ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
        List<LocalizedDataGroup> groups = SitePropertiesService.getGroups( AdminUserService.getAdminUser( request ).getLocale( ) );

        for ( LocalizedDataGroup group : groups )
        {
            List<LocalizedData> datas = group.getLocalizedDataList( );

            for ( LocalizedData data : datas )
            {
                String strValue = request.getParameter( data.getKey( ) );

                if ( ( strValue != null ) && !data.getValue( ).equals( strValue ) )
                {
                    DatastoreService.setDataValue( data.getKey( ), strValue );
                }
            }
        }

        // if the operation occurred well, redirects towards the view of the Properties
        return JSP_MANAGE_PROPERTIES;
    }
}
