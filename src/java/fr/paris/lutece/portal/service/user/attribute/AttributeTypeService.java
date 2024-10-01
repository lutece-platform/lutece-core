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
package fr.paris.lutece.portal.service.user.attribute;

import fr.paris.lutece.portal.business.user.attribute.AttributeType;
import fr.paris.lutece.portal.business.user.attribute.IAttribute;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.CDI;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * AttributeTypeService
 *
 */
@ApplicationScoped
public class AttributeTypeService
{
    private static List<AttributeType> _listAttributeTypes;

    AttributeTypeService( )
    {
     // Ctor
    }

    /**
     * Returns the unique instance of the {@link AttributeTypeService} service.
     * 
     * <p>This method is deprecated and is provided for backward compatibility only. 
     * For new code, use dependency injection with {@code @Inject} to obtain the 
     * {@link AttributeTypeService} instance instead.</p>
     * 
     * @return The unique instance of {@link AttributeTypeService}.
     * 
     * @deprecated Use {@code @Inject} to obtain the {@link AttributeTypeService} 
     * instance. This method will be removed in future versions.
     */
    @Deprecated( since = "8.0", forRemoval = true )
    public static AttributeTypeService getInstance( )
    {
        return CDI.current( ).select( AttributeTypeService.class ).get( );
    }

    /**
     * Get the list of attribute types defined the core_context.xml
     * 
     * @param locale
     *            the {@link Locale}
     * @return a list of {@link AttributeType}
     */
    public synchronized List<AttributeType> getAttributeTypes( Locale locale )
    {
        if ( _listAttributeTypes == null )
        {
            List<AttributeType> listAttributTypes = new ArrayList<>( );

            CDI.current().select(IAttribute.class).forEach(attribute -> {
            	attribute.setAttributeType( locale );
                listAttributTypes.add( attribute.getAttributeType( ) );
            });
            _listAttributeTypes = listAttributTypes;
        }
        return _listAttributeTypes;
    }
}
