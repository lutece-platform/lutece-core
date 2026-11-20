/*
 * Copyright (c) 2002-2026, City of Paris
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
package fr.paris.lutece.portal.service.template;

import java.util.HashMap;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.test.LuteceTestCase;

/**
 * Front office macro cInputPassword: strength meter and generator
 */
public class CInputPasswordTemplateTest extends LuteceTestCase
{
    // change password: the first field has no meter, the second one has
    private static final String SNIPPET = "<@cInputPassword name='old_password' passwordMeter=false />"
            + "<@cInputPassword name='new_password' id='new_password' passwordMeter=true pmConfirmFieldId='confirmation_password' />";

    private static String render( Locale locale )
    {
        return AppTemplateService.getTemplateFromStringFtl( SNIPPET, locale, new HashMap<String, Object>( ) ).getHtml( );
    }

    /**
     * The meter, the generator and the functions they use are written for a metered field that is not the first one of the page
     */
    @Test
    public void testMeterOfTheSecondField( )
    {
        String strHtml = render( Locale.ENGLISH );

        assertTrue( strHtml.contains( "password-new_password-wrapper" ), strHtml );
        assertTrue( strHtml.contains( "function passwordStrength" ), strHtml );
        assertTrue( strHtml.contains( "passwordStrength( 'new_password'" ), strHtml );
    }

    /**
     * The strength is computed on input, with the labels of the language of the page
     */
    @Test
    public void testStrengthLabelsAndEvent( )
    {
        String strHtml = render( Locale.ENGLISH );

        assertTrue( strHtml.contains( "addEventListener(\"input\"" ), strHtml );
        assertFalse( strHtml.contains( "\"Faible\"" ), strHtml );
        assertTrue( strHtml.contains( "\"Weak\"" ), strHtml );
        assertTrue( render( Locale.FRENCH ).contains( "\"Faible\"" ) );
    }
}
