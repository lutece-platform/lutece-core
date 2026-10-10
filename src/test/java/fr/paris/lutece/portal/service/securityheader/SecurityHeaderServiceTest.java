/*
 * Copyright (c) 2002-2026, City of Paris
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
package fr.paris.lutece.portal.service.securityheader;

import java.util.Collection;
import java.util.Locale;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.securityheader.SecurityHeader;
import fr.paris.lutece.portal.business.securityheader.SecurityHeaderHome;
import fr.paris.lutece.portal.business.securityheader.SecurityHeaderPageCategory;
import fr.paris.lutece.portal.business.securityheader.SecurityHeaderType;
import fr.paris.lutece.test.LuteceTestCase;
import jakarta.inject.Inject;

/**
 * Tests the active security headers served to the filters on every request.
 */
public class SecurityHeaderServiceTest extends LuteceTestCase
{
    private static final String TYPE = SecurityHeaderType.PAGE.getCode( );
    private static final String CATEGORY = SecurityHeaderPageCategory.ALL.getCode( );

    @Inject
    private SecurityHeaderService _service;
    private SecurityHeader _header;

    /**
     * Creates an active header.
     */
    @BeforeEach
    protected void createHeader( )
    {
        _header = new SecurityHeader( );
        _header.setName( "X-Test-" + UUID.randomUUID( ) );
        _header.setValue( "test" );
        _header.setDescription( "test" );
        _header.setType( TYPE );
        _header.setPageCategory( CATEGORY );
        _service.create( _header );
        _service.enable( _header.getId( ) );
    }

    /**
     * Removes the header.
     */
    @AfterEach
    protected void removeHeader( )
    {
        SecurityHeaderHome.remove( _header.getId( ) );
        _service.getSecurityHeaderCache( ).resetCache( );
    }

    private boolean isActive( )
    {
        Collection<SecurityHeader> headers = _service.findActive( TYPE, CATEGORY );
        return headers != null && headers.stream( ).anyMatch( header -> header.getName( ).equals( _header.getName( ) ) );
    }

    /**
     * A change made on this instance resets the cache: the headers are read again.
     */
    @Test
    public void testChangeOnThisInstance( )
    {
        assertTrue( isActive( ) );
        _service.disable( _header.getId( ) );
        assertFalse( isActive( ) );
    }

    /**
     * Another instance reloaded the headers after a change (the cache is shared): this instance rebuilds its active headers.
     */
    @Test
    public void testReloadByAnotherInstance( )
    {
        Assumptions.assumeTrue( _service.getSecurityHeaderCache( ).isCacheEnable( ), "the security header cache is disabled" );
        assertTrue( isActive( ) );

        SecurityHeaderHome.updateIsActive( _header.getId( ), false );
        SecurityHeader loaded = new SecurityHeader( );
        loaded.setId( -1 );
        loaded.setName( SecurityHeaderService.KEY_LOADED );
        loaded.setValue( UUID.randomUUID( ).toString( ) );
        _service.getSecurityHeaderCache( ).put( SecurityHeaderService.KEY_LOADED, loaded );

        assertFalse( isActive( ) );
    }

    /**
     * The load marker is not a security header.
     */
    @Test
    public void testLoadMarkerNotListed( )
    {
        assertTrue( isActive( ) );
        assertTrue( _service.findAllSorted( Locale.FRENCH ).stream( ).noneMatch( header -> SecurityHeaderService.KEY_LOADED.equals( header.getName( ) ) ) );
        assertTrue( _service.findAllSorted( Locale.FRENCH ).stream( ).anyMatch( header -> header.getName( ).equals( _header.getName( ) ) ) );
    }
}
