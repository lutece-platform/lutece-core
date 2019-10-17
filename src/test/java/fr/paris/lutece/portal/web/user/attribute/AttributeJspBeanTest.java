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

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.attribute.AttributeField;
import fr.paris.lutece.portal.business.user.attribute.AttributeType;
import fr.paris.lutece.portal.business.user.attribute.IAttribute;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.PasswordResetException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.user.attribute.AttributeService;
import fr.paris.lutece.portal.service.user.attribute.AttributeTypeService;
import fr.paris.lutece.portal.web.dashboard.AdminDashboardJspBean;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.Utils;

public class AttributeJspBeanTest extends LuteceTestCase
{
    private Map<AttributeType, IAttribute> _attributes;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        _attributes = new HashMap<>( );
        List<AttributeType> types = AttributeTypeService.getInstance( ).getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            IAttribute attribute = (IAttribute) Class.forName( type.getClassName( ) ).newInstance( );
            attribute.setTitle( getRandomName( ) );
            attribute.setHelpMessage( attribute.getTitle( ) );
            List<AttributeField> listAttributeFields = new ArrayList<>( );
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

    public void testGetCreateAttribute( ) throws PasswordResetException, AccessDeniedException
    {
        List<AttributeType> types = AttributeTypeService.getInstance( ).getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            testGetCreateAttribute( type );
        }
    }

    private void testGetCreateAttribute( AttributeType type ) throws PasswordResetException, AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "attribute_type_class_name", type.getClassName( ) );

        Utils.registerAdminUserWithRigth( request, new AdminUser( ), "CORE_USERS_MANAGEMENT" );
        AttributeJspBean instance = new AttributeJspBean( );
        instance.init( request, "CORE_USERS_MANAGEMENT" );

        assertNotNull( instance.getCreateAttribute( request ) );
    }

    public void testDoCreateAttribute( ) throws PasswordResetException, AccessDeniedException, InstantiationException, IllegalAccessException,
            ClassNotFoundException
    {
        List<AttributeType> types = AttributeTypeService.getInstance( ).getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            testDoCreateAttribute( type );
        }
    }

    private void testDoCreateAttribute( AttributeType type ) throws PasswordResetException, AccessDeniedException, InstantiationException,
            IllegalAccessException, ClassNotFoundException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "attribute_type_class_name", type.getClassName( ) );
        String strTitle = getRandomName( );
        request.setParameter( "title", strTitle );
        request.setParameter( "width", "5" );
        IAttribute attribute = (IAttribute) Class.forName( type.getClassName( ) ).newInstance( );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, attribute.getTemplateCreateAttribute( ) ) );

        Utils.registerAdminUserWithRigth( request, new AdminUser( ), "CORE_USERS_MANAGEMENT" );
        AttributeJspBean instance = new AttributeJspBean( );
        instance.init( request, "CORE_USERS_MANAGEMENT" );

        try
        {
            instance.doCreateAttribute( request );
            assertTrue( "Did not find attribute of type " + type.getClassName( ), AttributeService.getInstance( ).getAllAttributesWithoutFields( Locale.FRANCE )
                    .stream( ).anyMatch( a -> a.getTitle( ).equals( strTitle ) ) );
        }
        finally
        {
            AttributeService.getInstance( ).getAllAttributesWithoutFields( Locale.FRANCE ).stream( ).filter( a -> a.getTitle( ).equals( strTitle ) )
                    .forEach( a -> AttributeService.getInstance( ).removeAttribute( a.getIdAttribute( ) ) );
        }
    }

    public void testDoCreateAttributeInvalidToken( ) throws PasswordResetException, AccessDeniedException, InstantiationException, IllegalAccessException,
            ClassNotFoundException
    {
        List<AttributeType> types = AttributeTypeService.getInstance( ).getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            testDoCreateAttributeInvalidToken( type );
        }
    }

    private void testDoCreateAttributeInvalidToken( AttributeType type ) throws PasswordResetException, AccessDeniedException, InstantiationException,
            IllegalAccessException, ClassNotFoundException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "attribute_type_class_name", type.getClassName( ) );
        String strTitle = getRandomName( );
        request.setParameter( "title", strTitle );
        request.setParameter( "width", "5" );
        IAttribute attribute = (IAttribute) Class.forName( type.getClassName( ) ).newInstance( );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, attribute.getTemplateCreateAttribute( ) ) + "b" );

        Utils.registerAdminUserWithRigth( request, new AdminUser( ), "CORE_USERS_MANAGEMENT" );
        AttributeJspBean instance = new AttributeJspBean( );
        instance.init( request, "CORE_USERS_MANAGEMENT" );

        try
        {
            instance.doCreateAttribute( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertFalse( "Did find attribute of type " + type.getClassName( ), AttributeService.getInstance( ).getAllAttributesWithoutFields( Locale.FRANCE )
                    .stream( ).anyMatch( a -> a.getTitle( ).equals( strTitle ) ) );
        }
        finally
        {
            AttributeService.getInstance( ).getAllAttributesWithoutFields( Locale.FRANCE ).stream( ).filter( a -> a.getTitle( ).equals( strTitle ) )
                    .forEach( a -> AttributeService.getInstance( ).removeAttribute( a.getIdAttribute( ) ) );
        }
    }

    public void testDoCreateAttributeNoToken( ) throws PasswordResetException, AccessDeniedException, InstantiationException, IllegalAccessException,
            ClassNotFoundException
    {
        List<AttributeType> types = AttributeTypeService.getInstance( ).getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            testDoCreateAttributeNoToken( type );
        }
    }

    private void testDoCreateAttributeNoToken( AttributeType type ) throws PasswordResetException, AccessDeniedException, InstantiationException,
            IllegalAccessException, ClassNotFoundException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "attribute_type_class_name", type.getClassName( ) );
        String strTitle = getRandomName( );
        request.setParameter( "title", strTitle );
        request.setParameter( "width", "5" );

        Utils.registerAdminUserWithRigth( request, new AdminUser( ), "CORE_USERS_MANAGEMENT" );
        AttributeJspBean instance = new AttributeJspBean( );
        instance.init( request, "CORE_USERS_MANAGEMENT" );

        try
        {
            instance.doCreateAttribute( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertFalse( "Did find attribute of type " + type.getClassName( ), AttributeService.getInstance( ).getAllAttributesWithoutFields( Locale.FRANCE )
                    .stream( ).anyMatch( a -> a.getTitle( ).equals( strTitle ) ) );
        }
        finally
        {
            AttributeService.getInstance( ).getAllAttributesWithoutFields( Locale.FRANCE ).stream( ).filter( a -> a.getTitle( ).equals( strTitle ) )
                    .forEach( a -> AttributeService.getInstance( ).removeAttribute( a.getIdAttribute( ) ) );
        }
    }

    public void testGetModifyAttribute( ) throws PasswordResetException, AccessDeniedException
    {
        List<AttributeType> types = AttributeTypeService.getInstance( ).getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            testGetModifyAttribute( type );
        }
    }

    private void testGetModifyAttribute( AttributeType type ) throws PasswordResetException, AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        IAttribute attribute = _attributes.get( type );
        assertNotNull( attribute );
        request.setParameter( "id_attribute", Integer.toString( attribute.getIdAttribute( ) ) );

        Utils.registerAdminUserWithRigth( request, new AdminUser( ), "CORE_USERS_MANAGEMENT" );
        AttributeJspBean instance = new AttributeJspBean( );
        instance.init( request, "CORE_USERS_MANAGEMENT" );

        assertNotNull( instance.getModifyAttribute( request ) );
    }

    public void testDoModifyAttribute( ) throws PasswordResetException, AccessDeniedException, InstantiationException, IllegalAccessException,
            ClassNotFoundException
    {
        List<AttributeType> types = AttributeTypeService.getInstance( ).getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            testDoModifyAttribute( type );
        }
    }

    private void testDoModifyAttribute( AttributeType type ) throws PasswordResetException, AccessDeniedException, InstantiationException,
            IllegalAccessException, ClassNotFoundException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        IAttribute attribute = _attributes.get( type );
        assertNotNull( attribute );
        request.setParameter( "id_attribute", Integer.toString( attribute.getIdAttribute( ) ) );
        String strTitle = getRandomName( );
        request.setParameter( "title", strTitle );
        request.setParameter( "width", "5" );

        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, attribute.getTemplateModifyAttribute( ) ) );

        Utils.registerAdminUserWithRigth( request, new AdminUser( ), "CORE_USERS_MANAGEMENT" );
        AttributeJspBean instance = new AttributeJspBean( );
        instance.init( request, "CORE_USERS_MANAGEMENT" );

        instance.doModifyAttribute( request );
        IAttribute stored = AttributeService.getInstance( ).getAttributeWithoutFields( attribute.getIdAttribute( ), Locale.FRANCE );
        assertNotNull( stored );
        assertEquals( strTitle, stored.getTitle( ) );
    }

    public void testDoModifyAttributeInvalidToken( ) throws PasswordResetException, AccessDeniedException, InstantiationException, IllegalAccessException,
            ClassNotFoundException
    {
        List<AttributeType> types = AttributeTypeService.getInstance( ).getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            testDoModifyAttributeInvalidToken( type );
        }
    }

    private void testDoModifyAttributeInvalidToken( AttributeType type ) throws PasswordResetException, AccessDeniedException, InstantiationException,
            IllegalAccessException, ClassNotFoundException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        IAttribute attribute = _attributes.get( type );
        assertNotNull( attribute );
        request.setParameter( "id_attribute", Integer.toString( attribute.getIdAttribute( ) ) );
        String strTitle = getRandomName( );
        request.setParameter( "title", strTitle );
        request.setParameter( "width", "5" );

        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, attribute.getTemplateModifyAttribute( ) ) + "b" );

        Utils.registerAdminUserWithRigth( request, new AdminUser( ), "CORE_USERS_MANAGEMENT" );
        AttributeJspBean instance = new AttributeJspBean( );
        instance.init( request, "CORE_USERS_MANAGEMENT" );

        try
        {
            instance.doModifyAttribute( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            IAttribute stored = AttributeService.getInstance( ).getAttributeWithoutFields( attribute.getIdAttribute( ), Locale.FRANCE );
            assertNotNull( stored );
            assertEquals( attribute.getTitle( ), stored.getTitle( ) );
        }
    }

    public void testDoModifyAttributeNoToken( ) throws PasswordResetException, AccessDeniedException, InstantiationException, IllegalAccessException,
            ClassNotFoundException
    {
        List<AttributeType> types = AttributeTypeService.getInstance( ).getAttributeTypes( Locale.FRANCE );
        for ( AttributeType type : types )
        {
            testDoModifyAttributeNoToken( type );
        }
    }

    private void testDoModifyAttributeNoToken( AttributeType type ) throws PasswordResetException, AccessDeniedException, InstantiationException,
            IllegalAccessException, ClassNotFoundException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        IAttribute attribute = _attributes.get( type );
        assertNotNull( attribute );
        request.setParameter( "id_attribute", Integer.toString( attribute.getIdAttribute( ) ) );
        String strTitle = getRandomName( );
        request.setParameter( "title", strTitle );
        request.setParameter( "width", "5" );

        Utils.registerAdminUserWithRigth( request, new AdminUser( ), "CORE_USERS_MANAGEMENT" );
        AttributeJspBean instance = new AttributeJspBean( );
        instance.init( request, "CORE_USERS_MANAGEMENT" );

        try
        {
            instance.doModifyAttribute( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            IAttribute stored = AttributeService.getInstance( ).getAttributeWithoutFields( attribute.getIdAttribute( ), Locale.FRANCE );
            assertNotNull( stored );
            assertEquals( attribute.getTitle( ), stored.getTitle( ) );
        }
    }

    public void testDoConfirmRemoveAttribute( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_attribute",
                Integer.toString( _attributes.values( ).stream( ).findFirst( ).orElseThrow( IllegalStateException::new ).getIdAttribute( ) ) );

        AttributeJspBean instance = new AttributeJspBean( );
        instance.doConfirmRemoveAttribute( request );

        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertTrue( message.getRequestParameters( ).containsKey( SecurityTokenService.PARAMETER_TOKEN ) );
    }

    public void testDoRemoveAttribute( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        int idAttribute = _attributes.values( ).stream( ).findFirst( ).orElseThrow( IllegalStateException::new ).getIdAttribute( );
        request.setParameter( "id_attribute", Integer.toString( idAttribute ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/attribute/DoRemoveAttribute.jsp" ) );

        AttributeJspBean instance = new AttributeJspBean( );
        instance.doRemoveAttribute( request );

        IAttribute stored = AttributeService.getInstance( ).getAttributeWithoutFields( idAttribute, Locale.FRANCE );
        assertNull( stored );
    }

    public void testDoRemoveAttributeInvalidToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        int idAttribute = _attributes.values( ).stream( ).findFirst( ).orElseThrow( IllegalStateException::new ).getIdAttribute( );
        request.setParameter( "id_attribute", Integer.toString( idAttribute ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/attribute/DoRemoveAttribute.jsp" ) + "b" );

        AttributeJspBean instance = new AttributeJspBean( );
        try
        {
            instance.doRemoveAttribute( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            IAttribute stored = AttributeService.getInstance( ).getAttributeWithoutFields( idAttribute, Locale.FRANCE );
            assertNotNull( stored );
        }
    }

    public void testDoRemoveAttributeNoToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        int idAttribute = _attributes.values( ).stream( ).findFirst( ).orElseThrow( IllegalStateException::new ).getIdAttribute( );
        request.setParameter( "id_attribute", Integer.toString( idAttribute ) );

        AttributeJspBean instance = new AttributeJspBean( );
        try
        {
            instance.doRemoveAttribute( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            IAttribute stored = AttributeService.getInstance( ).getAttributeWithoutFields( idAttribute, Locale.FRANCE );
            assertNotNull( stored );
        }
    }

    public void testDoMoveDownAttribute( ) throws PasswordResetException, AccessDeniedException
    {
        List<IAttribute> listAttributes = AttributeService.getInstance( ).getAllAttributesWithoutFields( Locale.FRANCE );
        assertTrue( listAttributes.size( ) >= 2 );
        int nIdAttribute = listAttributes.get( 0 ).getIdAttribute( );
        int nPosition = listAttributes.get( 0 ).getPosition( );

        MockHttpServletRequest request = new MockHttpServletRequest( );

        Utils.registerAdminUserWithRigth( request, new AdminUser( ), "CORE_USERS_MANAGEMENT" );
        AttributeJspBean instance = new AttributeJspBean( );
        instance.init( request, "CORE_USERS_MANAGEMENT" );

        request.setParameter( "id_attribute", Integer.toString( nIdAttribute ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, AdminDashboardJspBean.TEMPLATE_MANAGE_DASHBOARDS ) );

        instance.doMoveDownAttribute( request );

        IAttribute stored = AttributeService.getInstance( ).getAttributeWithoutFields( nIdAttribute, Locale.FRANCE );
        assertNotNull( stored );
        assertEquals( nPosition + 1, stored.getPosition( ) );
    }

    public void testDoMoveDownAttributeInvalidToken( ) throws PasswordResetException, AccessDeniedException
    {
        List<IAttribute> listAttributes = AttributeService.getInstance( ).getAllAttributesWithoutFields( Locale.FRANCE );
        assertTrue( listAttributes.size( ) >= 2 );
        int nIdAttribute = listAttributes.get( 0 ).getIdAttribute( );
        int nPosition = listAttributes.get( 0 ).getPosition( );

        MockHttpServletRequest request = new MockHttpServletRequest( );

        Utils.registerAdminUserWithRigth( request, new AdminUser( ), "CORE_USERS_MANAGEMENT" );
        AttributeJspBean instance = new AttributeJspBean( );
        instance.init( request, "CORE_USERS_MANAGEMENT" );

        request.setParameter( "id_attribute", Integer.toString( nIdAttribute ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/user/attribute/manage_attributes.html" ) + "b" );

        try
        {
            instance.doMoveDownAttribute( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            IAttribute stored = AttributeService.getInstance( ).getAttributeWithoutFields( nIdAttribute, Locale.FRANCE );
            assertNotNull( stored );
            assertEquals( nPosition, stored.getPosition( ) );
        }
    }

    public void testDoMoveDownAttributeNoToken( ) throws PasswordResetException, AccessDeniedException
    {
        List<IAttribute> listAttributes = AttributeService.getInstance( ).getAllAttributesWithoutFields( Locale.FRANCE );
        assertTrue( listAttributes.size( ) >= 2 );
        int nIdAttribute = listAttributes.get( 0 ).getIdAttribute( );
        int nPosition = listAttributes.get( 0 ).getPosition( );

        MockHttpServletRequest request = new MockHttpServletRequest( );

        Utils.registerAdminUserWithRigth( request, new AdminUser( ), "CORE_USERS_MANAGEMENT" );
        AttributeJspBean instance = new AttributeJspBean( );
        instance.init( request, "CORE_USERS_MANAGEMENT" );

        request.setParameter( "id_attribute", Integer.toString( nIdAttribute ) );

        try
        {
            instance.doMoveDownAttribute( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            IAttribute stored = AttributeService.getInstance( ).getAttributeWithoutFields( nIdAttribute, Locale.FRANCE );
            assertNotNull( stored );
            assertEquals( nPosition, stored.getPosition( ) );
        }
    }

    public void testDoMoveUpAttribute( ) throws PasswordResetException, AccessDeniedException
    {
        List<IAttribute> listAttributes = AttributeService.getInstance( ).getAllAttributesWithoutFields( Locale.FRANCE );
        assertTrue( listAttributes.size( ) >= 2 );
        int nIdAttribute = listAttributes.get( listAttributes.size( ) - 1 ).getIdAttribute( );
        int nPosition = listAttributes.get( listAttributes.size( ) - 1 ).getPosition( );

        MockHttpServletRequest request = new MockHttpServletRequest( );

        Utils.registerAdminUserWithRigth( request, new AdminUser( ), "CORE_USERS_MANAGEMENT" );
        AttributeJspBean instance = new AttributeJspBean( );
        instance.init( request, "CORE_USERS_MANAGEMENT" );

        request.setParameter( "id_attribute", Integer.toString( nIdAttribute ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, AdminDashboardJspBean.TEMPLATE_MANAGE_DASHBOARDS ) );

        instance.doMoveUpAttribute( request );

        IAttribute stored = AttributeService.getInstance( ).getAttributeWithoutFields( nIdAttribute, Locale.FRANCE );
        assertNotNull( stored );
        assertEquals( nPosition - 1, stored.getPosition( ) );
    }

    public void testDoMoveUpAttributeInvalidToken( ) throws PasswordResetException, AccessDeniedException
    {
        List<IAttribute> listAttributes = AttributeService.getInstance( ).getAllAttributesWithoutFields( Locale.FRANCE );
        assertTrue( listAttributes.size( ) >= 2 );
        int nIdAttribute = listAttributes.get( listAttributes.size( ) - 1 ).getIdAttribute( );
        int nPosition = listAttributes.get( listAttributes.size( ) - 1 ).getPosition( );

        MockHttpServletRequest request = new MockHttpServletRequest( );

        Utils.registerAdminUserWithRigth( request, new AdminUser( ), "CORE_USERS_MANAGEMENT" );
        AttributeJspBean instance = new AttributeJspBean( );
        instance.init( request, "CORE_USERS_MANAGEMENT" );

        request.setParameter( "id_attribute", Integer.toString( nIdAttribute ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/user/attribute/manage_attributes.html" ) + "b" );

        try
        {
            instance.doMoveUpAttribute( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            IAttribute stored = AttributeService.getInstance( ).getAttributeWithoutFields( nIdAttribute, Locale.FRANCE );
            assertNotNull( stored );
            assertEquals( nPosition, stored.getPosition( ) );
        }
    }

    public void testDoMoveUpAttributeNoToken( ) throws PasswordResetException, AccessDeniedException
    {
        List<IAttribute> listAttributes = AttributeService.getInstance( ).getAllAttributesWithoutFields( Locale.FRANCE );
        assertTrue( listAttributes.size( ) >= 2 );
        int nIdAttribute = listAttributes.get( listAttributes.size( ) - 1 ).getIdAttribute( );
        int nPosition = listAttributes.get( listAttributes.size( ) - 1 ).getPosition( );

        MockHttpServletRequest request = new MockHttpServletRequest( );

        Utils.registerAdminUserWithRigth( request, new AdminUser( ), "CORE_USERS_MANAGEMENT" );
        AttributeJspBean instance = new AttributeJspBean( );
        instance.init( request, "CORE_USERS_MANAGEMENT" );

        request.setParameter( "id_attribute", Integer.toString( nIdAttribute ) );

        try
        {
            instance.doMoveUpAttribute( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            IAttribute stored = AttributeService.getInstance( ).getAttributeWithoutFields( nIdAttribute, Locale.FRANCE );
            assertNotNull( stored );
            assertEquals( nPosition, stored.getPosition( ) );
        }
    }

    private String getRandomName( )
    {
        Random rand = new SecureRandom( );
        BigInteger bigInt = new BigInteger( 128, rand );
        return "junit" + bigInt.toString( 36 );
    }
}
