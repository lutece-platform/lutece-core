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
package fr.paris.lutece.portal.web.features;

import fr.paris.lutece.portal.business.right.Level;
import fr.paris.lutece.portal.business.right.LevelHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage levels features ( manage,
 * create, modify )
 */
public class LevelsJspBean extends AdminFeaturesPageJspBean
{
    // Right 
    public static final String RIGHT_MANAGE_LEVELS = "CORE_LEVEL_RIGHT_MANAGEMENT";

    // Properties for page titles                             
    private static final String PROPERTY_PAGE_TITLE_LEVEL_LIST = "portal.features.manage_levels.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_LEVEL = "portal.features.create_level.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_LEVEL = "portal.features.modify_level.pageTitle";

    // Markers            
    private static final String MARK_LEVELS_LIST = "levels_list";
    private static final String MARK_LEVEL = "level";

    // Templates files path    
    private static final String TEMPLATE_MANAGE_LEVELS = "admin/features/manage_levels.html";
    private static final String TEMPLATE_CREATE_LEVEL = "admin/features/create_level.html";
    private static final String TEMPLATE_MODIFY_LEVEL = "admin/features/modify_level.html";

    // Jsp definition
    private static final String JSP_MANAGE_LEVEL = "ManageLevels.jsp";

    /**
     * Returns the list of levels
     *
     * @param request The Http request
     * @return the html code for display the levels list
     */
    public String getManageLevels( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_LEVEL_LIST );

        HashMap<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_LEVELS_LIST, LevelHome.getLevelsList(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_LEVELS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns the level form of creation
     *
     * @param request The Http request
     * @return the html code of the level
     */
    public String getCreateLevel( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_LEVEL );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_LEVEL, getLocale(  ) );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Processes the creation form of a new level by recovering the parameters
     * in the http request
     *
     * @param request the http request
     * @return The Jsp URL of the process result
     */
    public String doCreateLevel( HttpServletRequest request )
    {
        String strName = request.getParameter( Parameters.LEVEL_NAME );

        //Mandatory fields
        if ( strName.equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        Level level = new Level(  );
        level.setName( strName );
        LevelHome.create( level );

        // If the process is successfull, redirects towards the theme view
        return JSP_MANAGE_LEVEL;
    }

    /**
     * Returns the level form of update
     *
     * @param request The Http request
     * @return the html code of the level form
     */
    public String getModifyLevel( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_LEVEL );

        String strId = request.getParameter( Parameters.LEVEL_ID );

        Level level = LevelHome.findByPrimaryKey( Integer.parseInt( strId ) );

        if ( level == null )
        {
            return getManageLevels( request );
        }

        HashMap<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_LEVEL, level );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_LEVEL, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Processes the updating form of a level whose new parameters are stored in
     * the http request
     *
     * @param request The http request
     * @return The Jsp URL of the process result
     */
    public String doModifyLevel( HttpServletRequest request )
    {
        String strId = request.getParameter( Parameters.LEVEL_ID );
        String strName = request.getParameter( Parameters.LEVEL_NAME );

        //Mandatory fields
        if ( strName.equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        Level level = LevelHome.findByPrimaryKey( Integer.parseInt( strId ) );
        level.setName( strName );
        LevelHome.update( level );

        // If the process is successfull, redirects towards the level management page
        return JSP_MANAGE_LEVEL;
    }
}
