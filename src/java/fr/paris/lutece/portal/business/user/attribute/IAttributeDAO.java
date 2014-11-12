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
package fr.paris.lutece.portal.business.user.attribute;

import java.util.List;
import java.util.Locale;


/**
 *
 * IAttributeDAO
 *
 */
public interface IAttributeDAO
{
    /**
     * Load attribute
     * @param nIdAttribute ID
     * @param locale Locale
     * @return Attribute
     */
    IAttribute load( int nIdAttribute, Locale locale );

    /**
     * Insert a new attribute
     * @param attribute the attribute
     * @return new PK
     */
    int insert( IAttribute attribute );

    /**
     * Update an attribute
     * @param attribute the attribute
     */
    void store( IAttribute attribute );

    /**
     * Delete an attribute
     * @param nIdAttribute the ID of the attribute
     */
    void delete( int nIdAttribute );

    /**
     * Load every attributes
     * @param locale locale
     * @return list of attributes
     */
    List<IAttribute> selectAll( Locale locale );

    /**
     * Load every attributes from plugin name
     * @param strPluginName plugin name
     * @param locale locale
     * @return list of attributes
     */
    List<IAttribute> selectPluginAttributes( String strPluginName, Locale locale );

    /**
     * Load every attributes that do not come from a plugin
     * @param locale locale
     * @return list of attributes
     */
    List<IAttribute> selectCoreAttributes( Locale locale );

    /**
     * Update the anonymization status of the attribute.
     * @param nIdAttribute Id of the attribute
     * @param bAnonymize New value of the anonymization status. True means the
     *            attribute should be anonymize, false means it doesn't.
     */
    void updateAttributeAnonymization( int nIdAttribute, boolean bAnonymize );
}
