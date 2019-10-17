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
import fr.paris.lutece.portal.business.right.IRightDAO;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.right.RightHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.web.dashboard.AdminDashboardJspBean;
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
    private FeatureGroup featureGroup;
    private Right right;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        instance = new FeaturesGroupJspBean( );
        String strGroupName = getRandomName( );
        featureGroup = new FeatureGroup( );
        featureGroup.setId( strGroupName );
        featureGroup.setLabelKey( strGroupName );
        featureGroup.setDescriptionKey( strGroupName );

        FeatureGroupHome.create( featureGroup );

        right = new Right( );
        String strRight = getRandomName( );
        right.setDescriptionKey( strRight );
        right.setId( strRight );
        RightHome.create( right );
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        RightHome.remove( right.getId( ) );
        FeatureGroupHome.remove( featureGroup.getId( ) );
        super.tearDown( );
    }


    /**
     * Test of doDispatchFeature method, of class fr.paris.lutece.portal.web.features.FeaturesGroupJspBean.
     * 
     * @throws AccessDeniedException
     */
    public void testDoDispatchFeature( ) throws AccessDeniedException
    {
        Right stored = RightHome.findByPrimaryKey( right.getId( ) );
        assertNotNull( stored );
        assertNull( stored.getFeatureGroup( ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "right_id", right.getId( ) );
        request.addParameter( "group_name", featureGroup.getId( ) );
        request.addParameter( "order_id", Integer.toString( stored.getOrder( ) + 1 ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, AdminDashboardJspBean.TEMPLATE_MANAGE_DASHBOARDS ) );

        instance.doDispatchFeature( request );
        stored = RightHome.findByPrimaryKey( right.getId( ) );
        assertNotNull( stored );
        assertEquals( featureGroup.getId( ), stored.getFeatureGroup( ) );
        assertEquals( stored.getOrder( ) + 1, right.getOrder( ) );
    }

    public void testDoDispatchFeatureInvalidToken( ) throws AccessDeniedException
    {
        Right stored = RightHome.findByPrimaryKey( right.getId( ) );
        assertNotNull( stored );
        assertNull( stored.getFeatureGroup( ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "right_id", right.getId( ) );
        request.addParameter( "group_name", featureGroup.getId( ) );
        request.addParameter( "order_id", Integer.toString( stored.getOrder( ) + 1 ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/features/dispatch_features.html" ) + "b" );

        try
        {
            instance.doDispatchFeature( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            stored = RightHome.findByPrimaryKey( right.getId( ) );
            assertNotNull( stored );
            assertNull( stored.getFeatureGroup( ) );
            assertEquals( right.getOrder( ), stored.getOrder( ) );
        }
    }

    public void testDoDispatchFeatureNoToken( ) throws AccessDeniedException
    {
        Right stored = RightHome.findByPrimaryKey( right.getId( ) );
        assertNotNull( stored );
        assertNull( stored.getFeatureGroup( ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "right_id", right.getId( ) );
        request.addParameter( "group_name", featureGroup.getId( ) );
        request.addParameter( "order_id", Integer.toString( stored.getOrder( ) + 1 ) );

        try
        {
            instance.doDispatchFeature( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            stored = RightHome.findByPrimaryKey( right.getId( ) );
            assertNotNull( stored );
            assertNull( stored.getFeatureGroup( ) );
            assertEquals( right.getOrder( ), stored.getOrder( ) );
        }
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
        assertNotNull( instance.getModifyGroup( request ) );
    }

    /**
     * Test of doCreateGroup method, of class fr.paris.lutece.portal.web.features.FeaturesGroupJspBean.
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
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, AdminDashboardJspBean.TEMPLATE_MANAGE_DASHBOARDS ) );

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
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "admin/features/create_group.html" )
                + "b" );

        try
        {
            instance.doCreateGroup( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
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
        catch( AccessDeniedException e )
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
     * 
     * @throws AccessDeniedException
     */
    public void testDoModifyGroup( ) throws AccessDeniedException
    {
        String strGroupName = getRandomName( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "group_id", featureGroup.getId( ) );
        request.addParameter( "group_name", strGroupName );
        request.addParameter( "group_description", strGroupName );
        request.addParameter( "group_order", Integer.toString( featureGroup.getOrder( ) + 1 ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, AdminDashboardJspBean.TEMPLATE_MANAGE_DASHBOARDS ) );

        instance.doModifyGroup( request );
        FeatureGroup group = FeatureGroupHome.findByPrimaryKey( featureGroup.getId( ) );
        assertNotNull( group );
        assertEquals( featureGroup.getId( ), group.getId( ) );
        assertEquals( strGroupName, group.getLabelKey( ) );
        assertEquals( strGroupName, group.getDescriptionKey( ) );
        assertEquals( featureGroup.getOrder( ) + 1, group.getOrder( ) );
    }

    public void testDoModifyGroupInvalidToken( ) throws AccessDeniedException
    {
        String strGroupName = getRandomName( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "group_id", featureGroup.getId( ) );
        request.addParameter( "group_name", strGroupName );
        request.addParameter( "group_description", strGroupName );
        request.addParameter( "group_order", Integer.toString( featureGroup.getOrder( ) + 1 ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "admin/features/modify_group.html" )
                + "b" );

        try
        {
            instance.doModifyGroup( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            FeatureGroup group = FeatureGroupHome.findByPrimaryKey( featureGroup.getId( ) );
            assertNotNull( group );
            assertEquals( featureGroup.getId( ), group.getId( ) );
            assertEquals( featureGroup.getLabelKey( ), group.getLabelKey( ) );
            assertEquals( featureGroup.getDescriptionKey( ), group.getDescriptionKey( ) );
            assertEquals( featureGroup.getOrder( ), group.getOrder( ) );
        }
    }

    public void testDoModifyGroupNoToken( ) throws AccessDeniedException
    {
        String strGroupName = getRandomName( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "group_id", featureGroup.getId( ) );
        request.addParameter( "group_name", strGroupName );
        request.addParameter( "group_description", strGroupName );
        request.addParameter( "group_order", Integer.toString( featureGroup.getOrder( ) + 1 ) );

        try
        {
            instance.doModifyGroup( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            FeatureGroup group = FeatureGroupHome.findByPrimaryKey( featureGroup.getId( ) );
            assertNotNull( group );
            assertEquals( featureGroup.getId( ), group.getId( ) );
            assertEquals( featureGroup.getLabelKey( ), group.getLabelKey( ) );
            assertEquals( featureGroup.getDescriptionKey( ), group.getDescriptionKey( ) );
            assertEquals( featureGroup.getOrder( ), group.getOrder( ) );
        }
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
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertTrue( message.getRequestParameters( ).containsKey( SecurityTokenService.PARAMETER_TOKEN ) );
    }

    /**
     * Test of doRemoveGroup method, of class fr.paris.lutece.portal.web.features.FeaturesGroupJspBean.
     * 
     * @throws AccessDeniedException
     */
    public void testDoRemoveGroup( ) throws AccessDeniedException
    {
        assertNotNull( FeatureGroupHome.findByPrimaryKey( featureGroup.getId( ) ) );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_GROUP_ID, featureGroup.getId( ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, AdminDashboardJspBean.TEMPLATE_MANAGE_DASHBOARDS ) );

        instance.doRemoveGroup( request );
        assertNull( FeatureGroupHome.findByPrimaryKey( featureGroup.getId( ) ) );
    }

    public void testDoRemoveGroupInvalidToken( ) throws AccessDeniedException
    {
        assertNotNull( FeatureGroupHome.findByPrimaryKey( featureGroup.getId( ) ) );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_GROUP_ID, featureGroup.getId( ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/features/DoRemoveGroup.jsp" ) + "b" );

        try
        {
            instance.doRemoveGroup( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertNotNull( FeatureGroupHome.findByPrimaryKey( featureGroup.getId( ) ) );
        }
    }

    public void testDoRemoveGroupNoToken( ) throws AccessDeniedException
    {
        assertNotNull( FeatureGroupHome.findByPrimaryKey( featureGroup.getId( ) ) );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_GROUP_ID, featureGroup.getId( ) );

        try
        {
            instance.doRemoveGroup( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertNotNull( FeatureGroupHome.findByPrimaryKey( featureGroup.getId( ) ) );
        }
    }

    public void testDoDispatchFeatureGroup( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "group_id", featureGroup.getId( ) );
        request.addParameter( "order_id", Integer.toString( featureGroup.getOrder( ) + 1 ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, AdminDashboardJspBean.TEMPLATE_MANAGE_DASHBOARDS ) );

        instance.doDispatchFeatureGroup( request );
        FeatureGroup stored = FeatureGroupHome.findByPrimaryKey( featureGroup.getId( ) );
        assertNotNull( stored );
        assertEquals( featureGroup.getOrder( ) + 1, stored.getOrder( ) );

    }

    public void testDoDispatchFeatureGroupInvalidToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "group_id", featureGroup.getId( ) );
        request.addParameter( "order_id", Integer.toString( featureGroup.getOrder( ) + 1 ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "admin/features/manage_groups.html" )
                + "b" );

        try
        {
            instance.doDispatchFeatureGroup( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            FeatureGroup stored = FeatureGroupHome.findByPrimaryKey( featureGroup.getId( ) );
            assertNotNull( stored );
            assertEquals( featureGroup.getOrder( ), stored.getOrder( ) );
        }
    }

    public void testDoDispatchFeatureGroupNoToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "group_id", featureGroup.getId( ) );
        request.addParameter( "order_id", Integer.toString( featureGroup.getOrder( ) + 1 ) );

        try
        {
            instance.doDispatchFeatureGroup( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            FeatureGroup stored = FeatureGroupHome.findByPrimaryKey( featureGroup.getId( ) );
            assertNotNull( stored );
            assertEquals( featureGroup.getOrder( ), stored.getOrder( ) );
        }
    }

    public void testDoReinitFeatures( ) throws AccessDeniedException
    {
        right.setFeatureGroup( featureGroup.getId( ) );
        RightHome.update( right );
        right.setOrder( 100 );
        ( (IRightDAO) SpringContextService.getBean( "rightDAO" ) ).store( right );

        Right stored = RightHome.findByPrimaryKey( right.getId( ) );
        assertNotNull( stored );
        assertEquals( 100, stored.getOrder( ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "group_id", featureGroup.getId( ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, AdminDashboardJspBean.TEMPLATE_MANAGE_DASHBOARDS ) );

        instance.doReinitFeatures( request );
        stored = RightHome.findByPrimaryKey( right.getId( ) );
        assertNotNull( stored );
        assertEquals( 1, stored.getOrder( ) );
    }

    public void testDoReinitFeaturesInvalidToken( ) throws AccessDeniedException
    {
        right.setFeatureGroup( featureGroup.getId( ) );
        RightHome.update( right );
        right.setOrder( 100 );
        ( (IRightDAO) SpringContextService.getBean( "rightDAO" ) ).store( right );

        Right stored = RightHome.findByPrimaryKey( right.getId( ) );
        assertNotNull( stored );
        assertEquals( 100, stored.getOrder( ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "group_id", featureGroup.getId( ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/features/dispatch_features.html" ) + "b" );

        try
        {
            instance.doReinitFeatures( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            stored = RightHome.findByPrimaryKey( right.getId( ) );
            assertNotNull( stored );
            assertEquals( 100, stored.getOrder( ) );
        }
    }

    public void testDoReinitFeaturesNoToken( ) throws AccessDeniedException
    {
        right.setFeatureGroup( featureGroup.getId( ) );
        RightHome.update( right );
        right.setOrder( 100 );
        ( (IRightDAO) SpringContextService.getBean( "rightDAO" ) ).store( right );

        Right stored = RightHome.findByPrimaryKey( right.getId( ) );
        assertNotNull( stored );
        assertEquals( 100, stored.getOrder( ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "group_id", featureGroup.getId( ) );

        try
        {
            instance.doReinitFeatures( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            stored = RightHome.findByPrimaryKey( right.getId( ) );
            assertNotNull( stored );
            assertEquals( 100, stored.getOrder( ) );
        }
    }
}
