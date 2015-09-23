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
package fr.paris.lutece.portal.web.xpages;

import fr.paris.lutece.portal.business.XmlContent;
import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.page.PageHome;
import fr.paris.lutece.portal.business.portalcomponent.PortalComponentHome;
import fr.paris.lutece.portal.business.style.ModeHome;
import fr.paris.lutece.portal.business.stylesheet.StyleSheet;
import fr.paris.lutece.portal.service.html.XmlTransformerService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.admin.AdminPageJspBean;
import fr.paris.lutece.util.xml.XmlUtil;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the map of the pages on the site
 */
public class SiteMapApp implements XPageApplication
{
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 1799476496018552101L;
    private static final int PORTAL_COMPONENT_SITE_MAP_ID = 6;
    private static final int MODE_NORMAL = 0;
    private static final int MODE_ADMIN = 1;
    private static final String PARAMETER_SITE_PATH = "site-path";
    private static final String MARKER_TARGET = "target";
    private static final String TARGET_TOP = "target='_top'";
    private static final String PROPERTY_SERVICE_NAME = "portal.site.serviceName.siteMapService";
    private static final String PROPERTY_PATH_LABEL = "portal.site.site_map.pathLabel";
    private static final String PROPERTY_PAGE_TITLE = "portal.site.site_map.pageTitle";

    /**
     * Creates a new SiteMapPage object
     */
    public SiteMapApp(  )
    {
    }

    /**
     * Returns the localized service name
     * @param locale The locale
     * @return The localized service name
     */
    public String getName( Locale locale )
    {
        return I18nService.getLocalizedString( PROPERTY_SERVICE_NAME, locale );
    }

    /**
     * Build or get in the cache the page which contains the site map depending
     * on the mode
     *
     * @param request The Http request
     * @param nMode The selected mode
     * @param plugin The plugin
     * @return The content of the site map
     */
    @Override
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
    {
        XPage page = new XPage(  );
        String strKey = getKey( nMode, request );

        Locale locale = request.getLocale(  );

        SiteMapCacheService siteMapCacheService = SiteMapCacheService.getInstance(  );

        // Check the key in the cache
        String strCachedPage = siteMapCacheService.isCacheEnable(  )
            ? (String) siteMapCacheService.getFromCache( strKey ) : null;

        if ( strCachedPage == null )
        {
            // Build the HTML document
            String strPage = buildPageContent( nMode, request );

            // Add it to the cache
            if ( siteMapCacheService.isCacheEnable(  ) )
            {
                synchronized ( strKey )
                {
                    siteMapCacheService.putInCache( strKey, strPage );
                }
            }

            page.setPathLabel( I18nService.getLocalizedString( PROPERTY_PATH_LABEL, locale ) );
            page.setTitle( I18nService.getLocalizedString( PROPERTY_PAGE_TITLE, locale ) );
            page.setContent( strPage );

            return page;
        }

        // The document exist in the cache
        page.setPathLabel( I18nService.getLocalizedString( PROPERTY_PATH_LABEL, locale ) );
        page.setTitle( I18nService.getLocalizedString( PROPERTY_PAGE_TITLE, locale ) );
        page.setContent( strCachedPage );

        return page;
    }

    /**
     * Gets the cache key
     * @param nMode The mode
     * @param request The HTTP request
     * @return The key
     */
    private String getKey( int nMode, HttpServletRequest request )
    {
        String strRoles = "-";

        if ( SecurityService.isAuthenticationEnable(  ) )
        {
            LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );

            if ( ( user != null ) && ( user.getRoles(  ) != null ) )
            {
                String[] roles = user.getRoles(  );
                Arrays.sort( roles );
                strRoles = StringUtils.join( roles, ',' );
            }
        }

        return "[m:" + nMode + "][roles:" + strRoles + "]";
    }

    /**
     * Build an XML document containing the arborescence of the site pages and
     * transform it with the stylesheet
     * combined with the mode
     * @param nMode The selected mode
     * @param request The HttpServletRequest
     * @return The content of the site map
     */
    private String buildPageContent( int nMode, HttpServletRequest request )
    {
        StringBuffer strArborescenceXml = new StringBuffer(  );
        strArborescenceXml.append( XmlUtil.getXmlHeader(  ) );

        int nLevel = 0;
        findPages( strArborescenceXml, PortalService.getRootPageId(  ), nLevel, request );

        // Added in v1.3
        // Use the same stylesheet for normal or admin mode
        StyleSheet xslSource;

        switch ( nMode )
        {
            case MODE_NORMAL:
            case MODE_ADMIN:
                xslSource = PortalComponentHome.getXsl( PORTAL_COMPONENT_SITE_MAP_ID, MODE_NORMAL );

                break;

            default:
                xslSource = PortalComponentHome.getXsl( PORTAL_COMPONENT_SITE_MAP_ID, nMode );

                break;
        }

        // Added in v1.3
        // Add a path param for choose url to use in admin or normal mode
        Map<String, String> mapParamRequest = new HashMap<String, String>(  );

        if ( nMode != MODE_ADMIN )
        {
            mapParamRequest.put( PARAMETER_SITE_PATH, AppPathService.getPortalUrl(  ) );
        }
        else
        {
            mapParamRequest.put( PARAMETER_SITE_PATH, AppPathService.getAdminPortalUrl(  ) );
            mapParamRequest.put( MARKER_TARGET, TARGET_TOP );
        }

        Properties outputProperties = ModeHome.getOuputXslProperties( nMode );

        XmlTransformerService xmlTransformerService = new XmlTransformerService(  );

        return xmlTransformerService.transformBySourceWithXslCache( strArborescenceXml.toString(  ), xslSource,
            mapParamRequest, outputProperties );
    }

    /**
     * Build recursively the XML document containing the arborescence of the
     * site pages
     * @param strXmlArborescence The buffer in which adding the current page of
     *            the arborescence
     * @param nPageId The current page of the recursive course
     * @param nLevel The depth level of the page in the arborescence
     * @param request The HttpServletRequest
     */
    private void findPages( StringBuffer strXmlArborescence, int nPageId, int nLevel, HttpServletRequest request )
    {
        Page page = PageHome.getPage( nPageId );

        if ( page.isVisible( request ) )
        {
            XmlUtil.beginElement( strXmlArborescence, XmlContent.TAG_PAGE );
            XmlUtil.addElement( strXmlArborescence, XmlContent.TAG_PAGE_ID, page.getId(  ) );
            XmlUtil.addElementHtml( strXmlArborescence, XmlContent.TAG_PAGE_NAME, page.getName(  ) );
            XmlUtil.addElement( strXmlArborescence, XmlContent.TAG_PAGE_DESCRIPTION, page.getDescription(  ) );
            XmlUtil.addElement( strXmlArborescence, XmlContent.TAG_PAGE_LEVEL, nLevel );

            AdminPageJspBean adminPage = new AdminPageJspBean(  );

            if ( page.getImageContent(  ) != null )
            {
                int nImageLength = page.getImageContent(  ).length;

                if ( nImageLength >= 1 )
                {
                    String strPageId = Integer.toString( page.getId(  ) );
                    XmlUtil.addElement( strXmlArborescence, XmlContent.TAG_PAGE_IMAGE,
                        adminPage.getResourceImagePage( page, strPageId ) );
                }
            }

            XmlUtil.beginElement( strXmlArborescence, XmlContent.TAG_CHILD_PAGES_LIST );

            for ( Page pageChild : PageHome.getChildPagesMinimalData( nPageId ) )
            {
                findPages( strXmlArborescence, pageChild.getId(  ), nLevel + 1, request );
            }

            XmlUtil.endElement( strXmlArborescence, XmlContent.TAG_CHILD_PAGES_LIST );
            XmlUtil.endElement( strXmlArborescence, XmlContent.TAG_PAGE );
        }
    }
}
