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
package fr.paris.lutece.portal.business.stylesheet;

import java.util.Collection;

import jakarta.enterprise.inject.spi.CDI;

/**
 * This class provides instances management methods (create, find, ...) for Stylesheet objects
 */
@Deprecated( since = "8.0", forRemoval = true )
public final class StyleSheetHome
{
    private static IStyleSheetRepository _repository = CDI.current( ).select( IStyleSheetRepository.class ).get( );

    /**
     * Creates a new StyleSheetHome object.
     */
    private StyleSheetHome( )
    {
    }

    /**
     * Returns an instance of a stylesheet file whose identifier is specified in parameter
     *
     * @param nKey
     *            the stylesheet primary key
     * @deprecated Use {@code @Inject} to obtain the {@link IStyleSheetRepository} instance and access the method {@link IStyleSheetRepository#load(Integer)}.
     *             This method will be removed in future versions.
     * @return the instance of the styleSheet whose identifier is the nKey
     */
    @Deprecated( since = "8.0", forRemoval = true )
    public static StyleSheet findByPrimaryKey( int nKey )
    {
        return _repository.load( nKey ).orElse( null );
    }

    /**
     * Returns a collection of StyleSheet objet
     *
     * @param nModeId
     *            The mode identifier
     * @deprecated Use {@code @Inject} to obtain the {@link IStyleSheetRepository} instance and access the method {@link IStyleSheetRepository#findByMode(int)}.
     *             This method will be removed in future versions.
     * @return A collection of StyleSheet object
     */
    @Deprecated( since = "8.0", forRemoval = true )
    public static Collection<StyleSheet> getStyleSheetList( int nModeId )
    {
        return _repository.findByMode( nModeId );
    }
}
