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


/**
 *
 * AttributeFieldHome
 *
 */
public final class AttributeFieldHome
{
    private static IAttributeFieldDAO _dao = (IAttributeFieldDAO) SpringContextService.getBean( "attributeFieldDAO" );

    /**
     * Private constructor
     */
    private AttributeFieldHome(  )
    {
    }

    /**
     * Load attribute field
     * @param nIdField ID Field
     * @return Attribute Field
     */
    public static AttributeField findByPrimaryKey( int nIdField )
    {
        return _dao.load( nIdField );
    }

    /**
     * Load the lists of attribute field associated to an attribute
     * @param nIdAttribute the ID attribute
     * @return the list of attribute fields
     */
    public static List<AttributeField> selectAttributeFieldsByIdAttribute( int nIdAttribute )
    {
        return _dao.selectAttributeFieldsByIdAttribute( nIdAttribute );
    }

    /**
     * Load the attribute associated to the id field
     * @param nIdField the id field
     * @return attribute
     */
    public static IAttribute selectAttributeByIdField( int nIdField )
    {
        return _dao.selectAttributeByIdField( nIdField );
    }

    /**
     * Insert an new attribute field
     * @param attributeField the attribute field
     * @return @return new PK
     */
    public static int create( AttributeField attributeField )
    {
        return _dao.insert( attributeField );
    }

    /**
     * Update an attribute field
     * @param attributeField the attribute field
     */
    public static void update( AttributeField attributeField )
    {
        _dao.store( attributeField );
    }

    /**
     * Delete an attribute field
     * @param nIdField the attribute field id
     */
    public static void remove( int nIdField )
    {
        _dao.delete( nIdField );
    }

    /**
     * Delete all attribute field from an attribute id
     * @param nIdAttribute the ID attribute
     */
    public static void removeAttributeFieldsFromIdAttribute( int nIdAttribute )
    {
        _dao.deleteAttributeFieldsFromIdAttribute( nIdAttribute );
    }
}
