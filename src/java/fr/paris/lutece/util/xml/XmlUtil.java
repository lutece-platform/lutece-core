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

import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.io.StringWriter;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;


/**
 * This class provides utils for XML document management.
 */
public final class XmlUtil
{
    public static final String PROPERTIES_XML_HEADER = "xml.header";
    private static final String TAG_BEGIN = "<";
    private static final String TAG_CLOSE_BEGIN = "</";
    private static final String TAG_END = ">\r\n";
    private static final String TAG_CLOSE_END = " />\r\n";
    private static final String TAG_SEPARATOR = " ";
    private static final String TAG_ASSIGNMENT = "=";
    private static final String TAG_ENCLOSED = "\"";

    /**
     * Instantiates a new xml util.
     */
    private XmlUtil(  )
    {
    }

    /**
     * Gets the header of an XML file
     * @return The header
     */
    public static String getXmlHeader(  )
    {
        String strXmlHeader = AppPropertiesService.getProperty( PROPERTIES_XML_HEADER );

        return strXmlHeader;
    }

    /**
     * This method performs XSL Transformation.
     * <br />
     * <b>Deprecated use XmlTransformer.transform</b>
     * @param source The input XML document
     * @param stylesheet The XSL stylesheet
     * @param params parameters to apply to the XSL Stylesheet
     * @param outputProperties properties to use for the xsl transform. Will overload the xsl output definition.
     * @return The output document transformed
     * @throws Exception The exception
     */
    @Deprecated
    public static String transform( Source source, Source stylesheet, Map<String, String> params,
        Properties outputProperties ) throws Exception
    {
        try
        {
            TransformerFactory factory = TransformerFactory.newInstance(  );
            Transformer transformer = factory.newTransformer( stylesheet );

            if ( outputProperties != null )
            {
                transformer.setOutputProperties( outputProperties );
            }

            if ( params != null )
            {
                transformer.clearParameters(  );

                for ( Entry<String, String> entry : params.entrySet(  ) )
                {
                    String name = entry.getKey(  );
                    String value = entry.getValue(  );
                    transformer.setParameter( name, value );
                }
            }

            StringWriter sw = new StringWriter(  );
            Result result = new StreamResult( sw );
            transformer.transform( source, result );

            return sw.toString(  );
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
        catch ( TransformerException e )
        {
            String strMessage = e.getMessage(  );

            if ( e.getLocationAsString(  ) != null )
            {
                strMessage += ( "- location : " + e.getLocationAsString(  ) );
            }

            throw new Exception( "Error transforming document XSLT : " + strMessage, e.getCause(  ) );
        }
        catch ( Exception e )
        {
            throw new Exception( "Error transforming document XSLT : " + e.getMessage(  ), e );
        }
    }

    /**
     * Add an element to an XML document buffer
     *
     * @param strXmlBuffer The XML document buffer
     * @param strTag The tag name of the element to add
     * @param strValue The value of the element
     */
    public static void addElement( StringBuffer strXmlBuffer, String strTag, String strValue )
    {
        strXmlBuffer.append( TAG_BEGIN );
        strXmlBuffer.append( strTag );
        strXmlBuffer.append( ">" );
        strXmlBuffer.append( strValue );
        strXmlBuffer.append( TAG_CLOSE_BEGIN );
        strXmlBuffer.append( strTag );
        strXmlBuffer.append( TAG_END );
    }

    /**
     * Add an empty element (<   />) to an XML document buffer.
     *
     * @param strXmlBuffer The XML document buffer
     * @param strTag The tag name of the element to add
     * @param attrList The attributes list
     */
    public static void addEmptyElement( StringBuffer strXmlBuffer, String strTag, Map<?, ?> attrList )
    {
        strXmlBuffer.append( TAG_BEGIN );
        strXmlBuffer.append( strTag );

        if ( attrList != null )
        {
            for ( Entry<?, ?> entry : attrList.entrySet(  ) )
            {
                String code = (String) entry.getKey(  );
                strXmlBuffer.append( TAG_SEPARATOR + code + TAG_ASSIGNMENT + TAG_ENCLOSED + entry.getValue(  ) +
                    TAG_ENCLOSED );
            }
        }

        strXmlBuffer.append( TAG_CLOSE_END );
    }

    /**
     * Add an element to an XML document buffer with attributes
     *
     * @param strXmlBuffer The XML document buffer
     * @param strTag The tag name of the element to add
     * @param strValue The value of the element
     * @param attrList the attribute list
     */
    public static void addElement( StringBuffer strXmlBuffer, String strTag, String strValue, Map<?, ?> attrList )
    {
        strXmlBuffer.append( TAG_BEGIN );
        strXmlBuffer.append( strTag );

        if ( attrList != null )
        {
            for ( Entry<?, ?> entry : attrList.entrySet(  ) )
            {
                String code = (String) entry.getKey(  );
                strXmlBuffer.append( TAG_SEPARATOR + code + TAG_ASSIGNMENT + TAG_ENCLOSED + entry.getValue(  ) +
                    TAG_ENCLOSED );
            }
        }

        strXmlBuffer.append( ">" );
        strXmlBuffer.append( strValue );
        strXmlBuffer.append( TAG_CLOSE_BEGIN );
        strXmlBuffer.append( strTag );
        strXmlBuffer.append( TAG_END );
    }

    /**
     * Add an element to an XML document buffer.
     *
     * @param strXmlBuffer The XML document buffer
     * @param strTag The tag name of the element to add
     * @param nValue The value of the element
     */
    public static void addElement( StringBuffer strXmlBuffer, String strTag, int nValue )
    {
        addElement( strXmlBuffer, strTag, String.valueOf( nValue ) );
    }

    /**
     * Add a CDATA type element to XML document buffer.
     *
     * @param strXmlBuffer The XML document buffer
     * @param strTag The tag name of the element to add
     * @param strValue The value of the element
     */
    public static void addElementHtml( StringBuffer strXmlBuffer, String strTag, String strValue )
    {
        strXmlBuffer.append( TAG_BEGIN );
        strXmlBuffer.append( strTag );
        strXmlBuffer.append( "><![CDATA[" );
        strXmlBuffer.append( strValue );
        strXmlBuffer.append( "]]></" );
        strXmlBuffer.append( strTag );
        strXmlBuffer.append( TAG_END );
    }

    /**
     * Add a CDATA type element to XML document buffer.
     *
     * @param strXmlBuffer The XML document buffer
     * @param strTag The tag name of the element to add
     * @param strValue The value of the element
     * @param attrList The attributes list
     */
    public static void addElementHtml( StringBuffer strXmlBuffer, String strTag, String strValue, Map<?, ?> attrList )
    {
        strXmlBuffer.append( TAG_BEGIN );
        strXmlBuffer.append( strTag );

        if ( attrList != null )
        {
            for ( Entry<?, ?> entry : attrList.entrySet(  ) )
            {
                String code = (String) entry.getKey(  );
                strXmlBuffer.append( TAG_SEPARATOR + code + TAG_ASSIGNMENT + TAG_ENCLOSED + entry.getValue(  ) +
                    TAG_ENCLOSED );
            }
        }

        strXmlBuffer.append( "><![CDATA[" );
        strXmlBuffer.append( strValue );
        strXmlBuffer.append( "]]></" );
        strXmlBuffer.append( strTag );
        strXmlBuffer.append( TAG_END );
    }

    /**
     * Add an opening tag for an element in a XML document buffer
     *
     * @param strXmlBuffer The XML document buffer
     * @param strTag The tag name of the element to add
     */
    public static void beginElement( StringBuffer strXmlBuffer, String strTag )
    {
        beginElement( strXmlBuffer, strTag, null );
    }

    /**
     * Add an opening tag for an element in a XML document buffer
     *
     * @param strXmlBuffer The XML document buffer
     * @param strTag The tag name of the element to add
     * @param attrList The attributes list
     */
    public static void beginElement( StringBuffer strXmlBuffer, String strTag, Map<?, ?> attrList )
    {
        strXmlBuffer.append( TAG_BEGIN );
        strXmlBuffer.append( strTag );

        if ( attrList != null )
        {
            for ( Entry<?, ?> entry : attrList.entrySet(  ) )
            {
                String code = (String) entry.getKey(  );
                strXmlBuffer.append( TAG_SEPARATOR + code + TAG_ASSIGNMENT + TAG_ENCLOSED + entry.getValue(  ) +
                    TAG_ENCLOSED );
            }
        }

        strXmlBuffer.append( TAG_END );
    }

    /**
     * Add a closing tag for an element in a XML document buffer
     *
     * @param strXmlBuffer The XML document buffer
     * @param strTag The tag name of the element to add
     */
    public static void endElement( StringBuffer strXmlBuffer, String strTag )
    {
        strXmlBuffer.append( TAG_CLOSE_BEGIN );
        strXmlBuffer.append( strTag );
        strXmlBuffer.append( TAG_END );
    }
}
