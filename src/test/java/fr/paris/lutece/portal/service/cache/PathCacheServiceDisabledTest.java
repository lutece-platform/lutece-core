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
package fr.paris.lutece.portal.service.cache;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.inject.Inject;

public class PathCacheServiceDisabledTest extends LuteceTestCase
{
    private @Inject PathCacheService service;
    boolean bEnabled;

    @BeforeEach
    protected void setUp( ) throws Exception
    {
        bEnabled = service.isCacheEnable( );
        service.enableCache( false );
        assertNotNull( service );
    }

    
    @AfterEach
    protected void tearDown( ) throws Exception
    {
        service.enableCache( bEnabled );
    }

    @Test
    public void testGetKey( )
    {
        assertNull( service.getKey( "junit", 0, new MockHttpServletRequest( ) ) );
        assertNull( service.getKey( "junit", 0, "junit", new MockHttpServletRequest( ) ) );
    }

    @Test
    public void testPutAndGetFromCache( )
    {
        try
        {
            service.put( "junit1", "junit" );
            String key = service.getKey( "junit1", 0, null );
            assertNull( key );
            assertNull( service.get( "junit1" ) );
            assertNull( service.get( "NotInCache" ) );
        }
        catch( IllegalStateException e )
        {
            // This failure can be legit because the cache is disabled and if ehcache is used as implementation then the exception will be thrown
        }
    }

}
