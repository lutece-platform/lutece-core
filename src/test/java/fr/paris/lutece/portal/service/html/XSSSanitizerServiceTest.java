/*
 * Copyright (c) 2002-2025, City of Paris
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
package fr.paris.lutece.portal.service.html;

import fr.paris.lutece.test.LuteceTestCase;

/**
 * HtmlCleanerService Test Class
 */
public class XSSSanitizerServiceTest extends LuteceTestCase
{
    /**
     * Test of clean method, of class fr.paris.lutece.portal.service.html.HtmlCleanerService.
     * 
     * @throws HtmlCleanerException
     *             if there is an exception during the test
     */
    public void testClean( )
    {
	String strHtmlsuspicious = "<p>text<img src=\"2.gif\" onerror=\"alert(123);\"/><script>alert(123);</script></p>";
        String strExpectedResult = "<p>text<img src=\"2.gif\" /></p>";

        // test
        testClean( strHtmlsuspicious, strExpectedResult );
    }

    /**
     * The id of a rich text content is kept with the prefix user-content-, and a link to an anchor of the content follows
     * it, so that the anchors written by the authors keep working while an id of the content can never take the name of
     * an element or a global of the page (LUT-34350). An id that is not a plain identifier is removed.
     */
    public void testIdAttribute( )
    {
        testClean( "<h2 id=\"intro\">Intro</h2><p><a href=\"#intro\">Back</a></p>",
                "<h2 id=\"user-content-intro\">Intro</h2><p><a href=\"#user-content-intro\" rel=\"nofollow noopener noreferrer\">Back</a></p>" );
        // a content sanitized twice keeps a single prefix
        testClean( "<p id=\"user-content-intro\">x</p>", "<p id=\"user-content-intro\">x</p>" );
        // ids of the page cannot be shadowed
        testClean( "<form id=\"token\"></form><p id=\"token\">x</p>", "<p id=\"user-content-token\">x</p>" );
        testClean( "<p id=\"bad id\">x</p><p id=\"1st\">y</p>", "<p>x</p><p>y</p>" );
        // other links are left to the URL policy
        testClean( "<a href=\"https://www.paris.fr/#top\">Paris</a>", "<a href=\"https://www.paris.fr/#top\" rel=\"nofollow noopener noreferrer\">Paris</a>" );
    }

    /**
     * Test of clean method, of class fr.paris.lutece.portal.service.html.HtmlCleanerService.
     * 
     * @param strSource
     *            the source String to test
     * @param strExpectedResult
     *            the expected result
     * @throws HtmlCleanerException
     *             if there is an exception during the test
     */
    private void testClean( String strSource, String strExpectedResult )
    {
        String result ;
        
	try
	{
	    result = XSSSanitizerService.sanitize( strSource );
	    assertNotNull( result );
	    assertEquals( strExpectedResult, result );
	} 
	catch ( XSSSanitizerException e )
	{
	    fail( e.getLocalizedMessage ( ) );
	}       
    }


}
