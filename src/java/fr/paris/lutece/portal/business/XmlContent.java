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
package fr.paris.lutece.portal.business;

import fr.paris.lutece.portal.service.message.SiteMessageException;

import javax.servlet.http.HttpServletRequest;


/**
 * This class represents the interface XmlContent which contains the common
 * Xml tags
 */
public interface XmlContent
{
    //Plugins
    String TAG_PLUGIN_NAME = "plugin-name";

    //Mode
    String TAG_MODE = "mode";

    // Menus
    String TAG_MENU_LIST = "menu-list";
    String TAG_MENU = "menu";
    String TAG_SUBLEVEL_MENU_LIST = "sublevel-menu-list";
    String TAG_SUBLEVEL_MENU = "sublevel-menu";
    String TAG_MENU_INDEX = "menu-index";
    String TAG_SUBLEVEL_INDEX = "sublevel-menu-index";

    // Portlets
    String TAG_PORTLET = "portlet";
    String TAG_PORTLET_ID = "portlet-id";
    String TAG_PORTLET_NAME = "portlet-name";
    String TAG_DISPLAY_PORTLET_TITLE = "display-portlet-title";
    String TAG_DISPLAY_ON_SMALL_DEVICE = "display-on-small-device";
    String TAG_DISPLAY_ON_NORMAL_DEVICE = "display-on-normal-device";
    String TAG_DISPLAY_ON_LARGE_DEVICE = "display-on-large-device";
    String TAG_DISPLAY_ON_XLARGE_DEVICE = "display-on-xlarge-device";

    // Pages
    String TAG_PAGE = "page";
    String TAG_PAGE_ID = "page-id";
    String TAG_PAGE_NAME = "page-name";
    String TAG_PAGE_DESCRIPTION = "page-description";
    String TAG_PAGE_IMAGE = "page-image";
    String TAG_PAGE_LEVEL = "page-level";
    String TAG_CHILD_PAGES_LIST = "child-pages-list";
    String TAG_CURRENT_PAGE_ID = "current-page-id";
    String TAG_PARENT_PAGE_ID = "parent-page-id";

    // Methods which must be implemented by the childs pages

    /**
     * This method should return the body of an Xml document providing the content
     * @param request The HTTP Servlet Request
     * @return string The String
     * @throws SiteMessageException occurs when a site message need to be displayed
     */
    String getXml( HttpServletRequest request ) throws SiteMessageException;

    /**
     * This method should return the whole Xml document providing the content (including the header)
     * @param request The HTTP Servlet Request
     * @return string The String
     * @throws SiteMessageException occurs when a site message need to be displayed
     */
    String getXmlDocument( HttpServletRequest request )
        throws SiteMessageException;
}
