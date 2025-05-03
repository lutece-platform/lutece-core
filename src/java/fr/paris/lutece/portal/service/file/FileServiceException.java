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
package fr.paris.lutece.portal.service.file;

public class FileServiceException extends Exception
{

    private static final long serialVersionUID = -4788782240985061911L;

    // The response code status
    private Integer _nResponseCodeStatus;

    // The response I18N message key
    private String _strI18nMessageKey;

    /**
     * Creates a new instance of ExpiredLinkException
     * 
     * @param strMessage
     *            The exception message
     */
    public FileServiceException( String strMessage )
    {
        super( strMessage );
    }

    /**
     * Creates a new instance of HttpAccessException.
     *
     * @param strMessage
     *            The error message
     * @param e
     *            The exception
     */
    public FileServiceException( String strMessage, Exception e )
    {
        super( strMessage, e );
    }

    /**
     * Creates a new instance of HttpAccessException.
     *
     * @param strMessage
     *            The error message
     * @param nResponseCode
     *            the http response code associated to the Exception
     * @param e
     *            The exception
     */
    public FileServiceException( String strMessage, Integer nResponseCodeStatus, Exception e )
    {
        super( strMessage, e );
        _nResponseCodeStatus = nResponseCodeStatus;
    }

    /**
     * the response code (based on http codes)
     *
     * @return the response code associated to the Exception
     */
    public Integer getResponseCode( )
    {
        return _nResponseCodeStatus;
    }

    /**
     * get the i18n message key
     * 
     * @return the key
     */
    public String getI18nMessageKey( )
    {
        return _strI18nMessageKey;
    }
}
