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
package fr.paris.lutece.portal.service.portal;

import fr.paris.lutece.portal.business.XmlContent;
import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.page.PageHome;
import fr.paris.lutece.portal.business.portalcomponent.PortalComponentHome;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.business.style.ModeHome;
import fr.paris.lutece.portal.business.stylesheet.StyleSheet;
import fr.paris.lutece.portal.service.cache.CacheService;
import fr.paris.lutece.portal.service.cache.CacheableService;
import fr.paris.lutece.portal.service.content.ContentService;
import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.html.XmlTransformerService;
import fr.paris.lutece.portal.service.includes.PageInclude;
import fr.paris.lutece.portal.service.includes.PageIncludeService;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.page.IPageService;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Markers;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.xml.XmlUtil;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides methods to build the pages of the portal and manage the
 * cache
 */
public final class PortalService
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final int PORTAL_COMPONENT_PAGE_PATH_ID = 5;

    // Properties
    private static final String PROPERTY_HOME_PAGE_HEADER = "home.page.header.mode";
    private static final String PROPERTY_INTERNAL_PAGE_HEADER = "internal.page.header.mode";
    private static final String PROPERTY_PAGE_TOOLS_MENU = "page.tools.menu.mode";
    private static final String PROPERTY_PORTAL_FOOTER = "page.portal.footer.mode";
    private static final String PROPERTY_PATH_ON_ROOT = "lutece.root.path";
    private static final String PROPERTY_ENCODING = "lutece.encoding";
    private static final String PROPERTY_ENCODING_DEFAULT = "UTF-8";

    // Datastore keys
    private static final String KEY_SITE_NAME = "portal.site.site_property.name";
    private static final String KEY_WEBMASTER_EMAIL = "portal.site.site_property.email";

    // Templates
    private static final String TEMPLATE_PAGE_FRAMESET = "skin/site/page_frameset.html";
    private static final String TEMPLATE_HOME_PAGE_HEADER = "skin/site/page_header_home.html";
    private static final String TEMPLATE_INTERNAL_PAGE_HEADER = "skin/site/page_header_internal.html";
    private static final String TEMPLATE_PAGE_TOOLS_MENU = "skin/site/page_menu_tools.html";
    private static final String TEMPLATE_PAGE_PATH = "skin/site/page_path.html";
    private static final String TEMPLATE_PORTAL_FOOTER = "skin/site/portal_footer.html";

    // Markers
    private static final String MARKER_TARGET = "target";
    private static final String MARKER_PAGE_DATA = "data";
    private static final String PLUGIN_EXTEND_NAME = "extend";
    private static final String PLUGIN_CONTACT_NAME = "contact";
    private static final String MARK_IS_EXTEND_INSTALLED = "isExtendInstalled";
    private static final String MARK_IS_CONTACT_INSTALLED = "isContactInstalled";
    private static final String MARK_LUTECE_USER = "lutece_user";
    private static final String TARGET_TOP = "target='_top'";
    private static final String BOOKMARK_BASE_URL = "@base_url@";

    // Added in v1.3
    private static final int MODE_NORMAL = 0;
    private static final int MODE_ADMIN = 1;
    private static final String PARAMETER_SITE_PATH = "site-path";

    // Content Service registry
    private static Map<String, ContentService> _mapContentServicesRegistry = new HashMap<String, ContentService>(  );
    private static IPageService _pageService = (IPageService) SpringContextService.getBean( "pageService" );

    /**
     * Private Constructor
     */
    private PortalService(  )
    {
    }

    /**
     * Reset the cache
     * @deprecated use CacheService.resetCaches()
     */
    @Deprecated
    public static void resetCache(  )
    {
        CacheService.resetCaches(  );
    }

    /**
     * Analyzes request's parameters to find the ContentService that should
     * handle the request
     *
     * @param request The HTTP request
     * @return ContentService that should handle the request
     */
    public static ContentService getInvokedContentService( HttpServletRequest request )
    {
        for ( ContentService cs : getContentServicesList(  ) )
        {
            if ( cs.isInvoked( request ) )
            {
                return cs;
            }
        }

        return null;
    }

    /**
     * Registers a new ContentService
     * @param strName The name
     * @param cs The ContentService
     */
    public static void registerContentService( String strName, ContentService cs )
    {
        _mapContentServicesRegistry.put( strName, cs );
    }

    /**
     * Returns all registered Content services
     *
     * @return A collection containing all registered Content services
     */
    public static Collection<ContentService> getContentServicesList(  )
    {
        return _mapContentServicesRegistry.values(  );
    }

    /**
     * Registers a new CacheableService
     * @deprecated Use CacheService.registerCacheableService( String strName,
     *             CacheableService cs ) instead
     * @param strName The name
     * @param cs The CacheableService
     */
    @Deprecated
    public static void registerCacheableService( String strName, CacheableService cs )
    {
        CacheService.registerCacheableService( strName, cs );
    }

    /**
     * Returns all registered Cacheable services
     * @deprecated Use CacheService.getCacheableServicesList() instead
     *
     * @return A collection containing all registered Cacheable services
     */
    @Deprecated
    public static Collection<CacheableService> getCacheableServicesList(  )
    {
        return CacheService.getCacheableServicesList(  );
    }

    /**
     * Returns the identifier of the root page of the portal read in the
     * lutece.properties file
     *
     * @return The identifier of the root page
     */
    public static int getRootPageId(  )
    {
        return AppPropertiesService.getPropertyInt( "lutece.page.root", 1 );
    }

    /**
     * Return the default page of the portal (the home page)
     * @param request The request
     * @param nMode the mode id
     * @return default page as a String
     * @throws SiteMessageException occurs when a site message need to be
     *             displayed
     */
    public static String getDefaultPage( HttpServletRequest request, int nMode )
        throws SiteMessageException
    {
        return _pageService.getPage( String.valueOf( getRootPageId(  ) ), nMode, request );
    }

    /**
     * Return the xml content of the pages contained in the list specified in
     * parameter
     *
     * @param listPages The pages list
     * @return the xml code for the content page
     */
    public static String getXmlPagesList( Collection<Page> listPages )
    {
        StringBuffer strXml = new StringBuffer(  );
        strXml.append( XmlUtil.getXmlHeader(  ) );
        XmlUtil.beginElement( strXml, XmlContent.TAG_CHILD_PAGES_LIST );

        for ( Page page : listPages )
        {
            XmlUtil.beginElement( strXml, XmlContent.TAG_PAGE );
            XmlUtil.addElement( strXml, XmlContent.TAG_PAGE_ID, page.getId(  ) );
            XmlUtil.addElementHtml( strXml, XmlContent.TAG_PAGE_NAME, page.getName(  ) );
            XmlUtil.endElement( strXml, XmlContent.TAG_PAGE );
        }

        XmlUtil.endElement( strXml, XmlContent.TAG_CHILD_PAGES_LIST );

        return strXml.toString(  );
    }

    ////////////////////////////////////////////////////////////////////////////
    // pages builder

    /**
     * Returns the html code which represents the page content
     * @param data The structure which contains the informations about the page
     * @param nMode The mode in which displaying the page : normal or
     *            administration
     * @param request The request
     * @return The html code of a page
     */
    public static String buildPageContent( PageData data, int nMode, HttpServletRequest request )
    {
        return buildPageContent( getRootPageId(  ), data, nMode, request );
    }

    /**
     * Returns the html code which represents the page content
     * @param nCurrentPageId the current page id
     * @param data The structure which contains the informations about the page
     * @param nMode The mode in which displaying the page : normal or
     *            administration
     * @param request The request
     * @return The html code of a page
     */
    public static String buildPageContent( int nCurrentPageId, PageData data, int nMode, HttpServletRequest request )
    {
        Locale locale = null;
        HashMap<String, Object> model = new HashMap<String, Object>(  );
        LuteceUser user = null;
        String strWebmasterEmail = DatastoreService.getDataValue( KEY_WEBMASTER_EMAIL, "" );
        model.put( Markers.WEBMASTER_EMAIL, strWebmasterEmail );

        if ( request != null )
        {
            locale = LocaleService.getUserSelectedLocale( request );
            user = SecurityService.getInstance(  ).getRegisteredUser( request );
            if ( nMode != MODE_ADMIN) 
            {
            	  model.put( MARK_LUTECE_USER, user );
            }
        }

        List<PageInclude> listIncludes = PageIncludeService.getIncludes(  );

        for ( PageInclude pic : listIncludes )
        {
            pic.fillTemplate( model, data, nMode, request );
        }

        String strHeader = ( data.isHomePage(  ) )
            ? AppPropertiesService.getProperty( PROPERTY_HOME_PAGE_HEADER + nMode, TEMPLATE_HOME_PAGE_HEADER )
            : AppPropertiesService.getProperty( PROPERTY_INTERNAL_PAGE_HEADER + nMode, TEMPLATE_INTERNAL_PAGE_HEADER );
        HtmlTemplate tHeader = AppTemplateService.getTemplate( strHeader, locale, model );

        String strFooter = AppPropertiesService.getProperty( PROPERTY_PORTAL_FOOTER + nMode, TEMPLATE_PORTAL_FOOTER );
        String strToolsMenu = AppPropertiesService.getProperty( PROPERTY_PAGE_TOOLS_MENU + nMode,
                TEMPLATE_PAGE_TOOLS_MENU );
        model.put( MARK_IS_CONTACT_INSTALLED, isContactActivated(  ) );

        HtmlTemplate tFooter = AppTemplateService.getTemplate( strFooter, locale, model );

        HtmlTemplate tToolsMenu = AppTemplateService.getTemplate( strToolsMenu, locale, model );
        model.put( Markers.PAGE_HEADER, tHeader.getHtml(  ) );
        model.put( MARKER_PAGE_DATA, data );
        model.put( Markers.PAGE_NAME, ( data.getName(  ) == null ) ? "" : data.getName(  ) );
        model.put( Markers.PAGE_CONTENT, ( data.getContent(  ) == null ) ? "" : data.getContent(  ) );
        model.put( Markers.PAGE_PATH, ( data.getPagePath(  ) == null ) ? "" : data.getPagePath(  ) );
        model.put( Markers.PAGE_TOOLS_MENU, tToolsMenu.getHtml(  ) );
        model.put( Markers.PAGE_ID, nCurrentPageId );
        
        model.put( Markers.PAGE_FOOTER, tFooter.getHtml(  ) );

        String strBaseUrl = ( request != null ) ? AppPathService.getBaseUrl( request ) : ""; // request could be null (method called by daemons or batch)

        // for link service
        model.put( Markers.WEBAPP_PATH_FOR_LINKSERVICE, strBaseUrl );
        model.put( Markers.BASE_URL, strBaseUrl );

        String strEncoding = AppPropertiesService.getProperty( PROPERTY_ENCODING, PROPERTY_ENCODING_DEFAULT );

        if ( ( strEncoding == null ) || strEncoding.equals( "" ) )
        {
            strEncoding = PROPERTY_ENCODING_DEFAULT;
        }

        model.put( Markers.ENCODING, strEncoding );

        model.put( MARK_IS_EXTEND_INSTALLED, isExtendActivated(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PAGE_FRAMESET, locale, model );

        template.substitute( BOOKMARK_BASE_URL, ( request != null ) ? AppPathService.getBaseUrl( request ) : "" ); // request could be null (method called by daemons or batch)

        return template.getHtml(  );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Management of the pages path

    /**
     * Returns the formated path of the site page whose identifier is specified
     * in parameter
     *
     * @param nPageId The identifier of the page
     * @param nMode The mode to use for the formatting
     * @param request The HTTP request
     * @return the formated path
     */
    public static String getPagePathContent( int nPageId, int nMode, HttpServletRequest request )
    {
        String strPathOnRoot = AppPropertiesService.getProperty( PROPERTY_PATH_ON_ROOT );

        // If the current page is the home page or the string strPathOnRoot equals false, not display the path
        if ( ( nPageId == getRootPageId(  ) ) &&
                ( ( strPathOnRoot == null ) || strPathOnRoot.equalsIgnoreCase( "false" ) ) )
        {
            return "";
        }

        // Selection of the XSL stylesheet
        // Added in v1.3
        // Use the same stylesheet for normal or admin mode
        StyleSheet xslSource;

        switch ( nMode )
        {
            case MODE_NORMAL:
            case MODE_ADMIN:
                xslSource = PortalComponentHome.getXsl( PORTAL_COMPONENT_PAGE_PATH_ID, MODE_NORMAL );

                break;

            default:
                xslSource = PortalComponentHome.getXsl( PORTAL_COMPONENT_PAGE_PATH_ID, nMode );

                break;
        }

        String strXml = getXmlPagesList( getPagePath( nPageId ) );

        Properties outputProperties = ModeHome.getOuputXslProperties( nMode );

        // Added in v1.3
        // Add a path param for choose url to use in admin or normal mode
        Map<String, String> mapParamRequest = new HashMap<String, String>(  );
        setXslPortalPath( mapParamRequest, nMode );

        XmlTransformerService xmlTransformerService = new XmlTransformerService(  );
        String strPath = xmlTransformerService.transformBySourceWithXslCache( strXml, xslSource, mapParamRequest,
                outputProperties );

        return formatPath( strPath, nMode, request );
    }

    /**
     * Returns the formated path of a xpage (ex : result of a seek)
     *
     * @param strXPageName The xpage name
     * @param nMode The mode to use for the formatting
     * @param request The HTTP request
     * @return the formated path
     */
    public static String getXPagePathContent( String strXPageName, int nMode, HttpServletRequest request )
    {
        // Added in v1.3
        StyleSheet xslSource;

        // Selection of the XSL stylesheet
        switch ( nMode )
        {
            case MODE_NORMAL:
            case MODE_ADMIN:
                xslSource = PortalComponentHome.getXsl( PORTAL_COMPONENT_PAGE_PATH_ID, MODE_NORMAL );

                break;

            default:
                xslSource = PortalComponentHome.getXsl( PORTAL_COMPONENT_PAGE_PATH_ID, nMode );

                break;
        }

        String strXml = StringUtils.EMPTY;
        String strPageId = request.getParameter( Parameters.PAGE_ID );

        if ( StringUtils.isNotBlank( strPageId ) && StringUtils.isNumeric( strPageId ) )
        {
            int nPageId = Integer.parseInt( strPageId );
            strXml = getXmlPagesList( getXPagePath( strXPageName, nPageId ) );
        }
        else
        {
            String strPortletId = request.getParameter( Parameters.PORTLET_ID );

            if ( StringUtils.isNotBlank( strPortletId ) && StringUtils.isNumeric( strPortletId ) )
            {
                int nPortletId = Integer.parseInt( strPortletId );
                Portlet portlet = PortletHome.findByPrimaryKey( nPortletId );

                if ( portlet != null )
                {
                    int nPageId = portlet.getPageId(  );
                    strXml = getXmlPagesList( getXPagePath( strXPageName, nPageId ) );
                }
            }
        }

        if ( StringUtils.isBlank( strXml ) )
        {
            strXml = getXmlPagesList( getXPagePath( strXPageName ) );
        }

        Properties outputProperties = ModeHome.getOuputXslProperties( nMode );

        //Added in v1.3
        // Add a path param for choose url to use in admin or normal mode
        Map<String, String> mapXslParams = new HashMap<String, String>(  );
        setXslPortalPath( mapXslParams, nMode );

        XmlTransformerService xmlTransformerService = new XmlTransformerService(  );
        String strPath = xmlTransformerService.transformBySourceWithXslCache( strXml, xslSource, mapXslParams,
                outputProperties );

        return formatPath( strPath, nMode, request );
    }

    /**
     * Builds a collection of pages corresponding to the path of the page
     * specified in parameter
     *
     * @param nPageId The identifier of the page
     * @return A collection of pages from the home page to the specified page
     */
    public static Collection<Page> getPagePath( int nPageId )
    {
        ArrayList<Page> list = new ArrayList<Page>(  );
        Page page = PageHome.getPage( nPageId );
        int nParentPageId = page.getParentPageId(  );
        list.add( page );

        while ( nParentPageId != 0 )
        {
            Page parentPage = PageHome.getPage( nParentPageId );

            // Insert the page in the begin of the list
            list.add( 0, parentPage );
            nParentPageId = parentPage.getParentPageId(  );
        }

        return list;
    }

    /**
     * Builds a collection of pages corresponding to the path of a xpage
     *
     * @param strXPageName The xpage name
     * @return A collection of pages made by the home page and the xpage
     */
    private static Collection<Page> getXPagePath( String strXPageName )
    {
        ArrayList<Page> list = new ArrayList<Page>(  );
        Page homePage = PageHome.getPage( getRootPageId(  ) );
        list.add( homePage );

        Page xPage = new Page(  );
        xPage.setName( strXPageName );
        list.add( xPage );

        return list;
    }

    /**
     * Builds a collection of pages corresponding to the path of a xpage
     *
     * @param strXPageName The xpage name
     * @param nPageId The Page's ID
     * @return A collection of pages made by the home page and the xpage
     */
    private static Collection<Page> getXPagePath( String strXPageName, int nPageId )
    {
        List<Page> list = new ArrayList<Page>(  );
        Page page = PageHome.getPage( nPageId );

        if ( page != null )
        {
            int nParentPageId = page.getParentPageId(  );

            while ( ( nParentPageId > 0 ) && ( nParentPageId != getRootPageId(  ) ) )
            {
                Page parentPage = PageHome.getPage( nParentPageId );

                if ( parentPage != null )
                {
                    // Insert the page at the beginning of the list
                    list.add( 0, parentPage );
                    nParentPageId = parentPage.getParentPageId(  );
                }
            }

            if ( nPageId != getRootPageId(  ) )
            {
                list.add( page );
            }
        }

        // Insert the home page at the beginning of the list
        Page homePage = PageHome.getPage( getRootPageId(  ) );
        list.add( 0, homePage );

        // Insert the XPage at the end of the list
        Page xPage = new Page(  );
        xPage.setName( strXPageName );
        xPage.setId( nPageId );
        list.add( xPage );

        return list;
    }

    ////////////////////////////////////////////////////////////////////////////

    /**
     * Formats the path specified in parameter and returns it
     *
     * @param strPath The path to format
     * @param nMode The mode to use for the formatting
     * @param request The HTTP request
     * @return the html code to display the path
     */
    public static String formatPath( String strPath, int nMode, HttpServletRequest request )
    {
        HashMap<String, Object> model = new HashMap<String, Object>(  );
        model.put( Markers.PAGE_PATH, strPath );

        List<PageInclude> listIncludes = PageIncludeService.getIncludes(  );
        PageData data = new PageData(  );

        for ( PageInclude pic : listIncludes )
        {
            pic.fillTemplate( model, data, nMode, request );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PAGE_PATH,
                ( request == null ) ? null : request.getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * Return the xml content of the pages specified by the xml code.
     * This is called when using the Extended Xml path Label.
     *
     * @param strXmlExtend The xml code to append to the path
     * @return the xml code for the content page
     */
    private static String getXmlPagesListExtended( String strXmlExtend )
    {
        StringBuffer strXml = new StringBuffer(  );
        strXml.append( XmlUtil.getXmlHeader(  ) );
        XmlUtil.beginElement( strXml, XmlContent.TAG_CHILD_PAGES_LIST );

        Page homePage = PageHome.getPage( getRootPageId(  ) );

        XmlUtil.beginElement( strXml, XmlContent.TAG_PAGE );
        XmlUtil.addElement( strXml, XmlContent.TAG_PAGE_ID, homePage.getId(  ) );
        XmlUtil.addElementHtml( strXml, XmlContent.TAG_PAGE_NAME, homePage.getName(  ) );
        XmlUtil.endElement( strXml, XmlContent.TAG_PAGE );

        strXml.append( strXmlExtend );

        XmlUtil.endElement( strXml, XmlContent.TAG_CHILD_PAGES_LIST );

        return strXml.toString(  );
    }

    /**
     * Returns the formated extended path of an xpage.
     * This method is used when giving the list of elements in the path as a Xml
     * code.
     * This is called when using the Extended Xml path Label.
     *
     * @param strXPageName The xpage name
     * @param nMode The mode to use for the formatting
     * @param strTitlesUrls list of links (url and titles)
     * @param request The HTTP request
     * @return the formatted path
     */
    public static String getXPagePathContent( String strXPageName, int nMode, String strTitlesUrls,
        HttpServletRequest request )
    {
        // Selection of the XSL stylesheet
        StyleSheet xslSource;

        // Selection of the XSL stylesheet
        switch ( nMode )
        {
            case MODE_NORMAL:
            case MODE_ADMIN:
                xslSource = PortalComponentHome.getXsl( PORTAL_COMPONENT_PAGE_PATH_ID, MODE_NORMAL );

                break;

            default:
                xslSource = PortalComponentHome.getXsl( PORTAL_COMPONENT_PAGE_PATH_ID, nMode );

                break;
        }

        String strXml = getXmlPagesListExtended( strTitlesUrls );

        //Added in v1.3
        // Add a path param for choose url to use in admin or normal mode
        Map<String, String> mapXslParams = new HashMap<String, String>(  );
        setXslPortalPath( mapXslParams, nMode );

        XmlTransformerService xmlTransformerService = new XmlTransformerService(  );
        String strPath = xmlTransformerService.transformBySourceWithXslCache( strXml, xslSource, mapXslParams );

        return formatPath( strPath, nMode, request );
    }

    /**
     * Sets XSL portal path
     * @param mapParameters Parameters as a map
     * @param nMode The mode
     */
    public static void setXslPortalPath( Map<String, String> mapParameters, int nMode )
    {
        if ( nMode != MODE_ADMIN )
        {
            mapParameters.put( PARAMETER_SITE_PATH, AppPathService.getPortalUrl(  ) );
        }
        else
        {
            mapParameters.put( PARAMETER_SITE_PATH, AppPathService.getAdminPortalUrl(  ) );
            mapParameters.put( MARKER_TARGET, TARGET_TOP );
        }
    }

    /**
     * Returns the site name
     * @return The site name
     */
    public static String getSiteName(  )
    {
        return DatastoreService.getDataValue( KEY_SITE_NAME, StringUtils.EMPTY );
    }

    /**
     * Check if the extend plugin is activated
     * @return True if the plugin is activated, false otherwise
     */
    public static boolean isExtendActivated(  )
    {
        return PluginService.isPluginEnable( PLUGIN_EXTEND_NAME );
    }

    /**
     * Check if the cotnact plugin is activated
     * @return True if the plugin is activated, false otherwise
     */
    public static boolean isContactActivated(  )
    {
        return PluginService.isPluginEnable( PLUGIN_CONTACT_NAME );
    }
}
