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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
    private static final String PARAMETER_SESSION_TOKENS = "tokens";
    @Inject 	
    private Instance<Models> modelInstance;

    /**
     * SecurityTokenService
     */
    SecurityTokenService( )
    {
    }

    /**
     * Returns the unique instance of the {@link SecurityTokenService} service.
     * 
     * <p>This method is deprecated and is provided for backward compatibility only. 
     * For new code, use dependency injection with {@code @Inject} to obtain the 
     * {@link SecurityTokenService} instance instead.</p>
     * 
     * @return The unique instance of {@link SecurityTokenService}.
     * 
     * @deprecated Use {@code @Inject} to obtain the {@link SecurityTokenService} 
     * instance. This method will be removed in future versions.
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

        if ( session.getAttribute( PARAMETER_SESSION_TOKENS ) == null )
        {
            session.setAttribute( PARAMETER_SESSION_TOKENS, new HashMap<String, HashSet<String>>( ) );
        }

        Map<String, HashSet<String>> hashTokens = (Map<String, HashSet<String>>) session.getAttribute( PARAMETER_SESSION_TOKENS );

        if ( !hashTokens.containsKey( strAction ) )
        {
            hashTokens.put( strAction, new HashSet<>( ) );
        }

        hashTokens.get( strAction ).add( strToken );
        fillSecurityToken( strToken );
        
        return strToken;
    }
    /**
     * Fill the model with security token
     * 
     * @param strToken
     *            The token
     */
    private void fillSecurityToken( String  strToken )
    {
    	Models model = modelInstance.get();
        if (model != null) {
            model.put( SecurityTokenHandler.MARK_CSRF_TOKEN, strToken );
        }                
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate( HttpServletRequest request, String strAction )
    {
        HttpSession session = request.getSession( true );

        String strToken = request.getParameter( PARAMETER_TOKEN );

        if ( ( session.getAttribute( PARAMETER_SESSION_TOKENS ) != null )
                && ( (Map<String, Set<String>>) session.getAttribute( PARAMETER_SESSION_TOKENS ) ).containsKey( strAction )
                && ( (Map<String, Set<String>>) session.getAttribute( PARAMETER_SESSION_TOKENS ) ).get( strAction ).contains( strToken ) )
        {
            ( (Map<String, Set<String>>) session.getAttribute( PARAMETER_SESSION_TOKENS ) ).get( strAction ).remove( strToken );

            return true;
        }

        return false;
    }

    /**
     * Generate a new key
     *
     * @return a new key
     */
    private String generateNewKey( )
    {
        UUID key = UUID.randomUUID( );

        return key.toString( );
    }
}
