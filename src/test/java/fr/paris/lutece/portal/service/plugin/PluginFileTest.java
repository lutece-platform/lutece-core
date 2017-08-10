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
/*
 * PluginFileTest.java
 * JUnit based test
 *
 * Created on 7 mai 2006, 19:55
 */
package fr.paris.lutece.portal.service.plugin;

import fr.paris.lutece.portal.service.includes.PageIncludeEntry;
import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.test.LuteceTestCase;

import junit.framework.*;

import java.util.List;

/**
 *
 * @author Pierre
 */
public class PluginFileTest extends LuteceTestCase
{
    public PluginFileTest( String testName )
    {
        super( testName );
    }

    public static Test suite( )
    {
        TestSuite suite = new TestSuite( PluginFileTest.class );

        return suite;
    }

    /**
     * Test of getName method, of class fr.paris.lutece.portal.service.PluginFile.
     */
    public void testLoad( ) throws LuteceInitException
    {
        System.out.println( "load" );

        PluginFile instance = new PluginFile( );

        // TODO path en dur...
        String strFilename = getResourcesDir( ) + "../test-classes/plugin-test.xml";
        instance.load( strFilename );

        assertNotNull( instance.getParams( ) );
        assertEquals( AppPropertiesService.getProperty( "lutece.encoding" ), instance.getParams( ).get( "test_properties" ) );

        List<PageIncludeEntry> includes = instance.getPageIncludes( );
        assertEquals( 3, includes.size( ) );

        for ( PageIncludeEntry anInclude : includes )
        {
            if ( anInclude.getId( ).contains( "disabled" ) )
            {
                assertFalse( anInclude.isEnabled( ) );
            }
            else
            {
                assertTrue( anInclude.isEnabled( ) );
            }
        }
    }
}
