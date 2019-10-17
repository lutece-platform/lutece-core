/*
 * Copyright (c) 2002-2019, Mairie de Paris
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
package fr.paris.lutece.portal.service.util;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * This kind of exception is thrown when the application encounters a critical problem. This class extends RuntimeException in order to avoid try/catch blocks
 * This class forces to write to the logs immediatly to protect against swallowing exceptions.
 */
public class AppException extends RuntimeException
{
    /**
     * Generated serialVersionUID
     */
    private static final long serialVersionUID = -742252097057629674L;

    /**
     * Constructor 1
     *
     * @param strMessage
     *            The error message
     */
    public AppException( String strMessage )
    {
        super( strMessage );
        writeToLogs( );
    }

    /**
     * Constructor 2
     *
     * @param strMessage
     *            The error message
     * @param t
     *            The initial throwable
     */
    public AppException( String strMessage, Throwable t )
    {
        super( strMessage, t );
        writeToLogs( );
    }

    /**
     * Constructor for backwards binary compatibility, same as AppException( String s, Throwable t )
     *
     * @param strMessage
     *            The error message
     * @param e
     *            The initial exception
     */
    public AppException( String strMessage, Exception e )
    {
        this( strMessage, (Throwable) e );
    }

    /**
     * Constructor 3
     */
    public AppException( )
    {
        writeToLogs( );
    }

    /**
     * Constructor 4
     *
     * @param strMessage
     *            The error message
     * @param e
     *            The initial exception
     * @param enableSuppression
     *            enableSuppresion
     * @param writableStackTrace
     *            writableStackTrace
     */
    public AppException( String strMessage, Throwable t, boolean enableSuppression, boolean writableStackTrace )
    {
        super( strMessage, t, enableSuppression, writableStackTrace );
        writeToLogs( );
    }

    private void writeToLogs( )
    {
        StringBuilder sb = new StringBuilder( "Critical AppException" );

        Throwable strRootCause = ExceptionUtils.getRootCause( this );
        if ( strRootCause != null )
        {
            sb.append( ", root cause: " );
            String strShortName = strRootCause.getClass( ).getSimpleName( );
            sb.append( strShortName );
        }

        Throwable throwableForMessage = strRootCause == null ? this : strRootCause;
        String strMessage = throwableForMessage.getMessage( );
        if ( strMessage != null )
        {
            sb.append( ": " );
            sb.append( strMessage );
        }

        String strHeader = sb.toString( );
        AppLogService.error( strHeader, this );
    }
}
