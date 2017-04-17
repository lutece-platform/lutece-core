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
package fr.paris.lutece.portal.web.role;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Random;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.role.Role;
import fr.paris.lutece.portal.business.role.RoleHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

public class RoleJspBeanTest extends LuteceTestCase
{
    private static final String PARAMETER_PAGE_ROLE = "role";
    private RoleJspBean bean;
    private Role role;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        bean = new RoleJspBean( );
        role = new Role( );
        role.setRole( getRandomName( ) );
        role.setRoleDescription( role.getRole( ) );
        role.setWorkgroup( AdminWorkgroupService.ALL_GROUPS );
        RoleHome.create( role );
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        RoleHome.remove( role.getRole( ) );
        super.tearDown( );
    }

    private String getRandomName( )
    {
        Random rand = new SecureRandom( );
        BigInteger bigInt = new BigInteger( 128, rand );
        return "junit" + bigInt.toString( 36 );
    }

    public void testGetRemovePageRole( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        // no args
        bean.getRemovePageRole( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        ReferenceList listLanguages = I18nService.getAdminLocales( Locale.FRANCE );
        for ( ReferenceItem lang : listLanguages )
        {
            assertTrue( message.getText( new Locale( lang.getCode( ) ) ).contains( PARAMETER_PAGE_ROLE ) );
        }
        // invalid arg
        request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_PAGE_ROLE, role.getRole( ) );
        bean.getRemovePageRole( request );
        message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        for ( ReferenceItem lang : listLanguages )
        {
            assertTrue( message.getText( new Locale( lang.getCode( ) ) ).contains( role.getRole( ) ) );
        }
        // valid arg
        request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_PAGE_ROLE, role.getRole( ) );
        bean.getRemovePageRole( request );
        message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        for ( ReferenceItem lang : listLanguages )
        {
            assertTrue( message.getText( new Locale( lang.getCode( ) ) ).contains( role.getRole( ) ) );
        }
        assertTrue( message.getRequestParameters( ).containsKey( SecurityTokenService.PARAMETER_TOKEN ) );
    }

    public void testDoCreatePageRole( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        final String name = getRandomName( );
        request.setParameter( "role", name );
        request.setParameter( "role_description", name );
        request.setParameter( "workgroup_key", AdminWorkgroupService.ALL_GROUPS );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/role/create_page_role.html" ) );

        assertNull( RoleHome.findByPrimaryKey( name ) );
        try
        {
            bean.doCreatePageRole( request );
            Role stored = RoleHome.findByPrimaryKey( name );
            assertNotNull( stored );
            assertEquals( name, stored.getRole( ) );
            assertEquals( name, stored.getRoleDescription( ) );
            assertEquals( AdminWorkgroupService.ALL_GROUPS, stored.getWorkgroup( ) );
        }
        finally
        {
            RoleHome.remove( name );
        }
    }

    public void testDoCreatePageRoleInvalidToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        final String name = getRandomName( );
        request.setParameter( "role", name );
        request.setParameter( "role_description", name );
        request.setParameter( "workgroup_key", AdminWorkgroupService.ALL_GROUPS );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/role/create_page_role.html" ) + "b" );

        assertNull( RoleHome.findByPrimaryKey( name ) );
        try
        {
            bean.doCreatePageRole( request );
            fail( "Shoud have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertNull( RoleHome.findByPrimaryKey( name ) );
        }
        finally
        {
            RoleHome.remove( name );
        }
    }

    public void testDoCreatePageRoleNoToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        final String name = getRandomName( );
        request.setParameter( "role", name );
        request.setParameter( "role_description", name );
        request.setParameter( "workgroup_key", AdminWorkgroupService.ALL_GROUPS );

        assertNull( RoleHome.findByPrimaryKey( name ) );
        try
        {
            bean.doCreatePageRole( request );
            fail( "Shoud have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertNull( RoleHome.findByPrimaryKey( name ) );
        }
        finally
        {
            RoleHome.remove( name );
        }
    }

    public void testDoModifyPageRole( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "role", role.getRole( ) );
        request.setParameter( "role_description", role.getRoleDescription( ) + "_mod" );
        request.setParameter( "workgroup_key", AdminWorkgroupService.ALL_GROUPS );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/role/modify_page_role.html" ) );

        assertEquals( role.getRoleDescription( ), RoleHome.findByPrimaryKey( role.getRole( ) ).getRoleDescription( ) );
        bean.doModifyPageRole( request );
        assertEquals( role.getRoleDescription( ) + "_mod",
                RoleHome.findByPrimaryKey( role.getRole( ) ).getRoleDescription( ) );
    }

    public void testDoModifyPageRoleInvalidtoken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "role", role.getRole( ) );
        request.setParameter( "role_description", role.getRoleDescription( ) + "_mod" );
        request.setParameter( "workgroup_key", AdminWorkgroupService.ALL_GROUPS );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/role/modify_page_role.html" ) + "b" );

        assertEquals( role.getRoleDescription( ), RoleHome.findByPrimaryKey( role.getRole( ) ).getRoleDescription( ) );
        try
        {
            bean.doModifyPageRole( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertEquals( role.getRoleDescription( ),
                    RoleHome.findByPrimaryKey( role.getRole( ) ).getRoleDescription( ) );
        }
    }

    public void testDoModifyPageRoleNotoken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "role", role.getRole( ) );
        request.setParameter( "role_description", role.getRoleDescription( ) + "_mod" );
        request.setParameter( "workgroup_key", AdminWorkgroupService.ALL_GROUPS );

        assertEquals( role.getRoleDescription( ), RoleHome.findByPrimaryKey( role.getRole( ) ).getRoleDescription( ) );
        try
        {
            bean.doModifyPageRole( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertEquals( role.getRoleDescription( ),
                    RoleHome.findByPrimaryKey( role.getRole( ) ).getRoleDescription( ) );
        }
    }

    public void testDoRemovePageRole( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "role", role.getRole( ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "DoRemovePageRole.jsp" ) );

        assertNotNull( RoleHome.findByPrimaryKey( role.getRole( ) ) );
        bean.doRemovePageRole( request );
        assertNull( RoleHome.findByPrimaryKey( role.getRole( ) ) );
    }

    public void testDoRemovePageRoleInvalidToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "role", role.getRole( ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "DoRemovePageRole.jsp" ) + "b" );

        assertNotNull( RoleHome.findByPrimaryKey( role.getRole( ) ) );
        try
        {
            bean.doRemovePageRole( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertNotNull( RoleHome.findByPrimaryKey( role.getRole( ) ) );
        }
    }

    public void testDoRemovePageRoleNoToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "role", role.getRole( ) );

        assertNotNull( RoleHome.findByPrimaryKey( role.getRole( ) ) );
        try
        {
            bean.doRemovePageRole( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertNotNull( RoleHome.findByPrimaryKey( role.getRole( ) ) );
        }
    }
}
