/*
 * Copyright (c) 2002-2021, City of Paris
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.portal.business.style.PageTemplate;
import fr.paris.lutece.portal.business.style.PageTemplateHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.fileupload.FileUploadService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.file.FileUtil;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * This class provides the user interface to manage page templates features ( manage, create, modify, remove)
 */
public class PageTemplatesJspBean extends AdminFeaturesPageJspBean
{
    // Right
    /**
     * Right to manage page templates
     */
    public static final String RIGHT_MANAGE_PAGE_TEMPLATES = "CORE_PAGE_TEMPLATE_MANAGEMENT";

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -142214702397662732L;

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_PAGE_TEMPLATE_LIST = "portal.style.manage_page_templates.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_PAGE_TEMPLATE = "portal.style.create_page_template.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_PAGE_TEMPLATE = "portal.style.modify_page_template.pageTitle";

    // Markers
    private static final String MARK_PAGE_TEMPLATES_LIST = "page_templates_list";
    private static final String MARK_PAGE_TEMPLATE = "page_template";

    // Templates files path
    private static final String TEMPLATE_PAGE_TEMPLATES = "admin/style/manage_page_templates.html";
    private static final String TEMPLATE_CREATE_PAGE_TEMPLATE = "admin/style/create_page_template.html";
    private static final String TEMPLATE_MODIFY_PAGE_TEMPLATE = "admin/style/modify_page_template.html";

    // Properties
    private static final String PROPERTY_PATH_TEMPLATE = "path.templates";
    private static final String PROPERTY_PATH_FILE_PAGE_TEMPLATE = "path.file.page.template";
    private static final String PROPERTY_PATH_IMAGE_PAGE_TEMPLATE = "path.image.page.template";

    // Messages
    private static final String MESSAGE_CONFIRM_DELETE_PAGE_TEMPLATE = "portal.style.message.pageTemplateConfirmDelete";
    private static final String MESSAGE_PAGE_TEMPLATE_IS_USED = "portal.style.message.pageTemplateIsUsed";
    private static final String MESSAGE_WRONG_IMAGE_EXTENSION = "portal.util.message.wrongImageExtention";
    private static final String MESSAGE_WRONG_HTML_EXTENSION = "portal.util.message.wrongHtmlExtention";

    // Parameters
    private static final String PARAMETER_PAGE_TEMPLATE_FILE = "page_template_file";
    private static final String PARAMETER_PAGE_TEMPLATE_PICTURE = "page_template_picture";
    private static final String PARAMETER_PAGE_TEMPLATE_UPDATE_IMAGE = "update_image";
    private static final String PARAMETER_PAGE_TEMPLATE_UPDATE_FILE = "update_file";
    private static final String PATH_IMAGE_PAGE_TEMPLATE = AppPathService.getPath( PROPERTY_PATH_IMAGE_PAGE_TEMPLATE ) + File.separator;
    private static final String PATH_TEMPLATE = AppPathService.getPath( PROPERTY_PATH_TEMPLATE ) + File.separator
            + AppPropertiesService.getProperty( PROPERTY_PATH_FILE_PAGE_TEMPLATE );

    // JSP
    private static final String JSP_DO_REMOVE_PAGE_TEMPLATE = "jsp/admin/style/DoRemovePageTemplate.jsp";

    /**
     * Returns the list of page templates
     *
     * @param request
     *            The Http request
     * @return the html code for display the page templates list
     */
    public String getManagePageTemplate( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_PAGE_TEMPLATE_LIST );

        HashMap<String, Object> model = new HashMap<>( );
        model.put( MARK_PAGE_TEMPLATES_LIST, PageTemplateHome.getPageTemplatesList( ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PAGE_TEMPLATES, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Returns the page template form of creation
     *
     * @param request
     *            The Http request
     * @return the html code of the page template
     */
    public String getCreatePageTemplate( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_PAGE_TEMPLATE );

        HashMap<String, Object> model = new HashMap<>( );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_CREATE_PAGE_TEMPLATE ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_PAGE_TEMPLATE, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Processes the creation form of a new page template by recovering the parameters in the http request
     *
     * @param request
     *            the http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doCreatePageTemplate( HttpServletRequest request ) throws AccessDeniedException
    {
        PageTemplate pageTemplate = new PageTemplate( );

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        String strDescription = multipartRequest.getParameter( Parameters.PAGE_TEMPLATE_DESCRIPTION );

        // Mandatory fields
        if ( StringUtils.isEmpty( strDescription ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        FileItem fileTemplate = multipartRequest.getFile( PARAMETER_PAGE_TEMPLATE_FILE );
        FileItem filePicture = multipartRequest.getFile( PARAMETER_PAGE_TEMPLATE_PICTURE );

        String strFileName = FileUploadService.getFileNameOnly( fileTemplate );
        String strPictureName = FileUploadService.getFileNameOnly( filePicture );

        if ( StringUtils.isEmpty( strFileName ) || StringUtils.isEmpty( strPictureName ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FILE, AdminMessage.TYPE_STOP );
        }

        if ( !( FileUtil.hasHtmlExtension( strFileName ) || FileUtil.hasFreemarkerExtension( strFileName ) ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_WRONG_HTML_EXTENSION, AdminMessage.TYPE_STOP );
        }

        if ( !FileUtil.hasImageExtension( strPictureName ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_WRONG_IMAGE_EXTENSION, AdminMessage.TYPE_STOP );
        }

        if ( !SecurityTokenService.getInstance( ).validate( multipartRequest, TEMPLATE_CREATE_PAGE_TEMPLATE ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }

        pageTemplate.setFile( AppPropertiesService.getProperty( PROPERTY_PATH_FILE_PAGE_TEMPLATE ) + strFileName );
        writeTemplateFile( strFileName, PATH_TEMPLATE, fileTemplate );

        pageTemplate.setPicture( strPictureName );

        writeTemplateFile( strPictureName, PATH_IMAGE_PAGE_TEMPLATE, filePicture );

        pageTemplate.setDescription( strDescription );
        PageTemplateHome.create( pageTemplate );

        // If the process is successful, redirects towards the theme view
        return getHomeUrl( request );
    }

    /**
     * Returns the page template form of update
     *
     * @param request
     *            The Http request
     * @return the html code of the page template form
     */
    public String getModifyPageTemplate( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_PAGE_TEMPLATE );

        String strId = request.getParameter( Parameters.PAGE_TEMPLATE_ID );

        HashMap<String, Object> model = new HashMap<>( );
        model.put( MARK_PAGE_TEMPLATE, PageTemplateHome.findByPrimaryKey( Integer.parseInt( strId ) ) );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_MODIFY_PAGE_TEMPLATE ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_PAGE_TEMPLATE, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Processes the updating form of a page template whose new parameters are stored in the http request
     *
     * @param request
     *            The http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doModifyPageTemplate( HttpServletRequest request ) throws AccessDeniedException
    {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        String strId = multipartRequest.getParameter( Parameters.PAGE_TEMPLATE_ID );
        String strDescription = multipartRequest.getParameter( Parameters.PAGE_TEMPLATE_DESCRIPTION );
        String strUpdatePicture = multipartRequest.getParameter( PARAMETER_PAGE_TEMPLATE_UPDATE_IMAGE );
        String strUpdateFile = multipartRequest.getParameter( PARAMETER_PAGE_TEMPLATE_UPDATE_FILE );

        PageTemplate pageTemplate = PageTemplateHome.findByPrimaryKey( Integer.parseInt( strId ) );

        boolean bUpdateFile = false;
        boolean bUpdatePicture = false;
        FileItem fileTemplate = multipartRequest.getFile( PARAMETER_PAGE_TEMPLATE_FILE );
        String strFileName = FileUploadService.getFileNameOnly( fileTemplate );
        FileItem filePicture = multipartRequest.getFile( PARAMETER_PAGE_TEMPLATE_PICTURE );
        String strPictureName = FileUploadService.getFileNameOnly( filePicture );

        boolean bHasError = false;

        if ( strUpdateFile != null )
        {
            if ( StringUtils.isEmpty( strFileName ) )
            {
                bHasError = true;
            }
            else
                if ( !( FileUtil.hasHtmlExtension( strFileName ) || FileUtil.hasFreemarkerExtension( strFileName ) ) )
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_WRONG_HTML_EXTENSION, AdminMessage.TYPE_STOP );
                }

            bUpdateFile = true;
        }

        if ( strUpdatePicture != null )
        {
            if ( StringUtils.isEmpty( strPictureName ) )
            {
                bHasError = true;
            }
            else
                if ( !FileUtil.hasImageExtension( strPictureName ) )
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_WRONG_IMAGE_EXTENSION, AdminMessage.TYPE_STOP );
                }

            bUpdatePicture = true;
        }

        if ( bHasError || StringUtils.isEmpty( strDescription ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FILE, AdminMessage.TYPE_STOP );
        }

        if ( !SecurityTokenService.getInstance( ).validate( multipartRequest, TEMPLATE_MODIFY_PAGE_TEMPLATE ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }

        if ( bUpdateFile )
        {
            FileUtil.deleteFile( new File( AppPathService.getPath( PROPERTY_PATH_TEMPLATE ), pageTemplate.getFile( ) ) );
            pageTemplate.setFile( AppPropertiesService.getProperty( PROPERTY_PATH_FILE_PAGE_TEMPLATE ) + strFileName );

            writeTemplateFile( strFileName, PATH_TEMPLATE, fileTemplate );
        }

        if ( bUpdatePicture )
        {
            FileUtil.deleteFile( new File( PATH_IMAGE_PAGE_TEMPLATE, pageTemplate.getPicture( ) ) );
            pageTemplate.setPicture( strPictureName );

            writeTemplateFile( strPictureName, PATH_IMAGE_PAGE_TEMPLATE, filePicture );
        }

        pageTemplate.setDescription( strDescription );
        PageTemplateHome.update( pageTemplate );

        // If the process is successful, redirects towards the page template management
        // page
        return getHomeUrl( request );
    }

    /**
     * Returns the confirm of removing the page_template whose identifier is in the http request
     * 
     * @param request
     *            The Http request
     * @return the html code for the remove confirmation page
     */
    public String getConfirmRemovePageTemplate( HttpServletRequest request )
    {
        String strId = request.getParameter( Parameters.PAGE_TEMPLATE_ID );
        int nId = Integer.parseInt( strId );

        boolean bIsUsed = PageTemplateHome.checkStylePageTemplateIsUsed( nId );

        if ( !bIsUsed )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_PAGE_TEMPLATE_IS_USED, AdminMessage.TYPE_STOP );
        }

        PageTemplate pageTemplate = PageTemplateHome.findByPrimaryKey( Integer.parseInt( strId ) );
        String strPathPageTemplateFile = AppPathService.getPath( PROPERTY_PATH_TEMPLATE ) + File.separator + pageTemplate.getFile( );
        String strPathPictureFile = PATH_IMAGE_PAGE_TEMPLATE + pageTemplate.getPicture( );
        Object [ ] args = {
                strPathPageTemplateFile, strPathPictureFile
        };

        Map<String, Object> parameters = new HashMap<>( );
        parameters.put( Parameters.PAGE_TEMPLATE_ID, strId );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, JSP_DO_REMOVE_PAGE_TEMPLATE ) );
        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_PAGE_TEMPLATE, args, null, JSP_DO_REMOVE_PAGE_TEMPLATE, null,
                AdminMessage.TYPE_CONFIRMATION, parameters );
    }

    /**
     * Processes the deletion of a page template
     * 
     * @param request
     *            the http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doRemovePageTemplate( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, JSP_DO_REMOVE_PAGE_TEMPLATE ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }

        String strId = request.getParameter( Parameters.PAGE_TEMPLATE_ID );
        int nId = Integer.parseInt( strId );

        // Delete files associated
        PageTemplate pageTemplate = PageTemplateHome.findByPrimaryKey( Integer.parseInt( strId ) );

        File filePageTemplateToDelete = new File( AppPathService.getPath( PROPERTY_PATH_TEMPLATE ), pageTemplate.getFile( ) );
        FileUtil.deleteFile( filePageTemplateToDelete );

        File filePictureToDelete = new File( PATH_IMAGE_PAGE_TEMPLATE, pageTemplate.getPicture( ) );
        FileUtil.deleteFile( filePictureToDelete );

        PageTemplateHome.remove( nId );

        return getHomeUrl( request );
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // private methods

    /**
     * Write the templates files (html and image)
     *
     * @param strFileName
     *            The name of the file
     * @param strPath
     *            The path of the file
     * @param fileItem
     *            The fileItem object which contains the new file
     */
    private void writeTemplateFile( String strFileName, String strPath, FileItem fileItem )
    {
        File file = new File( strPath + strFileName );
        FileUtil.deleteFile( file );

        try ( FileOutputStream fosFile = new FileOutputStream( file ) )
        {
            fosFile.write( fileItem.get( ) );
        }
        catch( IOException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
    }
}
