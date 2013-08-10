/*
 * Copyright (c) 2002-2013, Mairie de Paris
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
import static fr.paris.lutece.portal.service.i18n.I18nService.getLocalizedString;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintViolation;

/**
 * ValidationError
 */
public class ValidationError
{
    private static final Pattern PATTERN_LOCALIZED_KEY = Pattern.compile( "#i18n\\{(.*?)\\}" );
    private static final String VALUE1 = "value,regexp,min";
    private static final String VALUE2 = "max";
    private static final String[] PREFIX  = { "_str" , "_n" , "_l" , "_" };

    private Locale _locale;
    private ConstraintViolation _constraintViolation;
    private String _strFieldKeysPrefix;
    
    /**
     * Constructor
     * @param cv The constraint violation
     * @param locale  The locale
     */
    public ValidationError( ConstraintViolation cv , Locale locale , String strFieldKeysPrefix )
    {
        _constraintViolation = cv;
        _locale = locale;
        _strFieldKeysPrefix = strFieldKeysPrefix;
    }
    
    /**
     * Return the error message
     * @return The error message
     */
    public String getMessage()
    {
        String strMessage = _constraintViolation.getMessage();
        Matcher matcher = PATTERN_LOCALIZED_KEY.matcher( strMessage );

        if ( matcher.find(  ) )
        {
            StringBuffer sb = new StringBuffer(  );

            do
            {
                matcher.appendReplacement( sb, getLocalizedString( matcher.group( 1 ), _locale ) );
            }
            while ( matcher.find(  ) );

            matcher.appendTail( sb );
            strMessage = sb.toString(  );
        }
        
        String strValue1 = "";
        String strValue2 = "";
        Map<String,Object> mapAttributes = _constraintViolation.getConstraintDescriptor().getAttributes();
        for( String strKey : mapAttributes.keySet() )
        {
            if( VALUE1.contains(strKey))
            {
                strValue1 = getValue( mapAttributes.get(strKey));
            }
            else if( VALUE2.contains(strKey))
            {
                strValue2 = getValue( mapAttributes.get(strKey));
            }
        }
        String strFieldname = "";
        
        if( _strFieldKeysPrefix != null )
        {
            strFieldname = getFieldname();
        }
        strMessage = MessageFormat.format( strMessage, strFieldname, strValue1 , strValue2 , _constraintViolation.getInvalidValue() );
        
        return strMessage;
       
    }
    
    
    private String getFieldname( )
    {
        String strField = _constraintViolation.getPropertyPath().toString();
        
        // remove the variable prefix
        for( int i = 0 ; i < PREFIX.length ; i++ )
        {
            strField = ( strField.startsWith( PREFIX[i] ))? strField.substring( PREFIX[i].length()) : strField;
        }
        
        // set first letter in lower case
        strField = strField.substring( 0 , 1 ).toLowerCase() + strField.substring( 1 );
        String strKey = _strFieldKeysPrefix + strField;
        
        String strFieldName = I18nService.getLocalizedString( strKey , _locale );
        
        // if the key isn't found
        if( strFieldName.equals( "" ))
        {
            // display the missing key as the field name
            strFieldName = "[" + strKey + "]";
        }
        return strFieldName;
    }

    /**
     * Convert an unkown type value to a String value
     * @param value The valus
     * @return The value as a String
     */
    private String getValue(Object value)
    {
        if( value instanceof Integer )
        {
            return Integer.toString( (Integer) value );
        }
        if( value instanceof Long )
        {
            return Long.toString( (Long) value );
        }
        return (String) value;
    }
}
