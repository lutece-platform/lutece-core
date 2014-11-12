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
package fr.paris.lutece.portal.web.admin;

import fr.paris.lutece.portal.business.XmlContent;
import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.page.PageHome;
import fr.paris.lutece.portal.business.portalcomponent.PortalComponentHome;
import fr.paris.lutece.portal.business.style.ModeHome;
import fr.paris.lutece.portal.business.stylesheet.StyleSheet;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.html.XmlTransformerService;
import fr.paris.lutece.portal.service.page.PageResourceIdService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the map of the pages on the site
 */
public class AdminMapJspBean extends AdminFeaturesPageJspBean
{
    // Right
    public static final String RIGHT_MANAGE_ADMIN_SITE = "CORE_ADMIN_SITE";
    private static final long serialVersionUID = 2871979154306491511L;

    // Markers
    private static final String MARKER_MAP_SITE = "map_site";
    private static final String MARK_PAGE = "page";

    // Templates
    private static final String TEMPLATE_MAP_SITE = "admin/site/site_map.html";

    // Parameters
    private static final String PARAMETER_SITE_PATH = "site-path";
    private static final String PARAMETER_PAGE_ID = "page_id";

    // Properties
    private static final String PROPERTY_ADMIN_PATH = "lutece.admin.path";

    // Xml Tags
    private static final String TAG_CSS_ID = "css-id";
    private static final String TAG_PAGE_ROLE = "page-role";
    private static final int PORTAL_COMPONENT_SITE_MAP_ID = 8;
    private static final int MODE_ADMIN = 1;

    /**
     * Build or get in the cache the page which contains the site map depending on the mode
     *
     * @param request The Http request
     * @return The content of the site map
     */
    public String getMap( HttpServletRequest request )
    {
        StringBuffer strArborescenceXml = new StringBuffer(  );

        StringBuffer strCssId = new StringBuffer(  );
        int nLevel = 0;

        String strCurrentPageId = request.getParameter( PARAMETER_PAGE_ID );

        findPages( request, strArborescenceXml, PortalService.getRootPageId(  ), nLevel, strCurrentPageId, strCssId );

        StyleSheet xslSource = PortalComponentHome.getXsl( PORTAL_COMPONENT_SITE_MAP_ID, MODE_ADMIN );

        // Added in v1.3
        // Add a path param for choose url to use in admin or normal mode
        Map<String, String> mapParamRequest = new HashMap<String, String>(  );
        mapParamRequest.put( PARAMETER_SITE_PATH, AppPropertiesService.getProperty( PROPERTY_ADMIN_PATH ) );

        Properties outputProperties = ModeHome.getOuputXslProperties( MODE_ADMIN );

        Map<String, Object> model = new HashMap<String, Object>(  );
        XmlTransformerService xmlTransformerService = new XmlTransformerService(  );
        String map = xmlTransformerService.transformBySourceWithXslCache( strArborescenceXml.toString(  ), xslSource,
                mapParamRequest, outputProperties );

        String strPageId = request.getParameter( PARAMETER_PAGE_ID );
        int nPageId = ( strPageId != null ) ? Integer.parseInt( strPageId ) : 1;
        Page page = PageHome.getPage( nPageId );

        model.put( MARK_PAGE, page );
        model.put( MARKER_MAP_SITE, map );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MAP_SITE, getLocale(  ), model );

        return getAdminPage( t.getHtml(  ) );
    }

    /**
     * Build recursively the XML document containing the arborescence of the site pages
     * @param request The HttpServletRequest
     * @param strXmlArborescence The buffer in which adding the current page of the arborescence
     * @param nPageId The current page of the recursive course
     * @param nLevel The depth level of the page in the arborescence
     * @param strCurrentPageId the id of the current page
     * @param strCssId The id Css for menu tree
     */
    private void findPages( HttpServletRequest request, StringBuffer strXmlArborescence, int nPageId, int nLevel,
        String strCurrentPageId, StringBuffer strCssId )
    {
        Page page = PageHome.getPage( nPageId );

        AdminUser user = AdminUserService.getAdminUser( request );
        String strPageId = Integer.toString( nPageId );

        boolean bAuthorizationPage;

        if ( nPageId == PortalService.getRootPageId(  ) )
        {
            bAuthorizationPage = true;
        }
        else
        {
            // Control the node_status
            if ( page.getNodeStatus(  ) != 0 )
            {
                Page parentPage = PageHome.getPage( page.getParentPageId(  ) );
                int nParentPageNodeStatus = parentPage.getNodeStatus(  );
                int nParentPageId = parentPage.getId(  );

                // If 0 the page have a node authorization, else
                // the parent page node_status must be controlled
                // until it is equal to 0
                while ( nParentPageNodeStatus != 0 )
                {
                    parentPage = PageHome.getPage( nParentPageId );
                    nParentPageNodeStatus = parentPage.getNodeStatus(  );
                    nParentPageId = parentPage.getParentPageId(  );
                }

                strPageId = Integer.toString( parentPage.getId(  ) );
            }

            bAuthorizationPage = RBACService.isAuthorized( Page.RESOURCE_TYPE, strPageId,
                    PageResourceIdService.PERMISSION_VIEW, user );
        }

        XmlUtil.beginElement( strXmlArborescence, XmlContent.TAG_PAGE );

        if ( bAuthorizationPage )
        {
            XmlUtil.addElementHtml( strXmlArborescence, XmlContent.TAG_CURRENT_PAGE_ID, strCurrentPageId );
            XmlUtil.addElement( strXmlArborescence, XmlContent.TAG_PAGE_ID, page.getId(  ) );
            XmlUtil.addElementHtml( strXmlArborescence, XmlContent.TAG_PAGE_NAME, page.getName(  ) );
            XmlUtil.addElement( strXmlArborescence, XmlContent.TAG_PAGE_DESCRIPTION, page.getDescription(  ) );
            XmlUtil.addElement( strXmlArborescence, XmlContent.TAG_PAGE_LEVEL, nLevel );
            XmlUtil.addElement( strXmlArborescence, XmlContent.TAG_PARENT_PAGE_ID, page.getParentPageId(  ) );
            XmlUtil.addElement( strXmlArborescence, TAG_PAGE_ROLE, page.getRole(  ) );

            AdminPageJspBean adminPage = new AdminPageJspBean(  );

            if ( page.getImageContent(  ) != null )
            {
                int nImageLength = page.getImageContent(  ).length;

                if ( nImageLength >= 1 )
                {
                    XmlUtil.addElement( strXmlArborescence, XmlContent.TAG_PAGE_IMAGE,
                        adminPage.getResourceImagePage( page, strPageId ) );
                }
            }
        }

        XmlUtil.beginElement( strXmlArborescence, XmlContent.TAG_CHILD_PAGES_LIST );

        for ( Page pageChild : PageHome.getChildPagesMinimalData( nPageId ) )
        {
            findPages( request, strXmlArborescence, pageChild.getId(  ), nLevel + 1, strCurrentPageId, strCssId );
            strCssId.append( "initializeMenu('menu" + pageChild.getId(  ) + "' , 'actuator" + pageChild.getId(  ) +
                "');\n" );
        }

        XmlUtil.endElement( strXmlArborescence, XmlContent.TAG_CHILD_PAGES_LIST );

        if ( bAuthorizationPage )
        {
            XmlUtil.addElementHtml( strXmlArborescence, TAG_CSS_ID, strCssId.toString(  ) );
        }

        XmlUtil.endElement( strXmlArborescence, XmlContent.TAG_PAGE );
    }
}
