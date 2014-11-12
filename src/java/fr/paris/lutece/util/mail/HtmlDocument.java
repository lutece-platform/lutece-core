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
package fr.paris.lutece.util.mail;

import fr.paris.lutece.portal.service.util.AppLogService;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.w3c.tidy.Tidy;

import java.io.ByteArrayInputStream;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;


/**
 * This classes provides implementation to retrieve urls from specified tags
 * on an HTML page.
 */
public class HtmlDocument
{
    // Definition of some basic html elements
    /**
     *  To define a CSS, html element must have:
     *  <ul>
     *  <li>"link" tag name</li>
     *  <li>"rel" attribute equal to "stylesheet"</li>
     *  </ul>
     *  The url is contained in the attributed named "href"
     */
    public static final ElementUrl ELEMENT_CSS;

    /**
     *  To define a javascript, html element must have:
     *  <ul>
     *  <li>"script" tag name</li>
     *  <li>"type" attribute equal to "text/javascript"</li>
     *  </ul>
     *  The url is contained in the attributed named "src"
     */
    public static final ElementUrl ELEMENT_JAVASCRIPT;

    /**
         *  To define an image, html element must have:
         *  <ul>
         *  <li>"img" tag name</li>
         *  </ul>
         *  The url is contained in the attributed named "src"
         */
    public static final ElementUrl ELEMENT_IMG;

    static
    {
        ELEMENT_CSS = new ElementUrl( "link", "href", "rel", "stylesheet" );
        ELEMENT_JAVASCRIPT = new ElementUrl( "script", "src", "type", "text/javascript" );
        ELEMENT_IMG = new ElementUrl( "img", "src", null, null );
    }

    private Document _content;
    private String _strBaseUrl;
    private boolean _useAbsoluteUrl;

    /**
     * Instanciates an HtmlDocument after having built the DOM tree.
     *
     * @param strHtml The Html code to be parsed.
     * @param strBaseUrl The Base url used to retrieve urls.
     * @param useAbsoluteUrl Determine if we use absolute or relative url for HTML element's names
     */
    public HtmlDocument( String strHtml, String strBaseUrl, boolean useAbsoluteUrl )
    {
        // use of tidy to retrieve the DOM tree
        Tidy tidy = new Tidy(  );
        tidy.setQuiet( true );
        tidy.setShowWarnings( false );

        _content = tidy.parseDOM( new ByteArrayInputStream( strHtml.getBytes(  ) ), null );
        _strBaseUrl = ( strBaseUrl == null ) ? "" : strBaseUrl;
        _useAbsoluteUrl = useAbsoluteUrl;
    }

    /**
     * Get the urls of all html elements specified by elementType
     *
     * @param elementType the type of element to get
     * @return a Collection containing the urls. Those urls are Objects, as defined by getUrl().
     */
    public Map<String, URL> getAllUrls( ElementUrl elementType )
    {
        Map<String, URL> mapUrl = new HashMap<String, URL>(  );

        NodeList nodes = _content.getElementsByTagName( elementType.getTagName(  ) );

        for ( int i = 0; i < nodes.getLength(  ); i++ )
        {
            Node node = nodes.item( i );
            NamedNodeMap attributes = node.getAttributes(  );

            // Test if the element matches the required attribute
            if ( elementType.getTestedAttributeName(  ) != null )
            {
                String strRel = attributes.getNamedItem( elementType.getTestedAttributeName(  ) ).getNodeValue(  );

                if ( !elementType.getTestedAttributeValue(  ).equals( strRel ) )
                {
                    continue;
                }
            }

            // Retrieve the url, then test if it matches the base url
            String strSrc = attributes.getNamedItem( elementType.getAttributeName(  ) ).getNodeValue(  );

            if ( strSrc.startsWith( _strBaseUrl ) )
            {
                try
                {
                    URL url = new URL( strSrc );
                    mapUrl.put( getUrlName( url ), url );
                }
                catch ( MalformedURLException e )
                {
                    // ignored document
                    AppLogService.info( strSrc + " not found, location ignored." );
                }
            }
        }

        return mapUrl;
    }

    /**
     * Get the urls of all html elements specified by elementType
     *
     * @param elementType the type of element to get
     * @return a Collection containing the urls. Those urls are Objects, as defined by getUrl().
     */
    public List<UrlAttachment> getAllUrlsAttachement( ElementUrl elementType )
    {
        List<UrlAttachment> listUrlAttachement = new ArrayList<UrlAttachment>(  );
        UrlAttachment urlAttachement;
        NodeList nodes = _content.getElementsByTagName( elementType.getTagName(  ) );

        for ( int i = 0; i < nodes.getLength(  ); i++ )
        {
            Node node = nodes.item( i );
            NamedNodeMap attributes = node.getAttributes(  );

            // Test if the element matches the required attribute
            if ( elementType.getTestedAttributeName(  ) != null )
            {
                String strRel = attributes.getNamedItem( elementType.getTestedAttributeName(  ) ).getNodeValue(  );

                if ( !elementType.getTestedAttributeValue(  ).equals( strRel ) )
                {
                    continue;
                }
            }

            // Retrieve the url, then test if it matches the base url
            String strAttributeName = elementType.getAttributeName(  );

            if ( ( strAttributeName != null ) && ( attributes != null ) )
            {
                Node attributeNode = attributes.getNamedItem( strAttributeName );

                if ( attributeNode != null )
                {
                    String strSrc = attributeNode.getNodeValue(  );

                    if ( ( strSrc != null ) && strSrc.startsWith( _strBaseUrl ) )
                    {
                        try
                        {
                            URL url = new URL( strSrc );
                            urlAttachement = new UrlAttachment( getUrlName( url ), url );
                            listUrlAttachement.add( urlAttachement );
                        }
                        catch ( MalformedURLException e )
                        {
                            // ignored document
                            AppLogService.info( strSrc + " not found, location ignored." );
                        }
                    }
                }
            }
        }

        return listUrlAttachement;
    }

    /**
     * Loads the url in a DataHandler
     *
     * @param url an absolute url
     * @return an Object containing the DataHandler
     */
    protected Object getUrlContent( URL url )
    {
        return new DataHandler( url );
    }

    /**
     * Return the absolute or relative url depending on _useAbsoluteUrl
     * @param url an absolute url
     * @return a String representing the url
     */
    protected String getUrlName( URL url )
    {
        return _useAbsoluteUrl ? url.toExternalForm(  ) : url.getPath(  );
    }

    /**
     * provide a description for the HTML elements to be parsed
     */
    private static class ElementUrl
    {
        private String _strTagName;
        private String _strAttributeName;
        private String _strTestedAttributeName;
        private String _strTestedAttributeValue;

        /**
         * Instanciates an ElementUrl
         *
         * @param strTagName the tag name to get (example: link, script, img, ...)
         * @param strAttributeName the attribute name to get (example: src, href, ...)
         * @param strTestedAttributeName the attribute name to test
         * @param strTestedAttributeValue the value of the attribute to test :
         * if the value of the attribute strTestedAttributeName equals strTestedAttributeValue,
         * then we get the element's url, else we do nothing.
         */
        public ElementUrl( String strTagName, String strAttributeName, String strTestedAttributeName,
            String strTestedAttributeValue )
        {
            _strTagName = strTagName;
            _strAttributeName = strAttributeName;
            _strTestedAttributeName = strTestedAttributeName;
            _strTestedAttributeValue = strTestedAttributeValue;
        }

        /**
         * Returns the attributeName
         * @return the attributeName
         */
        public String getAttributeName(  )
        {
            return _strAttributeName;
        }

        /**
         * Returns the tagName
         * @return the tagName
         */
        public String getTagName(  )
        {
            return _strTagName;
        }

        /**
         * Returns the testedAttributeName
         * @return the testedAttributeName
         */
        public String getTestedAttributeName(  )
        {
            return _strTestedAttributeName;
        }

        /**
         * Returns the testedAttributeValue
         * @return the testedAttributeValue
         */
        public String getTestedAttributeValue(  )
        {
            return _strTestedAttributeValue;
        }
    }
}
