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
package fr.paris.lutece.util.file;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import fr.paris.lutece.test.LuteceTestCase;

public class FileUtilTest extends LuteceTestCase
{

    public void testHasImageExtension( )
    {
        assertTrue( FileUtil.hasImageExtension( "temp.jpg" ) );
        assertFalse( FileUtil.hasImageExtension( "temp.html" ) );
    }

    public void testHasHtmlExtension( )
    {
        assertFalse( FileUtil.hasHtmlExtension( "temp.jpg" ) );
        assertTrue( FileUtil.hasHtmlExtension( "temp.html" ) );
    }

    public void testZipFiles( )
    {
        try
        {
            Path tempFolder = Paths.get( System.getProperty( "java.io.tmpdir" ) );

            Path f1 = Paths.get( tempFolder.toFile( ).getAbsolutePath( ), "test1.txt" );
            f1.toFile( ).createNewFile( );
            Files.write( f1, "hello".getBytes( ) );
            Path f2 = Paths.get( tempFolder.toFile( ).getAbsolutePath( ), "test2.txt" );
            f2.toFile( ).createNewFile( );

            Path zip = Paths.get( tempFolder.toFile( ).getAbsolutePath( ), "test.zip" );

            FileUtil.zipFiles( zip, f1, f2 );

            ZipInputStream zis = new ZipInputStream( Files.newInputStream( zip ) );

            List<String> entries = new ArrayList<>( );
            ZipEntry entry;
            while ( ( entry = zis.getNextEntry( ) ) != null )
            {
                entries.add( entry.getName( ) );
            }

            assertEquals( 2, entries.size( ) );
            assertTrue( entries.contains( "test1.txt" ) );
            assertTrue( entries.contains( "test2.txt" ) );
            Files.delete( f1 );
            Files.delete( f2 );
            Files.delete( zip );
        }
        catch( Exception e )
        {
            fail( e.getMessage( ) );
        }
    }

    public void testNormalizeFileName( )
    {
        String input = "Test éèà:ô";
        String res = FileUtil.normalizeFileName( input );
        assertEquals( "test_eeao", res );
    }
}
