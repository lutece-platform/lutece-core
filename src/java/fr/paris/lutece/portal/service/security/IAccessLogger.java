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
package fr.paris.lutece.portal.service.security;

import fr.paris.lutece.api.user.User;

/**
 * Interface for access loggers
 */
public interface IAccessLogger
{

    /**
     * Log a message object with the INFO level.
     *
     * @param strEventType
     *            the event type
     * @param strAppEventCode
     *            the event code
     * @param connectedUser
     *            the user connected
     * @param data
     *            the message object to log
     * @param specificOrigin
     *            specific origin of the event to log
     */
    public void info( String strEventType, String strAppEventCode, User connectedUser, Object data, String specificOrigin );

    /**
     * Log a message object with the INFO level.
     *
     * @param strEventType
     *            the event type
     * @param strAppEventCode
     *            the event code
     * @param connectedUser
     *            the user connected
     * @param data
     *            the message object to log
     */
    public default void info( String strEventType, String strAppEventCode, User connectedUser, Object data )
    {
        info( strEventType, strAppEventCode, connectedUser, data, null );
    }

    /**
     * Log a message object with the DEBUG level.
     *
     * @param strEventType
     *            the event type
     * @param strAppEventCode
     *            the event code
     * @param connectedUser
     *            the user connected
     * @param data
     *            the message object to log
     * @param specificOrigin
     *            specific origin of the event to log
     */
    public void debug( String strEventType, String strAppEventCode, User connectedUser, Object data, String specificOrigin );

    /**
     * Log a message object with the DEBUG level.
     *
     * @param strEventType
     *            the event type
     * @param strAppEventCode
     *            the event code
     * @param connectedUser
     *            the user connected
     * @param data
     *            the message object to log
     */
    public default void debug( String strEventType, String strAppEventCode, User connectedUser, Object data )
    {
        debug( strEventType, strAppEventCode, connectedUser, data, null );
    }

    /**
     * Log a message object with the TRACE level.
     *
     * @param strEventType
     *            the event type
     * @param strAppEventCode
     *            the event code
     * @param connectedUser
     *            the user connected
     * @param data
     *            the message object to log
     * @param specificOrigin
     *            specific origin of the event to log
     */
    public void trace( String strEventType, String strAppEventCode, User connectedUser, Object data, String specificOrigin );

    /**
     * Log a message object with the TRACE level.
     *
     * @param strEventType
     *            the event type
     * @param strAppEventCode
     *            the event code
     * @param connectedUser
     *            the user connected
     * @param data
     *            the message object to log
     */
    public default void trace( String strEventType, String strAppEventCode, User connectedUser, Object data )
    {
        trace( strEventType, strAppEventCode, connectedUser, data, null );
    }

    /**
     * Log a message object with the WARN level.
     *
     * @param strEventType
     *            the event type
     * @param strAppEventCode
     *            the event code
     * @param connectedUser
     *            the user connected
     * @param data
     *            the message object to log
     * @param specificOrigin
     *            specific origin of the event to log
     */
    public void warn( String strEventType, String strAppEventCode, User connectedUser, Object data, String specificOrigin );

    /**
     * Log a message object with the WARN level.
     *
     * @param strEventType
     *            the event type
     * @param strAppEventCode
     *            the event code
     * @param connectedUser
     *            the user connected
     * @param data
     *            the message object to log
     */
    public default void warn( String strEventType, String strAppEventCode, User connectedUser, Object data )
    {
        warn( strEventType, strAppEventCode, connectedUser, data, null );
    }

}
