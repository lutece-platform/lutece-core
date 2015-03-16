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
 * This class provides the Parameters interface which contains the common parameters
 */
public final class Parameters
{
    public static final String PAGE_ID = "page_id";
    public static final String PAGE_NAME = "page_name";
    public static final String PAGE_DESCRIPTION = "page_description";
    public static final String PAGE_TEMPLATE_ID = "page_template_id";
    public static final String PAGE_TEMPLATE_DESCRIPTION = "page_template_description";
    public static final String PAGE_TEMPLATE_FILE = "page_template_file";
    public static final String PAGE_TEMPLATE_PICTURE = "page_template_picture";
    public static final String PARENT_ID = "parent_id";
    public static final String CHILD_PAGE_NAME = "child_page_name";
    public static final String CHILD_PAGE_DESCRIPTION = "child_page_description";
    public static final String PORTLET_TYPE_ID = "portlet_type_id";
    public static final String PORTLET_ID = "portlet_id";
    public static final String PORTLET_NAME = "portlet_name";
    public static final String ACCEPT_ALIAS = "accept_alias";
    public static final String DISPLAY_PORTLET_TITLE = "display_portlet_title";
    public static final String ORDER = "order";
    public static final String STYLE = "style";
    public static final String COLUMN = "column";
    public static final String STYLESHEET_ID = "stylesheet_id";
    public static final String SEARCH_MESSAGE = "search_message";
    public static final String ROLE = "role"; /* since v1.1 */
    public static final String WORKGROUP_KEY = "workgroup_key"; /* since v2.2.0b1 */
    public static final String THEME = "theme"; /* since v1.1 */
    public static final String META_KEYWORDS = "meta_keywords";
    public static final String META_DESCRIPTION = "meta_description";
    public static final String DISPLAY_ON_SMALL_DEVICE = "display_on_small_device";
    public static final String DISPLAY_ON_NORMAL_DEVICE = "display_on_normal_device";
    public static final String DISPLAY_ON_LARGE_DEVICE = "display_on_large_device";
    public static final String DISPLAY_ON_XLARGE_DEVICE = "display_on_xlarge_device";
    public static final String BASE_URL = "base_url"; /* since v5.0.1 */

    // url parameter for redirection after admin
    public static final String REDIRECT_URL = "redirectUrl";

    // Stylesheet
    public static final String STYLESHEET_NAME = "stylesheet_name";
    public static final String STYLESHEET_FILE = "stylesheet_file";
    public static final String STYLESHEET_SOURCE = "stylesheet_source";
    public static final String MODE_STYLESHEET = "mode_stylesheet";
    public static final String STYLESHEET_ERROR = "stylesheet_error";

    // User
    public static final String ACCESS_CODE = "access_code";
    public static final String PASSWORD = "password";
    public static final String PASSWORD_CURRENT = "password_current";
    public static final String NEW_PASSWORD = "new_password";
    public static final String CONFIRM_NEW_PASSWORD = "confirm_new_password";
    public static final String USER_ID = "user_id";
    public static final String USER_LASTNAME = "user_lastname";
    public static final String USER_FIRSTNAME = "user_firstname";
    public static final String IP_ADDRESS = "ip_address";
    public static final String EMAIL = "email";
    public static final String PROVIDER = "providers";
    public static final String STATUS = "status";

    // Rights
    public static final String RIGHT_LEVEL_0_1_2 = "0";
    public static final String RIGHT_LEVEL_2_3 = "2";
    public static final String RIGHT_LEVEL_3 = "3";
    public static final String RIGHTS = "rights";
    public static final String LEVEL_ID = "level_id";
    public static final String LEVEL_NAME = "level_name";

    //Styles - Portlets
    public static final String PORTLET_TYPE = "portlet_type";
    public static final String STYLE_ID = "style_id";
    public static final String STYLE_NAME = "style_name";
    public static final String STYLES = "styles";
    public static final String PORTAL_COMPONENT = "portal_component";

    //Modes
    public static final String MODE_ID = "mode_id";
    public static final String MODE_DESCRIPTION = "mode_description";
    public static final String MODE_PATH = "mode_path";
    public static final String MODE_OUTPUT_XSL_METHOD = "mode_output_xsl_method"; /* Since v1.2 */
    public static final String MODE_OUTPUT_XSL_VERSION = "mode_output_xsl_version"; /* Since v1.2 */
    public static final String MODE_OUTPUT_XSL_MEDIA_TYPE = "mode_output_xsl_media_type"; /* Since v1.2 */
    public static final String MODE_OUTPUT_XSL_ENCODING = "mode_output_xsl_encoding"; /* Since v1.2 */
    public static final String MODE_OUTPUT_XSL_INDENT = "mode_output_xsl_indent"; /* Since v1.2 */
    public static final String MODE_OUTPUT_XSL_OMIT_XML_DECLARATION = "mode_output_xsl_omit_xml_declaration"; /* Since v1.2 */
    public static final String MODE_OUTPUT_XSL_STANDALONE = "mode_output_xsl_standalone"; /* Since v1.2 */

    //Image Servlet
    public static final String IMAGE_SERVLET = "image";
    public static final String RESOURCE_ID = "id";
    public static final String RESOURCE_TYPE = "resource_type";

    //Sorted list
    public static final String SORTED_ATTRIBUTE_NAME = "sorted_attribute_name";
    public static final String SORTED_ASC = "asc_sort";

    /** Private constructor */
    private Parameters(  )
    {
    }
}
