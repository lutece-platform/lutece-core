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
package fr.paris.lutece.portal.web.style;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.portlet.PortletTemplate;
import fr.paris.lutece.portal.business.portlet.PortletTemplateHome;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.rbac.RBACHome;
import fr.paris.lutece.portal.business.rbac.RBACRole;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.portlet.PortletTemplateResourceIdService;
import fr.paris.lutece.portal.util.mvc.binding.BindingResultImpl;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.portal.web.cdi.mvc.ModelsImpl;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.inject.Inject;

/**
 * PortletTemplateJspBean Test Class
 */
public class PortletTemplateJspBeanTest extends LuteceTestCase
{
    private static final String PORTLET_TYPE = "PORTLET_TEMPLATE_JSPBEAN_TEST";
    private static final String DESCRIPTION = "PortletTemplateJspBeanTest template";
    private static final String TEMPLATE_PATH = "skin/plugins/test/portlet_jspbean_test.html";
    private static final String PARAMETER_TEMPLATE_ID = "template_id";
    /** A portlet type declared by the core init script */
    private static final String EXISTING_PORTLET_TYPE = "ALIAS_PORTLET";
    /** A template shipped by the core */
    private static final String EXISTING_TEMPLATE_PATH = "admin/style/manage_portlet_templates.html";
    private static final String MARK_TEMPLATES_USED = "templates_used";

    @Inject
    private PortletTemplateJspBean _bean;

    private MockHttpServletRequest _request;
    private PortletTemplate _template;
    private String _strRoleKey;

    @BeforeEach
    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );

        _template = new PortletTemplate( );
        _template.setPortletTypeId( PORTLET_TYPE );
        _template.setDescription( DESCRIPTION );
        _template.setTemplatePath( TEMPLATE_PATH );
        PortletTemplateHome.create( _template );

        // a role granting the CREATE and MODIFY permissions on every portlet template
        _strRoleKey = "ROLE_" + new BigInteger( 40, new SecureRandom( ) ).toString( 32 );

        for ( String strPermission : new String [ ] {
                PortletTemplateResourceIdService.PERMISSION_CREATE, PortletTemplateResourceIdService.PERMISSION_MODIFY
        } )
        {
            RBAC rbac = new RBAC( );
            rbac.setResourceTypeKey( PortletTemplate.RESOURCE_TYPE );
            rbac.setPermissionKey( strPermission );
            rbac.setResourceId( RBAC.WILDCARD_RESOURCES_ID );
            rbac.setRoleKey( _strRoleKey );
            RBACHome.create( rbac );
        }
        RBACRole role = new RBACRole( );
        role.setKey( _strRoleKey );
        role.setDescription( _strRoleKey );
        Map<String, RBACRole> roles = new HashMap<>( );
        roles.put( _strRoleKey, role );

        AdminUser user = new AdminUser( );
        user.setLocale( Locale.FRENCH );
        user.setRoles( roles );
        _request = new MockHttpServletRequest( );
        AdminUserUtils.registerAdminUserWithRight( _request, user, PortletTemplateJspBean.RIGHT_MANAGE_PORTLET_TEMPLATES );
        _bean.init( _request, PortletTemplateJspBean.RIGHT_MANAGE_PORTLET_TEMPLATES );
    }

    @AfterEach
    @Override
    protected void tearDown( ) throws Exception
    {
        if ( _template != null )
        {
            PortletTemplateHome.remove( _template.getId( ) );
        }

        if ( _strRoleKey != null )
        {
            RBACHome.removeForRoleKey( _strRoleKey );
        }

        super.tearDown( );
    }

    @Test
    public void testGetManagePortletTemplates( )
    {
        Models model = new ModelsImpl( );
        String strHtml = _bean.getManagePortletTemplates( _request, model );

        assertNotNull( strHtml );
        assertTrue( strHtml.contains( DESCRIPTION ), "the page should list the template" );
        assertTrue( strHtml.contains( TEMPLATE_PATH ), "the page should show the template path" );
        assertTrue( strHtml.contains( PORTLET_TYPE ), "the page should show the portlet type of the template" );

        @SuppressWarnings( "unchecked" )
        Map<String, Boolean> mapTemplatesUsed = (Map<String, Boolean>) model.get( MARK_TEMPLATES_USED );
        assertNotNull( mapTemplatesUsed, "the model should tell which templates are used" );
        assertFalse( mapTemplatesUsed.get( _template.getResourceId( ) ), "a template used by no portlet should not be flagged as used" );
    }

    @Test
    public void testGetManagePortletTemplatesFilteredByPortletType( )
    {
        String strOtherDescription = DESCRIPTION + " other type";
        PortletTemplate other = new PortletTemplate( );
        other.setPortletTypeId( EXISTING_PORTLET_TYPE );
        other.setDescription( strOtherDescription );
        other.setTemplatePath( EXISTING_TEMPLATE_PATH );
        PortletTemplateHome.create( other );

        try
        {
            _request.addParameter( "portlet_type_id", PORTLET_TYPE );
            Models model = new ModelsImpl( );
            String strHtml = _bean.getManagePortletTemplates( _request, model );

            assertTrue( strHtml.contains( DESCRIPTION ), "the page should list the templates of the filtered portlet type" );
            assertFalse( strHtml.contains( strOtherDescription ), "the page should not list the templates of another portlet type" );
            assertEquals( PORTLET_TYPE, model.get( "current_portlet_type" ), "the model should hold the current filter" );
            assertNotNull( model.get( "portlet_type_filter_list" ), "the model should offer the portlet types of the filter" );
        }
        finally
        {
            PortletTemplateHome.remove( other.getId( ) );
        }
    }

    @Test
    public void testDoCreatePortletTemplateFileNotFound( )
    {
        String strDescription = DESCRIPTION + " missing file";
        String strMissingPath = "skin/plugins/test/does_not_exist_" + System.nanoTime( ) + ".html";
        Models model = new ModelsImpl( );

        _bean.doCreatePortletTemplate( _request, model, EXISTING_PORTLET_TYPE, strDescription, strMissingPath, new BindingResultImpl( ) );

        AdminMessage message = AdminMessageService.getMessage( _request );
        assertNotNull( message, "an error message should be set when the template file does not exist" );
        assertEquals( AdminMessage.TYPE_STOP, message.getType( ) );
        assertNull( findByDescription( strDescription ), "the template should not be created when its file does not exist" );
    }

    @Test
    public void testDoCreatePortletTemplate( )
    {
        String strDescription = DESCRIPTION + " existing file";
        Models model = new ModelsImpl( );

        _bean.doCreatePortletTemplate( _request, model, EXISTING_PORTLET_TYPE, strDescription, EXISTING_TEMPLATE_PATH, new BindingResultImpl( ) );

        PortletTemplate created = findByDescription( strDescription );

        try
        {
            assertNull( AdminMessageService.getMessage( _request ), "no error message should be set when the template file exists" );
            assertNotNull( created, "the template should be created when its file exists" );
            assertEquals( EXISTING_TEMPLATE_PATH, created.getTemplatePath( ) );
        }
        finally
        {
            if ( created != null )
            {
                PortletTemplateHome.remove( created.getId( ) );
            }
        }
    }

    @Test
    public void testDoModifyPortletTemplateKeepsPath( )
    {
        String strNewDescription = DESCRIPTION + " modified";
        _request.addParameter( PARAMETER_TEMPLATE_ID, String.valueOf( _template.getId( ) ) );
        Models model = new ModelsImpl( );

        _bean.doModifyPortletTemplate( _request, model, EXISTING_PORTLET_TYPE, strNewDescription, new BindingResultImpl( ) );

        assertNull( AdminMessageService.getMessage( _request ), "no error message should be set when modifying a template" );
        PortletTemplate stored = PortletTemplateHome.findByPrimaryKey( _template.getId( ) );
        assertEquals( strNewDescription, stored.getDescription( ), "the description should be updated" );
        assertEquals( EXISTING_PORTLET_TYPE, stored.getPortletTypeId( ), "the portlet type should be updated" );
        assertEquals( TEMPLATE_PATH, stored.getTemplatePath( ), "the template path should never change on modification" );
    }

    private PortletTemplate findByDescription( String strDescription )
    {
        return PortletTemplateHome.findAll( ).stream( ).filter( t -> strDescription.equals( t.getDescription( ) ) ).findFirst( ).orElse( null );
    }

    @Test
    public void testGetCreatePortletTemplate( )
    {
        Models model = new ModelsImpl( );
        String strHtml = _bean.getCreatePortletTemplate( _request, model );

        assertNotNull( strHtml );
        assertTrue( strHtml.contains( "name=\"portlet_type_id\"" ), "the form should offer the portlet types" );
        assertTrue( strHtml.contains( "name=\"template_path\"" ), "the form should ask for the template path" );
    }

    @Test
    public void testGetModifyPortletTemplate( )
    {
        _request.addParameter( PARAMETER_TEMPLATE_ID, String.valueOf( _template.getId( ) ) );
        Models model = new ModelsImpl( );
        String strHtml = _bean.getModifyPortletTemplate( _request, model );

        assertNotNull( strHtml );
        assertEquals( _template.getId( ), ( (PortletTemplate) model.get( "template" ) ).getId( ), "the template should be in the model" );
    }
}
