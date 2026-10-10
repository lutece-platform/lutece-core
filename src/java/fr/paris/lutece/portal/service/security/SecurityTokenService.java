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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 *
 * This class provides a security service for getting and verify tokens
 *
 */
@ApplicationScoped
public class SecurityTokenService implements ISecurityTokenService
{
    public static final String MARK_TOKEN = "token";
    public static final String PARAMETER_TOKEN = "token";
    // Former store: one map of every token in a single attribute
    private static final String PARAMETER_SESSION_TOKENS = "tokens";
    // One session attribute per token: lutece.token.<action>|<token>. Requests of a session served by several instances change
    // different attributes, so a token consumed on one instance is not restored, nor a new one lost, by a concurrent request on
    // another instance (a distributed session writes back every attribute a request has read)
    private static final String PREFIX_TOKEN_ATTRIBUTE = "lutece.token.";
    private static final char SEPARATOR_ACTION = '|';
    private static final String PROPERTY_MAX_TOKENS_PER_ACTION = "lutece.security.token.maxPerAction";
    // A token is removed when it is validated only: every display of a page with a form (login box on every portal page, step of a form...) added
    // one that is never removed when the user leaves the page. Only the most recent tokens of an action are kept.
    private static final int MAX_TOKENS_PER_ACTION = Math.max( 1, AppPropertiesService.getPropertyInt( PROPERTY_MAX_TOKENS_PER_ACTION, 50 ) );
    // Orders the tokens created in the same millisecond on this instance
    private static final AtomicLong _sequence = new AtomicLong( );
    // Tokens being validated on this instance: a token submitted twice at the same time is accepted once
    private final Set<String> _setTokensBeingConsumed = ConcurrentHashMap.newKeySet( );

    @Inject
    private Instance<Models> modelInstance;

    /**
     * Private constructor
     */
    SecurityTokenService( )
    {
    }

    /**
     * Get the unique instance of the SecurityTokenService
     * 
     * @return The instance
     * @deprecated Use {@code @Inject} to obtain the {@link ISecurityTokenService} instance. This method will be removed in future versions.
     */
    @Deprecated( since = "8.0", forRemoval = true )
    public static ISecurityTokenService getInstance( )
    {
        return CDI.current( ).select( ISecurityTokenService.class ).get( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getToken( HttpServletRequest request, String strAction )
    {
        String strToken = generateNewKey( );
        HttpSession session = request.getSession( true );
        session.setAttribute( getAttributeName( strAction, strToken ), Boolean.TRUE );
        removeOldestTokens( session, strAction );

        fillSecurityToken( strToken );

        return strToken;
    }

    /**
     * Put the token in the MVC model
     * 
     * @param strToken
     *            the token
     */
    private void fillSecurityToken( String strToken )
    {
        if ( modelInstance.isResolvable( ) )
        {
            Models model = modelInstance.get( );
            model.put( SecurityTokenHandler.MARK_CSRF_TOKEN, strToken );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate( HttpServletRequest request, String strAction )
    {
        return consumeToken( request, strAction, request.getParameter( PARAMETER_TOKEN ) );
    }

    /**
     * Validates a token of an action and removes it: a token is used once
     * 
     * @param request
     *            the request
     * @param strAction
     *            the action
     * @param strToken
     *            the token submitted
     * @return true if the token was valid
     */
    public boolean consumeToken( HttpServletRequest request, String strAction, String strToken )
    {
        HttpSession session = request.getSession( false );

        if ( session == null || strToken == null )
        {
            return false;
        }

        String strAttributeName = getAttributeName( strAction, strToken );

        if ( !_setTokensBeingConsumed.add( strAttributeName ) )
        {
            return false;
        }

        try
        {
            if ( session.getAttribute( strAttributeName ) == null )
            {
                return false;
            }

            session.removeAttribute( strAttributeName );

            return true;
        }
        finally
        {
            _setTokensBeingConsumed.remove( strAttributeName );
        }
    }

    /**
     * Keeps the most recent tokens of an action. Only the attribute names are read: reading the values would make a distributed
     * session write them back
     * 
     * @param session
     *            the session
     * @param strAction
     *            the action
     */
    private static void removeOldestTokens( HttpSession session, String strAction )
    {
        String strPrefix = getAttributeName( strAction, "" );
        List<String> listNames = new ArrayList<>( );

        for ( String strName : Collections.list( session.getAttributeNames( ) ) )
        {
            if ( strName.startsWith( strPrefix ) )
            {
                listNames.add( strName );
            }
        }

        if ( listNames.size( ) > MAX_TOKENS_PER_ACTION )
        {
            // The token starts with its creation time: the names sort in creation order
            Collections.sort( listNames );

            for ( String strName : listNames.subList( 0, listNames.size( ) - MAX_TOKENS_PER_ACTION ) )
            {
                session.removeAttribute( strName );
            }
        }

        if ( Collections.list( session.getAttributeNames( ) ).contains( PARAMETER_SESSION_TOKENS ) )
        {
            session.removeAttribute( PARAMETER_SESSION_TOKENS );
        }
    }

    /**
     * Name of the session attribute of a token
     * 
     * @param strAction
     *            the action
     * @param strToken
     *            the token
     * @return the attribute name
     */
    private static String getAttributeName( String strAction, String strToken )
    {
        return PREFIX_TOKEN_ATTRIBUTE + strAction + SEPARATOR_ACTION + strToken;
    }

    /**
     * Generates a new token: creation time, sequence, random part
     * 
     * @return the token
     */
    private String generateNewKey( )
    {
        return String.format( "%013d%06d-%s", System.currentTimeMillis( ), _sequence.incrementAndGet( ) % 1_000_000, UUID.randomUUID( ) );
    }
}
