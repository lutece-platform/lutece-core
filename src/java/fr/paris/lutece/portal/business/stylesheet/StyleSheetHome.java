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
package fr.paris.lutece.portal.business.stylesheet;

import fr.paris.lutece.portal.service.html.XmlTransformerService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.Collection;


/**
 * This class provides instances management methods (create, find, ...) for Stylesheet objects
 */
public final class StyleSheetHome
{
    // Static variable pointed to the DAO instance
    private static IStyleSheetDAO _dao = (IStyleSheetDAO) SpringContextService.getBean( "styleSheetDAO" );

    /**
     * Creates a new StyleSheetHome object.
     */
    private StyleSheetHome(  )
    {
    }

    /**
     * Creation of an instance of a Stylesheet file in the database
     *
     * @param stylesheet An instance of a stylesheet which contains the informations to store
     * @return The instance of the stylesheet which has been created.
     */
    public static StyleSheet create( StyleSheet stylesheet )
    {
        _dao.insert( stylesheet );

        return stylesheet;
    }

    /**
     * Deletes the StylesSheet whose identifier is specified in parameter
     *
     * @param nId the identifier of the stylesheet to delete
     */
    public static void remove( int nId )
    {
        _dao.delete( nId );
        XmlTransformerService.clearXslCache(  );
    }

    /**
     * Update the StylesSheet whose identifier is specified in parameter
     *
     * @param stylesheet the stylesheet to update
     */
    public static void update( StyleSheet stylesheet )
    {
        _dao.store( stylesheet );
        XmlTransformerService.clearXslCache(  );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a stylesheet file whose identifier is specified in parameter
     *
     * @param nKey the stylesheet primary key
     * @return the instance of the styleSheet whose identifier is the nKey
     */
    public static StyleSheet findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey );
    }

    /**
     * Returns the number of stylesheets associated to the style and the mode specified in parameter
     *
     * @param nStyleId the style id
     * @param nModeId the mode id
     * @return the number of stylesheet associated
     */
    public static int getStyleSheetNbPerStyleMode( int nStyleId, int nModeId )
    {
        return _dao.selectStyleSheetNbPerStyleMode( nStyleId, nModeId );
    }

    /**
     * Returns a collection of StyleSheet objet
     *
     * @param nModeId The mode identifier
     * @return A collection of StyleSheet object
     */
    public static Collection<StyleSheet> getStyleSheetList( int nModeId )
    {
        return _dao.selectStyleSheetList( nModeId );
    }
}
