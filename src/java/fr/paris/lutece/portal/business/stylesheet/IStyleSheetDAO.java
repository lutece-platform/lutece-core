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

import java.util.Collection;


/**
 *
 * @author LEVY
 */
public interface IStyleSheetDAO
{
    /**
     * Delete the StyleSheet from the database whose identifier is specified
     * in parameter
     *
     * @param nIdStylesheet the identifier of the StyleSheet to delete
     */
    void delete( int nIdStylesheet );

    /**
     * Insert a new record in the table.
     *
     * @param stylesheet The StyleSheet object
     */
    void insert( StyleSheet stylesheet );

    /**
     * Load the data of Stylesheet from the table
     *
     * @param nIdStylesheet the identifier of the Stylesheet to load
     * @return stylesheet
     */
    StyleSheet load( int nIdStylesheet );

    /**
     * Returns the identifier of the mode of the stylesheet whose identifier
     * is specified in parameter
     *
     * @param nIdStylesheet the identifier of the stylesheet
     * @return the identifier of the mode
     */
    int selectModeId( int nIdStylesheet );

    /**
     * Load the list of stylesheet
     *
     * @param nModeId The Mode identifier
     * @return the list of the StyleSheet in form of a collection of StyleSheet objects
     */
    Collection<StyleSheet> selectStyleSheetList( int nModeId );

    /**
     * Returns the number of stylesheets associated to the style and the mode
     * specified in parameter
     *
     * @param nStyleId the style id
     * @param nModeId the mode id
     * @return the number of stylesheet associated
     */
    int selectStyleSheetNbPerStyleMode( int nStyleId, int nModeId );

    /**
     * Update the record in the table
     *
     * @param stylesheet The stylesheet to store
     */
    void store( StyleSheet stylesheet );
}
