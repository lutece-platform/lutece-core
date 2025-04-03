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
package fr.paris.lutece.portal.web.upload;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload2.core.DiskFileItem;
import org.apache.commons.fileupload2.core.FileItem;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;
import fr.paris.lutece.util.http.MultipartUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UploadServletTest extends LuteceTestCase
{

    /**
     * Test of doPost method, of class fr.paris.lutece.portal.web.upload.UploadServlet.
     */
	@Test
    public void testDoPost_NoFiles_NoHandler( ) throws Exception
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        MultipartHttpServletRequest multipartRequest = new MultipartHttpServletRequest( request, new HashMap<>( ), new HashMap<>( ) );

        new UploadServlet( ).doPost( multipartRequest, response );

        String strResponseJson = response.getContentAsString( );
        System.out.println( strResponseJson );

        String strRefJson = "{\"files\":[]}";
        ObjectMapper objectMapper = new ObjectMapper( );
        JsonNode objectNodeRef = objectMapper.readTree( strRefJson );
        JsonNode objectNodeJson = objectMapper.readTree( strResponseJson );

        assertEquals( objectNodeRef, objectNodeJson );
    }
	@Test
    public void testDoPost_Files_NoHandler( ) throws Exception
    {
        MockHttpServletRequest request = getMultipartRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        MultipartHttpServletRequest multipartRequest = MultipartUtil.convert( 10000, 10000, false, request, false );

        new UploadServlet( ).doPost( multipartRequest, response );

        String strResponseJson = response.getContentAsString( );
        System.out.println( strResponseJson );

        String strRefJson = "{\"files\":[{\"fileName\":\"file1\",\"fileSize\":3}]}";
        ObjectMapper objectMapper = new ObjectMapper( );
        JsonNode objectNodeRef = objectMapper.readTree( strRefJson );
        JsonNode objectNodeJson = objectMapper.readTree( strResponseJson );

        assertEquals( objectNodeRef, objectNodeJson );
    }
	@Test
    public void testDoPost_NoFiles_Handler( ) throws Exception
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        Map<String, List<FileItem<DiskFileItem>>> mapFiles = new HashMap<>( );
        Map<String, String [ ]> mapParameters = new HashMap<>( );
        mapParameters.put( "handler", new String [ ] {
                TestAsynchronousUploadHandler2.BEAN_NAME
        } );
        MultipartHttpServletRequest multipartRequest = new MultipartHttpServletRequest( request, mapFiles, mapParameters );

        try
        {
            new UploadServlet( ).doPost( multipartRequest, response );
        }
        catch( Exception e )
        {
            throw new RuntimeException( e );
        }

        String strResponseJson = response.getContentAsString( );
        System.out.println( strResponseJson );

        String strRefJson = TestAsynchronousUploadHandler2.SERIALIZED_MAP_CONTENT;
        ObjectMapper objectMapper = new ObjectMapper( );
        JsonNode objectNodeRef = objectMapper.readTree( strRefJson );
        JsonNode objectNodeJson = objectMapper.readTree( strResponseJson );

        assertEquals( objectNodeRef, objectNodeJson );
    }

    @ApplicationScoped
    public static class TestAsynchronousUploadHandler2 implements IAsynchronousUploadHandler2
    {
        public static final String BEAN_NAME = "junit_test_bean";
        public static final String SERIALIZED_MAP_CONTENT = "{\"testmap\":\"valuetestmap\"}";

        @Override
        public void process(HttpServletRequest request, HttpServletResponse response, Map<String, Object> mainObject, List<FileItem<DiskFileItem>> fileItems)
        {
            mainObject.clear();
            mainObject.put("testmap", "valuetestmap");
        }

        @Override
        public boolean isInvoked(HttpServletRequest request)
        {
            return BEAN_NAME.equals(request.getParameter("handler"));
        }
    }
    @Test
    public void testDoPost_NoFiles_Handler2( ) throws Exception
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        Map<String, List<FileItem<DiskFileItem>>> mapFiles = new HashMap<>( );
        Map<String, String [ ]> mapParameters = new HashMap<>( );
        mapParameters.put( "handler", new String [ ] {
                TestAsynchronousUploadHandler2.BEAN_NAME
        } );
        MultipartHttpServletRequest multipartRequest = new MultipartHttpServletRequest( request, mapFiles, mapParameters );

        try
        {
            new UploadServlet( ).doPost( multipartRequest, response );
        }
        catch( Exception e )
        {
            throw new RuntimeException( e );
        }

        String strResponseJson = response.getContentAsString( );
        System.out.println( strResponseJson );

        String strRefJson = TestAsynchronousUploadHandler2.SERIALIZED_MAP_CONTENT;
        ObjectMapper objectMapper = new ObjectMapper( );
        JsonNode objectNodeRef = objectMapper.readTree( strRefJson );
        JsonNode objectNodeJson = objectMapper.readTree( strResponseJson );

        assertEquals( objectNodeRef, objectNodeJson );
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
