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
package fr.paris.lutece.portal.web.portlet;

import fr.paris.lutece.portal.business.page.PageHome;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.business.portlet.PortletType;
import fr.paris.lutece.portal.business.portlet.PortletTypeHome;
import fr.paris.lutece.portal.business.role.RoleHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This class represents user interface Portlet. It is the base class of all
 * user interface portlets. It is abstract
 * and the implementation of the interface PortletJspBeanInterface is
 * compulsary.
 */
public abstract class PortletJspBean extends AdminFeaturesPageJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants
    public static final String RIGHT_MANAGE_ADMIN_SITE = "CORE_ADMIN_SITE";

    // Parameters
    protected static final String PARAMETER_PAGE_ID = "page_id";
    protected static final String PARAMETER_PORTLET_ID = "portlet_id";
    protected static final String PARAMETER_PORTLET_TYPE_ID = "portlet_type_id";
    private static final long serialVersionUID = -3546292252642160812L;

    // Markers
    private static final String MARK_PORTLET = "portlet";
    private static final String MARK_PORTLET_TYPE = "portletType";
    private static final String MARK_PORTLET_PAGE_ID = "portlet_page_id";
    private static final String MARK_PORTLET_ORDER_COMBO = "portlet_order_combo";
    private static final String MARK_PORTLET_COLUMNS_COMBO = "portlet_columns_combo";
    private static final String MARK_PORTLET_STYLES_COMBO = "portlet_style_combo";
    private static final String MARK_PORTLET_ROLES_COMBO = "portlet_role_combo";
    private static final String MARK_SMALL_CHECKED = "small_checked";
    private static final String MARK_NORMAL_CHECKED = "normal_checked";
    private static final String MARK_LARGE_CHECKED = "large_checked";
    private static final String MARK_XLARGE_CHECKED = "xlarge_checked";
    private static final String VALUE_CHECKED = "checked=\"checked\"";
    private static final String VALUE_UNCHECKED = "";

    // Templates
    private static final String TEMPLATE_CREATE_PORTLET = "admin/portlet/create_portlet.html";
    private static final String TEMPLATE_MODIFY_PORTLET = "admin/portlet/modify_portlet.html";

    // Properties
    private static final String PROPERTY_LIST_ORDER_MAX = "list.order.max";
    private static final String PROPERTY_COLUMN_NUM_MAX = "nb.columns";

    // Messages
    private static final String MESSAGE_INVALID_PAGE_ID = "portal.site.message.pageIdInvalid";

    // Jsp
    private static final String JSP_ADMIN_SITE = "../../site/AdminSite.jsp";

    /**
     * Displays the portlet's creation form
     *
     * @param request The http request
     * @return The html code for displaying the creation form
     */
    public abstract String getCreate( HttpServletRequest request );

    /**
     * Processes portlet's creation
     *
     * @param request The http request
     * @return The Management jsp url
     */
    public abstract String doCreate( HttpServletRequest request );

    /**
     * Displays the portlet's modification form
     *
     * @param request The http request
     * @return The html code for displaying the modification form
     */
    public abstract String getModify( HttpServletRequest request );

    /**
     * Processes portlet's modification
     *
     * @param request The http request
     * @return The Management jsp url
     */
    public abstract String doModify( HttpServletRequest request );

    ////////////////////////////////////////////////////////////////////////////
    // Management of the combos common to all portlets
    // Order combo

    /**
     * Returns a list of orders
     *
     * @return the list of orders in form of ReferenceList
     */
    private ReferenceList getOrdersList(  )
    {
        ReferenceList list = new ReferenceList(  );
        int nOrderMax = AppPropertiesService.getPropertyInt( PROPERTY_LIST_ORDER_MAX, 15 );

        for ( int i = 1; i <= nOrderMax; i++ )
        {
            list.addItem( i, String.valueOf( i ) );
        }

        return list;
    }

    /**
     * Returns the list of the columns of the page where the portlet can be
     * positioned
     *
     * @return the list of the page columns in form of ReferenceList
     */
    private ReferenceList getColumnsList(  )
    {
        ReferenceList list = new ReferenceList(  );
        int nColumnNumMax = AppPropertiesService.getPropertyInt( PROPERTY_COLUMN_NUM_MAX, 1 );

        for ( int i = 1; i <= nColumnNumMax; i++ )
        {
            list.addItem( i, String.valueOf( i ) );
        }

        return list;
    }

    /**
     * Recovers the common attributes of the portlet
     *
     * @param request the http request
     * @param portlet The instance of the portlet
     * @return An error Key if error, otherwise null.
     */
    protected String setPortletCommonData( HttpServletRequest request, Portlet portlet )
    {
        String strErrorKey = null;

        // get portlet attributes
        String strName = request.getParameter( Parameters.PORTLET_NAME );
        String strStyleId = request.getParameter( Parameters.STYLE );
        String strColumn = request.getParameter( Parameters.COLUMN );
        String strOrder = request.getParameter( Parameters.ORDER );
        String strAcceptAlias = request.getParameter( Parameters.ACCEPT_ALIAS );
        String strAcceptPortletTitle = request.getParameter( Parameters.DISPLAY_PORTLET_TITLE );
        String strPortletTypeId = request.getParameter( Parameters.PORTLET_TYPE_ID );
        String strRole = request.getParameter( Parameters.ROLE );
        String strDisplaySmall = request.getParameter( Parameters.DISPLAY_ON_SMALL_DEVICE );
        String strDisplayNormal = request.getParameter( Parameters.DISPLAY_ON_NORMAL_DEVICE );
        String strDisplayLarge = request.getParameter( Parameters.DISPLAY_ON_LARGE_DEVICE );
        String strDisplayXLarge = request.getParameter( Parameters.DISPLAY_ON_XLARGE_DEVICE );

        strName = strName.replaceAll( "\"", "" );

        // Check Mandatory fields
        if ( strName.equals( "" ) || strOrder.equals( "" ) || strColumn.equals( "" ) || strAcceptAlias.equals( "" ) ||
                strAcceptPortletTitle.equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // style id is not mandatory if the content is not generated from XML and XSL 
        if ( portlet.isContentGeneratedByXmlAndXsl(  ) )
        {
            if ( ( strStyleId == null ) || strStyleId.trim(  ).equals( "" ) )
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }
        }

        String strPageId = request.getParameter( PARAMETER_PAGE_ID );
        int nPageId;

        // Test format of the id and the existence of the page
        try
        {
            nPageId = Integer.parseInt( strPageId );

            if ( !PageHome.checkPageExist( nPageId ) )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_INVALID_PAGE_ID, AdminMessage.TYPE_STOP );
            }
        }
        catch ( NumberFormatException e )
        {
            AppLogService.error( e.getMessage(  ), e );

            return AdminMessageService.getMessageUrl( request, MESSAGE_INVALID_PAGE_ID, AdminMessage.TYPE_STOP );
        }

        int nOrder = Integer.parseInt( strOrder );
        int nColumn = Integer.parseInt( strColumn );
        int nAcceptAlias = Integer.parseInt( strAcceptAlias );
        int nAcceptPortletTitle = Integer.parseInt( strAcceptPortletTitle );

        int nStyleId = portlet.isContentGeneratedByXmlAndXsl(  ) ? Integer.parseInt( strStyleId ) : 0;

        int nDeviceDisplayFlags = 0;

        if ( strDisplaySmall != null )
        {
            nDeviceDisplayFlags |= Portlet.FLAG_DISPLAY_ON_SMALL_DEVICE;
        }

        if ( strDisplayNormal != null )
        {
            nDeviceDisplayFlags |= Portlet.FLAG_DISPLAY_ON_NORMAL_DEVICE;
        }

        if ( strDisplayLarge != null )
        {
            nDeviceDisplayFlags |= Portlet.FLAG_DISPLAY_ON_LARGE_DEVICE;
        }

        if ( strDisplayXLarge != null )
        {
            nDeviceDisplayFlags |= Portlet.FLAG_DISPLAY_ON_XLARGE_DEVICE;
        }

        portlet.setName( strName );
        portlet.setOrder( nOrder );
        portlet.setColumn( nColumn );
        portlet.setStyleId( nStyleId );
        portlet.setPageId( nPageId );
        portlet.setAcceptAlias( nAcceptAlias );
        portlet.setDisplayPortletTitle( nAcceptPortletTitle );
        portlet.setPortletTypeId( strPortletTypeId );
        portlet.setRole( strRole );
        portlet.setDeviceDisplayFlags( nDeviceDisplayFlags );

        return strErrorKey;
    }

    /**
     * Fills templates with common values shared by portlet creation form
     *
     * @param strPageId the page identifier
     * @param strPortletTypeId the Portlet type identifier
     * @return the template filled
     */
    protected HtmlTemplate getCreateTemplate( String strPageId, String strPortletTypeId )
    {
        return getCreateTemplate( strPageId, strPortletTypeId, new HashMap<String, Object>(  ) );
    }

    /**
     * Fills templates with common values shared by portlet creation form
     *
     * @param strPageId the page identifier
     * @param strPortletTypeId the Portlet type identifier
     * @param model Specific data stored in a hashtable
     * @return the template filled
     */
    protected HtmlTemplate getCreateTemplate( String strPageId, String strPortletTypeId, Map<String, Object> model )
    {
        PortletType portletType = PortletTypeHome.findByPrimaryKey( strPortletTypeId );
        Locale locale = getLocale(  );
        portletType.setLocale( locale );

        model.put( MARK_PORTLET_TYPE, portletType );
        model.put( MARK_PORTLET_PAGE_ID, strPageId );
        model.put( MARK_PORTLET_ORDER_COMBO, getOrdersList(  ) );
        model.put( MARK_PORTLET_COLUMNS_COMBO, getColumnsList(  ) );
        model.put( MARK_PORTLET_STYLES_COMBO, PortletHome.getStylesList( strPortletTypeId ) );
        model.put( MARK_PORTLET_ROLES_COMBO, RoleHome.getRolesList( getUser(  ) ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_PORTLET, locale, model );

        return template;
    }

    /**
     * Fills update template with portlet values
     *
     * @param portlet the object to update
     * @return the update template filled
     */
    protected HtmlTemplate getModifyTemplate( Portlet portlet )
    {
        return getModifyTemplate( portlet, new HashMap<String, Object>(  ) );
    }

    /**
     * Fills update template with portlet values
     *
     * @param portlet the object to update
     * @param model The Data model
     * @return the update template filled
     */
    protected HtmlTemplate getModifyTemplate( Portlet portlet, Map<String, Object> model )
    {
        PortletType portletType = PortletTypeHome.findByPrimaryKey( portlet.getPortletTypeId(  ) );
        portletType.setLocale( getLocale(  ) );
        model.put( MARK_PORTLET_TYPE, portletType );
        model.put( MARK_PORTLET, portlet );
        model.put( MARK_PORTLET_ORDER_COMBO, getOrdersList(  ) );
        model.put( MARK_PORTLET_COLUMNS_COMBO, getColumnsList(  ) );
        model.put( MARK_PORTLET_STYLES_COMBO, PortletHome.getStylesList( portlet.getPortletTypeId(  ) ) );
        model.put( MARK_PORTLET_ROLES_COMBO, RoleHome.getRolesList( getUser(  ) ) );
        putCheckBox( model, MARK_SMALL_CHECKED, portlet.hasDeviceDisplayFlag( Portlet.FLAG_DISPLAY_ON_SMALL_DEVICE ) );
        putCheckBox( model, MARK_NORMAL_CHECKED, portlet.hasDeviceDisplayFlag( Portlet.FLAG_DISPLAY_ON_NORMAL_DEVICE ) );
        putCheckBox( model, MARK_LARGE_CHECKED, portlet.hasDeviceDisplayFlag( Portlet.FLAG_DISPLAY_ON_LARGE_DEVICE ) );
        putCheckBox( model, MARK_XLARGE_CHECKED, portlet.hasDeviceDisplayFlag( Portlet.FLAG_DISPLAY_ON_XLARGE_DEVICE ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_PORTLET, getLocale(  ), model );

        return template;
    }

    /**
     * Put check box.
     *
     * @param model the model
     * @param strMarkerChecked the str marker checked
     * @param bChecked the b checked
     */
    protected void putCheckBox( Map<String, Object> model, String strMarkerChecked, boolean bChecked )
    {
        String strChecked = ( bChecked ) ? VALUE_CHECKED : VALUE_UNCHECKED;
        model.put( strMarkerChecked, strChecked );
    }

    /**
     * Gets the page URL
     * @param nIdPage Page ID
     * @return The page URL
     */
    protected String getPageUrl( int nIdPage )
    {
        return JSP_ADMIN_SITE + "?" + PARAMETER_PAGE_ID + "=" + nIdPage;
    }
}
