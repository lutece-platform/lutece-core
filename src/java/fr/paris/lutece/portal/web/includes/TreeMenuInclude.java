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
package fr.paris.lutece.portal.web.includes;

import fr.paris.lutece.portal.business.XmlContent;
import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.page.PageHome;
import fr.paris.lutece.portal.business.portalcomponent.PortalComponentHome;
import fr.paris.lutece.portal.business.style.ModeHome;
import fr.paris.lutece.portal.business.stylesheet.StyleSheet;
import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.html.XmlTransformerService;
import fr.paris.lutece.portal.service.includes.PageInclude;
import fr.paris.lutece.portal.service.portal.PortalMenuService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Markers;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides  the list of the page associated by the main menu of the site
 */
public class TreeMenuInclude implements PageInclude
{
    /////////////////////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final int PORTAL_COMPONENT_MENU_TREE = 7;

    // Properties
    private static final String PROPERTY_ROOT_TREE = "lutece.root.tree";

    /**
     * Substitue specific Freemarker markers in the page template.
     * @param rootModel the HashMap containing markers to substitute
     * @param data A PageData object containing applications data
     * @param nMode The current mode
     * @param request The HTTP request
     */
    public void fillTemplate( Map<String, Object> rootModel, PageData data, int nMode, HttpServletRequest request )
    {
        if ( request != null )
        {
            int nCurrentPageId;

            try
            {
                nCurrentPageId = ( request.getParameter( Parameters.PAGE_ID ) == null )
                    ? PortalService.getRootPageId(  ) : Integer.parseInt( request.getParameter( Parameters.PAGE_ID ) );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.info( "TreeMenuInclude.fillTemplate() : " + nfe.getLocalizedMessage(  ) );
                nCurrentPageId = 0;
            }

            data.setTreeMenu( buildTreeMenuContent( nCurrentPageId, nMode, request ) );
            rootModel.put( Markers.PAGE_TREE_MENU, ( data.getTreeMenu(  ) == null ) ? "" : data.getTreeMenu(  ) );
        }
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

        String strCurrentPageId = Integer.toString( nIdPage );

        String strTreeOnRoot = AppPropertiesService.getProperty( PROPERTY_ROOT_TREE );
        Collection<Page> listPagesMenu;

        // If the current page is the home page or the string strTreeOnRoot equals false, not display the treeMenu
        if ( strTreeOnRoot.equalsIgnoreCase( "true" ) )
        {
            listPagesMenu = PageHome.getChildPagesMinimalData( getPageTree( nIdPage ) );
        }
        else
        {
            listPagesMenu = PageHome.getChildPagesMinimalData( nIdPage );
        }

        strXml.append( XmlUtil.getXmlHeader(  ) );
        XmlUtil.beginElement( strXml, XmlContent.TAG_MENU_LIST );

        int nMenuIndex = 1;

        for ( Page menuPage : listPagesMenu )
        {
            if ( ( menuPage.isVisible( request ) ) || ( nMode == PortalMenuService.MODE_ADMIN ) )
            {
                XmlUtil.beginElement( strXml, XmlContent.TAG_MENU );
                XmlUtil.addElement( strXml, XmlContent.TAG_MENU_INDEX, nMenuIndex );
                XmlUtil.addElement( strXml, XmlContent.TAG_PAGE_ID, menuPage.getId(  ) );
                XmlUtil.addElementHtml( strXml, XmlContent.TAG_PAGE_NAME, menuPage.getName(  ) );
                XmlUtil.addElementHtml( strXml, XmlContent.TAG_PAGE_DESCRIPTION, menuPage.getDescription(  ) );
                XmlUtil.addElementHtml( strXml, XmlContent.TAG_CURRENT_PAGE_ID, strCurrentPageId );

                // Seek of the sub-menus
                XmlUtil.beginElement( strXml, XmlContent.TAG_SUBLEVEL_MENU_LIST );

                Collection<Page> listSubLevelMenuPages = PageHome.getChildPagesMinimalData( menuPage.getId(  ) );
                int nSubLevelMenuIndex = 1;

                for ( Page subLevelMenuPage : listSubLevelMenuPages )
                {
                    if ( ( subLevelMenuPage.isVisible( request ) ) || ( nMode == PortalMenuService.MODE_ADMIN ) )
                    {
                        XmlUtil.beginElement( strXml, XmlContent.TAG_SUBLEVEL_MENU );
                        XmlUtil.addElement( strXml, XmlContent.TAG_MENU_INDEX, nMenuIndex );
                        XmlUtil.addElement( strXml, XmlContent.TAG_SUBLEVEL_INDEX, nSubLevelMenuIndex );
                        XmlUtil.addElement( strXml, XmlContent.TAG_PAGE_ID, subLevelMenuPage.getId(  ) );
                        XmlUtil.addElementHtml( strXml, XmlContent.TAG_PAGE_NAME, subLevelMenuPage.getName(  ) );
                        XmlUtil.addElementHtml( strXml, XmlContent.TAG_PAGE_DESCRIPTION,
                            subLevelMenuPage.getDescription(  ) );
                        XmlUtil.addElementHtml( strXml, XmlContent.TAG_CURRENT_PAGE_ID, strCurrentPageId );
                        XmlUtil.endElement( strXml, XmlContent.TAG_SUBLEVEL_MENU );
                    }
                }

                XmlUtil.endElement( strXml, XmlContent.TAG_SUBLEVEL_MENU_LIST );
                XmlUtil.endElement( strXml, XmlContent.TAG_MENU );
                nMenuIndex++;
            }
        }

        XmlUtil.endElement( strXml, XmlContent.TAG_MENU_LIST );

        StyleSheet xslSource;

        // Selection of the XSL stylesheet
        switch ( nMode )
        {
            case PortalMenuService.MODE_NORMAL:
            case PortalMenuService.MODE_ADMIN:
                xslSource = PortalComponentHome.getXsl( PORTAL_COMPONENT_MENU_TREE, PortalMenuService.MODE_NORMAL );

                break;

            default:
                xslSource = PortalComponentHome.getXsl( PORTAL_COMPONENT_MENU_TREE, nMode );

                break;
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

            Page parentPage = PageHome.getPageWithoutImageContent( nParentPageId );
            nParentPageId = parentPage.getParentPageId(  );
        }

        return nParentTree;
    }
}
