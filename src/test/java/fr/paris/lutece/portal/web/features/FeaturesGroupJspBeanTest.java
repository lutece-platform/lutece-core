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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.web.pluginaction.DefaultPluginActionResult;
import fr.paris.lutece.portal.business.right.FeatureGroup;
import fr.paris.lutece.portal.business.right.FeatureGroupHome;
import fr.paris.lutece.portal.business.right.IRightDAO;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.right.RightHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.ISecurityTokenService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.portal.web.dashboard.AdminDashboardJspBean;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.inject.Inject;

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
    private @Inject IRightDAO rightDAO;
    private @Inject ISecurityTokenService _securityTokenService;

    
    @BeforeEach
    protected void setUp( ) throws Exception
    {
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

    @AfterEach
    protected void tearDown( ) throws Exception
    {
        RightHome.remove( right.getId( ) );
        FeatureGroupHome.remove( featureGroup.getId( ) );
    }

    /**
     * Test of doDispatchFeature method, of class fr.paris.lutece.portal.web.features.FeaturesGroupJspBean.
     * 
     * @throws AccessDeniedException
     */
    @Test
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
                _securityTokenService.getToken( request, AdminDashboardJspBean.TEMPLATE_MANAGE_DASHBOARDS ) );

        instance.doDispatchFeature( request );
        stored = RightHome.findByPrimaryKey( right.getId( ) );
        assertNotNull( stored );
        assertEquals( featureGroup.getId( ), stored.getFeatureGroup( ) );
        assertEquals( stored.getOrder( ) + 1, right.getOrder( ) );
    }
    @Test
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
                _securityTokenService.getToken( request, "admin/features/dispatch_features.html" ) + "b" );

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
    @Test
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
    @Test
    public void testGetCreateGroup( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), FeaturesGroupJspBean.RIGHT_FEATURES_MANAGEMENT );

        instance.init( request, FeaturesGroupJspBean.RIGHT_FEATURES_MANAGEMENT );
        assertNotNull( instance.getCreateGroup( request ) );
    }

    /**
     * Test of getModifyGroup method, of class fr.paris.lutece.portal.web.features.FeaturesGroupJspBean.
     */
    @Test
    public void testGetModifyGroup( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_GROUP_ID, TEST_GROUP_ID );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), FeaturesGroupJspBean.RIGHT_FEATURES_MANAGEMENT );

        instance.init( request, FeaturesGroupJspBean.RIGHT_FEATURES_MANAGEMENT );
        assertNotNull( instance.getModifyGroup( request ).getHtmlContent( ) );
    }

    /**
     * An unknown group identifier gives the URL of the feature groups list, no page
     */
    @Test
    public void testGetModifyGroupUnknownId( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_GROUP_ID, "nope" );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), FeaturesGroupJspBean.RIGHT_FEATURES_MANAGEMENT );

        instance.init( request, FeaturesGroupJspBean.RIGHT_FEATURES_MANAGEMENT );
        DefaultPluginActionResult result = instance.getModifyGroup( request );
        assertNull( result.getHtmlContent( ) );
        assertTrue( result.getRedirect( ).contains( "AdminTechnicalMenu.jsp" ) );
    }

    /**
     * Test of doCreateGroup method, of class fr.paris.lutece.portal.web.features.FeaturesGroupJspBean.
     * 
     * @throws AccessDeniedException
     */
    @Test
    public void testDoCreateGroup( ) throws AccessDeniedException
    {
        String strGroupName = getRandomName( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "group_id", strGroupName );
        request.addParameter( "group_name", strGroupName );
        request.addParameter( "group_description", strGroupName );
        request.addParameter( "group_order", "1" );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, AdminDashboardJspBean.TEMPLATE_MANAGE_DASHBOARDS ) );

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
    @Test
    public void testDoCreateGroupWithAnExistingIdIsRefused( ) throws AccessDeniedException
    {
        String strGroupName = getRandomName( );
        FeatureGroup existing = new FeatureGroup( );
        existing.setId( strGroupName );
        existing.setLabelKey( strGroupName );
        existing.setDescriptionKey( strGroupName );
        FeatureGroupHome.create( existing );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "group_id", strGroupName );
        request.addParameter( "group_name", "other" );
        request.addParameter( "group_description", "other" );
        request.addParameter( "group_order", "1" );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, AdminDashboardJspBean.TEMPLATE_MANAGE_DASHBOARDS ) );

        try
        {
            instance.doCreateGroup( request );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message, "a message tells the identifier is already used" );
            assertEquals( AdminMessage.TYPE_STOP, message.getType( ) );
            assertEquals( strGroupName, FeatureGroupHome.findByPrimaryKey( strGroupName ).getLabelKey( ) );
        }
        finally
        {
            FeatureGroupHome.remove( strGroupName );
        }
    }
    @Test
    public void testDoCreateGroupInvalidToken( ) throws AccessDeniedException
    {
        String strGroupName = getRandomName( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "group_id", strGroupName );
        request.addParameter( "group_name", strGroupName );
        request.addParameter( "group_description", strGroupName );
        request.addParameter( "group_order", "1" );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, "admin/features/create_group.html" ) + "b" );

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
    @Test
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
    @Test
    public void testDoModifyGroup( ) throws AccessDeniedException
    {
        String strGroupName = getRandomName( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "group_id", featureGroup.getId( ) );
        request.addParameter( "group_name", strGroupName );
        request.addParameter( "group_description", strGroupName );
        request.addParameter( "group_order", Integer.toString( featureGroup.getOrder( ) + 1 ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, AdminDashboardJspBean.TEMPLATE_MANAGE_DASHBOARDS ) );

        instance.doModifyGroup( request );
        FeatureGroup group = FeatureGroupHome.findByPrimaryKey( featureGroup.getId( ) );
        assertNotNull( group );
        assertEquals( featureGroup.getId( ), group.getId( ) );
        assertEquals( strGroupName, group.getLabelKey( ) );
        assertEquals( strGroupName, group.getDescriptionKey( ) );
        assertEquals( featureGroup.getOrder( ) + 1, group.getOrder( ) );
    }
    @Test
    public void testDoModifyGroupInvalidToken( ) throws AccessDeniedException
    {
        String strGroupName = getRandomName( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "group_id", featureGroup.getId( ) );
        request.addParameter( "group_name", strGroupName );
        request.addParameter( "group_description", strGroupName );
        request.addParameter( "group_order", Integer.toString( featureGroup.getOrder( ) + 1 ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, "admin/features/modify_group.html" ) + "b" );

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
    @Test
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
    @Test
    public void testGetRemoveGroup( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_GROUP_ID, featureGroup.getId( ) );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), FeaturesGroupJspBean.RIGHT_FEATURES_MANAGEMENT );

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
    @Test
    public void testDoRemoveGroup( ) throws AccessDeniedException
    {
        assertNotNull( FeatureGroupHome.findByPrimaryKey( featureGroup.getId( ) ) );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_GROUP_ID, featureGroup.getId( ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, AdminDashboardJspBean.TEMPLATE_MANAGE_DASHBOARDS ) );

        instance.doRemoveGroup( request );
        assertNull( FeatureGroupHome.findByPrimaryKey( featureGroup.getId( ) ) );
    }
    @Test
    public void testDoRemoveGroupInvalidToken( ) throws AccessDeniedException
    {
        assertNotNull( FeatureGroupHome.findByPrimaryKey( featureGroup.getId( ) ) );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_GROUP_ID, featureGroup.getId( ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, "jsp/admin/features/DoRemoveGroup.jsp" ) + "b" );

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
    @Test
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
    @Test
    public void testDoDispatchFeatureGroup( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "group_id", featureGroup.getId( ) );
        request.addParameter( "order_id", Integer.toString( featureGroup.getOrder( ) + 1 ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, AdminDashboardJspBean.TEMPLATE_MANAGE_DASHBOARDS ) );

        instance.doDispatchFeatureGroup( request );
        FeatureGroup stored = FeatureGroupHome.findByPrimaryKey( featureGroup.getId( ) );
        assertNotNull( stored );
        assertEquals( featureGroup.getOrder( ) + 1, stored.getOrder( ) );

    }
    @Test
    public void testDoDispatchFeatureGroupInvalidToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "group_id", featureGroup.getId( ) );
        request.addParameter( "order_id", Integer.toString( featureGroup.getOrder( ) + 1 ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, "admin/features/manage_groups.html" ) + "b" );

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
    @Test
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
    
    @Test
    public void testDoReinitFeatures( ) throws AccessDeniedException
    {
        right.setFeatureGroup( featureGroup.getId( ) );
        RightHome.update( right );
        right.setOrder( 100 );
        rightDAO.store( right );

        Right stored = RightHome.findByPrimaryKey( right.getId( ) );
        assertNotNull( stored );
        assertEquals( 100, stored.getOrder( ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "group_id", featureGroup.getId( ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, AdminDashboardJspBean.TEMPLATE_MANAGE_DASHBOARDS ) );

        instance.doReinitFeatures( request );
        stored = RightHome.findByPrimaryKey( right.getId( ) );
        assertNotNull( stored );
        assertEquals( 1, stored.getOrder( ) );
    }
    @Test
    public void testDoReinitFeaturesInvalidToken( ) throws AccessDeniedException
    {
        right.setFeatureGroup( featureGroup.getId( ) );
        RightHome.update( right );
        right.setOrder( 100 );
        rightDAO.store( right );

        Right stored = RightHome.findByPrimaryKey( right.getId( ) );
        assertNotNull( stored );
        assertEquals( 100, stored.getOrder( ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "group_id", featureGroup.getId( ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, "admin/features/dispatch_features.html" ) + "b" );

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
    @Test
    public void testDoReinitFeaturesNoToken( ) throws AccessDeniedException
    {
        right.setFeatureGroup( featureGroup.getId( ) );
        RightHome.update( right );
        right.setOrder( 100 );
        rightDAO.store( right );

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

    /**
     * The error message of the creation of a group leads back to the creation form when there is no page to go back to
     */
    @Test
    public void testDoCreateGroupMissingFieldReturnsToTheCreationScreen( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_GROUP_ID, "" );
        request.addParameter( "group_name", "" );
        request.addParameter( "group_description", "" );

        instance.doCreateGroup( request );

        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertTrue( message.getBackUrl( ).endsWith( "jsp/admin/features/CreateGroup.jsp" ), message.getBackUrl( ) );
    }

    /**
     * A group created without (or with a non numeric) order keeps the order given at its creation (last position) instead of
     * failing after the group has been inserted.
     *
     * @throws AccessDeniedException
     */
    @Test
    public void testDoCreateGroupWithoutOrderKeepsTheLastPosition( ) throws AccessDeniedException
    {
        for ( String strOrder : new String [ ] { null, "", "abc" } )
        {
            String strGroupName = getRandomName( );
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request.addParameter( "group_id", strGroupName );
            request.addParameter( "group_name", strGroupName );
            request.addParameter( "group_description", strGroupName );
            if ( strOrder != null )
            {
                request.addParameter( "group_order", strOrder );
            }
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                    _securityTokenService.getToken( request, AdminDashboardJspBean.TEMPLATE_MANAGE_DASHBOARDS ) );
            try
            {
                instance.doCreateGroup( request );
                FeatureGroup group = FeatureGroupHome.findByPrimaryKey( strGroupName );
                assertNotNull( group );
                assertEquals( FeatureGroupHome.getFeatureGroupsCount( ), group.getOrder( ), "order " + strOrder );
            }
            finally
            {
                FeatureGroupHome.remove( strGroupName );
            }
        }
    }

    /**
     * A modification without (or with a non numeric) order keeps the current order of the group.
     *
     * @throws AccessDeniedException
     */
    @Test
    public void testDoModifyGroupWithoutOrderKeepsTheOrder( ) throws AccessDeniedException
    {
        String strGroupName = getRandomName( );
        FeatureGroup group = new FeatureGroup( );
        group.setId( strGroupName );
        group.setLabelKey( strGroupName );
        group.setDescriptionKey( strGroupName );
        FeatureGroupHome.create( group );
        int nOrder = FeatureGroupHome.findByPrimaryKey( strGroupName ).getOrder( );
        try
        {
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request.addParameter( "group_id", strGroupName );
            request.addParameter( "group_name", strGroupName + "_modified" );
            request.addParameter( "group_description", strGroupName );
            request.addParameter( "group_order", "abc" );
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                    _securityTokenService.getToken( request, AdminDashboardJspBean.TEMPLATE_MANAGE_DASHBOARDS ) );

            instance.doModifyGroup( request );

            FeatureGroup modified = FeatureGroupHome.findByPrimaryKey( strGroupName );
            assertEquals( strGroupName + "_modified", modified.getLabelKey( ) );
            assertEquals( nOrder, modified.getOrder( ) );
        }
        finally
        {
            FeatureGroupHome.remove( strGroupName );
        }
    }
}
