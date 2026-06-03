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
package fr.paris.lutece.portal.service.portlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletType;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.cache.ICacheKeyService;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.page.PortletCacheService;
import fr.paris.lutece.portal.service.page.PortletCustomAdminAction;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;

public class PortletContentService
{
    // Markers
    private static final String MARK_PORTLET = "portlet";
    private static final String MARK_STATUS_PUBLISHED = "portlet_status_published";
    private static final String MARK_STATUS_UNPUBLISHED = "portlet_status_unpublished";
    private static final String MARK_CUSTOM_ACTIONS = "custom_action_list";
    private static final String MARK_MAX_ORDER = "order_max";

    // Templates
    private static final String TEMPLATE_ADMIN_BUTTONS = "/admin/admin_buttons.html";

    // Properties
    private static final String PARAMETER_PLUGIN_NAME = "plugin-name";
    private static final String PARAMETER_PORTLET = "portlet";
    private static final int MODE_ADMIN = 1;
    protected static final String XSL_DEFAULT_VALUE = "XSL rendering not implemented. Please consider installing the XML transformer plugin (xmltransformer).";
    private static final String DEFAULT_OPEN_TAG_PREFIX = "<div class=\"lutece-admin-portlet\" draggable=\"true\">";
    private static final String DEFAULT_CLOSE_TAG_PREFIX = "</div>";
    private static final String ADMIN_PORTLET_OPEN_TAG = AppPropertiesService.getProperty( "lutece.portlet.open.tag", DEFAULT_OPEN_TAG_PREFIX );
    private static final String ADMIN_PORTLET_CLOSE_TAG = AppPropertiesService.getProperty( "lutece.portlet.close.tag", DEFAULT_CLOSE_TAG_PREFIX );
    private static final int DEFAULT_PORTLET_ORDER_MAX = 15;
    private static final int PORTLET_MAX_ORDER = AppPropertiesService.getPropertyInt( "lutece.list.order.max", DEFAULT_PORTLET_ORDER_MAX );
    
    // Specific for plugin-document
    private static final String DOCUMENT_LIST_PORTLET = "DOCUMENT_LIST_PORTLET";
    private static final String DOCUMENT_PORTLET = "DOCUMENT_PORTLET";
    private static final String DOCUMENT_ACTION_URL = "jsp/admin/plugins/document/ManagePublishing.jsp";
    private static final String DOCUMENT_IMAGE_URL = "images/admin/skin/actions/publish.png";
    private static final String DOCUMENT_TITLE = "portal.site.portletPreview.buttonManage";

    private ICacheKeyService _cksPortlet;
    private PortletCacheService _cachePortlets;

    @Inject
    public PortletContentService( PortletCacheService portletCacheService, ICacheKeyService portletCacheKeyService )
    {
        this._cachePortlets = portletCacheService;
        this._cksPortlet = portletCacheKeyService;
        _cachePortlets.initCache( );
    }
    
    /**
     * @param cacheKeyService
     *            the _cacheKeyService to set
     */
    public void setPortletCacheKeyService( ICacheKeyService cacheKeyService )
    {
        _cksPortlet = cacheKeyService;
    }
    
    /**
     * Gets the portlet cache service
     * 
     * @param portletCacheService
     *            the portlet cache service
     */
    public void setPortletCacheService( PortletCacheService portletCacheService )
    {
        _cachePortlets = portletCacheService;
    }

    public String getPortletContent( HttpServletRequest request, Portlet portlet, Map<String, String> mapRequestParams, int nMode ) throws SiteMessageException
    {
        if ( ( request != null ) && !isPortletVisible( request, portlet, nMode ) )
        {
            return StringUtils.EMPTY;
        }

        if ( request != null )
        {
            String strPluginName = portlet.getPluginName( );
            request.setAttribute( PARAMETER_PLUGIN_NAME, strPluginName );
        }

        String strPortletContent = StringUtils.EMPTY;

        // Add the admin buttons for portlet management on admin mode
        if ( nMode == MODE_ADMIN )
        {
            strPortletContent = ADMIN_PORTLET_OPEN_TAG + addAdminButtons( request, portlet );
        }

        String strKey = StringUtils.EMPTY;

        LuteceUser user = null;

        if ( SecurityService.isAuthenticationEnable( ) )
        {
            user = SecurityService.getInstance( ).getRegisteredUser( request );
        }

        boolean isCacheEnabled = nMode != MODE_ADMIN && _cachePortlets.isCacheEnable( );
        boolean bCanBeCached = user != null ? portlet.canBeCachedForConnectedUsers( ) : portlet.canBeCachedForAnonymousUsers( );

        if ( portlet.isContentGeneratedByXmlAndXsl( ) )
        {
            Map<String, String> mapParams = mapRequestParams;
            Map<String, String> mapXslParams = portlet.getXslParams( );

            if ( mapParams != null )
            {
                if ( mapXslParams != null )
                {
                    mapParams.putAll( mapXslParams );
                }
            }
            else
            {
                mapParams = mapXslParams;
            }

            if ( isCacheEnabled && bCanBeCached )
            {
                mapParams.put( PARAMETER_PORTLET, String.valueOf( portlet.getId( ) ) );
                strKey = _cksPortlet.getKey( mapParams, nMode, user );

                String strPortlet = (String) _cachePortlets.getFromCache( strKey );

                if ( strPortlet != null )
                {
                    return strPortlet;
                }
            }

            strPortletContent += getXslContent( request, portlet, mapParams, nMode );
        }
        else
        {
            if ( isCacheEnabled && bCanBeCached )
            {
                mapRequestParams.put( PARAMETER_PORTLET, String.valueOf( portlet.getId( ) ) );
                strKey = _cksPortlet.getKey( mapRequestParams, nMode, user );

                String strPortlet = (String) _cachePortlets.getFromCache( strKey );

                if ( strPortlet != null )
                {
                    return strPortlet;
                }
            }

            strPortletContent += portlet.getHtmlContent( request );
        }

        if ( isCacheEnabled && StringUtils.isNotEmpty( strKey ) )
        {
            _cachePortlets.putInCache( strKey, strPortletContent );
        }
        
        if ( nMode == MODE_ADMIN )
        {
            strPortletContent += ADMIN_PORTLET_CLOSE_TAG;
        }
        
        return strPortletContent;
    }

    private boolean isPortletVisible( HttpServletRequest request, Portlet portlet, int nMode )
    {
        if ( ( nMode != MODE_ADMIN ) && ( portlet.getStatus( ) == Portlet.STATUS_UNPUBLISHED ) )
        {
            return false;
        }

        String strRole = portlet.getRole( );
        boolean bUserInRole = SecurityService.isAuthenticationEnable( ) ? SecurityService.getInstance( ).isUserInRole( request, strRole ) : true;

        boolean [ ] conditions = new boolean [ ] {
                strRole.equals( Page.ROLE_NONE ), // No role is required so the portlet is visible for anyone
                !SecurityService.isAuthenticationEnable( ), // No authentication
                nMode == MODE_ADMIN, // We are in Admin mode, so all the portlet should be visible
                bUserInRole // The authentication is ON and the user get the role
        };

        return BooleanUtils.or( conditions );
    }

    /**
     * Add the HTML code to display admin buttons under each portlet
     *
     * @param request
     *            The Http request
     * @param portlet
     *            The portlet
     * @return The buttons code
     */
    private String addAdminButtons( HttpServletRequest request, Portlet portlet )
    {
        AdminUser user = AdminUserService.getAdminUser( request );

        if ( RBACService.isAuthorized( PortletType.RESOURCE_TYPE, portlet.getPortletTypeId( ), PortletResourceIdService.PERMISSION_MANAGE, user ) )
        {
            Locale locale = user.getLocale( );
            Collection<PortletCustomAdminAction> listCustomActions = new ArrayList<>( );

            // TODO : listCustomActions should be provided by PortletType
            // FIXME : Delete plugin-document specifics
            if ( portlet.getPortletTypeId( ).equals( DOCUMENT_LIST_PORTLET ) || portlet.getPortletTypeId( ).equals( DOCUMENT_PORTLET ) )
            {
                PortletCustomAdminAction customAction = new PortletCustomAdminAction( );
                customAction.setActionUrl( DOCUMENT_ACTION_URL );
                customAction.setImageUrl( DOCUMENT_IMAGE_URL );
                customAction.setTitle( DOCUMENT_TITLE );
                listCustomActions.add( customAction );
            }

            Map<String, Object> model = new HashMap<>( );
            model.put( MARK_PORTLET, portlet );
            model.put( MARK_STATUS_PUBLISHED, Portlet.STATUS_PUBLISHED );
            model.put( MARK_MAX_ORDER, PORTLET_MAX_ORDER );
            model.put( MARK_STATUS_UNPUBLISHED, Portlet.STATUS_UNPUBLISHED );
            model.put( MARK_CUSTOM_ACTIONS, listCustomActions );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADMIN_BUTTONS, locale, model );

            return template.getHtml( );
        }

        return StringUtils.EMPTY;
    }

    @Deprecated
    /**
     * @deprecated Gets the XSL content of the portlet, this method is for backward compatibility, portlets should render their contents without XSL
     *             transformation
     * @param request
     * @param portlet
     * @param mapParams
     * @param nMode
     * @return
     * @throws SiteMessageException
     */
    protected String getXslContent(HttpServletRequest request, Portlet portlet, Map<String, String> mapParams, int nMode) throws SiteMessageException
    {
        return XSL_DEFAULT_VALUE;
    }
}
