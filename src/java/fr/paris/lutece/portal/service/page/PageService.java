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
package fr.paris.lutece.portal.service.page;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.page.PageHome;
import fr.paris.lutece.portal.business.page.PageRoleRemovalListener;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletRoleRemovalListener;
import fr.paris.lutece.portal.business.portlet.PortletType;
import fr.paris.lutece.portal.business.style.ModeHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
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
import fr.paris.lutece.portal.service.portlet.PortletEvent;
import fr.paris.lutece.portal.service.portlet.PortletEventListener;
import fr.paris.lutece.portal.service.portlet.PortletResourceIdService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.util.RemovalListenerService;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.inject.Inject;

import javax.servlet.http.HttpServletRequest;


/**
 * This class delivers pages to web componants. It handles XML tranformation to
 * HTML and provides a cache feature in order to reduce the number of
 * tranformations.
 */
public class PageService implements IPageService, ImageResourceProvider, PageEventListener, PortletEventListener
{
    // //////////////////////////////////////////////////////////////////////////
    // Variables

    /**
     * Key for redirections
     */
    public static final String REDIRECTION_KEY = "redirect:";

    // Templates
    /** Access denied template */
    public static final String TEMPLATE_PAGE_ACCESS_DENIED = "/skin/site/page_access_denied.html";

    /** Access Controled template */
    public static final String TEMPLATE_PAGE_ACCESS_CONTROLED = "/skin/site/page_access_controled.html";
    private static final String TEMPLATE_ADMIN_BUTTONS = "/admin/admin_buttons.html";
    private static final String TEMPLATE_COLUMN_OUTLINE = "/admin/column_outline.html";

    // Markers
    private static final String MARK_PORTLET = "portlet";
    private static final String MARK_STATUS_PUBLISHED = "portlet_status_published";
    private static final String MARK_STATUS_UNPUBLISHED = "portlet_status_unpublished";
    private static final String MARK_CUSTOM_ACTIONS = "custom_action_list";
    private static final String MARK_URL_LOGIN = "url_login";
    private static final String MARKER_TARGET = "target";
    private static final String MARKER_IS_USER_AUTHENTICATED = "is-user-authenticated";
    private static final String MARK_COLUMN_CONTENT = "column_content";
    private static final String MARK_COLUMN_ID = "column_id";

    // Parameters
    private static final String PARAMETER_SITE_PATH = "site-path";
    private static final String PARAMETER_USER_SELECTED_LOCALE = "user-selected-language";
    private static final String PARAMETER_PLUGIN_NAME = "plugin-name";
    private static final String PARAMETER_PORTLET = "portlet";

    // Properties
    private static final String PROPERTY_MESSAGE_PAGE_ACCESS_DENIED = "portal.site.message.pageAccessDenied";
    private static final String CONTENT_SERVICE_NAME = "PageService";
    private static final String PROPERTY_COLUMN_MAX = "nb.columns";
    private static final int DEFAULT_COLUMN_MAX = 5;
    private static final String KEY_THEME = "theme";
    private static final String TARGET_TOP = "target='_top'";
    private static final String WELCOME_PAGE_ID = "1";
    private static final String WELCOME_PAGE_CACHE_KEY = "mode0";
    private static final int MODE_ADMIN = 1;
    private static final String VALUE_TRUE = "1";
    private static final String VALUE_FALSE = "0";
    private static final String XSL_UNIQUE_PREFIX = "page-";
    private static final String ATTRIBUTE_CORE_CAN_PAGE_BE_CACHED = "core.canPageBeCached";

    // Specific for plugin-document
    private static final String DOCUMENT_LIST_PORTLET = "DOCUMENT_LIST_PORTLET";
    private static final String DOCUMENT_PORTLET = "DOCUMENT_PORTLET";
    private static final String DOCUMENT_ACTION_URL = "jsp/admin/plugins/document/ManagePublishing.jsp";
    private static final String DOCUMENT_IMAGE_URL = "images/admin/skin/actions/publish.png";
    private static final String DOCUMENT_TITLE = "portal.site.portletPreview.buttonManage";
    private static final int MAX_COLUMNS = AppPropertiesService.getPropertyInt( PROPERTY_COLUMN_MAX, DEFAULT_COLUMN_MAX );
    private static List<PageEventListener> _listEventListeners = new ArrayList<PageEventListener>(  );
    private ICacheKeyService _cksPage;
    private ICacheKeyService _cksPortlet;
    private PageCacheService _cachePages;
    private PortletCacheService _cachePortlets;

    /**
     * Creates a new PageService object.
     * @deprecated use
     *             {@link #PageService(PageCacheService, PortletCacheService)}
     *             instead.
     */
    @Deprecated
    public PageService(  )
    {
        init(  );
    }

    /**
     * Creates a new PageService object.
     * @param pageCacheService the page cache service
     * @param portletCacheService the portlet cache service
     */
    @Inject
    public PageService( PageCacheService pageCacheService, PortletCacheService portletCacheService )
    {
        _cachePages = pageCacheService;
        _cachePortlets = portletCacheService;
        init(  );
    }

    /**
     * Initializes the service
     */
    private void init(  )
    {
        _cachePages.initCache(  );
        _cachePortlets.initCache(  );
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
     * @param request
     *            The page ID
     * @param nMode
     *            The current mode.
     * @return The HTML code of the page as a String.
     * @throws SiteMessageException
     *             If a message shouldbe displayed
     */
    @Override
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
     * @param strIdPage
     *            The page ID
     * @param nMode
     *            The current mode.
     * @param request
     *            The HttpRequest
     * @return The HTML code of the page as a String.
     * @throws SiteMessageException
     *             occurs when a site message need to be displayed
     */
    @Override
    public String getPage( String strIdPage, int nMode, HttpServletRequest request )
        throws SiteMessageException
    {
        try
        {
            String strPage = "";

            // The cache is enable !
            if ( _cachePages.isCacheEnable(  ) )
            {
                // Get request paramaters and store them in a HashMap
                Enumeration<?> enumParam = request.getParameterNames(  );
                HashMap<String, String> htParamRequest = new HashMap<String, String>(  );
                String paramName = "";

                while ( enumParam.hasMoreElements(  ) )
                {
                    paramName = (String) enumParam.nextElement(  );
                    htParamRequest.put( paramName, request.getParameter( paramName ) );
                }

                if ( !htParamRequest.containsKey( Parameters.PAGE_ID ) )
                {
                    htParamRequest.put( Parameters.PAGE_ID, strIdPage );
                }

                if ( !htParamRequest.containsKey( Parameters.BASE_URL ) )
                {
                    htParamRequest.put( Parameters.BASE_URL, AppPathService.getBaseUrl( request ) );
                }

                LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );
                String strUserTheme = ThemesService.getUserTheme( request );

                if ( strUserTheme != null )
                {
                    htParamRequest.put( KEY_THEME, strUserTheme );
                }

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
                            request.setAttribute( ATTRIBUTE_CORE_CAN_PAGE_BE_CACHED, null );
                            // The key is not in the cache, so we have to build
                            // the page
                            strPage = buildPageContent( strIdPage, nMode, request, bCanBeCached );

                            // We check if the page contains portlets that can not be cached. 
                            if ( ( request.getAttribute( ATTRIBUTE_CORE_CAN_PAGE_BE_CACHED ) != null ) &&
                                    !(Boolean) request.getAttribute( ATTRIBUTE_CORE_CAN_PAGE_BE_CACHED ) )
                            {
                                bCanBeCached = Boolean.FALSE;
                            }

                            if ( response.getRedirectLocation(  ) != null )
                            {
                                AppLogService.debug( "Redirection found " + response.getRedirectLocation(  ) );
                                strPage = REDIRECTION_KEY + response.getRedirectLocation(  );
                            }

                            // Add the page to the cache if the page can be
                            // cached
                            if ( bCanBeCached.booleanValue(  ) && ( nMode != MODE_ADMIN ) )
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
     * @param strIdPage
     *            The page ID
     * @param nMode
     *            The current mode.
     * @param request
     *            The HttpRequest
     * @param bCanBeCached
     *            The boolean
     * @return The HTML code of the page as a String.
     * @throws SiteMessageException
     *             occurs when a site message need to be displayed
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
                // The user is not registered and identify itself with the
                // Portal authentication
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
     * @param nIdPage
     *            The page ID
     * @param nMode
     *            The current mode.
     * @param request
     *            The HttpRequest
     * @return The HTML code of the page as a String.
     * @throws SiteMessageException
     *             occurs when a site message need to be displayed
     */
    @Override
    public String getPageContent( int nIdPage, int nMode, HttpServletRequest request )
        throws SiteMessageException
    {
        Locale locale = ( request == null ) ? LocaleService.getDefault(  ) : request.getLocale(  );

        String[] arrayContent = new String[MAX_COLUMNS];

        for ( int i = 0; i < MAX_COLUMNS; i++ )
        {
            arrayContent[i] = "";
        }

        Page page = PageHome.findByPrimaryKey( nIdPage );
        Map<String, String> mapParams = getParams( request, nMode, nIdPage );
        boolean bCanPageBeCached = Boolean.TRUE;
        LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );

        for ( Portlet portlet : page.getPortlets(  ) )
        {
            int nCol = portlet.getColumn(  ) - 1;

            if ( nCol < MAX_COLUMNS )
            {
                arrayContent[nCol] += getPortletContent( request, portlet, mapParams, nMode );
            }

            // We check if the portlet can be cached
            if ( ( user != null ) ? ( !portlet.canBeCachedForConnectedUsers(  ) )
                                      : ( !portlet.canBeCachedForAnonymousUsers(  ) ) )
            {
                bCanPageBeCached = false;
            }
        }

        // Add columns outline in admin mode
        if ( nMode == MODE_ADMIN )
        {
            for ( int i = 0; i < MAX_COLUMNS; i++ )
            {
                arrayContent[i] = addColumnOutline( i + 1, arrayContent[i], locale );
            }
        }

        // We save that the page that is generating can not be cached
        if ( !bCanPageBeCached )
        {
            request.setAttribute( ATTRIBUTE_CORE_CAN_PAGE_BE_CACHED, false );
        }

        Map<String, Object> rootModel = new HashMap<String, Object>(  );

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

        HtmlTemplate t = AppTemplateService.getTemplate( page.getTemplate(  ), locale, rootModel );

        return t.getHtml(  );
    }

    /**
     * Add the HTML code to display column outlines
     *
     * @param columnId
     *            the column id
     * @param content
     *            the column content
     * @param locale
     *            the locale
     * @return The column code
     */
    private String addColumnOutline( int columnId, String content, Locale locale )
    {
        Map<String, Object> model = new HashMap<String, Object>( 2 );
        model.put( MARK_COLUMN_CONTENT, content );
        model.put( MARK_COLUMN_ID, columnId );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_COLUMN_OUTLINE, locale, model );

        return template.getHtml(  );
    }

    /**
     * Get the portlet content
     *
     * @param request
     *            The HTTP request
     * @param portlet
     *            The portlet
     * @param mapRequestParams
     *            request parameters
     * @param nMode
     *            The mode
     * @return The content
     * @throws SiteMessageException
     *             If an error occurs
     */
    private String getPortletContent( HttpServletRequest request, Portlet portlet,
        Map<String, String> mapRequestParams, int nMode )
        throws SiteMessageException
    {
        if ( ( nMode != MODE_ADMIN ) && ( portlet.getStatus(  ) == Portlet.STATUS_UNPUBLISHED ) )
        {
            return StringUtils.EMPTY;
        }

        String strRole = portlet.getRole(  );

        if ( !strRole.equals( Page.ROLE_NONE ) && ( SecurityService.isAuthenticationEnable(  ) ) &&
                ( nMode != MODE_ADMIN ) )
        {
            if ( !SecurityService.getInstance(  ).isUserInRole( request, strRole ) )
            {
                return StringUtils.EMPTY;
            }
        }

        if ( request != null )
        {
            String strPluginName = portlet.getPluginName(  );
            request.setAttribute( PARAMETER_PLUGIN_NAME, strPluginName );
        }

        String strPortletContent = StringUtils.EMPTY;

        // Add the admin buttons for portlet management on admin mode
        if ( nMode == MODE_ADMIN )
        {
            strPortletContent = addAdminButtons( request, portlet );
        }

        String strKey = StringUtils.EMPTY;

        if ( portlet.isContentGeneratedByXmlAndXsl(  ) )
        {
            Map<String, String> mapParams = mapRequestParams;
            Map<String, String> mapXslParams = portlet.getXslParams(  );

            if ( mapParams != null )
            {
                if ( mapXslParams != null )
                {
                    for ( Entry<String, String> entry : mapXslParams.entrySet(  ) )
                    {
                        mapParams.put( entry.getKey(  ), entry.getValue(  ) );
                    }
                }
            }
            else
            {
                mapParams = mapXslParams;
            }

            Properties outputProperties = ModeHome.getOuputXslProperties( nMode );

            String strXslUniqueId = XSL_UNIQUE_PREFIX + String.valueOf( portlet.getStyleId(  ) );

            if ( ( ( nMode != MODE_ADMIN ) && _cachePortlets.isCacheEnable(  ) ) )
            {
                LuteceUser user = null;

                if ( SecurityService.isAuthenticationEnable(  ) )
                {
                    user = SecurityService.getInstance(  ).getRegisteredUser( request );
                }

                boolean bCanBeCached = ( user != null ) ? ( portlet.canBeCachedForConnectedUsers(  ) )
                                                        : ( portlet.canBeCachedForAnonymousUsers(  ) );

                if ( bCanBeCached )
                {
                    mapParams.put( PARAMETER_PORTLET, String.valueOf( portlet.getId(  ) ) );
                    strKey = _cksPortlet.getKey( mapParams, nMode, user );

                    String strPortlet = (String) _cachePortlets.getFromCache( strKey );

                    if ( strPortlet != null )
                    {
                        return strPortlet;
                    }
                }
            }

            XmlTransformerService xmlTransformerService = new XmlTransformerService(  );
            String strPortletXmlContent = portlet.getXml( request );
            strPortletContent += xmlTransformerService.transformBySourceWithXslCache( strPortletXmlContent,
                portlet.getXslSource( nMode ), strXslUniqueId, mapParams, outputProperties );
        }
        else
        {
            if ( ( ( nMode != MODE_ADMIN ) && _cachePortlets.isCacheEnable(  ) ) )
            {
                LuteceUser user = null;

                if ( SecurityService.isAuthenticationEnable(  ) )
                {
                    user = SecurityService.getInstance(  ).getRegisteredUser( request );
                }

                boolean bCanBeCached = ( user != null ) ? ( portlet.canBeCachedForConnectedUsers(  ) )
                                                        : ( portlet.canBeCachedForAnonymousUsers(  ) );

                if ( bCanBeCached )
                {
                    mapRequestParams.put( PARAMETER_PORTLET, String.valueOf( portlet.getId(  ) ) );
                    strKey = _cksPortlet.getKey( mapRequestParams, nMode, user );

                    String strPortlet = (String) _cachePortlets.getFromCache( strKey );

                    if ( strPortlet != null )
                    {
                        return strPortlet;
                    }
                }
            }

            strPortletContent += portlet.getHtmlContent( request );
        }

        if ( ( nMode != MODE_ADMIN ) && _cachePortlets.isCacheEnable(  ) && StringUtils.isNotEmpty( strKey ) )
        {
            _cachePortlets.putInCache( strKey, strPortletContent );
        }

        return strPortletContent;
    }

    /**
     * Build the Cache HashMap key for pages Goal is to be able to have a
     * synchronized on the key but a synchronize only work with strict
     * reference. So we manage an hashmap of string reference for cache keys to
     * be able to get them back.
     *
     * @param mapParams
     *            The Map params
     * @param nMode
     *            The current mode.
     * @param user
     *            Current Lutece user
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
     * @param nIdPage
     *            The page ID
     */
    private void invalidatePage( int nIdPage )
    {
        String strIdPage = String.valueOf( nIdPage );
        invalidatePage( strIdPage );
    }

    /**
     * @param cacheKeyService
     *            the _cacheKeyService to set
     */
    public void setPageCacheKeyService( ICacheKeyService cacheKeyService )
    {
        _cksPage = cacheKeyService;
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
     * @param removalService
     *            the removal listener service
     */
    public void setRoleRemovalService( RemovalListenerService removalService )
    {
        removalService.registerListener( new PageRoleRemovalListener(  ) );
        removalService.registerListener( new PortletRoleRemovalListener(  ) );
    }

    /**
     * Remove a page from the cache
     *
     * @param strIdPage
     *            The page ID
     */
    private void invalidatePage( String strIdPage )
    {
        if ( _cachePages.isCacheEnable(  ) )
        {
            String strKey = "[" + Parameters.PAGE_ID + ":" + strIdPage + "]";

            for ( String strKeyTemp : (List<String>) _cachePages.getCache(  ).getKeys(  ) )
            {
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

    // ///////////////////////////////////////////////////////////////////////////
    // Events Listeners management
    /**
     * Add a new page event listener
     *
     * @param listener
     *            An event listener to add
     */
    public static void addPageEventListener( PageEventListener listener )
    {
        _listEventListeners.add( listener );
        AppLogService.info( "New Page Event Listener registered : " + listener.getClass(  ).getName(  ) );
    }

    /**
     * Notify an event to all listeners
     *
     * @param event
     *            A page Event
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
     *
     * @return The resource type Id
     */
    @Override
    public String getResourceTypeId(  )
    {
        return Page.IMAGE_RESOURCE_TYPE_ID;
    }

    /**
     * Gets the image resource for a given resource
     *
     * @param nIdResource
     *            The Resource id
     * @return The image resource
     */
    @Override
    public ImageResource getImageResource( int nIdResource )
    {
        return PageHome.getImageResource( nIdResource );
    }

    /**
     * Create a page
     *
     * @param page
     *            The page to create
     */
    @Override
    public void createPage( Page page )
    {
        PageHome.create( page );

        PageEvent event = new PageEvent( page, PageEvent.PAGE_CREATED );
        notifyListeners( event );
    }

    /**
     * Update a given page
     *
     * @param page
     *            The page to update
     */
    @Override
    public void updatePage( Page page )
    {
        PageHome.update( page );

        PageEvent event = new PageEvent( page, PageEvent.PAGE_CONTENT_MODIFIED );
        notifyListeners( event );
    }

    /**
     * Remove a given page
     *
     * @param nPageId
     *            The page Id
     */
    @Override
    public void removePage( int nPageId )
    {
        Page page = PageHome.findByPrimaryKey( nPageId );
        PageEvent event = new PageEvent( page, PageEvent.PAGE_DELETED );
        PageHome.remove( nPageId );
        notifyListeners( event );
    }

    /**
     * Process a page event
     *
     * @param event
     *            The event to process
     */
    @Override
    public void processPageEvent( PageEvent event )
    {
        Page page = event.getPage(  );
        invalidatePage( page.getId(  ) );

        // Clearing ALL cache is not needed anymore
        // PortalService.resetCache( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processPortletEvent( PortletEvent event )
    {
        invalidateContent( event.getPageId(  ) );
    }

    /**
     * Invalidate Page Content
     *
     * @param nPageId
     *            The Page ID
     */
    @Override
    public void invalidateContent( int nPageId )
    {
        Page page = PageHome.findByPrimaryKey( nPageId );
        PageEvent event = new PageEvent( page, PageEvent.PAGE_CONTENT_MODIFIED );
        notifyListeners( event );
    }

    /**
     * Check that a given user is allowed to access a page for a given
     * permission
     *
     * @param nIdPage
     *            the id of the page to check
     * @param strPermission
     *            the permission needed
     * @param user
     *            The current user
     * @return true if authorized, otherwise false
     */
    @Override
    public boolean isAuthorizedAdminPage( int nIdPage, String strPermission, AdminUser user )
    {
        Page page = PageHome.findByPrimaryKey( nIdPage );

        if ( page.getIdAuthorizationNode(  ) != null )
        {
            String strAuthorizationNode = Integer.toString( page.getIdAuthorizationNode(  ) );

            return ( RBACService.isAuthorized( Page.RESOURCE_TYPE, strAuthorizationNode, strPermission, user ) );
        }

        return true;
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

    /**
     * Gets the params map
     *
     * @param request
     *            The HTTP request
     * @param nMode
     *            The mode
     * @param nIdPage
     *            The page ID
     * @return the map
     */
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

            if ( SecurityService.isAuthenticationEnable(  ) )
            {
                mapModifyParam.put( MARKER_IS_USER_AUTHENTICATED,
                    ( SecurityService.getInstance(  ).getRegisteredUser( request ) != null ) ? VALUE_TRUE : VALUE_FALSE );
            }
        }
        else
        {
            mapModifyParam.put( PARAMETER_SITE_PATH, AppPathService.getAdminPortalUrl(  ) );
            mapModifyParam.put( MARKER_TARGET, TARGET_TOP );
        }

        if ( !mapModifyParam.containsKey( Parameters.PAGE_ID ) )
        {
            mapModifyParam.put( Parameters.PAGE_ID, Integer.toString( PortalService.getRootPageId(  ) ) );
        }

        return mapModifyParam;
    }

    /**
     * Management of the image associated to the page
     *
     * @param strPageId
     *            The page identifier
     * @return The url
     */
    public String getResourceImagePage( String strPageId )
    {
        String strResourceType = getResourceTypeId(  );
        UrlItem url = new UrlItem( Parameters.IMAGE_SERVLET );
        url.addParameter( Parameters.RESOURCE_TYPE, strResourceType );
        url.addParameter( Parameters.RESOURCE_ID, strPageId );

        return url.getUrlWithEntity(  );
    }

    /**
     * Gets the page cache service.
     * @return the page cache service
     */
    public PageCacheService getPageCacheService(  )
    {
        return _cachePages;
    }

    /**
     * Sets the cache page service
     * @param pageCacheService the page cache service
     */
    public void setPageCacheService( PageCacheService pageCacheService )
    {
        _cachePages = pageCacheService;
    }

    /**
     * Gets the portlet cache service
     * @return the porlet cache service
     */
    public PortletCacheService getPortletCacheService(  )
    {
        return _cachePortlets;
    }

    /**
     * Gets the portlet cache service
     * @param portletCacheService the portlet cache service
     */
    public void setPortletCacheService( PortletCacheService portletCacheService )
    {
        _cachePortlets = portletCacheService;
    }

    /**
     * update authorization node of children page
     * @param nIdParentPage id of the parent page
     * @param nIdNewAuthorizationNode the new authorization id
     */
    public static void updateChildrenAuthorizationNode( int nIdParentPage, Integer nIdNewAuthorizationNode )
    {
        List<Integer> listPagesChildren = PageHome.getPagesWhichMustChangeAuthorizationNode( nIdParentPage );

        if ( listPagesChildren != null )
        {
            for ( Integer idPage : listPagesChildren )
            {
                PageHome.updateAuthorizationNode( idPage, nIdNewAuthorizationNode );
                updateChildrenAuthorizationNode( idPage, nIdNewAuthorizationNode );
            }
        }
    }
}
