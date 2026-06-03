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
	    assertTrue( result.equals( strExpectedResult ) );
	} 
	catch ( XSSSanitizerException e )
	{
	    fail( e.getLocalizedMessage ( ) );
	}       
    }


}
