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


/**
 *
 * IAttributeFieldDAO
 *
 */
public interface IAttributeFieldDAO
{
    /**
     * Load attribute field
     * @param nIdField ID Field
     * @return Attribute Field
     */
    AttributeField load( int nIdField );

    /**
     * Select attribute by id field
     * @param nIdField id field
     * @return user attribute
     */
    IAttribute selectAttributeByIdField( int nIdField );

    /**
     * Load the lists of attribute field associated to an attribute
     * @param nIdAttribute the ID attribute
     * @return the list of attribute fields
     */
    List<AttributeField> selectAttributeFieldsByIdAttribute( int nIdAttribute );

    /**
     * Insert a new attribute field
     * @param attributeField the attribute field
     * @return new PK
     */
    int insert( AttributeField attributeField );

    /**
     * Update an attribute field
     * @param attributeField the attribute field
     */
    void store( AttributeField attributeField );

    /**
     * Delete an attribute field
     * @param nIdField the attribute field id
     */
    void delete( int nIdField );

    /**
     * Delete all attribute field from an attribute id
     * @param nIdAttribute the ID attribute
     */
    void deleteAttributeFieldsFromIdAttribute( int nIdAttribute );
}
