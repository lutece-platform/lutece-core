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

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.right.FeatureGroupHome;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.right.RightHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.PasswordResetException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.Utils;

public class ExternalFeaturesJspBeanTest extends LuteceTestCase
{

    private static final String PARAMETER_ID_EXTERNAL_FEAUTRE = "external_feature_id";
    private static final String TEST_EXTERNAL_FEATURE_ID = "CORE_TEST";

    // To insert moke external feature
    private static final String RIGHT_ID = "CORE_TEST";
    private static final String NAMEKEY = "nameKeyTest";
    private static final String DESCRIPTIONKEY = "descriptionKeyTest";
    private static final int LEVEL = 0;
    private static final String URL = "urlTest";
    private static final String PLUGINNAME = "pluginNameTest";
    private static final String FEATUREGROUP = "featureGroupTest";
    private static final String ICONURL = "iconUrlTest";
    private static final boolean IS_EXTERNAL_FEATURE = true;
    private Right _right;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        _right = new Right( );
        _right.setId( RIGHT_ID );
        _right.setNameKey( NAMEKEY );
        _right.setDescriptionKey( DESCRIPTIONKEY );
        _right.setLevel( LEVEL );
        _right.setUrl( URL );
        _right.setPluginName( PLUGINNAME );
        _right.setFeatureGroup( FEATUREGROUP );
        _right.setIconUrl( ICONURL );
        _right.setExternalFeature( IS_EXTERNAL_FEATURE );
        RightHome.create( _right );
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        RightHome.remove( RIGHT_ID );
        super.tearDown( );
    }

    /**
     * Test of getCreateExternalFeature method, of class ExternalFeaturesJspBean.
     */
    @Test
    public void testGetCreateExternalFeature( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT );

        ExternalFeaturesJspBean instance = new ExternalFeaturesJspBean( );
        instance.init( request, ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT );
        assertNotNull( instance.getCreateExternalFeature( request ) );
    }

    /**
     * Test of getModifyExternalFeature method, of class ExternalFeaturesJspBean.
     */
    @Test
    public void testGetModifyExternalFeature( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_ID_EXTERNAL_FEAUTRE, TEST_EXTERNAL_FEATURE_ID );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT );

        ExternalFeaturesJspBean instance = new ExternalFeaturesJspBean( );
        instance.init( request, ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT );
        assertNotNull( instance.getModifyExternalFeature( request ) );
    }

    /**
     * Test of doModifyExternalFeature method, of class ExternalFeaturesJspBean.
     * 
     * @throws AccessDeniedException
     */
    @Test
    public void testDoModifyExternalFeature( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_ID_EXTERNAL_FEAUTRE, TEST_EXTERNAL_FEATURE_ID );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT );

        ExternalFeaturesJspBean instance = new ExternalFeaturesJspBean( );
        instance.init( request, ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT );
        String strRandom = getRandomName( );
        request.setParameter( "id", _right.getId( ) );
        request.setParameter( "nameKey", strRandom );
        request.setParameter( "descriptionKey", strRandom );
        request.setParameter( "level_id", "0" );
        request.setParameter( "url", strRandom );
        request.setParameter( "pluginName", strRandom );
        request.setParameter( "feature_group_id", FeatureGroupHome.getFeatureGroupsList( ).get( 0 ).getId( ) );
        request.setParameter( "iconUrl", strRandom );
        request.setParameter( "externalFeature", "false" );
        request.setParameter( "documentationUrl", strRandom );

        instance.getModifyExternalFeature( request );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/features/modify_external_feature.html" ) );
        instance.doModifyExternalFeature( request );

        Right right = RightHome.findByPrimaryKey( _right.getId( ) );
        assertNotNull( right );
        assertEquals( strRandom, right.getNameKey( ) );
        assertEquals( strRandom, right.getDescriptionKey( ) );
        assertEquals( 0, right.getLevel( ) );
        assertEquals( strRandom, right.getUrl( ) );
        assertEquals( strRandom, right.getPluginName( ) );
        assertEquals( FeatureGroupHome.getFeatureGroupsList( ).get( 0 ).getId( ), right.getFeatureGroup( ) );
        assertEquals( strRandom, right.getIconUrl( ) );
        assertTrue( right.isExternalFeature( ) );
        assertEquals( strRandom, right.getDocumentationUrl( ) );
    }

    public void testDoModifyExternalFeatureInvalidToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_ID_EXTERNAL_FEAUTRE, TEST_EXTERNAL_FEATURE_ID );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT );

        ExternalFeaturesJspBean instance = new ExternalFeaturesJspBean( );
        instance.init( request, ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT );
        String strRandom = getRandomName( );
        request.setParameter( "id", _right.getId( ) );
        request.setParameter( "nameKey", strRandom );
        request.setParameter( "descriptionKey", strRandom );
        request.setParameter( "level_id", "0" );
        request.setParameter( "url", strRandom );
        request.setParameter( "pluginName", strRandom );
        request.setParameter( "feature_group_id", FeatureGroupHome.getFeatureGroupsList( ).get( 0 ).getId( ) );
        request.setParameter( "iconUrl", strRandom );
        request.setParameter( "externalFeature", "false" );
        request.setParameter( "documentationUrl", strRandom );

        instance.getModifyExternalFeature( request );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/features/modify_external_feature.html" ) + "b" );
        try
        {
            instance.doModifyExternalFeature( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            Right right = RightHome.findByPrimaryKey( _right.getId( ) );
            assertNotNull( right );
            assertEquals( _right.getNameKey( ), right.getNameKey( ) );
            assertEquals( _right.getDescriptionKey( ), right.getDescriptionKey( ) );
            assertEquals( _right.getLevel( ), right.getLevel( ) );
            assertEquals( _right.getUrl( ), right.getUrl( ) );
            assertEquals( _right.getPluginName( ), right.getPluginName( ) );
            assertEquals( _right.getFeatureGroup( ), right.getFeatureGroup( ) );
            assertEquals( _right.getIconUrl( ), right.getIconUrl( ) );
            assertTrue( right.isExternalFeature( ) );
            assertEquals( _right.getDocumentationUrl( ), right.getDocumentationUrl( ) );
        }
    }

    public void testDoModifyExternalFeatureNoToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_ID_EXTERNAL_FEAUTRE, TEST_EXTERNAL_FEATURE_ID );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT );

        ExternalFeaturesJspBean instance = new ExternalFeaturesJspBean( );
        instance.init( request, ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT );
        String strRandom = getRandomName( );
        request.setParameter( "id", _right.getId( ) );
        request.setParameter( "nameKey", strRandom );
        request.setParameter( "descriptionKey", strRandom );
        request.setParameter( "level_id", "0" );
        request.setParameter( "url", strRandom );
        request.setParameter( "pluginName", strRandom );
        request.setParameter( "feature_group_id", FeatureGroupHome.getFeatureGroupsList( ).get( 0 ).getId( ) );
        request.setParameter( "iconUrl", strRandom );
        request.setParameter( "externalFeature", "false" );
        request.setParameter( "documentationUrl", strRandom );

        instance.getModifyExternalFeature( request );

        try
        {
            instance.doModifyExternalFeature( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            Right right = RightHome.findByPrimaryKey( _right.getId( ) );
            assertNotNull( right );
            assertEquals( _right.getNameKey( ), right.getNameKey( ) );
            assertEquals( _right.getDescriptionKey( ), right.getDescriptionKey( ) );
            assertEquals( _right.getLevel( ), right.getLevel( ) );
            assertEquals( _right.getUrl( ), right.getUrl( ) );
            assertEquals( _right.getPluginName( ), right.getPluginName( ) );
            assertEquals( _right.getFeatureGroup( ), right.getFeatureGroup( ) );
            assertEquals( _right.getIconUrl( ), right.getIconUrl( ) );
            assertTrue( right.isExternalFeature( ) );
            assertEquals( _right.getDocumentationUrl( ), right.getDocumentationUrl( ) );
        }
    }

    /**
     * Test of doCreateExternalFeature method, of class ExternalFeaturesJspBean.
     * 
     * @throws AccessDeniedException
     * @throws PasswordResetException
     */
    @Test
    public void testDoCreateExternalFeature( ) throws PasswordResetException, AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_ID_EXTERNAL_FEAUTRE, TEST_EXTERNAL_FEATURE_ID );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT );

        ExternalFeaturesJspBean instance = new ExternalFeaturesJspBean( );
        instance.init( request, ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT );
        String strRandom = getRandomName( );
        request.setParameter( "id", strRandom );
        request.setParameter( "nameKey", strRandom );
        request.setParameter( "descriptionKey", strRandom );
        request.setParameter( "level_id", "0" );
        request.setParameter( "url", strRandom );
        request.setParameter( "pluginName", strRandom );
        request.setParameter( "feature_group_id", FeatureGroupHome.getFeatureGroupsList( ).get( 0 ).getId( ) );
        request.setParameter( "iconUrl", strRandom );
        request.setParameter( "externalFeature", "false" );
        request.setParameter( "documentationUrl", strRandom );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/features/create_external_feature.html" ) );

        instance.doCreateExternalFeature( request );

        try
        {
            Right right = RightHome.findByPrimaryKey( strRandom );
            assertNotNull( right );
            assertEquals( strRandom, right.getNameKey( ) );
            assertEquals( strRandom, right.getDescriptionKey( ) );
            assertEquals( 0, right.getLevel( ) );
            assertEquals( strRandom, right.getUrl( ) );
            assertEquals( strRandom, right.getPluginName( ) );
            assertEquals( FeatureGroupHome.getFeatureGroupsList( ).get( 0 ).getId( ), right.getFeatureGroup( ) );
            assertEquals( strRandom, right.getIconUrl( ) );
            assertTrue( right.isExternalFeature( ) );
            assertEquals( strRandom, right.getDocumentationUrl( ) );
        }
        finally
        {
            RightHome.remove( strRandom );
        }
    }

    private String getRandomName( )
    {
        Random rand = new SecureRandom( );
        BigInteger bigInt = new BigInteger( 128, rand );
        return "junit" + bigInt.toString( 36 );
    }

    public void testDoCreateExternalFeatureInvalidToken( ) throws PasswordResetException, AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_ID_EXTERNAL_FEAUTRE, TEST_EXTERNAL_FEATURE_ID );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT );

        ExternalFeaturesJspBean instance = new ExternalFeaturesJspBean( );
        instance.init( request, ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT );
        String strRandom = getRandomName( );
        request.setParameter( "id", strRandom );
        request.setParameter( "nameKey", strRandom );
        request.setParameter( "descriptionKey", strRandom );
        request.setParameter( "level_id", "0" );
        request.setParameter( "url", strRandom );
        request.setParameter( "pluginName", strRandom );
        request.setParameter( "feature_group_id", FeatureGroupHome.getFeatureGroupsList( ).get( 0 ).getId( ) );
        request.setParameter( "iconUrl", strRandom );
        request.setParameter( "externalFeature", "false" );
        request.setParameter( "documentationUrl", strRandom );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/features/create_external_feature.html" ) + "b" );

        try
        {
            instance.doCreateExternalFeature( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            Right right = RightHome.findByPrimaryKey( strRandom );
            assertNull( right );
        }
        finally
        {
            RightHome.remove( strRandom );
        }
    }

    public void testDoCreateExternalFeatureNoToken( ) throws PasswordResetException, AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_ID_EXTERNAL_FEAUTRE, TEST_EXTERNAL_FEATURE_ID );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT );

        ExternalFeaturesJspBean instance = new ExternalFeaturesJspBean( );
        instance.init( request, ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT );
        String strRandom = getRandomName( );
        request.setParameter( "id", strRandom );
        request.setParameter( "nameKey", strRandom );
        request.setParameter( "descriptionKey", strRandom );
        request.setParameter( "level_id", "0" );
        request.setParameter( "url", strRandom );
        request.setParameter( "pluginName", strRandom );
        request.setParameter( "feature_group_id", FeatureGroupHome.getFeatureGroupsList( ).get( 0 ).getId( ) );
        request.setParameter( "iconUrl", strRandom );
        request.setParameter( "externalFeature", "false" );
        request.setParameter( "documentationUrl", strRandom );

        try
        {
            instance.doCreateExternalFeature( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            Right right = RightHome.findByPrimaryKey( strRandom );
            assertNull( right );
        }
        finally
        {
            RightHome.remove( strRandom );
        }
    }

    /**
     * Test of getRemoveExternalFeature method, of class ExternalFeaturesJspBean.
     */
    @Test
    public void testGetRemoveExternalFeature( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_ID_EXTERNAL_FEAUTRE, TEST_EXTERNAL_FEATURE_ID );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT );

        ExternalFeaturesJspBean instance = new ExternalFeaturesJspBean( );
        instance.init( request, ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT );
        instance.getRemoveExternalFeature( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertTrue( message.getRequestParameters( ).containsKey( SecurityTokenService.PARAMETER_TOKEN ) );
    }

    /**
     * Test of doRemoveExternalFeature method, of class ExternalFeaturesJspBean.
     * 
     * @throws AccessDeniedException
     * @throws PasswordResetException
     */
    @Test
    public void testDoRemoveExternalFeature( ) throws PasswordResetException, AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_ID_EXTERNAL_FEAUTRE, TEST_EXTERNAL_FEATURE_ID );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT );

        ExternalFeaturesJspBean instance = new ExternalFeaturesJspBean( );
        instance.init( request, ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT );
        instance.getRemoveExternalFeature( request );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/features/DoRemoveExternalFeature.jsp" ) );
        instance.doRemoveExternalFeature( request );

        Right right = RightHome.findByPrimaryKey( TEST_EXTERNAL_FEATURE_ID );
        assertNull( right );
    }

    public void testDoRemoveExternalFeatureInvalidToken( ) throws PasswordResetException, AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_ID_EXTERNAL_FEAUTRE, TEST_EXTERNAL_FEATURE_ID );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT );

        ExternalFeaturesJspBean instance = new ExternalFeaturesJspBean( );
        instance.init( request, ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT );
        instance.getRemoveExternalFeature( request );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/features/DoRemoveExternalFeature.jsp" ) + "b" );
        try
        {
            instance.doRemoveExternalFeature( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            Right right = RightHome.findByPrimaryKey( TEST_EXTERNAL_FEATURE_ID );
            assertNotNull( right );
        }
    }

    public void testDoRemoveExternalFeatureNoToken( ) throws PasswordResetException, AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_ID_EXTERNAL_FEAUTRE, TEST_EXTERNAL_FEATURE_ID );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT );

        ExternalFeaturesJspBean instance = new ExternalFeaturesJspBean( );
        instance.init( request, ExternalFeaturesJspBean.RIGHT_EXTERNAL_FEATURES_MANAGEMENT );
        instance.getRemoveExternalFeature( request );

        try
        {
            instance.doRemoveExternalFeature( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            Right right = RightHome.findByPrimaryKey( TEST_EXTERNAL_FEATURE_ID );
            assertNotNull( right );
        }
    }
}
