/*
 * Copyright (c) 2002-2012, Mairie de Paris
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;


/*
 * This class is used to normalize the file names.
 * This class  override the method getName () of FileItem
 */
public class NormalizeFileItem implements FileItem
{
    private static final long serialVersionUID = 8696893066570050604L;
    private FileItem _item;

    public NormalizeFileItem( FileItem item )
    {
        _item = item;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.commons.fileupload.FileItem#delete()
     */
    public void delete(  )
    {
        _item.delete(  );
    }

    /*
     * (non-Javadoc)
     * @see org.apache.commons.fileupload.FileItem#get()
     */
    public byte[] get(  )
    {
        return _item.get(  );
    }

    /*
     * (non-Javadoc)
     * @see org.apache.commons.fileupload.FileItem#getContentType()
     */
    public String getContentType(  )
    {
        return _item.getContentType(  );
    }

    /*
     * (non-Javadoc)
     * @see org.apache.commons.fileupload.FileItem#getFieldName()
     */
    public String getFieldName(  )
    {
        return _item.getFieldName(  );
    }

    /*
     * (non-Javadoc)
     * @see org.apache.commons.fileupload.FileItem#getInputStream()
     */
    public InputStream getInputStream(  ) throws IOException
    {
        return _item.getInputStream(  );
    }

    /*
     * (non-Javadoc)
     * @see org.apache.commons.fileupload.FileItem#getName()
     */
    public String getName(  )
    {
        return UploadUtil.cleanFileName( _item.getName(  ) );
    }

    /*
     * (non-Javadoc)
     * @see org.apache.commons.fileupload.FileItem#getOutputStream()
     */
    public OutputStream getOutputStream(  ) throws IOException
    {
        return _item.getOutputStream(  );
    }

    /*
     * (non-Javadoc)
     * @see org.apache.commons.fileupload.FileItem#getSize()
     */
    public long getSize(  )
    {
        return _item.getSize(  );
    }

    /*
     * (non-Javadoc)
     * @see org.apache.commons.fileupload.FileItem#getOutputStream()
     */
    public String getString(  )
    {
        return _item.getString(  );
    }

    /*
     * (non-Javadoc)
     * @see org.apache.commons.fileupload.FileItem#getOutputStream()
     */
    public String getString( String encoding ) throws UnsupportedEncodingException
    {
        return _item.getString( encoding );
    }

    /*
     * (non-Javadoc)
     * @see org.apache.commons.fileupload.FileItem#getOutputStream()
     */
    public boolean isFormField(  )
    {
        return _item.isFormField(  );
    }

    /*
     * (non-Javadoc)
     * @see org.apache.commons.fileupload.FileItem#getOutputStream()
     */
    public boolean isInMemory(  )
    {
        // TODO Auto-generated method stub
        return _item.isInMemory(  );
    }

    /*
     * (non-Javadoc)
     * @see org.apache.commons.fileupload.FileItem#getOutputStream()
     */
    public void setFieldName( String name )
    {
        _item.setFieldName( name );
    }

    /*
     * (non-Javadoc)
     * @see org.apache.commons.fileupload.FileItem#getOutputStream()
     */
    public void setFormField( boolean state )
    {
        _item.setFormField( state );
    }

    /*
     * (non-Javadoc)
     * @see org.apache.commons.fileupload.FileItem#getOutputStream()
     */
    public void write( File file ) throws Exception
    {
        _item.write( file );
    }
}
