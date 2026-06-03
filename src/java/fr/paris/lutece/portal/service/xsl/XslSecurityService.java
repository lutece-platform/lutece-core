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
package fr.paris.lutece.portal.service.xsl;

import fr.paris.lutece.portal.service.util.AppLogService;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Service for validating XSL files against security threats.
 * Detects dangerous constructs such as extension namespaces, script elements,
 * document() calls, and xsl:result-document before the stylesheet reaches
 * the XSLT processor.
 */
public final class XslSecurityService
{
    private static final String XSL_NAMESPACE = "http://www.w3.org/1999/XSL/Transform";

    private static final List<String> BLACKLISTED_NAMESPACE_PREFIXES = List.of(
            "http://xml.apache.org/xalan",
            "http://xml.apache.org/xslt",
            "http://xml.apache.org/xalan/java",
            "http://saxon.sf.net/",
            "http://icl.com/saxon",
            "http://exslt.org/common",
            "http://exslt.org/functions",
            "http://exslt.org/dynamic",
            "urn:schemas-microsoft-com:xslt",
            "http://www.w3.org/TR/xslt-30"
    );

    private static final String DOCUMENT_FUNCTION_PATTERN = "document(";

    /**
     * Checks whether a namespace URI matches any blacklisted prefix.
     * Uses startsWith to catch URIs like {@code http://xml.apache.org/xalan/java/java.lang.Runtime}.
     *
     * @param uri
     *            the namespace URI to check
     * @return true if the URI starts with any blacklisted prefix
     */
    private static boolean isBlacklistedNamespace( String uri )
    {
        return BLACKLISTED_NAMESPACE_PREFIXES.stream( ).anyMatch( uri::startsWith );
    }

    /**
     * Private constructor.
     */
    private XslSecurityService( )
    {
    }

    /**
     * Validates an XSL stylesheet for security threats.
     *
     * @param baXslSource
     *            the XSL source as a byte array
     * @return a list of security violations found, empty if the stylesheet is safe
     */
    public static List<String> validateXslSecurity( byte [ ] baXslSource )
    {
        List<String> listViolations = new ArrayList<>( );

        try
        {
            SAXParserFactory factory = SAXParserFactory.newInstance( );
            factory.setNamespaceAware( true );
            factory.setFeature( "http://apache.org/xml/features/disallow-doctype-decl", true );
            factory.setFeature( "http://xml.org/sax/features/external-general-entities", false );
            factory.setFeature( "http://xml.org/sax/features/external-parameter-entities", false );

            SAXParser parser = factory.newSAXParser( );
            XslSecurityHandler handler = new XslSecurityHandler( listViolations );
            InputSource is = new InputSource( new ByteArrayInputStream( baXslSource ) );
            parser.parse( is, handler );
        }
        catch( SAXException e )
        {
            if ( e.getMessage( ) != null && e.getMessage( ).contains( "DOCTYPE" ) )
            {
                listViolations.add( "DOCTYPE declaration detected" );
            }
            else
            {
                listViolations.add( "XML parsing error: " + e.getClass( ).getSimpleName( ) );
            }
            AppLogService.error( "XSL security validation error: {}", e.getMessage( ), e );
        }
        catch( Exception e )
        {
            listViolations.add( "XML parsing error: " + e.getClass( ).getSimpleName( ) );
            AppLogService.error( "XSL security validation error: {}", e.getMessage( ), e );
        }

        return listViolations;
    }

    /**
     * SAX handler that detects dangerous XSL constructs during parsing.
     */
    private static class XslSecurityHandler extends DefaultHandler
    {
        private final List<String> _listViolations;

        /**
         * Constructor.
         *
         * @param listViolations
         *            the list to collect violations into
         */
        XslSecurityHandler( List<String> listViolations )
        {
            _listViolations = listViolations;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void startPrefixMapping( String prefix, String uri ) throws SAXException
        {
            if ( isBlacklistedNamespace( uri ) )
            {
                _listViolations.add( "Forbidden extension namespace: " + uri + " (prefix: " + prefix + ")" );
                AppLogService.error( "XSL security: blocked extension namespace {} (prefix: {})", uri, prefix );
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void startElement( String uri, String localName, String qName, Attributes attributes ) throws SAXException
        {
            checkForbiddenElement( uri, localName );
            checkDocumentFunction( attributes );
        }

        /**
         * Checks if the element is a forbidden script or result-document element.
         *
         * @param uri
         *            the namespace URI
         * @param localName
         *            the local name of the element
         */
        private void checkForbiddenElement( String uri, String localName )
        {
            if ( "script".equals( localName ) && isBlacklistedNamespace( uri ) )
            {
                _listViolations.add( "Forbidden script element: {" + uri + "}" + localName );
                AppLogService.error( "XSL security: blocked script element {{}}{}", uri, localName );
            }

            if ( XSL_NAMESPACE.equals( uri ) && "result-document".equals( localName ) )
            {
                _listViolations.add( "Forbidden xsl:result-document element" );
                AppLogService.error( "XSL security: blocked xsl:result-document element" );
            }
        }

        /**
         * Checks all attributes for the presence of the document() function.
         *
         * @param attributes
         *            the element attributes
         */
        private void checkDocumentFunction( Attributes attributes )
        {
            for ( int i = 0; i < attributes.getLength( ); i++ )
            {
                String strValue = attributes.getValue( i );

                if ( strValue != null && strValue.contains( DOCUMENT_FUNCTION_PATTERN ) )
                {
                    _listViolations.add( "Forbidden document() function call in attribute " + attributes.getQName( i ) );
                    AppLogService.error( "XSL security: blocked document() call in attribute {}", attributes.getQName( i ) );
                }
            }
        }
    }
}
