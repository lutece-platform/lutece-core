/*
 * Copyright (c) 2002-2022, City of Paris
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
import fr.paris.lutece.portal.service.cache.Lutece107Cache;
import fr.paris.lutece.portal.service.cache.LuteceCache;
import fr.paris.lutece.portal.service.html.XmlTransformerService;
import fr.paris.lutece.portal.service.page.PageEvent;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.menu.MenuItem;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.xml.XmlUtil;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

/**
 * This Service build the portal menu
 */
@ApplicationScoped
public class PortalMenuService
{
    public static final int MENU_INIT = 0;
    public static final int MENU_MAIN = 1;
    public static final int MODE_NORMAL = 0;
    public static final int MODE_ADMIN = 1;
    private static final int PORTAL_COMPONENT_MENU_INIT_ID = 3;
    private static final int PORTAL_COMPONENT_MAIN_MENU_ID = 4;
    private static final String SERVICE_NAME = "PortalMenuService";
    private static final String TEMPLATE_MAIN_MENU = "skin/site/menu_main.html";
    private static final String MARK_SITE_PATH = "site_path";
    private static final String MARK_MENU_ITEMS = "items";
    private static final String PARAMETER_SITE_PATH = "site-path";
    @Inject
	@LuteceCache(cacheName = SERVICE_NAME, keyType = String.class, valueType = String.class, enable = true)
	private Lutece107Cache<String, String> _cachePortalMenu;
    @Inject
    @ConfigProperty( name = "lutece.style.menu.xsl", defaultValue = "false" )
    private boolean _bUseXslStylesheet;
    
    PortalMenuService( )
    {
        // Ctor
    }

    /**
     * Returns the unique instance of the {@link PortalMenuService} service.
     * 
     * <p>This method is deprecated and is provided for backward compatibility only. 
     * For new code, use dependency injection with {@code @Inject} to obtain the 
     * {@link PortalMenuService} instance instead.</p>
     * 
     * @return The unique instance of {@link PortalMenuService}.
     * 
     * @deprecated Use {@code @Inject} to obtain the {@link PortalMenuService} 
     * instance. This method will be removed in future versions.
     */
    @Deprecated( since = "8.0", forRemoval = true )
    public static PortalMenuService getInstance( )
    {
        return CDI.current().select(PortalMenuService.class).get();
    }

    /**
     * Returns the service name
     * 
     * @return The service name
     */
    public String getName( )
    {
        return SERVICE_NAME;
    }

    /**
     * Returns the menu bar from the cache or builds it if it not stored in it
     * 
     * @param request
     *            The HTTP request
     * @param nMode
     *            The selected mode
     * @param nPart
     *            The part of the menu to build
     * @param nCurrentPageId
     *            The current page ID
     * @return The list of the menus layed out with the stylesheet correpsonding to the mode
     */
    public String getMenuContent( int nCurrentPageId, int nMode, int nPart, HttpServletRequest request )
    {
        String strKey = getKey( nMode, nPart, request );
        String strMenu = null;
        if( _cachePortalMenu.isCacheEnable() && !_cachePortalMenu.isClosed( )) {
         strMenu =  _cachePortalMenu.get( strKey );
        }
        // Seek for the key in the cache
        if ( strMenu == null )
        {
            // Builds the HTML document
            strMenu = buildMenuContent( nCurrentPageId, nMode, nPart, request );
            // Add it in the cache
            _cachePortalMenu.put(strKey, strMenu);

            return strMenu;
        }

        // The document exist in the cache
        return strMenu;
    }

    /**
     * Builds the menu bar
     *
     * @param nCurrentPageId
     *            The current page ID
     * @param nMode
     *            The selected mode
     * @param nPart
     *            The part of the menu to build
     * @param request
     *            The HttpServletRequest
     * @return The list of the menus layed out with the stylesheet of the mode
     */
    private String buildMenuContent( int nCurrentPageId, int nMode, int nPart, HttpServletRequest request )
    {
        if ( _bUseXslStylesheet )
        {
            return buildMenuContentXsl( nCurrentPageId, nMode, nPart, request );
        }
        else
        {
            return buildMenuContentTemplate( nCurrentPageId, nMode, nPart, request );
        }
    }
    
    /**
     * Builds the menu bar with XSL stylesheet
     *
     * @param nCurrentPageId
     *            The current page ID
     * @param nMode
     *            The selected mode
     * @param nPart
     *            The part of the menu to build
     * @param request
     *            The HttpServletRequest
     * @return The list of the menus layed out with the stylesheet of the mode
     */
    private String buildMenuContentXsl( int nCurrentPageId, int nMode, int nPart, HttpServletRequest request )
    {
        Collection<Page> listPagesMenu = PageHome.getChildPagesMinimalData( PortalService.getRootPageId( ) );

        StringBuffer strXml = new StringBuffer( );
        strXml.append( XmlUtil.getXmlHeader( ) );
        XmlUtil.beginElement( strXml, XmlContent.TAG_MENU_LIST );

        int nMenuIndex = 1;

        for ( Page menuPage : listPagesMenu )
        {
            if ( ( menuPage.isVisible( request ) ) || ( nMode == MODE_ADMIN ) )
            {
                buildPageXml( menuPage, strXml, nMode, nMenuIndex, nCurrentPageId, request );

                nMenuIndex++;
            }
        }

        XmlUtil.endElement( strXml, XmlContent.TAG_MENU_LIST );

        // Added in v1.3
        StyleSheet xslSource = getMenuXslSource( nMode, nPart );

        Properties outputProperties = ModeHome.getOuputXslProperties( nMode );

        // Added in v1.3
        // Add a path param for choose url to use in admin or normal mode
        Map<String, String> mapParamRequest = new HashMap<>( );
        PortalService.setXslPortalPath( mapParamRequest, nMode );

        XmlTransformerService xmlTransformerService = new XmlTransformerService( );

        return xmlTransformerService.transformBySourceWithXslCache( strXml.toString( ), xslSource, mapParamRequest, outputProperties );
    }

    private void buildPageXml( Page menuPage, StringBuffer strXml, int nMode, int nMenuIndex, int nCurrentPageId, HttpServletRequest request )
    {
        XmlUtil.beginElement( strXml, XmlContent.TAG_MENU );
        XmlUtil.addElement( strXml, XmlContent.TAG_MENU_INDEX, nMenuIndex );
        XmlUtil.addElement( strXml, XmlContent.TAG_PAGE_ID, menuPage.getId( ) );
        XmlUtil.addElementHtml( strXml, XmlContent.TAG_PAGE_NAME, menuPage.getName( ) );
        XmlUtil.addElementHtml( strXml, XmlContent.TAG_CURRENT_PAGE_ID, String.valueOf( nCurrentPageId ) );

        Collection<Page> listSubLevelMenuPages = PageHome.getChildPagesMinimalData( menuPage.getId( ) );

        // add element submenu-list only if list not empty
        if ( !listSubLevelMenuPages.isEmpty( ) )
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
                    XmlUtil.addElement( strXml, XmlContent.TAG_PAGE_ID, subLevelMenuPage.getId( ) );
                    XmlUtil.addElementHtml( strXml, XmlContent.TAG_PAGE_NAME, subLevelMenuPage.getName( ) );
                    XmlUtil.endElement( strXml, XmlContent.TAG_SUBLEVEL_MENU );
                    XmlUtil.addElementHtml( strXml, XmlContent.TAG_CURRENT_PAGE_ID, String.valueOf( nCurrentPageId ) );
                }
            }

            XmlUtil.endElement( strXml, XmlContent.TAG_SUBLEVEL_MENU_LIST );
        }

        XmlUtil.endElement( strXml, XmlContent.TAG_MENU );
    }

    private StyleSheet getMenuXslSource( int nMode, int nPart )
    {
        // Use the same stylesheet for normal or admin mode
        StyleSheet xslSource;

        // Selection of the XSL stylesheet
        switch( nMode )
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
            switch( nMode )
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
        return xslSource;
    }

    /**
     * Builds the menu bar with freemarker template
     *
     * @param nCurrentPageId
     *            The current page ID
     * @param nMode
     *            The selected mode
     * @param nPart
     *            The part of the menu to build
     * @param request
     *            The HttpServletRequest
     * @return The list of the menus layed out with the stylesheet of the mode
     */
    private String buildMenuContentTemplate( int nCurrentPageId, int nMode, int nPart, HttpServletRequest request )
    {
        Collection<Page> listPagesMenu = PageHome.getChildPagesMinimalData( PortalService.getRootPageId( ) );

        List<MenuItem> menuItems = new ArrayList<MenuItem>( );

        int nMenuIndex = 1;

        for ( Page menuPage : listPagesMenu )
        {
            if ( ( menuPage.isVisible( request ) ) || ( nMode == MODE_ADMIN ) )
            {
                buildMenuItem( menuPage, menuItems, nMode, nMenuIndex, nCurrentPageId, request );
                nMenuIndex++;
            }
        }

        // Added in v1.3
        // Add a path param for choose url to use in admin or normal mode
        Map<String, String> mapParamRequest = new HashMap<>( );
        PortalService.setXslPortalPath( mapParamRequest, nMode );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_MENU_ITEMS, menuItems );
        model.put( MARK_SITE_PATH, mapParamRequest.get( PARAMETER_SITE_PATH ) );
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MAIN_MENU, null, model );
        return template.getHtml( );
    }
    
    private void buildMenuItem( Page menuPage, List<MenuItem> menuItems, int nMode, int nMenuIndex, int nCurrentPageId, HttpServletRequest request )
    {
        MenuItem menuItem = MenuItem.builder( ).pageId( menuPage.getId( ) ).name( menuPage.getName( ) ).menuIndex( nMenuIndex )
                .currentPageId( nCurrentPageId ).build( );

        Collection<Page> listSubLevelMenuPages = PageHome.getChildPagesMinimalData( menuPage.getId( ) );

        // add element submenu-list only if list not empty
        if ( !listSubLevelMenuPages.isEmpty( ) )
        {
            // Seek of the sub-menus

            int nSubLevelMenuIndex = 1;

            for ( Page subLevelMenuPage : listSubLevelMenuPages )
            {
                if ( ( subLevelMenuPage.isVisible( request ) ) || ( nMode == MODE_ADMIN ) )
                {
                    MenuItem subMenuItem = MenuItem.builder( ).pageId( subLevelMenuPage.getId( ) ).parentId( menuPage.getId( ) )
                            .name( subLevelMenuPage.getName( ) ).menuIndex( nMenuIndex ).subMenuIndex( nSubLevelMenuIndex ).currentPageId( nCurrentPageId )
                            .build( );
                    menuItem.getChildren( ).add( subMenuItem );
                }
            }
        }
        menuItems.add( menuItem );
    }

    /**
     * Returns the key corresponding to the part according to the selected mode
     *
     * @param nMode
     *            The mode
     * @param nPart
     *            the part
     * @param request
     *            The HTTP request
     * @return The key as a String
     */
    private String getKey( int nMode, int nPart, HttpServletRequest request )
    {
        String strRoles = "-";

        if ( SecurityService.isAuthenticationEnable( ) && request != null )
        {
            LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );

            if ( ( user != null ) && ( user.getRoles( ) != null ) )
            {
                String [ ] roles = user.getRoles( );
                Arrays.sort( roles );
                strRoles = StringUtils.join( roles, ',' );
            }
        }

        StringBuilder sbKey = new StringBuilder( );
        sbKey.append( "[menu:" ).append( nPart ).append( "][m:" ).append( nMode ).append( "][roles:" ).append( strRoles ).append( ']' );

        return sbKey.toString( );
    }

    /**
     * Process a page event
     *
     * @param event
     *            The event to process
     */
    public void processPageEvent( @Observes PageEvent event )
    {
    	// page was added, removed or updated; clear cache
    	_cachePortalMenu.resetCache( );
    	if ( event != null && event.getPage( ) != null )
    	{
    	    AppLogService.debug( "Portal menu cache has been reset because of a type {} page event on page {}", ( ) -> event.getEventType( ), ( ) -> event.getPage( ).getName( ) );
    	}
    }
    
    /**
     * This method observes the initialization of the {@link ApplicationScoped} context.
     * It ensures that this CDI beans are instantiated at the application startup.
     *
     * <p>This method is triggered automatically by CDI when the {@link ApplicationScoped} context is initialized,
     * which typically occurs during the startup of the application server.</p>
     *
     * @param context the {@link ServletContext} that is initialized. This parameter is observed
     *                and injected automatically by CDI when the {@link ApplicationScoped} context is initialized.
     */
    public void initializedService(@Observes @Initialized(ApplicationScoped.class) ServletContext context) {
        // This method is intentionally left empty to trigger CDI bean instantiation
    }
}
