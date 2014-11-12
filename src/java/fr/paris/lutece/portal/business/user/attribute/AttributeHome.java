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

import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;
import java.util.Locale;


/**
 *
 * AttributeHome
 *
 */
public final class AttributeHome
{
    private static IAttributeDAO _dao = (IAttributeDAO) SpringContextService.getBean( "attributeDAO" );

    /**
     * Private constructor
     */
    private AttributeHome(  )
    {
    }

    /**
     * Load attribute
     * @param nIdAttribute ID Attribute
     * @param locale Locale
     * @return Attribute
     */
    public static IAttribute findByPrimaryKey( int nIdAttribute, Locale locale )
    {
        return _dao.load( nIdAttribute, locale );
    }

    /**
     * Insert an new attribute
     * @param attribute attribute
     * @return new PK
     */
    public static int create( IAttribute attribute )
    {
        return _dao.insert( attribute );
    }

    /**
     * Update an attribute
     * @param attribute the attribute
     */
    public static void update( IAttribute attribute )
    {
        _dao.store( attribute );
    }

    /**
     * Delete an attribute
     * @param nIdAttribute The id of the attribute
     */
    public static void remove( int nIdAttribute )
    {
        _dao.delete( nIdAttribute );
    }

    /**
     * Load every attributes
     * @param locale locale
     * @return list of attributes
     */
    public static List<IAttribute> findAll( Locale locale )
    {
        return _dao.selectAll( locale );
    }

    /**
     * Load every attributes associated to a plugin
     * @param strPluginName plugin name
     * @param locale locale
     * @return list of attributes
     */
    public static List<IAttribute> findPluginAttributes( String strPluginName, Locale locale )
    {
        return _dao.selectPluginAttributes( strPluginName, locale );
    }

    /**
     * Load every attributes that do not come from a plugin
     * @param locale locale
     * @return list of attributes
     */
    public static List<IAttribute> findCoreAttributes( Locale locale )
    {
        return _dao.selectCoreAttributes( locale );
    }

    /**
     * Update the anonymization status of the attribute.
     * @param nIdAttribute Id of the attribute
     * @param bAnonymize New value of the anonymization status. True means the
     *            attribute should be anonymize, false means it doesn't.
     */
    public static void updateAttributeAnonymization( int nIdAttribute, boolean bAnonymize )
    {
        _dao.updateAttributeAnonymization( nIdAttribute, bAnonymize );
    }
}
