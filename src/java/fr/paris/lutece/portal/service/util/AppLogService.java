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
package fr.paris.lutece.portal.service.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Supplier;

/**
 * This class provides writing services in the application logs files
 */
public final class AppLogService
{
    // Constants
    private static final String LOGGER_EVENTS = "lutece.event";
    private static final String LOGGER_DEBUG = "lutece.debug";
    private static final String LOGGER_ERRORS = "lutece.error";
    private static Logger _loggerEvents = LogManager.getLogger( LOGGER_EVENTS );
    private static Logger _loggerErrors = LogManager.getLogger( LOGGER_ERRORS );
    private static Logger _loggerDebug = LogManager.getLogger( LOGGER_DEBUG );

    /**
     * Creates a new AppLogService object.
     */
    private AppLogService( )
    {
    }
    // //////////////////////////////////////////////////////////////////////////
    // Log4j wrappers

    /**
     * Tells if the logger accepts debug messages. If not it prevents to build consuming messages that will be ignored.
     * 
     * @return True if the logger accepts debug messages, otherwise false.
     */
    public static boolean isDebugEnabled( )
    {
        return _loggerDebug.isDebugEnabled( );
    }

    /**
     * Log a message object with the DEBUG level. It is logged in application.log
     *
     * @param objToLog
     *            the message object to log
     */
    public static void debug( Object objToLog )
    {
        _loggerDebug.debug( objToLog );
    }

    /**
     * Logs a message with parameters at the {@link Level#DEBUG DEBUG} level.
     *
     * To improve performance, do not use String concatenation such as : AppLogService.error( "my message with param1 " + param1 + " and " + param2, myexception
     * ); Recommended use : AppLogService.error( "my message with param1 {} and param2 {}", param1, param2, myexception );
     *
     * @param message
     *            the message to log; the format depends on the message factory.
     * @param params
     *            parameters to the message.
     * @see Logger##getMessageFactory()
     */
    public static void debug( String message, Object... params )
    {
        _loggerDebug.debug( message, params );
    }

    /**
     * Logs a message with parameters which are only to be constructed if the logging level is the {@link Level#DEBUG DEBUG} level.
     *
     * @param message
     *            the message to log; the format depends on the message factory.
     * @param paramSuppliers
     *            An array of functions, which when called, produce the desired log message parameters.
     */
    public static void debug( String message, Supplier<?>... paramSuppliers )
    {
        _loggerDebug.debug( message, paramSuppliers );
    }

    /**
     * Tells if the logger accepts debug messages. If not it prevents to build consuming messages that will be ignored.
     * 
     * @param strLogger
     *            The Logger name
     * @return True if the logger accepts debug messages, otherwise false.
     */
    public static boolean isDebugEnabled( String strLogger )
    {
        Logger logger = LogManager.getLogger( strLogger );

        return logger.isDebugEnabled( );
    }

    /**
     * Log a message object with the ERROR Level. It is logged in error.log
     *
     * @param objToLog
     *            the message object to log
     */
    public static void error( Object objToLog )
    {
        _loggerErrors.error( objToLog );
    }

    /**
     * Log a message object with the ERROR level including the stack trace of the Throwable t passed as parameter. It is logged in error.log
     *
     * @param message
     *            the message object to log
     * @param t
     *            the exception to log, including its stack trace
     */
    public static void error( Object message, Throwable t )
    {
        _loggerErrors.error( message, t );
    }

    /**
     * Logs a message with parameters at the {@link Level#ERROR ERROR} level.
     *
     * To improve performance, do not use String concatenation such as : AppLogService.error( "my message with param1 " + param1 + " and " + param2,
     * myexception); Recommended use : AppLogService.error( "my message with param1 {} and param2 {}", param1, param2, myexception );
     *
     * @param message
     *            the message to log; the format depends on the message factory.
     * @param params
     *            parameters to the message.
     * @see Logger#getMessageFactory()
     */
    public static void error( String message, Object... params )
    {

        _loggerErrors.error( message, params );
    }

    /**
     * Logs a message with parameters which are only to be constructed if the logging level is the {@link Level#ERROR ERROR} level.
     *
     * @param message
     *            the message to log; the format depends on the message factory.
     * @param paramSuppliers
     *            An array of functions, which when called, produce the desired log message parameters.
     */
    public static void error( String message, Supplier<?>... paramSuppliers )
    {

        _loggerErrors.error( message, paramSuppliers );
    }

    /**
     * Tells if the logger accepts error messages. If not it prevents to build consuming messages that will be ignored.
     * 
     * @return True if the logger accepts error messages, otherwise false.
     */
    public static boolean isErrorEnabled( )
    {
        return _loggerErrors.isErrorEnabled( );
    }

    /**
     * Log a message object with the INFO Level in application.log
     *
     * @param objToLog
     *            the message object to log
     */
    public static void info( Object objToLog )
    {
        _loggerEvents.info( objToLog );

    }

    /**
     * Logs a message with parameters at the {@link Level#INFO INFO} level.
     *
     * To improve performance, do not use String concatenation such as : AppLogService.error( "my message with param1 " + param1 + " and " + param2, myexception
     * ); Recommended use : AppLogService.error( "my message with param1 {} and param2 {}", param1, param2, myexception );
     *
     * @param message
     *            the message to log; the format depends on the message factory.
     * @param params
     *            parameters to the message.
     * @see Logger##getMessageFactory()
     */
    public static void info( String message, Object... params )
    {
        _loggerEvents.info( message, params );
    }

    /**
     * Logs a message with parameters which are only to be constructed if the logging level is the {@link Level#INFO INFO} level.
     *
     * @param message
     *            the message to log; the format depends on the message factory.
     * @param paramSuppliers
     *            An array of functions, which when called, produce the desired log message parameters.
     */
    public static void info( String message, Supplier<?>... paramSuppliers )
    {
        _loggerEvents.info( message, paramSuppliers );
    }

    /**
     * Tells if the logger accepts info messages. If not it prevents to build consuming messages that will be ignored.
     * 
     * @return True if the logger accepts info messages, otherwise false.
     */
    public static boolean isInfoEnabled( )
    {
        return _loggerEvents.isInfoEnabled( );
    }
}
