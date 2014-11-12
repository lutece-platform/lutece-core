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
package fr.paris.lutece.portal.service.security;

import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 *
 * This class provides a security service for getting and verify tokens
 *
 */
public class SecurityTokenService implements ISecurityTokenService
{
    public static final String MARK_TOKEN = "token";
    public static final String PARAMETER_TOKEN = "token";
    private static final String BEAN_SECURITY_TOKEN_SERVICE = "securityTokenService";
    private static final String PARAMETER_SESSION_TOKENS = "tokens";
    private static ISecurityTokenService _singleton;

    /**
     * SecurityTokenService
     */
    private SecurityTokenService(  )
    {
    }

    /**
     * Returns the instance of the singleton
     *
     * @return The instance of the singleton
     */
    public static ISecurityTokenService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = SpringContextService.getBean( BEAN_SECURITY_TOKEN_SERVICE );
        }

        return _singleton;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getToken( HttpServletRequest request, String strAction )
    {
        String strToken = generateNewKey(  );
        HttpSession session = request.getSession( true );
        Map<String, Set<String>> hashTokens;

        if ( session.getAttribute( PARAMETER_SESSION_TOKENS ) == null )
        {
            hashTokens = new HashMap<String, Set<String>>(  );
            session.setAttribute( PARAMETER_SESSION_TOKENS, hashTokens );
        }

        hashTokens = (Map<String, Set<String>>) session.getAttribute( PARAMETER_SESSION_TOKENS );

        if ( !hashTokens.containsKey( strAction ) )
        {
            hashTokens.put( strAction, new HashSet<String>(  ) );
        }

        hashTokens.get( strAction ).add( strToken );

        return strToken;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate( HttpServletRequest request, String StrAction )
    {
        HttpSession session = request.getSession( true );

        String strToken = request.getParameter( PARAMETER_TOKEN );

        if ( ( session.getAttribute( PARAMETER_SESSION_TOKENS ) != null ) &&
                ( (Map<String, Set<String>>) session.getAttribute( PARAMETER_SESSION_TOKENS ) ).containsKey( StrAction ) &&
                ( (Map<String, Set<String>>) session.getAttribute( PARAMETER_SESSION_TOKENS ) ).get( StrAction )
                      .contains( strToken ) )
        {
            ( (Map<String, Set<String>>) session.getAttribute( PARAMETER_SESSION_TOKENS ) ).get( StrAction )
              .remove( strToken );

            return true;
        }

        return false;
    }

    /**
     * Generate a new key
     *
     * @return a new key
     */
    private String generateNewKey(  )
    {
        UUID key = UUID.randomUUID(  );

        return key.toString(  );
    }
}
