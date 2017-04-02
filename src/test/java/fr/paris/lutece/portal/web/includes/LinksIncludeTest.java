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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;

import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.test.LuteceTestCase;

public class LinksIncludeTest extends LuteceTestCase
{
    private static final String PLUGIN_NAME = "linksIncludeTestPlugin";

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        File dirPlugin = new File( AppPathService.getPath( "path.plugins" ) );
        File testPluginFile = new File( dirPlugin, PLUGIN_NAME + ".xml" );
        try ( BufferedWriter writer = new BufferedWriter( new FileWriter( testPluginFile ) ) )
        {
            writer.write( "<plug-in><name>" + PLUGIN_NAME + "</name><class>" + LinksIncludeTestPlugin.class.getName( )
                    + "</class>"
                    + "<icon-url>../../images/admin/skin/plugins/myplugin/myplugin.gif</icon-url></plug-in>" );
        }
        PluginService.init( );
        PluginService.getPlugin( PLUGIN_NAME ).install( );

    }

    @Override
    protected void tearDown( ) throws Exception
    {
        PluginService.getPlugin( PLUGIN_NAME ).uninstall( );
        File dirPlugin = new File( AppPathService.getPath( "path.plugins" ) );
        File testPluginFile = new File( dirPlugin, PLUGIN_NAME + ".xml" );
        testPluginFile.delete( );
        PluginService.init( );
        super.tearDown( );
    }

    public void testGetURICssAddPrefix( )
    {
        LinksIncludeTestPlugin plugin = ( LinksIncludeTestPlugin ) PluginService.getPlugin( PLUGIN_NAME );
        List<String> styleSheets = new ArrayList<>( );
        styleSheets.add( PLUGIN_NAME + "/style.css" );
        plugin.setCssStyleSheets( styleSheets );
        plugin.setCssStyleSheetsScopePortal( true );
        LinksInclude include = new LinksInclude( );
        Map<String, Object> rootModel = new HashMap<>( );
        PageData data = new PageData( );
        int nMode = 0;
        HttpServletRequest request = new MockHttpServletRequest( );
        include.fillTemplate( rootModel, data, nMode, request );
        String cssLinks = ( String ) rootModel.get( "plugins_css_links" );
        assertEquals(
                "<link rel=\"stylesheet\"  href=\"css/plugins/linksIncludeTestPlugin/style.css\" type=\"text/css\"  media=\"screen\" />\n",
                cssLinks );
    }

    public void testGetURICssAbsoluteHttp( )
    {
        LinksIncludeTestPlugin plugin = ( LinksIncludeTestPlugin ) PluginService.getPlugin( PLUGIN_NAME );
        List<String> styleSheets = new ArrayList<>( );
        styleSheets.add( "http://example.com/style.css" );
        plugin.setCssStyleSheets( styleSheets );
        plugin.setCssStyleSheetsScopePortal( true );
        LinksInclude include = new LinksInclude( );
        Map<String, Object> rootModel = new HashMap<>( );
        PageData data = new PageData( );
        int nMode = 0;
        HttpServletRequest request = new MockHttpServletRequest( );
        include.fillTemplate( rootModel, data, nMode, request );
        String cssLinks = ( String ) rootModel.get( "plugins_css_links" );
        assertEquals(
                "<link rel=\"stylesheet\"  href=\"http://example.com/style.css\" type=\"text/css\"  media=\"screen\" />\n",
                cssLinks );
    }

    public void testGetURICssAbsoluteHttps( )
    {
        LinksIncludeTestPlugin plugin = ( LinksIncludeTestPlugin ) PluginService.getPlugin( PLUGIN_NAME );
        List<String> styleSheets = new ArrayList<>( );
        styleSheets.add( "https://example.com/style.css" );
        plugin.setCssStyleSheets( styleSheets );
        plugin.setCssStyleSheetsScopePortal( true );
        LinksInclude include = new LinksInclude( );
        Map<String, Object> rootModel = new HashMap<>( );
        PageData data = new PageData( );
        int nMode = 0;
        HttpServletRequest request = new MockHttpServletRequest( );
        include.fillTemplate( rootModel, data, nMode, request );
        String cssLinks = ( String ) rootModel.get( "plugins_css_links" );
        assertEquals(
                "<link rel=\"stylesheet\"  href=\"https://example.com/style.css\" type=\"text/css\"  media=\"screen\" />\n",
                cssLinks );
    }

    public void testGetURICssProtocolRelative( )
    {
        LinksIncludeTestPlugin plugin = ( LinksIncludeTestPlugin ) PluginService.getPlugin( PLUGIN_NAME );
        List<String> styleSheets = new ArrayList<>( );
        styleSheets.add( "//example.com/style.css" );
        plugin.setCssStyleSheets( styleSheets );
        plugin.setCssStyleSheetsScopePortal( true );
        LinksInclude include = new LinksInclude( );
        Map<String, Object> rootModel = new HashMap<>( );
        PageData data = new PageData( );
        int nMode = 0;
        HttpServletRequest request = new MockHttpServletRequest( );
        include.fillTemplate( rootModel, data, nMode, request );
        String cssLinks = ( String ) rootModel.get( "plugins_css_links" );
        assertEquals(
                "<link rel=\"stylesheet\"  href=\"//example.com/style.css\" type=\"text/css\"  media=\"screen\" />\n",
                cssLinks );
    }

    public void testGetURICssHash( ) throws IOException
    {
        LinksIncludeTestPlugin plugin = ( LinksIncludeTestPlugin ) PluginService.getPlugin( PLUGIN_NAME );
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
                        catch ( FileNotFoundException e )
                        {
                            return null;
                        }
                    }
                    return null;
                }

            };
            MockHttpServletRequest request = new MockHttpServletRequest( servletContext );
            include.fillTemplate( rootModel, data, nMode, request );
            String cssLinks = ( String ) rootModel.get( "plugins_css_links" );
            assertEquals(
                    "<link rel=\"stylesheet\"  href=\"css/plugins/linksIncludeTestPlugin/junithashed.css?lutece_h=88d4266fd4e6338d13b845fcf289579d209c897823b9217da3e161936f031589\" type=\"text/css\"  media=\"screen\" />\n",
                    cssLinks );
            try ( FileWriter writer = new FileWriter( hashedFile ) )
            {
                writer.write( "bbcd" );
            }
            include.fillTemplate( rootModel, data, nMode, request );
            String cssLinks2 = ( String ) rootModel.get( "plugins_css_links" );
            assertNotNull( cssLinks2 );
            assertFalse( cssLinks.equals( cssLinks2 ) );
            assertEquals(
                    "<link rel=\"stylesheet\"  href=\"css/plugins/linksIncludeTestPlugin/junithashed.css?lutece_h=531ba794ef006cd3d69cf1acb33ddeccf8d6c655fb08f469335f8c2c32e2ab68\" type=\"text/css\"  media=\"screen\" />\n",
                    cssLinks2 );
        }
        finally
        {
            hashedFile.delete( );
        }
    }

    public void testGetURICssHashQuery( ) throws IOException
    {
        LinksIncludeTestPlugin plugin = ( LinksIncludeTestPlugin ) PluginService.getPlugin( PLUGIN_NAME );
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
                        catch ( FileNotFoundException e )
                        {
                            return null;
                        }
                    }
                    return null;
                }

            };
            MockHttpServletRequest request = new MockHttpServletRequest( servletContext );
            include.fillTemplate( rootModel, data, nMode, request );
            String cssLinks = ( String ) rootModel.get( "plugins_css_links" );
            assertEquals(
                    "<link rel=\"stylesheet\"  href=\"css/plugins/linksIncludeTestPlugin/junithashed.css?arg=value&lutece_h=88d4266fd4e6338d13b845fcf289579d209c897823b9217da3e161936f031589\" type=\"text/css\"  media=\"screen\" />\n",
                    cssLinks );
        }
        finally
        {
            hashedFile.delete( );
        }
    }

    public void testGetURICssHashDigestError( ) throws IOException
    {
        LinksIncludeTestPlugin plugin = ( LinksIncludeTestPlugin ) PluginService.getPlugin( PLUGIN_NAME );
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
            include.fillTemplate( rootModel, data, nMode, request );
            String cssLinks = ( String ) rootModel.get( "plugins_css_links" );
            assertEquals(
                    "<link rel=\"stylesheet\"  href=\"css/plugins/linksIncludeTestPlugin/junithashed.css\" type=\"text/css\"  media=\"screen\" />\n",
                    cssLinks );
        }
        finally
        {
            hashedFile.delete( );
        }
    }

    public void testGetURICssHashStreamCloseError( ) throws IOException
    {
        LinksIncludeTestPlugin plugin = ( LinksIncludeTestPlugin ) PluginService.getPlugin( PLUGIN_NAME );
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
                        catch ( FileNotFoundException e )
                        {
                            return null;
                        }
                    }
                    return null;
                }

            };
            MockHttpServletRequest request = new MockHttpServletRequest( servletContext );
            include.fillTemplate( rootModel, data, nMode, request );
            String cssLinks = ( String ) rootModel.get( "plugins_css_links" );
            assertEquals(
                    "<link rel=\"stylesheet\"  href=\"css/plugins/linksIncludeTestPlugin/junithashed.css?lutece_h=88d4266fd4e6338d13b845fcf289579d209c897823b9217da3e161936f031589\" type=\"text/css\"  media=\"screen\" />\n",
                    cssLinks );
        }
        finally
        {
            hashedFile.delete( );
        }
    }

    public void testGetURICssBadURI( )
    {
        LinksIncludeTestPlugin plugin = ( LinksIncludeTestPlugin ) PluginService.getPlugin( PLUGIN_NAME );
        List<String> styleSheets = new ArrayList<>( );
        styleSheets.add( "://style.css" );
        plugin.setCssStyleSheets( styleSheets );
        plugin.setCssStyleSheetsScopePortal( true );
        LinksInclude include = new LinksInclude( );
        Map<String, Object> rootModel = new HashMap<>( );
        PageData data = new PageData( );
        int nMode = 0;
        HttpServletRequest request = new MockHttpServletRequest( );
        include.fillTemplate( rootModel, data, nMode, request );
        String cssLinks = ( String ) rootModel.get( "plugins_css_links" );
        assertEquals( "", cssLinks );
    }

    // Javscript link

    public void testGetURIJavascriptAddPrefix( )
    {
        LinksIncludeTestPlugin plugin = ( LinksIncludeTestPlugin ) PluginService.getPlugin( PLUGIN_NAME );
        List<String> javascripts = new ArrayList<>( );
        javascripts.add( PLUGIN_NAME + "/script.js" );
        plugin.setJavascriptFiles( javascripts );
        plugin.setJavascriptFilesScopePortal( true );
        LinksInclude include = new LinksInclude( );
        Map<String, Object> rootModel = new HashMap<>( );
        PageData data = new PageData( );
        int nMode = 0;
        HttpServletRequest request = new MockHttpServletRequest( );
        include.fillTemplate( rootModel, data, nMode, request );
        String cssLinks = ( String ) rootModel.get( "plugins_javascript_links" );
        assertEquals( "<script src=\"js/plugins/linksIncludeTestPlugin/script.js\" type=\"text/javascript\" ></script>",
                cssLinks );
    }

    public void testGetURIJavascriptAbsoluteHttp( )
    {
        LinksIncludeTestPlugin plugin = ( LinksIncludeTestPlugin ) PluginService.getPlugin( PLUGIN_NAME );
        List<String> javascripts = new ArrayList<>( );
        javascripts.add( "http://example.com/script.js" );
        plugin.setJavascriptFiles( javascripts );
        plugin.setJavascriptFilesScopePortal( true );
        LinksInclude include = new LinksInclude( );
        Map<String, Object> rootModel = new HashMap<>( );
        PageData data = new PageData( );
        int nMode = 0;
        HttpServletRequest request = new MockHttpServletRequest( );
        include.fillTemplate( rootModel, data, nMode, request );
        String cssLinks = ( String ) rootModel.get( "plugins_javascript_links" );
        assertEquals( "<script src=\"http://example.com/script.js\" type=\"text/javascript\" ></script>", cssLinks );
    }

    public void testGetURIJavascriptAbsoluteHttps( )
    {
        LinksIncludeTestPlugin plugin = ( LinksIncludeTestPlugin ) PluginService.getPlugin( PLUGIN_NAME );
        List<String> javascripts = new ArrayList<>( );
        javascripts.add( "https://example.com/script.js" );
        plugin.setJavascriptFiles( javascripts );
        plugin.setJavascriptFilesScopePortal( true );
        LinksInclude include = new LinksInclude( );
        Map<String, Object> rootModel = new HashMap<>( );
        PageData data = new PageData( );
        int nMode = 0;
        HttpServletRequest request = new MockHttpServletRequest( );
        include.fillTemplate( rootModel, data, nMode, request );
        String cssLinks = ( String ) rootModel.get( "plugins_javascript_links" );
        assertEquals( "<script src=\"https://example.com/script.js\" type=\"text/javascript\" ></script>", cssLinks );
    }

    public void testGetURIJavascriptProtocolRelative( )
    {
        LinksIncludeTestPlugin plugin = ( LinksIncludeTestPlugin ) PluginService.getPlugin( PLUGIN_NAME );
        List<String> javascripts = new ArrayList<>( );
        javascripts.add( "//example.com/script.js" );
        plugin.setJavascriptFiles( javascripts );
        plugin.setJavascriptFilesScopePortal( true );
        LinksInclude include = new LinksInclude( );
        Map<String, Object> rootModel = new HashMap<>( );
        PageData data = new PageData( );
        int nMode = 0;
        HttpServletRequest request = new MockHttpServletRequest( );
        include.fillTemplate( rootModel, data, nMode, request );
        String cssLinks = ( String ) rootModel.get( "plugins_javascript_links" );
        assertEquals( "<script src=\"//example.com/script.js\" type=\"text/javascript\" ></script>", cssLinks );
    }

    public void testGetURIJavascriptHash( ) throws IOException
    {
        LinksIncludeTestPlugin plugin = ( LinksIncludeTestPlugin ) PluginService.getPlugin( PLUGIN_NAME );
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
                        catch ( FileNotFoundException e )
                        {
                            return null;
                        }
                    }
                    return null;
                }

            };
            MockHttpServletRequest request = new MockHttpServletRequest( servletContext );
            include.fillTemplate( rootModel, data, nMode, request );
            String cssLinks = ( String ) rootModel.get( "plugins_javascript_links" );
            assertEquals(
                    "<script src=\"js/plugins/linksIncludeTestPlugin/scripthashed.js?lutece_h=88d4266fd4e6338d13b845fcf289579d209c897823b9217da3e161936f031589\" type=\"text/javascript\" ></script>",
                    cssLinks );
            try ( FileWriter writer = new FileWriter( hashedFile ) )
            {
                writer.write( "bbcd" );
            }
            include.fillTemplate( rootModel, data, nMode, request );
            String cssLinks2 = ( String ) rootModel.get( "plugins_javascript_links" );
            assertNotNull( cssLinks2 );
            assertFalse( cssLinks.equals( cssLinks2 ) );
            assertEquals(
                    "<script src=\"js/plugins/linksIncludeTestPlugin/scripthashed.js?lutece_h=531ba794ef006cd3d69cf1acb33ddeccf8d6c655fb08f469335f8c2c32e2ab68\" type=\"text/javascript\" ></script>",
                    cssLinks2 );
        }
        finally
        {
            hashedFile.delete( );
        }
    }

    public void testGetURIJavascriptHashQuery( ) throws IOException
    {
        LinksIncludeTestPlugin plugin = ( LinksIncludeTestPlugin ) PluginService.getPlugin( PLUGIN_NAME );
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
                        catch ( FileNotFoundException e )
                        {
                            return null;
                        }
                    }
                    return null;
                }

            };
            MockHttpServletRequest request = new MockHttpServletRequest( servletContext );
            include.fillTemplate( rootModel, data, nMode, request );
            String cssLinks = ( String ) rootModel.get( "plugins_javascript_links" );
            assertEquals(
                    "<script src=\"js/plugins/linksIncludeTestPlugin/scripthashed.js?arg=value&lutece_h=88d4266fd4e6338d13b845fcf289579d209c897823b9217da3e161936f031589\" type=\"text/javascript\" ></script>",
                    cssLinks );
        }
        finally
        {
            hashedFile.delete( );
        }
    }

    public void testGetURIJavascriptHashDigestError( ) throws IOException
    {
        LinksIncludeTestPlugin plugin = ( LinksIncludeTestPlugin ) PluginService.getPlugin( PLUGIN_NAME );
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
            include.fillTemplate( rootModel, data, nMode, request );
            String cssLinks = ( String ) rootModel.get( "plugins_javascript_links" );
            assertEquals(
                    "<script src=\"js/plugins/linksIncludeTestPlugin/scripthashed.js\" type=\"text/javascript\" ></script>",
                    cssLinks );
        }
        finally
        {
            hashedFile.delete( );
        }
    }

    public void testGetURIJavascriptHashStreamCloseError( ) throws IOException
    {
        LinksIncludeTestPlugin plugin = ( LinksIncludeTestPlugin ) PluginService.getPlugin( PLUGIN_NAME );
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
                        catch ( FileNotFoundException e )
                        {
                            return null;
                        }
                    }
                    return null;
                }

            };
            MockHttpServletRequest request = new MockHttpServletRequest( servletContext );
            include.fillTemplate( rootModel, data, nMode, request );
            String cssLinks = ( String ) rootModel.get( "plugins_javascript_links" );
            assertEquals(
                    "<script src=\"js/plugins/linksIncludeTestPlugin/scripthashed.js?lutece_h=88d4266fd4e6338d13b845fcf289579d209c897823b9217da3e161936f031589\" type=\"text/javascript\" ></script>",
                    cssLinks );
        }
        finally
        {
            hashedFile.delete( );
        }
    }

    public void testGetURIJavascriptBadURI( )
    {
        LinksIncludeTestPlugin plugin = ( LinksIncludeTestPlugin ) PluginService.getPlugin( PLUGIN_NAME );
        List<String> javascripts = new ArrayList<>( );
        javascripts.add( "://script.js" );
        plugin.setJavascriptFiles( javascripts );
        plugin.setJavascriptFilesScopePortal( true );
        LinksInclude include = new LinksInclude( );
        Map<String, Object> rootModel = new HashMap<>( );
        PageData data = new PageData( );
        int nMode = 0;
        HttpServletRequest request = new MockHttpServletRequest( );
        include.fillTemplate( rootModel, data, nMode, request );
        String cssLinks = ( String ) rootModel.get( "plugins_javascript_links" );
        assertEquals( "", cssLinks );
    }
}
