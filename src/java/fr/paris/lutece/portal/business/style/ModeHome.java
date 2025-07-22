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

import java.util.Properties;

import fr.paris.lutece.portal.service.style.IStyleService;
import fr.paris.lutece.util.ReferenceList;
import jakarta.enterprise.inject.spi.CDI;

/**
 * This class provides instances management methods (create, find, ...) for Mode objects
 */
@Deprecated( since = "8.0", forRemoval = true )
public final class ModeHome
{
    private static IModeRepository _repository = CDI.current( ).select( IModeRepository.class ).get( );

    /**
     * Creates a new ModeHome object.
     */
    private ModeHome( )
    {
    }

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of an mode whose identifier is specified in parameter
     *
     * @param nKey
     *            The mode primary key
     * @deprecated From the core use {@code @Inject} to obtain the {@link IModeRepository} instance and access the method {@link IModeRepository#load(int)}. 
     *             From a plugin use {@code @Inject} to obtain the {@link IStyleService} instance and access the method {@link IStyleService#findModeById(int)}. 
     *             This method will be removed in future versions.
     * @return an instance of a mode
     */
    @Deprecated( since = "8.0", forRemoval = true )
    public static Mode findByPrimaryKey( int nKey )
    {
        return _repository.load( nKey );
    }

    /**
     * Returns a reference list which contains all the modes
     *
     * @deprecated From the core use {@code @Inject} to obtain the {@link IModeRepository} instance and access the method {@link IModeRepository#findAllToReferenceList()}. 
     *             From a plugin use {@code @Inject} to obtain the {@link IStyleService} instance and access the method {@link IStyleService#findModesToReferenceList()}. 
     *             This method will be removed in future versions.
     * @return a reference list
     */
    @Deprecated( since = "8.0", forRemoval = true )
    public static ReferenceList getModes( )
    {
        return _repository.findAllToReferenceList( );
    }

    /**
     * Returns a set of properties used for xsl output
     *
     * @deprecated From the core use {@code @Inject} to obtain the {@link IModeRepository} instance and access the method {@link IModeRepository#findOuputXslProperties(int)}. 
     *             From a plugin use {@code @Inject} to obtain the {@link IStyleService} instance and access the method {@link IStyleService#findModeXlsOutputProperties(int)}. 
     *             This method will be removed in future versions.
     * @param nKey
     *            The mode primary key
     * @return the output properties to use for xsl transformation
     */
    @Deprecated( since = "8.0", forRemoval = true )
    public static Properties getOuputXslProperties( int nKey )
    {
        return _repository.findOuputXslProperties( nKey );
    }
}
