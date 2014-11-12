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
package fr.paris.lutece.portal.web.style;

import fr.paris.lutece.portal.business.style.Mode;
import fr.paris.lutece.portal.business.style.ModeHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.io.File;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage modes features ( manage,
 * create, modify, remove)
 */
public class ModesJspBean extends AdminFeaturesPageJspBean
{
    // Right
    /**
     * Right to manage modes
     */
    public static final String RIGHT_MANAGE_MODES = "CORE_MODES_MANAGEMENT";

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 8205010720652090863L;

    // Markers
    private static final String MARK_MODES_LIST = "mode_list";
    private static final String MARK_MODE = "mode";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MODE_LIST = "portal.style.manage_mode.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_MODE = "portal.style.create_mode.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_MODE = "portal.style.modify_mode.pageTitle";

    // Templates files path
    private static final String TEMPLATE_MANAGE_MODES = "admin/style/manage_modes.html";
    private static final String TEMPLATE_CREATE_MODE = "admin/style/create_mode.html";
    private static final String TEMPLATE_MODIFY_MODE = "admin/style/modify_mode.html";

    // Properties
    private static final String PROPERTY_PATH_XSL = "path.stylesheet";

    // Jsp definition
    private static final String JSP_MANAGE_MODES = "ManageModes.jsp";

    /**
     * Returns the list of modes
     *
     * @param request The Http request
     * @return the html code for display the modes list
     */
    public String getManageModes( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODE_LIST );

        HashMap<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_MODES_LIST, ModeHome.getModesList(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_MODES, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns the mode form of creation
     *
     * @param request The Http request
     * @return the html code of the mode
     */
    public String getCreateMode( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_MODE );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_MODE, getLocale(  ) );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Processes the creation form of a new mode by recovering the parameters in
     * the http request
     *
     * @param request the http request
     * @return The Jsp URL of the process result
     */
    public String doCreateMode( HttpServletRequest request )
    {
        String strDescription = request.getParameter( Parameters.MODE_DESCRIPTION );
        String strPath = request.getParameter( Parameters.MODE_PATH );

        //Mandatory fields
        if ( strDescription.equals( "" ) || strPath.equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( !strPath.endsWith( "/" ) && !strPath.endsWith( "\\" ) )
        {
            strPath += File.separator;
        }

        File dirPath = new File( AppPathService.getPath( PROPERTY_PATH_XSL ) + strPath );

        if ( dirPath.exists(  ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.PATH_ALREADY_EXISTS, AdminMessage.TYPE_STOP );
        }

        if ( !dirPath.mkdir(  ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.PATH_CREATION_ERROR, AdminMessage.TYPE_ERROR );
        }

        Mode mode = new Mode(  );
        mode.setDescription( strDescription );
        mode.setPath( strPath );

        /* Since v1.2 */
        mode.setOutputXslPropertyMethod( request.getParameter( Parameters.MODE_OUTPUT_XSL_METHOD ) );
        mode.setOutputXslPropertyVersion( request.getParameter( Parameters.MODE_OUTPUT_XSL_VERSION ) );
        mode.setOutputXslPropertyMediaType( request.getParameter( Parameters.MODE_OUTPUT_XSL_MEDIA_TYPE ) );
        mode.setOutputXslPropertyEncoding( request.getParameter( Parameters.MODE_OUTPUT_XSL_ENCODING ) );
        mode.setOutputXslPropertyIndent( request.getParameter( Parameters.MODE_OUTPUT_XSL_INDENT ) );
        mode.setOutputXslPropertyOmitXmlDeclaration( request.getParameter( 
                Parameters.MODE_OUTPUT_XSL_OMIT_XML_DECLARATION ) );
        mode.setOutputXslPropertyStandalone( request.getParameter( Parameters.MODE_OUTPUT_XSL_STANDALONE ) );

        ModeHome.create( mode );

        // If the process is successfull, redirects towards the theme view
        return JSP_MANAGE_MODES;
    }

    /**
     * Returns the mode form of update
     *
     * @param request The Http request
     * @return the html code of the mode form
     */
    public String getModifyMode( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_MODE );

        String strId = request.getParameter( Parameters.MODE_ID );

        HashMap<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_MODE, ModeHome.findByPrimaryKey( Integer.parseInt( strId ) ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_MODE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Processes the updating form of a mode whose new parameters are stored in
     * the http request
     *
     * @param request The http request
     * @return The Jsp URL of the process result
     */
    public String doModifyMode( HttpServletRequest request )
    {
        String strId = request.getParameter( Parameters.MODE_ID );
        String strDescription = request.getParameter( Parameters.MODE_DESCRIPTION );

        if ( strDescription.equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        Mode mode = ModeHome.findByPrimaryKey( Integer.parseInt( strId ) );
        mode.setDescription( strDescription );

        /* Since v1.2 */
        mode.setOutputXslPropertyMethod( request.getParameter( Parameters.MODE_OUTPUT_XSL_METHOD ) );
        mode.setOutputXslPropertyVersion( request.getParameter( Parameters.MODE_OUTPUT_XSL_VERSION ) );
        mode.setOutputXslPropertyMediaType( request.getParameter( Parameters.MODE_OUTPUT_XSL_MEDIA_TYPE ) );
        mode.setOutputXslPropertyEncoding( request.getParameter( Parameters.MODE_OUTPUT_XSL_ENCODING ) );
        mode.setOutputXslPropertyIndent( request.getParameter( Parameters.MODE_OUTPUT_XSL_INDENT ) );
        mode.setOutputXslPropertyOmitXmlDeclaration( request.getParameter( 
                Parameters.MODE_OUTPUT_XSL_OMIT_XML_DECLARATION ) );
        mode.setOutputXslPropertyStandalone( request.getParameter( Parameters.MODE_OUTPUT_XSL_STANDALONE ) );

        ModeHome.update( mode );

        // If the process is successfull, redirects towards the mode management page
        return JSP_MANAGE_MODES;
    }
}
