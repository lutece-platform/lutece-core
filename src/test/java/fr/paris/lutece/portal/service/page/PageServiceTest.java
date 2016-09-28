/*
 * Copyright (c) 2002-2015, Mairie de Paris
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
package fr.paris.lutece.portal.service.page;

import fr.paris.lutece.portal.service.cache.CacheService;
import fr.paris.lutece.portal.service.cache.CacheableService;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.MokeHttpServletRequest;

import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class PageServiceTest extends LuteceTestCase
{
    private boolean _pageCacheStatus;

    @Override
    public void setUp( ) throws Exception
    {
        super.setUp( );

        // activate the page cache
        List<CacheableService> services = CacheService.getCacheableServicesList( );

        for ( CacheableService cs : services )
        {
            if ( cs instanceof PageCacheService )
            {
                _pageCacheStatus = cs.isCacheEnable( );
                cs.enableCache( true );

                break;
            }
        }
    }

    @Override
    public void tearDown( ) throws Exception
    {
        // restore the previous page cache status
        List<CacheableService> services = CacheService.getCacheableServicesList( );

        for ( CacheableService cs : services )
        {
            if ( cs instanceof PageCacheService )
            {
                cs.enableCache( _pageCacheStatus );

                break;
            }
        }

        super.tearDown( );
    }

    /**
     * Tests that the base URL is correct when the page is cached and the URL used to access the site changes
     * 
     * @throws SiteMessageException
     */
    @Test
    public void testBaseUrl( ) throws SiteMessageException
    {
        IPageService pageService = (IPageService) SpringContextService.getBean( "pageService" );
        HttpServletResponse response = new MokeHttpServletResponse( );

        // FIXME : rework when LUTECE-1837 is resolved
        MokeHttpServletRequest request = new MokeHttpServletRequest( )
        {
            @Override
            public Object getAttribute( String string )
            {
                return null;
            }
        };

        LocalVariables.setLocal( null, request, response );
        request.addMokeParameters( Parameters.PAGE_ID, "1" );

        String pageContent = pageService.getPage( request, 0 );
        assertTrue( pageContent.contains( "<base href=\"://:0/\">" ) );

        // change server name and thus base_url
        request = new MokeHttpServletRequest( )
        {
            @Override
            public String getServerName( )
            {
                return "junit";
            }

            // FIXME : rework when LUTECE-1837 is resolved
            @Override
            public Object getAttribute( String string )
            {
                return null;
            }
        };
        LocalVariables.setLocal( null, request, response );
        request.addMokeParameters( Parameters.PAGE_ID, "1" );
        pageContent = pageService.getPage( request, 0 );
        assertTrue( pageContent.contains( "<base href=\"://junit:0/\">" ) );
    }

    // FIXME : remove when LUTECE-1838 is resolved
    private static final class MokeHttpServletResponse implements HttpServletResponse
    {
        @Override
        public void setLocale( Locale loc )
        {
            // TODO Auto-generated method stub
        }

        @Override
        public void setContentType( String type )
        {
            // TODO Auto-generated method stub
        }

        @Override
        public void setContentLength( int len )
        {
            // TODO Auto-generated method stub
        }

        @Override
        public void setCharacterEncoding( String charset )
        {
            // TODO Auto-generated method stub
        }

        @Override
        public void setBufferSize( int size )
        {
            // TODO Auto-generated method stub
        }

        @Override
        public void resetBuffer( )
        {
            // TODO Auto-generated method stub
        }

        @Override
        public void reset( )
        {
            // TODO Auto-generated method stub
        }

        @Override
        public boolean isCommitted( )
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public PrintWriter getWriter( ) throws IOException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public ServletOutputStream getOutputStream( ) throws IOException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Locale getLocale( )
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getContentType( )
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getCharacterEncoding( )
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public int getBufferSize( )
        {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void flushBuffer( ) throws IOException
        {
            // TODO Auto-generated method stub
        }

        @Override
        public void setStatus( int sc, String sm )
        {
            // TODO Auto-generated method stub
        }

        @Override
        public void setStatus( int sc )
        {
            // TODO Auto-generated method stub
        }

        @Override
        public void setIntHeader( String name, int value )
        {
            // TODO Auto-generated method stub
        }

        @Override
        public void setHeader( String name, String value )
        {
            // TODO Auto-generated method stub
        }

        @Override
        public void setDateHeader( String name, long date )
        {
            // TODO Auto-generated method stub
        }

        @Override
        public void sendRedirect( String location ) throws IOException
        {
            // TODO Auto-generated method stub
        }

        @Override
        public void sendError( int sc, String msg ) throws IOException
        {
            // TODO Auto-generated method stub
        }

        @Override
        public void sendError( int sc ) throws IOException
        {
            // TODO Auto-generated method stub
        }

        @Override
        public String encodeUrl( String url )
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String encodeURL( String url )
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String encodeRedirectUrl( String url )
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String encodeRedirectURL( String url )
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean containsHeader( String name )
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void addIntHeader( String name, int value )
        {
            // TODO Auto-generated method stub
        }

        @Override
        public void addHeader( String name, String value )
        {
            // TODO Auto-generated method stub
        }

        @Override
        public void addDateHeader( String name, long date )
        {
            // TODO Auto-generated method stub
        }

        @Override
        public void addCookie( Cookie cookie )
        {
            // TODO Auto-generated method stub
        }

        @Override
        public String getHeader(String arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Collection<String> getHeaderNames() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Collection<String> getHeaders(String arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public int getStatus() {
            // TODO Auto-generated method stub
            return 0;
        }
    }
}
