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

import fr.paris.lutece.util.ErrorMessage;

import java.text.MessageFormat;

import java.util.Locale;

import javax.validation.ConstraintViolation;


/**
 * ValidationError
 */
public class ValidationError implements ErrorMessage
{
    private Locale _locale;
    private ConstraintViolation _constraintViolation;
    private ValidationErrorConfig _config;

    /**
     * Constructor
     * @param cv The constraint violation
     * @param locale  The locale
     * @param config The config
     */
    public ValidationError( ConstraintViolation cv, Locale locale, ValidationErrorConfig config )
    {
        _constraintViolation = cv;
        _locale = locale;
        _config = config;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getMessage(  )
    {
        String strMessage = _constraintViolation.getMessage(  );
        String strValue1 = ValidationErrorUtil.getValue1( _constraintViolation, _config );
        String strValue2 = ValidationErrorUtil.getValue2( _constraintViolation, _config );
        String strFieldname = ValidationErrorUtil.getFieldname( _constraintViolation, _config, _locale );

        strMessage = MessageFormat.format( strMessage, strFieldname, strValue1, strValue2,
                _constraintViolation.getInvalidValue(  ) );

        return strMessage;
    }
}
