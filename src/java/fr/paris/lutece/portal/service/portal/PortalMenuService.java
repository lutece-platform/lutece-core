/*
 * Copyright (c) 2002-2009, Mairie de Paris
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
import fr.paris.lutece.portal.service.html.XmlTransformerService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;


/**
 * This Service build the portal menu
 */
public final class PortalMenuService implements CacheableService
{
    public static final int MENU_INIT = 0;
    public static final int MENU_MAIN = 1;
    private static final int PORTAL_COMPONENT_MENU_INIT_ID = 3;
    private static final int PORTAL_COMPONENT_MAIN_MENU_ID = 4;
    private static final int PORTAL_COMPONENT_MENU_TREE = 7;
    private static final int MODE_NORMAL = 0;
    private static final int MODE_ADMIN = 1;
    private static final String SERVICE_NAME = "PortalMenuService";
    private static final String PROPERTY_ROOT_TREE = "lutece.root.tree";

    // Parameters    
    private static final String PARAMETER_PAGE_ID = "page_id";

    // Menus cache
    private static PortalMenuService _singleton;
    private static Map<String, String> _mapMenusCache = new HashMap<String, String>(  );

    /** Creates a new instance of PortalMenuService */
    private PortalMenuService(  )
    {
    }

    /**
     * Get the unique instance of the service
     * @return The unique instance
     */
    public static synchronized PortalMenuService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = new PortalMenuService(  );
            // Register this service as cacheable service
            PortalService.registerCacheableService( _singleton.getName(  ), _singleton );
        }

        return _singleton;
    }

    /**
     * Returns the cache status : enable or disable
     * @return True if the cache is enable, otherwise false
     */
    public boolean isCacheEnable(  )
    {
        return true;
    }

    /**
     * Returns the number of objects handled by the cache
     * @return the number of objects handled by the cache
     */
    public int getCacheSize(  )
    {
        return _mapMenusCache.size(  );
    }

    /**
     * Clear the cache
     */
    public void resetCache(  )
    {
        _mapMenusCache.clear(  );
    }

    /**
     * Returns the service name
     * @return The service name
     */
    public String getName(  )
    {
        return SERVICE_NAME;
    }

    /**
     * Returns the menu bar from the cache or builds it if it not stored in it
     * @return The list of the menus layed out with the stylesheet correpsonding to the mode
     * @param request The HTTP request
     * @param nMode The selected mode
     * @param nPart The part of the menu to build
     */
    String getMenuContent( int nMode, int nPart, HttpServletRequest request )
    {
        String strKey = getKey( nMode, nPart, request );

        // Seek for the key in the cache
        if ( !_mapMenusCache.containsKey( strKey ) )
        {
            // Builds the HTML document
            String strMenu = buildMenuContent( nMode, nPart, request );

            // Add it in the cache
            _mapMenusCache.put( strKey, strMenu );

            return strMenu;
        }

        // The document exist in the cache
        return _mapMenusCache.get( strKey );
    }

    /**
     * Builds the menu bar
     *
     * @param nMode The selected mode
     * @param nPart The part of the menu to build
         * @param request The HttpServletRequest
     * @return The list of the menus layed out with the stylesheet of the mode
     */
    private String buildMenuContent( int nMode, int nPart, HttpServletRequest request )
    {
        StringBuffer strXml = new StringBuffer(  );
        Collection<Page> listPagesMenu = PageHome.getChildPages( PortalService.getRootPageId(  ) );

        String strCurrentPageId = Integer.toString( PortalService.getRootPageId(  ) );

        strXml.append( XmlUtil.getXmlHeader(  ) );
        XmlUtil.beginElement( strXml, XmlContent.TAG_MENU_LIST );

        int nMenuIndex = 1;

        for ( Page menuPage : listPagesMenu )
        {
            if ( ( menuPage.isVisible( request ) ) || ( nMode == MODE_ADMIN ) )
            {
                strCurrentPageId = request.getParameter( PARAMETER_PAGE_ID );
                XmlUtil.beginElement( strXml, XmlContent.TAG_MENU );
                XmlUtil.addElement( strXml, XmlContent.TAG_MENU_INDEX, nMenuIndex );
                XmlUtil.addElement( strXml, XmlContent.TAG_PAGE_ID, menuPage.getId(  ) );
                XmlUtil.addElementHtml( strXml, XmlContent.TAG_PAGE_NAME, menuPage.getName(  ) );
                XmlUtil.addElementHtml( strXml, XmlContent.TAG_CURRENT_PAGE_ID, strCurrentPageId );

                Collection<Page> listSubLevelMenuPages = PageHome.getChildPages( menuPage.getId(  ) );

                // add element submenu-list only if list not empty
                if ( !listSubLevelMenuPages.isEmpty(  ) )
                {
                    // Seek of the sub-menus
                    XmlUtil.beginElement( strXml, XmlContent.TAG_SUBLEVEL_MENU_LIST );

                    int nSubLevelMenuIndex = 1;

                    for ( Page subLevelMenuPage : listSubLevelMenuPages )
                    {
                        if ( ( subLevelMenuPage.isVisible( request ) ) || ( nMode == MODE_ADMIN ) )
                        {
                            XmlUtil.beginElement( strXml, XmlContent.TAG_SUBLEVEL_MENU );
                            XmlUtil.addElement( strXml, XmlContent.TAG_MENU_INDEX, nMenuIndex );
                            XmlUtil.addElement( strXml, XmlContent.TAG_SUBLEVEL_INDEX, nSubLevelMenuIndex );
                            XmlUtil.addElement( strXml, XmlContent.TAG_PAGE_ID, subLevelMenuPage.getId(  ) );
                            XmlUtil.addElementHtml( strXml, XmlContent.TAG_PAGE_NAME, subLevelMenuPage.getName(  ) );
                            XmlUtil.endElement( strXml, XmlContent.TAG_SUBLEVEL_MENU );
                        }
                    }

                    XmlUtil.endElement( strXml, XmlContent.TAG_SUBLEVEL_MENU_LIST );
                }

                XmlUtil.endElement( strXml, XmlContent.TAG_MENU );
                nMenuIndex++;
            }
        }

        XmlUtil.endElement( strXml, XmlContent.TAG_MENU_LIST );

        // Added in v1.3
        // Use the same stylesheet for normal or admin mode
        byte[] baXslSource;

        // Selection of the XSL stylesheet
        switch ( nMode )
        {
            case MODE_NORMAL:
            case MODE_ADMIN:
                baXslSource = PortalComponentHome.getXsl( PORTAL_COMPONENT_MAIN_MENU_ID, MODE_NORMAL ).getSource(  );

                break;

            default:
                baXslSource = PortalComponentHome.getXsl( PORTAL_COMPONENT_MAIN_MENU_ID, nMode ).getSource(  );

                break;
        }

        if ( nPart == MENU_INIT )
        {
            switch ( nMode )
            {
                case MODE_NORMAL:
                case MODE_ADMIN:
                    baXslSource = PortalComponentHome.getXsl( PORTAL_COMPONENT_MENU_INIT_ID, MODE_NORMAL ).getSource(  );

                    break;

                default:
                    baXslSource = PortalComponentHome.getXsl( PORTAL_COMPONENT_MENU_INIT_ID, nMode ).getSource(  );

                    break;
            }
        }

        Properties outputProperties = ModeHome.getOuputXslProperties( nMode );

        // Added in v1.3
        // Add a path param for choose url to use in admin or normal mode
        Map<String, String> mapParamRequest = new HashMap<String, String>(  );
        PortalService.setXslPortalPath( mapParamRequest, nMode );

        return XmlTransformerService.transformBySource( strXml.toString(  ), baXslSource, mapParamRequest,
            outputProperties );
    }

    /**
     * Builds the tree menu bar
     *
     * @param nIdPage The page id
     * @param nMode the mode id
         * @param request The HttpServletRequest
     * @return The list of the tree menus layed out with the stylesheet of the mode
     */
    public String buildTreeMenuContent( int nIdPage, int nMode, HttpServletRequest request )
    {
        StringBuffer strXml = new StringBuffer(  );

        String strTreeOnRoot = AppPropertiesService.getProperty( PROPERTY_ROOT_TREE );
        Collection<Page> listPagesMenu;

        // If the current page is the home page or the string strPathOnRoot equals false, not display the path
        if ( strTreeOnRoot.equalsIgnoreCase( "true" ) )
        {
            listPagesMenu = PageHome.getChildPages( getPageTree( nIdPage ) );
        }
        else
        {
            listPagesMenu = PageHome.getChildPages( nIdPage );
        }

        strXml.append( XmlUtil.getXmlHeader(  ) );
        XmlUtil.beginElement( strXml, XmlContent.TAG_MENU_LIST );

        int nMenuIndex = 1;

        for ( Page menuPage : listPagesMenu )
        {
            if ( ( menuPage.isVisible( request ) ) || ( nMode == MODE_ADMIN ) )
            {
                XmlUtil.beginElement( strXml, XmlContent.TAG_MENU );
                XmlUtil.addElement( strXml, XmlContent.TAG_MENU_INDEX, nMenuIndex );
                XmlUtil.addElement( strXml, XmlContent.TAG_PAGE_ID, menuPage.getId(  ) );
                XmlUtil.addElementHtml( strXml, XmlContent.TAG_PAGE_NAME, menuPage.getName(  ) );
                XmlUtil.addElementHtml( strXml, XmlContent.TAG_PAGE_DESCRIPTION, menuPage.getDescription(  ) );

                // Seek of the sub-menus
                XmlUtil.beginElement( strXml, XmlContent.TAG_SUBLEVEL_MENU_LIST );

                Collection<Page> listSubLevelMenuPages = PageHome.getChildPages( menuPage.getId(  ) );
                int nSubLevelMenuIndex = 1;

                for ( Page subLevelMenuPage : listSubLevelMenuPages )
                {
                    if ( ( subLevelMenuPage.isVisible( request ) ) || ( nMode == MODE_ADMIN ) )
                    {
                        XmlUtil.beginElement( strXml, XmlContent.TAG_SUBLEVEL_MENU );
                        XmlUtil.addElement( strXml, XmlContent.TAG_MENU_INDEX, nMenuIndex );
                        XmlUtil.addElement( strXml, XmlContent.TAG_SUBLEVEL_INDEX, nSubLevelMenuIndex );
                        XmlUtil.addElement( strXml, XmlContent.TAG_PAGE_ID, subLevelMenuPage.getId(  ) );
                        XmlUtil.addElementHtml( strXml, XmlContent.TAG_PAGE_NAME, subLevelMenuPage.getName(  ) );
                        XmlUtil.addElementHtml( strXml, XmlContent.TAG_PAGE_DESCRIPTION,
                            subLevelMenuPage.getDescription(  ) );
                        XmlUtil.endElement( strXml, XmlContent.TAG_SUBLEVEL_MENU );
                    }
                }

                XmlUtil.endElement( strXml, XmlContent.TAG_SUBLEVEL_MENU_LIST );
                XmlUtil.endElement( strXml, XmlContent.TAG_MENU );
                nMenuIndex++;
            }
        }

        XmlUtil.endElement( strXml, XmlContent.TAG_MENU_LIST );

        byte[] baXslSource;

        // Selection of the XSL stylesheet
        switch ( nMode )
        {
            case MODE_NORMAL:
            case MODE_ADMIN:
                baXslSource = PortalComponentHome.getXsl( PORTAL_COMPONENT_MENU_TREE, MODE_NORMAL ).getSource(  );

                break;

            default:
                baXslSource = PortalComponentHome.getXsl( PORTAL_COMPONENT_MENU_TREE, nMode ).getSource(  );

                break;
        }

        Properties outputProperties = ModeHome.getOuputXslProperties( nMode );

        // Added in v1.3
        // Add a path param for choose url to use in admin or normal mode
        Map<String, String> mapParamRequest = new HashMap<String, String>(  );
        PortalService.setXslPortalPath( mapParamRequest, nMode );

        return XmlTransformerService.transformBySource( strXml.toString(  ), baXslSource, mapParamRequest,
            outputProperties );
    }

    /**
     *
     * @param nPageId The page identifier
     * @return The parent page identifier
     */
    private int getPageTree( int nPageId )
    {
        Page page = PageHome.getPage( nPageId );
        int nParentPageId = page.getParentPageId(  );

        if ( nParentPageId == 0 )
        {
            return nPageId;
        }

        int nParentTree = nParentPageId;

        //while ( nParentPageId != 1 )
        while ( nParentPageId != 0 )
        {
            nParentTree = nParentPageId;

            Page parentPage = PageHome.getPage( nParentPageId );
            nParentPageId = parentPage.getParentPageId(  );
        }

        return nParentTree;
    }

    /**
     * Returns the key corresponding to the part accroding to the selected mode
     *
     * @param nMode The mode
     * @param nPart the part
     * @param request The HTTP request
     * @return The key as a String
     */
    private String getKey( int nMode, int nPart, HttpServletRequest request )
    {
        String strUser = "";

        if ( SecurityService.isAuthenticationEnable(  ) )
        {
            if ( request != null )
            {
                LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );

                if ( user != null )
                {
                    strUser = user.getName(  );
                }
            }
        }

        return "" + nMode + nPart + strUser;
    }
}
