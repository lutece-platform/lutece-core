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
package fr.paris.lutece.portal.web.download;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.file.ExpiredLinkException;
import fr.paris.lutece.portal.service.file.FileService;
import fr.paris.lutece.portal.service.file.IFileStoreServiceProvider;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.PortalJspBean;

public abstract class AbstractDownloadServlet extends HttpServlet
{
    private static final long serialVersionUID = 6622358100579620819L;
    private static final String MESSAGE_UNKNOWN_PROVIDER = "portal.file.download.provider.unknown";
    private static final String MESSAGE_UNKNOWN_FILE = "portal.file.download.file.unknown";

    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        File file = null;
        IFileStoreServiceProvider fileStoreServiceProvider = FileService.getInstance( )
                .getFileStoreServiceProvider( request.getParameter( FileService.PARAMETER_PROVIDER ) );

        try
        {
            if ( fileStoreServiceProvider == null )
            {
                SiteMessageService.setMessage( request, MESSAGE_UNKNOWN_PROVIDER );
            }
            else
            {

                try
                {
                    if ( isFromBo( ) )
                    {
                        file = fileStoreServiceProvider.getFileFromRequestBO( request );
                    }
                    else
                    {
                        file = fileStoreServiceProvider.getFileFromRequestFO( request );
                    }
                }
                catch( AccessDeniedException | ExpiredLinkException ex )
                {
                    SiteMessageService.setMessage( request, ex.getLocalizedMessage( ) );
                }
                catch( UserNotSignedException e )
                {
                    response.sendRedirect( response.encodeRedirectURL( PortalJspBean.redirectLogin( request ) ) );
                }
            }

            if ( file == null || file.getPhysicalFile( ) == null )
            {
                SiteMessageService.setMessage( request, MESSAGE_UNKNOWN_FILE );
            }

        }
        catch( SiteMessageException e )
        {
            response.sendRedirect( AppPathService.getSiteMessageUrl( request ) );
        }

        if ( file != null )
        {
            // send the file
            try ( OutputStream outputStream = response.getOutputStream( ) )
            {
                response.setContentType( file.getMimeType( ) );
                response.setHeader( "Content-Disposition", "attachment; filename=\"" + file.getTitle( ) + "\";" );
                outputStream.write( file.getPhysicalFile( ).getValue( ) );
            }
        }
    }

    protected abstract boolean isFromBo( );
}
