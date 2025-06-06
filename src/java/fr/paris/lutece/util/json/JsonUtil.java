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
package fr.paris.lutece.util.json;

import fr.paris.lutece.portal.service.util.AppLogService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * JsonUtil
 *
 */
public class JsonUtil
{
    public static final String CONTENT_TYPE_JSON = "application/json";

    // ObjectMapper
    private static final ObjectMapper _mapper = new ObjectMapper( );

    private JsonUtil( )
    {
        // Ctor
    }

    /**
     * return a string containing the JSON flow
     * 
     * @param jsonResponse
     *            the JSON Response Object
     * @return return a string containing the JSON flow
     */
    public static String buildJsonResponse( AbstractJsonResponse jsonResponse )
    {
        String strJsonResponse = null;

        try
        {
            strJsonResponse = _mapper.writeValueAsString( jsonResponse );
        }
        catch( JsonProcessingException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }

        return strJsonResponse;
    }
    /**
	 * Deserialize a JSON string into an object of the specified type.
	 * 
	 * @param requestBody
	 *            the JSON string to deserialize
	 * @param targetType
	 *            the class of the object to deserialize into
	 * @param <T>
	 *            the type of the object
	 * @return the deserialized object
	 * @throws JsonProcessingException
	 *             if there is an error during deserialization
	 */
    public static <T> T deserialize( String requestBody, Class<T> targetType ) throws JsonProcessingException
	{
		// Deserialize the JSON string into the target type
		return _mapper.readValue( requestBody, targetType );
	}
    
    /** 
     * Serialize an object to a JSON string.
     * @param objectValue
     * @return a JSON string representation of the object
     * @throws JsonProcessingException
     */
    public static String serialize( Object objectValue) throws JsonProcessingException
   	{
   		// serialize to JSON string
   		return _mapper.writeValueAsString( objectValue );
   	}

}
