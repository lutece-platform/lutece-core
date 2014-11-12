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

import java.util.List;
import java.util.Locale;


/**
 * IPortletTypeDAO Interface
 *
 */
public interface IPortletTypeDAO
{
    /**
     * Insert a new record in the table PortletType
     *
     * @param portletType The portlet Type object
     */
    void insert( PortletType portletType );

    /**
     * Load the data of PortletType  from the table
     *
     * @param strPortletTypeId The identifier of PortletType
     * @return The instance of the PortletType
     */
    PortletType load( String strPortletTypeId );

    /**
     * Delete a record from the table
     *
     * @param strPortletTypeId The POrtletTYpe identifier
     */
    void delete( String strPortletTypeId );

    /**
     * Returns the portlet type identifier
     *
     * @param strPluginHomeClass the name of the portlet type
     * @return the identifier of the portlet type
     */
    String selectPortletTypeId( String strPluginHomeClass );

    /**
     * Returns the portlet count for a given provider
     *
     * @param strPortletTypeId The provider's identifier
     * @return nCount
     */
    int selectNbPortletTypeByPortlet( String strPortletTypeId );

    /**
     * Return a Reference List of portletType
     * @param locale The locale
     * @return list The reference List
     */
    ReferenceList selectPortletsTypesList( Locale locale );

    /**
     * Returns the list of the portlet types
     *
     * @return the list of the portlet types
     */
    List<PortletType> selectPortletTypesList(  );
}
