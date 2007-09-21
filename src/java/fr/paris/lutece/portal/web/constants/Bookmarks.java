/*
 * Copyright (c) 2002-2007, Mairie de Paris
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
 * This class provides the Bookmarks interface which contains the common bookmarks
 */
public class Bookmarks
{
    // to retrieve the webapp url when using linkservice
    public static final String WEBAPP_PATH_FOR_LINKSERVICE = "@webapp_path_for_linkservice@";
    
   /* public static final String BASE_URL = "@base_url@";
    public static final String VERSION = "@version@";
    public static final String PAGE_ID = "@page_id@";
    public static final String PARENT_ID = "@page_id_parent@";
    public static final String PAGE_NAME = "@page_name@";
    public static final String PAGE_DESCRIPTION = "@page_description@";
    public static final String PAGE_HEADER = "@page_header@";
    public static final String PAGE_TOOLS_MENU = "@page_tools_menu@";
    public static final String PAGE_INIT_MENU = "@page_init_menu@";
    public static final String PAGE_TREE_MENU = "@page_tree_menu@";
    public static final String PAGE_MAIN_MENU = "@page_main_menu@";
    public static final String PAGE_PATH = "@page_path@";
    public static final String PAGE_CONTENT = "@page_content@";
    public static final String PAGE_TEMPLATES_LIST = "@page_template_list@";
    public static final String PAGE_TEMPLATES_LIST_ROWS = "@page_template_list_rows@";
    public static final String PAGE_FOOTER = "@page_footer@";
    public static final String WEBMASTER_EMAIL = "@web_mail@";
    public static final String PAGE_TEMPLATE_ID = "@page_template_id@";
    public static final String PAGE_TEMPLATE_DESCRIPTION = "@page_template_description@";
    public static final String PAGE_TEMPLATE_FILE = "@page_template_file@";
    public static final String PAGE_TEMPLATE_PICTURE = "@page_template_picture@";
    public static final String PAGE_TEMPLATE_CHECKED = "@checked@";
    public static final String ADMIN_BLOCK = "@admin_block@";
    public static final String PORTLET_TYPES_LIST = "@portlet_type_list@";
    public static final String PORTLET_ID = "@portlet_id@";
    public static final String PORTLET_TYPE_ID = "@portlet_type_id@";
    public static final String PORTLET_TYPE_NAME = "@portlet_type_name@";
    public static final String PORTLET_NAME = "@portlet_name@";
    public static final String PORTLET_PATH = "@portlet_path@";
    public static final String COMBO_PORTLET_TYPES = "@combo_portlet_type@";
    public static final String COMBO_ORDER = "@combo_order@";
    public static final String COMBO_STATUS = "@combo_status@";
    public static final String COMBO_COLUMNS = "@combo_column@";
    public static final String TEMPLATE_CREATE_PORTLET = "@template_create_portlet@";
    public static final String TEMPLATE_MODIFY_PORTLET = "@template_modify_template@";
    public static final String COMBO_STYLE = "@combo_style@";
    public static final String MODE = "@mode@";
    public static final String BUTTONS_ADMIN = "@admin_buttons@";
    public static final String CHECKED_ACCEPT_ALIAS_YES = "@checked_accept_alias_yes@";
    public static final String CHECKED_ACCEPT_ALIAS_NO = "@checked_accept_alias_no@";
    public static final String PORTLETS_LIST = "@portlets_list@";
    public static final String PAGE_INEXISTENT_MESSAGE = "@page_message@";
    public static final String PAGE_ID_OLD = "@page_id_old@";

    // Added in v1.3
    public static final String CHECKED_DISPLAY_PORTLET_TITLE_YES = "@checked_display_portlet_title_yes@";
    public static final String CHECKED_DISPLAY_PORTLET_TITLE_NO = "@checked_display_portlet_title_no@";

    // to retrieve the webapp url when using linkservice
    public static final String WEBAPP_PATH_FOR_LINKSERVICE = "@webapp_path_for_linkservice@";

    // Portlets
    public static final String FORM_PORTLET_SPECIFIC_FORM = "@portlet_specific_form_form@";
    public static final String FORM_PORTLET_SPECIFIC = "@portlet_specific_form@";
    public static final String FORM_PORTLET_SCRIPT = "@portlet_specific_form_script@";
    public static final String FORM_PORTLET_URL_ACTION = "@portlet_form_action_url@";
    public static final String FORM_PORTLET_TITLE = "@portlet_form_title@";
    public static final String IMAGE_MANAGE_PORTLET_TYPE = "@image_type_portlet@";
    public static final String CONFIRM_REMOVE_ALIAS_PORTLET = "@confirm_delete_alias_portlet@";
    public static final String IMAGE_CONTENT_CREATION_TITLE = "@image_title_manage_source@";
    public static final String PORTAL_ID = "@portal_id@";
    public static final String PORTAL_NAME = "@portal_name@";
    public static final String PORTALS_LIST = "@portals_list@";
    public static final String BUTTON_CREATE_ENTITY = "@button_create_entity@";

    // Level
    public static final String RIGHT_DESCRIPTION = "@right_description@";

    //User
    public static final String USERS_LIST = "@users_list@";
    public static final String USER_ID = "@user_id@";
    public static final String USER_LASTNAME = "@user_last_name@";
    public static final String USER_FIRSTNAME = "@user_first_name@";
    public static final String ACCESS_CODE = "@access_code@";
    public static final String PASSWORD = "@password@";
    public static final String IP_ADDRESS = "@ip_address@";
    public static final String EMAIL = "@email@";
    public static final String STATUS = "@status@";
    public static final String PROVIDER = "@provider@";
    public static final String RIGHT_ID = "@right_id@";
    public static final String RIGHT_NAME = "@right_name@";
    public static final String URL_FUNCTION = "@feature_url@";
    public static final String COMBO_PROVIDERS = "@combo_providers@";
    public static final String CHECKBOX_RIGHTS_LIST = "@checkbox_rights_list@";
    public static final String ENABLE = "@enable@";
    public static final String DISABLE = "@disable@";
    public static final String BUTTON_REMOVE_USER = "@button_remove_user@";
    public static final String COMBO_ROLE = "@combo_role@"; /* since v1.1 */
 /*   public static final String COMBO_THEME = "@combo_theme@"; /* since v1.3 */

    //Messages
 /*   public static final String MESSAGE_TEXT = "@text@";
    public static final String MESSAGE_URL = "@url@";

    // Stylesheets
    public static final String STYLESHEET_ID = "@stylesheet_id@";
    public static final String STYLESHEET_NAME = "@stylesheet_name@";
    public static final String STYLESHEET_FILE = "@stylesheet_file@";
    public static final String STYLESHEET_LIST = "@stylesheets_list@";

    //Styles
    public static final String STYLE_ID = "@style_id@";
    public static final String DELETE_MESSAGE_PORTLET = "@message_portlet@";
    public static final String DELETE_MESSAGE_STYLESHEET = "@message_stylesheet@";

    //Modes
    public static final String MODE_ID = "@mode_id@";
    public static final String MODE_DESCRIPTION = "@mode_description@";
    public static final String MODES_LIST = "@modes_list@";
    public static final String COMBO_MODES = "@combo_modes@";
    public static final String MODE_PATH = "@mode_path@";
    public static final String MODE_OUTPUT_XSL_METHOD = "@mode_output_xsl_method@"; /* since v1.2 */
 /*   public static final String MODE_OUTPUT_XSL_VERSION = "@mode_output_xsl_version@"; /* since v1.2 */
/*    public static final String MODE_OUTPUT_XSL_MEDIA_TYPE = "@mode_output_xsl_media_type@"; /* since v1.2 */
  /*  public static final String MODE_OUTPUT_XSL_ENCODING = "@mode_output_xsl_encoding@"; /* since v1.2 */
    /*public static final String MODE_OUTPUT_XSL_INDENT = "@mode_output_xsl_indent@"; /* since v1.2 */
    /*public static final String MODE_OUTPUT_XSL_OMIT_XML_DECLARATION = "@mode_output_xsl_omit_xml_declaration@"; /* since v1.2 */
    /*public static final String MODE_OUTPUT_XSL_STANDALONE = "@mode_output_xsl_standalone@"; /* since v1.2 */

    // Search

    //Levels right
    /*public static final String LEVEL_ID = "@level_id@";
    public static final String LEVEL_NAME = "@level_name@";
    public static final String LEVELS_LIST = "@levels_list@";*/
}
