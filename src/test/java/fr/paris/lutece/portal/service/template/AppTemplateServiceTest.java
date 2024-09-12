/*
 * Copyright (c) 2002-2022, City of Paris
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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.template.CommonsInclude;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.html.HtmlTemplate;
import freemarker.template.TemplateException;

/**
 *
 * @author Pierre
 */
public class AppTemplateServiceTest extends LuteceTestCase
{

    private static final String REFERENCE_TEMPLATE = "reference.html";
    final private static String TEST_TEMPLATES_PATH = "commons" + File.separator + "templates" + File.separator + "test";

    @Test
    public void testCommonsTemplates( ) throws IOException, TemplateException, URISyntaxException
    {

        URL resourceUrl = getClass( ).getClassLoader( ).getResource( TEST_TEMPLATES_PATH );

        if ( resourceUrl == null ) {
            fail( "Could not find the test templates path." );
        }

        Path testTemplatesPath = Paths.get( resourceUrl.toURI( ) );

        for ( CommonsInclude ci : CommonsService.getCommonsIncludes( ) )
        {
            try
            {
                CommonsService.activateCommons( ci.getKey( ) );
                String ciKey = ci.getKey( );
                Map<String, Object> model = new HashMap<>( );

                AppTemplateService.resetCache( );

                String strReferenceTemplate = readFile( testTemplatesPath.resolve( REFERENCE_TEMPLATE ).toString( ), StandardCharsets.UTF_8 );
                HtmlTemplate generated_template = AppTemplateService.getTemplateFromStringFtl( strReferenceTemplate, LocaleService.getDefault( ), model );

                org.junit.jupiter.api.Assertions.assertNotNull( generated_template.getHtml( ), "AppTemplateServiceTest freemarker lib :  " + ciKey );
            }
            catch( IOException e )
            {
                fail( e.getMessage( ) );
            }
        }

    }

    /**
     * read file
     *
     * @param path
     * @param encoding
     * @return the file as string
     * @throws IOException
     */
    static String readFile( String path, Charset encoding ) throws IOException
    {
        byte [ ] encoded = Files.readAllBytes( Paths.get( path ) );
        return new String( encoded, encoding );
    }

}
