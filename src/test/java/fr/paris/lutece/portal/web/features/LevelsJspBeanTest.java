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
package fr.paris.lutece.portal.web.features;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.right.Level;
import fr.paris.lutece.portal.business.right.LevelHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.security.ISecurityTokenService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;

/**
 * LevelsJspBeanTest Test Class
 *
 */
public class LevelsJspBeanTest extends LuteceTestCase
{
    // Templates files path, used as security token keys
    private static final String TEMPLATE_CREATE_LEVEL = "admin/features/create_level.html";
    private static final String TEMPLATE_MODIFY_LEVEL = "admin/features/modify_level.html";
    private static final String TEST_LEVEL_ID = "0"; // administrator level_right
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private @Inject ISecurityTokenService _securityTokenService;

    @BeforeEach
    protected void setUp( ) throws Exception
    {
        request = new MockHttpServletRequest( );
        response = new MockHttpServletResponse( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), LevelsJspBean.RIGHT_MANAGE_LEVELS );
    }

    /**
     * Gets a CDI-managed instance of the controller under test.
     *
     * @return the LevelsJspBean instance
     */
    private LevelsJspBean getInstance( )
    {
        return CDI.current( ).select( LevelsJspBean.class ).get( );
    }

    /**
     * Test of createLevel view, of class LevelsJspBean.
     *
     * @throws AccessDeniedException
     *             if the controller denies access
     */
    @Test
    public void testGetCreateLevel( ) throws AccessDeniedException
    {
        request.addParameter( MVCUtils.PARAMETER_VIEW, "createLevel" );
        assertNotNull( getInstance( ).processController( request, response ) );
    }

    /**
     * Test of createLevel action, of class LevelsJspBean.
     *
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Test
    public void testDoCreateLevel( ) throws AccessDeniedException
    {
        final String name = getRandomName( );
        request.addParameter( MVCUtils.PARAMETER_ACTION, "createLevel" );
        request.setParameter( "level_name", name );
        request.setParameter( "level_id", String.valueOf( getRandomId( ) ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, TEMPLATE_CREATE_LEVEL ) );

        LevelHome.getLevelsList( ).forEach( level -> assertFalse( name.equals( level.getName( ) ) ) );
        try
        {
            getInstance( ).processController( request, response );
            assertEquals( 1, LevelHome.getLevelsList( ).stream( ).filter( level -> name.equals( level.getName( ) ) ).count( ) );
        }
        finally
        {
            LevelHome.getLevelsList( ).stream( ).filter( level -> name.equals( level.getName( ) ) ).forEach( level -> LevelHome.remove( level.getId( ) ) );
        }
    }

    /**
     * Test that an invalid token prevents the level creation.
     *
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Test
    public void testDoCreateLevelInvalidToken( ) throws AccessDeniedException
    {
        final String name = getRandomName( );
        request.addParameter( MVCUtils.PARAMETER_ACTION, "createLevel" );
        request.setParameter( "level_name", name );
        request.setParameter( "level_id", String.valueOf( getRandomId( ) ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, TEMPLATE_CREATE_LEVEL ) + "b" );

        LevelHome.getLevelsList( ).forEach( level -> assertFalse( name.equals( level.getName( ) ) ) );
        try
        {
            getInstance( ).processController( request, response );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            LevelHome.getLevelsList( ).forEach( level -> assertFalse( name.equals( level.getName( ) ) ) );
        }
        finally
        {
            LevelHome.getLevelsList( ).stream( ).filter( level -> name.equals( level.getName( ) ) ).forEach( level -> LevelHome.remove( level.getId( ) ) );
        }
    }

    /**
     * Test that a missing token prevents the level creation.
     *
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Test
    public void testDoCreateLevelNoToken( ) throws AccessDeniedException
    {
        final String name = getRandomName( );
        request.addParameter( MVCUtils.PARAMETER_ACTION, "createLevel" );
        request.setParameter( "level_name", name );
        request.setParameter( "level_id", String.valueOf( getRandomId( ) ) );

        LevelHome.getLevelsList( ).forEach( level -> assertFalse( name.equals( level.getName( ) ) ) );
        try
        {
            getInstance( ).processController( request, response );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            LevelHome.getLevelsList( ).forEach( level -> assertFalse( name.equals( level.getName( ) ) ) );
        }
        finally
        {
            LevelHome.getLevelsList( ).stream( ).filter( level -> name.equals( level.getName( ) ) ).forEach( level -> LevelHome.remove( level.getId( ) ) );
        }
    }

    private String getRandomName( )
    {
        Random rand = new SecureRandom( );
        BigInteger bigInt = new BigInteger( 128, rand );
        return "junit" + bigInt.toString( 36 );
    }

    private int getRandomId( )
    {
        return ThreadLocalRandom.current( ).nextInt( 10, 50 );
    }

    /**
     * Test of modifyLevel view, of class LevelsJspBean.
     *
     * @throws AccessDeniedException
     *             if the controller denies access
     */
    @Test
    public void testGetModifyLevel( ) throws AccessDeniedException
    {
        request.addParameter( MVCUtils.PARAMETER_VIEW, "modifyLevel" );
        request.addParameter( Parameters.LEVEL_ID, TEST_LEVEL_ID );

        assertNotNull( getInstance( ).processController( request, response ) );
    }

    /**
     * Test of modifyLevel action, of class LevelsJspBean.
     *
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Test
    public void testDoModifyLevel( ) throws AccessDeniedException
    {
        final String name = getRandomName( );
        Level level = new Level( );
        level.setName( name );
        level.setId( getRandomId( ) );
        LevelHome.create( level );
        request.addParameter( MVCUtils.PARAMETER_ACTION, "modifyLevel" );
        request.setParameter( Parameters.LEVEL_ID, Integer.toString( level.getId( ) ) );
        request.setParameter( Parameters.LEVEL_NAME, name + "_mod" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, TEMPLATE_MODIFY_LEVEL ) );
        try
        {
            assertEquals( name, LevelHome.findByPrimaryKey( level.getId( ) ).getName( ) );
            getInstance( ).processController( request, response );
            assertEquals( name + "_mod", LevelHome.findByPrimaryKey( level.getId( ) ).getName( ) );
        }
        finally
        {
            LevelHome.remove( level.getId( ) );
        }
    }

    /**
     * Test that an invalid token prevents the level modification.
     *
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Test
    public void testDoModifyLevelInvalidToken( ) throws AccessDeniedException
    {
        final String name = getRandomName( );
        Level level = new Level( );
        level.setName( name );
        level.setId( getRandomId( ) );
        LevelHome.create( level );
        request.addParameter( MVCUtils.PARAMETER_ACTION, "modifyLevel" );
        request.setParameter( Parameters.LEVEL_ID, Integer.toString( level.getId( ) ) );
        request.setParameter( Parameters.LEVEL_NAME, name + "_mod" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, TEMPLATE_MODIFY_LEVEL ) + "b" );
        try
        {
            assertEquals( name, LevelHome.findByPrimaryKey( level.getId( ) ).getName( ) );
            getInstance( ).processController( request, response );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( name, LevelHome.findByPrimaryKey( level.getId( ) ).getName( ) );
        }
        finally
        {
            LevelHome.remove( level.getId( ) );
        }
    }

    /**
     * Test that a missing token prevents the level modification.
     *
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Test
    public void testDoModifyLevelNoToken( ) throws AccessDeniedException
    {
        final String name = getRandomName( );
        Level level = new Level( );
        level.setName( name );
        level.setId( getRandomId( ) );
        LevelHome.create( level );
        request.addParameter( MVCUtils.PARAMETER_ACTION, "modifyLevel" );
        request.setParameter( Parameters.LEVEL_ID, Integer.toString( level.getId( ) ) );
        request.setParameter( Parameters.LEVEL_NAME, name + "_mod" );
        try
        {
            assertEquals( name, LevelHome.findByPrimaryKey( level.getId( ) ).getName( ) );
            getInstance( ).processController( request, response );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( name, LevelHome.findByPrimaryKey( level.getId( ) ).getName( ) );
        }
        finally
        {
            LevelHome.remove( level.getId( ) );
        }
    }
}
