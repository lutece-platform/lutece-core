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
package fr.paris.lutece.portal.business.style;

import fr.paris.lutece.portal.business.stylesheet.StyleSheet;
import fr.paris.lutece.portal.service.style.IStyleService;
import jakarta.enterprise.inject.spi.CDI;

import java.util.Collection;

/**
 * This class provides instances management methods (create, find, ...) for Style objects
 */
@Deprecated( since = "8.0", forRemoval = true )
public final class StyleHome
{
    private static IStyleRepository _repository = CDI.current( ).select( IStyleRepository.class ).get( );

    /**
     * Creates a new StyleHome object.
     */
    private StyleHome( )
    {
    }

    /**
     * Returns an instance of a style whose identifier is specified in parameter
     *
     * @param nKey
     *            The primary key of the style to find in the database
     * @deprecated From the core use {@code @Inject} to obtain the {@link IStyleRepository} instance and access the method {@link IStyleRepository#load(Integer)}. 
     *             From a plugin use {@code @Inject} to obtain the {@link IStyleService} instance and access the method {@link IStyleService#findStyleById(int)}. 
     *             This method will be removed in future versions.
     * @return The Style object which corresponds to the key
     */
    @Deprecated( since = "8.0", forRemoval = true )
    public static Style findByPrimaryKey( int nKey )
    {
        return _repository.load( nKey ).orElse( null );
    }

    /**
     * Returns the collection of the Style objects stored in the database
     * 
     * @deprecated From the core use {@code @Inject} to obtain the {@link IStyleRepository} instance and access the method {@link IStyleRepository#findAll()}.
     *             From a plugin use {@code @Inject} to obtain the {@link IStyleService} instance and access the method {@link IStyleService#findStyleById(int)}. 
     *             This method will be removed in future versions.
     * @return A collection of styles
     */
    @Deprecated( since = "8.0", forRemoval = true )
    public static Collection<Style> getStylesList( )
    {
        return _repository.findAll( );
    }

    /**
     * Returns the collection of the StyleSheet objects associated to the Style
     *
     * @param nStyleId
     *            identifier of the style
     * @deprecated From the core use {@code @Inject} to obtain the {@link IStyleRepository} instance and access the method {@link IStyleRepository#findStyleSheetsByStyle(int)}. 
     *             From a plugin use {@code @Inject} to obtain the {@link IStyleService} instance and access the method {@link IStyleService#findStyleSheetsByStyle(int)}. 
     *             This method will be removed in future versions.
     * @return A collection of styles
     */
    @Deprecated( since = "8.0", forRemoval = true )
    public static Collection<StyleSheet> getStyleSheetList( int nStyleId )
    {
        return _repository.findStyleSheetsByStyle( nStyleId );
    }
}
