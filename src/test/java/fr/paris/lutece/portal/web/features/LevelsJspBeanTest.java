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
package fr.paris.lutece.portal.web.features;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.right.Level;
import fr.paris.lutece.portal.business.right.LevelHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.Utils;

/**
 * LevelsJspBeanTest Test Class
 *
 */
public class LevelsJspBeanTest extends LuteceTestCase
{
    private static final String TEST_LEVEL_ID = "0"; // administrator level_right
    private MockHttpServletRequest request;
    private LevelsJspBean instance;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), LevelsJspBean.RIGHT_MANAGE_LEVELS );

        instance = new LevelsJspBean( );
        instance.init( request, LevelsJspBean.RIGHT_MANAGE_LEVELS );
    }
    /**
     * Test of getManageLevels method, of class fr.paris.lutece.portal.web.features.LevelsJspBean.
     */
    public void testGetManageLevels( ) throws AccessDeniedException
    {
        instance.getManageLevels( request );
    }

    /**
     * Test of getCreateLevel method, of class fr.paris.lutece.portal.web.features.LevelsJspBean.
     */
    public void testGetCreateLevel( ) throws AccessDeniedException
    {
        instance.getCreateLevel( request );
    }

    /**
     * Test of doCreateLevel method, of class fr.paris.lutece.portal.web.features.LevelsJspBean.
     * @throws AccessDeniedException 
     */
    public void testDoCreateLevel( ) throws AccessDeniedException
    {
        final String name = getRandomName( );
        request.setParameter( "level_name", name );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/features/create_level.html" ) );

        LevelHome.getLevelsList( ).forEach( level -> {
            assertFalse( name.equals( level.getName( ) ) );
        } );
        try
        {
            instance.doCreateLevel( request );
            assertEquals( 1, LevelHome.getLevelsList( ).stream( ).filter( level -> {
                return name.equals( level.getName( ) );
            } ).count( ) );
        }
        finally
        {
            LevelHome.getLevelsList( ).stream( ).filter( level -> {
                return name.equals( level.getName( ) );
            } ).forEach( level -> {
                LevelHome.remove( level.getId( ) );
            } );
        }
    }

    public void testDoCreateLevelInvalidToken( ) throws AccessDeniedException
    {
        final String name = getRandomName( );
        request.setParameter( "level_name", name );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/features/create_level.html" ) + "b" );

        LevelHome.getLevelsList( ).forEach( level -> {
            assertFalse( name.equals( level.getName( ) ) );
        } );
        try
        {
            instance.doCreateLevel( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            LevelHome.getLevelsList( ).forEach( level -> {
                assertFalse( name.equals( level.getName( ) ) );
            } );
        }
        finally
        {
            LevelHome.getLevelsList( ).stream( ).filter( level -> {
                return name.equals( level.getName( ) );
            } ).forEach( level -> {
                LevelHome.remove( level.getId( ) );
            } );
        }
    }

    public void testDoCreateLevelNoToken( ) throws AccessDeniedException
    {
        final String name = getRandomName( );
        request.setParameter( "level_name", name );

        LevelHome.getLevelsList( ).forEach( level -> {
            assertFalse( name.equals( level.getName( ) ) );
        } );
        try
        {
            instance.doCreateLevel( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            LevelHome.getLevelsList( ).forEach( level -> {
                assertFalse( name.equals( level.getName( ) ) );
            } );
        }
        finally
        {
            LevelHome.getLevelsList( ).stream( ).filter( level -> {
                return name.equals( level.getName( ) );
            } ).forEach( level -> {
                LevelHome.remove( level.getId( ) );
            } );
        }
    }

    private String getRandomName( )
    {
        Random rand = new SecureRandom( );
        BigInteger bigInt = new BigInteger( 128, rand );
        return "junit" + bigInt.toString( 36 );
    }

    /**
     * Test of getModifyLevel method, of class fr.paris.lutece.portal.web.features.LevelsJspBean.
     */
    public void testGetModifyLevel( ) throws AccessDeniedException
    {
        request.addParameter( Parameters.LEVEL_ID, TEST_LEVEL_ID );

        instance.getModifyLevel( request );
    }

    /**
     * Test of doModifyLevel method, of class fr.paris.lutece.portal.web.features.LevelsJspBean.
     * @throws AccessDeniedException 
     */
    public void testDoModifyLevel( ) throws AccessDeniedException
    {
        final String name = getRandomName( );
        Level level = new Level( );
        level.setName( name );
        LevelHome.create( level );
        request.setParameter( Parameters.LEVEL_ID, Integer.toString( level.getId( ) ) );
        request.setParameter( Parameters.LEVEL_NAME, name + "_mod" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/features/modify_level.html" ) );
        try
        {
            assertEquals( name, LevelHome.findByPrimaryKey( level.getId( ) ).getName( ) );
            instance.doModifyLevel( request );
            assertEquals( name + "_mod", LevelHome.findByPrimaryKey( level.getId( ) ).getName( ) );
        }
        finally
        {
            LevelHome.remove( level.getId( ) );
        }
    }

    public void testDoModifyLevelInvalidToken( ) throws AccessDeniedException
    {
        final String name = getRandomName( );
        Level level = new Level( );
        level.setName( name );
        LevelHome.create( level );
        request.setParameter( Parameters.LEVEL_ID, Integer.toString( level.getId( ) ) );
        request.setParameter( Parameters.LEVEL_NAME, name + "_mod" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/features/modify_level.html" ) + "b" );
        try
        {
            assertEquals( name, LevelHome.findByPrimaryKey( level.getId( ) ).getName( ) );
            instance.doModifyLevel( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertEquals( name, LevelHome.findByPrimaryKey( level.getId( ) ).getName( ) );
        }
        finally
        {
            LevelHome.remove( level.getId( ) );
        }
    }

    public void testDoModifyLevelNoToken( ) throws AccessDeniedException
    {
        final String name = getRandomName( );
        Level level = new Level( );
        level.setName( name );
        LevelHome.create( level );
        request.setParameter( Parameters.LEVEL_ID, Integer.toString( level.getId( ) ) );
        request.setParameter( Parameters.LEVEL_NAME, name + "_mod" );
        try
        {
            assertEquals( name, LevelHome.findByPrimaryKey( level.getId( ) ).getName( ) );
            instance.doModifyLevel( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertEquals( name, LevelHome.findByPrimaryKey( level.getId( ) ).getName( ) );
        }
        finally
        {
            LevelHome.remove( level.getId( ) );
        }
    }
}
