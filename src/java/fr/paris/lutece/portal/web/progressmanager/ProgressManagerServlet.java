/*
 * Copyright (c) 2002-2017, Mairie de Paris
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
package fr.paris.lutece.portal.web.progressmanager;

import fr.paris.lutece.portal.service.progressmanager.ProgressManagerService;
import fr.paris.lutece.util.json.ErrorJsonResponse;
import fr.paris.lutece.util.json.JsonResponse;
import fr.paris.lutece.util.json.JsonUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet to manage progress feeds
 * 
 * @author lutece team
 */
public class ProgressManagerServlet extends HttpServlet
{

    private static final long serialVersionUID = -6400074588556875395L;

    private static final String PARAMETER_PROGRESS = "progress";
    private static final String PARAMETER_REPORT = "report";
    private static final String PARAMETER_TOKEN = "token";
    private static final String PARAMETER_FROM_LINE = "fromLine";

    private static final String CONTENT_TYPE = "application/json";
    private static final String STATUS_NOT_FOUND = "not found";
    private static final String STATUS_BAD_PARAMETER = "bad parameter";

    private static final String ATTRIBUTE_NAME_LAST_LINE = "lastLine";
    private static final String ATTRIBUTE_NAME_LINES = "lines";

    /**
     * Initialize the servlet
     * @param config The servlet config
     * @throws ServletException If an exception occurs that interrupts the servlet's normal operation
     */
    public void init( ServletConfig config ) throws ServletException
    {
        super.init( config );
    }

    /**
     * Process HTTP Post request
     * @param request The http request
     * @param response The http response
     * @throws ServletException If an exception occurs that interrupts the servlet's normal operation
     * @throws IOException If an I/O exception occurs
     */
    @Override
    public void doPost( HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        response.setContentType( CONTENT_TYPE );
        PrintWriter out = response.getWriter(  );
        
        // token
        String strToken = (String) request.getParameter( PARAMETER_TOKEN );
        
        if ( strToken == null || strToken.isEmpty( ) )
        {
            out.println( JsonUtil.buildJsonResponse( new ErrorJsonResponse( STATUS_NOT_FOUND ) ) );
            out.flush( );
            out.close( );
            return;
        }
        
        ProgressManagerService progressManagerService = ProgressManagerService.getInstance( );

        if ( !progressManagerService.isRegistred( strToken ) )
        {
            out.println( JsonUtil.buildJsonResponse( new ErrorJsonResponse( STATUS_NOT_FOUND ) ) );
            out.flush( );
            out.close( );
            return;
        }

        if ( request.getParameter( PARAMETER_PROGRESS ) != null )
        {
            out.println( JsonUtil.buildJsonResponse( new JsonResponse( progressManagerService.getProgressStatus( strToken ) ) ) );
            out.flush( );
            out.close( );
            return;
        }
        else if ( request.getParameter( PARAMETER_REPORT ) != null )
        {
            int iFromLine = -1;
            try 
            {
                iFromLine = Integer.parseInt( request.getParameter( PARAMETER_FROM_LINE ) );
            }
            catch ( NumberFormatException e )
            { 
                out.println( JsonUtil.buildJsonResponse( new ErrorJsonResponse( STATUS_BAD_PARAMETER ) ) );
            }
            
            Map<String, Object> mapResponse = new HashMap<>( );
            List<String> reportLines = progressManagerService.getReport( strToken, iFromLine );
            mapResponse.put( ATTRIBUTE_NAME_LAST_LINE , iFromLine + reportLines.size( ) );
            mapResponse.put( ATTRIBUTE_NAME_LINES , reportLines );
            out.println( JsonUtil.buildJsonResponse( new JsonResponse( mapResponse ) ) );
            out.flush( );
            out.close( );
            return;
        }
        else
        {
            out.println( JsonUtil.buildJsonResponse( new ErrorJsonResponse( STATUS_NOT_FOUND ) ) );
        }
        
        out.flush( );
        out.close( );
    }    
}
