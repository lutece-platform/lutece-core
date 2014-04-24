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

import static org.junit.Assert.*;

import org.junit.Test;

import java.math.BigDecimal;

import java.sql.Date;

import java.util.List;
import java.util.Locale;

import javax.validation.Validator;


/**
 * BeanValidationUtilTest
 */
public class BeanValidationUtilTest
{
    /**
     * Test of validate method, of class BeanValidationUtil.
     */
    @Test
    public void testValidate_GenericType_Locale(  )
    {
        System.out.println( "validate" );

        Locale locale = Locale.FRENCH;

        Bean[] beans = { new BeanDefaultMessages(  ), new BeanLuteceMessages(  ) };

        for ( int i = 0; i < beans.length; i++ )
        {
            Bean bean = beans[i];
            bean.setName( "contains-invalid-char-1" );
            bean.setDescription( "too-short" );
            bean.setEmail( "invalid-email" );

            long lTimeNow = new java.util.Date(  ).getTime(  );
            bean.setDateBirth( new Date( lTimeNow + 1000000000L ) );
            bean.setDateEndOfWorld( new Date( lTimeNow - 1000000000L ) );

            bean.setSalary( new BigDecimal( "100.00" ) );
            bean.setPercent( new BigDecimal( "200.00" ) );
            bean.setCurrency( "150.9999" );
            bean.setUrl( "invalid-url" );

            List<ValidationError> list = BeanValidationUtil.validate( bean, locale, "fields_prefix." );
            assertTrue( list.size(  ) > 0 );

            for ( ValidationError error : list )
            {
                System.out.println( error.getMessage(  ) );
            }
        }
    }
}
