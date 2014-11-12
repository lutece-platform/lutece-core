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
package fr.paris.lutece.portal.business.xsl;

import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.List;


/**
 *
 * IXslExportDAO
 *
 */
public interface IXslExportDAO
{
    /**
     * Generates a new primary key
     *
     * @return The new primary key
     */
    int newPrimaryKey(  );

    /**
     * Insert a new record in the table.
     *
     * @param xslExport instance of the XslExport object to insert
     */
    void insert( XslExport xslExport );

    /**
     * Load the data of the XslExport from the table
     *
     * @param nId The identifier of the xsl
     * @return the instance of the XslExport
     */
    XslExport load( int nId );

    /**
     * Delete a record from the table
     *
     * @param nIdXslExport The identifier of the directory xsl
     */
    void delete( int nIdXslExport );

    /**
     * Update the xslExport in the table
     *
     * @param xslExport instance of the XslExport object to update
     */
    void store( XslExport xslExport );

    /**
     * Get the list of Xsl Export.
     * @return The list of all Xsl Export.
     */
    List<XslExport> selectList(  );

    /**
     * Get the list of Xsl Export associated to a specified plugin.
     * @param plugin The plugin
     * @return The list of Xsl Export associated with the given plugin.
     */
    List<XslExport> selectListByPlugin( Plugin plugin );
}
