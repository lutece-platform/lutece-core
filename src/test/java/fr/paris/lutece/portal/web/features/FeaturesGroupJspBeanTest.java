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
package fr.paris.lutece.portal.web.features;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.right.FeatureGroup;
import fr.paris.lutece.portal.business.right.FeatureGroupHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.Utils;

/**
 * FeaturesGroupJspBeanTest Test Class
 *
 */
public class FeaturesGroupJspBeanTest extends LuteceTestCase
{
    private static final String PARAMETER_GROUP_ID = "group_id";
    private static final String TEST_GROUP_ID = "CONTENT"; // content feautures_group
    private FeaturesGroupJspBean instance;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        instance = new FeaturesGroupJspBean( );
    }
    /**
     * Test of getManageFeatures method, of class fr.paris.lutece.portal.web.features.FeaturesGroupJspBean.
     */
    public void testGetManageFeatures( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), FeaturesGroupJspBean.RIGHT_FEATURES_MANAGEMENT );

        instance.init( request, FeaturesGroupJspBean.RIGHT_FEATURES_MANAGEMENT );
        instance.getManageFeatures( request );
    }

    /**
     * Test of getManageGroups method, of class fr.paris.lutece.portal.web.features.FeaturesGroupJspBean.
     */
    public void testGetManageGroups( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), FeaturesGroupJspBean.RIGHT_FEATURES_MANAGEMENT );

        instance.init( request, FeaturesGroupJspBean.RIGHT_FEATURES_MANAGEMENT );
        instance.getManageGroups( request );
    }

    /**
     * Test of getDispatchFeatures method, of class fr.paris.lutece.portal.web.features.FeaturesGroupJspBean.
     */
    public void testGetDispatchFeatures( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), FeaturesGroupJspBean.RIGHT_FEATURES_MANAGEMENT );

        instance.init( request, FeaturesGroupJspBean.RIGHT_FEATURES_MANAGEMENT );
        instance.getDispatchFeatures( request );
    }

    /**
     * Test of doDispatchFeature method, of class fr.paris.lutece.portal.web.features.FeaturesGroupJspBean.
     */
    public void testDoDispatchFeature( )
    {
    }

    /**
     * Test of getCreateGroup method, of class fr.paris.lutece.portal.web.features.FeaturesGroupJspBean.
     */
    public void testGetCreateGroup( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), FeaturesGroupJspBean.RIGHT_FEATURES_MANAGEMENT );

        instance.init( request, FeaturesGroupJspBean.RIGHT_FEATURES_MANAGEMENT );
        assertNotNull( instance.getCreateGroup( request ) );
    }

    /**
     * Test of getModifyGroup method, of class fr.paris.lutece.portal.web.features.FeaturesGroupJspBean.
     */
    public void testGetModifyGroup( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_GROUP_ID, TEST_GROUP_ID );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), FeaturesGroupJspBean.RIGHT_FEATURES_MANAGEMENT );

        instance.init( request, FeaturesGroupJspBean.RIGHT_FEATURES_MANAGEMENT );
        instance.getModifyGroup( request );
    }

    /**
     * Test of doCreateGroup method, of class
     * fr.paris.lutece.portal.web.features.FeaturesGroupJspBean.
     * 
     * @throws AccessDeniedException
     */
    public void testDoCreateGroup( ) throws AccessDeniedException
    {
        String strGroupName = getRandomName( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "group_id", strGroupName );
        request.addParameter( "group_name", strGroupName );
        request.addParameter( "group_description", strGroupName );
        request.addParameter( "group_order", "1" );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/features/create_group.html" ) );

        try
        {
            instance.doCreateGroup( request );
            FeatureGroup group = FeatureGroupHome.findByPrimaryKey( strGroupName );
            assertNotNull( group );
            assertEquals( strGroupName, group.getId( ) );
            assertEquals( strGroupName, group.getLabelKey( ) );
            assertEquals( strGroupName, group.getDescriptionKey( ) );
            assertEquals( 1, group.getOrder( ) );
        }
        finally
        {
            FeatureGroupHome.remove( strGroupName );
        }
    }

    public void testDoCreateGroupInvalidToken( ) throws AccessDeniedException
    {
        String strGroupName = getRandomName( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "group_id", strGroupName );
        request.addParameter( "group_name", strGroupName );
        request.addParameter( "group_description", strGroupName );
        request.addParameter( "group_order", "1" );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/features/create_group.html" ) + "b" );

        try
        {
            instance.doCreateGroup( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        { 
            FeatureGroup group = FeatureGroupHome.findByPrimaryKey( strGroupName );
            assertNull( group );
        }
        finally
        {
            FeatureGroupHome.remove( strGroupName );
        }
    }

    public void testDoCreateGroupNoToken( ) throws AccessDeniedException
    {
        String strGroupName = getRandomName( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "group_id", strGroupName );
        request.addParameter( "group_name", strGroupName );
        request.addParameter( "group_description", strGroupName );
        request.addParameter( "group_order", "1" );

        try
        {
            instance.doCreateGroup( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        { 
            FeatureGroup group = FeatureGroupHome.findByPrimaryKey( strGroupName );
            assertNull( group );
        }
        finally
        {
            FeatureGroupHome.remove( strGroupName );
        }
    }

    private String getRandomName( )
    {
        Random rand = new SecureRandom( );
        BigInteger bigInt = new BigInteger( 128, rand );
        return "junit" + bigInt.toString( 36 );
    }
    
    /**
     * Test of doModifyGroup method, of class fr.paris.lutece.portal.web.features.FeaturesGroupJspBean.
     */
    public void testDoModifyGroup( )
    {
    }

    /**
     * Test of getRemoveGroup method, of class fr.paris.lutece.portal.web.features.FeaturesGroupJspBean.
     */
    public void testGetRemoveGroup( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_GROUP_ID, TEST_GROUP_ID );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), FeaturesGroupJspBean.RIGHT_FEATURES_MANAGEMENT );

        instance.init( request, FeaturesGroupJspBean.RIGHT_FEATURES_MANAGEMENT );
        instance.getRemoveGroup( request );
    }

    /**
     * Test of doRemoveGroup method, of class fr.paris.lutece.portal.web.features.FeaturesGroupJspBean.
     */
    public void testDoRemoveGroup( )
    {
    }
}
