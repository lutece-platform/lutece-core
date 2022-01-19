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
package fr.paris.lutece.portal.service.security.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.paris.lutece.api.user.User;
import fr.paris.lutece.portal.service.security.AccessLoggerConstants;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.security.IAccessLogger;

import java.text.MessageFormat;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class provides a default implementation for AccessLogger Service
 * 
 * A specific log format can be set in the properties file
 * 
 * - portal.defaultAccessLogger.messageFormat - portal.defaultAccessLogger.messageFormatSeparator
 * 
 * Log lines could be certified with a hash, that could be verified, whith this property set to true :
 * 
 * - portal.defaultAccessLogger.addHashToLogs
 * 
 */
public final class DefaultAccessLogger implements IAccessLogger
{

    private static final String CONSTANT_HASH_ENCODING = "UTF-8";
    private static final String CONSTANT_HASH_DIGEST = "MD5";
    private static final String PROPERTY_ADD_HASH_TO_LOGS = "accessLogger.defaultAccessLogger.addHashToLogs";
    private static final String PROPERTY_ACCESSLOG_MESSAGE_FORMAT = "accessLogger.defaultAccessLogger.messageFormat";
    private static final String PROPERTY_ACCESSLOG_MESSAGE_FORMAT_SEPARATOR = "accessLogger.defaultAccessLogger.messageFormatSeparator";

    private static final String DEFAULT_ACCESSLOG_MESSAGE_FORMAT = "|{0}|{1}|{2}|{3}|{4}|{5}|";
    private static final String DEFAULT_ACCESSLOG_MESSAGE_FORMAT_SEPARATOR = "|";

    private static final String ERROR_MSG = "ERROR : unable to create json from data";

    private final boolean _bAddHashToLogs = AppPropertiesService.getPropertyBoolean( PROPERTY_ADD_HASH_TO_LOGS, false );
    private final String _messageFormat = AppPropertiesService.getProperty( PROPERTY_ACCESSLOG_MESSAGE_FORMAT, DEFAULT_ACCESSLOG_MESSAGE_FORMAT );
    private final String _messageFormatSeparator = AppPropertiesService.getProperty( PROPERTY_ACCESSLOG_MESSAGE_FORMAT_SEPARATOR,
            DEFAULT_ACCESSLOG_MESSAGE_FORMAT_SEPARATOR );

    public static final String DEFAULT_LOGGER_ACCESS_LOG = "lutece.accessLogger";
    private static Logger _defaultLogger = LogManager.getLogger( DEFAULT_LOGGER_ACCESS_LOG );

    /**
     * {@inheritDoc}
     */
    @Override
    public void info( String strEventType, String strAppEventCode, User connectedUser, Object data, String specificOrigin )
    {
        Logger logger = getLogger( specificOrigin );

        if ( logger.isInfoEnabled( ) )
        {
            String strAppId = AppPropertiesService.getProperty( AccessLoggerConstants.PROPERTY_SITE_CODE, "?" );
            String logMessage = getLogMessage( strAppId, strEventType, strAppEventCode, ( connectedUser != null ? connectedUser.getAccessCode( ) : "null" ),
                    data );

            logger.info( logMessage );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug( String strEventType, String strAppEventCode, User connectedUser, Object data, String specificOrigin )
    {
        Logger logger = getLogger( specificOrigin );

        if ( logger.isDebugEnabled( ) )
        {
            String strAppId = AppPropertiesService.getProperty( AccessLoggerConstants.PROPERTY_SITE_CODE, "?" );
            String logMessage = getLogMessage( strAppId, strEventType, strAppEventCode, ( connectedUser != null ? connectedUser.getAccessCode( ) : "null" ),
                    data );

            logger.debug( logMessage );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void trace( String strEventType, String strAppEventCode, User connectedUser, Object data, String specificOrigin )
    {
        Logger logger = getLogger( specificOrigin );

        if ( logger.isTraceEnabled( ) )
        {
            String strAppId = AppPropertiesService.getProperty( AccessLoggerConstants.PROPERTY_SITE_CODE, "?" );
            String logMessage = getLogMessage( strAppId, strEventType, strAppEventCode, ( connectedUser != null ? connectedUser.getAccessCode( ) : "null" ),
                    data );

            logger.trace( logMessage );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn( String strEventType, String strAppEventCode, User connectedUser, Object data, String specificOrigin )
    {
        Logger logger = getLogger( specificOrigin );

        if ( logger.isEnabled( Level.WARN ) )
        {
            String strAppId = AppPropertiesService.getProperty( AccessLoggerConstants.PROPERTY_SITE_CODE, "?" );
            String logMessage = getLogMessage( strAppId, strEventType, strAppEventCode, ( connectedUser != null ? connectedUser.getAccessCode( ) : "null" ),
                    data );

            logger.warn( logMessage );
        }
    }

    /**
     * build log message
     *
     * @param eventType
     * @param description
     * @param connectedUserLogin
     * @param data
     * @return the log message
     */
    private String getLogMessage( String strAppId, String strEventType, String strAppEventCode, String strConnectedUserLogin, Object data )
    {

        String jsonData = "";

        if ( data != null )
        {
            ObjectMapper obj = new ObjectMapper( );

            try
            {
                jsonData = obj.writeValueAsString( data );
            }
            catch( JsonProcessingException e )
            {
                jsonData = ERROR_MSG;
            }
        }

        return getLogMessage( strAppId, strEventType, strAppEventCode, strConnectedUserLogin, jsonData, isAddHashToLogs( ) );
    }

    /**
     * build log message
     *
     * @param eventType
     * @param description
     * @param connectedUserLogin
     * @param data
     * @return the log message
     */
    private String getLogMessage( String strAppId, String strEventType, String strAppEventCode, String strConnectedUserLogin, String strData,
            boolean isAddHashToLogs )
    {

        String strHash = "";

        if ( isAddHashToLogs )
        {
            strHash = getHash( MessageFormat.format( _messageFormat, "", strAppId, strEventType, strAppEventCode, strConnectedUserLogin, strData ) );
        }

        return MessageFormat.format( _messageFormat, strHash, strAppId, strEventType, strAppEventCode, strConnectedUserLogin, strData );
    }

    /**
     * get hash
     *
     * @param message
     * @param last
     *            hash
     *
     * @return the hash in String
     */
    private static String getHash( String message )
    {

        byte [ ] byteChaine;
        try
        {
            byteChaine = message.getBytes( CONSTANT_HASH_ENCODING );
            MessageDigest md = MessageDigest.getInstance( CONSTANT_HASH_DIGEST );
            byte [ ] hash = md.digest( byteChaine );

            // convert byte array to Hexadecimal String
            StringBuilder sb = new StringBuilder( 2 * hash.length );
            for ( byte b : hash )
            {
                sb.append( String.format( "%02x", b & 0xff ) );
            }

            return sb.toString( );

        }
        catch( UnsupportedEncodingException | NoSuchAlgorithmException e )
        {
            return "Hash ERROR : " + e.getLocalizedMessage( );
        }

    }

    /**
     * verify hash
     *
     * @param message
     *
     * @return true if the hash contained in the message is valid
     */
    public boolean verifyMessageHash( String message )
    {
        try
        {

            int idx = message.indexOf( _messageFormatSeparator, message.indexOf( DEFAULT_LOGGER_ACCESS_LOG ) );
            String hash = message.substring( idx + 1, idx + 33 );
            String data = "||" + message.substring( idx + 34 );

            return ( hash != null && ( hash.equals( "" ) || hash.equals( getHash( data ) ) ) );

        }
        catch( StringIndexOutOfBoundsException e )
        {

            return false;
        }
    }

    /**
     * is addHashToLogs enabled
     * 
     * @return true if addHashToLogs enabled
     */
    public boolean isAddHashToLogs( )
    {
        return _bAddHashToLogs;
    }

    /**
     * get logger
     * 
     * @param strSpecificLogger
     * @return the logger
     */
    private Logger getLogger( String specificOrigin )
    {
        if ( specificOrigin != null && !"".equals( specificOrigin ) )
        {
            return LogManager.getLogger( DEFAULT_LOGGER_ACCESS_LOG + "." + specificOrigin );
        }

        return _defaultLogger;

    }
}
