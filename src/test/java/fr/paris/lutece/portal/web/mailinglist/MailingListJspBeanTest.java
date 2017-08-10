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
package fr.paris.lutece.portal.web.mailinglist;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.mailinglist.MailingList;
import fr.paris.lutece.portal.business.mailinglist.MailingListHome;
import fr.paris.lutece.portal.business.mailinglist.MailingListUsersFilter;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.mailinglist.AdminMailingListService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.test.LuteceTestCase;

public class MailingListJspBeanTest extends LuteceTestCase
{
    private MailingList mailingList;
    private MailingListJspBean bean;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        mailingList = new MailingList( );
        mailingList.setName( getRandomName( ) );
        mailingList.setDescription( mailingList.getName( ) );
        mailingList.setWorkgroup( AdminWorkgroupService.ALL_GROUPS );
        MailingListHome.create( mailingList );
        bean = new MailingListJspBean( );
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        MailingList storedMailinglist = MailingListHome.findByPrimaryKey( mailingList.getId( ) );
        if ( storedMailinglist != null )
        {
            for ( MailingListUsersFilter filter : storedMailinglist.getFilters( ) )
            {
                MailingListHome.deleteFilterToMailingList( filter, mailingList.getId( ) );
            }
            MailingListHome.remove( mailingList.getId( ) );
        }
        super.tearDown( );
    }

    private String getRandomName( )
    {
        Random rand = new SecureRandom( );
        BigInteger bigInt = new BigInteger( 128, rand );
        return "junit" + bigInt.toString( 36 );
    }

    public void testDoAddUsers( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_mailinglist", Integer.toString( mailingList.getId( ) ) );
        request.setParameter( "workgroup", AdminWorkgroupService.ALL_GROUPS );
        request.setParameter( "role", AdminMailingListService.ALL_ROLES );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "admin/mailinglist/add_users.html" ) );

        MailingListUsersFilter filter = new MailingListUsersFilter( );
        filter.setRole( AdminMailingListService.ALL_ROLES );
        filter.setWorkgroup( AdminWorkgroupService.ALL_GROUPS );
        assertFalse( MailingListHome.checkFilter( filter, mailingList.getId( ) ) );

        bean.doAddUsers( request );

        assertTrue( MailingListHome.checkFilter( filter, mailingList.getId( ) ) );
    }

    public void testDoAddUsersInvalidToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_mailinglist", Integer.toString( mailingList.getId( ) ) );
        request.setParameter( "workgroup", AdminWorkgroupService.ALL_GROUPS );
        request.setParameter( "role", AdminMailingListService.ALL_ROLES );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "admin/mailinglist/add_users.html" )
                + "b" );

        MailingListUsersFilter filter = new MailingListUsersFilter( );
        filter.setRole( AdminMailingListService.ALL_ROLES );
        filter.setWorkgroup( AdminWorkgroupService.ALL_GROUPS );
        assertFalse( MailingListHome.checkFilter( filter, mailingList.getId( ) ) );
        try
        {
            bean.doAddUsers( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertFalse( MailingListHome.checkFilter( filter, mailingList.getId( ) ) );
        }
    }

    public void testDoAddUsersNoToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_mailinglist", Integer.toString( mailingList.getId( ) ) );
        request.setParameter( "workgroup", AdminWorkgroupService.ALL_GROUPS );
        request.setParameter( "role", AdminMailingListService.ALL_ROLES );

        MailingListUsersFilter filter = new MailingListUsersFilter( );
        filter.setRole( AdminMailingListService.ALL_ROLES );
        filter.setWorkgroup( AdminWorkgroupService.ALL_GROUPS );
        assertFalse( MailingListHome.checkFilter( filter, mailingList.getId( ) ) );
        try
        {
            bean.doAddUsers( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertFalse( MailingListHome.checkFilter( filter, mailingList.getId( ) ) );
        }
    }

    public void testDoCreateMailingList( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        final String name = getRandomName( );
        request.setParameter( "name", name );
        request.setParameter( "workgroup", AdminWorkgroupService.ALL_GROUPS );
        request.setParameter( "description", name );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/mailinglist/create_mailinglist.html" ) );

        MailingListHome.findAll( ).forEach( mailingList -> {
            assertFalse( name.equals( mailingList.getName( ) ) );
        } );
        try
        {
            bean.doCreateMailingList( request );
            assertEquals( 1, MailingListHome.findAll( ).stream( ).filter( mailingList -> {
                return name.equals( mailingList.getName( ) );
            } ).count( ) );
        }
        finally
        {
            MailingListHome.findAll( ).stream( ).filter( mailingList -> {
                return name.equals( mailingList.getName( ) );
            } ).forEach( mailingList -> {
                MailingListHome.remove( mailingList.getId( ) );
            } );
        }
    }

    public void testDoCreateMailingListInvalidToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        final String name = getRandomName( );
        request.setParameter( "name", name );
        request.setParameter( "workgroup", AdminWorkgroupService.ALL_GROUPS );
        request.setParameter( "description", name );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/mailinglist/create_mailinglist.html" ) + "b" );

        MailingListHome.findAll( ).forEach( mailingList -> {
            assertFalse( name.equals( mailingList.getName( ) ) );
        } );
        try
        {
            bean.doCreateMailingList( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            MailingListHome.findAll( ).forEach( mailingList -> {
                assertFalse( name.equals( mailingList.getName( ) ) );
            } );
        }
        finally
        {
            MailingListHome.findAll( ).stream( ).filter( mailingList -> {
                return name.equals( mailingList.getName( ) );
            } ).forEach( mailingList -> {
                MailingListHome.remove( mailingList.getId( ) );
            } );
        }
    }

    public void testDoCreateMailingListNoToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        final String name = getRandomName( );
        request.setParameter( "name", name );
        request.setParameter( "workgroup", AdminWorkgroupService.ALL_GROUPS );
        request.setParameter( "description", name );

        MailingListHome.findAll( ).forEach( mailingList -> {
            assertFalse( name.equals( mailingList.getName( ) ) );
        } );
        try
        {
            bean.doCreateMailingList( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            MailingListHome.findAll( ).forEach( mailingList -> {
                assertFalse( name.equals( mailingList.getName( ) ) );
            } );
        }
        finally
        {
            MailingListHome.findAll( ).stream( ).filter( mailingList -> {
                return name.equals( mailingList.getName( ) );
            } ).forEach( mailingList -> {
                MailingListHome.remove( mailingList.getId( ) );
            } );
        }
    }

    public void testDoDeleteFilter( ) throws AccessDeniedException
    {
        assertEquals( 0, MailingListHome.findByPrimaryKey( mailingList.getId( ) ).getFilters( ).size( ) );

        MailingListUsersFilter filter = new MailingListUsersFilter( );
        filter.setRole( AdminMailingListService.ALL_ROLES );
        filter.setWorkgroup( AdminWorkgroupService.ALL_GROUPS );
        MailingListHome.addFilterToMailingList( filter, mailingList.getId( ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_mailinglist", Integer.toString( mailingList.getId( ) ) );
        request.setParameter( "role", AdminMailingListService.ALL_ROLES );
        request.setParameter( "workgroup", AdminWorkgroupService.ALL_GROUPS );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/mailinglist/modify_mailinglist.html" ) );

        assertEquals( 1, MailingListHome.findByPrimaryKey( mailingList.getId( ) ).getFilters( ).size( ) );
        bean.doDeleteFilter( request );
        assertEquals( 0, MailingListHome.findByPrimaryKey( mailingList.getId( ) ).getFilters( ).size( ) );
    }

    public void testDoDeleteFilterInvalidToken( ) throws AccessDeniedException
    {
        assertEquals( 0, MailingListHome.findByPrimaryKey( mailingList.getId( ) ).getFilters( ).size( ) );

        MailingListUsersFilter filter = new MailingListUsersFilter( );
        filter.setRole( AdminMailingListService.ALL_ROLES );
        filter.setWorkgroup( AdminWorkgroupService.ALL_GROUPS );
        MailingListHome.addFilterToMailingList( filter, mailingList.getId( ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_mailinglist", Integer.toString( mailingList.getId( ) ) );
        request.setParameter( "role", AdminMailingListService.ALL_ROLES );
        request.setParameter( "workgroup", AdminWorkgroupService.ALL_GROUPS );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/mailinglist/modify_mailinglist.html" ) + "b" );

        assertEquals( 1, MailingListHome.findByPrimaryKey( mailingList.getId( ) ).getFilters( ).size( ) );
        try
        {
            bean.doDeleteFilter( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( 1, MailingListHome.findByPrimaryKey( mailingList.getId( ) ).getFilters( ).size( ) );
        }
    }

    public void testDoDeleteFilterNoToken( ) throws AccessDeniedException
    {
        assertEquals( 0, MailingListHome.findByPrimaryKey( mailingList.getId( ) ).getFilters( ).size( ) );

        MailingListUsersFilter filter = new MailingListUsersFilter( );
        filter.setRole( AdminMailingListService.ALL_ROLES );
        filter.setWorkgroup( AdminWorkgroupService.ALL_GROUPS );
        MailingListHome.addFilterToMailingList( filter, mailingList.getId( ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_mailinglist", Integer.toString( mailingList.getId( ) ) );
        request.setParameter( "role", AdminMailingListService.ALL_ROLES );
        request.setParameter( "workgroup", AdminWorkgroupService.ALL_GROUPS );

        assertEquals( 1, MailingListHome.findByPrimaryKey( mailingList.getId( ) ).getFilters( ).size( ) );
        try
        {
            bean.doDeleteFilter( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( 1, MailingListHome.findByPrimaryKey( mailingList.getId( ) ).getFilters( ).size( ) );
        }
    }

    public void testDoModifyMailingList( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_mailinglist", Integer.toString( mailingList.getId( ) ) );
        request.setParameter( "name", mailingList.getName( ) + "_mod" );
        request.setParameter( "description", mailingList.getDescription( ) + "_mod" );
        request.setParameter( "workgroup", AdminWorkgroupService.ALL_GROUPS );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/mailinglist/modify_mailinglist.html" ) );

        MailingList storedMailling = MailingListHome.findByPrimaryKey( mailingList.getId( ) );
        assertEquals( mailingList.getName( ), storedMailling.getName( ) );
        assertEquals( mailingList.getDescription( ), storedMailling.getDescription( ) );

        bean.doModifyMailingList( request );

        storedMailling = MailingListHome.findByPrimaryKey( mailingList.getId( ) );
        assertEquals( mailingList.getName( ) + "_mod", storedMailling.getName( ) );
        assertEquals( mailingList.getDescription( ) + "_mod", storedMailling.getDescription( ) );
    }

    public void testDoModifyMailingListInvalidToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_mailinglist", Integer.toString( mailingList.getId( ) ) );
        request.setParameter( "name", mailingList.getName( ) + "_mod" );
        request.setParameter( "description", mailingList.getDescription( ) + "_mod" );
        request.setParameter( "workgroup", AdminWorkgroupService.ALL_GROUPS );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/mailinglist/modify_mailinglist.html" ) + "b" );

        MailingList storedMailling = MailingListHome.findByPrimaryKey( mailingList.getId( ) );
        assertEquals( mailingList.getName( ), storedMailling.getName( ) );
        assertEquals( mailingList.getDescription( ), storedMailling.getDescription( ) );
        try
        {
            bean.doModifyMailingList( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            storedMailling = MailingListHome.findByPrimaryKey( mailingList.getId( ) );
            assertEquals( mailingList.getName( ), storedMailling.getName( ) );
            assertEquals( mailingList.getDescription( ), storedMailling.getDescription( ) );
        }
    }

    public void testDoModifyMailingListNoToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_mailinglist", Integer.toString( mailingList.getId( ) ) );
        request.setParameter( "name", mailingList.getName( ) + "_mod" );
        request.setParameter( "description", mailingList.getDescription( ) + "_mod" );
        request.setParameter( "workgroup", AdminWorkgroupService.ALL_GROUPS );

        MailingList storedMailling = MailingListHome.findByPrimaryKey( mailingList.getId( ) );
        assertEquals( mailingList.getName( ), storedMailling.getName( ) );
        assertEquals( mailingList.getDescription( ), storedMailling.getDescription( ) );
        try
        {
            bean.doModifyMailingList( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            storedMailling = MailingListHome.findByPrimaryKey( mailingList.getId( ) );
            assertEquals( mailingList.getName( ), storedMailling.getName( ) );
            assertEquals( mailingList.getDescription( ), storedMailling.getDescription( ) );
        }
    }

    public void testGetConfirmRemoveMailingList( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_mailinglist", Integer.toString( mailingList.getId( ) ) );

        bean.getConfirmRemoveMailingList( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertTrue( message.getRequestParameters( ).containsKey( SecurityTokenService.PARAMETER_TOKEN ) );
        assertTrue( message.getRequestParameters( ).containsKey( "id_mailinglist" ) );
        assertEquals( Integer.toString( mailingList.getId( ) ), message.getRequestParameters( ).get( "id_mailinglist" ) );
    }

    public void testDoRemoveMailingList( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_mailinglist", Integer.toString( mailingList.getId( ) ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/mailinglist/DoRemoveMailingList.jsp" ) );

        assertNotNull( MailingListHome.findByPrimaryKey( mailingList.getId( ) ) );
        bean.doRemoveMailingList( request );
        assertNull( MailingListHome.findByPrimaryKey( mailingList.getId( ) ) );
    }

    public void testDoRemoveMailingListInvalidToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_mailinglist", Integer.toString( mailingList.getId( ) ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/mailinglist/DoRemoveMailingList.jsp" ) + "b" );

        assertNotNull( MailingListHome.findByPrimaryKey( mailingList.getId( ) ) );
        try
        {
            bean.doRemoveMailingList( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertNotNull( MailingListHome.findByPrimaryKey( mailingList.getId( ) ) );
        }
    }

    public void testDoRemoveMailingListNoToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_mailinglist", Integer.toString( mailingList.getId( ) ) );

        assertNotNull( MailingListHome.findByPrimaryKey( mailingList.getId( ) ) );
        try
        {
            bean.doRemoveMailingList( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertNotNull( MailingListHome.findByPrimaryKey( mailingList.getId( ) ) );
        }
    }
}
