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
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.web.cdi.mvc.event.BeforeControllerEvent;
import fr.paris.lutece.portal.web.cdi.mvc.event.MvcEvent.ControllerInvocationType;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * Access Log service
 *
 * - Report all actions regarding authentication, user or rights management. - All messages should contain the application Id, the event type, a specific
 * application event code, the logged user, and specific contextual data. - The logger should implement the interface IAccessLogger of lutece core
 * 
 * Use "debug" or "trace" level for fine access control history, use "info" level otherwise
 * 
 * By default : - Lutece authentication, user and rights management are logged at INFO level, - action requests at DEBUG level, - views requests are logged at
 * TRACE level.
 * 
 * To log specific actions, use :
 * 
 * AccessLogService.getInstance( ).warn( String strEventType, String strAppEventCode, String strConnectedUserLogin, Object data ); AccessLogService.getInstance(
 * ).info( String strEventType, String strAppEventCode, String strConnectedUserLogin, Object data ); AccessLogService.getInstance( ).debug( String strEventType,
 * String strAppEventCode, String strConnectedUserLogin, Object data ); AccessLogService.getInstance( ).trace( String strEventType, String strAppEventCode,
 * String strConnectedUserLogin, Object data );
 * 
 * Event types are available in AccessLoggerConstants class.
 * 
 */
@ApplicationScoped
public class AccessLogService
{
    // constants
    public static final String ACCESS_LOG_FO = "fo";
    public static final String ACCESS_LOG_BO = "bo";
    @Inject 
    HttpServletRequest request;
    private List<IAccessLogger> _accessLoggerList;

    AccessLogService( )
    {
        // Ctor
    }

    /**
     * Creates a new AppLogService object.
     */
    @PostConstruct
    void initAccessLogService( )
    {
        _accessLoggerList = CDI.current().select(IAccessLogger.class).stream().toList();
    }

    /**
     * Returns the unique instance of the {@link AccessLogService} service.
     * 
     * <p>This method is deprecated and is provided for backward compatibility only. 
     * For new code, use dependency injection with {@code @Inject} to obtain the 
     * {@link AccessLogService} instance instead.</p>
     * 
     * @return The unique instance of {@link AccessLogService}.
     * 
     * @deprecated Use {@code @Inject} to obtain the {@link AccessLogService} 
     * instance. This method will be removed in future versions.
     */
    @Deprecated( since = "8.0", forRemoval = true )
    public static AccessLogService getInstance( )
    {
        return CDI.current( ).select( AccessLogService.class ).get( );
    }

    /**
     * Log action with info level
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
    public void info( String strEventType, String strAppEventCode, User connectedUser, Object data, String specificOrigin )
    {
        _accessLoggerList.forEach( accessLogger -> {
            accessLogger.info( strEventType, strAppEventCode, connectedUser, data, specificOrigin );
        } );
    }

    /**
     * Log action with info level
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
    public void info( String strEventType, String strAppEventCode, User connectedUser, Object data )
    {
        info( strEventType, strAppEventCode, connectedUser, data, null );
    }

    /**
     * Log action with Debug level
     * 
     * @param strEventType
     * @param strAppEventCode
     * @param connectedUser
     * @param data
     * @param specificOrigin
     */
    public void debug( String strEventType, String strAppEventCode, User connectedUser, Object data, String specificOrigin )
    {
        _accessLoggerList.forEach( accessLogger -> {
            accessLogger.debug( strEventType, strAppEventCode, connectedUser, data, specificOrigin );
        } );
    }

    /**
     * Log action with Debug level
     * 
     * @param strEventType
     * @param strAppEventCode
     * @param connectedUser
     * @param data
     */
    public void debug( String strEventType, String strAppEventCode, User connectedUser, Object data )
    {
        debug( strEventType, strAppEventCode, connectedUser, data, null );
    }

    /**
     * Log action with trace level
     * 
     * @param strEventType
     * @param strAppEventCode
     * @param connectedUser
     * @param data
     * @param specificOrigin
     */
    public void trace( String strEventType, String strAppEventCode, User connectedUser, Object data, String specificOrigin )
    {
        _accessLoggerList.forEach( accessLogger -> {
            accessLogger.trace( strEventType, strAppEventCode, connectedUser, data, specificOrigin );
        } );
    }

    /**
     * Log action with trace level
     * 
     * @param strEventType
     * @param strAppEventCode
     * @param connectedUser
     * @param data
     */
    public void trace( String strEventType, String strAppEventCode, User connectedUser, Object data )
    {
        trace( strEventType, strAppEventCode, connectedUser, data, null );
    }

    /**
     * Log action with warn level
     * 
     * @param strEventType
     * @param strAppEventCode
     * @param connectedUser
     * @param data
     * @param specificOrigin
     */
    public void warn( String strEventType, String strAppEventCode, User connectedUser, Object data, String specificOrigin )
    {
        _accessLoggerList.forEach( accessLogger -> {
            accessLogger.warn( strEventType, strAppEventCode, connectedUser, data, specificOrigin );
        } );
    }

    /**
     * Log action with warn level
     * 
     * @param strEventType
     * @param strAppEventCode
     * @param connectedUser
     * @param data
     */
    public void warn( String strEventType, String strAppEventCode, User connectedUser, Object data )
    {
        warn( strEventType, strAppEventCode, connectedUser, data, null );
    }
    
    /**
     * Observes all controller invocations and logs access according to context.
     *
     * @param event the controller invocation event
     */
    public void onControllerInvocation(@Observes BeforeControllerEvent event) {
        ControllerInvocationType type = event.getInvocationType();
        if( ! event.isBackOffice() ){
        	trace( type.name(), event.getInvokedMethod( ).getName( ), SecurityService.getInstance( ).getRegisteredUser( request ),
                    request.getRequestURL( ) + "?" + request.getQueryString( ), AccessLogService.ACCESS_LOG_FO );
        }else {
	    	AdminUser adminUser = AdminAuthenticationService.getInstance( ).getRegisteredUser( request );
        	trace( type.name(), event.getInvokedMethod( ).getName( ), adminUser,
	                request.getRequestURL( ) + "?" + request.getQueryString( ), AccessLogService.ACCESS_LOG_BO );
	  
        }
    	
    }
}
