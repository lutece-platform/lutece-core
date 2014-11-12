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

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;

// Java IO
import java.io.File;
import java.io.IOException;

// Java Util
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.activation.MimetypesFileTypeMap;


/**
 * This Service is used to manipulate Files and Directories in the File System.
 */
public final class FileSystemUtil
{
    private static final String DEFAULT_MIME_TYPE = "application/octet-stream";
    private static final String FILE_SEPARATOR = File.separator;
    private static final String FILE_MIME_TYPE = "WEB-INF" + FILE_SEPARATOR + "conf" + FILE_SEPARATOR + "mime.types";

    /**
     * Private constructor
     */
    private FileSystemUtil(  )
    {
    }

    /**
     * Returns the sub-directories of a directory.
     *
     * @param strRoot the root directory.
     * @param strDirectoryRelativePath the parent directory.
     * @throws DirectoryNotFoundException if the directory does not exist.
     * @return The list of sub-directories.
     */
    public static List<File> getSubDirectories( String strRoot, String strDirectoryRelativePath )
        throws DirectoryNotFoundException
    {
        // Files' list init
        ArrayList<File> listFiles = new ArrayList<File>(  );

        // Directory path
        String strDirectory = strRoot + strDirectoryRelativePath;

        // Directory
        File fDirectory = new File( strDirectory );

        if ( !fDirectory.exists(  ) )
        {
            throw new DirectoryNotFoundException(  );
        }

        File[] files = fDirectory.listFiles(  );

        for ( int i = 0; i < files.length; i++ )
        {
            File file = files[i];

            if ( file.isDirectory(  ) )
            {
                listFiles.add( file );
            }
        }

        return listFiles;
    }

    /**
     * Returns the files of a directory, alphabetically ordered.
     *
     * @param strRoot The root directory
     * @param strDirectoryRelativePath The directory's path relative to the root directory.
     * @throws DirectoryNotFoundException if the directory does not exist.
     * @return The list of files.
     */
    public static List<File> getFiles( String strRoot, String strDirectoryRelativePath )
        throws DirectoryNotFoundException
    {
        // Use a treeset to order files with a comparator
        TreeSet<File> set = new TreeSet<File>( new FileNameComparator(  ) );

        // Directory path
        String strDirectory = strRoot + strDirectoryRelativePath;

        // Directory
        File fDirectory = new File( strDirectory );

        if ( !fDirectory.exists(  ) )
        {
            throw new DirectoryNotFoundException(  );
        }

        File[] files = fDirectory.listFiles(  );

        for ( int i = 0; i < files.length; i++ )
        {
            File file = files[i];

            if ( file.isFile(  ) )
            {
                set.add( file );
            }
        }

        // Convert into a list to preserve the order
        return new ArrayList<File>( set );
    }

    /**
     * Return the mimetype of the file depending of his extension and the mime.types file
     * @param strFilename the file name
     * @return the file mime type
     */
    public static String getMIMEType( String strFilename )
    {
        try
        {
            MimetypesFileTypeMap mimeTypeMap = new MimetypesFileTypeMap( AppPathService.getWebAppPath(  ) +
                    File.separator + FILE_MIME_TYPE );

            return mimeTypeMap.getContentType( strFilename.toLowerCase(  ) );
        }
        catch ( IOException e )
        {
            AppLogService.error( e );

            return DEFAULT_MIME_TYPE;
        }
    }
}
