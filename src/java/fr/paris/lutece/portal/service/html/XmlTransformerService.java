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
package fr.paris.lutece.portal.service.html;

import fr.paris.lutece.portal.business.stylesheet.StyleSheet;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.UniqueIDGenerator;
import fr.paris.lutece.util.xml.XmlTransformer;
import fr.paris.lutece.util.xml.XmlUtil;

import java.io.ByteArrayInputStream;
import java.io.StringReader;

import java.util.Map;
import java.util.Properties;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;


/**
 * This class provides methods to transform XML documents using XSLT.
 */
public final class XmlTransformerService
{
    private static final String XSLSOURCE_STYLE_PREFIX_ID = UniqueIDGenerator.getNewId(  );
    private static final String LOGGER_XML_CONTENT = "lutece.debug.xmlContent";

    /**
     * This method performes XSL transformation.
     *
     * @param strXml The XML document content
     * @param baSource The XSL source
     * @param params Parameters that can be used by the XSL stylesheet
     * @return The output document
     */
    @Deprecated
    public static String transformBySource( String strXml, byte[] baSource, Map<String, String> params )
    {
        return transformBySource( strXml, baSource, params, null );
    }

    /**
     * This method performes XSL transformation.
     *
     * @param strXml The XML document content
     * @param baSource The XSL source
     * @param params Parameters that can be used by the XSL stylesheet
     * @param outputProperties properties to use for the xsl transform. Will overload the xsl output definition.
     * @return The output document
     */
    @Deprecated
    public static String transformBySource( String strXml, byte[] baSource, Map<String, String> params,
        Properties outputProperties )
    {
        Source xslSource = new StreamSource( new ByteArrayInputStream( baSource ) );

        return transformBySource( strXml, xslSource, params, outputProperties );
    }

    /**
     * This method performes XSL transformation.
     *
     * @param strXml The XML document content
     * @param sourceStyleSheet The XSL source
     * @param params Parameters that can be used by the XSL stylesheet
     * @param outputProperties properties to use for the xsl transform. Will overload the xsl output definition.
     * @return The output document
     */
    @Deprecated
    public static synchronized String transformBySource( String strXml, Source sourceStyleSheet,
        Map<String, String> params, Properties outputProperties )
    {
        StringReader srInputXml = new StringReader( strXml );
        StreamSource sourceDocument = new StreamSource( srInputXml );
        String strContent = null;

        try
        {
            strContent = XmlUtil.transform( sourceDocument, sourceStyleSheet, params, outputProperties );
        }
        catch ( Exception e )
        {
            strContent = e.getMessage(  );
            AppLogService.error( e.getMessage(  ), e );
        }

        return strContent;
    }

    //-------------------------------

    /**
     * This method performs XSL transformation with cache.
     * @param strXml The XML document content
     * @param xslSource The XSL source
     * @param params Parameters that can be used by the XSL StyleSheet
     * @return the output html
     */
    public String transformBySourceWithXslCache( String strXml, StyleSheet xslSource, Map<String, String> params )
    {
        return transformBySourceWithXslCache( strXml, xslSource.getSource(  ),
            XSLSOURCE_STYLE_PREFIX_ID + xslSource.getId(  ), params, null );
    }

    /**
     * This method performs XSL transformation with cache.
     * @param strXml The XML document content
     * @param xslSource The XSL source
     * @param params Parameters that can be used by the XSL StyleSheet
     * @param outputProperties Properties to use for the XSL transform. Will overload the XSL output definition.
     * @return the output html
     */
    public String transformBySourceWithXslCache( String strXml, StyleSheet xslSource, Map<String, String> params,
        Properties outputProperties )
    {
        return transformBySourceWithXslCache( strXml, xslSource.getSource(  ),
            XSLSOURCE_STYLE_PREFIX_ID + xslSource.getId(  ), params, outputProperties );
    }

    /**
     * This method performs XSL transformation with cache.
     * @param strXml The XML document content
     * @param baSource The XSL source
     * @param strStyleSheetId The StyleSheet Id
     * @param params Parameters that can be used by the XSL StyleSheet
     * @return The output document
     */
    public String transformBySourceWithXslCache( String strXml, byte[] baSource, String strStyleSheetId,
        Map<String, String> params )
    {
        return transformBySourceWithXslCache( strXml, baSource, strStyleSheetId, params, null );
    }

    /**
     * This method performs XSL transformation with cache.
     * @param strXml The XML document content
     * @param baSource The XSL source
     * @param strStyleSheetId The StyleSheet Id
     * @param params Parameters that can be used by the XSL StyleSheet
     * @param outputProperties Properties to use for the XSL transform. Will overload the XSL output definition.
     * @return The output document
     */
    public String transformBySourceWithXslCache( String strXml, byte[] baSource, String strStyleSheetId,
        Map<String, String> params, Properties outputProperties )
    {
        Source xslSource = new StreamSource( new ByteArrayInputStream( baSource ) );

        return transformBySourceWithXslCache( strXml, xslSource, strStyleSheetId, params, outputProperties );
    }

    /**
     * This method performs XSL transformation with cache.
     * @param strXml The XML document content
     * @param sourceStyleSheet The XSL source
     * @param strStyleSheetId The StyleSheet Id
     * @param params Parameters that can be used by the XSL StyleSheet
     * @param outputProperties the output parameter
     * @return The output document
     */
    public String transformBySourceWithXslCache( String strXml, Source sourceStyleSheet, String strStyleSheetId,
        Map<String, String> params, Properties outputProperties )
    {
        StringReader srInputXml = new StringReader( strXml );
        StreamSource sourceDocument = new StreamSource( srInputXml );
        String strContent = null;
        XmlTransformer xmlTransformer = new XmlTransformer(  );

        try
        {
            if ( AppLogService.isDebugEnabled( LOGGER_XML_CONTENT ) )
            {
                AppLogService.debug( LOGGER_XML_CONTENT, strXml );
            }

            strContent = xmlTransformer.transform( sourceDocument, sourceStyleSheet, strStyleSheetId, params,
                    outputProperties );
        }
        catch ( Exception e )
        {
            strContent = e.getMessage(  );
            AppLogService.error( e.getMessage(  ), e );
        }

        return strContent;
    }

    /**
     * This method performs XSL transformation with cache.
     * @param sourceXml The XML document content
     * @param sourceStyleSheet The XSL source
     * @param strStyleSheetId The StyleSheet Id
     * @param params Parameters that can be used by the XSL StyleSheet
     * @param outputProperties the output parameter
     * @return The output document
     */
    public String transformBySourceWithXslCache( Source sourceXml, Source sourceStyleSheet, String strStyleSheetId,
        Map<String, String> params, Properties outputProperties )
    {
        String strContent = null;
        XmlTransformer xmlTransformer = new XmlTransformer(  );

        try
        {
            strContent = xmlTransformer.transform( sourceXml, sourceStyleSheet, strStyleSheetId, params,
                    outputProperties );
        }
        catch ( Exception e )
        {
            strContent = e.getMessage(  );
            AppLogService.error( e.getMessage(  ), e );
        }

        return strContent;
    }

    /**
     * This method clean XSL transformer cache
     */
    public static void clearXslCache(  )
    {
        XmlTransformer.cleanTransformerList(  );
    }
}
