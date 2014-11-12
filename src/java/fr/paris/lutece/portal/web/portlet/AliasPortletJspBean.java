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

import fr.paris.lutece.portal.business.portlet.AliasPortlet;
import fr.paris.lutece.portal.business.portlet.AliasPortletHome;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage Alias Portlet
 */
public class AliasPortletJspBean extends PortletJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // Right
    public static final String RIGHT_MANAGE_ADMIN_SITE = "CORE_ADMIN_SITE";
    private static final long serialVersionUID = 1894295808070813451L;
    private static final String PARAM_PORTLET_NAME = "portlet_name";
    private static final String PARAM_ORDER = "order";
    private static final String PARAM_COLUMN = "column";
    private static final String PARAM_ALIAS_ID = "alias_id";
    private static final String PARAM_ACCEPT_ALIAS = "accept_alias";
    private static final String MARK_ALIAS_PORTLETS_LIST = "alias_portlets_list";
    private static final String MARK_ALIAS_PORTLET = "alias_portlet";

    /**
     * Process portlet's creation
     *
     * @param request The Http request
     * @return The management url
     */
    @Override
    public String doCreate( HttpServletRequest request )
    {
        AliasPortlet aliasPortlet = new AliasPortlet(  );
        String strAliasId = request.getParameter( PARAM_ALIAS_ID );

        //if no portlet has the accept alias field true
        if ( strAliasId == null )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // Gets the parameters of the alias portlet posted in the request
        String strName = request.getParameter( PARAM_PORTLET_NAME );

        // mandatory field
        if ( ( strName == null ) || strName.trim(  ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        String strOrder = request.getParameter( PARAM_ORDER );
        int nOrder = Integer.parseInt( strOrder );
        String strColumn = request.getParameter( PARAM_COLUMN );
        int nColumn = Integer.parseInt( strColumn );
        String strAcceptAlias = request.getParameter( PARAM_ACCEPT_ALIAS );
        int nAcceptAlias = Integer.parseInt( strAcceptAlias );
        aliasPortlet.setName( strName );
        aliasPortlet.setOrder( nOrder );
        aliasPortlet.setColumn( nColumn );
        aliasPortlet.setAcceptAlias( nAcceptAlias );

        String strPageId = request.getParameter( PARAMETER_PAGE_ID );
        int nPageId = Integer.parseInt( strPageId );
        int nAliasId = Integer.parseInt( strAliasId );
        aliasPortlet.setPageId( nPageId );
        aliasPortlet.setAliasId( nAliasId );

        //gets the style of the parent portlet
        Portlet portlet = PortletHome.findByPrimaryKey( nAliasId );
        aliasPortlet.setStyleId( portlet.getStyleId(  ) );

        // creates the alias portlet
        AliasPortletHome.getInstance(  ).create( aliasPortlet );

        // Displays the page with the new portlet
        return getPageUrl( nPageId );
    }

    /**
     * Process portlet's modification
     *
     * @param request The http request
     * @return Management's Url
     */
    @Override
    public String doModify( HttpServletRequest request )
    {
        // recovers portlet attributes
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        AliasPortlet portlet = (AliasPortlet) AliasPortletHome.findByPrimaryKey( nPortletId );

        // Gets the parameters of the alias portlet posted in the request
        String strName = request.getParameter( PARAM_PORTLET_NAME );
        String strOrder = request.getParameter( PARAM_ORDER );
        int nOrder = Integer.parseInt( strOrder );
        String strColumn = request.getParameter( PARAM_COLUMN );
        int nColumn = Integer.parseInt( strColumn );

        // mandatory field
        if ( ( strName == null ) || strName.trim(  ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        portlet.setName( strName );
        portlet.setOrder( nOrder );
        portlet.setColumn( nColumn );

        String strIdAlias = request.getParameter( PARAM_ALIAS_ID );
        int nIdAlias = Integer.parseInt( strIdAlias );
        portlet.setAliasId( nIdAlias );

        Portlet rub = PortletHome.findByPrimaryKey( nIdAlias );
        portlet.setStyleId( rub.getStyleId(  ) );

        // updates the portlet
        portlet.update(  );

        // Displays the page with the portlet updated
        return getPageUrl( portlet.getPageId(  ) );
    }

    /**
     * Returns the Alias portlet creation form
     *
     * @param request The http request
     * @return The HTML form
     */
    @Override
    public String getCreate( HttpServletRequest request )
    {
        String strIdPage = request.getParameter( PARAMETER_PAGE_ID );
        String strIdPortletType = request.getParameter( PARAMETER_PORTLET_TYPE_ID );
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_ALIAS_PORTLETS_LIST, AliasPortletHome.getAcceptAliasPortletList(  ) );

        HtmlTemplate template = getCreateTemplate( strIdPage, strIdPortletType, model );

        return template.getHtml(  );
    }

    /**
     * Returns the Alias portlet modification form
     *
     * @param request The Http request
     * @return The HTML form
     */
    @Override
    public String getModify( HttpServletRequest request )
    {
        String strIdPortlet = request.getParameter( PARAMETER_PORTLET_ID );
        int idPortlet = Integer.parseInt( strIdPortlet );
        Portlet portlet = PortletHome.findByPrimaryKey( idPortlet );
        AliasPortlet aliasPortlet = (AliasPortlet) portlet;
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_ALIAS_PORTLETS_LIST, AliasPortletHome.getAcceptAliasPortletList(  ) );
        model.put( MARK_ALIAS_PORTLET, aliasPortlet.getAliasId(  ) );

        HtmlTemplate template = getModifyTemplate( portlet, model );

        return template.getHtml(  );
    }
}
