/*
 * Copyright (c) 2002-2017, Mairie de Paris
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

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.html.HtmlTemplate;

public class AbstractMessageFormatTemplateMethodTest extends LuteceTestCase
{

    private static final class TestAbstractMessageFormatTemplateMethod extends AbstractMessageFormatTemplateMethod
    {
        private final String strKey;

        public TestAbstractMessageFormatTemplateMethod( String key )
        {
            strKey = key;
        }

        @Override
        protected String getPattern( String key, Locale locale )
        {
            return strKey;
        }
    }

    public void testExec( )
    {
        AbstractMessageFormatTemplateMethod method = new TestAbstractMessageFormatTemplateMethod( "test with 'quote'" );
        Map<Object, Object> model = new HashMap<>( );
        model.put( "method", method );
        String template = "${method(\"key\")}";
        HtmlTemplate res = FreeMarkerTemplateService.getInstance( ).loadTemplate( template, Locale.FRANCE, model );
        assertNotNull( res );
        assertEquals( "test with 'quote'", res.getHtml( ) );

        template = "${method(\"key\",\"arg\")}";
        res = FreeMarkerTemplateService.getInstance( ).loadTemplate( template, Locale.FRANCE, model );
        assertNotNull( res );
        assertEquals( "test with quote", res.getHtml( ) );

        method = new TestAbstractMessageFormatTemplateMethod( "test with 'quote' and arg {0}" );
        model.put( "method", method );
        template = "${method(\"key\",\"arg\")}";
        res = FreeMarkerTemplateService.getInstance( ).loadTemplate( template, Locale.FRANCE, model );
        assertNotNull( res );
        assertEquals( "test with quote and arg arg", res.getHtml( ) );

        template = "${method(\"key\",arg)}";
        model.put( "arg", "arg" );
        res = FreeMarkerTemplateService.getInstance( ).loadTemplate( template, Locale.FRANCE, model );
        assertNotNull( res );
        assertEquals( "test with quote and arg arg", res.getHtml( ) );

        model.put( "arg", 10 );
        res = FreeMarkerTemplateService.getInstance( ).loadTemplate( template, Locale.FRANCE, model );
        assertNotNull( res );
        assertEquals( "test with quote and arg 10", res.getHtml( ) );

        model.put( "arg", new Date( 123456789 ) );
        res = FreeMarkerTemplateService.getInstance( ).loadTemplate( template, Locale.FRANCE, model );
        assertNotNull( res );
        assertEquals( "test with quote and arg 02/01/70 11:17", res.getHtml( ) );

        model.put( "arg", new Date( 123456789 ) );
        res = FreeMarkerTemplateService.getInstance( ).loadTemplate( template, Locale.US, model );
        assertNotNull( res );
        assertEquals( "test with quote and arg 1/2/70 11:17 AM", res.getHtml( ) );
    }

}
