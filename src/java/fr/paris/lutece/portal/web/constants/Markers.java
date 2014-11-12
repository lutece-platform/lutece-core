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
package fr.paris.lutece.portal.web.constants;


/**
 * This class provides the Markers interface which contains the common Freemarker markers
 */
public final class Markers
{
    public static final String BASE_URL = "base_url";
    public static final String VERSION = "version";
    public static final String ENCODING = "encoding";
    public static final String PAGE_PATH = "page_path";
    public static final String PAGE_HEADER = "page_header";
    public static final String PAGE_NAME = "page_name";
    public static final String PAGE_MAIN_MENU = "page_main_menu";
    public static final String PAGE_CONTENT = "page_content";
    public static final String PAGE_INIT_MENU = "page_init_menu";
    public static final String PAGE_TOOLS_MENU = "page_tools_menu";
    public static final String PAGE_TREE_MENU = "page_tree_menu";
    public static final String PAGE_FOOTER = "page_footer";
    public static final String WEBMASTER_EMAIL = "web_mail";
    public static final String PAGE_TEMPLATE_CHECKED = "checked";
    public static final String PAGE_ID = "page_id";
    public static final String PAGE_TITLE = "page_title";

    // to retrieve the webapp url when using linkservice
    public static final String WEBAPP_PATH_FOR_LINKSERVICE = "webapp_path_for_linkservice";

    /** Private constructor */
    private Markers(  )
    {
    }
}
