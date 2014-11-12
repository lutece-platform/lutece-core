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
package fr.paris.lutece.util.parser;

import fr.paris.lutece.portal.business.editor.ParserComplexElement;
import fr.paris.lutece.portal.business.editor.ParserElement;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 *  This class provides parser utils  for BBCODE parsing
 *
 */
public final class BbcodeUtil
{
    private static final String CR_LF = "(?:\r\n|\r|\n)?";

    /**
     * Instantiates a new bbcode util.
     */
    private BbcodeUtil(  )
    {
    }

    /**
     * Parse BBCODE text and return HTML text
     * @param value the value of the text
     * @param listParserElement the list of simple parser element using for parsing
     * @param listParserComplexElement the list of complex  parser element using for parsing
     * @return HTML Text
     */
    public static String parse( String value, List<ParserElement> listParserElement,
        List<ParserComplexElement> listParserComplexElement )
    {
        StringBuffer buffer = new StringBuffer( value );

        // complex Element
        if ( listParserComplexElement != null )
        {
            for ( ParserComplexElement element : listParserComplexElement )
            {
                processNestedTags( buffer, element.getTagName(  ), element.getOpenSubstWithParam(  ),
                    element.getCloseSubstWithParam(  ), element.getOpenSubstWithoutParam(  ),
                    element.getCloseSubstWithoutParam(  ), element.getInternalSubst(  ),
                    element.isProcessInternalTags(  ), element.isAcceptParam(  ), element.isRequiresQuotedParam(  ) );
            }
        }

        String str = buffer.toString(  );

        // SimpleElement
        if ( listParserElement != null )
        {
            for ( ParserElement element : listParserElement )
            {
                str = str.replaceAll( element.getCode(  ), element.getValue(  ) );
            }
        }

        return str;
    }

    /**
     *
     * Method using for parsing complex element
     * @param buffer value to parse
     * @param tagName tagName
     * @param openSubstWithParam openSubstWithParam
     * @param closeSubstWithParam closeSubstWithParam
     * @param openSubstWithoutParam openSubstWithoutParam
     * @param closeSubstWithoutParam closeSubstWithoutParam
     * @param internalSubst internalSubst
     * @param processInternalTags processInternalTags
     * @param acceptParam acceptParam
     * @param requiresQuotedParam requiresQuotedParam
     */
    private static void processNestedTags( StringBuffer buffer, String tagName, String openSubstWithParam,
        String closeSubstWithParam, String openSubstWithoutParam, String closeSubstWithoutParam, String internalSubst,
        boolean processInternalTags, boolean acceptParam, boolean requiresQuotedParam )
    {
        String str = buffer.toString(  );

        Stack openStack = new Stack(  );
        Set subsOpen = new HashSet(  );
        Set subsClose = new HashSet(  );
        Set subsInternal = new HashSet(  );

        String openTag = CR_LF + "\\[" + tagName +
            ( acceptParam ? ( requiresQuotedParam ? "(?:=\"(.*?)\")?" : "(?:=\"?(.*?)\"?)?" ) : "" ) + "\\]" + CR_LF;
        String closeTag = CR_LF + "\\[/" + tagName + "\\]" + CR_LF;
        String internTag = CR_LF + "\\[\\*\\]" + CR_LF;

        String patternString = "(" + openTag + ")|(" + closeTag + ")";

        if ( processInternalTags )
        {
            patternString += ( "|(" + internTag + ")" );
        }

        Pattern tagsPattern = Pattern.compile( patternString );
        Matcher matcher = tagsPattern.matcher( str );

        int openTagGroup;
        int paramGroup;
        int closeTagGroup;
        int internalTagGroup;

        if ( acceptParam )
        {
            openTagGroup = 1;
            paramGroup = 2;
            closeTagGroup = 3;
            internalTagGroup = 4;
        }
        else
        {
            openTagGroup = 1;
            paramGroup = -1; // INFO
            closeTagGroup = 2;
            internalTagGroup = 3;
        }

        while ( matcher.find(  ) )
        {
            int length = matcher.end(  ) - matcher.start(  );
            MutableCharSequence matchedSeq = new MutableCharSequence( str, matcher.start(  ), length );

            // test opening tags
            if ( matcher.group( openTagGroup ) != null )
            {
                if ( acceptParam && ( matcher.group( paramGroup ) != null ) )
                {
                    matchedSeq._strParam = matcher.group( paramGroup );
                }

                openStack.push( matchedSeq );

                // test closing tags
            }
            else if ( ( matcher.group( closeTagGroup ) != null ) && !openStack.isEmpty(  ) )
            {
                MutableCharSequence openSeq = (MutableCharSequence) openStack.pop(  );

                if ( acceptParam )
                {
                    matchedSeq._strParam = openSeq._strParam;
                }

                subsOpen.add( openSeq );
                subsClose.add( matchedSeq );

                // test internal tags
            }
            else if ( processInternalTags && ( matcher.group( internalTagGroup ) != null ) && ( !openStack.isEmpty(  ) ) )
            {
                subsInternal.add( matchedSeq );
            }
        }

        LinkedList subst = new LinkedList(  );
        subst.addAll( subsOpen );
        subst.addAll( subsClose );
        subst.addAll( subsInternal );

        Collections.sort( subst,
            new Comparator(  )
            {
                @Override
                public int compare( Object o1, Object o2 )
                {
                    MutableCharSequence s1 = (MutableCharSequence) o1;
                    MutableCharSequence s2 = (MutableCharSequence) o2;

                    return -( s1._nStart - s2._nStart );
                }
            } );

        buffer.delete( 0, buffer.length(  ) );

        int start = 0;

        while ( !subst.isEmpty(  ) )
        {
            MutableCharSequence seq = (MutableCharSequence) subst.removeLast(  );
            buffer.append( str.substring( start, seq._nStart ) );

            if ( subsClose.contains( seq ) )
            {
                if ( seq._strParam != null )
                {
                    buffer.append( closeSubstWithParam );
                }
                else
                {
                    buffer.append( closeSubstWithoutParam );
                }
            }
            else if ( subsInternal.contains( seq ) )
            {
                buffer.append( internalSubst );
            }
            else if ( subsOpen.contains( seq ) )
            {
                Matcher m = Pattern.compile( openTag ).matcher( str.substring( seq._nStart, seq._nStart + seq._bLength ) );

                if ( m.matches(  ) )
                {
                    if ( acceptParam && ( seq._strParam != null ) )
                    {
                        buffer.append(  //
                            openSubstWithParam.replaceAll( "\\{BBCODE_PARAM\\}", seq._strParam ) );
                    }
                    else
                    {
                        buffer.append( openSubstWithoutParam );
                    }
                }
            }

            start = seq._nStart + seq._bLength;
        }

        buffer.append( str.substring( start ) );
    }

    /**
     *
     * Inner class MutableCharSequence
     *
     */
    static class MutableCharSequence implements CharSequence
    {
        /** _cBase*/
        private CharSequence _cBase;

        /** _nStart */
        private int _nStart;

        /** _bLength*/
        private int _bLength;

        /** */
        private String _strParam;

        /**
         * MutableCharSequence
         */
        public MutableCharSequence(  )
        {
        }

        /**
         * @param base base
         * @param start start
         * @param length length
         */
        public MutableCharSequence( CharSequence base, int start, int length )
        {
            reset( base, start, length );
        }

        /**
         * @return @see java.lang.CharSequence#length()
         */
        @Override
        public int length(  )
        {
            return _bLength;
        }

        /**
         * @param index index
         * @return @see java.lang.CharSequence#charAt(int)
         */
        @Override
        public char charAt( int index )
        {
            return _cBase.charAt( _nStart + index );
        }

        /**
         * @param pStart pStart
         * @param end end
         * @return @see java.lang.CharSequence#subSequence(int, int)
         */
        @Override
        public CharSequence subSequence( int pStart, int end )
        {
            return new MutableCharSequence( _cBase, _nStart + pStart, _nStart + ( end - pStart ) );
        }

        /**
         * return CharSequence
         * @param pBase pBase
         * @param pStart pStart
         * @param pLength pLength
         * @return  CharSequence
         */
        public CharSequence reset( CharSequence pBase, int pStart, int pLength )
        {
            _cBase = pBase;
            _nStart = pStart;
            _bLength = pLength;

            return this;
        }

        /**
         *
         * @return @see java.lang.Object#toString()
         */
        @Override
        public String toString(  )
        {
            StringBuffer sb = new StringBuffer(  );

            for ( int i = _nStart; i < ( _nStart + _bLength ); i++ )
            {
                sb.append( _cBase.charAt( i ) );
            }

            return sb.toString(  );
        }
    }
}
