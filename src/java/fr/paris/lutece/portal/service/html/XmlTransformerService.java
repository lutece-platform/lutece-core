/*
 * Copyright (c) 2002-2008, Mairie de Paris
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

import fr.paris.lutece.portal.service.util.AppLogService;
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
public abstract class XmlTransformerService
{
    /**
     * Constructor
     */
    protected XmlTransformerService(  )
    {
    }

    /**
     * This method performes XSL transformation.
     *
     * @param strXml The XML document content
     * @param baSource The XSL source
     * @param params Parameters that can be used by the XSL stylesheet
     * @return The output document
     */
    public static synchronized String transformBySource( String strXml, byte[] baSource, Map params )
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
    public static synchronized String transformBySource( String strXml, byte[] baSource, Map params,
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
    public static synchronized String transformBySource( String strXml, Source sourceStyleSheet, Map params,
        Properties outputProperties )
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
}
