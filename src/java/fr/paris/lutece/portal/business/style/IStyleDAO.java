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
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;


/**
 *
 * @author LEVY
 */
public interface IStyleDAO
{
    /**
     * Checks if a style has been created in the database with the given portal componenet
     *
     * @param nPortalComponentId The identifier of the portal component
     * @return true if a style has been created for this portal component, false otherwise
     */
    boolean checkStylePortalComponent( int nPortalComponentId );

    /**
     * Delete a record from the table
     *
     * @param nStyleId the identifier of the style to delete
     */
    void delete( int nStyleId );

    /**
     * Insert a new record in the table.
     *
     * @param style The Instance of the object Style
     */
    void insert( Style style );

    /**
     * load the data of the Style whose identifier is specified in parameter from the table
     *
     * @param nStyleId The identifier of the Style
     * @return an instance of the Style which has been created
     */
    Style load( int nStyleId );

    /**
     * Returns the list of the portal component in form of a ReferenceList
     *
     * @return the list of the portal component
     */
    ReferenceList selectPortalComponentList(  );

    /**
     * load the data of the StyleSheet which re associated to the given style
     *
     *
     * @param nStyleId The identifier of the Style
     * @return an instance of the Style which has been created
     */
    Collection<StyleSheet> selectStyleSheetList( int nStyleId );

    /**
     * Load the list of styles stored in the database
     *
     * @return The styles list in form of a Collection object
     */
    Collection<Style> selectStylesList(  );

    /**
     * Update the record in the table
     *
     * @param style The instance of the Style to update
     */
    void store( Style style );
}
