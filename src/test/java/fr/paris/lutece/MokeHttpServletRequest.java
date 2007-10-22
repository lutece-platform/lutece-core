/*
 * Copyright (c) 2002-2006, Mairie de Paris
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
package fr.paris.lutece;

import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.user.AdminUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.security.Principal;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * The Class is a moke object to simulate a HttpServletRequest
 * @author Mairie de Paris
 * @version 1.0
 */
public class MokeHttpServletRequest implements HttpServletRequest
{
    private static final String ATTRIBUTE_ADMIN_USER = "lutece_admin_user";
    private Cookie[] _cookies = null;
    private Map _mapParameters = new HashMap(  );
    private MokeHttpSession _session = null;

    public void registerAdminUserWithRigth( AdminUser user, String strRight )
    {
        Map<String, Right> mapRights = new HashMap<String, Right>(  );
        Right right = new Right(  );
        right.setId( strRight );
        mapRights.put( strRight, right );
        user.setRights( mapRights );

        //TODO set locale user
        user.setLocale( new Locale( "fr", "FR", "" ) );
        registerAdminUser( user );
    }

    public void registerAdminUser( AdminUser user )
    {
        getSession( true ).setAttribute( ATTRIBUTE_ADMIN_USER, user );
    }

    /**
     * getAuthType
     *
     * @return String
     */
    public String getAuthType(  )
    {
        return "";
    }

    /**
     * getCookies
     *
     * @return Cookie[]
    
     */
    public Cookie[] getCookies(  )
    {
        return _cookies;
    }

    /**
     * getDateHeader
     *
     * @param string String
     * @return long
    
     */
    public long getDateHeader( String string )
    {
        return 0L;
    }

    /**
     * getHeader
     *
     * @param string String
     * @return String
    
     */
    public String getHeader( String string )
    {
        return "";
    }

    /**
     * getHeaders
     *
     * @param string String
     * @return Enumeration
    
     */
    public Enumeration getHeaders( String string )
    {
        return null;
    }

    /**
     * getHeaderNames
     *
     * @return Enumeration
    
     */
    public Enumeration getHeaderNames(  )
    {
        return null;
    }

    /**
     * getIntHeader
     *
     * @param string String
     * @return int
    
     */
    public int getIntHeader( String string )
    {
        return 0;
    }

    /**
     * getMethod
     *
     * @return String
    
     */
    public String getMethod(  )
    {
        return "";
    }

    /**
     * getPathInfo
     *
     * @return String
    
     */
    public String getPathInfo(  )
    {
        return "";
    }

    /**
     * getPathTranslated
     *
     * @return String
    
     */
    public String getPathTranslated(  )
    {
        return "";
    }

    /**
     * getContextPath
     *
     * @return String
    
     */
    public String getContextPath(  )
    {
        return "";
    }

    /**
     * getQueryString
     *
     * @return String
    
     */
    public String getQueryString(  )
    {
        return "";
    }

    /**
     * getRemoteUser
     *
     * @return String
    
     */
    public String getRemoteUser(  )
    {
        return "";
    }

    /**
     * isUserInRole
     *
     * @param string String
     * @return boolean
    
     */
    public boolean isUserInRole( String string )
    {
        return false;
    }

    /**
     * getUserPrincipal
     *
     * @return Principal
    
     */
    public Principal getUserPrincipal(  )
    {
        return null;
    }

    /**
     * getRequestedSessionId
     *
     * @return String
    
     */
    public String getRequestedSessionId(  )
    {
        return "";
    }

    /**
     * getRequestURI
     *
     * @return String
    
     */
    public String getRequestURI(  )
    {
        return "";
    }

    /**
     * getRequestURL
     *
     * @return StringBuffer
    
     */
    public StringBuffer getRequestURL(  )
    {
        return null;
    }

    /**
     * getServletPath
     *
     * @return String
    
     */
    public String getServletPath(  )
    {
        return "";
    }

    /**
     * getSession
     *
     * @param boolean0 boolean
     * @return HttpSession
    
     */
    public HttpSession getSession( boolean bCreate )
    {
        if ( _session == null )
        {
            _session = new MokeHttpSession(  );
        }

        return _session;
    }

    /**
     * getSession
     *
     * @return HttpSession
    
     */
    public HttpSession getSession(  )
    {
        return _session;
    }

    /**
     * isRequestedSessionIdValid
     *
     * @return boolean
    
     */
    public boolean isRequestedSessionIdValid(  )
    {
        return false;
    }

    /**
     * isRequestedSessionIdFromCookie
     *
     * @return boolean
    
     */
    public boolean isRequestedSessionIdFromCookie(  )
    {
        return false;
    }

    /**
     * isRequestedSessionIdFromURL
     *
     * @return boolean
    
     */
    public boolean isRequestedSessionIdFromURL(  )
    {
        return false;
    }

    /**
     * isRequestedSessionIdFromUrl
     *
     * @return boolean
    
     */
    public boolean isRequestedSessionIdFromUrl(  )
    {
        return false;
    }

    /**
     * getAttribute
     *
     * @param string String
     * @return Object
     *
     */
    public Object getAttribute( String string )
    {
        return "";
    }

    /**
     * getAttributeNames
     *
     * @return Enumeration
     *
     */
    public Enumeration getAttributeNames(  )
    {
        return null;
    }

    /**
     * getCharacterEncoding
     *
     * @return String
     *
     */
    public String getCharacterEncoding(  )
    {
        return "";
    }

    /**
     * setCharacterEncoding
     *
     * @param string String
     * @throws UnsupportedEncodingException
     *
     */
    public void setCharacterEncoding( String string ) throws UnsupportedEncodingException
    {
    }

    /**
     * getContentLength
     *
     * @return int
     *
     */
    public int getContentLength(  )
    {
        return 0;
    }

    /**
     * getContentType
     *
     * @return String
     *
     */
    public String getContentType(  )
    {
        return "";
    }

    /**
     * getInputStream
     *
     * @throws IOException
     * @return ServletInputStream
     *
     */
    public ServletInputStream getInputStream(  ) throws IOException
    {
        return null;
    }

    /**
     * getParameter
     *
     * @param string String
     * @return String
     *
     */
    public String getParameter( String strParameterName )
    {
        return (String) _mapParameters.get( strParameterName );
    }

    /**
     * getParameterNames
     *
     * @return Enumeration
     *
     */
    public Enumeration getParameterNames(  )
    {
        return new Vector(  ).elements(  );
    }

    /**
     * getParameterValues
     *
     * @param string String
     * @return String[]
     *
     */
    public String[] getParameterValues( String string )
    {
        return null;
    }

    /**
     * getParameterMap
     *
     * @return Map
     *
     */
    public Map getParameterMap(  )
    {
        return null;
    }

    /**
     * getProtocol
     *
     * @return String
     *
     */
    public String getProtocol(  )
    {
        return "";
    }

    /**
     * getScheme
     *
     * @return String
     *
     */
    public String getScheme(  )
    {
        return "";
    }

    /**
     * getServerName
     *
     * @return String
     *
     */
    public String getServerName(  )
    {
        return "";
    }

    /**
     * getServerPort
     *
     * @return int
     *
     */
    public int getServerPort(  )
    {
        return 0;
    }

    /**
     * getReader
     *
     * @throws IOException
     * @return BufferedReader
     *
     */
    public BufferedReader getReader(  ) throws IOException
    {
        return null;
    }

    /**
     * getRemoteAddr
     *
     * @return String
     *
     */
    public String getRemoteAddr(  )
    {
        return "";
    }

    /**
     * getRemoteHost
     *
     * @return String
     *
     */
    public String getRemoteHost(  )
    {
        return "";
    }

    /**
     * setAttribute
     *
     * @param string String
     * @param object Object
     *
     */
    public void setAttribute( String string, Object object )
    {
    }

    /**
     * removeAttribute
     *
     * @param string String
     *
     */
    public void removeAttribute( String string )
    {
    }

    /**
     * getLocale
     *
     * @return Locale
     *
     */
    public Locale getLocale(  )
    {
        return Locale.getDefault();
    }

    /**
     * getLocales
     *
     * @return Enumeration
     *
     */
    public Enumeration getLocales(  )
    {
        return null;
    }

    /**
     * isSecure
     *
     * @return boolean
     *
     */
    public boolean isSecure(  )
    {
        return false;
    }

    /**
     * getRequestDispatcher
     *
     * @param string String
     * @return RequestDispatcher
     *
     */
    public RequestDispatcher getRequestDispatcher( String string )
    {
        return null;
    }

    /**
     * getRealPath
     *
     * @param string String
     * @return String
     *
     */
    public String getRealPath( String string )
    {
        return "";
    }

    ////////////////////////////////////////////////////////////////////////////
    // Initialize moke values

    /**
     * Initialize moke parameters
     * @param map Map
     */
    public void addMokeParameters( String strParameterName, Object value )
    {
        _mapParameters.put( strParameterName, value );
    }

    /**
     * Initialize moke cookies
     * @param cookies Cookie
     */
    public void setMokeCookies( Cookie[] cookies )
    {
        _cookies = cookies;
    }

    public int getRemotePort(  )
    {
        return 80;
    }

    public String getLocalName(  )
    {
        return "";
    }

    public String getLocalAddr(  )
    {
        return "";
    }

    public int getLocalPort(  )
    {
        return 80;
    }
}
