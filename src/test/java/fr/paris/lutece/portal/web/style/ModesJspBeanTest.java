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
package fr.paris.lutece.portal.web.style;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.style.ModeHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.Utils;

/**
 * ModeJspBeanTest Test Class
 *
 */
public class ModesJspBeanTest extends LuteceTestCase
{
    private static final String TEST_MODE_ID = "0"; // normal mode
    private MockHttpServletRequest request;
    private ModesJspBean instance;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), ModesJspBean.RIGHT_MANAGE_MODES );

        instance = new ModesJspBean( );
        instance.init( request, ModesJspBean.RIGHT_MANAGE_MODES );
    }
    /**
     * Test of getManageModes method, of class fr.paris.lutece.portal.web.style.ModesJspBean.
     */
    public void testGetManageModes( ) throws AccessDeniedException
    {
        instance.getManageModes( request );
    }

    /**
     * Test of getCreateMode method, of class fr.paris.lutece.portal.web.style.ModesJspBean.
     */
    public void testGetCreateMode( ) throws AccessDeniedException
    {
        instance.getCreateMode( request );
    }

    /**
     * Test of doCreateMode method, of fr.paris.lutece.portal.web.style.ModesJspBean.
     * @throws AccessDeniedException 
     */
    public void testDoCreateMode( ) throws AccessDeniedException
    {
        final String desc = getRandomName( );
        request.addParameter( Parameters.MODE_DESCRIPTION, desc );
        request.addParameter( Parameters.MODE_PATH, desc );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "admin/style/create_mode.html" ) );
        try
        {
            instance.doCreateMode( request );
            assertTrue( ModeHome.getModesList( ).stream( ).anyMatch( m -> m.getDescription( ).equals( desc ) ) );
        }
        finally
        {
            ModeHome.getModesList( ).stream( ).filter( m -> m.getDescription( ).equals( desc ) ).forEach( m -> ModeHome.remove( m.getId( ) ) );
        }
    }

    public void testDoCreateModeInvalidToken( ) throws AccessDeniedException
    {
        final String desc = getRandomName( );
        request.addParameter( Parameters.MODE_DESCRIPTION, desc );
        request.addParameter( Parameters.MODE_PATH, desc );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "admin/style/create_mode.html" ) + "b" );

        try
        {
            instance.doCreateMode( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertFalse( ModeHome.getModesList( ).stream( ).anyMatch( m -> m.getDescription( ).equals( desc ) ) );
        }
        finally
        {
            ModeHome.getModesList( ).stream( ).filter( m -> m.getDescription( ).equals( desc ) ).forEach( m -> ModeHome.remove( m.getId( ) ) );
        }
    }

    public void testDoCreateModeNoToken( ) throws AccessDeniedException
    {
        final String desc = getRandomName( );
        request.addParameter( Parameters.MODE_DESCRIPTION, desc );
        request.addParameter( Parameters.MODE_PATH, desc );

        try
        {
            instance.doCreateMode( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertFalse( ModeHome.getModesList( ).stream( ).anyMatch( m -> m.getDescription( ).equals( desc ) ) );
        }
        finally
        {
            ModeHome.getModesList( ).stream( ).filter( m -> m.getDescription( ).equals( desc ) ).forEach( m -> ModeHome.remove( m.getId( ) ) );
        }
    }

    /**
     * Test of testGetModifyMode method, of class fr.paris.lutece.portal.web.style.ModesJspBean.
     */
    public void testGetModifyMode( ) throws AccessDeniedException
    {
        request.addParameter( Parameters.MODE_ID, TEST_MODE_ID );
        instance.getModifyMode( request );
    }

    /**
     * Test of doModifyMode method, of fr.paris.lutece.portal.web.style.ModesJspBean.
     */
    public void testDoModifyMode( )
    {
        System.out.println( "doModifyMode" );

        // Not implemented yet
    }

    /**
     * Test of testGetModeView method, of class fr.paris.lutece.portal.web.style.ModesJspBean.
     */
    public void testGetModeView( ) throws AccessDeniedException
    {
        request.addParameter( Parameters.MODE_ID, TEST_MODE_ID );
        instance.init( request, ModesJspBean.RIGHT_MANAGE_MODES );
    }

    private String getRandomName( )
    {
        Random rand = new SecureRandom( );
        BigInteger bigInt = new BigInteger( 128, rand );
        return "junit" + bigInt.toString( 36 );
    }
}
