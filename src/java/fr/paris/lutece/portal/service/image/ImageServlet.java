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
package fr.paris.lutece.portal.service.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.LocalVariables;

/**
 * Servlet serving document file resources
 */
public class ImageServlet extends HttpServlet
{
    private static final long serialVersionUID = -5713203328367191908L;
    private static final String ERROR_MSG = "ImageServlet error : {}";
    public static final String PARAMETER_RESOURCE_TYPE = "resource_type";
    public static final String PARAMETER_ID = "id";
    private static final String PROPERTY_PATH_IMAGES = "path.images.root";
    private static final String PROPERTY_IMAGE_PAGE_DEFAULT = "image.page.default";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * 
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     */
    protected void processRequest( HttpServletRequest request, HttpServletResponse response )
    {
        String strResourceId = request.getParameter( PARAMETER_ID );
        String strResourceTypeId = request.getParameter( PARAMETER_RESOURCE_TYPE );

        // Passing the request through thread local variables to keep binary
        // compatibility
        // because the ImageResourceProvider doesn't pass it explicitly. When everything
        // is java8, we could instead use default methods of the interface.
        LocalVariables.setLocal( getServletConfig( ), request, response );

        try
        {
            ImageResource image;

            if ( strResourceId != null )
            {
                int nResourceId = Integer.parseInt( strResourceId );
                image = ImageResourceManager.getImageResource( strResourceTypeId, nResourceId );

                // Test the field image value
                if ( getImageExist( image ) )
                {
                    response.setContentType( image.getMimeType( ) );
                    try ( OutputStream out = response.getOutputStream( ) )
                    {
                        out.write( image.getImage( ) );
                    }
                    catch( IOException ex )
                    {
                        AppLogService.error( ERROR_MSG, ex.getMessage( ), ex );
                    }
                }
                else
                {
                    ServletContext sc = getServletContext( );
                    String strImageUrl = AppPathService.getAbsolutePathFromRelativePath(
                            AppPropertiesService.getProperty( PROPERTY_PATH_IMAGES ) + "/" + AppPropertiesService.getProperty( PROPERTY_IMAGE_PAGE_DEFAULT ) ); //
                    response.setContentType( sc.getMimeType( strImageUrl ) );

                    File file = new File( strImageUrl );
                    response.setContentLength( (int) file.length( ) );

                    try ( FileInputStream in = new FileInputStream( file ) ; OutputStream out = response.getOutputStream( ) )
                    {
                        // Copy the contents of the file to the output stream
                        byte [ ] buf = new byte [ 1024];
                        int count;
                        while ( ( count = in.read( buf ) ) >= 0 )
                        {
                            out.write( buf, 0, count );
                        }

                    }
                    catch( IOException ex )
                    {
                        AppLogService.error( ERROR_MSG, ex.getMessage( ), ex );
                    }
                }
            }
        }
        finally
        {
            LocalVariables.setLocal( null, null, null );
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     * 
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     * @throws ServletException
     *             the servlet Exception
     * @throws IOException
     *             the io exception
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
    {
        processRequest( request, response );
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * 
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     * @throws ServletException
     *             the servlet Exception
     * @throws IOException
     *             the io exception
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response )
    {
        processRequest( request, response );
    }

    /**
     * Returns a short description of the servlet.
     * 
     * @return message
     */
    @Override
    public String getServletInfo( )
    {
        return "Servlet serving images content";
    }

    /**
     * Test the existence of an image in the base. If the size of the contents of the field is null or lower or equal 1, nImageLength is with false, otherwise
     * it's true
     * 
     * @param image
     *            The Resource Image
     * @return true if images exist, otherwise return false
     */
    private boolean getImageExist( ImageResource image )
    {
        if ( image != null && image.getImage( ) != null )
        {
            int nImageLength = image.getImage( ).length;

            if ( nImageLength >= 1 )
            {
                return true;
            }
        }

        return false;
    }
}
