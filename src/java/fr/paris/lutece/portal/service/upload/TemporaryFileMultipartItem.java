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
package fr.paris.lutece.portal.service.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.nio.file.Files;

import fr.paris.lutece.util.file.FileUtil;

public class TemporaryFileMultipartItem implements MultipartItem, Serializable
{

    private final File _file;
    private final String _strSubmittedFileName;
    private final String _strContentType;
    private final String _strFieldName;

    public TemporaryFileMultipartItem( File file, String strSubmittedFileName, String strContentType, String strFieldName )
    {
        _file = file;
        _strSubmittedFileName = strSubmittedFileName;
        _strContentType = strContentType;
        _strFieldName = strFieldName;
    }

    @Override
    public String getName( )
    {
        return _strSubmittedFileName;
    }

    @Override
    public InputStream getInputStream( ) throws IOException
    {
        return new FileInputStream( _file );
    }

    @Override
    public String getContentType( )
    {
        return _strContentType;
    }

    @Override
    public void delete( ) throws IOException
    {
        FileUtil.deleteFile( _file );
    }

    @Override
    public String getFieldName( )
    {
        return _strFieldName;
    }

    @Override
    public byte [ ] get( ) throws UncheckedIOException
    {
        try
        {
            return getInputStream( ).readAllBytes( );
        }
        catch( IOException e )
        {
            throw new UncheckedIOException( e );
        }
    }

    @Override
    public long getSize( )
    {
        return this._file.length( );
    }

}
