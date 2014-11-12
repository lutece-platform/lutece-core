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
package fr.paris.lutece.util.xml;

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.io.StringWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;


/**
 * This class provides methods to transform XML documents using XSLT with cache
 */
public final class XmlTransformer
{
    public static final String PROPERTY_TRANSFORMER_POOL_SIZE = "service.xmlTransformer.transformerPoolSize";
    public static final int TRANSFORMER_POOL_SIZE = AppPropertiesService.getPropertyInt( PROPERTY_TRANSFORMER_POOL_SIZE,
            2 );
    public static final int MAX_TRANSFORMER_SIZE = 1000;
    private static final List<ConcurrentMap<String, Transformer>> transformerPoolList = new ArrayList<ConcurrentMap<String, Transformer>>( TRANSFORMER_POOL_SIZE );

    static
    {
        for ( int i = 0; i < TRANSFORMER_POOL_SIZE; i++ )
        {
            transformerPoolList.add( new ConcurrentHashMap<String, Transformer>( MAX_TRANSFORMER_SIZE ) );
        }
    }

    /**
     * This method try to get a transformer instance from cache or create a new one if can't
     * @param stylesheet The XML document content
     * @param strStyleSheetId The StyleSheet Id
     * @return XmlTransformer object
     * @throws Exception the exception
     */
    private Transformer getTransformer( Source stylesheet, String strStyleSheetId )
        throws Exception
    {
        Transformer result = null;

        if ( TRANSFORMER_POOL_SIZE > 0 )
        {
            int nTransformerListIndex = 0;

            do
            {
                result = transformerPoolList.get( nTransformerListIndex ).remove( strStyleSheetId );
                nTransformerListIndex++;
            }
            while ( ( result == null ) && ( nTransformerListIndex < TRANSFORMER_POOL_SIZE ) );
        }

        if ( result == null )
        {
            // only one thread can use transformer
            try
            {
                result = TransformerFactory.newInstance(  ).newTransformer( stylesheet );
                AppLogService.debug( " --  XML Transformer instantiation : strStyleSheetId=" + strStyleSheetId );
            }
            catch ( TransformerConfigurationException e )
            {
                String strMessage = e.getMessage(  );

                if ( e.getLocationAsString(  ) != null )
                {
                    strMessage += ( "- location : " + e.getLocationAsString(  ) );
                }

                throw new Exception( "Error transforming document XSLT : " + strMessage, e.getCause(  ) );
            }
            catch ( TransformerFactoryConfigurationError e )
            {
                throw new Exception( "Error transforming document XSLT : " + e.getMessage(  ), e );
            }
        }
        else
        {
            //AppLogService.debug("Get XML Transformer from cache : strStyleSheetId=" + strStyleSheetId);
            result.clearParameters(  );
            result.setOutputProperties( null );
        }

        return result;
    }

    /**
     * Remove all Transformer instance from cache
     */
    public static void cleanTransformerList(  )
    {
        for ( ConcurrentMap<String, Transformer> transformerList : transformerPoolList )
        {
            transformerList.clear(  );
        }
    }

    /**
     * Gets the number of transformers
     * @return the transformers count
     */
    public static int getTransformersCount(  )
    {
        int nCount = 0;

        for ( ConcurrentMap<String, Transformer> transformerList : transformerPoolList )
        {
            nCount += transformerList.size(  );
        }

        return nCount;
    }

    /**
     * Release Transformer instance in cache
     * @param transformer The XML transformer
     * @param strStyleSheetId The StyleSheet Id
     */
    private void releaseTransformer( Transformer transformer, String strStyleSheetId )
    {
        if ( TRANSFORMER_POOL_SIZE > 0 )
        {
            Transformer result = null;
            ConcurrentMap<String, Transformer> transformerList = null;
            int nTransformerListIndex = 0;

            do
            {
                transformerList = transformerPoolList.get( nTransformerListIndex );
                nTransformerListIndex++;

                // This set of action is not performed atomically but it can not cause problems
                if ( transformerList.size(  ) < MAX_TRANSFORMER_SIZE )
                {
                    result = transformerList.putIfAbsent( strStyleSheetId, transformer );
                }
                else
                {
                    // Aggressive release ( speed up GC )
                    transformerList.clear(  );

                    AppLogService.info( "XmlTransformer : cache is full, you may need to increase cache size." );
                }
            }
            while ( ( result != null ) && ( nTransformerListIndex < TRANSFORMER_POOL_SIZE ) );
        }
    }

    /**
     * Transform XML documents using XSLT with cache
     * @param source The XML document content
     * @param stylesheet The XSL source
     * @param strStyleSheetId The StyleSheet Id
     * @param params Parameters that can be used by the XSL StyleSheet
     * @param outputProperties Properties to use for the XSL transform. Will overload the XSL output definition.
     * @return The output document
     * @throws Exception The exception
     */
    public String transform( Source source, Source stylesheet, String strStyleSheetId, Map<String, String> params,
        Properties outputProperties ) throws Exception
    {
        Transformer transformer = this.getTransformer( stylesheet, strStyleSheetId );

        if ( outputProperties != null )
        {
            transformer.setOutputProperties( outputProperties );
        }

        if ( params != null )
        {
            transformer.clearParameters(  );

            for ( Entry<String, String> entry : params.entrySet(  ) )
            {
                transformer.setParameter( entry.getKey(  ), entry.getValue(  ) );
            }
        }

        StringWriter sw = new StringWriter(  );
        Result result = new StreamResult( sw );

        try
        {
            transformer.transform( source, result );
        }
        catch ( TransformerException e )
        {
            String strMessage = "strStyleSheetId = " + strStyleSheetId + " " + e.getMessage(  );

            if ( e.getLocationAsString(  ) != null )
            {
                strMessage += ( " - location : " + e.getLocationAsString(  ) );
            }

            throw new Exception( "Error transforming document XSLT : " + strMessage, e.getCause(  ) );
        }
        finally
        {
            this.releaseTransformer( transformer, strStyleSheetId );
        }

        return sw.toString(  );
    }
}
