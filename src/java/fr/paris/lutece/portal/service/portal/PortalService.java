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
package fr.paris.lutece.portal.service.portal;

import fr.paris.lutece.portal.business.XmlContent;
import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.page.PageHome;
import fr.paris.lutece.portal.business.portalcomponent.PortalComponentHome;
import fr.paris.lutece.portal.business.style.ModeHome;
import fr.paris.lutece.portal.service.cache.CacheableService;
import fr.paris.lutece.portal.service.content.ContentService;
import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.html.XmlTransformerService;
import fr.paris.lutece.portal.service.includes.PageInclude;
import fr.paris.lutece.portal.service.includes.PageIncludeService;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.page.PageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Markers;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides methods to build the pages of the portal and manage the cache
 */
public final class PortalService extends XmlTransformerService
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final int PORTAL_COMPONENT_PAGE_PATH_ID = 5;
    private static final String PROPERTY_HOME_PAGE_HEADER = "home.page.header.mode";
    private static final String PROPERTY_INTERNAL_PAGE_HEADER = "internal.page.header.mode";
    private static final String PROPERTY_PAGE_TOOLS_MENU = "page.tools.menu.mode";
    private static final String PROPERTY_PORTAL_FOOTER = "page.portal.footer.mode";
    private static final String PROPERTY_WEBMASTER_EMAIL = "email.webmaster";
    private static final String PROPERTY_PATH_ON_ROOT = "lutece.root.path";
    private static final String TEMPLATE_PAGE_FRAMESET = "skin/site/page_frameset.html";
    private static final String TEMPLATE_HOME_PAGE_HEADER = "skin/site/page_header_home.html";
    private static final String TEMPLATE_INTERNAL_PAGE_HEADER = "skin/site/page_header_internal.html";
    private static final String TEMPLATE_PAGE_TOOLS_MENU = "skin/site/page_menu_tools.html";
    private static final String TEMPLATE_PAGE_PATH = "skin/site/page_path.html";
    private static final String TEMPLATE_PORTAL_FOOTER = "skin/site/portal_footer.html";
    private static final String BOOKMARK_BASE_URL = "@base_url@";

    // Added in v1.3
    private static final int MODE_NORMAL = 0;
    private static final int MODE_ADMIN = 1;
    private static final String PARAMETER_SITE_PATH = "site-path";

    // Content Service registry
    private static Map<String, ContentService> _mapContentServicesRegistry = new HashMap<String, ContentService>(  );

    // Cacheable Services registry
    private static Map<String, CacheableService> _mapCacheableServicesRegistry = new HashMap<String, CacheableService>(  );

    /**
     * Reset the cache
     */
    public static void resetCache(  )
    {
        // Reset cache
        for ( CacheableService cs : getCacheableServicesList(  ) )
        {
            cs.resetCache(  );
        }
    }

    /**
     * Analyzes request's parameters to find the ContentService that should handle the request
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

        // Register also the service as a Cacheable service
        registerCacheableService( strName, cs );
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
     * @param strName The name
     * @param cs The CacheableService
     */
    public static void registerCacheableService( String strName, CacheableService cs )
    {
        _mapCacheableServicesRegistry.put( strName, cs );
    }

    /**
     * Returns all registered Cacheable services
     *
     * @return A collection containing all registered Cacheable services
     */
    public static Collection<CacheableService> getCacheableServicesList(  )
    {
        return _mapCacheableServicesRegistry.values(  );
    }

    /**
     * Returns the identifier of the root page of the portal read in the lutece.properties file
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
     * @throws SiteMessageException occurs when a site message need to be displayed
     */
    public static String getDefaultPage( HttpServletRequest request, int nMode )
        throws SiteMessageException
    {
        return PageService.getInstance(  ).getPage( String.valueOf( getRootPageId(  ) ), nMode, request );
    }

    /**
     * Return the xml content of the pages contained in the list specified in parameter
     *
     * @param list The list which contains the pages
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
     * @since v1.2.4
     * @param data The structure which contains the informations about the page
     * @param nMode The mode in which displaying the page : normal or administration
     * @param request The request
     * @return The html code of a page
     */
    public static String buildPageContent( PageData data, int nMode, HttpServletRequest request )
    {
        Locale locale = null;
        HashMap<String, String> model = new HashMap<String, String>(  );
        String strWebmasterEmail = AppPropertiesService.getProperty( PROPERTY_WEBMASTER_EMAIL );
        model.put( Markers.WEBMASTER_EMAIL, strWebmasterEmail );

        if ( request != null )
        {
            locale = request.getLocale(  );
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

        HtmlTemplate tFooter = AppTemplateService.getTemplate( strFooter, locale, model );

        HtmlTemplate tToolsMenu = AppTemplateService.getTemplate( strToolsMenu, locale, model );
        model.put( Markers.PAGE_HEADER, tHeader.getHtml(  ) );
        model.put( Markers.PAGE_NAME, ( data.getName(  ) == null ) ? "" : data.getName(  ) );
        model.put( Markers.PAGE_MAIN_MENU,
            PortalMenuService.getInstance(  ).getMenuContent( nMode, PortalMenuService.MENU_MAIN, request ) );
        model.put( Markers.PAGE_INIT_MENU,
            PortalMenuService.getInstance(  ).getMenuContent( nMode, PortalMenuService.MENU_INIT, request ) );        
        model.put( Markers.PAGE_TREE_MENU, ( data.getTreeMenu(  ) == null ) ? "" : data.getTreeMenu(  ) );
        model.put( Markers.PAGE_CONTENT, ( data.getContent(  ) == null ) ? "" : data.getContent(  ) );
        model.put( Markers.PAGE_PATH, ( data.getPagePath(  ) == null ) ? "" : data.getPagePath(  ) );
        model.put( Markers.PAGE_TOOLS_MENU, tToolsMenu.getHtml(  ) );
        
        model.put( Markers.PAGE_FOOTER, tFooter.getHtml(  ) );

        String strBaseUrl = ( request != null ) ? AppPathService.getBaseUrl( request ) : ""; // request could be null (method called by daemons or batch)

        // for link service
        model.put( Markers.WEBAPP_PATH_FOR_LINKSERVICE, strBaseUrl );
        model.put( Markers.BASE_URL, strBaseUrl );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PAGE_FRAMESET, locale, model );

        template.substitute( BOOKMARK_BASE_URL, ( request != null ) ? AppPathService.getBaseUrl( request ) : "" ); // request could be null (method called by daemons or batch)

        return template.getHtml(  );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Management of the pages path

    /**
     * Returns the formated path of the site page whose identifier is specified in parameter
     *
     * @param nPageId The identifier of the page
     * @param nMode The mode to use for the formatting
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
        byte[] baXslSource;

        switch ( nMode )
        {
            case MODE_NORMAL:
            case MODE_ADMIN:
                baXslSource = PortalComponentHome.getXsl( PORTAL_COMPONENT_PAGE_PATH_ID, MODE_NORMAL ).getSource(  );

                break;

            default:
                baXslSource = PortalComponentHome.getXsl( PORTAL_COMPONENT_PAGE_PATH_ID, nMode ).getSource(  );

                break;
        }

        String strXml = getXmlPagesList( getPagePath( nPageId ) );

        Properties outputProperties = ModeHome.getOuputXslProperties( nMode );

        // Added in v1.3
        // Add a path param for choose url to use in admin or normal mode
        Map<String, String> mapParamRequest = new HashMap<String, String>(  );
        setXslPortalPath( mapParamRequest, nMode );

        String strPath = XmlTransformerService.transformBySource( strXml, baXslSource, mapParamRequest, outputProperties );

        return formatPath( strPath, request );
    }

    /**
     * Returns the formated path of a xpage (ex : result of a seek)
     *
     * @param strXPageName The xpage name
     * @param nMode The mode to use for the formatting
     * @return the formated path
     */
    public static String getXPagePathContent( String strXPageName, int nMode, HttpServletRequest request )
    {
        // Added in v1.3
        byte[] baXslSource;

        // Selection of the XSL stylesheet
        switch ( nMode )
        {
            case MODE_NORMAL:
            case MODE_ADMIN:
                baXslSource = PortalComponentHome.getXsl( PORTAL_COMPONENT_PAGE_PATH_ID, MODE_NORMAL ).getSource(  );

                break;

            default:
                baXslSource = PortalComponentHome.getXsl( PORTAL_COMPONENT_PAGE_PATH_ID, nMode ).getSource(  );

                break;
        }

        String strXml = getXmlPagesList( getXPagePath( strXPageName ) );

        Properties outputProperties = ModeHome.getOuputXslProperties( nMode );

        //Added in v1.3
        // Add a path param for choose url to use in admin or normal mode
        Map<String, String> mapXslParams = new HashMap<String, String>(  );
        setXslPortalPath( mapXslParams, nMode );

        String strPath = XmlTransformerService.transformBySource( strXml, baXslSource, mapXslParams, outputProperties );

        return formatPath( strPath, request );
    }

    /**
     * Builds a collection of pages corresponding to the path of the page specified in parameter
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

    ////////////////////////////////////////////////////////////////////////////

    /**
     * Formats the path specified in parameter and returns it
     *
     * @param strPath The path to format
     * @return the html code to display the path
     */
    public static String formatPath( String strPath, HttpServletRequest request )
    {
        HashMap<String, String> model = new HashMap<String, String>(  );
        model.put( Markers.PAGE_PATH, strPath );

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
     * This method is used when giving the list of elements in the path as a Xml code.
     * This is called when using the Extended Xml path Label.
     *
     * @param strXPageName The xpage name
     * @param nMode The mode to use for the formatting
     * @param strTitlesUrls list of links (url and titles)
     * @return the formatted path
     */
    public static String getXPagePathContent( String strXPageName, int nMode, String strTitlesUrls,
        HttpServletRequest request )
    {
        // Selection of the XSL stylesheet
        byte[] baXslSource;

        // Selection of the XSL stylesheet
        switch ( nMode )
        {
            case MODE_NORMAL:
            case MODE_ADMIN:
                baXslSource = PortalComponentHome.getXsl( PORTAL_COMPONENT_PAGE_PATH_ID, MODE_NORMAL ).getSource(  );

                break;

            default:
                baXslSource = PortalComponentHome.getXsl( PORTAL_COMPONENT_PAGE_PATH_ID, nMode ).getSource(  );

                break;
        }

        String strXml = getXmlPagesListExtended( strTitlesUrls );

        //Added in v1.3
        // Add a path param for choose url to use in admin or normal mode
        Map<String, String> mapXslParams = new HashMap<String, String>(  );
        setXslPortalPath( mapXslParams, nMode );

        String strPath = XmlTransformerService.transformBySource( strXml, baXslSource, mapXslParams );

        return formatPath( strPath, request );
    }

    /**
     *
     */
    static void setXslPortalPath( Map<String, String> mapParameters, int nMode )
    {
        if ( nMode != MODE_ADMIN )
        {
            mapParameters.put( PARAMETER_SITE_PATH, AppPathService.getPortalUrl(  ) );
        }
        else
        {
            mapParameters.put( PARAMETER_SITE_PATH, AppPathService.getAdminPortalUrl(  ) );
        }
    }
}
