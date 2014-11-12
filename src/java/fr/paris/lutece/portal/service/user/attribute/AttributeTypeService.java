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

import fr.paris.lutece.portal.business.user.attribute.AttributeType;
import fr.paris.lutece.portal.business.user.attribute.IAttribute;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 *
 * AttributeTypeService
 *
 */
public final class AttributeTypeService
{
    private static volatile AttributeTypeService _singleton;
    private static volatile List<AttributeType> _listAttributeTypes;

    /**
     * Private constructor
     */
    private AttributeTypeService(  )
    {
    }

    /**
     * Get an instance of {@link AttributeTypeService}
     * @return the instance of {@link AttributeTypeService}
     */
    public static synchronized AttributeTypeService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = new AttributeTypeService(  );
        }

        return _singleton;
    }

    /**
     * Get the list of attribute types defined the core_context.xml
     * @param locale the {@link Locale}
     * @return a list of {@link AttributeType}
     */
    public List<AttributeType> getAttributeTypes( Locale locale )
    {
        if ( _listAttributeTypes == null )
        {
            synchronized ( this )
            {
                if ( _listAttributeTypes == null )
                {
                    List<AttributeType> listAttributTypes = new ArrayList<AttributeType>(  );

                    for ( IAttribute attribute : SpringContextService.getBeansOfType( IAttribute.class ) )
                    {
                        attribute.setAttributeType( locale );
                        listAttributTypes.add( attribute.getAttributeType(  ) );
                    }

                    _listAttributeTypes = listAttributTypes;
                }
            }
        }

        return _listAttributeTypes;
    }
}
