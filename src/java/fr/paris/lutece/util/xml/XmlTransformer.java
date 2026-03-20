/*
 * Copyright (c) 2002-2025, City of Paris
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

import java.io.StringReader;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.XMLConstants;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * This class provides methods to transform XML documents using XSLT with cache
 */
public final class XmlTransformer
{
    private static final String ERROR_MESSAGE_XLST = "Error transforming document XSLT : ";
    public static final String PROPERTY_TRANSFORMER_POOL_SIZE = "service.xmlTransformer.transformerPoolSize";
    public static final int TRANSFORMER_POOL_SIZE = AppPropertiesService.getPropertyInt( PROPERTY_TRANSFORMER_POOL_SIZE, 2 );
    public static final int MAX_TRANSFORMER_SIZE = 1000;
    private static final List<ConcurrentMap<String, Templates>> transformersPoolList = new ArrayList<>( TRANSFORMER_POOL_SIZE );

    static
    {
        for ( int i = 0; i < TRANSFORMER_POOL_SIZE; i++ )
        {
            transformersPoolList.add( new ConcurrentHashMap<String, Templates>( MAX_TRANSFORMER_SIZE ) );
        }
    }

    private static final String ORACLE_ENABLE_EXTENSION_FUNCTIONS = "http://www.oracle.com/xml/jaxp/properties/enableExtensionFunctions";
    private static final String SAXON_ALLOW_EXTERNAL_FUNCTIONS = "http://saxon.sf.net/feature/allow-external-functions";

    /**
     * Creates a TransformerFactory hardened against XSLT-based RCE, XXE and SSRF attacks.
     *
     * The following security locks are applied:
     * <ul>
     *   <li>FEATURE_SECURE_PROCESSING — enables the JDK secure-processing mode</li>
     *   <li>enableExtensionFunctions=false — blocks calls to Java classes from XSLT</li>
     *   <li>ACCESS_EXTERNAL_DTD="" — prevents loading of external DTDs (JDK Xalan only)</li>
     *   <li>ACCESS_EXTERNAL_STYLESHEET="" — blocks document(), xsl:include, xsl:import to external URIs (JDK Xalan only)</li>
     * </ul>
     * A restrictive URIResolver is set as an additional defense-in-depth layer.
     * The method adapts to the underlying XSLT processor (JDK Xalan or Saxon) automatically.
     *
     * @return a security-hardened TransformerFactory
     * @throws TransformerConfigurationException if a critical security feature is not supported
     */
    private TransformerFactory createSecureTransformerFactory( ) throws TransformerConfigurationException
    {
        TransformerFactory tf = TransformerFactory.newInstance( );
        tf.setFeature( XMLConstants.FEATURE_SECURE_PROCESSING, true );
        disableExtensionFunctions( tf );
        setExternalAccessRestrictions( tf );
        tf.setURIResolver( ( href, base ) -> {
            throw new TransformerException( "External URI resolution blocked: " + href );
        } );
        return tf;
    }

    /**
     * Disables XSLT extension functions on the given TransformerFactory.
     * Tries the Oracle/JDK property name first (for the built-in Xalan XSLTC processor),
     * then the Saxon-specific property. At least one must succeed.
     *
     * @param tf the TransformerFactory to configure
     * @throws TransformerConfigurationException if extension functions could not be disabled
     */
    private void disableExtensionFunctions( TransformerFactory tf ) throws TransformerConfigurationException
    {
        boolean bDisabled = false;

        try
        {
            tf.setFeature( ORACLE_ENABLE_EXTENSION_FUNCTIONS, false );
            bDisabled = true;
        }
        catch( TransformerConfigurationException e )
        {
            AppLogService.debug( "Oracle enableExtensionFunctions not supported, trying Saxon property" );
        }

        if ( !bDisabled )
        {
            try
            {
                tf.setFeature( SAXON_ALLOW_EXTERNAL_FUNCTIONS, false );
                bDisabled = true;
            }
            catch( TransformerConfigurationException e )
            {
                AppLogService.debug( "Saxon allow-external-functions not supported either" );
            }
        }

        if ( !bDisabled )
        {
            throw new TransformerConfigurationException(
                    "Failed to disable XSLT extension functions: neither Oracle/JDK nor Saxon property is supported by " + tf.getClass( ).getName( ) );
        }
    }

    /**
     * Restricts external DTD and stylesheet access on the given TransformerFactory.
     * These JAXP attributes are supported by the JDK built-in Xalan processor but not by Saxon,
     * which relies on FEATURE_SECURE_PROCESSING instead. Failures are logged but not fatal.
     *
     * @param tf the TransformerFactory to configure
     */
    private void setExternalAccessRestrictions( TransformerFactory tf )
    {
        try
        {
            tf.setAttribute( XMLConstants.ACCESS_EXTERNAL_DTD, "" );
        }
        catch( IllegalArgumentException e )
        {
            AppLogService.debug( "ACCESS_EXTERNAL_DTD not supported by {}", tf.getClass( ).getName( ) );
        }

        try
        {
            tf.setAttribute( XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "" );
        }
        catch( IllegalArgumentException e )
        {
            AppLogService.debug( "ACCESS_EXTERNAL_STYLESHEET not supported by {}", tf.getClass( ).getName( ) );
        }
    }

    /**
     * This method try to get a templates instance from cache or create a new one if can't.
     *
     * Previously (before 6.0.0) it returned directly a transformer, now it returns a templates which can create transformers cheaply.
     * 
     * @param stylesheet
     *            The XML document content
     * @param strStyleSheetId
     *            The StyleSheet Id
     * @return XmlTransformer object
     * @throws TransformerException
     */
    private Templates getTemplates( Source stylesheet, String strStyleSheetId ) throws TransformerException
    {
        Templates result = null;

        if ( TRANSFORMER_POOL_SIZE > 0 )
        {
            int nTransformerListIndex = 0;

            do
            {
                result = transformersPoolList.get( nTransformerListIndex ).remove( strStyleSheetId );
                nTransformerListIndex++;
            }
            while ( ( result == null ) && ( nTransformerListIndex < TRANSFORMER_POOL_SIZE ) );
        }

        if ( result == null )
        {
            // only one thread can use transformer
            try
            {
                result = createSecureTransformerFactory( ).newTemplates( stylesheet );
                AppLogService.debug( " --  XML Templates instantiation : strStyleSheetId= {}", strStyleSheetId );
            }
            catch( TransformerConfigurationException e )
            {
                String strMessage = e.getMessage( );

                if ( e.getLocationAsString( ) != null )
                {
                    strMessage += ( "- location : " + e.getLocationAsString( ) );
                }

                throw new TransformerException( ERROR_MESSAGE_XLST + strMessage, e.getCause( ) );
            }
            catch( TransformerFactoryConfigurationError e )
            {
                throw new TransformerException( ERROR_MESSAGE_XLST + e.getMessage( ), e );
            }
        }

        return result;
    }

    /**
     * Remove all Templates instance from cache. Previously (before 6.0.0) the cache stored transformers, now it stores templates.
     */
    public static void cleanTransformerList( )
    {
        for ( ConcurrentMap<String, Templates> transformerList : transformersPoolList )
        {
            transformerList.clear( );
        }
    }

    /**
     * Gets the number of templates. Previously (before 6.0.0) the cache stored transformers, now it stores templates.
     * 
     * @return the transformers count
     */
    public static int getTransformersCount( )
    {
        int nCount = 0;

        for ( ConcurrentMap<String, Templates> transformerList : transformersPoolList )
        {
            nCount += transformerList.size( );
        }

        return nCount;
    }

    /**
     * Release Transformer instance in cache. Previously (before 6.0.0) the cache stored transformers, now it stores templates.
     * 
     * @param templates
     *            The XML templates
     * @param strStyleSheetId
     *            The StyleSheet Id
     */
    private void releaseTemplates( Templates templates, String strStyleSheetId )
    {
        if ( TRANSFORMER_POOL_SIZE > 0 )
        {
            Templates result = null;
            ConcurrentMap<String, Templates> transformerList = null;
            int nTransformerListIndex = 0;

            do
            {
                transformerList = transformersPoolList.get( nTransformerListIndex );
                nTransformerListIndex++;

                // This set of action is not performed atomically but it can not cause problems
                if ( transformerList.size( ) < MAX_TRANSFORMER_SIZE )
                {
                    result = transformerList.putIfAbsent( strStyleSheetId, templates );
                }
                else
                {
                    // Aggressive release ( speed up GC )
                    transformerList.clear( );

                    AppLogService.info( "XmlTransformer : cache is full, you may need to increase cache size." );
                }
            }
            while ( ( result != null ) && ( nTransformerListIndex < TRANSFORMER_POOL_SIZE ) );
        }
    }

    /**
     * Transform XML documents using XSLT with cache
     * 
     * @param source
     *            The XML document content
     * @param stylesheet
     *            The XSL source
     * @param strStyleSheetId
     *            The StyleSheet Id
     * @param params
     *            Parameters that can be used by the XSL StyleSheet
     * @param outputProperties
     *            Properties to use for the XSL transform. Will overload the XSL output definition.
     * @return The output document
     * @throws TransformerException
     *             The exception
     */
    public String transform( Source source, Source stylesheet, String strStyleSheetId, Map<String, String> params, Properties outputProperties )
            throws TransformerException
    {
        Templates templates = this.getTemplates( stylesheet, strStyleSheetId );
        Transformer transformer = templates.newTransformer( );
        // SECURITY: must return a non-null Source, not throw. XSLTC's LoadDocument silently catches
        // TransformerException and falls back to direct URL loading when source is null.
        transformer.setURIResolver( ( href, base ) -> {
            AppLogService.error( "XSLT security: blocked document() call to external URI: {}", href );
            return new StreamSource( new StringReader( "<blocked/>" ) );
        } );

        if ( outputProperties != null )
        {
            transformer.setOutputProperties( outputProperties );
        }

        if ( params != null )
        {
            transformer.clearParameters( );

            for ( Entry<String, String> entry : params.entrySet( ) )
            {
                transformer.setParameter( entry.getKey( ), entry.getValue( ) );
            }
        }

        StringWriter sw = new StringWriter( );
        Result result = new StreamResult( sw );

        try
        {
            transformer.transform( source, result );
        }
        catch( TransformerException e )
        {
            String strMessage = "strStyleSheetId = " + strStyleSheetId + " " + e.getMessage( );

            if ( e.getLocationAsString( ) != null )
            {
                strMessage += ( " - location : " + e.getLocationAsString( ) );
            }

            throw new TransformerException( ERROR_MESSAGE_XLST + strMessage, e.getCause( ) );
        }
        finally
        {
            this.releaseTemplates( templates, strStyleSheetId );
        }

        return sw.toString( );
    }
}
