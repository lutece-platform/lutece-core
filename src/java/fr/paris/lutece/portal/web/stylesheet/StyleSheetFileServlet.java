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
package fr.paris.lutece.portal.web.stylesheet;

import fr.paris.lutece.portal.business.stylesheet.StyleSheet;
import fr.paris.lutece.portal.business.stylesheet.StyleSheetHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.web.constants.Parameters;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * StyleSheetFile Servlet
 */
public class StyleSheetFileServlet extends HttpServlet
{
    /**
     *
     */
    private static final long serialVersionUID = 9154028959985088272L;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
         * @throws ServletException the servlet Exception
         * @throws IOException the io exception
     */
    protected void processRequest( HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        AdminUser user = AdminUserService.getAdminUser( request );

        // FIXME : use a jsp instead of a servlet to control the right in a standard way
        if ( ( user != null ) && ( user.checkRight( StyleSheetJspBean.RIGHT_MANAGE_STYLESHEET ) ) )
        {
            String strStyleSheetId = request.getParameter( Parameters.STYLESHEET_ID );

            if ( strStyleSheetId != null )
            {
                int nStyleSheetId = Integer.parseInt( strStyleSheetId );

                StyleSheet stylesheet = StyleSheetHome.findByPrimaryKey( nStyleSheetId );

                ServletContext context = getServletConfig(  ).getServletContext(  );
                String strMimetype = context.getMimeType( stylesheet.getFile(  ) );
                response.setContentType( ( strMimetype != null ) ? strMimetype : "application/octet-stream" );
                response.setHeader( "Content-Disposition", "attachement; filename=\"" + stylesheet.getFile(  ) + "\"" );

                OutputStream out = response.getOutputStream(  );
                out.write( stylesheet.getSource(  ) );
                out.flush(  );
                out.close(  );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        processRequest( request, response );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        processRequest( request, response );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getServletInfo(  )
    {
        return "Servlet serving stylesheets";
    }
}
