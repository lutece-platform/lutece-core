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
package fr.paris.lutece.portal.web.dashboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.prefs.AdminUserPreferencesService;
import fr.paris.lutece.portal.service.util.AppLogService;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet storing and restoring the admin home dashboard widget layout (visibility, column and order of each widget) in the shared
 * {@code core_admin_user_preferences} table, so the layout follows the user across browsers and environments.
 *
 * <p>
 * The layout is primarily kept in the browser {@code localStorage} (see {@code webapp/themes/admin/shared/js/dashboard-widgets.js}). This servlet is the
 * cross-environment fallback : the browser POSTs the layout on logout (before {@code localStorage} is wiped by the {@code Clear-Site-Data} header) and GETs it
 * back on the next login when no local copy is available.
 * </p>
 */
public class DashboardWidgetsPreferencesServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    /** Preference key under which the layout JSON is stored, per admin user. */
    private static final String PREFERENCE_KEY = "core.dashboard.widgets.layout";

    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String EMPTY_JSON_OBJECT = "{}";

    /** Hard cap on the accepted payload size to protect the LONG VARCHAR column from abuse (64 KB is far above a real layout). */
    private static final int MAX_PAYLOAD_LENGTH = 64 * 1024;

    private final transient ObjectMapper _mapper = new ObjectMapper( );

    /**
     * Returns the stored layout JSON for the authenticated admin user, or an empty JSON object when nothing is stored.
     *
     * @param request
     *            The http request
     * @param response
     *            The http response
     * @throws IOException
     *             If an I/O exception occurs
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        AdminUser user = AdminAuthenticationService.getInstance( ).getRegisteredUser( request );

        if ( user == null )
        {
            response.sendError( HttpServletResponse.SC_FORBIDDEN );
            return;
        }

        String strUserId = String.valueOf( user.getUserId( ) );
        String strValue = AdminUserPreferencesService.instance( ).get( strUserId, PREFERENCE_KEY, EMPTY_JSON_OBJECT );

        response.setContentType( CONTENT_TYPE_JSON );

        PrintWriter out = response.getWriter( );
        out.print( ( strValue == null || strValue.isEmpty( ) ) ? EMPTY_JSON_OBJECT : strValue );
    }

    /**
     * Stores the layout JSON sent in the request body for the authenticated admin user.
     *
     * @param request
     *            The http request
     * @param response
     *            The http response
     * @throws IOException
     *             If an I/O exception occurs
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        AdminUser user = AdminAuthenticationService.getInstance( ).getRegisteredUser( request );

        if ( user == null )
        {
            response.sendError( HttpServletResponse.SC_FORBIDDEN );
            return;
        }

        String strBody = readBody( request );

        if ( strBody.isEmpty( ) || strBody.length( ) > MAX_PAYLOAD_LENGTH || !isJsonObject( strBody ) )
        {
            response.sendError( HttpServletResponse.SC_BAD_REQUEST );
            return;
        }

        String strUserId = String.valueOf( user.getUserId( ) );
        AdminUserPreferencesService.instance( ).put( strUserId, PREFERENCE_KEY, strBody );

        response.setStatus( HttpServletResponse.SC_NO_CONTENT );
    }

    /**
     * Reads the request body, stopping early once the size cap is exceeded.
     *
     * @param request
     *            The http request
     * @return The body as a trimmed String (possibly longer than the cap, in which case the caller rejects it)
     * @throws IOException
     *             If an I/O exception occurs
     */
    private String readBody( HttpServletRequest request ) throws IOException
    {
        StringBuilder sb = new StringBuilder( );

        try ( BufferedReader reader = request.getReader( ) )
        {
            char [ ] buffer = new char [ 4096];
            int nRead;

            while ( ( nRead = reader.read( buffer ) ) != -1 )
            {
                sb.append( buffer, 0, nRead );

                if ( sb.length( ) > MAX_PAYLOAD_LENGTH )
                {
                    break;
                }
            }
        }

        return sb.toString( ).trim( );
    }

    /**
     * Lightweight check that the payload is a well-formed JSON object.
     *
     * @param strBody
     *            The payload
     * @return {@code true} if the payload parses as a JSON object
     */
    private boolean isJsonObject( String strBody )
    {
        if ( strBody.charAt( 0 ) != '{' )
        {
            return false;
        }

        try
        {
            _mapper.readTree( strBody );
            return true;
        }
        catch( IOException e )
        {
            AppLogService.debug( "DashboardWidgetsPreferencesServlet : rejected malformed layout payload : {}", e.getMessage( ) );
            return false;
        }
    }
}
