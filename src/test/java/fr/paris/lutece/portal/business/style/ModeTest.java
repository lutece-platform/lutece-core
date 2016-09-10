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
package fr.paris.lutece.portal.business.style;

import fr.paris.lutece.test.LuteceTestCase;

import java.util.Collection;

public class ModeTest extends LuteceTestCase
{
    private final static String DESCRIPTION1 = "Description 1";
    private final static String DESCRIPTION2 = "Description 2";
    private final static String PATH1 = "Path 1";
    private final static String PATH2 = "Path 2";
    private final static String OUTPUTXSLPROPERTYMETHOD1 = "OutputXslPropertyMethod 1";
    private final static String OUTPUTXSLPROPERTYMETHOD2 = "OutputXslPropertyMethod 2";
    private final static String OUTPUTXSLPROPERTYVERSION1 = "OutputXslPropertyVersion 1";
    private final static String OUTPUTXSLPROPERTYVERSION2 = "OutputXslPropertyVersion 2";
    private final static String OUTPUTXSLPROPERTYMEDIATYPE1 = "OutputXslPropertyMediaType 1";
    private final static String OUTPUTXSLPROPERTYMEDIATYPE2 = "OutputXslPropertyMediaType 2";
    private final static String OUTPUTXSLPROPERTYENCODING1 = "OutputXslPropertyEncoding 1";
    private final static String OUTPUTXSLPROPERTYENCODING2 = "OutputXslPropertyEncoding 2";
    private final static String OUTPUTXSLPROPERTYINDENT1 = "OutputXslPropertyIndent 1";
    private final static String OUTPUTXSLPROPERTYINDENT2 = "OutputXslPropertyIndent 2";
    private final static String OUTPUTXSLPROPERTYOMITXMLDECLARATION1 = "OutputXslPropertyOmitXmlDeclaration 1";
    private final static String OUTPUTXSLPROPERTYOMITXMLDECLARATION2 = "OutputXslPropertyOmitXmlDeclaration 2";
    private final static String OUTPUTXSLPROPERTYSTANDALONE1 = "OutputXslPropertyStandalone 1";
    private final static String OUTPUTXSLPROPERTYSTANDALONE2 = "OutputXslPropertyStandalone 2";

    public void testBusinessMode( )
    {
        // Initialize an object
        Mode mode = new Mode( );
        mode.setDescription( DESCRIPTION1 );
        mode.setPath( PATH1 );
        mode.setOutputXslPropertyMethod( OUTPUTXSLPROPERTYMETHOD1 );
        mode.setOutputXslPropertyVersion( OUTPUTXSLPROPERTYVERSION1 );
        mode.setOutputXslPropertyMediaType( OUTPUTXSLPROPERTYMEDIATYPE1 );
        mode.setOutputXslPropertyEncoding( OUTPUTXSLPROPERTYENCODING1 );
        mode.setOutputXslPropertyIndent( OUTPUTXSLPROPERTYINDENT1 );
        mode.setOutputXslPropertyOmitXmlDeclaration( OUTPUTXSLPROPERTYOMITXMLDECLARATION1 );
        mode.setOutputXslPropertyStandalone( OUTPUTXSLPROPERTYSTANDALONE1 );

        // Create test
        ModeHome.create( mode );

        Mode modeStored = ModeHome.findByPrimaryKey( mode.getId( ) );
        assertEquals( modeStored.getDescription( ), mode.getDescription( ) );
        assertEquals( modeStored.getPath( ), mode.getPath( ) );
        assertEquals( modeStored.getOutputXslPropertyMethod( ), mode.getOutputXslPropertyMethod( ) );
        assertEquals( modeStored.getOutputXslPropertyVersion( ), mode.getOutputXslPropertyVersion( ) );
        assertEquals( modeStored.getOutputXslPropertyMediaType( ), mode.getOutputXslPropertyMediaType( ) );
        assertEquals( modeStored.getOutputXslPropertyEncoding( ), mode.getOutputXslPropertyEncoding( ) );
        assertEquals( modeStored.getOutputXslPropertyIndent( ), mode.getOutputXslPropertyIndent( ) );
        assertEquals( modeStored.getOutputXslPropertyOmitXmlDeclaration( ), mode.getOutputXslPropertyOmitXmlDeclaration( ) );
        assertEquals( modeStored.getOutputXslPropertyStandalone( ), mode.getOutputXslPropertyStandalone( ) );

        // Update test
        mode.setDescription( DESCRIPTION2 );
        mode.setPath( PATH2 );
        mode.setOutputXslPropertyMethod( OUTPUTXSLPROPERTYMETHOD2 );
        mode.setOutputXslPropertyVersion( OUTPUTXSLPROPERTYVERSION2 );
        mode.setOutputXslPropertyMediaType( OUTPUTXSLPROPERTYMEDIATYPE2 );
        mode.setOutputXslPropertyEncoding( OUTPUTXSLPROPERTYENCODING2 );
        mode.setOutputXslPropertyIndent( OUTPUTXSLPROPERTYINDENT2 );
        mode.setOutputXslPropertyOmitXmlDeclaration( OUTPUTXSLPROPERTYOMITXMLDECLARATION2 );
        mode.setOutputXslPropertyStandalone( OUTPUTXSLPROPERTYSTANDALONE2 );

        ModeHome.update( mode );
        modeStored = ModeHome.findByPrimaryKey( mode.getId( ) );
        assertEquals( modeStored.getDescription( ), mode.getDescription( ) );
        assertEquals( modeStored.getPath( ), mode.getPath( ) );
        assertEquals( modeStored.getOutputXslPropertyMethod( ), mode.getOutputXslPropertyMethod( ) );
        assertEquals( modeStored.getOutputXslPropertyVersion( ), mode.getOutputXslPropertyVersion( ) );
        assertEquals( modeStored.getOutputXslPropertyMediaType( ), mode.getOutputXslPropertyMediaType( ) );
        assertEquals( modeStored.getOutputXslPropertyEncoding( ), mode.getOutputXslPropertyEncoding( ) );
        assertEquals( modeStored.getOutputXslPropertyIndent( ), mode.getOutputXslPropertyIndent( ) );
        assertEquals( modeStored.getOutputXslPropertyOmitXmlDeclaration( ), mode.getOutputXslPropertyOmitXmlDeclaration( ) );
        assertEquals( modeStored.getOutputXslPropertyStandalone( ), mode.getOutputXslPropertyStandalone( ) );

        Collection listModes = ModeHome.getModesList( );
        assertTrue( listModes.size( ) > 0 );

        listModes = ModeHome.getModes( );
        assertTrue( listModes.size( ) > 0 );

        // Delete test
        ModeHome.remove( mode.getId( ) );
        modeStored = ModeHome.findByPrimaryKey( mode.getId( ) );
        assertNull( modeStored );
    }
}
