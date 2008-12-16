/*
 * Copyright (c) 2002-2008, Mairie de Paris
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.page.PageHome;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletType;
import fr.paris.lutece.portal.business.style.ModeHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.content.ContentService;
import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.html.XmlTransformerService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.image.ImageResourceManager;
import fr.paris.lutece.portal.service.image.ImageResourceProvider;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.portal.PortalMenuService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.portlet.PortletResourceIdService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import fr.paris.lutece.util.html.HtmlTemplate;


/**
 * This class delivers pages to web componants. It handles XML tranformation to HTML and provides a cache feature in
 * order to reduce the number of tranformations.
 */
public class PageService extends ContentService implements ImageResourceProvider, PageEventListener
{
    ////////////////////////////////////////////////////////////////////////////
    // Variables

    /** Access denied template */
    public static final String TEMPLATE_PAGE_ACCESS_DENIED = "/skin/site/page_access_denied.html";

    /** Access Controled template */
    public static final String TEMPLATE_PAGE_ACCESS_CONTROLED = "/skin/site/page_access_controled.html";
    private static final String MARK_PORTLET = "portlet";
    private static final String MARK_STATUS_PUBLISHED = "portlet_status_published";
    private static final String MARK_STATUS_UNPUBLISHED = "portlet_status_unpublished";
    private static final String MARK_CUSTOM_ACTIONS = "custom_action_list";
    private static final String MARK_URL_LOGIN = "url_login";

    // Added in v1.3
    private static final String TEMPLATE_ADMIN_BUTTONS = "/admin/admin_buttons.html";
    private static final String PARAMETER_SITE_PATH = "site-path";
    private static final String PARAMETER_USER_SELECTED_LOCALE = "user-selected-language";
    private static final String CONTENT_SERVICE_NAME = "PageService";
    private static final String PARAMETER_MODE = "mode";
    private static final String PROP_NB_COLUMN = "nb.columns";
    private static final String PROPERTY_PAGE_SERVICE_CACHE = "service.pages.cache.enable";
    private static final String PROPERTY_MESSAGE_PAGE_ACCESS_DENIED = "portal.site.message.pageAccessDenied";
    private static final int MODE_ADMIN = 1;
    private static final String LOGGER_PORTLET_XML_CONTENT = "lutece.debug.portlet.xmlContent";
    private static final String IMAGE_RESOURCE_TYPE_ID = "page_thumbnail";

    // Added in v1.2
    private static final String PARAMETER_PLUGIN_NAME = "plugin-name";
    private static PageService _singleton;

    // Specific for plugin-document
    private static final String DOCUMENT_LIST_PORTLET = "DOCUMENT_LIST_PORTLET";
    private static final String DOCUMENT_PORTLET = "DOCUMENT_PORTLET";
    private static final String DOCUMENT_ACTION_URL = "jsp/admin/plugins/document/ManagePublishing.jsp";
    private static final String DOCUMENT_IMAGE_URL = "images/admin/skin/actions/publish.png";
    private static final String DOCUMENT_TITLE = "portal.site.portletPreview.buttonManage";
    private ArrayList<PageEventListener> _listEventListeners = new ArrayList<PageEventListener>(  );

    /**
     * Creates a new PageService object.
     */
    public PageService(  )
    {
        super(  );
        init(  );
        _singleton = this;
    }

    /**
     * Initializes the service
     */
    private void init(  )
    {
        String strCachePages = AppPropertiesService.getProperty( PROPERTY_PAGE_SERVICE_CACHE, "true" );

        if ( strCachePages.equalsIgnoreCase( "true" ) )
        {
            initCache( getName(  ) );
        }

        ImageResourceManager.registerProvider( this );

        Page.init(  );
    }

    /**
     * Gets the service instance
     * @return The unique Instance
     */
    public static synchronized PageService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = new PageService(  );
        }

        return _singleton;
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
     * Analyzes request's parameters to see if the request should be handled by the current Content Service
     *
     * @param request The HTTP request
     * @return true if this ContentService should handle this request
     */
    public boolean isInvoked( HttpServletRequest request )
    {
        String strPageId = request.getParameter( Parameters.PAGE_ID );

        if ( ( strPageId != null ) && ( strPageId.length(  ) > 0 ) )
        {
            return true;
        }

        return false;
    }

    /**
     * Returns the page for a given ID. The page is built using XML data of each portlet or retrieved from the cache if
     * it's enable.
     *
     * @param request The page ID
     * @param nMode The current mode.
     * @return The HTML code of the page as a String.
     * @throws SiteMessageException If a message shouldbe displayed
     */
    public synchronized String getPage( HttpServletRequest request, int nMode )
        throws SiteMessageException
    {
        String strPageId = request.getParameter( Parameters.PAGE_ID );

        return getPage( strPageId, nMode, request );
    }

    /**
     * Returns the page for a given ID. The page is built using XML data of each portlet or retrieved from the cache if
     * it's enable.
     *
     * @param strIdPage The page ID
     * @param nMode The current mode.
     * @param request The HttpRequest
     * @return The HTML code of the page as a String.
     * @throws SiteMessageException occurs when a site message need to be displayed
     */
    public synchronized String getPage( String strIdPage, int nMode, HttpServletRequest request )
        throws SiteMessageException
    {
        try
        {
            String strPage = "";

            // Get request paramaters and store them in a HashMap
            Enumeration enumParam = request.getParameterNames(  );
            HashMap<String, String> htParamRequest = new HashMap<String, String>(  );
            String paramName = "";

            while ( enumParam.hasMoreElements(  ) )
            {
                paramName = (String) enumParam.nextElement(  );
                htParamRequest.put( paramName, request.getParameter( paramName ) );
            }

            LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );
            String strKey = getKey( htParamRequest, nMode, user );

            // The cache is enable !
            if ( isCacheEnable(  ) )
            {
                strPage = (String) getFromCache( strKey );

                if ( strPage == null )
                {
                    Boolean bCanBeCached = Boolean.TRUE;

                    // The key is not in the cache, so we have to build the page
                    strPage = buildPageContent( strIdPage, nMode, request, bCanBeCached );

                    // Add the page to the cache if the page can be cached
                    if ( bCanBeCached.booleanValue(  ) )
                    {
                        putInCache( strKey, strPage );
                    }
                }

                // The page is already in the cache, so just return it
                return strPage;
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
            return PortalService.getDefaultPage( request, nMode );
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

        // Checks the page role (v1.1)
        String strRole = page.getRole(  );

        if ( !strRole.equals( Page.ROLE_NONE ) && SecurityService.isAuthenticationEnable(  ) )
        {
            if ( nMode != MODE_ADMIN )
            {
                LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );

                if ( ( user == null ) && ( !SecurityService.getInstance(  ).isExternalAuthentication(  ) ) )
                {
                    // The user is not registered and identify itself with the Portal authentication
                    String strAccessControledTemplate = SecurityService.getInstance(  ).getAccessControledTemplate(  );
                    HashMap<String, String> model = new HashMap<String, String>(  );
                    String strLoginUrl = SecurityService.getInstance(  ).getLoginPageUrl(  );
                    model.put( MARK_URL_LOGIN, strLoginUrl );

                    HtmlTemplate tAccessControled = AppTemplateService.getTemplate( strAccessControledTemplate,
                            request.getLocale(  ), model );

                    data.setContent( tAccessControled.getHtml(  ) );

                    return PortalService.buildPageContent( data, nMode, request );
                }

                if ( !SecurityService.getInstance(  ).isUserInRole( request, strRole ) )
                {
                    // The user doesn't have the correct role
                    String strAccessDeniedTemplate = SecurityService.getInstance(  ).getAccessDeniedTemplate(  );
                    HtmlTemplate tAccessDenied = AppTemplateService.getTemplate( strAccessDeniedTemplate,
                            request.getLocale(  ) );
                    data.setContent( tAccessDenied.getHtml(  ) );

                    return PortalService.buildPageContent( data, nMode, request );
                }
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

        /* if ( nIdPage == PortalService.getRootPageId(  ) )
         {
             data.setTreeMenu( "" );
         }
         else
         {*/
        data.setTreeMenu( PortalMenuService.getInstance(  ).buildTreeMenuContent( nIdPage, nMode, request ) );

        //}
        if ( nIdPage == PortalService.getRootPageId(  ) )
        {
            // This page is the home page.
            data.setHomePage( true );
        }

        return PortalService.buildPageContent( data, nMode, request );
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
        Map<String, String> mapModifyParam = new HashMap<String, String>(  );
        String paramName = "";

        // Get request paramaters and store them in a HashMap
        if ( request != null )
        {
            Enumeration enumParam = request.getParameterNames(  );

            while ( enumParam.hasMoreElements(  ) )
            {
                paramName = (String) enumParam.nextElement(  );
                mapModifyParam.put( paramName, request.getParameter( paramName ) );
            }

            // Add selected locale
            mapModifyParam.put( PARAMETER_USER_SELECTED_LOCALE,
                LocaleService.getUserSelectedLocale( request ).getLanguage(  ) );
        }

        //Added in v1.3
        // Add a path param for choose url to use in admin or normal mode
        if ( nMode != MODE_ADMIN )
        {
            mapModifyParam.put( PARAMETER_SITE_PATH, AppPathService.getPortalUrl(  ) );
        }
        else
        {
            mapModifyParam.put( PARAMETER_SITE_PATH, AppPathService.getAdminPortalUrl(  ) );
        }

        String strColumn = AppPropertiesService.getProperty( PROP_NB_COLUMN );
        int nColumn = Integer.parseInt( strColumn );
        String[] arrayContent = new String[nColumn];

        for ( int i = 0; i < nColumn; i++ )
        {
            arrayContent[i] = "";
        }

        Page page = PageHome.findByPrimaryKey( nIdPage );

        for ( Portlet portlet : page.getPortlets(  ) )
        {
            Map<String, String> mapXslParams = portlet.getXslParams(  );

            if ( mapModifyParam != null )
            {
                if ( mapXslParams != null )
                {
                    for ( String strKey : mapXslParams.keySet(  ) )
                    {
                        mapModifyParam.put( strKey, mapXslParams.get( strKey ) );
                    }
                }
            }
            else
            {
                mapModifyParam = mapXslParams;
            }

            Properties outputProperties = ModeHome.getOuputXslProperties( nMode );

            if ( request != null )
            {
                String strPluginName = portlet.getPluginName(  );
                request.setAttribute( PARAMETER_PLUGIN_NAME, strPluginName );
            }

            String strPortletXmlContent = portlet.getXml( request );

            if ( AppLogService.isDebugEnabled( LOGGER_PORTLET_XML_CONTENT ) )
            {
                AppLogService.debug( LOGGER_PORTLET_XML_CONTENT, strPortletXmlContent );
            }

            String strPortletContent = XmlTransformerService.transformBySource( strPortletXmlContent,
                    portlet.getXslSource( nMode ), mapModifyParam, outputProperties );

            // Added in v1.3
            // Add the admin buttons for portlet management on admin mode
            if ( nMode == MODE_ADMIN )
            {
                AdminUser user = AdminUserService.getAdminUser( request );

                if ( RBACService.isAuthorized( PortletType.RESOURCE_TYPE, portlet.getPortletTypeId(  ),
                            PortletResourceIdService.PERMISSION_MANAGE, user ) )
                {
                    Locale locale = user.getLocale(  );
                    Collection<CustomAction> listCustomActions = new ArrayList<CustomAction>(  );

                    //TODO : listCustomActions should be provided by PortletType
                    //FIXME : Delete plugin-document specifics 
                    if ( portlet.getPortletTypeId(  ).equals( DOCUMENT_LIST_PORTLET ) ||
                            portlet.getPortletTypeId(  ).equals( DOCUMENT_PORTLET ) )
                    {
                        CustomAction customAction = new CustomAction(  );
                        customAction.setActionUrl( DOCUMENT_ACTION_URL );
                        customAction.setImageUrl( DOCUMENT_IMAGE_URL );
                        customAction.setTitle( DOCUMENT_TITLE );
                        listCustomActions.add( customAction );
                    }

                    Map<String, Object> model = new HashMap<String, Object>(  );
                    model.put( MARK_PORTLET, portlet );
                    model.put( MARK_STATUS_PUBLISHED, portlet.STATUS_PUBLISHED );
                    model.put( MARK_STATUS_UNPUBLISHED, portlet.STATUS_UNPUBLISHED );
                    model.put( MARK_CUSTOM_ACTIONS, listCustomActions );

                    HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADMIN_BUTTONS, locale, model );
                    strPortletContent = strPortletContent + template.getHtml(  );
                }
            }

            if ( ( nMode != MODE_ADMIN ) && ( portlet.getStatus(  ) == Portlet.STATUS_UNPUBLISHED ) )
            {
                strPortletContent = "";
            }

            int nCol = portlet.getColumn(  ) - 1;

            if ( nCol < nColumn )
            {
                arrayContent[nCol] += strPortletContent;
            }
        }

        HashMap<String, String> rootModel = new HashMap<String, String>(  );

        for ( int j = 0; j < nColumn; j++ )
        {
            rootModel.put( "page_content_col" + ( j + 1 ), arrayContent[j] );
        }

        HtmlTemplate t = AppTemplateService.getTemplate( page.getTemplate(  ),
                ( request == null ) ? null : request.getLocale(  ), rootModel );

        return t.getHtml(  );
    }

    /**
     * Build the Cache HashMap key for pages
     *
     * @return The HashMap key for articles pages as a String.
     * @param mapParams The Map params
     * @param nMode The current mode.
     * @param user The user
     */
    private String getKey( Map<String, String> mapParams, int nMode, LuteceUser user )
    {
        String strKey = "";
        String strUserName = "";

        for ( String strHtKey : mapParams.keySet(  ) )
        {
            strKey += ( strHtKey + "'" + mapParams.get( strHtKey ) + "'" );
        }

        if ( user != null )
        {
            strUserName = user.getName(  );
        }

        return strKey + strUserName + PARAMETER_MODE + nMode;
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
     * Remove a page from the cache
     *
     * @param strIdPage The page ID
     */
    private void invalidatePage( String strIdPage )
    {
        String strKey = Parameters.PAGE_ID + "'" + strIdPage + "'";

        if ( isCacheEnable(  ) )
        {
            for ( String strKeyTemp : (List<String>) getCache(  ).getKeys(  ) )
            {
                if ( strKeyTemp.indexOf( strKey ) != -1 )
                {
                    getCache(  ).remove( strKeyTemp );
                    AppLogService.debug( "Page (cache key : " + strKeyTemp + ") removed from the cache." );
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
        PortalService.resetCache(  );
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
    * Check that a given user is allowed to access a page  for a given permission and his workgroups
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

    /**
    * CustomAction define a customized action for portlet types
    *
    */
    public class CustomAction
    {
        private String _strActionUrl;
        private String _strImageUrl;
        private String _strTitle;

        /**
         * @return the _actionUrl
         */
        public String getActionUrl(  )
        {
            return _strActionUrl;
        }

        /**
         * @param strActionUrl the _actionUrl to set
         */
        public void setActionUrl( String strActionUrl )
        {
            _strActionUrl = strActionUrl;
        }

        /**
         * @return the _imageUrl
         */
        public String getImageUrl(  )
        {
            return _strImageUrl;
        }

        /**
         * @param strImageUrl the _imageUrl to set
         */
        public void setImageUrl( String strImageUrl )
        {
            _strImageUrl = strImageUrl;
        }

        /**
         * @return the _strTitle
         */
        public String getTitle(  )
        {
            return _strTitle;
        }

        /**
         * @param strTitle the _strTitle to set
         */
        public void setTitle( String strTitle )
        {
            _strTitle = strTitle;
        }
    }
}
