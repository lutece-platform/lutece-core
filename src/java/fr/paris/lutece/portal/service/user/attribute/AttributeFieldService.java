/*
 * Copyright (c) 2002-2017, Mairie de Paris
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
package fr.paris.lutece.portal.service.user.attribute;

import fr.paris.lutece.portal.business.user.attribute.AttributeField;
import fr.paris.lutece.portal.business.user.attribute.AttributeFieldHome;

/**
 *
 * AttributeFieldService
 *
 */
public final class AttributeFieldService
{
    private static AttributeFieldService _singleton;

    /**
     * Private constructor
     */
    private AttributeFieldService( )
    {
    }

    /**
     * Get an instance of {@link AttributeFieldService}
     * 
     * @return the instance of {@link AttributeFieldService}
     */
    public static synchronized AttributeFieldService getInstance( )
    {
        if ( _singleton == null )
        {
            _singleton = new AttributeFieldService( );
        }

        return _singleton;
    }

    /**
     * Get the attribute field from a given attribute field ID
     * 
     * @param nIdAttributeField
     *            the attribute field ID
     * @return the {@link AttributeField}
     */
    public AttributeField getAttributeField( int nIdAttributeField )
    {
        return AttributeFieldHome.findByPrimaryKey( nIdAttributeField );
    }

    /**
     * Create a new attribute field
     * 
     * @param attributeField
     *            the attribute field
     */
    public void createAttributeField( AttributeField attributeField )
    {
        if ( ( attributeField != null ) && ( attributeField.getAttribute( ) != null ) )
        {
            int nId = AttributeFieldHome.create( attributeField );
            attributeField.setIdField( nId );
        }
    }

    /**
     * Update an attribute field
     * 
     * @param attributeField
     *            the attribute field
     */
    public void updateAttributeField( AttributeField attributeField )
    {
        if ( attributeField != null )
        {
            AttributeFieldHome.update( attributeField );
        }
    }

    /**
     * Remove the attribute field from a given attribute field.
     *
     * @param nIdAttributeField
     *            the n id attribute field
     */
    public void removeAttributeFieldFromIdField( int nIdAttributeField )
    {
        AttributeFieldHome.remove( nIdAttributeField );
    }

    /**
     * Remove the attribute fields from a given id attribute
     * 
     * @param nIdAttribute
     *            the ID attribute
     */
    public void removeAttributeFieldsFromIdAttribute( int nIdAttribute )
    {
        AttributeFieldHome.removeAttributeFieldsFromIdAttribute( nIdAttribute );
    }
}
