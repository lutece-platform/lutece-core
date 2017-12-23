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
package fr.paris.lutece.portal.web.user.attribute;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.user.attribute.AttributeField;
import fr.paris.lutece.portal.business.user.attribute.AttributeType;
import fr.paris.lutece.portal.business.user.attribute.IAttribute;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.user.attribute.AttributeService;
import fr.paris.lutece.portal.service.user.attribute.AttributeTypeService;
import fr.paris.lutece.test.LuteceTestCase;

public class AttributeFieldJspBeanTest extends LuteceTestCase
{
    private AttributeFieldJspBean instance;
    private Map<AttributeType, IAttribute> _attributes;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        instance = new AttributeFieldJspBean( );
        _attributes = new HashMap<>( );
        List<AttributeType> types = AttributeTypeService.getInstance( ).getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            IAttribute attribute = ( IAttribute ) Class.forName( type.getClassName( ) ).newInstance( );
            attribute.setTitle( getRandomName( ) );
            attribute.setHelpMessage( attribute.getTitle( ) );
            List<AttributeField> listAttributeFields = new ArrayList<AttributeField>( );
            AttributeField attributeField = new AttributeField( );
            attributeField.setValue( attribute.getTitle( ) );
            listAttributeFields.add( attributeField );
            attribute.setListAttributeFields( listAttributeFields );
            AttributeService.getInstance( ).createAttribute( attribute );
            _attributes.put( type, attribute );
        }
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        for ( IAttribute attribute : _attributes.values( ) )
        {
            AttributeService.getInstance( ).removeAttribute( attribute.getIdAttribute( ) );
        }
        super.tearDown( );
    }

    private String getRandomName( )
    {
        Random rand = new SecureRandom( );
        BigInteger bigInt = new BigInteger( 128, rand );
        return "junit" + bigInt.toString( 36 );
    }

    public void testDoConfirmRemoveAttributeField( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "id_attribute", "1" );
        request.addParameter( "id_field", "1" );

        instance.doConfirmRemoveAttributeField( request );

        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertTrue( message.getRequestParameters( ).containsKey( SecurityTokenService.PARAMETER_TOKEN ) );
    }

    public void testDoRemoveAttributeField( ) throws AccessDeniedException
    {
        List<AttributeType> types = AttributeTypeService.getInstance( ).getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            testDoRemoveAttributeField( _attributes.get( type ) );
        }
    }

    private void testDoRemoveAttributeField( IAttribute attribute ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "id_attribute", Integer.toString( attribute.getIdAttribute( ) ) );
        request.addParameter( "id_field",
                Integer.toString( attribute.getListAttributeFields( ).get( 0 ).getIdField( ) ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( )
                .getToken( request, "jsp/admin/user/attribute/DoRemoveAttributeField.jsp" ) );

        instance.doRemoveAttributeField( request );

        IAttribute stored = AttributeService.getInstance( ).getAttributeWithFields( attribute.getIdAttribute( ),
                Locale.FRANCE );
        assertNotNull( stored );
        assertEquals( 0, stored.getListAttributeFields( ).size( ) );
    }

    public void testDoRemoveAttributeFieldInvalidToken( ) throws AccessDeniedException
    {
        List<AttributeType> types = AttributeTypeService.getInstance( ).getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            testDoRemoveAttributeFieldInvalidToken( _attributes.get( type ) );
        }
    }

    private void testDoRemoveAttributeFieldInvalidToken( IAttribute attribute ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "id_attribute", Integer.toString( attribute.getIdAttribute( ) ) );
        request.addParameter( "id_field",
                Integer.toString( attribute.getListAttributeFields( ).get( 0 ).getIdField( ) ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( )
                .getToken( request, "jsp/admin/user/attribute/DoRemoveAttributeField.jsp" ) + "b" );

        try
        {
            instance.doRemoveAttributeField( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            IAttribute stored = AttributeService.getInstance( ).getAttributeWithFields( attribute.getIdAttribute( ),
                    Locale.FRANCE );
            assertNotNull( stored );
            assertEquals( 1, stored.getListAttributeFields( ).size( ) );
        }
    }

    public void testDoRemoveAttributeFieldNoToken( ) throws AccessDeniedException
    {
        List<AttributeType> types = AttributeTypeService.getInstance( ).getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            testDoRemoveAttributeFieldNoToken( _attributes.get( type ) );
        }
    }

    private void testDoRemoveAttributeFieldNoToken( IAttribute attribute ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "id_attribute", Integer.toString( attribute.getIdAttribute( ) ) );
        request.addParameter( "id_field",
                Integer.toString( attribute.getListAttributeFields( ).get( 0 ).getIdField( ) ) );

        try
        {
            instance.doRemoveAttributeField( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            IAttribute stored = AttributeService.getInstance( ).getAttributeWithFields( attribute.getIdAttribute( ),
                    Locale.FRANCE );
            assertNotNull( stored );
            assertEquals( 1, stored.getListAttributeFields( ).size( ) );
        }
    }
}
