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
package fr.paris.lutece.portal.web.portlet;

import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.business.portlet.PortletType;
import fr.paris.lutece.portal.business.portlet.PortletTypeHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
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
 * This class represents user interface Portlet. It is the base class of all user interface portlets. It is abstract
 * and the implementation of the interface PortletJspBeanInterface is compulsary.
 */
public abstract class PortletJspBean extends AdminFeaturesPageJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants
    public static final String RIGHT_MANAGE_ADMIN_SITE = "CORE_ADMIN_SITE";
    protected static final String PARAMETER_PAGE_ID = "page_id";
    protected static final String PARAMETER_PORTLET_ID = "portlet_id";
    protected static final String PARAMETER_PORTLET_TYPE_ID = "portlet_type_id";
    private static final String JSP_ADMIN_SITE = "../../site/AdminSite.jsp";

    // Markers
    private final static String MARK_PORTLET = "portlet";
    private final static String MARK_PORTLET_TYPE = "portletType";
    private final static String MARK_PORTLET_PAGE_ID = "portlet_page_id";
    private final static String MARK_PORTLET_ORDER_COMBO = "portlet_order_combo";
    private final static String MARK_PORTLET_COLUMNS_COMBO = "portlet_columns_combo";
    private final static String MARK_PORTLET_STYLES_COMBO = "portlet_style_combo";

    // Templates
    private static final String TEMPLATE_CREATE_PORTLET = "admin/portlet/create_portlet.html";
    private static final String TEMPLATE_MODIFY_PORTLET = "admin/portlet/modify_portlet.html";
    private static final String PROPERTY_LIST_ORDER_MAX = "list.order.max";
    private static final String PROPERTY_COLUMN_NUM_MAX = "nb.columns";

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
     * Returns the list of the columns of the page where the portlet can be positioned
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

        strName = strName.replaceAll( "\"", "" );

        // Check Mandatory fields
        if ( strName.equals( "" ) || ( strStyleId == null ) || ( strStyleId.trim(  ).equals( "" ) ) ||
                strOrder.equals( "" ) || strColumn.equals( "" ) || strStyleId.equals( "" ) ||
                strAcceptAlias.equals( "" ) || strAcceptPortletTitle.equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        int nOrder = Integer.parseInt( strOrder );
        int nColumn = Integer.parseInt( strColumn );
        int nStyleId = Integer.parseInt( strStyleId );
        int nAcceptAlias = Integer.parseInt( strAcceptAlias );
        int nAcceptPortletTitle = Integer.parseInt( strAcceptPortletTitle );

        portlet.setName( strName );
        portlet.setOrder( nOrder );
        portlet.setColumn( nColumn );
        portlet.setStyleId( nStyleId );
        portlet.setAcceptAlias( nAcceptAlias );
        portlet.setDisplayPortletTitle( nAcceptPortletTitle );
        portlet.setPortletTypeId( strPortletTypeId );

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
        return getCreateTemplate( strPageId, strPortletTypeId, new HashMap(  ) );
    }

    /**
     * Fills templates with common values shared by portlet creation form
     *
     * @param strPageId the page identifier
     * @param strPortletTypeId the Portlet type identifier
     * @param model Specific data stored in a hashtable
     * @return the template filled
     */
    protected HtmlTemplate getCreateTemplate( String strPageId, String strPortletTypeId, Map model )
    {
        PortletType portletType = PortletTypeHome.findByPrimaryKey( strPortletTypeId );
        Locale locale = getLocale(  );
        portletType.setLocale( locale );
        
        model.put( MARK_PORTLET_TYPE, portletType );
        model.put( MARK_PORTLET_PAGE_ID, strPageId );
        model.put( MARK_PORTLET_ORDER_COMBO, getOrdersList(  ) );
        model.put( MARK_PORTLET_COLUMNS_COMBO, getColumnsList(  ) );
        model.put( MARK_PORTLET_STYLES_COMBO, PortletHome.getStylesList( strPortletTypeId ) );

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
        return getModifyTemplate( portlet, new HashMap(  ) );
    }

    /**
     * Fills update template with portlet values
     *
     * @param portlet the object to update
     * @return the update template filled
     */
    protected HtmlTemplate getModifyTemplate( Portlet portlet, Map model )
    {
        PortletType portletType = PortletTypeHome.findByPrimaryKey( portlet.getPortletTypeId(  ) );

        model.put( MARK_PORTLET_TYPE, portletType );
        model.put( MARK_PORTLET, portlet );
        model.put( MARK_PORTLET_ORDER_COMBO, getOrdersList(  ) );
        model.put( MARK_PORTLET_COLUMNS_COMBO, getColumnsList(  ) );
        model.put( MARK_PORTLET_STYLES_COMBO, PortletHome.getStylesList( portlet.getPortletTypeId(  ) ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_PORTLET, getLocale(  ), model );

        return template;
    }

    protected String getPageUrl( int nIdPage )
    {
        return JSP_ADMIN_SITE + "?" + PARAMETER_PAGE_ID + "=" + nIdPage;
    }
}
