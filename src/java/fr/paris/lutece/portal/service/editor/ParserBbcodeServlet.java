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
package fr.paris.lutece.portal.service.editor;

import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet using for BBCODE parsing
 */
public class ParserBbcodeServlet extends HttpServlet
{
    private static final long serialVersionUID = -6564244054015195801L;

    //Properties
    private static final String PROPERTY_ENCODING = "lutece.encoding";

    //Parameter
    private static final String PARAMETER_DATA = "data";

    /**
     *  ParserBbcodeServlet
     */
    public ParserBbcodeServlet(  )
    {
        super(  );
    }

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
        String strValue = request.getParameter( PARAMETER_DATA );
        String strValueReturn = ( strValue != null ) ? EditorBbcodeService.getInstance(  ).parse( strValue ) : "";
        OutputStream out = response.getOutputStream(  );
        out.write( strValueReturn.getBytes( AppPropertiesService.getProperty( PROPERTY_ENCODING ) ) );
        out.flush(  );
        out.close(  );
    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
         * @throws ServletException the servlet Exception
         * @throws IOException the io exception
     */
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        processRequest( request, response );
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
         * @throws ServletException the servlet Exception
         * @throws IOException the io exception
     */
    protected void doPost( HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        processRequest( request, response );
    }

    /** Returns a short description of the servlet.
         * @return message
     */
    public String getServletInfo(  )
    {
        return "Servlet parsing content";
    }
}
