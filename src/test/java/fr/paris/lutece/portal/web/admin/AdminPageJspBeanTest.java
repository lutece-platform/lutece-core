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
package fr.paris.lutece.portal.web.admin;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.page.PageHome;
import fr.paris.lutece.portal.business.rbac.AdminRole;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.rbac.RBACHome;
import fr.paris.lutece.portal.business.style.PageTemplateHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.PasswordResetException;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.page.IPageService;
import fr.paris.lutece.portal.service.page.PageResourceIdService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.Utils;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

public class AdminPageJspBeanTest extends LuteceTestCase
{
    private String _randomPageName;
    private Page _page;
    private AdminPageJspBean _bean;
    private AdminUser _adminUser;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        _randomPageName = "page" + new SecureRandom( ).nextLong( );
        IPageService pageService = ( IPageService ) SpringContextService.getBean( "pageService" );
        _page = new Page( );
        _page.setParentPageId( PortalService.getRootPageId( ) );
        _page.setPageTemplateId( PageTemplateHome.getPageTemplatesList( ).get( 0 ).getId( ) );
        _page.setName( _randomPageName );
        _page.setDescription( _randomPageName );
        _page.setMetaKeywords( "" );
        _page.setMetaDescription( "" );
        _page.setNodeStatus( 1 );
        pageService.createPage( _page );
        _bean = new AdminPageJspBean( );
        _adminUser = getAdminUser( );
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        IPageService pageService = ( IPageService ) SpringContextService.getBean( "pageService" );
        if ( _page != null )
        {
            try
            {
                Collection<Page> children = PageHome.getChildPages( _page.getId( ) );
                children.stream( ).forEach( page -> pageService.removePage( page.getId( ) ) );
                pageService.removePage( _page.getId( ) );
            }
            finally
            {
            }
        }
        removeUser( _adminUser );
        super.tearDown( );
    }

    private AdminUser getAdminUser( )
    {
        String strRoleKey = "ROLE_" + new BigInteger( 40, new SecureRandom( ) ).toString( 32 );
        RBAC rbac = new RBAC( );
        rbac.setResourceTypeKey( Page.RESOURCE_TYPE );
        rbac.setPermissionKey( PageResourceIdService.PERMISSION_MANAGE );
        rbac.setResourceId( RBAC.WILDCARD_RESOURCES_ID );
        rbac.setRoleKey( strRoleKey );
        RBACHome.create( rbac );
        AdminRole role = new AdminRole( );
        role.setKey( strRoleKey );
        role.setDescription( strRoleKey );
        AdminUser user = new AdminUser( );
        Map<String, AdminRole> roles = new HashMap<>( );
        roles.put( strRoleKey, role );
        user.setRoles( roles );
        return user;
    }

    /**
     * Remove objects persisted with the test user
     * 
     * @param user
     *            the test user
     */
    private void removeUser( AdminUser user )
    {
        Map<String, AdminRole> roles = user.getRoles( );
        for ( String roleKey : roles.keySet( ) )
        {
            RBACHome.removeForRoleKey( roleKey );
        }
    }

    public void testGetRemovePageNoArgs( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        _bean.getRemovePage( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_ERROR, message.getType( ) );
    }

    public void testGetRemovePageNotANumber( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.PAGE_ID, "foo" );
        _bean.getRemovePage( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_ERROR, message.getType( ) );
    }

    public void testGetRemovePageNotExisting( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.PAGE_ID, Integer.toString( 314159265 ) );
        _bean.getRemovePage( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_ERROR, message.getType( ) );
    }

    public void testGetRemovePage( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.PAGE_ID, Integer.toString( _page.getId( ) ) );
        _bean.getRemovePage( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertNotNull( message.getRequestParameters( ).get( SecurityTokenService.PARAMETER_TOKEN ) );
        assertEquals( AdminMessage.TYPE_CONFIRMATION, message.getType( ) );
        ReferenceList listLanguages = I18nService.getAdminLocales( Locale.FRANCE );
        for ( ReferenceItem lang : listLanguages )
        {
            assertTrue( message.getText( new Locale( lang.getCode( ) ) ).contains( _randomPageName ) );
        }
    }

    public void testDoRemovePageNoArgs( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        _bean.doRemovePage( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_ERROR, message.getType( ) );
    }

    public void testDoRemovePageNotANumber( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.PAGE_ID, "foo" );
        _bean.doRemovePage( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_ERROR, message.getType( ) );
    }

    public void testDoRemovePageNotExisting( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.PAGE_ID, Integer.toString( 314159265 ) );
        _bean.doRemovePage( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_ERROR, message.getType( ) );
    }

    public void testDoRemovePageWithChild( ) throws AccessDeniedException
    {
        String childPageName = _randomPageName + "-child";
        Page childPage = null;
        IPageService pageService = ( IPageService ) SpringContextService.getBean( "pageService" );
        try
        {
            childPage = new Page( );
            childPage.setParentPageId( _page.getId( ) );
            childPage.setPageTemplateId( PageTemplateHome.getPageTemplatesList( ).get( 0 ).getId( ) );
            childPage.setName( childPageName );
            pageService.createPage( childPage );
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request.addParameter( Parameters.PAGE_ID, Integer.toString( _page.getId( ) ) );
            _bean.doRemovePage( request );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( AdminMessage.TYPE_STOP, message.getType( ) );
            ReferenceList listLanguages = I18nService.getAdminLocales( Locale.FRANCE );
            for ( ReferenceItem lang : listLanguages )
            {
                assertTrue( message.getText( new Locale( lang.getCode( ) ) ).contains( _randomPageName ) );
            }
        }
        finally
        {
            if ( childPage != null )
            {
                try
                {
                    pageService.removePage( childPage.getId( ) );
                }
                finally
                {
                }
            }
        }
    }

    public void testDoRemovePage( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.PAGE_ID, Integer.toString( _page.getId( ) ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/site/DoRemovePage.jsp" ) );
        _bean.doRemovePage( request );
        assertFalse( PageHome.checkPageExist( _page.getId( ) ) );
    }

    public void testDoRemovePageNoToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.PAGE_ID, Integer.toString( _page.getId( ) ) );
        try
        {
            _bean.doRemovePage( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertTrue( PageHome.checkPageExist( _page.getId( ) ) );
        }
    }

    public void testDoRemovePageInvalidToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.PAGE_ID, Integer.toString( _page.getId( ) ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/site/DoRemovePage.jsp" ) + "b" );
        try
        {
            _bean.doRemovePage( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertTrue( PageHome.checkPageExist( _page.getId( ) ) );
        }
    }

    public void testGetAdminPageBlockProperty( ) throws PasswordResetException, AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, _adminUser, AdminPageJspBean.RIGHT_MANAGE_ADMIN_SITE );
        _bean.init( request, AdminPageJspBean.RIGHT_MANAGE_ADMIN_SITE );
        request.addParameter( "param_block", "2" );
        String html = _bean.getAdminPage( request );
        assertNotNull( html );
    }

    public void testDoModifyPage( ) throws AccessDeniedException, SizeLimitExceededException, FileUploadException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        String descriptionMod = _page.getDescription( ) + "_mod";
        assertEquals( _randomPageName, _page.getDescription( ) );
        Map<String, String[ ]> parameters = new HashMap<>( );
        parameters.put( Parameters.PAGE_ID, new String[ ] { Integer.toString( _page.getId( ) ) } );
        parameters.put( Parameters.PAGE_DESCRIPTION, new String[ ] { descriptionMod } );
        parameters.put( Parameters.PAGE_TEMPLATE_ID, new String[ ] { Integer.toString( _page.getPageTemplateId( ) ) } );
        parameters.put( Parameters.META_KEYWORDS, new String[ ] { _page.getMetaKeywords( ) } );
        parameters.put( Parameters.META_DESCRIPTION, new String[ ] { _page.getMetaDescription( ) } );
        parameters.put( "node_status", new String[ ] { Integer.toString( _page.getNodeStatus( ) ) } );
        parameters.put( Parameters.PAGE_NAME, new String[ ] { _page.getName( ) } );
        parameters.put( Parameters.PARENT_ID, new String[ ] { Integer.toString( _page.getParentPageId( ) ) } );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, new String[ ] { SecurityTokenService.getInstance( )
                .getToken( request, "admin/site/admin_page_block_property.html" ) } );
        _bean.doModifyPage( new MultipartHttpServletRequest( request, Collections.emptyMap( ), parameters ) );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNull( message );
        Page page = PageHome.findByPrimaryKey( _page.getId( ) );
        assertEquals( descriptionMod, page.getDescription( ) );
    }

    public void testDoModifyPagePageDataError( )
            throws AccessDeniedException, SizeLimitExceededException, FileUploadException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Map<String, String[ ]> parameters = new HashMap<>( );
        parameters.put( Parameters.PAGE_ID, new String[ ] { Integer.toString( _page.getId( ) ) } );
        parameters.put( Parameters.PAGE_DESCRIPTION, new String[ ] { _page.getDescription( ) } );
        parameters.put( Parameters.PAGE_TEMPLATE_ID, new String[ ] { Integer.toString( _page.getPageTemplateId( ) ) } );
        parameters.put( Parameters.META_KEYWORDS, new String[ ] { _page.getMetaKeywords( ) } );
        parameters.put( Parameters.META_DESCRIPTION, new String[ ] { _page.getMetaDescription( ) } );
        parameters.put( "node_status", new String[ ] { Integer.toString( _page.getNodeStatus( ) ) } );
        // empty page name parameter
        parameters.put( Parameters.PAGE_NAME, new String[ ] { "" } );
        parameters.put( Parameters.PARENT_ID, new String[ ] { Integer.toString( _page.getParentPageId( ) ) } );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, new String[ ] { SecurityTokenService.getInstance( )
                .getToken( request, "admin/site/admin_page_block_property.html" ) } );
        _bean.doModifyPage( new MultipartHttpServletRequest( request, Collections.emptyMap( ), parameters ) );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_STOP, message.getType( ) );
        Page page = PageHome.findByPrimaryKey( _page.getId( ) );
        assertEquals( _randomPageName, page.getName( ) );
    }

    public void testDoModifyPageInexistentParentPage( )
            throws AccessDeniedException, SizeLimitExceededException, FileUploadException
    {
        int origParentPageId = _page.getParentPageId( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Map<String, String[ ]> parameters = new HashMap<>( );
        parameters.put( Parameters.PAGE_ID, new String[ ] { Integer.toString( _page.getId( ) ) } );
        parameters.put( Parameters.PAGE_DESCRIPTION, new String[ ] { _page.getDescription( ) } );
        parameters.put( Parameters.PAGE_TEMPLATE_ID, new String[ ] { Integer.toString( _page.getPageTemplateId( ) ) } );
        parameters.put( Parameters.META_KEYWORDS, new String[ ] { _page.getMetaKeywords( ) } );
        parameters.put( Parameters.META_DESCRIPTION, new String[ ] { _page.getMetaDescription( ) } );
        parameters.put( "node_status", new String[ ] { Integer.toString( _page.getNodeStatus( ) ) } );
        parameters.put( Parameters.PAGE_NAME, new String[ ] { _page.getName( ) } );
        parameters.put( Parameters.PARENT_ID, new String[ ] { "567894535" } );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, new String[ ] { SecurityTokenService.getInstance( )
                .getToken( request, "admin/site/admin_page_block_property.html" ) } );
        _bean.doModifyPage( new MultipartHttpServletRequest( request, Collections.emptyMap( ), parameters ) );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_STOP, message.getType( ) );
        Page page = PageHome.findByPrimaryKey( _page.getId( ) );
        assertEquals( origParentPageId, page.getParentPageId( ) );
    }

    public void testDoModifyPagePictureError( )
            throws AccessDeniedException, SizeLimitExceededException, FileUploadException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Map<String, String[ ]> parameters = new HashMap<>( );
        parameters.put( Parameters.PAGE_ID, new String[ ] { Integer.toString( _page.getId( ) ) } );
        parameters.put( Parameters.PAGE_DESCRIPTION, new String[ ] { _page.getDescription( ) } );
        parameters.put( Parameters.PAGE_TEMPLATE_ID, new String[ ] { Integer.toString( _page.getPageTemplateId( ) ) } );
        parameters.put( Parameters.META_KEYWORDS, new String[ ] { _page.getMetaKeywords( ) } );
        parameters.put( Parameters.META_DESCRIPTION, new String[ ] { _page.getMetaDescription( ) } );
        parameters.put( "node_status", new String[ ] { Integer.toString( _page.getNodeStatus( ) ) } );
        parameters.put( Parameters.PAGE_NAME, new String[ ] { _page.getName( ) } );
        parameters.put( Parameters.PARENT_ID, new String[ ] { Integer.toString( _page.getParentPageId( ) ) } );
        parameters.put( "update_image", new String[ ] { "update_image" } );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, new String[ ] { SecurityTokenService.getInstance( )
                .getToken( request, "admin/site/admin_page_block_property.html" ) } );
        Map<String, List<FileItem>> fileItems = new HashMap<>( );
        List<FileItem> items = new ArrayList<>( );
        FileItem fileItem = new DiskFileItemFactory( ).createItem( "image_content", "", true, "" );
        items.add( fileItem );
        fileItems.put( "image_content", items );
        _bean.doModifyPage( new MultipartHttpServletRequest( request, fileItems, parameters ) );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_STOP, message.getType( ) );
        Page page = PageHome.findByPrimaryKey( _page.getId( ) );
        assertNull( page.getImageContent( ) );
        assertNull( page.getMimeType( ) );
    }

    public void testDoModifyPageInvalidToken( )
            throws AccessDeniedException, SizeLimitExceededException, FileUploadException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        String descriptionMod = _page.getDescription( ) + "_mod";
        assertEquals( _randomPageName, _page.getDescription( ) );
        Map<String, String[ ]> parameters = new HashMap<>( );
        parameters.put( Parameters.PAGE_ID, new String[ ] { Integer.toString( _page.getId( ) ) } );
        parameters.put( Parameters.PAGE_DESCRIPTION, new String[ ] { descriptionMod } );
        parameters.put( Parameters.PAGE_TEMPLATE_ID, new String[ ] { Integer.toString( _page.getPageTemplateId( ) ) } );
        parameters.put( Parameters.META_KEYWORDS, new String[ ] { _page.getMetaKeywords( ) } );
        parameters.put( Parameters.META_DESCRIPTION, new String[ ] { _page.getMetaDescription( ) } );
        parameters.put( "node_status", new String[ ] { Integer.toString( _page.getNodeStatus( ) ) } );
        parameters.put( Parameters.PAGE_NAME, new String[ ] { _page.getName( ) } );
        parameters.put( Parameters.PARENT_ID, new String[ ] { Integer.toString( _page.getParentPageId( ) ) } );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, new String[ ] {
                SecurityTokenService.getInstance( ).getToken( request, "admin/site/admin_page_block_property.html" )
                        + "b" } );
        try
        {
            _bean.doModifyPage( new MultipartHttpServletRequest( request, Collections.emptyMap( ), parameters ) );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            Page page = PageHome.findByPrimaryKey( _page.getId( ) );
            assertEquals( _randomPageName, page.getDescription( ) );
        }
    }

    public void testDoModifyPageNoToken( ) throws AccessDeniedException, SizeLimitExceededException, FileUploadException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        String descriptionMod = _page.getDescription( ) + "_mod";
        assertEquals( _randomPageName, _page.getDescription( ) );
        Map<String, String[ ]> parameters = new HashMap<>( );
        parameters.put( Parameters.PAGE_ID, new String[ ] { Integer.toString( _page.getId( ) ) } );
        parameters.put( Parameters.PAGE_DESCRIPTION, new String[ ] { descriptionMod } );
        parameters.put( Parameters.PAGE_TEMPLATE_ID, new String[ ] { Integer.toString( _page.getPageTemplateId( ) ) } );
        parameters.put( Parameters.META_KEYWORDS, new String[ ] { _page.getMetaKeywords( ) } );
        parameters.put( Parameters.META_DESCRIPTION, new String[ ] { _page.getMetaDescription( ) } );
        parameters.put( "node_status", new String[ ] { Integer.toString( _page.getNodeStatus( ) ) } );
        parameters.put( Parameters.PAGE_NAME, new String[ ] { _page.getName( ) } );
        parameters.put( Parameters.PARENT_ID, new String[ ] { Integer.toString( _page.getParentPageId( ) ) } );
        try
        {
            _bean.doModifyPage( new MultipartHttpServletRequest( request, Collections.emptyMap( ), parameters ) );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            Page page = PageHome.findByPrimaryKey( _page.getId( ) );
            assertEquals( _randomPageName, page.getDescription( ) );
        }
    }

    public void testGetAdminPageBlockChildPage( ) throws PasswordResetException, AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, _adminUser, AdminPageJspBean.RIGHT_MANAGE_ADMIN_SITE );
        _bean.init( request, AdminPageJspBean.RIGHT_MANAGE_ADMIN_SITE );
        request.addParameter( "param_block", "5" );
        String html = _bean.getAdminPage( request );
        assertNotNull( html );
    }

    public void testDoCreateChildPage( )
            throws AccessDeniedException, SizeLimitExceededException, FileUploadException, IOException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Map<String, String[ ]> parameters = new HashMap<>( );
        parameters.put( Parameters.PAGE_ID, new String[ ] { Integer.toString( _page.getId( ) ) } );
        parameters.put( Parameters.PAGE_DESCRIPTION, new String[ ] { _page.getDescription( ) } );
        parameters.put( Parameters.PAGE_TEMPLATE_ID, new String[ ] { Integer.toString( _page.getPageTemplateId( ) ) } );
        parameters.put( Parameters.META_KEYWORDS, new String[ ] { _page.getMetaKeywords( ) } );
        parameters.put( Parameters.META_DESCRIPTION, new String[ ] { _page.getMetaDescription( ) } );
        parameters.put( "node_status", new String[ ] { Integer.toString( _page.getNodeStatus( ) ) } );
        parameters.put( Parameters.PAGE_NAME, new String[ ] { _page.getName( ) + "_child" } );
        parameters.put( Parameters.PARENT_ID, new String[ ] { Integer.toString( _page.getParentPageId( ) ) } );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, new String[ ] { SecurityTokenService.getInstance( )
                .getToken( request, "admin/site/admin_page_block_childpage.html" ) } );
        Collection<Page> children = PageHome.getChildPages( _page.getId( ) );
        assertNotNull( children );
        assertTrue( children.isEmpty( ) );

        Map<String, List<FileItem>> fileItems = new HashMap<>( );
        List<FileItem> listItems = new ArrayList<>( );
        FileItem pageImageFile = new DiskFileItemFactory( ).createItem( "image_content", "", false, "" );
        pageImageFile.getOutputStream( ).write( new byte[ 1 ] );
        listItems.add( pageImageFile );
        fileItems.put( "image_content", listItems );
        _bean.doCreateChildPage( new MultipartHttpServletRequest( request, fileItems, parameters ) );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNull( message );
        children = PageHome.getChildPages( _page.getId( ) );
        assertNotNull( children );
        assertFalse( children.isEmpty( ) );
        assertTrue( children.stream( ).allMatch( page -> page.getParentPageId( ) == _page.getId( )
                && page.getName( ).equals( _page.getName( ) + "_child" ) ) );
    }

    public void testDoCreateChildPageInvalidToken( )
            throws AccessDeniedException, SizeLimitExceededException, FileUploadException, IOException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Map<String, String[ ]> parameters = new HashMap<>( );
        parameters.put( Parameters.PAGE_ID, new String[ ] { Integer.toString( _page.getId( ) ) } );
        parameters.put( Parameters.PAGE_DESCRIPTION, new String[ ] { _page.getDescription( ) } );
        parameters.put( Parameters.PAGE_TEMPLATE_ID, new String[ ] { Integer.toString( _page.getPageTemplateId( ) ) } );
        parameters.put( Parameters.META_KEYWORDS, new String[ ] { _page.getMetaKeywords( ) } );
        parameters.put( Parameters.META_DESCRIPTION, new String[ ] { _page.getMetaDescription( ) } );
        parameters.put( "node_status", new String[ ] { Integer.toString( _page.getNodeStatus( ) ) } );
        parameters.put( Parameters.PAGE_NAME, new String[ ] { _page.getName( ) + "_child" } );
        parameters.put( Parameters.PARENT_ID, new String[ ] { Integer.toString( _page.getParentPageId( ) ) } );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, new String[ ] {
                SecurityTokenService.getInstance( ).getToken( request, "admin/site/admin_page_block_childpage.html" )
                        + "b" } );
        Collection<Page> children = PageHome.getChildPages( _page.getId( ) );
        assertNotNull( children );
        assertTrue( children.isEmpty( ) );

        Map<String, List<FileItem>> fileItems = new HashMap<>( );
        List<FileItem> listItems = new ArrayList<>( );
        FileItem pageImageFile = new DiskFileItemFactory( ).createItem( "image_content", "", false, "" );
        pageImageFile.getOutputStream( ).write( new byte[ 1 ] );
        listItems.add( pageImageFile );
        fileItems.put( "image_content", listItems );
        try
        {
            _bean.doCreateChildPage( new MultipartHttpServletRequest( request, fileItems, parameters ) );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            children = PageHome.getChildPages( _page.getId( ) );
            assertNotNull( children );
            assertTrue( children.isEmpty( ) );
        }
    }

    public void testDoCreateChildPageNoToken( )
            throws AccessDeniedException, SizeLimitExceededException, FileUploadException, IOException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Map<String, String[ ]> parameters = new HashMap<>( );
        parameters.put( Parameters.PAGE_ID, new String[ ] { Integer.toString( _page.getId( ) ) } );
        parameters.put( Parameters.PAGE_DESCRIPTION, new String[ ] { _page.getDescription( ) } );
        parameters.put( Parameters.PAGE_TEMPLATE_ID, new String[ ] { Integer.toString( _page.getPageTemplateId( ) ) } );
        parameters.put( Parameters.META_KEYWORDS, new String[ ] { _page.getMetaKeywords( ) } );
        parameters.put( Parameters.META_DESCRIPTION, new String[ ] { _page.getMetaDescription( ) } );
        parameters.put( "node_status", new String[ ] { Integer.toString( _page.getNodeStatus( ) ) } );
        parameters.put( Parameters.PAGE_NAME, new String[ ] { _page.getName( ) + "_child" } );
        parameters.put( Parameters.PARENT_ID, new String[ ] { Integer.toString( _page.getParentPageId( ) ) } );
        Collection<Page> children = PageHome.getChildPages( _page.getId( ) );
        assertNotNull( children );
        assertTrue( children.isEmpty( ) );

        Map<String, List<FileItem>> fileItems = new HashMap<>( );
        List<FileItem> listItems = new ArrayList<>( );
        FileItem pageImageFile = new DiskFileItemFactory( ).createItem( "image_content", "", false, "" );
        pageImageFile.getOutputStream( ).write( new byte[ 1 ] );
        listItems.add( pageImageFile );
        fileItems.put( "image_content", listItems );
        try
        {
            _bean.doCreateChildPage( new MultipartHttpServletRequest( request, fileItems, parameters ) );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            children = PageHome.getChildPages( _page.getId( ) );
            assertNotNull( children );
            assertTrue( children.isEmpty( ) );
        }
    }
}
