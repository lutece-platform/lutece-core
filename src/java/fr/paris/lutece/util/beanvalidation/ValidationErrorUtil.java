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
package fr.paris.lutece.util.beanvalidation;

import fr.paris.lutece.portal.service.i18n.I18nService;

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.ConstraintViolation;


/**
 * ValidationError Utils
 */
public final class ValidationErrorUtil
{
    /**
     * Private constructor
     */
    private ValidationErrorUtil(  )
    {
        // Nothing to do
    }

    /**
     * Return the attribute's value to set as the value#1 of the message
     * @param constraintViolation The Constraint violation
     * @param config The config
     * @return The value
     */
    public static String getValue1( ConstraintViolation constraintViolation, ValidationErrorConfig config )
    {
        return getValue( constraintViolation, config, config.getValue1Attributes(  ) );
    }

    /**
     * Return the attribute's value to set as the value#2 of the message
     * @param constraintViolation The Constraint violation
     * @param config The config
     * @return The value
     */
    public static String getValue2( ConstraintViolation constraintViolation, ValidationErrorConfig config )
    {
        return getValue( constraintViolation, config, config.getValue2Attributes(  ) );
    }

    /**
     * Return the field name as it will appear in the message
     * @param constraintViolation The Constraint violation
     * @param config The config
     * @param locale The locale
     * @return The field name
     */
    public static String getFieldname( ConstraintViolation constraintViolation, ValidationErrorConfig config,
        Locale locale )
    {
        String strField = constraintViolation.getPropertyPath(  ).toString(  );

        // remove the variable prefix
        String[] prefix = config.getVariablesPrefix(  );

        for ( int i = 0; i < prefix.length; i++ )
        {
            strField = removePrefix( strField, prefix[i] );
        }

        // set first letter in lower case
        strField = strField.substring( 0, 1 ).toLowerCase(  ) + strField.substring( 1 );

        String strKey = config.getFieldKeysPrefix(  ) + strField;

        String strFieldName = I18nService.getLocalizedString( strKey, locale );

        // if the key isn't found
        if ( strFieldName.equals( "" ) )
        {
            // display the missing key as the field name
            strFieldName = "[" + strKey + "]";
        }

        strFieldName = config.getFieldWrapperBegin(  ) + strFieldName + config.getFieldWrapperEnd(  );

        return strFieldName;
    }

    /**
     * Remove the variable prefix
     * @param strSource The source
     * @param strPrefix The prefix
     * @return The string with the prefix removed
     */
    private static String removePrefix( String strSource, String strPrefix )
    {
        String strReturn = strSource;

        if ( strSource.startsWith( strPrefix ) )
        {
            strReturn = strSource.substring( strPrefix.length(  ) );
        }

        return strReturn;
    }

    /**
     * Return the attribute's value to set as value the of the message
     * @param constraintViolation The Constraint violation
     * @param config The config
     * @param strAttributes The attributes names list
     * @return The value
     */
    private static String getValue( ConstraintViolation constraintViolation, ValidationErrorConfig config,
        String strAttributes )
    {
        String strValue = "";

        Map<String, Object> mapAttributes = constraintViolation.getConstraintDescriptor(  ).getAttributes(  );

        for ( Entry<String, Object> entry : mapAttributes.entrySet(  ) )
        {
            if ( strAttributes.contains( entry.getKey(  ) ) )
            {
                strValue = getValue( entry.getValue(  ) );
            }
        }

        return strValue;
    }

    /**
     * Convert an unkown type value to a String value
     * @param value The valus
     * @return The value as a String
     */
    private static String getValue( Object value )
    {
        if ( value instanceof Integer )
        {
            return Integer.toString( (Integer) value );
        }

        if ( value instanceof Long )
        {
            return Long.toString( (Long) value );
        }

        return (String) value;
    }
}
