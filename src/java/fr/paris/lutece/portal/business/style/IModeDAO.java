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

import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;


/**
 *
 * @author LEVY
 */
public interface IModeDAO
{
    /**
     * Delete a record from the table
     *
     * @param nModeId The indentifier of the object Mode
     */
    void delete( int nModeId );

    /**
     * Returns the list of the modes in form of a reference list
     *
     *
     * @return the modes list in form of a ReferenceList object
     */
    ReferenceList getModesList(  );

    /**
     * Insert a new record in the table.
     *
     * @param mode The mode object
     */
    void insert( Mode mode );

    /**
     * load the data of Level from the table
     *
     *
     * @param nIdMode The indentifier of the object Mode
     * @return The Instance of the object Mode
     */
    Mode load( int nIdMode );

    /**
     * Returns a list of all the modes
     *
     * @return A collection of modes objects
     */
    Collection<Mode> selectModesList(  );

    /**
     * Update the record in the table
     *
     * @param mode The instance of the Mode to update
     */
    void store( Mode mode );
}
