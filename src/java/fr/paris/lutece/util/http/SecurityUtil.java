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
package fr.paris.lutece.util.http;

import fr.paris.lutece.util.string.StringUtil;

import org.apache.commons.lang.StringUtils;

import org.apache.log4j.Logger;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;


/**
 * Security utils
 *
 */
public final class SecurityUtil
{
    private static final String LOGGER_NAME = "lutece.security.http";
    private static final String CONSTANT_HTTP_HEADER_X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String PATTERN_IP_ADDRESS = "^([0-9]{1,3}\\.){3}[0-9]{1,3}$";
    private static final String CONSTANT_COMMA = ",";

    //private static final String PATTERN_CLEAN_PARAMETER = "^[\\w/]+$+";

    /**
     * Private Constructor
     */
    private SecurityUtil(  )
    {
    }

    /**
     * Scan request parameters to see if there no malicious code.
     *
     * @param request The HTTP request
     * @return true if all parameters don't contains any special characters
     */
    public static boolean containsCleanParameters( HttpServletRequest request )
    {
        return containsCleanParameters( request, null );
    }

    /**
     * Scan request parameters to see if there no malicious code.
     *
     * @param request The HTTP request
     * @param strXssCharacters a String wich contain a list of Xss characters to check in strValue
     * @return true if all parameters don't contains any special characters
     */
    public static boolean containsCleanParameters( HttpServletRequest request, String strXssCharacters )
    {
        String key;
        String[] values;

        Enumeration<String> e = request.getParameterNames(  );

        while ( e.hasMoreElements(  ) )
        {
            key = e.nextElement(  );
            values = request.getParameterValues( key );

            int length = values.length;

            for ( int i = 0; i < length; i++ )
            {
                if ( SecurityUtil.containsXssCharacters( request, values[i], strXssCharacters ) )
                {
                    Logger logger = Logger.getLogger( LOGGER_NAME );
                    logger.warn( "SECURITY WARNING : INVALID REQUEST PARAMETERS" + dumpRequest( request ) );

                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Checks if a String contains characters that could be used for a
     * cross-site scripting attack.
     *
     * @param request The HTTP request
     * @param strString a character String
     * @return true if the String contains illegal characters
     */
    public static boolean containsXssCharacters( HttpServletRequest request, String strString )
    {
        return containsXssCharacters( request, strString, null );
    }

    /**
     * Checks if a String contains characters that could be used for a
     * cross-site scripting attack.
     *
     * @param request The HTTP request
     * @param strValue a character String
     * @param strXssCharacters a String wich contain a list of Xss characters to check in strValue
     * @return true if the String contains illegal characters
     */
    public static boolean containsXssCharacters( HttpServletRequest request, String strValue, String strXssCharacters )
    {
        boolean bContains = ( strXssCharacters == null ) ? StringUtil.containsXssCharacters( strValue )
                                                         : StringUtil.containsXssCharacters( strValue, strXssCharacters );

        if ( bContains )
        {
            Logger logger = Logger.getLogger( LOGGER_NAME );
            logger.warn( "SECURITY WARNING : XSS CHARACTERS DETECTED" + dumpRequest( request ) );
        }

        return bContains;
    }

    /**
     * Dump all request info
     * @param request The HTTP request
     * @return A report containing all request info
     */
    public static String dumpRequest( HttpServletRequest request )
    {
        StringBuffer sbDump = new StringBuffer( "\r\n Request Dump : \r\n" );
        dumpTitle( sbDump, "Request variables" );
        dumpVariables( sbDump, request );
        dumpTitle( sbDump, "Request parameters" );
        dumpParameters( sbDump, request );
        dumpTitle( sbDump, "Request headers" );
        dumpHeaders( sbDump, request );

        return sbDump.toString(  );
    }

    /**
     * Get the IP of the user from a request. If the user is behind an apache
     * server, return the ip of the user instead of the ip of the server.
     * @param request The request
     * @return The IP of the user that made the request
     */
    public static String getRealIp( HttpServletRequest request )
    {
        String strIPAddress = request.getHeader( CONSTANT_HTTP_HEADER_X_FORWARDED_FOR );

        if ( strIPAddress != null )
        {
            String strIpForwarded = null;

            while ( !strIPAddress.matches( PATTERN_IP_ADDRESS ) && strIPAddress.contains( CONSTANT_COMMA ) )
            {
                strIpForwarded = strIPAddress.substring( 0, strIPAddress.indexOf( CONSTANT_COMMA ) );
                strIPAddress = strIPAddress.substring( strIPAddress.indexOf( CONSTANT_COMMA ) )
                                           .replaceFirst( CONSTANT_COMMA, StringUtils.EMPTY ).trim(  );

                if ( ( strIpForwarded != null ) && strIpForwarded.matches( PATTERN_IP_ADDRESS ) )
                {
                    strIPAddress = strIpForwarded;
                }
            }

            if ( !strIPAddress.matches( PATTERN_IP_ADDRESS ) )
            {
                strIPAddress = request.getRemoteAddr(  );
            }
        }
        else
        {
            strIPAddress = request.getRemoteAddr(  );
        }

        return strIPAddress;
    }

    /**
     * Write a title into the dump stringbuffer
     * @param sbDump The dump stringbuffer
     * @param strTitle The title
     */
    private static void dumpTitle( StringBuffer sbDump, String strTitle )
    {
        sbDump.append( "** " );
        sbDump.append( strTitle );
        sbDump.append( "  **\r\n" );
    }

    /**
     * Write request variables into the dump stringbuffer
     * @param sb The dump stringbuffer
     * @param request The HTTP request
     */
    private static void dumpVariables( StringBuffer sb, HttpServletRequest request )
    {
        dumpVariable( sb, "AUTH_TYPE", request.getAuthType(  ) );
        dumpVariable( sb, "REQUEST_METHOD", request.getMethod(  ) );
        dumpVariable( sb, "PATH_INFO", request.getPathInfo(  ) );
        dumpVariable( sb, "PATH_TRANSLATED", request.getPathTranslated(  ) );
        dumpVariable( sb, "QUERY_STRING", request.getQueryString(  ) );
        dumpVariable( sb, "REQUEST_URI", request.getRequestURI(  ) );
        dumpVariable( sb, "SCRIPT_NAME", request.getServletPath(  ) );
        dumpVariable( sb, "LOCAL_ADDR", request.getLocalAddr(  ) );
        dumpVariable( sb, "SERVER_PROTOCOL", request.getProtocol(  ) );
        dumpVariable( sb, "REMOTE_ADDR", request.getRemoteAddr(  ) );
        dumpVariable( sb, "REMOTE_HOST", request.getRemoteHost(  ) );
        dumpVariable( sb, "HTTPS", request.getScheme(  ) );
        dumpVariable( sb, "SERVER_NAME", request.getServerName(  ) );
        dumpVariable( sb, "SERVER_PORT", String.valueOf( request.getServerPort(  ) ) );
    }

    /**
     * Write request headers infos into the dump stringbuffer
     * @param sb The dump stringbuffer
     * @param request The HTTP request
     */
    private static void dumpHeaders( StringBuffer sb, HttpServletRequest request )
    {
        Enumeration<String> values;
        String key;
        Enumeration<String> headers = request.getHeaderNames(  );

        while ( headers.hasMoreElements(  ) )
        {
            key = headers.nextElement(  );
            values = request.getHeaders( key );

            while ( values.hasMoreElements(  ) )
            {
                dumpVariable( sb, key, values.nextElement(  ) );
            }
        }
    }

    /**
     * Write request parameters infos into the dump stringbuffer
     * @param sb The dump stringbuffer
     * @param request The HTTP request
     */
    private static void dumpParameters( StringBuffer sb, HttpServletRequest request )
    {
        String key;
        String[] values;

        Enumeration<String> e = request.getParameterNames(  );

        while ( e.hasMoreElements(  ) )
        {
            key = e.nextElement(  );
            values = request.getParameterValues( key );

            int length = values.length;

            for ( int i = 0; i < length; i++ )
            {
                dumpVariable( sb, key, values[i] );
            }
        }
    }

    /**
     * Write name / value infos into the dump stringbuffer
     * @param sb The dump string buffer
     * @param strName The info name
     * @param strValue The info value
     */
    private static void dumpVariable( StringBuffer sb, String strName, String strValue )
    {
        sb.append( strName );
        sb.append( " : \"" );
        sb.append( strValue );
        sb.append( "\"\r\n" );
    }
}
