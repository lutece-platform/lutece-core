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
package fr.paris.lutece.portal.web.includes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.cache.configuration.Configuration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.service.cache.CacheService;
import fr.paris.lutece.portal.service.cache.CacheableService;
import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockServletContext;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

public abstract class LinksIncludeTest extends LuteceTestCase
{
    private static final String PLUGIN_NAME = "linksIncludeTestPlugin";
    private boolean bOrigCacheEnabled;

    @Inject
    private LinksIncludeCacheService _cacheService;
    
    public abstract boolean enableCache( );

    @BeforeEach
    protected void before( ) throws Exception
    {
        _cacheService.getInfos( );
        File dirPlugin = new File( AppPathService.getPath( "path.plugins" ) );
        File testPluginFile = new File( dirPlugin, PLUGIN_NAME + ".xml" );
        try ( BufferedWriter writer = new BufferedWriter( new FileWriter( testPluginFile ) ) )
        {
            writer.write( "<plug-in><name>" + PLUGIN_NAME + "</name><class>" + LinksIncludeTestPlugin.class.getName( ) + "</class>"
                    + "<icon-url>../../images/admin/skin/plugins/myplugin/myplugin.gif</icon-url></plug-in>" );
        }
        PluginService.init( );
        PluginService.getPlugin( PLUGIN_NAME ).install( );
        List<CacheableService<?, ?>> caches = CacheService.getCacheableServicesList( );
        for ( CacheableService cacheService : caches )
        {
            if ( LinksIncludeCacheService.SERVICE_NAME.equals( cacheService.getName( ) ) )
            {
                bOrigCacheEnabled = cacheService.isCacheEnable( );
                cacheService.enableCache( enableCache( ) );
                break;
            }
        }
    }

    @AfterEach
    protected void after( ) throws Exception
    {
        List<CacheableService<?, ?>> caches = CacheService.getCacheableServicesList( );
        for ( CacheableService cacheService : caches )
        {
            if ( LinksIncludeCacheService.SERVICE_NAME.equals( cacheService.getName( ) ) )
            {
                cacheService.enableCache( bOrigCacheEnabled );
                break;
            }
        }
        PluginService.getPlugin( PLUGIN_NAME ).uninstall( );
        File dirPlugin = new File( AppPathService.getPath( "path.plugins" ) );
        File testPluginFile = new File( dirPlugin, PLUGIN_NAME + ".xml" );
        testPluginFile.delete( );
        PluginService.init( );
    }

    @Test
    public void testFillTemplateNull( )
    {
        try
        {
            LinksInclude include = new LinksInclude( );
            include.fillTemplate( null, null, 0, null );
            // did not throw
        }
        catch( Exception e )
        {
            fail( );
        }
    }

    @Test
    public void testGetURICssAddPrefix( )
    {
        LinksIncludeTestPlugin plugin = (LinksIncludeTestPlugin) PluginService.getPlugin( PLUGIN_NAME );
        List<String> styleSheets = new ArrayList<>( );
        styleSheets.add( PLUGIN_NAME + "/style.css" );
        plugin.setCssStyleSheets( styleSheets );
        plugin.setCssStyleSheetsScopePortal( true );
        LinksInclude include = new LinksInclude( );
        Map<String, Object> rootModel = new HashMap<>( );
        PageData data = new PageData( );
        int nMode = 0;
        HttpServletRequest request = new MockHttpServletRequest( );
        try {
            include.fillTemplate( rootModel, data, nMode, request );            
            String cssLinks = (String) rootModel.get( "plugins_css_links" );
            assertEquals( "<link rel=\"stylesheet\"  href=\"css/plugins/linksIncludeTestPlugin/style.css\" type=\"text/css\" crossorigin=\"anonymous\" media=\"screen\" />",
                    cssLinks.replace( "\n", "" ).replace( "\r", "" ) );
        }
        catch( IllegalStateException e )
        {
            if ( enableCache( ) )
            {
                fail(e);
            } else {
                // This failure is legit because the cache is disabled and if ehcache is used as implementation then the exception will be thrown                
            }
        }
    }

    @Test
    public void testGetURICssAbsoluteHttp( )
    {
        LinksIncludeTestPlugin plugin = (LinksIncludeTestPlugin) PluginService.getPlugin( PLUGIN_NAME );
        List<String> styleSheets = new ArrayList<>( );
        styleSheets.add( "http://example.com/style.css" );
        plugin.setCssStyleSheets( styleSheets );
        plugin.setCssStyleSheetsScopePortal( true );
        LinksInclude include = new LinksInclude( );
        Map<String, Object> rootModel = new HashMap<>( );
        PageData data = new PageData( );
        int nMode = 0;
        HttpServletRequest request = new MockHttpServletRequest( );
        try {
            include.fillTemplate( rootModel, data, nMode, request );
            String cssLinks = (String) rootModel.get( "plugins_css_links" );
            assertEquals( "<link rel=\"stylesheet\"  href=\"http://example.com/style.css\" type=\"text/css\" crossorigin=\"anonymous\" media=\"screen\" />",
                    cssLinks.replace( "\n", "" ).replace( "\r", "" ) );
        }
        catch( IllegalStateException e )
        {
            if ( enableCache( ) )
            {
                fail(e);
            } else {
                // This failure is legit because the cache is disabled and if ehcache is used as implementation then the exception will be thrown                
            }
        }
    }

    @Test
    public void testGetURICssAbsoluteHttps( )
    {
        LinksIncludeTestPlugin plugin = (LinksIncludeTestPlugin) PluginService.getPlugin( PLUGIN_NAME );
        List<String> styleSheets = new ArrayList<>( );
        styleSheets.add( "https://example.com/style.css" );
        plugin.setCssStyleSheets( styleSheets );
        plugin.setCssStyleSheetsScopePortal( true );
        LinksInclude include = new LinksInclude( );
        Map<String, Object> rootModel = new HashMap<>( );
        PageData data = new PageData( );
        int nMode = 0;
        HttpServletRequest request = new MockHttpServletRequest( );
        try {
            include.fillTemplate( rootModel, data, nMode, request );
            String cssLinks = (String) rootModel.get( "plugins_css_links" );
            assertEquals( "<link rel=\"stylesheet\"  href=\"https://example.com/style.css\" type=\"text/css\" crossorigin=\"anonymous\" media=\"screen\" />",
                    cssLinks.replace( "\n", "" ).replace( "\r", "" ) );
        }
        catch( IllegalStateException e )
        {
            if ( enableCache( ) )
            {
                fail(e);
            } else {
                // This failure is legit because the cache is disabled and if ehcache is used as implementation then the exception will be thrown                
            }
        }
    }

    @Test
    public void testGetURICssProtocolRelative( )
    {
        LinksIncludeTestPlugin plugin = (LinksIncludeTestPlugin) PluginService.getPlugin( PLUGIN_NAME );
        List<String> styleSheets = new ArrayList<>( );
        styleSheets.add( "//example.com/style.css" );
        plugin.setCssStyleSheets( styleSheets );
        plugin.setCssStyleSheetsScopePortal( true );
        LinksInclude include = new LinksInclude( );
        Map<String, Object> rootModel = new HashMap<>( );
        PageData data = new PageData( );
        int nMode = 0;
        HttpServletRequest request = new MockHttpServletRequest( );
        try {            
            include.fillTemplate( rootModel, data, nMode, request );
            String cssLinks = (String) rootModel.get( "plugins_css_links" );
            assertEquals( "<link rel=\"stylesheet\"  href=\"//example.com/style.css\" type=\"text/css\" crossorigin=\"anonymous\" media=\"screen\" />",
                    cssLinks.replace( "\n", "" ).replace( "\r", "" ) );
        }
        catch( IllegalStateException e )
        {
            if ( enableCache( ) )
            {
                fail(e);
            } else {
                // This failure is legit because the cache is disabled and if ehcache is used as implementation then the exception will be thrown                
            }
        }
    }

    @Test
    public void testGetURICssHash( ) throws IOException
    {
        LinksIncludeTestPlugin plugin = (LinksIncludeTestPlugin) PluginService.getPlugin( PLUGIN_NAME );
        List<String> styleSheets = new ArrayList<>( );
        styleSheets.add( "linksIncludeTestPlugin/junithashed.css" );
        File hashedFile = new File( getResourcesDir( ), "css/plugins/linksIncludeTestPlugin/junithashed.css" );
        System.out.println( hashedFile.toString( ) );
        hashedFile.getParentFile( ).mkdirs( );
        try ( FileWriter writer = new FileWriter( hashedFile ) )
        {
            writer.write( "abcd" );
        }
        try
        {
            plugin.setCssStyleSheets( styleSheets );
            plugin.setCssStyleSheetsScopePortal( true );
            LinksInclude include = new LinksInclude( );
            Map<String, Object> rootModel = new HashMap<>( );
            PageData data = new PageData( );
            int nMode = 0;
            ServletContext servletContext = new MockServletContext( )
            {

                @Override
                public InputStream getResourceAsStream( String path )
                {
                    File file = new File( getResourcesDir( ), path );
                    if ( file.exists( ) )
                    {
                        try
                        {
                            return new FileInputStream( file );
                        }
                        
                        catch( FileNotFoundException e )
                        {
                            return null;
                        }
                    }
                    return null;
                }

            };
            MockHttpServletRequest request = new MockHttpServletRequest( servletContext );
            try {            
                include.fillTemplate( rootModel, data, nMode, request );
                String cssLinks = (String) rootModel.get( "plugins_css_links" );

                assertEquals(
                        "<link rel=\"stylesheet\"  href=\"css/plugins/linksIncludeTestPlugin/junithashed.css?lutece_h=88d4266fd4e6338d13b845fcf289579d209c897823b9217da3e161936f031589\" type=\"text/css\" crossorigin=\"anonymous\" media=\"screen\" />",
                        cssLinks.replace( "\n", "" ).replace( "\r", "" ) );
                try ( FileWriter writer = new FileWriter( hashedFile ) )
                {
                    writer.write( "bbcd" );
                }
                include.fillTemplate( rootModel, data, nMode, request );
                String cssLinks2 = (String) rootModel.get( "plugins_css_links" );
                assertNotNull( cssLinks2 );
                if ( enableCache( ) )
                {
                	if(_cacheService.getConfiguration(Configuration.class).isStoreByValue())
                	{
                        assertEquals( cssLinks, cssLinks2 );
                	}else {
                        assertSame( cssLinks, cssLinks2 );
                	}
                }
                else
                {
                    assertFalse( cssLinks.equals( cssLinks2 ) );
                    assertEquals(
                            "<link rel=\"stylesheet\"  href=\"css/plugins/linksIncludeTestPlugin/junithashed.css?lutece_h=531ba794ef006cd3d69cf1acb33ddeccf8d6c655fb08f469335f8c2c32e2ab68\" type=\"text/css\" crossorigin=\"anonymous\" media=\"screen\" />",
                            cssLinks2.replace( "\n", "" ).replace( "\r", "" ) );
                }
            }
            catch( IllegalStateException e )
            {
                if ( enableCache( ) )
                {
                    fail(e);
                } else {
                    // This failure is legit because the cache is disabled and if ehcache is used as implementation then the exception will be thrown                
                }
            }
        }
        finally
        {
            hashedFile.delete( );
        }
    }

    @Test
    public void testGetURICssHashQuery( ) throws IOException
    {
        LinksIncludeTestPlugin plugin = (LinksIncludeTestPlugin) PluginService.getPlugin( PLUGIN_NAME );
        List<String> styleSheets = new ArrayList<>( );
        styleSheets.add( "linksIncludeTestPlugin/junithashed.css?arg=value" );
        File hashedFile = new File( getResourcesDir( ), "css/plugins/linksIncludeTestPlugin/junithashed.css" );
        System.out.println( hashedFile.toString( ) );
        hashedFile.getParentFile( ).mkdirs( );
        try ( FileWriter writer = new FileWriter( hashedFile ) )
        {
            writer.write( "abcd" );
        }
        try
        {
            plugin.setCssStyleSheets( styleSheets );
            plugin.setCssStyleSheetsScopePortal( true );
            LinksInclude include = new LinksInclude( );
            Map<String, Object> rootModel = new HashMap<>( );
            PageData data = new PageData( );
            int nMode = 0;
            ServletContext servletContext = new MockServletContext( )
            {

                @Override
                public InputStream getResourceAsStream( String path )
                {
                    File file = new File( getResourcesDir( ), path );
                    if ( file.exists( ) )
                    {
                        try
                        {
                            return new FileInputStream( file );
                        }
                        catch( FileNotFoundException e )
                        {
                            return null;
                        }
                    }
                    return null;
                }

            };
            MockHttpServletRequest request = new MockHttpServletRequest( servletContext );
            try {            
                include.fillTemplate( rootModel, data, nMode, request );
                String cssLinks = (String) rootModel.get( "plugins_css_links" );
                assertEquals(
                        "<link rel=\"stylesheet\"  href=\"css/plugins/linksIncludeTestPlugin/junithashed.css?arg=value&lutece_h=88d4266fd4e6338d13b845fcf289579d209c897823b9217da3e161936f031589\" type=\"text/css\" crossorigin=\"anonymous\" media=\"screen\" />",
                        cssLinks.replace( "\n", "" ).replace( "\r", "" ) );
            }
            catch( IllegalStateException e )
            {
                if ( enableCache( ) )
                {
                    fail(e);
                } else {
                    // This failure is legit because the cache is disabled and if ehcache is used as implementation then the exception will be thrown                
                }
            }
        }
        finally
        {
            hashedFile.delete( );
        }
    }

    @Test
    public void testGetURICssHashDigestError( ) throws IOException
    {
        LinksIncludeTestPlugin plugin = (LinksIncludeTestPlugin) PluginService.getPlugin( PLUGIN_NAME );
        List<String> styleSheets = new ArrayList<>( );
        styleSheets.add( "linksIncludeTestPlugin/junithashed.css" );
        File hashedFile = new File( getResourcesDir( ), "css/plugins/linksIncludeTestPlugin/junithashed.css" );
        System.out.println( hashedFile.toString( ) );
        hashedFile.getParentFile( ).mkdirs( );
        try ( FileWriter writer = new FileWriter( hashedFile ) )
        {
            writer.write( "abcd" );
        }
        try
        {
            plugin.setCssStyleSheets( styleSheets );
            plugin.setCssStyleSheetsScopePortal( true );
            LinksInclude include = new LinksInclude( );
            Map<String, Object> rootModel = new HashMap<>( );
            PageData data = new PageData( );
            int nMode = 0;
            ServletContext servletContext = new MockServletContext( )
            {

                @Override
                public InputStream getResourceAsStream( String path )
                {
                    File file = new File( getResourcesDir( ), path );
                    if ( file.exists( ) )
                    {
                        return new InputStream( )
                        {

                            @Override
                            public int read( ) throws IOException
                            {
                                throw new IOException( "Mocking IO error" );
                            }
                        };
                    }
                    return null;
                }

            };
            MockHttpServletRequest request = new MockHttpServletRequest( servletContext );
            try {            
                include.fillTemplate( rootModel, data, nMode, request );
                String cssLinks = (String) rootModel.get( "plugins_css_links" );
                assertEquals( "<link rel=\"stylesheet\"  href=\"css/plugins/linksIncludeTestPlugin/junithashed.css\" type=\"text/css\" crossorigin=\"anonymous\" media=\"screen\" />",
                        cssLinks.replace( "\n", "" ).replace( "\r", "" ) );
            }
            catch( IllegalStateException e )
            {
                if ( enableCache( ) )
                {
                    fail(e);
                } else {
                    // This failure is legit because the cache is disabled and if ehcache is used as implementation then the exception will be thrown                
                }
            }
        }
        finally
        {
            hashedFile.delete( );
        }
    }

    @Test
    public void testGetURICssHashStreamCloseError( ) throws IOException
    {
        LinksIncludeTestPlugin plugin = (LinksIncludeTestPlugin) PluginService.getPlugin( PLUGIN_NAME );
        List<String> styleSheets = new ArrayList<>( );
        styleSheets.add( "linksIncludeTestPlugin/junithashed.css" );
        File hashedFile = new File( getResourcesDir( ), "css/plugins/linksIncludeTestPlugin/junithashed.css" );
        System.out.println( hashedFile.toString( ) );
        hashedFile.getParentFile( ).mkdirs( );
        try ( FileWriter writer = new FileWriter( hashedFile ) )
        {
            writer.write( "abcd" );
        }
        try
        {
            plugin.setCssStyleSheets( styleSheets );
            plugin.setCssStyleSheetsScopePortal( true );
            LinksInclude include = new LinksInclude( );
            Map<String, Object> rootModel = new HashMap<>( );
            PageData data = new PageData( );
            int nMode = 0;
            ServletContext servletContext = new MockServletContext( )
            {

                @Override
                public InputStream getResourceAsStream( String path )
                {
                    File file = new File( getResourcesDir( ), path );
                    if ( file.exists( ) )
                    {
                        try
                        {
                            return new FileInputStream( file )
                            {

                                @Override
                                public void close( ) throws IOException
                                {
                                    throw new IOException( "Mocking IO error on close" );
                                }

                            };
                        }
                        catch( FileNotFoundException e )
                        {
                            return null;
                        }
                    }
                    return null;
                }

            };
            MockHttpServletRequest request = new MockHttpServletRequest( servletContext );
            try {            
                include.fillTemplate( rootModel, data, nMode, request );
                String cssLinks = (String) rootModel.get( "plugins_css_links" );
                assertEquals(
                        "<link rel=\"stylesheet\"  href=\"css/plugins/linksIncludeTestPlugin/junithashed.css?lutece_h=88d4266fd4e6338d13b845fcf289579d209c897823b9217da3e161936f031589\" type=\"text/css\" crossorigin=\"anonymous\" media=\"screen\" />",
                        cssLinks.replace( "\n", "" ).replace( "\r", "" ) );
            }
            catch( IllegalStateException e )
            {
                if ( enableCache( ) )
                {
                    fail(e);
                } else {
                    // This failure is legit because the cache is disabled and if ehcache is used as implementation then the exception will be thrown                
                }
            }
        }
        finally
        {
            hashedFile.delete( );
        }
    }

    @Test
    public void testGetURICssBadURI( )
    {
        LinksIncludeTestPlugin plugin = (LinksIncludeTestPlugin) PluginService.getPlugin( PLUGIN_NAME );
        List<String> styleSheets = new ArrayList<>( );
        styleSheets.add( "://style.css" );
        plugin.setCssStyleSheets( styleSheets );
        plugin.setCssStyleSheetsScopePortal( true );
        LinksInclude include = new LinksInclude( );
        Map<String, Object> rootModel = new HashMap<>( );
        PageData data = new PageData( );
        int nMode = 0;
        HttpServletRequest request = new MockHttpServletRequest( );
        try {            
            include.fillTemplate( rootModel, data, nMode, request );
            String cssLinks = (String) rootModel.get( "plugins_css_links" );
            assertEquals( "", cssLinks );
        }
        catch( IllegalStateException e )
        {
            if ( enableCache( ) )
            {
                fail(e);
            } else {
                // This failure is legit because the cache is disabled and if ehcache is used as implementation then the exception will be thrown                
            }
        }
    }

    // Javscript link
    @Test
    public void testGetURIJavascriptAddPrefix( )
    {
        LinksIncludeTestPlugin plugin = (LinksIncludeTestPlugin) PluginService.getPlugin( PLUGIN_NAME );
        List<String> javascripts = new ArrayList<>( );
        javascripts.add( PLUGIN_NAME + "/script.js" );
        plugin.setJavascriptFiles( javascripts );
        plugin.setJavascriptFilesScopePortal( true );
        LinksInclude include = new LinksInclude( );
        Map<String, Object> rootModel = new HashMap<>( );
        PageData data = new PageData( );
        int nMode = 0;
        HttpServletRequest request = new MockHttpServletRequest( );
        try {            
            include.fillTemplate( rootModel, data, nMode, request );
            String cssLinks = (String) rootModel.get( "plugins_javascript_links" );
            assertEquals( "<script src=\"js/plugins/linksIncludeTestPlugin/script.js\"></script>", cssLinks.replace( "\n", "" ).replace( "\r", "" ) );
        }
        catch( IllegalStateException e )
        {
            if ( enableCache( ) )
            {
                fail(e);
            } else {
                // This failure is legit because the cache is disabled and if ehcache is used as implementation then the exception will be thrown                
            }
        }
    }
    @Test
    public void testGetURIJavascriptAbsoluteHttp( )
    {
        LinksIncludeTestPlugin plugin = (LinksIncludeTestPlugin) PluginService.getPlugin( PLUGIN_NAME );
        List<String> javascripts = new ArrayList<>( );
        javascripts.add( "http://example.com/script.js" );
        plugin.setJavascriptFiles( javascripts );
        plugin.setJavascriptFilesScopePortal( true );
        LinksInclude include = new LinksInclude( );
        Map<String, Object> rootModel = new HashMap<>( );
        PageData data = new PageData( );
        int nMode = 0;
        HttpServletRequest request = new MockHttpServletRequest( );
        try {            
            include.fillTemplate( rootModel, data, nMode, request );
            String cssLinks = (String) rootModel.get( "plugins_javascript_links" );
            assertEquals( "<script src=\"http://example.com/script.js\"></script>", cssLinks.replace( "\n", "" ).replace( "\r", "" ) );
        }
        catch( IllegalStateException e )
        {
            if ( enableCache( ) )
            {
                fail(e);
            } else {
                // This failure is legit because the cache is disabled and if ehcache is used as implementation then the exception will be thrown                
            }
        }
    }
    @Test
    public void testGetURIJavascriptAbsoluteHttps( )
    {
        LinksIncludeTestPlugin plugin = (LinksIncludeTestPlugin) PluginService.getPlugin( PLUGIN_NAME );
        List<String> javascripts = new ArrayList<>( );
        javascripts.add( "https://example.com/script.js" );
        plugin.setJavascriptFiles( javascripts );
        plugin.setJavascriptFilesScopePortal( true );
        LinksInclude include = new LinksInclude( );
        Map<String, Object> rootModel = new HashMap<>( );
        PageData data = new PageData( );
        int nMode = 0;
        HttpServletRequest request = new MockHttpServletRequest( );
        try {            
            include.fillTemplate( rootModel, data, nMode, request );
            String cssLinks = (String) rootModel.get( "plugins_javascript_links" );
            assertEquals( "<script src=\"https://example.com/script.js\"></script>", cssLinks.replace( "\n", "" ).replace( "\r", "" ) );
        }
        catch( IllegalStateException e )
        {
            if ( enableCache( ) )
            {
                fail(e);
            } else {
                // This failure is legit because the cache is disabled and if ehcache is used as implementation then the exception will be thrown                
            }
        }
    }
    @Test
    public void testGetURIJavascriptProtocolRelative( )
    {
        LinksIncludeTestPlugin plugin = (LinksIncludeTestPlugin) PluginService.getPlugin( PLUGIN_NAME );
        List<String> javascripts = new ArrayList<>( );
        javascripts.add( "//example.com/script.js" );
        plugin.setJavascriptFiles( javascripts );
        plugin.setJavascriptFilesScopePortal( true );
        LinksInclude include = new LinksInclude( );
        Map<String, Object> rootModel = new HashMap<>( );
        PageData data = new PageData( );
        int nMode = 0;
        HttpServletRequest request = new MockHttpServletRequest( );
        try {            
            include.fillTemplate( rootModel, data, nMode, request );
            String cssLinks = (String) rootModel.get( "plugins_javascript_links" );
            assertEquals( "<script src=\"//example.com/script.js\"></script>", cssLinks.replace( "\n", "" ).replace( "\r", "" ) );
        }
        catch( IllegalStateException e )
        {
            if ( enableCache( ) )
            {
                fail(e);
            } else {
                // This failure is legit because the cache is disabled and if ehcache is used as implementation then the exception will be thrown                
            }
        }
    }
    @Test
    public void testGetURIJavascriptHash( ) throws IOException
    {
        LinksIncludeTestPlugin plugin = (LinksIncludeTestPlugin) PluginService.getPlugin( PLUGIN_NAME );
        List<String> javascripts = new ArrayList<>( );
        javascripts.add( "linksIncludeTestPlugin/scripthashed.js" );
        File hashedFile = new File( getResourcesDir( ), "js/plugins/linksIncludeTestPlugin/scripthashed.js" );
        hashedFile.getParentFile( ).mkdirs( );
        try ( FileWriter writer = new FileWriter( hashedFile ) )
        {
            writer.write( "abcd" );
        }
        try
        {
            plugin.setJavascriptFiles( javascripts );
            plugin.setJavascriptFilesScopePortal( true );
            LinksInclude include = new LinksInclude( );
            Map<String, Object> rootModel = new HashMap<>( );
            PageData data = new PageData( );
            int nMode = 0;
            ServletContext servletContext = new MockServletContext( )
            {

                @Override
                public InputStream getResourceAsStream( String path )
                {
                    File file = new File( getResourcesDir( ), path );
                    if ( file.exists( ) )
                    {
                        try
                        {
                            return new FileInputStream( file );
                        }
                        catch( FileNotFoundException e )
                        {
                            return null;
                        }
                    }
                    return null;
                }

            };
            MockHttpServletRequest request = new MockHttpServletRequest( servletContext );
            try {            
                include.fillTemplate( rootModel, data, nMode, request );
                String cssLinks = (String) rootModel.get( "plugins_javascript_links" );
                assertEquals(
                        "<script src=\"js/plugins/linksIncludeTestPlugin/scripthashed.js?lutece_h=88d4266fd4e6338d13b845fcf289579d209c897823b9217da3e161936f031589\"></script>",
                        cssLinks.replace( "\n", "" ).replace( "\r", "" ) );
                try ( FileWriter writer = new FileWriter( hashedFile ) )
                {
                    writer.write( "bbcd" );
                }
                include.fillTemplate( rootModel, data, nMode, request );
                String cssLinks2 = (String) rootModel.get( "plugins_javascript_links" );
                assertNotNull( cssLinks2 );
                if ( enableCache( ) )
                {
                	if(_cacheService.getConfiguration(Configuration.class).isStoreByValue())
                	{
                        assertEquals( cssLinks, cssLinks2 );
                	}else {
                        assertSame( cssLinks, cssLinks2 );
                	}
                }
                else
                {
                    assertFalse( cssLinks.equals( cssLinks2 ) );
                    assertEquals(
                            "<script src=\"js/plugins/linksIncludeTestPlugin/scripthashed.js?lutece_h=531ba794ef006cd3d69cf1acb33ddeccf8d6c655fb08f469335f8c2c32e2ab68\"></script>",
                            cssLinks2.replace( "\n", "" ).replace( "\r", "" ) );
                }
            }
            catch( IllegalStateException e )
            {
                if ( enableCache( ) )
                {
                    fail(e);
                } else {
                    // This failure is legit because the cache is disabled and if ehcache is used as implementation then the exception will be thrown                
                }
            }
        }
        finally
        {
            hashedFile.delete( );
        }
    }
    @Test
    public void testGetURIJavascriptHashQuery( ) throws IOException
    {
        LinksIncludeTestPlugin plugin = (LinksIncludeTestPlugin) PluginService.getPlugin( PLUGIN_NAME );
        List<String> javascripts = new ArrayList<>( );
        javascripts.add( "linksIncludeTestPlugin/scripthashed.js?arg=value" );
        File hashedFile = new File( getResourcesDir( ), "js/plugins/linksIncludeTestPlugin/scripthashed.js" );
        hashedFile.getParentFile( ).mkdirs( );
        try ( FileWriter writer = new FileWriter( hashedFile ) )
        {
            writer.write( "abcd" );
        }
        try
        {
            plugin.setJavascriptFiles( javascripts );
            plugin.setJavascriptFilesScopePortal( true );
            LinksInclude include = new LinksInclude( );
            Map<String, Object> rootModel = new HashMap<>( );
            PageData data = new PageData( );
            int nMode = 0;
            ServletContext servletContext = new MockServletContext( )
            {

                @Override
                public InputStream getResourceAsStream( String path )
                {
                    File file = new File( getResourcesDir( ), path );
                    if ( file.exists( ) )
                    {
                        try
                        {
                            return new FileInputStream( file );
                        }
                        catch( FileNotFoundException e )
                        {
                            return null;
                        }
                    }
                    return null;
                }

            };
            MockHttpServletRequest request = new MockHttpServletRequest( servletContext );
            try {            
                include.fillTemplate( rootModel, data, nMode, request );
                String cssLinks = (String) rootModel.get( "plugins_javascript_links" );
                assertEquals(
                        "<script src=\"js/plugins/linksIncludeTestPlugin/scripthashed.js?arg=value&lutece_h=88d4266fd4e6338d13b845fcf289579d209c897823b9217da3e161936f031589\"></script>",
                        cssLinks.replace( "\n", "" ).replace( "\r", "" ) );
            }
            catch( IllegalStateException e )
            {
                if ( enableCache( ) )
                {
                    fail(e);
                } else {
                    // This failure is legit because the cache is disabled and if ehcache is used as implementation then the exception will be thrown                
                }
            }
        }
        finally
        {
            hashedFile.delete( );
        }
    }
    @Test
    public void testGetURIJavascriptHashDigestError( ) throws IOException
    {
        LinksIncludeTestPlugin plugin = (LinksIncludeTestPlugin) PluginService.getPlugin( PLUGIN_NAME );
        List<String> javascripts = new ArrayList<>( );
        javascripts.add( "linksIncludeTestPlugin/scripthashed.js" );
        File hashedFile = new File( getResourcesDir( ), "js/plugins/linksIncludeTestPlugin/scripthashed.js" );
        hashedFile.getParentFile( ).mkdirs( );
        try ( FileWriter writer = new FileWriter( hashedFile ) )
        {
            writer.write( "abcd" );
        }
        try
        {
            plugin.setJavascriptFiles( javascripts );
            plugin.setJavascriptFilesScopePortal( true );
            LinksInclude include = new LinksInclude( );
            Map<String, Object> rootModel = new HashMap<>( );
            PageData data = new PageData( );
            int nMode = 0;
            ServletContext servletContext = new MockServletContext( )
            {

                @Override
                public InputStream getResourceAsStream( String path )
                {
                    File file = new File( getResourcesDir( ), path );
                    if ( file.exists( ) )
                    {
                        return new InputStream( )
                        {

                            @Override
                            public int read( ) throws IOException
                            {
                                throw new IOException( "Mocking IO error" );
                            }
                        };
                    }
                    return null;
                }

            };
            MockHttpServletRequest request = new MockHttpServletRequest( servletContext );
            try {            
                include.fillTemplate( rootModel, data, nMode, request );
                String cssLinks = (String) rootModel.get( "plugins_javascript_links" );
                assertEquals( "<script src=\"js/plugins/linksIncludeTestPlugin/scripthashed.js\"></script>", cssLinks.replace( "\n", "" ).replace( "\r", "" ) );
            }
            catch( IllegalStateException e )
            {
                if ( enableCache( ) )
                {
                    fail(e);
                } else {
                    // This failure is legit because the cache is disabled and if ehcache is used as implementation then the exception will be thrown                
                }
            }
        }
        finally
        {
            hashedFile.delete( );
        }
    }
    @Test
    public void testGetURIJavascriptHashStreamCloseError( ) throws IOException
    {
        LinksIncludeTestPlugin plugin = (LinksIncludeTestPlugin) PluginService.getPlugin( PLUGIN_NAME );
        List<String> javascripts = new ArrayList<>( );
        javascripts.add( "linksIncludeTestPlugin/scripthashed.js" );
        File hashedFile = new File( getResourcesDir( ), "js/plugins/linksIncludeTestPlugin/scripthashed.js" );
        hashedFile.getParentFile( ).mkdirs( );
        try ( FileWriter writer = new FileWriter( hashedFile ) )
        {
            writer.write( "abcd" );
        }
        try
        {
            plugin.setJavascriptFiles( javascripts );
            plugin.setJavascriptFilesScopePortal( true );
            LinksInclude include = new LinksInclude( );
            Map<String, Object> rootModel = new HashMap<>( );
            PageData data = new PageData( );
            int nMode = 0;
            ServletContext servletContext = new MockServletContext( )
            {

                @Override
                public InputStream getResourceAsStream( String path )
                {
                    File file = new File( getResourcesDir( ), path );
                    if ( file.exists( ) )
                    {
                        try
                        {
                            return new FileInputStream( file )
                            {

                                @Override
                                public void close( ) throws IOException
                                {
                                    throw new IOException( "Mocking IO error on close" );
                                }

                            };
                        }
                        catch( FileNotFoundException e )
                        {
                            return null;
                        }
                    }
                    return null;
                }

            };
            MockHttpServletRequest request = new MockHttpServletRequest( servletContext );
            try {            
                include.fillTemplate( rootModel, data, nMode, request );
                String cssLinks = (String) rootModel.get( "plugins_javascript_links" );
                assertEquals(
                        "<script src=\"js/plugins/linksIncludeTestPlugin/scripthashed.js?lutece_h=88d4266fd4e6338d13b845fcf289579d209c897823b9217da3e161936f031589\"></script>",
                        cssLinks.replace( "\n", "" ).replace( "\r", "" ) );
            }
            catch( IllegalStateException e )
            {
                if ( enableCache( ) )
                {
                    fail(e);
                } else {
                    // This failure is legit because the cache is disabled and if ehcache is used as implementation then the exception will be thrown                
                }
            }
        }
        finally
        {
            hashedFile.delete( );
        }
    }
    @Test
    public void testGetURIJavascriptBadURI( )
    {
        LinksIncludeTestPlugin plugin = (LinksIncludeTestPlugin) PluginService.getPlugin( PLUGIN_NAME );
        List<String> javascripts = new ArrayList<>( );
        javascripts.add( "://script.js" );
        plugin.setJavascriptFiles( javascripts );
        plugin.setJavascriptFilesScopePortal( true );
        LinksInclude include = new LinksInclude( );
        Map<String, Object> rootModel = new HashMap<>( );
        PageData data = new PageData( );
        int nMode = 0;
        HttpServletRequest request = new MockHttpServletRequest( );
        try {            
            include.fillTemplate( rootModel, data, nMode, request );
            String cssLinks = (String) rootModel.get( "plugins_javascript_links" );
            assertEquals( "", cssLinks );
        }
        catch( IllegalStateException e )
        {
            if ( enableCache( ) )
            {
                fail(e);
            } else {
                // This failure is legit because the cache is disabled and if ehcache is used as implementation then the exception will be thrown                
            }
        }
    }
}
