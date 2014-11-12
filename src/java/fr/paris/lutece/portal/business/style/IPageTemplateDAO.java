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

import java.util.List;


/**
 *
 * @author LEVY
 */
public interface IPageTemplateDAO
{
    /**
     * Delete a record from the table
     *
     * @param nPageTemplateId The indentifier of the object PageTemplate
     */
    void delete( int nPageTemplateId );

    /**
     * Insert a new record in the table.
     *
     * @param pageTemplate The Instance of the object PageTemplate
     */
    void insert( PageTemplate pageTemplate );

    /**
     * load the data of PageTemplate from the table
     *
     *
     * @param nPageTemplateId The indentifier of the object PageTemplate
     * @return The Instance of the object PageTemplate
     */
    PageTemplate load( int nPageTemplateId );

    /**
     * Returns a list of all the page templates
     *
     * @return A list of PageTemplates objects
     */
    List<PageTemplate> selectPageTemplatesList(  );

    /**
     * Update the record in the table
     *
     * @param pageTemplate The instance of the PageTemplate to update
     */
    void store( PageTemplate pageTemplate );

    /**
     * Checks if a page template has been used by a page
     * @param nPageTemplateId The identifier of the page template
     * @return true if a page template is used by a page, false otherwise
     */
    boolean checkPageTemplateIsUsed( int nPageTemplateId );
}
