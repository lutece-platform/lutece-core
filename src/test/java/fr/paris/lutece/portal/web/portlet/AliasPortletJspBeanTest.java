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
package fr.paris.lutece.portal.web.portlet;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Random;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.portlet.AliasPortlet;
import fr.paris.lutece.portal.business.portlet.AliasPortletHome;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletType;
import fr.paris.lutece.portal.business.portlet.PortletTypeHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.web.admin.AdminPagePortletJspBeanTest.TestPortletHome;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.test.LuteceTestCase;

public class AliasPortletJspBeanTest extends LuteceTestCase
{
    private PortletType _portletType;
    private TestPortlet _portlet;
    private AliasPortletJspBean _instance;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        _portletType = new PortletType( );
        _portletType.setId( getRandomName( ) );
        _portletType.setHomeClass( TestPortletHome.class.getName( ) );
        PortletTypeHome.create( _portletType );
        _portlet = new TestPortlet( _portletType.getId( ) );
        _portlet.setName( getRandomName( ) );
        _portlet.setHomeClassName( TestPortletHome.class.getName( ) );
        _portlet.setPageId( 1 );
        _portlet.setAcceptAlias( 1 );
        TestPortletHome _testPortletHome = new TestPortletHome( );
        _testPortletHome.create( _portlet );
        _instance = new AliasPortletJspBean( );
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        Collection<Portlet> aliases = AliasPortletHome.getAliasList( _portlet.getId( ) ); // only
                                                                                          // loads
                                                                                          // Ids
        if ( aliases != null )
        {
            for ( Portlet alias : aliases )
            {
                AliasPortletHome.findByPrimaryKey( alias.getId( ) ).remove( );
            }
        }
        TestPortletHome _testPortletHome = new TestPortletHome( );
        _testPortletHome.remove( _portlet );
        PortletTypeHome.remove( _portletType.getId( ) );
        super.tearDown( );
    }

    private String getRandomName( )
    {
        Random rand = new SecureRandom( );
        BigInteger bigInt = new BigInteger( 128, rand );
        return "junit" + bigInt.toString( 36 );
    }

    public void testDoCreate( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "alias_id", Integer.toString( _portlet.getId( ) ) );
        String strName = getRandomName( );
        request.setParameter( Parameters.PORTLET_NAME, strName );
        request.setParameter( Parameters.ORDER, "2" );
        request.setParameter( Parameters.COLUMN, "2" );
        request.setParameter( Parameters.ACCEPT_ALIAS, "0" );
        request.setParameter( Parameters.DISPLAY_PORTLET_TITLE, "1" );
        request.setParameter( Parameters.PORTLET_TYPE_ID, AliasPortletHome.getInstance( ).getPortletTypeId( ) );
        request.setParameter( "page_id", "1" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/portlet/create_portlet.html" ) );

        _instance.doCreate( request );

        Collection<Portlet> aliases = AliasPortletHome.getAliasList( _portlet.getId( ) ); // only
                                                                                          // loads
                                                                                          // Ids
        assertNotNull( aliases );
        assertEquals( 1, aliases.size( ) );
        Portlet alias = aliases.stream( ).findFirst( ).orElseThrow( IllegalStateException::new );
        alias = AliasPortletHome.findByPrimaryKey( alias.getId( ) );
        assertEquals( strName, alias.getName( ) );
        assertEquals( 2, alias.getOrder( ) );
        assertEquals( 2, alias.getColumn( ) );
        assertEquals( 0, alias.getAcceptAlias( ) );
        assertEquals( 1, alias.getDisplayPortletTitle( ) );
    }

    public void testDoCreateInvalidToken( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "alias_id", Integer.toString( _portlet.getId( ) ) );
        String strName = getRandomName( );
        request.setParameter( Parameters.PORTLET_NAME, strName );
        request.setParameter( Parameters.ORDER, "2" );
        request.setParameter( Parameters.COLUMN, "2" );
        request.setParameter( Parameters.ACCEPT_ALIAS, "0" );
        request.setParameter( Parameters.DISPLAY_PORTLET_TITLE, "1" );
        request.setParameter( Parameters.PORTLET_TYPE_ID, AliasPortletHome.getInstance( ).getPortletTypeId( ) );
        request.setParameter( "page_id", "1" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/portlet/create_portlet.html" ) + "b" );

        try
        {
            _instance.doCreate( request );
            fail( "Should have thrown" );
        }
        catch ( AppException e )
        {
            assertTrue( e.getCause( ) instanceof AccessDeniedException );
            Collection<Portlet> aliases = AliasPortletHome.getAliasList( _portlet.getId( ) );
            assertNotNull( aliases );
            assertEquals( 0, aliases.size( ) );
        }
    }

    public void testDoCreateNoToken( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "alias_id", Integer.toString( _portlet.getId( ) ) );
        String strName = getRandomName( );
        request.setParameter( Parameters.PORTLET_NAME, strName );
        request.setParameter( Parameters.ORDER, "2" );
        request.setParameter( Parameters.COLUMN, "2" );
        request.setParameter( Parameters.ACCEPT_ALIAS, "0" );
        request.setParameter( Parameters.DISPLAY_PORTLET_TITLE, "1" );
        request.setParameter( Parameters.PORTLET_TYPE_ID, AliasPortletHome.getInstance( ).getPortletTypeId( ) );
        request.setParameter( "page_id", "1" );

        try
        {
            _instance.doCreate( request );
            fail( "Should have thrown" );
        }
        catch ( AppException e )
        {
            assertTrue( e.getCause( ) instanceof AccessDeniedException );
            Collection<Portlet> aliases = AliasPortletHome.getAliasList( _portlet.getId( ) );
            assertNotNull( aliases );
            assertEquals( 0, aliases.size( ) );
        }
    }

    public void testDoModify( )
    {
        AliasPortlet alias = new AliasPortlet( );
        alias.setAliasId( _portlet.getId( ) );
        alias.setPageId( 1 );
        alias.setPortletTypeId( AliasPortletHome.getInstance( ).getPortletTypeId( ) );
        AliasPortletHome.getInstance( ).create( alias );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "portlet_id", Integer.toString( alias.getId( ) ) );
        request.setParameter( "alias_id", Integer.toString( _portlet.getId( ) ) );
        String strName = getRandomName( );
        request.setParameter( Parameters.PORTLET_NAME, strName );
        request.setParameter( Parameters.ORDER, "2" );
        request.setParameter( Parameters.COLUMN, "2" );
        request.setParameter( Parameters.ACCEPT_ALIAS, "0" );
        request.setParameter( Parameters.DISPLAY_PORTLET_TITLE, "1" );
        request.setParameter( Parameters.PORTLET_TYPE_ID, AliasPortletHome.getInstance( ).getPortletTypeId( ) );
        request.setParameter( "page_id", "1" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/portlet/create_portlet.html" ) );

        _instance.doModify( request );

        AliasPortlet stored = ( AliasPortlet ) AliasPortletHome.findByPrimaryKey( alias.getId( ) );
        assertNotNull( stored );
        assertEquals( strName, stored.getName( ) );
        assertEquals( 2, stored.getOrder( ) );
        assertEquals( 2, stored.getColumn( ) );
        assertEquals( 0, stored.getAcceptAlias( ) );
        assertEquals( 1, stored.getDisplayPortletTitle( ) );
    }

    public void testDoModifyInvalidToken( )
    {
        AliasPortlet alias = new AliasPortlet( );
        alias.setAliasId( _portlet.getId( ) );
        alias.setPageId( 1 );
        alias.setPortletTypeId( AliasPortletHome.getInstance( ).getPortletTypeId( ) );
        AliasPortletHome.getInstance( ).create( alias );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "portlet_id", Integer.toString( alias.getId( ) ) );
        request.setParameter( "alias_id", Integer.toString( _portlet.getId( ) ) );
        String strName = getRandomName( );
        request.setParameter( Parameters.PORTLET_NAME, strName );
        request.setParameter( Parameters.ORDER, "2" );
        request.setParameter( Parameters.COLUMN, "2" );
        request.setParameter( Parameters.ACCEPT_ALIAS, "0" );
        request.setParameter( Parameters.DISPLAY_PORTLET_TITLE, "1" );
        request.setParameter( Parameters.PORTLET_TYPE_ID, AliasPortletHome.getInstance( ).getPortletTypeId( ) );
        request.setParameter( "page_id", "1" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/portlet/create_portlet.html" ) + "b" );

        try
        {
            _instance.doModify( request );
            fail( "Should have thrown" );
        }
        catch ( AppException e )
        {
            assertTrue( e.getCause( ) instanceof AccessDeniedException );
            AliasPortlet stored = ( AliasPortlet ) AliasPortletHome.findByPrimaryKey( alias.getId( ) );
            assertNotNull( stored );
            assertNull( stored.getName( ) );
            assertEquals( 0, stored.getOrder( ) );
            assertEquals( 0, stored.getColumn( ) );
            assertEquals( 0, stored.getAcceptAlias( ) );
            assertEquals( 0, stored.getDisplayPortletTitle( ) );
        }
    }

    public void testDoModifyNoToken( )
    {
        AliasPortlet alias = new AliasPortlet( );
        alias.setAliasId( _portlet.getId( ) );
        alias.setPageId( 1 );
        alias.setPortletTypeId( AliasPortletHome.getInstance( ).getPortletTypeId( ) );
        AliasPortletHome.getInstance( ).create( alias );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "portlet_id", Integer.toString( alias.getId( ) ) );
        request.setParameter( "alias_id", Integer.toString( _portlet.getId( ) ) );
        String strName = getRandomName( );
        request.setParameter( Parameters.PORTLET_NAME, strName );
        request.setParameter( Parameters.ORDER, "2" );
        request.setParameter( Parameters.COLUMN, "2" );
        request.setParameter( Parameters.ACCEPT_ALIAS, "0" );
        request.setParameter( Parameters.DISPLAY_PORTLET_TITLE, "1" );
        request.setParameter( Parameters.PORTLET_TYPE_ID, AliasPortletHome.getInstance( ).getPortletTypeId( ) );
        request.setParameter( "page_id", "1" );

        try
        {
            _instance.doModify( request );
            fail( "Should have thrown" );
        }
        catch ( AppException e )
        {
            assertTrue( e.getCause( ) instanceof AccessDeniedException );
            AliasPortlet stored = ( AliasPortlet ) AliasPortletHome.findByPrimaryKey( alias.getId( ) );
            assertNotNull( stored );
            assertNull( stored.getName( ) );
            assertEquals( 0, stored.getOrder( ) );
            assertEquals( 0, stored.getColumn( ) );
            assertEquals( 0, stored.getAcceptAlias( ) );
            assertEquals( 0, stored.getDisplayPortletTitle( ) );
        }
    }
}
