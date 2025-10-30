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
package fr.paris.lutece.portal.web.user.attribute;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.attribute.AttributeField;
import fr.paris.lutece.portal.business.user.attribute.AttributeType;
import fr.paris.lutece.portal.business.user.attribute.IAttribute;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.PasswordResetException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.ISecurityTokenService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.user.attribute.AttributeService;
import fr.paris.lutece.portal.service.user.attribute.AttributeTypeService;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.inject.Inject;

public class AttributeFieldJspBeanTest extends LuteceTestCase
{
    private @Inject AttributeFieldJspBean instance;
    private Map<AttributeType, IAttribute> _attributes;
    private @Inject AttributeService _attributeService;
    private @Inject AttributeTypeService _attributeTypeService;
    private @Inject ISecurityTokenService _securityTokenService;
    
    @BeforeEach
    protected void setUp( ) throws Exception
    {
        _attributes = new HashMap<>( );
        List<AttributeType> types = _attributeTypeService.getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            IAttribute attribute = (IAttribute) Class.forName( type.getClassName( ) ).getDeclaredConstructor().newInstance( );
            String strName = getRandomName( );
            attribute.setTitle( strName );
            attribute.setHelpMessage( strName );
            List<AttributeField> listAttributeFields = new ArrayList<>( );
            for ( int i = 0; i < 3; i++ )
            {
                AttributeField attributeField = new AttributeField( );
                attributeField.setTitle( strName + "_" + i );
                attributeField.setValue( strName + "_" + i );
                listAttributeFields.add( attributeField );
            }
            attribute.setListAttributeFields( listAttributeFields );
            _attributeService.createAttribute( attribute );
            _attributes.put( type, attribute );
        }
    }

    @AfterEach
    protected void tearDown( ) throws Exception
    {
        for ( IAttribute attribute : _attributes.values( ) )
        {
            _attributeService.removeAttribute( attribute.getIdAttribute( ) );
        }
    }

    private String getRandomName( )
    {
        Random rand = new SecureRandom( );
        BigInteger bigInt = new BigInteger( 128, rand );
        return "junit" + bigInt.toString( 36 );
    }
    @Test
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
    @Test
    public void testDoRemoveAttributeField( ) throws AccessDeniedException
    {
        List<AttributeType> types = _attributeTypeService.getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            testDoRemoveAttributeField( _attributes.get( type ) );
        }
    }

    private void testDoRemoveAttributeField( IAttribute attribute ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "id_attribute", Integer.toString( attribute.getIdAttribute( ) ) );
        request.addParameter( "id_field", Integer.toString( attribute.getListAttributeFields( ).get( 0 ).getIdField( ) ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, "jsp/admin/user/attribute/DoRemoveAttributeField.jsp" ) );

        instance.doRemoveAttributeField( request );

        IAttribute stored = _attributeService.getAttributeWithFields( attribute.getIdAttribute( ), Locale.FRANCE );
        assertNotNull( stored );
        assertEquals( 2, stored.getListAttributeFields( ).size( ) );
    }
    @Test
    public void testDoRemoveAttributeFieldInvalidToken( ) throws AccessDeniedException
    {
        List<AttributeType> types = _attributeTypeService.getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            testDoRemoveAttributeFieldInvalidToken( _attributes.get( type ) );
        }
    }

    private void testDoRemoveAttributeFieldInvalidToken( IAttribute attribute ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "id_attribute", Integer.toString( attribute.getIdAttribute( ) ) );
        request.addParameter( "id_field", Integer.toString( attribute.getListAttributeFields( ).get( 0 ).getIdField( ) ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, "jsp/admin/user/attribute/DoRemoveAttributeField.jsp" ) + "b" );

        try
        {
            instance.doRemoveAttributeField( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            IAttribute stored = _attributeService.getAttributeWithFields( attribute.getIdAttribute( ), Locale.FRANCE );
            assertNotNull( stored );
            assertEquals( 3, stored.getListAttributeFields( ).size( ) );
        }
    }
    @Test
    public void testDoRemoveAttributeFieldNoToken( ) throws AccessDeniedException
    {
        List<AttributeType> types = _attributeTypeService.getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            testDoRemoveAttributeFieldNoToken( _attributes.get( type ) );
        }
    }

    private void testDoRemoveAttributeFieldNoToken( IAttribute attribute ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "id_attribute", Integer.toString( attribute.getIdAttribute( ) ) );
        request.addParameter( "id_field", Integer.toString( attribute.getListAttributeFields( ).get( 0 ).getIdField( ) ) );

        try
        {
            instance.doRemoveAttributeField( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            IAttribute stored = _attributeService.getAttributeWithFields( attribute.getIdAttribute( ), Locale.FRANCE );
            assertNotNull( stored );
            assertEquals( 3, stored.getListAttributeFields( ).size( ) );
        }
    }
    @Test
    public void testGetCreateAttributeField( ) throws AccessDeniedException
    {
        List<AttributeType> types = _attributeTypeService.getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            testGetCreateAttributeField( _attributes.get( type ) );
        }
    }

    private void testGetCreateAttributeField( IAttribute attribute ) throws PasswordResetException, AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_attribute", Integer.toString( attribute.getIdAttribute( ) ) );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), "CORE_USERS_MANAGEMENT" );
        instance.init( request, "CORE_USERS_MANAGEMENT" );

        assertNotNull( instance.getCreateAttributeField( request ) );
    }
    @Test
    public void testDoCreateAttributeField( ) throws AccessDeniedException
    {
        List<AttributeType> types = _attributeTypeService.getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            testDoCreateAttributeField( _attributes.get( type ) );
        }
    }

    private void testDoCreateAttributeField( IAttribute attribute ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_attribute", Integer.toString( attribute.getIdAttribute( ) ) );
        String strName = getRandomName( );
        request.setParameter( "title", strName );
        request.setParameter( "value", strName );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, "admin/user/attribute/create_attribute_field.html" ) );

        instance.doCreateAttributeField( request );

        IAttribute stored = _attributeService.getAttributeWithFields( attribute.getIdAttribute( ), Locale.FRANCE );
        assertNotNull( stored );
        assertEquals( 4, stored.getListAttributeFields( ).size( ) );
        assertEquals( 1,
                stored.getListAttributeFields( ).stream( ).filter( f -> strName.equals( f.getTitle( ) ) && strName.equals( f.getValue( ) ) ).count( ) );
    }
    @Test
    public void testDoCreateAttributeFieldInvalidToken( ) throws AccessDeniedException
    {
        List<AttributeType> types = _attributeTypeService.getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            testDoCreateAttributeFieldInvalidToken( _attributes.get( type ) );
        }
    }

    private void testDoCreateAttributeFieldInvalidToken( IAttribute attribute ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_attribute", Integer.toString( attribute.getIdAttribute( ) ) );
        String strName = getRandomName( );
        request.setParameter( "title", strName );
        request.setParameter( "value", strName );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, "admin/user/attribute/create_attribute_field.html" ) + "b" );

        try
        {
            instance.doCreateAttributeField( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            IAttribute stored = _attributeService.getAttributeWithFields( attribute.getIdAttribute( ), Locale.FRANCE );
            assertNotNull( stored );
            assertEquals( 3, stored.getListAttributeFields( ).size( ) );
            assertEquals( 0,
                    stored.getListAttributeFields( ).stream( ).filter( f -> strName.equals( f.getTitle( ) ) && strName.equals( f.getValue( ) ) ).count( ) );
        }
    }
    @Test
    public void testDoCreateAttributeFieldNoToken( ) throws AccessDeniedException
    {
        List<AttributeType> types = _attributeTypeService.getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            testDoCreateAttributeFieldNoToken( _attributes.get( type ) );
        }
    }

    private void testDoCreateAttributeFieldNoToken( IAttribute attribute ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_attribute", Integer.toString( attribute.getIdAttribute( ) ) );
        String strName = getRandomName( );
        request.setParameter( "title", strName );
        request.setParameter( "value", strName );

        try
        {
            instance.doCreateAttributeField( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            IAttribute stored = _attributeService.getAttributeWithFields( attribute.getIdAttribute( ), Locale.FRANCE );
            assertNotNull( stored );
            assertEquals( 3, stored.getListAttributeFields( ).size( ) );
            assertEquals( 0,
                    stored.getListAttributeFields( ).stream( ).filter( f -> strName.equals( f.getTitle( ) ) && strName.equals( f.getValue( ) ) ).count( ) );
        }
    }
    @Test
    public void testGetModifyAttributeField( ) throws AccessDeniedException
    {
        List<AttributeType> types = _attributeTypeService.getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            testGetModifyAttributeField( _attributes.get( type ) );
        }
    }

    private void testGetModifyAttributeField( IAttribute attribute ) throws PasswordResetException, AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_attribute", Integer.toString( attribute.getIdAttribute( ) ) );
        request.setParameter( "id_field", Integer.toString( attribute.getListAttributeFields( ).get( 0 ).getIdField( ) ) );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), "CORE_USERS_MANAGEMENT" );
        instance.init( request, "CORE_USERS_MANAGEMENT" );

        assertNotNull( instance.getModifyAttributeField( request ) );
    }
    @Test
    public void testDoModifyAttributeField( ) throws AccessDeniedException
    {
        List<AttributeType> types = _attributeTypeService.getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            testDoModifyAttributeField( _attributes.get( type ) );
        }
    }

    private void testDoModifyAttributeField( IAttribute attribute ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_attribute", Integer.toString( attribute.getIdAttribute( ) ) );
        request.setParameter( "id_field", Integer.toString( attribute.getListAttributeFields( ).get( 0 ).getIdField( ) ) );
        String strName = getRandomName( );
        request.setParameter( "title", strName );
        request.setParameter( "value", strName );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, "admin/user/attribute/modify_attribute_field.html" ) );

        instance.doModifyAttributeField( request );

        IAttribute stored = _attributeService.getAttributeWithFields( attribute.getIdAttribute( ), Locale.FRANCE );
        assertNotNull( stored );
        assertEquals( 3, stored.getListAttributeFields( ).size( ) );
        assertEquals( 1,
                stored.getListAttributeFields( ).stream( ).filter( f -> strName.equals( f.getTitle( ) ) && strName.equals( f.getValue( ) ) ).count( ) );
    }
    @Test
    public void testDoModifyAttributeFieldInvalidToken( ) throws AccessDeniedException
    {
        List<AttributeType> types = _attributeTypeService.getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            testDoModifyAttributeFieldInvalidToken( _attributes.get( type ) );
        }
    }

    private void testDoModifyAttributeFieldInvalidToken( IAttribute attribute ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_attribute", Integer.toString( attribute.getIdAttribute( ) ) );
        request.setParameter( "id_field", Integer.toString( attribute.getListAttributeFields( ).get( 0 ).getIdField( ) ) );
        String strName = getRandomName( );
        request.setParameter( "title", strName );
        request.setParameter( "value", strName );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, "admin/user/attribute/modify_attribute_field.html" ) + "b" );

        try
        {
            instance.doModifyAttributeField( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            IAttribute stored = _attributeService.getAttributeWithFields( attribute.getIdAttribute( ), Locale.FRANCE );
            assertNotNull( stored );
            assertEquals( 3, stored.getListAttributeFields( ).size( ) );
            assertEquals( 0,
                    stored.getListAttributeFields( ).stream( ).filter( f -> strName.equals( f.getTitle( ) ) && strName.equals( f.getValue( ) ) ).count( ) );
        }
    }
    @Test
    public void testDoModifyAttributeFieldNoToken( ) throws AccessDeniedException
    {
        List<AttributeType> types = _attributeTypeService.getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            testDoModifyAttributeFieldNoToken( _attributes.get( type ) );
        }
    }

    private void testDoModifyAttributeFieldNoToken( IAttribute attribute ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_attribute", Integer.toString( attribute.getIdAttribute( ) ) );
        request.setParameter( "id_field", Integer.toString( attribute.getListAttributeFields( ).get( 0 ).getIdField( ) ) );
        String strName = getRandomName( );
        request.setParameter( "title", strName );
        request.setParameter( "value", strName );

        try
        {
            instance.doModifyAttributeField( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            IAttribute stored = _attributeService.getAttributeWithFields( attribute.getIdAttribute( ), Locale.FRANCE );
            assertNotNull( stored );
            assertEquals( 3, stored.getListAttributeFields( ).size( ) );
            assertEquals( 0,
                    stored.getListAttributeFields( ).stream( ).filter( f -> strName.equals( f.getTitle( ) ) && strName.equals( f.getValue( ) ) ).count( ) );
        }
    }
    @Test
    public void testdoMoveDownAttributeField( ) throws AccessDeniedException
    {
        List<AttributeType> types = _attributeTypeService.getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            // we load the attribute from db to get all fields including
            // position
            IAttribute attribute = _attributeService.getAttributeWithFields( _attributes.get( type ).getIdAttribute( ), Locale.FRANCE );
            testdoMoveDownAttributeField( attribute );
        }
    }

    private void testdoMoveDownAttributeField( IAttribute attribute ) throws AccessDeniedException
    {
        assertTrue( attribute.getListAttributeFields( ).size( ) > 1 );
        AttributeField attributeField = attribute.getListAttributeFields( ).get( 0 );
        int nOrigPosition = attributeField.getPosition( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_attribute", Integer.toString( attribute.getIdAttribute( ) ) );
        request.setParameter( "id_field", Integer.toString( attributeField.getIdField( ) ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, attribute.getTemplateModifyAttribute( ) ) );

        instance.doMoveDownAttributeField( request );

        IAttribute stored = _attributeService.getAttributeWithFields( attribute.getIdAttribute( ), Locale.FRANCE );
        assertNotNull( stored );
        AttributeField field = stored.getListAttributeFields( ).stream( ).filter( f -> f.getIdField( ) == attributeField.getIdField( ) ).findFirst( )
                .orElseThrow( AssertionError::new );
        assertEquals( "Orig position was " + nOrigPosition + "; expected higher position but got " + field.getPosition( ), nOrigPosition + 1,
                field.getPosition( ) );
    }
    @Test
    public void testdoMoveDownAttributeFieldInvalidToken( ) throws AccessDeniedException
    {
        List<AttributeType> types = _attributeTypeService.getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            // we load the attribute from db to get all fields including
            // position
            IAttribute attribute = _attributeService.getAttributeWithFields( _attributes.get( type ).getIdAttribute( ), Locale.FRANCE );
            testdoMoveDownAttributeFieldInvalidToken( attribute );
        }
    }

    private void testdoMoveDownAttributeFieldInvalidToken( IAttribute attribute ) throws AccessDeniedException
    {
        assertTrue( attribute.getListAttributeFields( ).size( ) > 1 );
        AttributeField attributeField = attribute.getListAttributeFields( ).get( 0 );
        int nOrigPosition = attributeField.getPosition( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_attribute", Integer.toString( attribute.getIdAttribute( ) ) );
        request.setParameter( "id_field", Integer.toString( attributeField.getIdField( ) ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, attribute.getTemplateModifyAttribute( ) ) + "b" );

        try
        {
            instance.doMoveDownAttributeField( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            IAttribute stored = _attributeService.getAttributeWithFields( attribute.getIdAttribute( ), Locale.FRANCE );
            assertNotNull( stored );
            AttributeField field = stored.getListAttributeFields( ).stream( ).filter( f -> f.getIdField( ) == attributeField.getIdField( ) ).findFirst( )
                    .orElseThrow( AssertionError::new );
            assertEquals( nOrigPosition, field.getPosition( ) );
        }
    }
    @Test
    public void testdoMoveDownAttributeFieldNoToken( ) throws AccessDeniedException
    {
        List<AttributeType> types = _attributeTypeService.getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            // we load the attribute from db to get all fields including
            // position
            IAttribute attribute = _attributeService.getAttributeWithFields( _attributes.get( type ).getIdAttribute( ), Locale.FRANCE );
            testdoMoveDownAttributeFieldNoToken( attribute );
        }
    }

    private void testdoMoveDownAttributeFieldNoToken( IAttribute attribute ) throws AccessDeniedException
    {
        assertTrue( attribute.getListAttributeFields( ).size( ) > 1 );
        AttributeField attributeField = attribute.getListAttributeFields( ).get( 0 );
        int nOrigPosition = attributeField.getPosition( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_attribute", Integer.toString( attribute.getIdAttribute( ) ) );
        request.setParameter( "id_field", Integer.toString( attributeField.getIdField( ) ) );

        try
        {
            instance.doMoveDownAttributeField( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            IAttribute stored = _attributeService.getAttributeWithFields( attribute.getIdAttribute( ), Locale.FRANCE );
            assertNotNull( stored );
            AttributeField field = stored.getListAttributeFields( ).stream( ).filter( f -> f.getIdField( ) == attributeField.getIdField( ) ).findFirst( )
                    .orElseThrow( AssertionError::new );
            assertEquals( nOrigPosition, field.getPosition( ) );
        }
    }
    @Test
    public void testdoMoveUpAttributeField( ) throws AccessDeniedException
    {
        List<AttributeType> types = _attributeTypeService.getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            // we load the attribute from db to get all fields including
            // position
            IAttribute attribute = _attributeService.getAttributeWithFields( _attributes.get( type ).getIdAttribute( ), Locale.FRANCE );
            testdoMoveUpAttributeField( attribute );
        }
    }

    private void testdoMoveUpAttributeField( IAttribute attribute ) throws AccessDeniedException
    {
        assertTrue( attribute.getListAttributeFields( ).size( ) > 1 );
        AttributeField attributeField = attribute.getListAttributeFields( ).get( attribute.getListAttributeFields( ).size( ) - 1 );
        int nOrigPosition = attributeField.getPosition( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_attribute", Integer.toString( attribute.getIdAttribute( ) ) );
        request.setParameter( "id_field", Integer.toString( attributeField.getIdField( ) ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, attribute.getTemplateModifyAttribute( ) ) );

        instance.doMoveUpAttributeField( request );

        IAttribute stored = _attributeService.getAttributeWithFields( attribute.getIdAttribute( ), Locale.FRANCE );
        assertNotNull( stored );
        AttributeField field = stored.getListAttributeFields( ).stream( ).filter( f -> f.getIdField( ) == attributeField.getIdField( ) ).findFirst( )
                .orElseThrow( AssertionError::new );
        assertEquals( "Orig position was " + nOrigPosition + "; expected lower position but got " + field.getPosition( ), nOrigPosition - 1,
                field.getPosition( ) );
    }
    @Test
    public void testdoMoveUpAttributeFieldInvalidToken( ) throws AccessDeniedException
    {
        List<AttributeType> types = _attributeTypeService.getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            // we load the attribute from db to get all fields including
            // position
            IAttribute attribute = _attributeService.getAttributeWithFields( _attributes.get( type ).getIdAttribute( ), Locale.FRANCE );
            testdoMoveUpAttributeFieldInvalidToken( attribute );
        }
    }

    private void testdoMoveUpAttributeFieldInvalidToken( IAttribute attribute ) throws AccessDeniedException
    {
        assertTrue( attribute.getListAttributeFields( ).size( ) > 1 );
        AttributeField attributeField = attribute.getListAttributeFields( ).get( 0 );
        int nOrigPosition = attributeField.getPosition( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_attribute", Integer.toString( attribute.getIdAttribute( ) ) );
        request.setParameter( "id_field", Integer.toString( attributeField.getIdField( ) ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, attribute.getTemplateModifyAttribute( ) ) + "b" );

        try
        {
            instance.doMoveUpAttributeField( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            IAttribute stored = _attributeService.getAttributeWithFields( attribute.getIdAttribute( ), Locale.FRANCE );
            assertNotNull( stored );
            AttributeField field = stored.getListAttributeFields( ).stream( ).filter( f -> f.getIdField( ) == attributeField.getIdField( ) ).findFirst( )
                    .orElseThrow( AssertionError::new );
            assertEquals( nOrigPosition, field.getPosition( ) );
        }
    }
    @Test
    public void testdoMoveUpAttributeFieldNoToken( ) throws AccessDeniedException
    {
        List<AttributeType> types = _attributeTypeService.getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            // we load the attribute from db to get all fields including
            // position
            IAttribute attribute = _attributeService.getAttributeWithFields( _attributes.get( type ).getIdAttribute( ), Locale.FRANCE );
            testdoMoveUpAttributeFieldNoToken( attribute );
        }
    }

    private void testdoMoveUpAttributeFieldNoToken( IAttribute attribute ) throws AccessDeniedException
    {
        assertTrue( attribute.getListAttributeFields( ).size( ) > 1 );
        AttributeField attributeField = attribute.getListAttributeFields( ).get( 0 );
        int nOrigPosition = attributeField.getPosition( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_attribute", Integer.toString( attribute.getIdAttribute( ) ) );
        request.setParameter( "id_field", Integer.toString( attributeField.getIdField( ) ) );

        try
        {
            instance.doMoveUpAttributeField( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            IAttribute stored = _attributeService.getAttributeWithFields( attribute.getIdAttribute( ), Locale.FRANCE );
            assertNotNull( stored );
            AttributeField field = stored.getListAttributeFields( ).stream( ).filter( f -> f.getIdField( ) == attributeField.getIdField( ) ).findFirst( )
                    .orElseThrow( AssertionError::new );
            assertEquals( nOrigPosition, field.getPosition( ) );
        }
    }
}
