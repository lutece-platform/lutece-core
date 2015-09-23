/*
 * Copyright (c) 2002-2015, Mairie de Paris
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
import fr.paris.lutece.portal.business.stylesheet.StyleSheet;
import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import fr.paris.lutece.portal.service.html.XmlTransformerService;
import fr.paris.lutece.portal.service.page.PageEvent;
import fr.paris.lutece.portal.service.page.PageEventListener;
import fr.paris.lutece.portal.service.page.PageService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.util.xml.XmlUtil;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;


/**
 * This Service build the portal menu
 */
public final class PortalMenuService extends AbstractCacheableService implements PageEventListener
{
    public static final int MENU_INIT = 0;
    public static final int MENU_MAIN = 1;
    public static final int MODE_NORMAL = 0;
    public static final int MODE_ADMIN = 1;
    private static final int PORTAL_COMPONENT_MENU_INIT_ID = 3;
    private static final int PORTAL_COMPONENT_MAIN_MENU_ID = 4;
    private static final String SERVICE_NAME = "PortalMenuService";

    // Menus cache
    private static PortalMenuService _singleton;

    /** Creates a new instance of PortalMenuService */
    private PortalMenuService(  )
    {
        initCache( getName(  ) );
        PageService.addPageEventListener( this );
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
        }

        return _singleton;
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
     * @param request The HTTP request
     * @param nMode The selected mode
     * @param nPart The part of the menu to build
     * @param nCurrentPageId The current page ID
     * @return The list of the menus layed out with the stylesheet correpsonding to the mode
     */
    public String getMenuContent( int nCurrentPageId, int nMode, int nPart, HttpServletRequest request )
    {
        String strKey = getKey( nMode, nPart, request );
        String strMenu = (String) getFromCache( strKey );

        // Seek for the key in the cache
        if ( strMenu == null )
        {
            // Builds the HTML document
            strMenu = buildMenuContent( nCurrentPageId, nMode, nPart, request );

            // Add it in the cache
            putInCache( strKey, strMenu );

            return strMenu;
        }

        // The document exist in the cache
        return strMenu;
    }

    /**
     * Builds the menu bar
     *
     * @param nCurrentPageId The current page ID
     * @param nMode The selected mode
     * @param nPart The part of the menu to build
     * @param request The HttpServletRequest
     * @return The list of the menus layed out with the stylesheet of the mode
     */
    private String buildMenuContent( int nCurrentPageId, int nMode, int nPart, HttpServletRequest request )
    {
        StringBuffer strXml = new StringBuffer(  );
        Collection<Page> listPagesMenu = PageHome.getChildPagesMinimalData( PortalService.getRootPageId(  ) );

        String strCurrentPageId = Integer.toString( nCurrentPageId );

        strXml.append( XmlUtil.getXmlHeader(  ) );
        XmlUtil.beginElement( strXml, XmlContent.TAG_MENU_LIST );

        int nMenuIndex = 1;

        for ( Page menuPage : listPagesMenu )
        {
            if ( ( menuPage.isVisible( request ) ) || ( nMode == MODE_ADMIN ) )
            {
                //strCurrentPageId = request.getParameter( PARAMETER_PAGE_ID );
                XmlUtil.beginElement( strXml, XmlContent.TAG_MENU );
                XmlUtil.addElement( strXml, XmlContent.TAG_MENU_INDEX, nMenuIndex );
                XmlUtil.addElement( strXml, XmlContent.TAG_PAGE_ID, menuPage.getId(  ) );
                XmlUtil.addElementHtml( strXml, XmlContent.TAG_PAGE_NAME, menuPage.getName(  ) );
                XmlUtil.addElementHtml( strXml, XmlContent.TAG_CURRENT_PAGE_ID, strCurrentPageId );

                Collection<Page> listSubLevelMenuPages = PageHome.getChildPagesMinimalData( menuPage.getId(  ) );

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
                            XmlUtil.addElementHtml( strXml, XmlContent.TAG_CURRENT_PAGE_ID, strCurrentPageId );
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
        StyleSheet xslSource;

        // Selection of the XSL stylesheet
        switch ( nMode )
        {
            case MODE_NORMAL:
            case MODE_ADMIN:
                xslSource = PortalComponentHome.getXsl( PORTAL_COMPONENT_MAIN_MENU_ID, MODE_NORMAL );

                break;

            default:
                xslSource = PortalComponentHome.getXsl( PORTAL_COMPONENT_MAIN_MENU_ID, nMode );

                break;
        }

        if ( nPart == MENU_INIT )
        {
            switch ( nMode )
            {
                case MODE_NORMAL:
                case MODE_ADMIN:
                    xslSource = PortalComponentHome.getXsl( PORTAL_COMPONENT_MENU_INIT_ID, MODE_NORMAL );

                    break;

                default:
                    xslSource = PortalComponentHome.getXsl( PORTAL_COMPONENT_MENU_INIT_ID, nMode );

                    break;
            }
        }

        Properties outputProperties = ModeHome.getOuputXslProperties( nMode );

        // Added in v1.3
        // Add a path param for choose url to use in admin or normal mode
        Map<String, String> mapParamRequest = new HashMap<String, String>(  );
        PortalService.setXslPortalPath( mapParamRequest, nMode );

        XmlTransformerService xmlTransformerService = new XmlTransformerService(  );

        return xmlTransformerService.transformBySourceWithXslCache( strXml.toString(  ), xslSource, mapParamRequest,
            outputProperties );
    }

    /**
     * Returns the key corresponding to the part according to the selected mode
     *
     * @param nMode The mode
     * @param nPart the part
     * @param request The HTTP request
     * @return The key as a String
     */
    private String getKey( int nMode, int nPart, HttpServletRequest request )
    {
        String strRoles = "-";

        if ( SecurityService.isAuthenticationEnable(  ) )
        {
            if ( request != null )
            {
                LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );

                if ( ( user != null ) && ( user.getRoles(  ) != null ) )
                {
                    String[] roles = user.getRoles(  );
                    Arrays.sort( roles );
                    strRoles = StringUtils.join( roles, ',' );
                }
            }
        }

        StringBuilder sbKey = new StringBuilder(  );
        sbKey.append( "[menu:" ).append( nPart ).append( "][m:" ).append( nMode ).append( "][roles:" ).append( strRoles )
             .append( ']' );

        return sbKey.toString(  );
    }

    @Override
    public void processPageEvent( PageEvent event )
    {
        // page was added, removed or updated; clear cache
        resetCache(  );
    }
}
