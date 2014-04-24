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
package fr.paris.lutece.util.string;

import fr.paris.lutece.test.LuteceTestCase;


/**
 * StringUtil Test Class
 *
 */
public class StringUtilTest extends LuteceTestCase
{
    /**
     * Test of substitute method, of class fr.paris.lutece.util.string.StringUtil.
     */
    public void testSubstitute(  )
    {
        System.out.println( "substitute" );

        String strSource = "Hello, @name@";
        String strValue = "World";
        String strBookmark = "@name@";

        String expResult = "Hello, World";
        String result = fr.paris.lutece.util.string.StringUtil.substitute( strSource, strValue, strBookmark );
        assertEquals( expResult, result );
    }

    /**
     * Test of replaceAccent method, of class fr.paris.lutece.util.string.StringUtil.
     */
    public void testReplaceAccent(  )
    {
        System.out.println( "replaceAccent" );

        String strInit = "Hello, World";

        String expResult = "Hello, World";
        String result = fr.paris.lutece.util.string.StringUtil.replaceAccent( strInit );
        assertEquals( expResult, result );
    }

    /**
     * Test of containsHtmlSpecialCharacters method, of class fr.paris.lutece.util.string.StringUtil.
     */
    public void testContainsHtmlSpecialCharacters(  )
    {
        System.out.println( "containsHtmlSpecialCharacters" );

        String strValue = "<";

        boolean expResult = true;
        boolean result = fr.paris.lutece.util.string.StringUtil.containsHtmlSpecialCharacters( strValue );
        assertEquals( expResult, result );
    }

    /**
     * Test of containsXssCharacters method, of class fr.paris.lutece.util.string.StringUtil.
     */
    public void testContainsXssCharacters(  )
    {
        System.out.println( "containsXssCharacters" );

        String strValue = "<";

        boolean expResult = true;
        boolean result = fr.paris.lutece.util.string.StringUtil.containsXssCharacters( strValue );
        assertEquals( expResult, result );
    }

    /**
     * Test of getXssCharactersAsString method, of class fr.paris.lutece.util.string.StringUtil.
     */
    public void testGetXssCharactersAsString(  )
    {
        System.out.println( "getXssCharactersAsString" );

        String result = fr.paris.lutece.util.string.StringUtil.getXssCharactersAsString(  );
        // The result should not be empty
        assertTrue( result.length(  ) > 0 );
    }
}
