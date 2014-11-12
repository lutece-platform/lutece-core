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
package fr.paris.lutece.portal.web.upload;

import fr.paris.lutece.util.filesystem.UploadUtil;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;


/**
 * This class is used to normalize the file names.
 * This class  override the method getName () of FileItem
 */
public class NormalizeFileItem implements FileItem
{
    private static final long serialVersionUID = 8696893066570050604L;
    private FileItem _item;

    /**
     * Instantiates a new normalize file item.
     *
     * @param item the item
     */
    public NormalizeFileItem( FileItem item )
    {
        _item = item;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(  )
    {
        _item.delete(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] get(  )
    {
        return _item.get(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getContentType(  )
    {
        return _item.getContentType(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFieldName(  )
    {
        return _item.getFieldName(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getInputStream(  ) throws IOException
    {
        return _item.getInputStream(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName(  )
    {
        return UploadUtil.cleanFileName( FilenameUtils.getName( _item.getName(  ) ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OutputStream getOutputStream(  ) throws IOException
    {
        return _item.getOutputStream(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getSize(  )
    {
        return _item.getSize(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getString(  )
    {
        return _item.getString(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getString( String encoding ) throws UnsupportedEncodingException
    {
        return _item.getString( encoding );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFormField(  )
    {
        return _item.isFormField(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInMemory(  )
    {
        return _item.isInMemory(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFieldName( String name )
    {
        _item.setFieldName( name );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFormField( boolean state )
    {
        _item.setFormField( state );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write( File file ) throws Exception
    {
        _item.write( file );
    }
}
