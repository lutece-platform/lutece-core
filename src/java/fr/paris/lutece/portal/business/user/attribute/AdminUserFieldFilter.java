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
package fr.paris.lutece.portal.business.user.attribute;

import fr.paris.lutece.portal.service.user.attribute.AttributeService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * AdminUserFieldFilter
 *
 */
public class AdminUserFieldFilter
{
    // CONSTANTS
    private static final String CONSTANT_ESPERLUETTE = "&";
    private static final String CONSTANT_EQUAL = "=";
    private static final String CONSTANT_UNDERSCORE = "_";
    private static final int ALL_INT = -1;

    // PARAMETERS
    private static final String PARAMETER_SEARCH_IS_SEARCH = "search_is_search";
    private static final String PARAMETER_ATTRIBUTE = "attribute";

    // PROPERTIES
    private static final String PROPERTY_ENCODING_URL = "lutece.encoding.url";
    private List<AdminUserField> _listUserFields;
    private int _nIdUser = ALL_INT;
    private int _nIdAttribute = ALL_INT;
    private int _nIdField = ALL_INT;

    /**
     * Get list user fields
     * @return list user fields
     */
    public List<AdminUserField> getListUserFields(  )
    {
        return _listUserFields;
    }

    /**
     * Set list user fields
     * @param listUserFields list user fields
     */
    public void setListUserFields( List<AdminUserField> listUserFields )
    {
        _listUserFields = listUserFields;
    }

    /**
     * Get id user
     * @return id user
     */
    public int getIdUser(  )
    {
        return _nIdUser;
    }

    /**
     * Set id user
     * @param nIdUser id User
     */
    public void setIdUser( int nIdUser )
    {
        _nIdUser = nIdUser;
    }

    /**
     * Get id attribute
     * @return id attribute
     */
    public int getIdAttribute(  )
    {
        return _nIdAttribute;
    }

    /**
     * Set id attirbute
     * @param nIdAttribute id attribute
     */
    public void setIdAttribute( int nIdAttribute )
    {
        _nIdAttribute = nIdAttribute;
    }

    /**
     * Get id field
     * @return id field
     */
    public int getIdField(  )
    {
        return _nIdField;
    }

    /**
     * Set id field
     * @param nIdField id field
     */
    public void setIdField( int nIdField )
    {
        _nIdField = nIdField;
    }

    /**
     * Check if the filter contains an id attribute
     * @return true if it contains, false otherwise
     */
    public boolean containsIdAttribute(  )
    {
        return ( _nIdAttribute != ALL_INT );
    }

    /**
     * Check if the filter contains an id user
     * @return true if it contains, false otherwise
     */
    public boolean containsIdUser(  )
    {
        return ( _nIdUser != ALL_INT );
    }

    /**
     * Check if the filter contains an id field
     * @return true if it contains, false otherwise
     */
    public boolean containsIdField(  )
    {
        return ( _nIdField != ALL_INT );
    }

    /**
     * Set admin user field filter
     * @param request HttpServletRequest
     * @param locale locale
     */
    public void setAdminUserFieldFilter( HttpServletRequest request, Locale locale )
    {
        _listUserFields = new ArrayList<AdminUserField>(  );

        String strIsSearch = request.getParameter( PARAMETER_SEARCH_IS_SEARCH );

        if ( strIsSearch != null )
        {
            List<IAttribute> listAttributes = AttributeService.getInstance(  ).getAllAttributesWithoutFields( locale );

            for ( IAttribute attribute : listAttributes )
            {
                for ( AdminUserField userField : attribute.getUserFieldsData( request, null ) )
                {
                    if ( ( userField != null ) && StringUtils.isNotBlank( userField.getValue(  ) ) )
                    {
                        _listUserFields.add( userField );
                    }
                }
            }
        }
    }

    /**
    * Build url attributes
    * @param url The url item
    */
    public void setUrlAttributes( UrlItem url )
    {
        for ( AdminUserField userField : _listUserFields )
        {
            try
            {
                url.addParameter( PARAMETER_ATTRIBUTE + CONSTANT_UNDERSCORE +
                    userField.getAttribute(  ).getIdAttribute(  ),
                    URLEncoder.encode( userField.getValue(  ), AppPropertiesService.getProperty( PROPERTY_ENCODING_URL ) ) );
            }
            catch ( UnsupportedEncodingException e )
            {
                AppLogService.error( e.getMessage(  ), e );
            }
        }
    }

    /**
     * Build url attributes
     * @return the url attributes
     */
    public String getUrlAttributes(  )
    {
        StringBuilder sbUrlAttributes = new StringBuilder(  );

        for ( AdminUserField userField : _listUserFields )
        {
            try
            {
                sbUrlAttributes.append( CONSTANT_ESPERLUETTE + PARAMETER_ATTRIBUTE + CONSTANT_UNDERSCORE +
                    userField.getAttribute(  ).getIdAttribute(  ) + CONSTANT_EQUAL +
                    URLEncoder.encode( userField.getValue(  ), AppPropertiesService.getProperty( PROPERTY_ENCODING_URL ) ) );
            }
            catch ( UnsupportedEncodingException e )
            {
                AppLogService.error( e.getMessage(  ), e );
            }
        }

        return sbUrlAttributes.toString(  );
    }
}
