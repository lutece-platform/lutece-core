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
package fr.paris.lutece.portal.web;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.page.PageHome;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.right.RightHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.dashboard.DashboardComponent;
import fr.paris.lutece.portal.service.page.PageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * SiteDashboardComponent
 */
public class SiteDashboardComponent extends DashboardComponent
{
    // CONSTANTS
    //    private static final int ZONE_1 = 1;
    private static final String BEAN_PAGE_SERVICE = "pageService";

    // MARKS
    private static final String MARK_PAGES_COUNT = "pages_count";
    private static final String MARK_URL = "url";
    private static final String MARK_ICON = "icon";
    private static final String MARK_LAST_MODIFIED_PAGE = "last_modified_page";
    private static final String MARK_LAST_MODIFIED_PORTLET = "last_modified_portlet";
    private static final String MARK_IMAGE_THUMBNAIL_PAGE_URL = "image_thumbnail_page_url";

    // TEMPLATES
    private static final String TEMPLATE_DASHBOARD = "/admin/site/site_dashboard.html";

    /**
     * The HTML code of the component
     * @param user The Admin User
     * @param request HttpServletRequest
     * @return The dashboard component
     */
    @Override
    public String getDashboardData( AdminUser user, HttpServletRequest request )
    {
        Right right = RightHome.findByPrimaryKey( getRight(  ) );
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_URL, right.getUrl(  ) );
        model.put( MARK_ICON, right.getIconUrl(  ) );

        PageService pageService = (PageService) SpringContextService.getBean( BEAN_PAGE_SERVICE );
        Page page = PageHome.getLastModifiedPage(  );
        model.put( MARK_LAST_MODIFIED_PAGE, page );
        model.put( MARK_LAST_MODIFIED_PORTLET, PortletHome.getLastModifiedPortlet(  ) );
        model.put( MARK_PAGES_COUNT, PageHome.getAllPages(  ).size(  ) );

        if ( page != null )
        {
            model.put( MARK_IMAGE_THUMBNAIL_PAGE_URL,
                pageService.getResourceImagePage( String.valueOf( page.getId(  ) ) ) );
        }

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_DASHBOARD, user.getLocale(  ), model );

        return t.getHtml(  );
    }
}
