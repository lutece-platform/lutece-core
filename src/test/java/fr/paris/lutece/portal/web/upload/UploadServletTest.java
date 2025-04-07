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

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.paris.lutece.portal.service.upload.MultipartAsyncUploadHandler;
import fr.paris.lutece.portal.service.upload.MultipartHandler;
import fr.paris.lutece.portal.service.upload.MultipartItem;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;
import fr.paris.lutece.test.mocks.MockPart;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UploadServletTest extends LuteceTestCase
{

    @Inject
    @MultipartAsyncUploadHandler
    private MultipartHandler _multipartHandler;
    
    @Inject
    private UploadServletTestWrapper _uploadServlet;
    
    /**
     * Test of doPost method, of class fr.paris.lutece.portal.web.upload.UploadServlet.
     */
	@Test
    public void testDoPost_NoFiles_NoHandler( ) throws Exception
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );

        _uploadServlet.doPost( request, response );

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

        _uploadServlet.doPost( request, response );

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
        request.addPart( new MockPart( "handler", null, null, TestAsynchronousUploadHandler2.BEAN_NAME.getBytes( ) ) );
        
        try
        {
            _uploadServlet.doPost( request, response );
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
        public void process(HttpServletRequest request, HttpServletResponse response, Map<String, Object> mainObject, List<MultipartItem> fileItems)
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

    @ApplicationScoped
    public static class UploadServletTestWrapper extends UploadServlet
    {

    }

    @Test
    public void testDoPost_NoFiles_Handler2( ) throws Exception
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        request.addPart( new MockPart( "handler", null, null, TestAsynchronousUploadHandler2.BEAN_NAME.getBytes( ) ) );

        try
        {
            _uploadServlet.doPost( request, response );
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
        request.addPart( new MockPart( "file1", "", "file1", fileContent) );
        request.setMethod( "POST" );
        return request;
    }
}
