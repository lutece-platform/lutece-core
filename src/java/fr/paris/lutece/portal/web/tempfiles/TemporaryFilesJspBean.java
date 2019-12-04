/*
 * Copyright (c) 2002-2019, Mairie de Paris
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
package fr.paris.lutece.portal.web.tempfiles;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rometools.utils.Strings;

import fr.paris.lutece.portal.business.file.TemporaryFile;
import fr.paris.lutece.portal.business.file.TemporaryFileHome;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFileHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * This class provides the user interface to list temporary files
 */
@Controller( controllerJsp = "ManageMyFiles", controllerPath = "jsp/admin/tempfiles/", right = "CORE_TEMP_FILES" )
public class TemporaryFilesJspBean extends MVCAdminJspBean
{
    private static final long serialVersionUID = 2823024035296798126L;
    
    // Rights
    public static final String CORE_TEMP_FILES = "CORE_TEMP_FILES";
    
    // Parameter
    public static final String PARAMETER_FILE_ID = "file_id";
    
    // View
    private static final String VIEW_MY_FILES = "view_myFiles";
    
    // Templates
    private static final String TEMPLATE_TEMPORARY_FILES = "admin/tempfiles/manage_temporary_files.html";
    private static final String PROPERTY_TITLE_MANAGE_FILES_SYSTEM = "portal.tempfiles.manage_temporary_files.pageTitle";
    
    // Marks
    private static final String MARK_FILES = "files_list";
    
    @View( value = VIEW_MY_FILES, defaultView = true )
    public String getTemporaryFiles( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_TITLE_MANAGE_FILES_SYSTEM );
        List<TemporaryFile> listFiles = TemporaryFileHome.findByUser( getUser( ) );
        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_FILES, listFiles );
        
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TEMPORARY_FILES, getLocale( ), model );
        
        return getAdminPage( template.getHtml( ) );
    }
 
    public String doDownloadFile( HttpServletRequest request, HttpServletResponse response ) throws AccessDeniedException, IOException
    {
        String strId = request.getParameter( PARAMETER_FILE_ID );
        if ( Strings.isNotEmpty( strId ) )
        {
            TemporaryFile file = TemporaryFileHome.findByPrimaryKey( Integer.valueOf( strId ) );
            
            if ( file.getUser( ).getUserId( ) != getUser( ).getUserId( ) )
            {
               throw new AccessDeniedException( "Access Denied to this file" );
            }
            if ( file.getPhysicalFile( ) == null )
            {
                throw new AccessDeniedException( "File not yet generated" );
            }
            PhysicalFile physicalFile = PhysicalFileHome.findByPrimaryKey( file.getPhysicalFile( ).getIdPhysicalFile( ) );
            
            response.setContentType( file.getTitle( ) );
            response.setHeader( "Content-Disposition", "attachment; filename=\"" + file.getTitle( ) + "\";" );
            OutputStream out = response.getOutputStream( );
            out.write( physicalFile.getValue( ) );
            out.flush( );
        }
        return null;
    }
}
