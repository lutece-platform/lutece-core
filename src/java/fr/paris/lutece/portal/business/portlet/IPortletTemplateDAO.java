/*
 * Copyright (c) 2002-2022, City of Paris
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

import java.util.List;

/**
 * Data access interface of the portlet templates
 */
public interface IPortletTemplateDAO
{
    /**
     * Inserts a template and sets its generated identifier
     *
     * @param template
     *            the template
     */
    void insert( PortletTemplate template );

    /**
     * Updates a template
     *
     * @param template
     *            the template
     */
    void store( PortletTemplate template );

    /**
     * Deletes a template
     *
     * @param nIdTemplate
     *            the template identifier
     */
    void delete( int nIdTemplate );

    /**
     * Loads a template
     *
     * @param nIdTemplate
     *            the template identifier
     * @return the template, null when it does not exist
     */
    PortletTemplate load( int nIdTemplate );

    /**
     * Loads every template
     *
     * @return the templates, ordered by portlet type then identifier
     */
    List<PortletTemplate> selectAll( );

    /**
     * Loads the templates registered for a portlet type
     *
     * @param strPortletTypeId
     *            the portlet type identifier
     * @return the templates, ordered by identifier
     */
    List<PortletTemplate> selectByPortletType( String strPortletTypeId );

    /**
     * Tells whether at least one portlet uses a template
     *
     * @param nIdTemplate
     *            the template identifier
     * @return true when a portlet uses the template
     */
    boolean checkTemplateIsUsed( int nIdTemplate );
}
