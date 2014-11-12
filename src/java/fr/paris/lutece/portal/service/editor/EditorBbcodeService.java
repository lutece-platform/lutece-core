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
package fr.paris.lutece.portal.service.editor;

import fr.paris.lutece.portal.business.editor.ParserComplexElement;
import fr.paris.lutece.portal.business.editor.ParserElement;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.parser.BbcodeUtil;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * This class Provides a parser BBCODE
 *
 */
public class EditorBbcodeService implements IEditorBbcodeService
{
    /** Constants **/
    private static final String CONSTANT_EDITOR_BBCODE_ELEMENT_CODE = "code";
    private static final String CONSTANT_EDITOR_BBCODE_ELEMENT_VALUE = "value";
    private static final String CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_TAG_NAME = "tagName";
    private static final String CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_OPEN_SUBST_WITH_PARAM = "openSubstWithParam";
    private static final String CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_CLOSE_SUBST_WITH_PARAM = "closeSubstWithParam";
    private static final String CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_OPEN_SUBST_WITHOUT_PARAM = "openSubstWithoutParam";
    private static final String CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_CLOSE_SUBST_WITHOUT_PARAM = "closeSubstWithoutParam";
    private static final String CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_INTERNAL_SUBST = "internalSubst";
    private static final String CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_PROCESS_INTERNAL_TAGS = "processInternalTags";
    private static final String CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_ACCEPT_PARAM = "acceptParam";
    private static final String CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_REQUIRES_QUOTED_PARAM = "requiresQuotedParam";

    /** Properties**/
    private static final String PROPERTY_EDITOR_BBCODE_COMPLEX_ELEMENT_PATH = "editors.parser.bbcode.complexElement";
    private static final String PROPERTY_EDITOR_BBCODE_ELEMENT_PATH = "editors.parser.bbcode.element";
    private static final String PROPERTY_PARSER_ELEMENTS = "editors.parser.bbcode.elements";
    private static final String PROPERTY_PARSER_COMPLEX_ELEMENTS = "editors.parser.bbcode.complexElements";
    private static final String SEPARATOR = ",";
    private static volatile EditorBbcodeService _singleton;
    private static List<ParserElement> _listParserElement;
    private static List<ParserComplexElement> _listParserComplexElement;

    /**
     * {@inheritDoc}
     */
    @Override
    public String parse( String strValue )
    {
        return BbcodeUtil.parse( strValue, _listParserElement, _listParserComplexElement );
    }

    /**
     * Returns the unique instance of the service
     * @return The instance of the service
     */
    public static EditorBbcodeService getInstance(  )
    {
        if ( _singleton == null )
        {
            synchronized ( EditorBbcodeService.class )
            {
                EditorBbcodeService service = new EditorBbcodeService(  );
                service.init(  );
                _singleton = service;
            }
        }

        return _singleton;
    }

    /**
     * Init service
     */
    public void init(  )
    {
        _listParserElement = new ArrayList<ParserElement>(  );
        _listParserComplexElement = new ArrayList<ParserComplexElement>(  );

        //init simple elements
        String strParserElements = AppPropertiesService.getProperty( PROPERTY_PARSER_ELEMENTS );

        if ( StringUtils.isNotBlank( strParserElements ) )
        {
            String[] tabParserElements = strParserElements.split( SEPARATOR );
            String strCodeElement;
            String strValueElement;

            for ( int i = 0; i < tabParserElements.length; i++ )
            {
                strCodeElement = AppPropertiesService.getProperty( PROPERTY_EDITOR_BBCODE_ELEMENT_PATH + "." +
                        tabParserElements[i] + "." + CONSTANT_EDITOR_BBCODE_ELEMENT_CODE );
                strValueElement = AppPropertiesService.getProperty( PROPERTY_EDITOR_BBCODE_ELEMENT_PATH + "." +
                        tabParserElements[i] + "." + CONSTANT_EDITOR_BBCODE_ELEMENT_VALUE );
                _listParserElement.add( new ParserElement( strCodeElement, strValueElement ) );
            }
        }

        //init complex elements
        String strParserComplexElements = AppPropertiesService.getProperty( PROPERTY_PARSER_COMPLEX_ELEMENTS );

        if ( StringUtils.isNotBlank( strParserComplexElements ) )
        {
            String[] tabParserComplexElements = strParserComplexElements.split( SEPARATOR );
            String strTagName;
            String strOpenSubstWithParam;
            String strCloseSubstWithParam;
            String strOpenSubstWithoutParam;
            String strCloseSubstWithoutParam;
            String strInternalSubst;
            boolean bProcessInternalTags;
            boolean bAcceptParam;
            boolean bRequiresQuotedParam;

            for ( int i = 0; i < tabParserComplexElements.length; i++ )
            {
                strTagName = AppPropertiesService.getProperty( PROPERTY_EDITOR_BBCODE_COMPLEX_ELEMENT_PATH + "." +
                        tabParserComplexElements[i] + "." + CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_TAG_NAME );
                strOpenSubstWithParam = AppPropertiesService.getProperty( PROPERTY_EDITOR_BBCODE_COMPLEX_ELEMENT_PATH +
                        "." + tabParserComplexElements[i] + "." +
                        CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_OPEN_SUBST_WITH_PARAM );
                strCloseSubstWithParam = AppPropertiesService.getProperty( PROPERTY_EDITOR_BBCODE_COMPLEX_ELEMENT_PATH +
                        "." + tabParserComplexElements[i] + "." +
                        CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_CLOSE_SUBST_WITH_PARAM );
                strOpenSubstWithoutParam = AppPropertiesService.getProperty( PROPERTY_EDITOR_BBCODE_COMPLEX_ELEMENT_PATH +
                        "." + tabParserComplexElements[i] + "." +
                        CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_OPEN_SUBST_WITHOUT_PARAM );
                strCloseSubstWithoutParam = AppPropertiesService.getProperty( PROPERTY_EDITOR_BBCODE_COMPLEX_ELEMENT_PATH +
                        "." + tabParserComplexElements[i] + "." +
                        CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_CLOSE_SUBST_WITHOUT_PARAM );
                strInternalSubst = AppPropertiesService.getProperty( PROPERTY_EDITOR_BBCODE_COMPLEX_ELEMENT_PATH + "." +
                        tabParserComplexElements[i] + "." + CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_INTERNAL_SUBST );
                bProcessInternalTags = AppPropertiesService.getPropertyBoolean( PROPERTY_EDITOR_BBCODE_COMPLEX_ELEMENT_PATH +
                        "." + tabParserComplexElements[i] + "." +
                        CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_PROCESS_INTERNAL_TAGS, false );
                bAcceptParam = AppPropertiesService.getPropertyBoolean( PROPERTY_EDITOR_BBCODE_COMPLEX_ELEMENT_PATH +
                        "." + tabParserComplexElements[i] + "." + CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_ACCEPT_PARAM,
                        false );
                bRequiresQuotedParam = AppPropertiesService.getPropertyBoolean( PROPERTY_EDITOR_BBCODE_COMPLEX_ELEMENT_PATH +
                        "." + tabParserComplexElements[i] + "." +
                        CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_REQUIRES_QUOTED_PARAM, false );

                _listParserComplexElement.add( new ParserComplexElement( strTagName, strOpenSubstWithParam,
                        strCloseSubstWithParam, strOpenSubstWithoutParam, strCloseSubstWithoutParam, strInternalSubst,
                        bProcessInternalTags, bAcceptParam, bRequiresQuotedParam ) );
            }
        }
    }
}
