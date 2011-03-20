/*
 * Copyright (c) 2002-2010, Mairie de Paris
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
package fr.paris.lutece.portal.service.page;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.page.PageHome;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletType;
import fr.paris.lutece.portal.business.style.ModeHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.cache.CacheService;
import fr.paris.lutece.portal.service.cache.ICacheKeyService;
import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.html.XmlTransformerService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.image.ImageResourceManager;
import fr.paris.lutece.portal.service.image.ImageResourceProvider;
import fr.paris.lutece.portal.service.includes.PageInclude;
import fr.paris.lutece.portal.service.includes.PageIncludeService;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.portal.ThemesService;
import fr.paris.lutece.portal.service.portlet.PortletResourceIdService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;


/**
 * This class delivers pages to web componants. It handles XML tranformation to
 * HTML and provides a cache feature in order to reduce the number of
 * tranformations.
 */
public class PageService implements IPageService, ImageResourceProvider, PageEventListener
{
    ////////////////////////////////////////////////////////////////////////////
    // Variables

    // Redirection
    public static final String REDIRECTION_KEY = "redirect:";

    // Templates
    /** Access denied template */
    public static final String TEMPLATE_PAGE_ACCESS_DENIED = "/skin/site/page_access_denied.html";

    /** Access Controled template */
    public static final String TEMPLATE_PAGE_ACCESS_CONTROLED = "/skin/site/page_access_controled.html";
    private static final String TEMPLATE_ADMIN_BUTTONS = "/admin/admin_buttons.html";

    // Markers
    private static final String MARK_PORTLET = "portlet";
    private static final String MARK_STATUS_PUBLISHED = "portlet_status_published";
    private static final String MARK_STATUS_UNPUBLISHED = "portlet_status_unpublished";
    private static final String MARK_CUSTOM_ACTIONS = "custom_action_list";
    private static final String MARK_URL_LOGIN = "url_login";
    private static final String MARKER_TARGET = "target";

    // Parameters
    private static final String PARAMETER_SITE_PATH = "site-path";
    private static final String PARAMETER_PAGE_ID = "page-id";
    private static final String PARAMETER_USER_SELECTED_LOCALE = "user-selected-language";
    private static final String PARAMETER_PLUGIN_NAME = "plugin-name";
    private static final String PARAMETER_PORTLET = "portlet";

    // Properties
    private static final String PROPERTY_PAGE_SERVICE_CACHE = "service.pages.cache.enable";
    private static final String PROPERTY_MESSAGE_PAGE_ACCESS_DENIED = "portal.site.message.pageAccessDenied";
    private static final String CONTENT_SERVICE_NAME = "PageService";
    private static final String PROPERTY_COLUMN_MAX = "nb.columns";
    private static final int DEFAULT_COLUMN_MAX = 5;
    private static final String IMAGE_RESOURCE_TYPE_ID = "page_thumbnail";
    private static final String KEY_THEME = "theme";
    private static final String TARGET_TOP = "target='_top'";
    private static final String WELCOME_PAGE_ID = "1";
    private static final String WELCOME_PAGE_CACHE_KEY = "mode0";
    private static final int MODE_ADMIN = 1;
    private static final String XSL_UNIQUE_PREFIX = "page-";

    // Specific for plugin-document
    private static final String DOCUMENT_LIST_PORTLET = "DOCUMENT_LIST_PORTLET";
    private static final String DOCUMENT_PORTLET = "DOCUMENT_PORTLET";
    private static final String DOCUMENT_ACTION_URL = "jsp/admin/plugins/document/ManagePublishing.jsp";
    private static final String DOCUMENT_IMAGE_URL = "images/admin/skin/actions/publish.png";
    private static final String DOCUMENT_TITLE = "portal.site.portletPreview.buttonManage";
    private static final int MAX_COLUMNS = AppPropertiesService.getPropertyInt( PROPERTY_COLUMN_MAX, DEFAULT_COLUMN_MAX );
    private ArrayList<PageEventListener> _listEventListeners = new ArrayList<PageEventListener>(  );
    private ICacheKeyService _cksPage;
    private ICacheKeyService _cksPortlet;
    private PageCacheService _cachePages = new PageCacheService(  );
    private PortletCacheService _cachePortlets = new PortletCacheService(  );

    /**
     * Creates a new PageService object.
     */
    public PageService(  )
    {
        init(  );
    }

    /**
     * Initializes the service
     */
    private void init(  )
    {
        String strCachePages = AppPropertiesService.getProperty( PROPERTY_PAGE_SERVICE_CACHE, "true" );

        if ( strCachePages.equalsIgnoreCase( "true" ) )
        {
            _cachePages.initCache(  );
            _cachePortlets.initCache(  );
        }
        else
        {
            CacheService.registerCacheableService( _cachePages );
            CacheService.registerCacheableService( _cachePortlets );
        }

        ImageResourceManager.registerProvider( this );
        addPageEventListener( this );
    }

    /**
     * Returns the Content Service name
     *
     * @return The name as a String
     */
    public String getName(  )
    {
        return CONTENT_SERVICE_NAME;
    }

    /**
     * Returns the page for a given ID. The page is built using XML data of each
     * portlet or retrieved from the cache if it's enable.
     *
     * @param request The page ID
     * @param nMode The current mode.
     * @return The HTML code of the page as a String.
     * @throws SiteMessageException If a message shouldbe displayed
     */
    public String getPage( HttpServletRequest request, int nMode )
        throws SiteMessageException
    {
        String strPageId = request.getParameter( Parameters.PAGE_ID );

        return getPage( strPageId, nMode, request );
    }

    /**
     * Returns the page for a given ID. The page is built using XML data of each
     * portlet or retrieved from the cache if it's enable.
     *
     * @param strIdPage The page ID
     * @param nMode The current mode.
     * @param request The HttpRequest
     * @return The HTML code of the page as a String.
     * @throws SiteMessageException occurs when a site message need to be displayed
     */
    public String getPage( String strIdPage, int nMode, HttpServletRequest request )
        throws SiteMessageException
    {
        try
        {
            String strPage = "";

            // Get request paramaters and store them in a HashMap
            Enumeration<?> enumParam = request.getParameterNames(  );
            HashMap<String, String> htParamRequest = new HashMap<String, String>(  );
            String paramName = "";

            while ( enumParam.hasMoreElements(  ) )
            {
                paramName = (String) enumParam.nextElement(  );
                htParamRequest.put( paramName, request.getParameter( paramName ) );
            }

            LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );
            String strUserTheme = ThemesService.getUserTheme( request );

            if ( strUserTheme != null )
            {
                htParamRequest.put( KEY_THEME, strUserTheme );
            }

            // The cache is enable !
            if ( _cachePages.isCacheEnable(  ) )
            {
                // we add the key in the memory key only if cache is enable
                String strKey = getKey( htParamRequest, nMode, user );

                // get page from cache
                strPage = (String) _cachePages.getFromCache( strKey );

                if ( strPage == null )
                {
                    // only one thread can evaluate the page
                    synchronized ( strKey )
                    {
                        // can be useful if an other thread had evaluate the
                        // page
                        strPage = (String) _cachePages.getFromCache( strKey );

                        // ignore checkstyle, this double verification is useful
                        // when page cache has been created when thread is
                        // blocked on synchronized
                        if ( strPage == null )
                        {
                            Boolean bCanBeCached = Boolean.TRUE;

                            AppLogService.debug( "Page generation " + strKey );

                            RedirectionResponseWrapper response = new RedirectionResponseWrapper( LocalVariables.getResponse(  ) );

                            LocalVariables.setLocal( LocalVariables.getConfig(  ), LocalVariables.getRequest(  ),
                                response );

                            // The key is not in the cache, so we have to build
                            // the page
                            strPage = buildPageContent( strIdPage, nMode, request, bCanBeCached );

                            if ( response.getRedirectLocation(  ) != null )
                            {
                                AppLogService.debug( "Redirection found " + response.getRedirectLocation(  ) );
                                strPage = REDIRECTION_KEY + response.getRedirectLocation(  );
                            }

                            // Add the page to the cache if the page can be
                            // cached
                            if ( bCanBeCached.booleanValue(  ) )
                            {
                                _cachePages.putInCache( strKey, strPage );
                            }
                        }
                        else
                        {
                            AppLogService.debug( "Page read from cache after synchronisation " + strKey );
                        }
                    }
                }
                else
                {
                    AppLogService.debug( "Page read from cache " + strKey );
                }

                // redirection handling
                if ( strPage.startsWith( REDIRECTION_KEY ) )
                {
                    strPage = strPage.replaceFirst( REDIRECTION_KEY, "" );

                    try
                    {
                        LocalVariables.getResponse(  ).sendRedirect( strPage );
                    }
                    catch ( IOException e )
                    {
                        AppLogService.error( "Error on sendRedirect for " + strPage );
                    }
                }
            }
            else
            {
                Boolean bCanBeCached = Boolean.FALSE;
                strPage = buildPageContent( strIdPage, nMode, request, bCanBeCached );
            }

            strPage = setPageBaseUrl( request, strPage );

            return strPage;
        }
        catch ( NumberFormatException nfe )
        {
            AppLogService.error( "PageService.getPage() : " + nfe.getLocalizedMessage(  ), nfe );

            throw new PageNotFoundException(  );
        }
    }

    /**
     * Build the page content.
     *
     * @param strIdPage The page ID
     * @param nMode The current mode.
     * @param request The HttpRequest
     * @param bCanBeCached The boolean
     * @return The HTML code of the page as a String.
     * @throws SiteMessageException occurs when a site message need to be displayed
     */
    public String buildPageContent( String strIdPage, int nMode, HttpServletRequest request, Boolean bCanBeCached )
        throws SiteMessageException
    {
        int nIdPage = 0;
        Page page = null;

        nIdPage = Integer.parseInt( strIdPage );

        boolean bPageExist = PageHome.checkPageExist( nIdPage );

        if ( bPageExist )
        {
            page = PageHome.getPage( nIdPage );
        }
        else
        {
            // If there is a problem finding the page, returns the home page
            nIdPage = PortalService.getRootPageId(  );
            page = PageHome.getPage( nIdPage );
        }

        PageData data = new PageData(  );
        data.setName( page.getName(  ) );
        data.setPagePath( PortalService.getPagePathContent( nIdPage, nMode, request ) );
        data.setTheme( page.getCodeTheme(  ) );
        data.setMetaKeywords( page.getMetaKeywords(  ) );
        data.setMetaDescription( page.getMetaDescription(  ) );

        // Checks the page role (v1.1)
        String strRole = page.getRole(  );

        if ( !strRole.equals( Page.ROLE_NONE ) && ( SecurityService.isAuthenticationEnable(  ) ) &&
                ( nMode != MODE_ADMIN ) )
        {
            LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );

            if ( ( user == null ) && ( !SecurityService.getInstance(  ).isExternalAuthentication(  ) ) )
            {
                // The user is not registered and identify itself with the Portal authentication
                String strAccessControledTemplate = SecurityService.getInstance(  ).getAccessControledTemplate(  );
                HashMap<String, Object> model = new HashMap<String, Object>(  );
                String strLoginUrl = SecurityService.getInstance(  ).getLoginPageUrl(  );
                model.put( MARK_URL_LOGIN, strLoginUrl );

                HtmlTemplate tAccessControled = AppTemplateService.getTemplate( strAccessControledTemplate,
                        request.getLocale(  ), model );

                data.setContent( tAccessControled.getHtml(  ) );

                return PortalService.buildPageContent( nIdPage, data, nMode, request );
            }

            if ( !SecurityService.getInstance(  ).isUserInRole( request, strRole ) )
            {
                // The user doesn't have the correct role
                String strAccessDeniedTemplate = SecurityService.getInstance(  ).getAccessDeniedTemplate(  );
                HtmlTemplate tAccessDenied = AppTemplateService.getTemplate( strAccessDeniedTemplate,
                        request.getLocale(  ) );
                data.setContent( tAccessDenied.getHtml(  ) );

                return PortalService.buildPageContent( nIdPage, data, nMode, request );
            }
        }

        // Added in v2.0
        // Add the page authorization
        if ( nMode == MODE_ADMIN )
        {
            AdminUser user = AdminUserService.getAdminUser( request );

            if ( isAuthorizedAdminPage( nIdPage, PageResourceIdService.PERMISSION_VIEW, user ) )
            {
                // Fill a PageData structure for those elements
                data.setContent( getPageContent( nIdPage, nMode, request ) );
            }
            else
            {
                data.setContent( I18nService.getLocalizedString( PROPERTY_MESSAGE_PAGE_ACCESS_DENIED, user.getLocale(  ) ) );
            }
        }
        else
        {
            data.setContent( getPageContent( nIdPage, nMode, request ) );
        }

        if ( nIdPage == PortalService.getRootPageId(  ) )
        {
            // This page is the home page.
            data.setHomePage( true );
        }

        return PortalService.buildPageContent( nIdPage, data, nMode, request );
    }

    /**
     * Build the page content.
     *
     * @param nIdPage The page ID
     * @param nMode The current mode.
     * @param request The HttpRequest
     * @return The HTML code of the page as a String.
     * @throws SiteMessageException occurs when a site message need to be displayed
     */
    public String getPageContent( int nIdPage, int nMode, HttpServletRequest request )
        throws SiteMessageException
    {
        String[] arrayContent = new String[MAX_COLUMNS];

        for ( int i = 0; i < MAX_COLUMNS; i++ )
        {
            arrayContent[i] = "";
        }

        Page page = PageHome.findByPrimaryKey( nIdPage );
        Map<String, String> mapParams = getParams( request, nMode, nIdPage );

        for ( Portlet portlet : page.getPortlets(  ) )
        {
            int nCol = portlet.getColumn(  ) - 1;

            if ( nCol < MAX_COLUMNS )
            {
                arrayContent[nCol] += getPortletContent( request, portlet, mapParams, nMode );
            }
        }

        HashMap<String, Object> rootModel = new HashMap<String, Object>(  );

        for ( int j = 0; j < MAX_COLUMNS; j++ )
        {
            rootModel.put( "page_content_col" + ( j + 1 ), arrayContent[j] );
        }

        List<PageInclude> listIncludes = PageIncludeService.getIncludes(  );
        PageData data = new PageData(  );

        for ( PageInclude pic : listIncludes )
        {
            pic.fillTemplate( rootModel, data, nMode, request );
        }

        HtmlTemplate t = AppTemplateService.getTemplate( page.getTemplate(  ),
                ( request == null ) ? null : request.getLocale(  ), rootModel );

        return t.getHtml(  );
    }

    private String getPortletContent( HttpServletRequest request, Portlet portlet, Map<String, String> mapParams,
        int nMode ) throws SiteMessageException
    {
        if ( ( nMode != MODE_ADMIN ) && ( portlet.getStatus(  ) == Portlet.STATUS_UNPUBLISHED ) )
        {
            return "";
        }

        String strRole = portlet.getRole(  );

        if ( !strRole.equals( Page.ROLE_NONE ) && ( SecurityService.isAuthenticationEnable(  ) ) &&
                ( nMode != MODE_ADMIN ) )
        {
            if ( !SecurityService.getInstance(  ).isUserInRole( request, strRole ) )
            {
                return "";
            }
        }

        Map<String, String> mapXslParams = portlet.getXslParams(  );

        if ( mapParams != null )
        {
            if ( mapXslParams != null )
            {
                for ( String strKey : mapXslParams.keySet(  ) )
                {
                    mapParams.put( strKey, mapXslParams.get( strKey ) );
                }
            }
        }
        else
        {
            mapParams = mapXslParams;
        }

        Properties outputProperties = ModeHome.getOuputXslProperties( nMode );

        if ( request != null )
        {
            String strPluginName = portlet.getPluginName(  );
            request.setAttribute( PARAMETER_PLUGIN_NAME, strPluginName );
        }

        String strXslUniqueId = XSL_UNIQUE_PREFIX + String.valueOf( portlet.getStyleId(  ) );

        String strKey = "";

        if ( ( ( nMode != MODE_ADMIN ) && _cachePortlets.isCacheEnable(  ) ) )
        {
            LuteceUser user = null;

            if ( SecurityService.isAuthenticationEnable(  ) )
            {
                user = SecurityService.getInstance(  ).getRegisteredUser( request );
            }

            mapParams.put( PARAMETER_PORTLET, String.valueOf( portlet.getId(  ) ) );
            strKey = _cksPortlet.getKey( mapParams, nMode, user );

            String strPortlet = (String) _cachePortlets.getFromCache( strKey );

            if ( strPortlet != null )
            {
                return strPortlet;
            }
        }

        XmlTransformerService xmlTransformerService = new XmlTransformerService(  );
        String strPortletXmlContent = portlet.getXml( request );
        String strPortletContent = xmlTransformerService.transformBySourceWithXslCache( strPortletXmlContent,
                portlet.getXslSource( nMode ), strXslUniqueId, mapParams, outputProperties );

        if ( _cachePortlets.isCacheEnable(  ) )
        {
            _cachePortlets.putInCache( strKey, strPortletContent );
        }

        // Add the admin buttons for portlet management on admin mode
        if ( nMode == MODE_ADMIN )
        {
            strPortletContent += addAdminButtons( request, portlet );
        }

        return strPortletContent;
    }

    /**
     * Build the Cache HashMap key for pages Goal is to be able to have a
     * synchronized on the key but a synchronize only work with strict
     * reference. So we manage an hashmap of string reference for cache keys to
     * be able to get them back.
     *
     * @param mapParams The Map params
     * @param nMode The current mode.
     * @param user Current Lutece user
     * @return The HashMap key for articles pages as a String.
     */
    private String getKey( Map<String, String> mapParams, int nMode, LuteceUser user )
    {
        String strKey = _cksPage.getKey( mapParams, nMode, user );

        return _cachePages.getKey( strKey );
    }

    /**
     * Remove a page from the cache
     *
     * @param nIdPage The page ID
     */
    private void invalidatePage( int nIdPage )
    {
        String strIdPage = String.valueOf( nIdPage );
        invalidatePage( strIdPage );
    }

    /**
     * @param cacheKeyService the _cacheKeyService to set
     */
    public void setPageCacheKeyService( ICacheKeyService cacheKeyService )
    {
        _cksPage = cacheKeyService;
    }

    /**
     * @param cacheKeyService the _cacheKeyService to set
     */
    public void setPortletCacheKeyService( ICacheKeyService cacheKeyService )
    {
        _cksPortlet = cacheKeyService;
    }

    /**
     * Remove a page from the cache
     *
     * @param strIdPage The page ID
     */
    private void invalidatePage( String strIdPage )
    {
        String strKey = Parameters.PAGE_ID + "'" + strIdPage + "'";

        if ( _cachePages.isCacheEnable(  ) )
        {
            for ( String strKeyTemp : (List<String>) _cachePages.getCache(  ).getKeys(  ) )
            {
                // FIXME Portal.jsp (welcome page) is cached as "mode0", and is actually page_id = 1.
                if ( ( strKeyTemp.indexOf( strKey ) != -1 ) ||
                        ( WELCOME_PAGE_ID.equals( strIdPage ) && WELCOME_PAGE_CACHE_KEY.equals( strKeyTemp ) ) )
                {
                    _cachePages.getCache(  ).remove( strKeyTemp );

                    if ( AppLogService.isDebugEnabled(  ) )
                    {
                        AppLogService.debug( "Page (cache key : " + strKeyTemp + ") removed from the cache." );
                    }
                }
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////
    // Events Listeners management
    /**
     * Add a new page event listener
     *
     * @param listener An event listener to add
     */
    public void addPageEventListener( PageEventListener listener )
    {
        _listEventListeners.add( listener );
        AppLogService.info( "New Page Event Listener registered : " + listener.getClass(  ).getName(  ) );
    }

    /**
     * Notify an event to all listeners
     *
     * @param event A page Event
     */
    private void notifyListeners( PageEvent event )
    {
        for ( PageEventListener listener : _listEventListeners )
        {
            listener.processPageEvent( event );
        }
    }

    /**
     * Returns the resource type Id
     * @return The resource type Id
     */
    public String getResourceTypeId(  )
    {
        return IMAGE_RESOURCE_TYPE_ID;
    }

    /**
     * Gets the image resource for a given resource
     *
     * @param nIdResource The Resource id
     * @return The image resource
     */
    public ImageResource getImageResource( int nIdResource )
    {
        return PageHome.getImageResource( nIdResource );
    }

    /**
     * Create a page
     * @param page The page to create
     */
    public void createPage( Page page )
    {
        PageHome.create( page );

        PageEvent event = new PageEvent( page, PageEvent.PAGE_CREATED );
        notifyListeners( event );
    }

    /**
     * Update a given page
     * @param page The page to update
     */
    public void updatePage( Page page )
    {
        PageHome.update( page );

        PageEvent event = new PageEvent( page, PageEvent.PAGE_CONTENT_MODIFIED );
        notifyListeners( event );
    }

    /**
     * Remove a given page
     * @param nPageId The page Id
     */
    public void removePage( int nPageId )
    {
        Page page = PageHome.findByPrimaryKey( nPageId );
        PageEvent event = new PageEvent( page, PageEvent.PAGE_DELETED );
        PageHome.remove( nPageId );
        notifyListeners( event );
    }

    /**
     * Process a page event
     * @param event The event to process
     */
    public void processPageEvent( PageEvent event )
    {
        Page page = event.getPage(  );
        invalidatePage( page.getId(  ) );

        // Clearing ALL cache is not needed anymore
        //PortalService.resetCache(  );
    }

    /**
     * Invalidate Page Content
     * @param nPageId The Page ID
     */
    public void invalidateContent( int nPageId )
    {
        Page page = PageHome.findByPrimaryKey( nPageId );
        PageEvent event = new PageEvent( page, PageEvent.PAGE_CONTENT_MODIFIED );
        notifyListeners( event );
    }

    /**
     * Check if a page should be visible to the user according its workgroup
     *
     * @param nIdPage the id of the page to check
     * @param user The current user
     * @return true if authorized, otherwise false
     */
    private boolean isAuthorizedAdminPageByWorkGroup( int nIdPage, AdminUser user )
    {
        Page page = PageHome.findByPrimaryKey( nIdPage );

        if ( AdminWorkgroupService.isAuthorized( page, user ) )
        {
            if ( page.getId(  ) != PortalService.getRootPageId(  ) )
            {
                return isAuthorizedAdminPageByWorkGroup( page.getParentPageId(  ), user );
            }

            return true;
        }

        return false;
    }

    /**
     * Check that a given user is allowed to access a page for a given
     * permission and his workgroups
     *
     * @param nIdPage the id of the page to check
     * @param strPermission the permission needed
     * @param user The current user
     * @return true if authorized, otherwise false
     */
    public boolean isAuthorizedAdminPage( int nIdPage, String strPermission, AdminUser user )
    {
        String strAuthorizationNode = Integer.toString( Page.getAuthorizationNode( nIdPage ) );

        return ( RBACService.isAuthorized( Page.RESOURCE_TYPE, strAuthorizationNode, strPermission, user ) &&
        isAuthorizedAdminPageByWorkGroup( nIdPage, user ) );
    }

    private String addAdminButtons( HttpServletRequest request, Portlet portlet )
    {
        AdminUser user = AdminUserService.getAdminUser( request );

        if ( RBACService.isAuthorized( PortletType.RESOURCE_TYPE, portlet.getPortletTypeId(  ),
                    PortletResourceIdService.PERMISSION_MANAGE, user ) )
        {
            Locale locale = user.getLocale(  );
            Collection<PortletCustomAdminAction> listCustomActions = new ArrayList<PortletCustomAdminAction>(  );

            // TODO : listCustomActions should be provided by PortletType
            // FIXME : Delete plugin-document specifics
            if ( portlet.getPortletTypeId(  ).equals( DOCUMENT_LIST_PORTLET ) ||
                    portlet.getPortletTypeId(  ).equals( DOCUMENT_PORTLET ) )
            {
                PortletCustomAdminAction customAction = new PortletCustomAdminAction(  );
                customAction.setActionUrl( DOCUMENT_ACTION_URL );
                customAction.setImageUrl( DOCUMENT_IMAGE_URL );
                customAction.setTitle( DOCUMENT_TITLE );
                listCustomActions.add( customAction );
            }

            Map<String, Object> model = new HashMap<String, Object>(  );
            model.put( MARK_PORTLET, portlet );
            model.put( MARK_STATUS_PUBLISHED, Portlet.STATUS_PUBLISHED );
            model.put( MARK_STATUS_UNPUBLISHED, Portlet.STATUS_UNPUBLISHED );
            model.put( MARK_CUSTOM_ACTIONS, listCustomActions );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADMIN_BUTTONS, locale, model );

            return template.getHtml(  );
        }

        return "";
    }

    private Map<String, String> getParams( HttpServletRequest request, int nMode, int nIdPage )
    {
        Map<String, String> mapModifyParam = new HashMap<String, String>(  );
        String paramName = "";

        // Get request paramaters and store them in a HashMap
        if ( request != null )
        {
            Enumeration<?> enumParam = request.getParameterNames(  );

            while ( enumParam.hasMoreElements(  ) )
            {
                paramName = (String) enumParam.nextElement(  );
                mapModifyParam.put( paramName, request.getParameter( paramName ) );
            }

            // Add selected locale
            mapModifyParam.put( PARAMETER_USER_SELECTED_LOCALE,
                LocaleService.getUserSelectedLocale( request ).getLanguage(  ) );
        }

        // Added in v1.3
        // Add a path param for choose url to use in admin or normal mode
        if ( nMode != MODE_ADMIN )
        {
            mapModifyParam.put( PARAMETER_SITE_PATH, AppPathService.getPortalUrl(  ) );
        }
        else
        {
            mapModifyParam.put( PARAMETER_SITE_PATH, AppPathService.getAdminPortalUrl(  ) );
            mapModifyParam.put( MARKER_TARGET, TARGET_TOP );
        }

        // Add current page id
        mapModifyParam.put( PARAMETER_PAGE_ID, Integer.toString( nIdPage ) );

        return mapModifyParam;
    }

    private String setPageBaseUrl( HttpServletRequest request, String strPage )
    {
        String strBase = AppPathService.getBaseUrl( request );
        boolean bBefore = strPage.contains( strBase );

        strPage = strPage.replaceFirst( "<base href=\".*\" >", "<base href=\"" + strBase + "\" >" );

        boolean bAfter = strPage.contains( strBase );

        AppLogService.debug( "Replacement of <base href=\"***\"> : base=" + strBase + ", before=" + bBefore +
            ", after=" + bAfter );

        return strPage;
    }
}
