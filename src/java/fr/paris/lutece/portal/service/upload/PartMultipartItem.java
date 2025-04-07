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

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UncheckedIOException;

import jakarta.servlet.http.Part;

public class PartMultipartItem implements MultipartItem, Serializable
{

    private final Part _part;
    private final String _strFileName;

    public PartMultipartItem( Part part, String fileName )
    {
        super( );
        _part = part;
        _strFileName = fileName;
    }

    @Override
    public String getName( )
    {
        return _strFileName;
    }

    @Override
    public InputStream getInputStream( ) throws IOException
    {
        return this._part.getInputStream( );
    }

    @Override
    public String getContentType( )
    {
        return _part.getContentType( );
    }

    @Override
    public void delete( ) throws IOException
    {
        _part.delete( );
    }

    @Override
    public String getFieldName( )
    {
        return _part.getName( );
    }

    @Override
    public byte [ ] get( ) throws UncheckedIOException
    {
        try
        {
            return _part.getInputStream( ).readAllBytes( );
        }
        catch( IOException e )
        {
            throw new UncheckedIOException( e );
        }
    }

    @Override
    public long getSize( )
    {
        return this._part.getSize( );
    }

}
