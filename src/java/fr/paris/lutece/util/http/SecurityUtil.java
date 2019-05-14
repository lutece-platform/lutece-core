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
package fr.paris.lutece.util.http;

import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.util.string.StringUtil;


import org.apache.log4j.Logger;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;

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
    private static final String [ ] XXE_TERMS = {
            "!DOCTYPE", "!ELEMENT", "!ENTITY"
    };
    private static final String [ ] PATH_MANIPULATION = {
            "..", "/", "\\"
    };

    public static final String PROPERTY_REDIRECT_URL_SAFE_PATTERNS = "lutece.security.redirectUrlSafePatterns";

    // private static final String PATTERN_CLEAN_PARAMETER = "^[\\w/]+$+";

    /**
     * Private Constructor
     */
    private SecurityUtil( )
    {
    }

    /**
     * Scan request parameters to see if there no malicious code.
     *
     * @param request
     *            The HTTP request
     * @return true if all parameters don't contains any special characters
     */
    public static boolean containsCleanParameters( HttpServletRequest request )
    {
        return containsCleanParameters( request, null );
    }

    /**
     * Scan request parameters to see if there no malicious code.
     *
     * @param request
     *            The HTTP request
     * @param strXssCharacters
     *            a String wich contain a list of Xss characters to check in strValue
     * @return true if all parameters don't contains any special characters
     */
    public static boolean containsCleanParameters( HttpServletRequest request, String strXssCharacters )
    {
        String key;
        String [ ] values;

        Enumeration<String> e = request.getParameterNames( );

        while ( e.hasMoreElements( ) )
        {
            key = e.nextElement( );
            values = request.getParameterValues( key );

            int length = values.length;

            for ( int i = 0; i < length; i++ )
            {
                if ( SecurityUtil.containsXssCharacters( request, values [i], strXssCharacters ) )
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
     * Checks if a String contains characters that could be used for a cross-site scripting attack.
     *
     * @param request
     *            The HTTP request
     * @param strString
     *            a character String
     * @return true if the String contains illegal characters
     */
    public static boolean containsXssCharacters( HttpServletRequest request, String strString )
    {
        return containsXssCharacters( request, strString, null );
    }

    /**
     * Checks if a String contains characters that could be used for a cross-site scripting attack.
     *
     * @param request
     *            The HTTP request
     * @param strValue
     *            a character String
     * @param strXssCharacters
     *            a String wich contain a list of Xss characters to check in strValue
     * @return true if the String contains illegal characters
     */
    public static boolean containsXssCharacters( HttpServletRequest request, String strValue, String strXssCharacters )
    {
        boolean bContains = ( strXssCharacters == null ) ? StringUtil.containsXssCharacters( strValue ) : StringUtil.containsXssCharacters( strValue,
                strXssCharacters );

        if ( bContains )
        {
            Logger logger = Logger.getLogger( LOGGER_NAME );
            logger.warn( "SECURITY WARNING : XSS CHARACTERS DETECTED" + dumpRequest( request ) );
        }

        return bContains;
    }

    /**
     * Check if the value contains terms used for XML External Entity Injection
     * 
     * @param strValue
     *            The value
     * @return true if
     */
    public static boolean containsXmlExternalEntityInjectionTerms( String strValue )
    {
        for ( String strTerm : XXE_TERMS )
        {
            if ( StringUtils.indexOfIgnoreCase( strValue, strTerm ) >= 0 )
            {
                Logger logger = Logger.getLogger( LOGGER_NAME );
                logger.warn( "SECURITY WARNING : XXE TERMS DETECTED : " + dumpRequest( LocalVariables.getRequest( ) ) );
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the value contains characters used for Path Manipulation
     * 
     * @param request
     *            The Http request
     * @param strValue
     *            The value
     * @return true if
     */
    public static boolean containsPathManipulationChars( HttpServletRequest request, String strValue )
    {
        for ( String strTerm : PATH_MANIPULATION )
        {
            if ( strValue.contains( strTerm ) )
            {
                Logger logger = Logger.getLogger( LOGGER_NAME );
                logger.warn( "SECURITY WARNING : PATH_MANIPULATION DETECTED : " + dumpRequest( request ) );
                return true;
            }
        }
        return false;
    }

    /**
     * Dump all request info
     * 
     * @param request
     *            The HTTP request
     * @return A report containing all request info
     */
    public static String dumpRequest( HttpServletRequest request )
    {
        StringBuffer sbDump = new StringBuffer( "\r\n Request Dump : \r\n" );
        if ( request != null )
        {
            dumpTitle( sbDump, "Request variables" );
            dumpVariables( sbDump, request );
            dumpTitle( sbDump, "Request parameters" );
            dumpParameters( sbDump, request );
            dumpTitle( sbDump, "Request headers" );
            dumpHeaders( sbDump, request );
        }
        else
        {
            sbDump.append( "no request provided." );
        }

        return sbDump.toString( );
    }

    /**
     * Get the IP of the user from a request. If the user is behind an apache server, return the ip of the user instead of the ip of the server.
     * 
     * @param request
     *            The request
     * @return The IP of the user that made the request
     */
    public static String getRealIp( HttpServletRequest request )
    {
        String strIPAddress = request.getHeader( CONSTANT_HTTP_HEADER_X_FORWARDED_FOR );

        if ( strIPAddress != null )
        {
            while ( !strIPAddress.matches( PATTERN_IP_ADDRESS ) && strIPAddress.contains( CONSTANT_COMMA ) )
            {
                String strIpForwarded = strIPAddress.substring( 0, strIPAddress.indexOf( CONSTANT_COMMA ) );
                strIPAddress = strIPAddress.substring( strIPAddress.indexOf( CONSTANT_COMMA ) ).replaceFirst( CONSTANT_COMMA, StringUtils.EMPTY ).trim( );

                if ( ( strIpForwarded != null ) && strIpForwarded.matches( PATTERN_IP_ADDRESS ) )
                {
                    strIPAddress = strIpForwarded;
                }
            }

            if ( !strIPAddress.matches( PATTERN_IP_ADDRESS ) )
            {
                strIPAddress = request.getRemoteAddr( );
            }
        }
        else
        {
            strIPAddress = request.getRemoteAddr( );
        }

        return strIPAddress;
    }

    /**
     * Validate a forward URL to avoid open redirect with url safe patterns found in properties
     * 
     * @see SecurityUtil#isInternalRedirectUrlSafe(java.lang.String, javax.servlet.http.HttpServletRequest, java.lang.String)
     * 
     * @param strUrl
     * @param request
     * @return true if valid
     */
    public static boolean isInternalRedirectUrlSafe( String strUrl, HttpServletRequest request )
    {
        String strAntPathMatcherPatternsValues = AppPropertiesService.getProperty( SecurityUtil.PROPERTY_REDIRECT_URL_SAFE_PATTERNS );

        return isInternalRedirectUrlSafe( strUrl, request, strAntPathMatcherPatternsValues );
    }

    /**
     * Validate an internal redirect URL to avoid internal open redirect. (Use this function only if the use of internal url redirect keys is not possible. For
     * external url redirection control, use the plugin plugin-verifybackurl)
     * 
     * the url should : - not be blank (null or empty string or spaces) - not start with "http://" or "https://" or "//" OR match the base URL or any URL in the
     * pattern list
     * 
     * example with a base url "https://lutece.fr/ : - valid : myapp/jsp/site/Portal.jsp , Another.jsp , https://lutece.fr/myapp/jsp/site/Portal.jsp - invalid :
     * http://anothersite.com , https://anothersite.com , //anothersite.com , file://my.txt , ...
     * 
     * 
     * @param strUrl
     *            the Url to validate
     * @param request
     *            the current request (containing the baseUrl)
     * @param strAntPathMatcherPatterns
     *            a comma separated list of AntPathMatcher patterns, as "http://**.lutece.com,https://**.lutece.com"
     * @return true if valid
     */
    public static boolean isInternalRedirectUrlSafe( String strUrl, HttpServletRequest request, String strAntPathMatcherPatterns )
    {

        if ( StringUtils.isBlank( strUrl ) )
            return true; // this is not a valid redirect Url, but it is not unsafe

        // filter schemes
        if ( !strUrl.startsWith( "//" ) && !strUrl.startsWith( "http:" ) && !strUrl.startsWith( "https:" ) && !strUrl.contains( "://" )
                && !strUrl.startsWith( "javascript:" ) )
            return true; // should be a relative path

        // compare with current baseUrl
        if ( strUrl.startsWith( AppPathService.getBaseUrl( request ) ) )
            return true;

        // compare with allowed url patterns
        if ( !StringUtils.isBlank( strAntPathMatcherPatterns ) )
        {
            AntPathMatcher pathMatcher = new AntPathMatcher( );

            String [ ] strAntPathMatcherPatternsTab = strAntPathMatcherPatterns.split( CONSTANT_COMMA );
            for ( String pattern : strAntPathMatcherPatternsTab )
            {
                if ( pattern != null && pathMatcher.match( pattern, strUrl ) )
                    return true;
            }
        }

        // the Url does not match the allowed patterns
        Logger logger = Logger.getLogger( LOGGER_NAME );
        logger.warn( "SECURITY WARNING : OPEN_REDIRECT DETECTED : " + dumpRequest( request ) );

        return false;

    }

    /**
     * Identify user data saved in log files to prevent Log Forging attacks
     * 
     * @param strUserInputData
     *            User Input Data
     * @return The User Data to log
     */
    public static String logForgingProtect( String strUserInputData )
    {
        int nCharCount = strUserInputData.length( );
        int nLineCount = StringUtils.countMatches( strUserInputData, "\n" );
        String strPrefixedLines = strUserInputData.replace( "\n", "\n** " );
        String strProtected = "\n** USER INPUT DATA : BEGIN (" + nLineCount + " lines and " + nCharCount + " chars) ** \n" + strPrefixedLines
                + "\n** USER INPUT DATA : END\n";
        return strProtected;
    }

    /**
     * Write a title into the dump stringbuffer
     * 
     * @param sbDump
     *            The dump stringbuffer
     * @param strTitle
     *            The title
     */
    private static void dumpTitle( StringBuffer sbDump, String strTitle )
    {
        sbDump.append( "** " );
        sbDump.append( strTitle );
        sbDump.append( "  **\r\n" );
    }

    /**
     * Write request variables into the dump stringbuffer
     * 
     * @param sb
     *            The dump stringbuffer
     * @param request
     *            The HTTP request
     */
    private static void dumpVariables( StringBuffer sb, HttpServletRequest request )
    {
        dumpVariable( sb, "AUTH_TYPE", request.getAuthType( ) );
        dumpVariable( sb, "REQUEST_METHOD", request.getMethod( ) );
        dumpVariable( sb, "PATH_INFO", request.getPathInfo( ) );
        dumpVariable( sb, "PATH_TRANSLATED", request.getPathTranslated( ) );
        dumpVariable( sb, "QUERY_STRING", request.getQueryString( ) );
        dumpVariable( sb, "REQUEST_URI", request.getRequestURI( ) );
        dumpVariable( sb, "SCRIPT_NAME", request.getServletPath( ) );
        dumpVariable( sb, "LOCAL_ADDR", request.getLocalAddr( ) );
        dumpVariable( sb, "SERVER_PROTOCOL", request.getProtocol( ) );
        dumpVariable( sb, "REMOTE_ADDR", request.getRemoteAddr( ) );
        dumpVariable( sb, "REMOTE_HOST", request.getRemoteHost( ) );
        dumpVariable( sb, "HTTPS", request.getScheme( ) );
        dumpVariable( sb, "SERVER_NAME", request.getServerName( ) );
        dumpVariable( sb, "SERVER_PORT", String.valueOf( request.getServerPort( ) ) );
    }

    /**
     * Write request headers infos into the dump stringbuffer
     * 
     * @param sb
     *            The dump stringbuffer
     * @param request
     *            The HTTP request
     */
    private static void dumpHeaders( StringBuffer sb, HttpServletRequest request )
    {
        Enumeration<String> values;
        String key;
        Enumeration<String> headers = request.getHeaderNames( );

        while ( headers.hasMoreElements( ) )
        {
            key = headers.nextElement( );
            values = request.getHeaders( key );

            while ( values.hasMoreElements( ) )
            {
                dumpVariable( sb, key, values.nextElement( ) );
            }
        }
    }

    /**
     * Write request parameters infos into the dump stringbuffer
     * 
     * @param sb
     *            The dump stringbuffer
     * @param request
     *            The HTTP request
     */
    private static void dumpParameters( StringBuffer sb, HttpServletRequest request )
    {
        String key;
        String [ ] values;

        Enumeration<String> e = request.getParameterNames( );

        while ( e.hasMoreElements( ) )
        {
            key = e.nextElement( );
            values = request.getParameterValues( key );

            int length = values.length;

            for ( int i = 0; i < length; i++ )
            {
                dumpVariable( sb, key, values [i] );
            }
        }
    }

    /**
     * Write name / value infos into the dump stringbuffer
     * 
     * @param sb
     *            The dump string buffer
     * @param strName
     *            The info name
     * @param strValue
     *            The info value
     */
    private static void dumpVariable( StringBuffer sb, String strName, String strValue )
    {
        sb.append( strName );
        sb.append( " : \"" );
        sb.append( strValue );
        sb.append( "\"\r\n" );
    }
}
