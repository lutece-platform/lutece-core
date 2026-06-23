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
package fr.paris.lutece.portal.web.features;

import org.apache.commons.lang3.StringUtils;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

import fr.paris.lutece.portal.business.right.Level;
import fr.paris.lutece.portal.business.right.LevelHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * This class provides the user interface to manage levels features ( create, modify ) through the admin MVC front-controller.
 */
@RequestScoped
@Named
@Controller( name = "levels", right = "CORE_LEVEL_RIGHT_MANAGEMENT" )
public class LevelsJspBean extends MVCAdminJspBean
{
    private static final long serialVersionUID = 5513182604869973362L;

    // Right
    public static final String RIGHT_MANAGE_LEVELS = "CORE_LEVEL_RIGHT_MANAGEMENT";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_CREATE_LEVEL = "portal.features.create_level.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_LEVEL = "portal.features.modify_level.pageTitle";

    // Markers
    private static final String MARK_LEVEL = "level";
    private static final String MARK_LEVEL_ID = "level_id";

    // Templates files path
    private static final String TEMPLATE_CREATE_LEVEL = "admin/features/create_level.html";
    private static final String TEMPLATE_MODIFY_LEVEL = "admin/features/modify_level.html";

    // Views
    private static final String VIEW_CREATE_LEVEL = "createLevel";
    private static final String VIEW_MODIFY_LEVEL = "modifyLevel";

    // Actions
    private static final String ACTION_CREATE_LEVEL = "createLevel";
    private static final String ACTION_MODIFY_LEVEL = "modifyLevel";

    private static final String ANCHOR_RIGHT_LEVELS = "right_levels";

    @Inject
    private Models _models;

    /**
     * Returns the level form of creation.
     *
     * @param request
     *            The Http request
     * @return the html code of the level creation form
     */
    @View( VIEW_CREATE_LEVEL )
    public String getCreateLevel( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_LEVEL );

        _models.put( SecurityTokenService.MARK_TOKEN, getSecurityTokenService( ).getToken( request, TEMPLATE_CREATE_LEVEL ) );
        _models.put( MARK_LEVEL_ID, LevelHome.newPrimaryKey( ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_LEVEL, getLocale( ), _models );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Processes the creation form of a new level by recovering the parameters in the http request.
     *
     * @param request
     *            the http request
     * @return the redirection to the level management dashboard
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Action( ACTION_CREATE_LEVEL )
    public String doCreateLevel( HttpServletRequest request ) throws AccessDeniedException
    {
        String strName = request.getParameter( Parameters.LEVEL_NAME );
        String strId = request.getParameter( Parameters.LEVEL_ID );

        if ( strId.equals( "" ) || strName.equals( "" ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP ) );
        }
        if ( !StringUtils.isNumeric( strId ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, Messages.MESSAGE_INVALID_ENTRY, AdminMessage.TYPE_STOP ) );
        }
        if ( !getSecurityTokenService( ).validate( request, TEMPLATE_CREATE_LEVEL ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }

        Level level = new Level( );
        level.setName( strName );
        level.setId( Integer.parseInt( strId ) );
        LevelHome.create( level );

        return redirect( request, getAdminDashboardsUrl( request, ANCHOR_RIGHT_LEVELS ) );
    }

    /**
     * Returns the level form of update.
     *
     * @param request
     *            The Http request
     * @return the html code of the level modification form
     */
    @View( VIEW_MODIFY_LEVEL )
    public String getModifyLevel( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_LEVEL );

        String strId = request.getParameter( Parameters.LEVEL_ID );

        Level level = LevelHome.findByPrimaryKey( Integer.parseInt( strId ) );

        if ( level == null )
        {
            return redirect( request, getAdminDashboardsUrl( request, ANCHOR_RIGHT_LEVELS ) );
        }

        _models.put( MARK_LEVEL, level );
        _models.put( SecurityTokenService.MARK_TOKEN, getSecurityTokenService( ).getToken( request, TEMPLATE_MODIFY_LEVEL ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_LEVEL, getLocale( ), _models );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Processes the updating form of a level whose new parameters are stored in the http request.
     *
     * @param request
     *            The http request
     * @return the redirection to the level management dashboard
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Action( ACTION_MODIFY_LEVEL )
    public String doModifyLevel( HttpServletRequest request ) throws AccessDeniedException
    {
        String strId = request.getParameter( Parameters.LEVEL_ID );
        String strName = request.getParameter( Parameters.LEVEL_NAME );

        if ( strName.equals( "" ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP ) );
        }
        if ( !getSecurityTokenService( ).validate( request, TEMPLATE_MODIFY_LEVEL ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }

        Level level = LevelHome.findByPrimaryKey( Integer.parseInt( strId ) );
        level.setName( strName );
        LevelHome.update( level );

        return redirect( request, getAdminDashboardsUrl( request, ANCHOR_RIGHT_LEVELS ) );
    }
}
