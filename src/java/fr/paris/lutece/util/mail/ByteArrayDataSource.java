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
package fr.paris.lutece.util.mail;

import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import javax.activation.DataSource;


/**
 * This class is used by MailUtil.
 */
public class ByteArrayDataSource implements DataSource, Serializable
{
    /**
         *
         */
    private static final long serialVersionUID = 3706343218798831522L;
    private static final String PROPERTY_CHARSET = "mail.charset";
    private byte[] _data; // data
    private String _type; // content-type

    /**
     * Creates a new ByteArrayDataSource object.
     *
     * @param data The data
     * @param type The parameters
     */
    public ByteArrayDataSource( String data, String type )
    {
        try
        {
            // Assumption that the string contains only ASCII
            // characters!  Otherwise just pass a charset into this
            // constructor and use it in getBytes()
            _data = data.getBytes( AppPropertiesService.getProperty( PROPERTY_CHARSET ) );
        }
        catch ( UnsupportedEncodingException uex )
        {
            throw new AppException( uex.toString(  ) );
        }

        _type = type;
    }

    /**
     * Creates a new ByteArrayDataSource object.
    *
    * @param data The data
    * @param type The parameters
    */
    public ByteArrayDataSource( byte[] data, String type )
    {
        _data = data;
        _type = type;
    }

    ////////////////////////////////////////////////////////////////////////////
    // DataSource interface implementation

    /**
     *
     * @return The input stream
     * @throws IOException The IO exception
     */
    public InputStream getInputStream(  ) throws IOException
    {
        if ( _data == null )
        {
            throw new IOException( "no data" );
        }

        return new ByteArrayInputStream( _data );
    }

    /**
     *
     * @return The output stream
     * @throws IOException The IO exception
     */
    public OutputStream getOutputStream(  ) throws IOException
    {
        throw new IOException( "cannot do this" );
    }

    /**
     *
     * @return The content type
     */
    public String getContentType(  )
    {
        return _type;
    }

    /**
     *
     * @return The Name
     */
    public String getName(  )
    {
        return "dummy";
    }
}
