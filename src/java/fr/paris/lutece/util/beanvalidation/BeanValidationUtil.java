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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


/**
 * BeanValidationUtils validates beans using JSR303 annotations.
 *
 * @see #validate(Object)
 */
public final class BeanValidationUtil
{
    /**
     * Validator (JSR 303) is thread safe.
     */
    private static final Validator VALIDATOR;

    static
    {
        // initialize the validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory(  );
        VALIDATOR = factory.getValidator(  );
    }

    /**
     * Utility class
     */
    private BeanValidationUtil(  )
    {
        // nothing
    }

    /**
     * Validates a bean.
     *
     * @param <T> the bean type
     * @param bean the bean to validate
     * @return the sets the
     */
    public static <T> Set<ConstraintViolation<T>> validate( T bean )
    {
        return VALIDATOR.validate( bean );
    }

    /**
     * Use this in case you need more than a global validation
     *
     * @return the validator
     */
    public static Validator getValidator(  )
    {
        return VALIDATOR;
    }

    /**
     * Validate a bean
     * @param <T> The bean class
     * @param bean The bean
     * @param locale The locale
     * @param strFieldKeysPrefix The fields keys prefix in the resource file
     * @return A list of errors
     */
    public static <T> List<ValidationError> validate( T bean, Locale locale, String strFieldKeysPrefix )
    {
        DefaultValidationErrorConfig config = new DefaultValidationErrorConfig(  );
        config.setFieldKeysPrefix( strFieldKeysPrefix );

        return validate( bean, locale, config );
    }

    /**
     * Validate a bean
     * @param <T> The bean class
     * @param bean The bean
     * @param locale The locale
     * @param config  The config for validation errors rendering
     * @return A list of errors
     */
    public static <T> List<ValidationError> validate( T bean, Locale locale, ValidationErrorConfig config )
    {
        List<ValidationError> list = new ArrayList<ValidationError>(  );
        Set<ConstraintViolation<T>> setViolation = validate( bean );

        for ( ConstraintViolation<T> cv : setViolation )
        {
            list.add( new ValidationError( cv, locale, config ) );
        }

        return list;
    }
}
