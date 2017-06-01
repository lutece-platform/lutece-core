/*
 * Copyright (c) 2002-2016, Mairie de Paris
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

package fr.paris.lutece.portal.web.upload;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginDefaultImplementation;
import fr.paris.lutece.portal.service.plugin.PluginEvent;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.http.MultipartUtil;

import net.sf.json.JSONObject;

public class UploadServletTest extends LuteceTestCase
{

    /**
     * Test of doPost method, of class fr.paris.lutece.portal.web.upload.UploadServlet.
     */
    public void testDoPost_NoFiles_NoHandler( ) throws Exception
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        MultipartHttpServletRequest multipartRequest = new MultipartHttpServletRequest( request, new HashMap( ), new HashMap( ) );

        new UploadServlet( ).doPost( multipartRequest, response );

        String strResponseJson = response.getContentAsString( );
        System.out.println( strResponseJson );

        String strRefJson = "{\"files\":[]}";
        ObjectMapper objectMapper = new ObjectMapper( );
        JsonNode objectNodeRef = objectMapper.readTree( strRefJson );
        JsonNode objectNodeJson = objectMapper.readTree( strResponseJson );

        assertEquals( objectNodeRef, objectNodeJson );
    }

    public void testDoPost_Files_NoHandler( ) throws Exception
    {
        MockHttpServletRequest request = getMultipartRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        MultipartHttpServletRequest multipartRequest = MultipartUtil.convert( 10000, 10000, false, request );

        new UploadServlet( ).doPost( multipartRequest, response );

        String strResponseJson = response.getContentAsString( );
        System.out.println( strResponseJson );

        String strRefJson = "{\"files\":[{\"fileName\":\"file1\",\"fileSize\":3}]}";
        ObjectMapper objectMapper = new ObjectMapper( );
        JsonNode objectNodeRef = objectMapper.readTree( strRefJson );
        JsonNode objectNodeJson = objectMapper.readTree( strResponseJson );

        assertEquals( objectNodeRef, objectNodeJson );
    }

    public void testDoPost_NoFiles_Handler( ) throws Exception
    {
        final String BEAN_NAME = "testAsyncUpNetSf";
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        Map<String, List<FileItem>> mapFiles = new HashMap<>( );
        Map<String, String [ ]> mapParameters = new HashMap<>( );
        mapParameters.put( "handler", new String [ ] {
            BEAN_NAME
        } );
        MultipartHttpServletRequest multipartRequest = new MultipartHttpServletRequest( request, mapFiles, mapParameters );

        clearLuteceSpringCache( );
        ConfigurableListableBeanFactory beanFactory = ( (ConfigurableApplicationContext) SpringContextService.getContext( ) ).getBeanFactory( );
        beanFactory.registerSingleton( BEAN_NAME, new IAsynchronousUploadHandler( )
        {
            @Override
            public void process( HttpServletRequest request, HttpServletResponse response, JSONObject mainObject, List<FileItem> fileItems )
            {
                mainObject.clear( );
                mainObject.element( "testnetsf", "valuetestnetsf" );
            }

            @Override
            public boolean isInvoked( HttpServletRequest request )
            {
                return BEAN_NAME.equals( request.getParameter( "handler" ) );
            }
        } );

        try
        {
            new UploadServlet( ).doPost( multipartRequest, response );
        }
        catch( Exception e )
        {
            throw new RuntimeException( e );
        }
        finally
        {
            ( (DefaultListableBeanFactory) beanFactory ).destroySingleton( BEAN_NAME );
            clearLuteceSpringCache( );
        }

        String strResponseJson = response.getContentAsString( );
        System.out.println( strResponseJson );

        String strRefJson = "{\"testnetsf\":\"valuetestnetsf\"}";
        ObjectMapper objectMapper = new ObjectMapper( );
        JsonNode objectNodeRef = objectMapper.readTree( strRefJson );
        JsonNode objectNodeJson = objectMapper.readTree( strResponseJson );

        assertEquals( objectNodeRef, objectNodeJson );
    }

    public void testDoPost_NoFiles_Handler2( ) throws Exception
    {
        final String BEAN_NAME = "testAsyncUpMap";
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        Map<String, List<FileItem>> mapFiles = new HashMap<>( );
        Map<String, String [ ]> mapParameters = new HashMap<>( );
        mapParameters.put( "handler", new String [ ] {
            BEAN_NAME
        } );
        MultipartHttpServletRequest multipartRequest = new MultipartHttpServletRequest( request, mapFiles, mapParameters );

        clearLuteceSpringCache( );
        ConfigurableListableBeanFactory beanFactory = ( (ConfigurableApplicationContext) SpringContextService.getContext( ) ).getBeanFactory( );
        beanFactory.registerSingleton( BEAN_NAME, new IAsynchronousUploadHandler2( )
        {
            @Override
            public void process( HttpServletRequest request, HttpServletResponse response, Map<String, Object> mainObject, List<FileItem> fileItems )
            {
                mainObject.clear( );
                mainObject.put( "testmap", "valuetestmap" );
            }

            @Override
            public boolean isInvoked( HttpServletRequest request )
            {
                return BEAN_NAME.equals( request.getParameter( "handler" ) );
            }
        } );

        try
        {
            new UploadServlet( ).doPost( multipartRequest, response );
        }
        catch( Exception e )
        {
            throw new RuntimeException( e );
        }
        finally
        {
            ( (DefaultListableBeanFactory) beanFactory ).destroySingleton( BEAN_NAME );
            clearLuteceSpringCache( );
        }

        String strResponseJson = response.getContentAsString( );
        System.out.println( strResponseJson );

        String strRefJson = "{\"testmap\":\"valuetestmap\"}";
        ObjectMapper objectMapper = new ObjectMapper( );
        JsonNode objectNodeRef = objectMapper.readTree( strRefJson );
        JsonNode objectNodeJson = objectMapper.readTree( strResponseJson );

        assertEquals( objectNodeRef, objectNodeJson );
    }

    private void clearLuteceSpringCache( )
    {
        // hack plugin installed event to clear the SpringContextService cache
        // TODO have a better way to do this ???
        Plugin p = new PluginDefaultImplementation( );
        p.setName( "FakePluginSpringCacheClearer" );
        PluginService.notifyListeners( new PluginEvent( p, PluginEvent.PLUGIN_INSTALLED ) );
    }

    private MockHttpServletRequest getMultipartRequest( ) throws Exception
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        byte [ ] fileContent = new byte [ ] {
                1, 2, 3
        };
        Part [ ] parts = new Part [ ] {
            new FilePart( "file1", new ByteArrayPartSource( "file1", fileContent ) )
        };
        MultipartRequestEntity multipartRequestEntity = new MultipartRequestEntity( parts, new PostMethod( ).getParams( ) );
        // Serialize request body
        ByteArrayOutputStream requestContent = new ByteArrayOutputStream( );
        multipartRequestEntity.writeRequest( requestContent );
        // Set request body to HTTP servlet request
        request.setContent( requestContent.toByteArray( ) );
        // Set content type to HTTP servlet request (important, includes Mime boundary string)
        request.setContentType( multipartRequestEntity.getContentType( ) );
        request.setMethod( "POST" );
        return request;
    }
}
