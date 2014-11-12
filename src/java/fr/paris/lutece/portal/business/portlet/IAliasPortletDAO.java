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
package fr.paris.lutece.portal.business.portlet;

import fr.paris.lutece.util.ReferenceList;


/**
 * @author lenaini
 *
 */
public interface IAliasPortletDAO extends IPortletInterfaceDAO
{
    /**
     * Insert a new record in the table. NB : The portlet identifier will already been obtained by the portlet girl
     *
     * @param portlet the portlet to insert in the database
     */
    void insert( Portlet portlet );

    /**
     * Delete a record from the table
     *
     * @param nPortletId the identifier of the portlet to be deleted
     */
    void delete( int nPortletId );

    /**
     * Load the data of a portlet from the database
     *
     * @param nPortletId the portlet identifier
     * @return the object Portlet initialized with the data of the database
     */
    Portlet load( int nPortletId );

    /**
     * Update the record in the table
     *
     * @param portlet the portlet reference
     */
    void store( Portlet portlet );

    /**
     * Select portlets by type
     * @param strPortletTypeId The type Id
     * @return A list of portlets
     */
    ReferenceList selectPortletsByTypeList( String strPortletTypeId );

    /**
     * Select the Alias Id
     * @param nIdPortlet The portlet Id
     * @return  The alias Id
     */
    int selectAliasId( int nIdPortlet );

    /**
     * Select all portlets that accept alias
     * @return A reference list
     */
    ReferenceList selectAcceptAliasPortletList(  );
}
