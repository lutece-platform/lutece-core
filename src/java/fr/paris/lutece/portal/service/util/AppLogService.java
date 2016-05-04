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

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.InputStream;

import java.util.Properties;


/**
 *  This class provides writing services in the application logs files
 */
public final class AppLogService
{
    // Constants
    private static final String LOGGER_EVENTS = "lutece.event";
    private static final String LOGGER_DEBUG = "lutece.debug";
    private static final String LOGGER_ERRORS = "lutece.error";
    private static final String SYSTEM_PROPERTY_LOG4J_CONFIGURATION = "log4j.configuration";

    /** alternate log4j property file */
    private static final String ALTERNATE_LOG_OVERRIDE_PATH = "override";
    private static final String ALTERNATE_LOG_FILE = "log.properties";
    private static Logger _loggerEvents = Logger.getLogger( LOGGER_EVENTS );
    private static Logger _loggerErrors = Logger.getLogger( LOGGER_ERRORS );
    private static Logger _loggerDebug = Logger.getLogger( LOGGER_DEBUG );

    /**
     * Creates a new AppLogService object.
     */
    private AppLogService(  )
    {
    }

    /**
     * Initializes a very basic logging system (everything to stdout)
     */
    public static void preinit(  )
    {
        BasicConfigurator.configure();
        info("Lutece logs pre-initialized: sending all logs to stdout.");
    }

    /**
     * initializes the errors log file and the application log file
     * @param strConfigPath The strConfigPath
     * @param strConfigFile The strConfigFile
     */
    public static void init( String strConfigPath, String strConfigFile )
    {
        BasicConfigurator.resetConfiguration();
        //Initialize the logger and configures it with the values of the properties file : config.properties
        try
        {
            _loggerEvents.setAdditivity( false );

            _loggerDebug.setAdditivity( false );

            String strAbsoluteConfigDirectoryPath = AppPathService.getAbsolutePathFromRelativePath( strConfigPath );

            String strAlternateFilePath = strAbsoluteConfigDirectoryPath +
                ( strAbsoluteConfigDirectoryPath.endsWith( "/" ) ? "" : "/" ) + ALTERNATE_LOG_OVERRIDE_PATH;

            File alternateLogFile = new File( strAlternateFilePath + File.separator + ALTERNATE_LOG_FILE );

            boolean bAlternateConfigFile = alternateLogFile.exists(  );

            InputStream is;
            String strLog4jConfigFile;

            if ( bAlternateConfigFile )
            {
                // Load loggers configuration from the log.properties
                is = AppPathService.getResourceAsStream( strConfigPath + ( strConfigPath.endsWith( "/" ) ? "" : "/" ) +
                        ALTERNATE_LOG_OVERRIDE_PATH + "/", ALTERNATE_LOG_FILE );
                strLog4jConfigFile = alternateLogFile.getAbsolutePath(  );
            }
            else
            {
                // Load loggers configuration from the config.properties
                is = AppPathService.getResourceAsStream( strConfigPath, strConfigFile );
                strLog4jConfigFile = strAbsoluteConfigDirectoryPath +
                    ( ( strAbsoluteConfigDirectoryPath.endsWith( "/" ) ) ? "" : "/" ) + strConfigFile;
            }

            Properties props = new Properties(  );
            props.load( is );
            PropertyConfigurator.configure( props );
            is.close(  );

            // Define the config.properties as log4j configuration file for other libraries using
            // the System property "log4j.configuration"
            System.setProperty( SYSTEM_PROPERTY_LOG4J_CONFIGURATION, strLog4jConfigFile );

            if ( bAlternateConfigFile )
            {
                debug( "Loaded log properties from alternate log.properties file " );
            }
        }
        catch ( Exception e )
        {
            System.err.println( "Bad Configuration of Log4j : " + e );
        }
        info("Lutece logs initialized, using configured property files to define levels and appenders.");
    }

    ////////////////////////////////////////////////////////////////////////////
    // Log4j wrappers

    /**
     * Tells if the logger accepts debug messages. If not it prevents to build
     * consuming messages that will be ignored.
     * @return True if the logger accepts debug messages, otherwise false.
     */
    public static boolean isDebugEnabled(  )
    {
        return _loggerDebug.isDebugEnabled(  );
    }

    /**
     * Log a message object with the DEBUG level. It is logged in application.log
     *
     * @param objToLog the message object to log
     */
    public static void debug( Object objToLog )
    {
        if ( _loggerDebug.isDebugEnabled(  ) )
        {
            _loggerDebug.debug( objToLog );
        }
    }

    /**
     * Tells if the logger accepts debug messages. If not it prevents to build
     * consuming messages that will be ignored.
     * @param strLogger The Logger name
     * @return True if the logger accepts debug messages, otherwise false.
     */
    public static boolean isDebugEnabled( String strLogger )
    {
        Logger logger = Logger.getLogger( strLogger );

        return logger.isDebugEnabled(  );
    }

    /**
     * Log a message object with the DEBUG level. It is logged in application.log
     *
     * @param strLogger The Logger name
     * @param objToLog the message object to log
     */
    public static void debug( String strLogger, Object objToLog )
    {
        Logger logger = Logger.getLogger( strLogger );

        if ( logger.isDebugEnabled(  ) )
        {
            logger.debug( objToLog );
        }
    }

    /**
     * Log a message object with the ERROR Level. It is logged in error.log
     *
     * @param objToLog the message object to log
     */
    public static void error( Object objToLog )
    {
        if ( _loggerErrors != null )
        {
            _loggerErrors.error( objToLog );
        }
    }

    /**
     * Log a message object with the ERROR level including the stack trace of
     * the Throwable t passed as parameter. It
     * is logged in error.log
     *
     * @param message the message object to log
     * @param t the exception to log, including its stack trace
     */
    public static void error( Object message, Throwable t )
    {
        if ( _loggerErrors != null )
        {
            _loggerErrors.error( message, t );
        }
    }

    /**
     * Log a message object with the INFO Level in application.log
     *
     * @param objToLog the message object to log
     */
    public static void info( Object objToLog )
    {
        if ( ( _loggerEvents != null ) && _loggerEvents.isInfoEnabled(  ) )
        {
            _loggerEvents.info( objToLog );
        }
    }
}
