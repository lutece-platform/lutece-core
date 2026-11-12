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
package fr.paris.lutece.portal.web.includes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.test.LuteceTestCase;

/**
 * The optional parts of the default page frameset are named as the core and the themes provide them
 */
public class PageFramesetPartsTest extends LuteceTestCase
{
    private static final Path SITE_TEMPLATES = Paths.get( "webapp/WEB-INF/templates/skin/site" );
    private static final Pattern FRAMESET_PART = Pattern.compile( "get_optional_template\\('([^'.][^']*frameset_[^']*)'\\)" );

    @Test
    public void testEveryFramesetPartExists( ) throws IOException
    {
        String strFrameset = new String( Files.readAllBytes( SITE_TEMPLATES.resolve( "page_frameset.html" ) ), StandardCharsets.UTF_8 );
        Matcher matcher = FRAMESET_PART.matcher( strFrameset );
        int nParts = 0;

        while ( matcher.find( ) )
        {
            nParts++;
            assertTrue( Files.exists( SITE_TEMPLATES.resolve( matcher.group( 1 ) ) ), matcher.group( 1 ) );
        }

        assertTrue( nParts > 5 );
    }
}
