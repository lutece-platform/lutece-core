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
package fr.paris.lutece.portal.service.util;


/**
 * This kind of exception is thrown when the application encounters a critical
 * problem. This class extends RuntimeException in order to avoid try/catch
 * blocks
 */
public class AppException extends RuntimeException
{
    /**
     * Generated serialVersionUID
     */
    private static final long serialVersionUID = -742252097057629674L;
    private Exception _exception; // Initial Exception to embed
    private String _strMessage; // Error message

    /**
     * Constructor 1
     *
     * @param strMessage The error message
     */
    public AppException( String strMessage )
    {
        _strMessage = strMessage;
        AppLogService.error( getPrintStack( this ) );
    }

    /**
     * Constructor 2
     *
     * @param strMessage The error message
     * @param e The initial exception
     */
    public AppException( String strMessage, Exception e )
    {
        _strMessage = strMessage;
        _exception = e;
        AppLogService.error( getAppMessage(  ), e );
    }

    /**
     * Constructor 3
     */
    public AppException(  )
    {
    }

    /**
     * Returns the initial exception.
     * @return The initial exception.
     */
    public Exception getInitialException(  )
    {
        return _exception;
    }

    /**
     * Overides getMessage method
     * @return strMessage The error message
     */
    @Override
    public String getMessage(  )
    {
        return getAppMessage(  );
    }

    /**
     * Get the exception's printstack and returns it as a string
     * @param e The Exception.
     * @return  The printstack as a String
     */
    private String getPrintStack( Exception e )
    {
        java.io.CharArrayWriter cw = new java.io.CharArrayWriter(  );
        java.io.PrintWriter pw = new java.io.PrintWriter( cw, true );
        e.printStackTrace( pw );

        return cw.toString(  );
    }

    /**
     * Overides getMessage method
     * @return strMessage The error message
     */
    private String getAppMessage(  )
    {
        StringBuffer strMessage = new StringBuffer(  );
        strMessage.append( _strMessage );
        strMessage.append( "\n" );

        /* Selects the initial exception, if it exists */
        if ( getInitialException(  ) != null )
        {
            strMessage.append( "Initial error print stack : \n" );
            strMessage.append( getPrintStack( getInitialException(  ) ) );
        }

        return strMessage.toString(  );
    }
}
