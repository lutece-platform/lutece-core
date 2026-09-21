/*
 * Copyright (c) 2002-2026, City of Paris
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
package fr.paris.lutece.portal.service.template;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;

/**
 * Plain helper functions used by {@code FreeMarkerAutoEscapeCompatibilityTest} (LUT-33153). No {@code @Test}, no
 * assertions — just the bits any module's own test class can call directly instead of copy-pasting them. Ships in
 * lutece-core's test-jar.
 *
 * <h2>Usage from another module's pom.xml</h2>
 *
 * <pre>{@code
 * <dependency>
 *     <groupId>fr.paris.lutece</groupId>
 *     <artifactId>lutece-core</artifactId>
 *     <version>[8.0.2-SNAPSHOT,)</version>
 *     <type>test-jar</type>
 *     <scope>test</scope>
 * </dependency>
 * }</pre>
 */
public final class FreeMarkerAutoEscapeTestUtils
{
    private FreeMarkerAutoEscapeTestUtils( )
    {
    }

    /** Same FreeMarker engine setup as AbstractFreeMarkerTemplateService.buildConfiguration(). */
    public static Configuration newConfiguration( boolean bAutoEscape )
    {
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_31 );
        cfg.setLocalizedLookup( false );

        if ( bAutoEscape )
        {
            cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
            cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        }

        return cfg;
    }

    /** All .ftl/.html files under strTemplatesRoot (relative to the caller's working directory). */
    public static List<Path> listPluginTemplates( String strTemplatesRoot ) throws IOException
    {
        Path root = Paths.get( strTemplatesRoot );

        if ( !Files.isDirectory( root ) )
        {
            root = Paths.get( "." + File.separator + strTemplatesRoot );
        }

        try ( Stream<Path> stream = Files.walk( root ) )
        {
            return stream.filter( Files::isRegularFile ).filter( p -> {
                String strName = p.getFileName( ).toString( );
                return strName.endsWith( ".ftl" ) || strName.endsWith( ".html" );
            } ).collect( Collectors.toList( ) );
        }
    }

    /**
     * Character ranges covered by a FreeMarker construct: a {@code <#...>} directive, a {@code <@...>} macro call,
     * or a {@code ${...}} interpolation. Quotes and nested braces inside a tag are honoured so an HTML attribute
     * quote doesn't close the span early.
     */
    public static List<int [ ]> freeMarkerSpans( String strSource )
    {
        List<int [ ]> listSpans = new ArrayList<>( );
        int nLength = strSource.length( );
        int i = 0;

        while ( i < nLength )
        {
            char c = strSource.charAt( i );

            if ( c == '<' && i + 1 < nLength && ( strSource.charAt( i + 1 ) == '#' || strSource.charAt( i + 1 ) == '@' ) )
            {
                int j = i + 2;
                char cQuote = 0;
                int nDepth = 0;

                while ( j < nLength )
                {
                    char d = strSource.charAt( j );

                    if ( cQuote != 0 )
                    {
                        if ( d == cQuote )
                        {
                            cQuote = 0;
                        }
                    }
                    else
                        if ( d == '\'' || d == '"' )
                        {
                            cQuote = d;
                        }
                        else
                            if ( d == '{' )
                            {
                                nDepth++;
                            }
                            else
                                if ( d == '}' )
                                {
                                    nDepth--;
                                }
                                else
                                    if ( d == '>' && nDepth <= 0 )
                                    {
                                        break;
                                    }

                    j++;
                }

                listSpans.add( new int [ ] {
                        i, Math.min( j, nLength - 1 )
                } );
                i = j + 1;
            }
            else
                if ( c == '$' && i + 1 < nLength && strSource.charAt( i + 1 ) == '{' )
                {
                    int j = strSource.indexOf( '}', i + 2 );
                    int nEnd = j < 0 ? nLength - 1 : j;
                    listSpans.add( new int [ ] {
                            i, nEnd
                    } );
                    i = nEnd + 1;
                }
                else
                {
                    i++;
                }
        }

        return listSpans;
    }

    public static boolean isInsideFreeMarker( int nPosition, List<int [ ]> listSpans )
    {
        for ( int [ ] span : listSpans )
        {
            if ( span [0] <= nPosition && nPosition <= span [1] )
            {
                return true;
            }

            if ( span [0] > nPosition )
            {
                break;
            }
        }

        return false;
    }

    public static int lineOf( String strSource, int nPosition )
    {
        int nLine = 1;

        for ( int i = 0; i < nPosition; i++ )
        {
            if ( strSource.charAt( i ) == '\n' )
            {
                nLine++;
            }
        }

        return nLine;
    }

    public static String firstLine( String strMessage )
    {
        if ( strMessage == null )
        {
            return "(no message)";
        }

        String strFirst = strMessage.split( "\n" ) [0].trim( );

        return strFirst.length( ) > 160 ? strFirst.substring( 0, 160 ) + "..." : strFirst;
    }
}
