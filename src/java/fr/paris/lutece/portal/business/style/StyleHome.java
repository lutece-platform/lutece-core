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
package fr.paris.lutece.portal.business.style;

import fr.paris.lutece.portal.business.stylesheet.StyleSheet;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;


/**
 * This class provides instances management methods (create, find, ...) for Style objects
 */
public final class StyleHome
{
    // Static variable pointed at the DAO instance
    private static IStyleDAO _dao = (IStyleDAO) SpringContextService.getBean( "styleDAO" );

    /**
     * Creates a new StyleHome object.
     */
    private StyleHome(  )
    {
    }

    /**
     * Creation of a Style record in the database
     *
     * @param style An instance of the style which contains the informations to store
     * @return The  instance of the style which has been created.
     */
    public static Style create( Style style )
    {
        _dao.insert( style );

        return style;
    }

    /**
     * Updates the record in the database which corresponds to the Style instance specified in parameter.
     *
     * @param style the instance of the style to update
     */
    public static void update( Style style )
    {
        _dao.store( style );
    }

    /**
     * Deletes the record in the database which corresponds to the Style instance specified in parameter.
     *
     * @param nStyleId The identifier of the style
     */
    public static void remove( int nStyleId )
    {
        _dao.delete( nStyleId );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a style whose identifier is specified in parameter
     *
     * @param nKey The primary key of the style to find in the database
     * @return The Style object which corresponds to the key
     */
    public static Style findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey );
    }

    /**
     * Returns the collection of the Style objects stored in the database
     *
     * @return A collection of styles
     */
    public static Collection<Style> getStylesList(  )
    {
        return _dao.selectStylesList(  );
    }

    /**
     * Returns the collection of the StyleSheet objects associated to the Style
     *
     * @param nStyleId identifier of the style
     * @return A collection of styles
     */
    public static Collection<StyleSheet> getStyleSheetList( int nStyleId )
    {
        return _dao.selectStyleSheetList( nStyleId );
    }

    /**
     * Returns a reference list which contains all the Portal Components
     *
     * @return a reference list
     */
    public static ReferenceList getPortalComponentList(  )
    {
        return _dao.selectPortalComponentList(  );
    }

    /**
     * Checks if a style has been created in the database with the given portal componenet
     *
     * @param nPortalComponentId The identifier of the portal component
     * @return true if a style has been created for this portal component, false otherwise
     */
    public static boolean checkStylePortalComponent( int nPortalComponentId )
    {
        return _dao.checkStylePortalComponent( nPortalComponentId );
    }
}
