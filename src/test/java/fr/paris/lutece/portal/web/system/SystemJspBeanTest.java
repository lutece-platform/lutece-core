/*
 * Copyright (c) 2002-2021, City of Paris
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
package fr.paris.lutece.portal.web.system;

import org.apache.commons.lang3.StringUtils;
import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.Utils;

/**
 * SystemJspBean Test Class
 *
 */
public class SystemJspBeanTest extends LuteceTestCase
{
    private static final String PARAMETER_DIR = "dir";
    private static final String PARAMETER_DIR_VALUE = "/WEB-INF/conf/";
    private static final String PARAMETER_DIRECTORY = "directory";
    private static final String PARAMETER_FILE = "file";
    private static final String PARAMETER_FILE_VALUE = "config.properties";
    private MockHttpServletRequest request;
    private SystemJspBean instance;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), SystemJspBean.RIGHT_PROPERTIES_MANAGEMENT );

        instance = new SystemJspBean( );
       instance.init( request, SystemJspBean.RIGHT_PROPERTIES_MANAGEMENT );
    }

    /**
     * Test of getManageFilesSystem method, of class fr.paris.lutece.portal.web.system.SystemJspBean.
     */
    public void testGetManageFilesSystem( ) throws AccessDeniedException
    {
        assertTrue( StringUtils.isNotEmpty( instance.getManageFilesSystem( request ) ) );
    }

    /**
     * Test of getManageFilesSystemDir method, of class fr.paris.lutece.portal.web.system.SystemJspBean.
     */
    public void testGetManageFilesSystemDir( ) throws AccessDeniedException
    {
        request.addParameter( PARAMETER_DIR, PARAMETER_DIR_VALUE );
        assertTrue( StringUtils.isNotEmpty( instance.getManageFilesSystemDir( request ) ) );
    }

    /**
     * Test of getFileView method, of class fr.paris.lutece.portal.web.system.SystemJspBean.
     */
    public void testGetFileView( ) throws AccessDeniedException
    {
        request.addParameter( PARAMETER_DIRECTORY, PARAMETER_DIR_VALUE );
        request.addParameter( PARAMETER_FILE, PARAMETER_FILE_VALUE );

        assertTrue( StringUtils.isNotEmpty( instance.getFileView( request ) ) );
    }

    /**
     * Test of getManageProperties method, of class fr.paris.lutece.portal.web.system.SystemJspBean.
     */
    public void testGetManageProperties( ) throws AccessDeniedException
    {
        assertTrue( StringUtils.isNotEmpty( instance.getManageProperties( request ) ) );
    }

    public void testDoModifyProperties( ) throws AccessDeniedException
    {
        final String property = "portal.site.site_property.email";
        final String origValue = DatastoreService.getDataValue( property, "" );
        request.setParameter( property, origValue + "_mod" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/system/modify_properties.html" ) );

        try
        {
            SystemJspBean.doModifyProperties( request, request.getServletContext( ) );
            assertEquals( origValue + "_mod", DatastoreService.getDataValue( property, "" ) );
        }
        finally
        {
            DatastoreService.setDataValue( property, origValue );
        }
    }

    public void testDoModifyPropertiesInvalidToken( ) throws AccessDeniedException
    {
        final String property = "portal.site.site_property.email";
        final String origValue = DatastoreService.getDataValue( property, "" );
        request.setParameter( property, origValue + "_mod" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/system/modify_properties.html" ) + "b" );

        try
        {
            SystemJspBean.doModifyProperties( request, request.getServletContext( ) );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( origValue, DatastoreService.getDataValue( property, "" ) );
        }
        finally
        {
            DatastoreService.setDataValue( property, origValue );
        }
    }

    public void testDoModifyPropertiesNoToken( ) throws AccessDeniedException
    {
        final String property = "portal.site.site_property.email";
        final String origValue = DatastoreService.getDataValue( property, "" );
        request.setParameter( property, origValue + "_mod" );

        try
        {
            SystemJspBean.doModifyProperties( request, request.getServletContext( ) );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( origValue, DatastoreService.getDataValue( property, "" ) );
        }
        finally
        {
            DatastoreService.setDataValue( property, origValue );
        }
    }
}
