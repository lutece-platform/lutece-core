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
package fr.paris.lutece.portal.service.user.attribute;

import fr.paris.lutece.portal.business.user.attribute.AttributeField;
import fr.paris.lutece.portal.business.user.attribute.AttributeFieldHome;
import fr.paris.lutece.portal.business.user.attribute.AttributeHome;
import fr.paris.lutece.portal.business.user.attribute.IAttribute;

import java.util.List;
import java.util.Locale;


/**
 *
 * AttributeService
 *
 */
public final class AttributeService
{
    private static AttributeService _singleton;

    /**
     * Private constructor
     */
    private AttributeService(  )
    {
    }

    /**
     * Get the instance of {@link AttributeService}
     * @return an instance of {@link AttributeService}
     */
    public static synchronized AttributeService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = new AttributeService(  );
        }

        return _singleton;
    }

    /**
     * Get an attribute without its attribute fields
     * @param nIdAttribute the id attribute
     * @param locale {@link Locale}
     * @return a {@link IAttribute}
     */
    public IAttribute getAttributeWithoutFields( int nIdAttribute, Locale locale )
    {
        return AttributeHome.findByPrimaryKey( nIdAttribute, locale );
    }

    /**
     * Get the attribute with its attribute fields
     * @param nIdAttribute the id attribute
     * @param locale the {@link Locale}
     * @return a {@link IAttribute}
     */
    public IAttribute getAttributeWithFields( int nIdAttribute, Locale locale )
    {
        IAttribute attribute = getAttributeWithoutFields( nIdAttribute, locale );
        setAttributeField( attribute );

        return attribute;
    }

    /**
     * Get all user attribute without its attribute fields.
     *
     * @param locale the {@link Locale}
     * @return a list of {@link IAttribute}
     */
    public List<IAttribute> getAllAttributesWithoutFields( Locale locale )
    {
        return AttributeHome.findAll( locale );
    }

    /**
     * Get core user attribute without its attribute fields
     * @param locale the {@link Locale}
     * @return a list of {@link IAttribute}
     */
    public List<IAttribute> getCoreAttributesWithoutFields( Locale locale )
    {
        return AttributeHome.findCoreAttributes( locale );
    }

    /**
     * Get plugin user attribute without its attribute fields
     * @param strPluginName the plugin name
     * @param locale the {@link Locale}
     * @return a list of {@link IAttribute}
     */
    public List<IAttribute> getPluginAttributesWithoutFields( String strPluginName, Locale locale )
    {
        return AttributeHome.findPluginAttributes( strPluginName, locale );
    }

    /**
    * Get all user attributes with its attribute fields
    * @param locale the {@link Locale}
    * @return a list of {@link IAttribute}
    */
    public List<IAttribute> getAllAttributesWithFields( Locale locale )
    {
        List<IAttribute> listAttributes = getAllAttributesWithoutFields( locale );
        setAttributeFields( listAttributes );

        return listAttributes;
    }

    /**
     * Get core user attributes with its attribute fields
     * @param locale the {@link Locale}
     * @return a list of {@link IAttribute}
     */
    public List<IAttribute> getCoreAttributesWithFields( Locale locale )
    {
        List<IAttribute> listAttributes = getCoreAttributesWithoutFields( locale );
        setAttributeFields( listAttributes );

        return listAttributes;
    }

    /**
     * Get plugin user attributes with its attribute fields
     * @param strPluginName the plugin name
     * @param locale the {@link Locale}
     * @return a list of {@link IAttribute}
     */
    public List<IAttribute> getPluginAttributesWithFields( String strPluginName, Locale locale )
    {
        List<IAttribute> listAttributes = getPluginAttributesWithoutFields( strPluginName, locale );
        setAttributeFields( listAttributes );

        return listAttributes;
    }

    /**
         * Set the attribute fields from a given list of {@link IAttribute}
         * @param listAttributes the list of {@link IAttribute}
         */
    public void setAttributeFields( List<IAttribute> listAttributes )
    {
        for ( IAttribute attribute : listAttributes )
        {
            setAttributeField( attribute );
        }
    }

    /**
     * Set the attribute field from a given {@link IAttribute}
     * @param attribute the {@link IAttribute}
     */
    public void setAttributeField( IAttribute attribute )
    {
        if ( attribute != null )
        {
            List<AttributeField> listAttributeFields = AttributeFieldHome.selectAttributeFieldsByIdAttribute( attribute.getIdAttribute(  ) );
            attribute.setListAttributeFields( listAttributeFields );
        }
    }

    /**
     * Create a new attribute and its attribute field.
     *
     * @param attribute the {@link IAttribute} to create
     */
    public void createAttribute( IAttribute attribute )
    {
        if ( attribute != null )
        {
            int nIdAttribute = AttributeHome.create( attribute );
            attribute.setIdAttribute( nIdAttribute );

            if ( attribute.getListAttributeFields(  ) != null )
            {
                for ( AttributeField attributeField : attribute.getListAttributeFields(  ) )
                {
                    attributeField.setAttribute( attribute );
                    AttributeFieldService.getInstance(  ).createAttributeField( attributeField );
                }
            }
        }
    }

    /**
     * Update the attribute
     * @param attribute the {@link IAttribute} to update
     */
    public void updateAttribute( IAttribute attribute )
    {
        if ( attribute != null )
        {
            AttributeHome.update( attribute );

            if ( attribute.getListAttributeFields(  ) != null )
            {
                for ( AttributeField attributeField : attribute.getListAttributeFields(  ) )
                {
                    attributeField.setAttribute( attribute );
                    AttributeFieldService.getInstance(  ).updateAttributeField( attributeField );
                }
            }
        }
    }

    /**
     * Remove the attribute from a given attribute ID
     * @param nIdAttribute the ID attribute
     */
    public void removeAttribute( int nIdAttribute )
    {
        // Remove the AdminUserField associated to the attribute
        AdminUserFieldService.doRemoveUserFieldsByIdAttribute( nIdAttribute );
        // Remove the AttributeField associated to the attribute
        AttributeFieldService.getInstance(  ).removeAttributeFieldsFromIdAttribute( nIdAttribute );
        // Remove the Attribute
        AttributeHome.remove( nIdAttribute );
    }

    /**
     * Update the anonymization status of the attribute.
     * @param nIdAttribute Id of the attribute
     * @param bAnonymize New value of the anonymization status. True means the
     *            attribute should be anonymize, false means it doesn't.
     */
    public void updateAnonymizationStatusUserField( int nIdAttribute, boolean bAnonymize )
    {
        AttributeHome.updateAttributeAnonymization( nIdAttribute, bAnonymize );
    }
}
