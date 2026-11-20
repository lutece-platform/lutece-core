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
package fr.paris.lutece.util.mail;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * HtmlDocument Test Class
 */
public class HtmlDocumentTest
{
    private static final String BASE_URL = "http://localhost:8080/lutece/";
    private static final String HTML = "<html><head><link rel=\"stylesheet\" href=\"" + BASE_URL + "css/page.css\"><link href=\"" + BASE_URL
            + "css/norel.css\"><script type=\"text/javascript\" src=\"" + BASE_URL + "js/page.js\"></script></head><body><p>Newsletter</p><img src=\"" + BASE_URL
            + "images/logo.png\" alt=\"logo\"><img src=\"https://other.example/image.png\" alt=\"other\"></body></html>";

    /**
     * The images, style sheets and scripts served by the site are found (multipart mails)
     */
    @Test
    public void testGetAllUrlsAttachement( )
    {
        HtmlDocument document = new HtmlDocument( HTML, BASE_URL, true );

        List<UrlAttachment> listImages = document.getAllUrlsAttachement( HtmlDocument.ELEMENT_IMG );
        assertEquals( 1, listImages.size( ) );
        assertEquals( BASE_URL + "images/logo.png", listImages.get( 0 ).getContentLocation( ) );

        assertEquals( 1, document.getAllUrlsAttachement( HtmlDocument.ELEMENT_CSS ).size( ) );
        assertEquals( 1, document.getAllUrlsAttachement( HtmlDocument.ELEMENT_JAVASCRIPT ).size( ) );
    }

    /**
     * With relative urls, the content location is the path of the url
     */
    @Test
    public void testRelativeContentLocation( )
    {
        HtmlDocument document = new HtmlDocument( HTML, BASE_URL, false );

        List<UrlAttachment> listImages = document.getAllUrlsAttachement( HtmlDocument.ELEMENT_IMG );
        assertEquals( 1, listImages.size( ) );
        assertEquals( "/lutece/images/logo.png", listImages.get( 0 ).getContentLocation( ) );
    }
}
