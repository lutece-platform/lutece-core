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
package fr.paris.lutece.portal.web.xpages;

import fr.paris.lutece.util.bean.BeanUtil;
import fr.paris.lutece.util.beanvalidation.BeanValidationUtil;
import fr.paris.lutece.util.beanvalidation.ValidationError;
import fr.paris.lutece.util.beanvalidation.ValidationErrorConfig;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import javax.validation.ConstraintViolation;


/**
 * Abstract XPageApplication provides
 *
 */
public abstract class AbstractXPageApplication implements XPageApplication
{
    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = -6298306432030274941L;

    /**
     * Populate a bean using parameters in http request
     * @param bean bean to populate
     * @param request http request
     */
    protected void populate( Object bean, HttpServletRequest request )
    {
        BeanUtil.populate( bean, request );
    }

    /**
     * Validates a bean.
     *
     * @param <T> the bean type
     * @param bean the bean to validate
     * @return the sets of constraints that has been violated
     */
    protected <T> Set<ConstraintViolation<T>> validate( T bean )
    {
        return BeanValidationUtil.validate( bean );
    }

    /**
     * Validates a bean
     * @param <T> The bean type
     * @param bean The bean to validate
     * @param strFieldsKeyPrefix The fields keys prefix in resources files
     * @param locale The locale
     * @return The error list
     */
    protected <T> List<ValidationError> validate( T bean, String strFieldsKeyPrefix, Locale locale )
    {
        return BeanValidationUtil.validate( bean, locale, strFieldsKeyPrefix );
    }

    /**
     * Validates a bean
     * @param <T> The bean type
     * @param bean The bean to validate
     * @param config The config for Error validation rendering
     * @param locale The locale
     * @return The error list
     */
    protected <T> List<ValidationError> validate( T bean, ValidationErrorConfig config, Locale locale )
    {
        return BeanValidationUtil.validate( bean, locale, config );
    }
}
