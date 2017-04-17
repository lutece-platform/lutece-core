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
package fr.paris.lutece.portal.web.system;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
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
        instance.getManageFilesSystem( request );
    }

    /**
     * Test of getManageFilesSystemDir method, of class fr.paris.lutece.portal.web.system.SystemJspBean.
     */
    public void testGetManageFilesSystemDir( ) throws AccessDeniedException
    {
        request.addParameter( PARAMETER_DIR, PARAMETER_DIR_VALUE );
        instance.getManageFilesSystemDir( request );
    }

    /**
     * Test of getFileView method, of class fr.paris.lutece.portal.web.system.SystemJspBean.
     */
    public void testGetFileView( ) throws AccessDeniedException
    {
        request.addParameter( PARAMETER_DIRECTORY, PARAMETER_DIR_VALUE );
        request.addParameter( PARAMETER_FILE, PARAMETER_FILE_VALUE );

        instance.getFileView( request );
    }

    /**
     * Test of getManageProperties method, of class fr.paris.lutece.portal.web.system.SystemJspBean.
     */
    public void testGetManageProperties( ) throws AccessDeniedException
    {
        instance.getManageProperties( request );
    }
}
