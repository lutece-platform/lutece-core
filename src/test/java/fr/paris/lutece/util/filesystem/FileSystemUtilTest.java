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
package fr.paris.lutece.util.filesystem;

import fr.paris.lutece.test.LuteceTestCase;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class FileSystemUtilTest extends LuteceTestCase
{
    public void testGetSubDirectories(  ) throws DirectoryNotFoundException, IOException
    {
        //Create a base folder named Folder
        String strTempDirectoryPath = System.getProperty( "java.io.tmpdir" ) + File.separator;
        String strFolderName = "Folder";
        String strFolderPath = strTempDirectoryPath + strFolderName;
        File fileFolder = new File( strFolderPath );
        fileFolder.mkdir(  );

        //Place two directories in the base folder
        File fileFolder1 = new File( strFolderPath + File.separator + "Folder1" );
        fileFolder1.mkdir(  );

        File fileFolder2 = new File( strFolderPath + File.separator + "Folder2" );
        fileFolder2.mkdir(  );

        String strDirectory = "Folder";
        List expectedList = new ArrayList(  );
        expectedList.add( fileFolder1 );
        expectedList.add( fileFolder2 );

        List result = FileSystemUtil.getSubDirectories( strTempDirectoryPath, strDirectory );
        assertEquals( new HashSet(expectedList), new HashSet(result) );

        // try a bad directory
        boolean bCatchedException = false;

        try
        {
            FileSystemUtil.getSubDirectories( strTempDirectoryPath, "dummy" );
        }
        catch ( DirectoryNotFoundException e )
        {
            bCatchedException = true;
        }

        assertTrue( bCatchedException );

        // Create files
        File file1 = new File( fileFolder.getAbsolutePath(  ), "dummy1.txt" );
        file1.createNewFile(  );

        File file2 = new File( fileFolder.getAbsolutePath(  ), "dummy2.txt" );
        file2.createNewFile(  );

        List listFiles = FileSystemUtil.getFiles( strTempDirectoryPath, "Folder" );
        assertTrue( listFiles.size(  ) == 2 );

        // Clean folders
        fileFolder1.delete(  );
        fileFolder2.delete(  );
        fileFolder.delete(  );
    }
}
