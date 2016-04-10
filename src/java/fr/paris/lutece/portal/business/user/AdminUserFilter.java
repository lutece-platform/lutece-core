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
package fr.paris.lutece.portal.business.user;

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides a filter for users search function
 */
public class AdminUserFilter implements Serializable
{
    private static final long serialVersionUID = 4791880812924591795L;

    // Constants
    private static final String CONSTANT_LEAST_ONE = "-1";
    private static final String CONSTANT_DEFAULT_LEVEL = "noValue";
    private static final String CONSTANT_EQUAL = "=";
    private static final String CONSTANT_AMPERSAND = "&";

    // Parameteres
    private static final String PARAMETER_SEARCH_ACCESS_CODE = "search_access_code";
    private static final String PARAMETER_SEARCH_LAST_NAME = "search_last_name";
    private static final String PARAMETER_SEARCH_FIRST_NAME = "search_first_name";
    private static final String PARAMETER_SEARCH_EMAIL = "search_email";
    private static final String PARAMETER_SEARCH_STATUS = "search_status";
    private static final String PARAMETER_SEARCH_USER_LEVEL = "search_user_level";
    private static final String PARAMETER_SEARCH_IS_SEARCH = "search_is_search";

    // Properties
    private static final String PROPERTY_ENCODING_URL = "lutece.encoding.url";
    private String _strAccessCode;
    private String _strLastName;
    private String _strFirstName;
    private String _strEmail;
    private int _nStatus;
    private int _nUserLevel;

    /**
    * Constructor
    */
    public AdminUserFilter(  )
    {
    }

    /**
     * Initialize each component of the object
     */
    public void init(  )
    {
        _strAccessCode = StringUtils.EMPTY;
        _strLastName = StringUtils.EMPTY;
        _strFirstName = StringUtils.EMPTY;
        _strEmail = StringUtils.EMPTY;
        _nStatus = -1;
        _nUserLevel = -1;
    }

    /**
     * Get the access code
     * @return The access code
     */
    public String getAccessCode(  )
    {
        return _strAccessCode;
    }

    /**
     * Set the access code
     * @param strAccessCode The Access Code
     */
    public void setAccessCode( String strAccessCode )
    {
        _strAccessCode = strAccessCode;
    }

    /**
     * Get the last name
     * @return The last name
     */
    public String getLastName(  )
    {
        return _strLastName;
    }

    /**
     * Set the last name
     * @param strLastName The Last Name
     */
    public void setLastName( String strLastName )
    {
        _strLastName = strLastName;
    }

    /**
     * Get the email
     * @return The email
     */
    public String getEmail(  )
    {
        return _strEmail;
    }

    /**
     * Set the email
     * @param strEmail The email
     */
    public void setEmail( String strEmail )
    {
        _strEmail = strEmail;
    }

    /**
     * Get the status
     * @return The status level
     */
    public int getStatus(  )
    {
        return _nStatus;
    }

    /**
     * Set the status
     * @param nStatus The Status
     */
    public void setStatus( int nStatus )
    {
        _nStatus = nStatus;
    }

    /**
     * Get the user level
     * @return The user level
     */
    public int getUserLevel(  )
    {
        return _nUserLevel;
    }

    /**
     * Set the user level
     * @param nUserLevel The User Level
     */
    public void setUserLevel( int nUserLevel )
    {
        _nUserLevel = nUserLevel;
    }

    /**
     * Set the value of the AdminUserFilter
     * @param request HttpServletRequest
     * @return true if there is a search
     */
    public boolean setAdminUserFilter( HttpServletRequest request )
    {
        boolean bIsSearch = false;
        String strIsSearch = request.getParameter( PARAMETER_SEARCH_IS_SEARCH );

        if ( strIsSearch != null )
        {
            bIsSearch = true;
            _strAccessCode = request.getParameter( PARAMETER_SEARCH_ACCESS_CODE );
            _strLastName = request.getParameter( PARAMETER_SEARCH_LAST_NAME );
            _strEmail = request.getParameter( PARAMETER_SEARCH_EMAIL );

            String strStatus = request.getParameter( PARAMETER_SEARCH_STATUS );
            String strUserLevel = request.getParameter( PARAMETER_SEARCH_USER_LEVEL );

            if ( CONSTANT_DEFAULT_LEVEL.equals( strStatus ) || CONSTANT_LEAST_ONE.equals( strStatus ) )
            {
                setStatus( -1 );
            }
            else
            {
                setStatus( Integer.valueOf( strStatus ) );
            }

            if ( (strUserLevel == null) || CONSTANT_DEFAULT_LEVEL.equals( strUserLevel ) || CONSTANT_LEAST_ONE.equals( strUserLevel ) )
            {
                setUserLevel( -1 );
            }
            else
            {
                setUserLevel( Integer.valueOf( strUserLevel ) );
            }
        }
        else
        {
            init(  );
        }

        return bIsSearch;
    }

    /**
     * Build url attributes
     * @param url The url item
     */
    public void setUrlAttributes( UrlItem url )
    {
        url.addParameter( PARAMETER_SEARCH_IS_SEARCH, Boolean.TRUE.toString(  ) );
        url.addParameter( PARAMETER_SEARCH_USER_LEVEL, _nUserLevel );
        url.addParameter( PARAMETER_SEARCH_STATUS, _nStatus );

        try
        {
            url.addParameter( PARAMETER_SEARCH_ACCESS_CODE,
                URLEncoder.encode( _strAccessCode, AppPropertiesService.getProperty( PROPERTY_ENCODING_URL ) ) );
            url.addParameter( PARAMETER_SEARCH_LAST_NAME,
                URLEncoder.encode( _strLastName, AppPropertiesService.getProperty( PROPERTY_ENCODING_URL ) ) );
            url.addParameter( PARAMETER_SEARCH_EMAIL,
                URLEncoder.encode( _strEmail, AppPropertiesService.getProperty( PROPERTY_ENCODING_URL ) ) );
        }
        catch ( UnsupportedEncodingException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
    }

    /**
     * Build url attributes
     * @return the url attributes
     */
    public String getUrlAttributes(  )
    {
        StringBuilder sbUrlAttributes = new StringBuilder(  );
        sbUrlAttributes.append( PARAMETER_SEARCH_IS_SEARCH + CONSTANT_EQUAL + Boolean.TRUE );
        sbUrlAttributes.append( CONSTANT_AMPERSAND + PARAMETER_SEARCH_USER_LEVEL + CONSTANT_EQUAL + _nUserLevel );
        sbUrlAttributes.append( CONSTANT_AMPERSAND + PARAMETER_SEARCH_STATUS + CONSTANT_EQUAL + _nStatus );

        try
        {
            sbUrlAttributes.append( CONSTANT_AMPERSAND + PARAMETER_SEARCH_ACCESS_CODE + CONSTANT_EQUAL +
                URLEncoder.encode( _strAccessCode, AppPropertiesService.getProperty( PROPERTY_ENCODING_URL ) ) );
            sbUrlAttributes.append( CONSTANT_AMPERSAND + PARAMETER_SEARCH_LAST_NAME + CONSTANT_EQUAL +
                URLEncoder.encode( _strLastName, AppPropertiesService.getProperty( PROPERTY_ENCODING_URL ) ) );
            sbUrlAttributes.append( CONSTANT_AMPERSAND + PARAMETER_SEARCH_EMAIL + CONSTANT_EQUAL +
                URLEncoder.encode( _strEmail, AppPropertiesService.getProperty( PROPERTY_ENCODING_URL ) ) );
        }
        catch ( UnsupportedEncodingException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }

        return sbUrlAttributes.toString(  );
    }
}
