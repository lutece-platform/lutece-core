/*
 * Copyright (c) 2002-2006, Mairie de Paris
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

package fr.paris.lutece.portal.business.stylesheet;

import fr.paris.lutece.LuteceTestCase;

public class StyleSheetTest extends LuteceTestCase
{

    private final static String DESCRIPTION1 = "Description 1";
    private final static String DESCRIPTION2 = "Description 2";
    private final static String FILE1 = "Filename 1";
    private final static String FILE2 = "Fileamne 2";
    private final static String SOURCE1 = "<xsl Source 1>";
    private final static String SOURCE2 = "<xsl Source 2>";


    public void testBusiness(  )
    {
        // Initialize an object
        StyleSheet styleSheet = new StyleSheet();
	styleSheet.setDescription( DESCRIPTION1 );
	styleSheet.setFile( FILE1 );
	styleSheet.setSource( SOURCE1.getBytes() );

        // Create test
        StyleSheetHome.create( styleSheet );
        StyleSheet styleSheetStored = StyleSheetHome.findByPrimaryKey( styleSheet.getId() );
        assertEquals( styleSheetStored.getDescription() , styleSheet.getDescription() );
        assertEquals( styleSheetStored.getFile() , styleSheet.getFile() );
        assertEquals( styleSheetStored.getSource().length , styleSheet.getSource().length );

        // Update test
	styleSheet.setDescription( DESCRIPTION2 );
	styleSheet.setFile( FILE2 );
	styleSheet.setSource( SOURCE2.getBytes() );

        StyleSheetHome.update( styleSheet );
        styleSheetStored = StyleSheetHome.findByPrimaryKey( styleSheet.getId() );
        assertEquals( styleSheetStored.getDescription() , styleSheet.getDescription() );
        assertEquals( styleSheetStored.getFile() , styleSheet.getFile() );
        assertEquals( styleSheetStored.getSource().length , styleSheet.getSource().length );
        
        // List Test
        StyleSheetHome.getStyleSheetList( 0 );
        StyleSheetHome.getStylesList();

        // Delete test
        StyleSheetHome.remove( styleSheet.getId() );
        styleSheetStored = StyleSheetHome.findByPrimaryKey( styleSheet.getId() );
        assertNull( styleSheetStored );
    }
}
