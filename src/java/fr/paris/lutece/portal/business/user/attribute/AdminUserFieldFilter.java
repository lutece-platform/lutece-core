/*
 * Copyright (c) 2002-2008, Mairie de Paris
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

import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.url.UrlItem;

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
    private static final String EMPTY_STRING = "";
    private static final String CONSTANT_ESPERLUETTE = "&";
    private static final String CONSTANT_EQUAL = "=";
    private static final String CONSTANT_UNDERSCORE = "_";

    // PARAMETERS
    private static final String PARAMETER_SEARCH_IS_SEARCH = "search_is_search";
    private static final String PARAMETER_ATTRIBUTE = "attribute";

    // PROPERTIES
    private static final String PROPERTY_ENCODING_URL = "lutece.encoding.url";
    private List<AdminUserField> _listUserFields;

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
            List<IAttribute> listAttributes = AttributeHome.findAll( locale );

            for ( IAttribute attribute : listAttributes )
            {
                for ( AdminUserField userField : attribute.getUserFieldsData( request, null ) )
                {
                    if ( ( userField != null ) && !userField.getValue(  ).equals( EMPTY_STRING ) )
                    {
                        _listUserFields.add( userField );
                    }
                }
            }
        }
    }

    /**
    * Build url attributes
    * @param the url
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
                e.printStackTrace(  );
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
                e.printStackTrace(  );
            }
        }

        return sbUrlAttributes.toString(  );
    }
}
