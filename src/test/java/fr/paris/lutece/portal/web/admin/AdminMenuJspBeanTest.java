/*
 * Copyright (c) 2002-2008, Mairie de Paris
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

import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.MokeHttpServletRequest;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.admin.AdminUserService;

import java.util.Locale;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;


/**
 * AdminMenuJspBeanTest Test Class
 *
 */
public class AdminMenuJspBeanTest extends LuteceTestCase
{
    private static final String PARAMETER_LANGUAGE = "language";
    private static final String TEST_USER_ACCESS_CODE = "admin";
    private static final String TEST_USER_PASSWORD = "admin";
    private static final String TEST_LANGUAGE = "en";
    AdminUser _user = new AdminUser(  );

    /**
     * Test of getAdminMenuHeader method, of class fr.paris.lutece.portal.web.admin.AdminMenuJspBean.
     */
    public void testGetAdminMenuHeader(  ) throws AccessDeniedException
    {
        System.out.println( "getAdminMenuHeader" );

        MokeHttpServletRequest request = new MokeHttpServletRequest(  );
        getUser( request );
        request.registerAdminUser( _user );

        AdminMenuJspBean instance = new AdminMenuJspBean(  );
        instance.getAdminMenuHeader( request );
    }

    /**
     * Test of getAdminMenuUser method, of class fr.paris.lutece.portal.web.admin.AdminMenuJspBean.
     */
    public void testGetAdminMenuUser(  ) throws AccessDeniedException
    {
        System.out.println( "getAdminMenuUser" );

        MokeHttpServletRequest request = new MokeHttpServletRequest(  );
        getUser( request );
        request.registerAdminUser( _user );

        AdminMenuJspBean instance = new AdminMenuJspBean(  );
        instance.getAdminMenuUser( request );
    }

    /**
     * Test of doChangeLanguage method, of class fr.paris.lutece.portal.web.admin.AdminMenuJspBean.
     */
    public void testDoChangeLanguage(  ) throws AccessDeniedException
    {
        System.out.println( "doChangeLanguage" );

        MokeHttpServletRequest request = new MokeHttpServletRequest(  );
        request.addMokeParameters( PARAMETER_LANGUAGE, TEST_LANGUAGE );

        getUser( request );
        request.registerAdminUser( _user );
        _user.setLocale( Locale.FRANCE );

        Locale localeSTored = _user.getLocale(  );

        AdminMenuJspBean instance = new AdminMenuJspBean(  );
        instance.doChangeLanguage( request );
        assertNotSame( localeSTored.getLanguage(  ), _user.getLocale(  ).getLanguage(  ) );
    }

    private void getUser( MokeHttpServletRequest request )
    {
        try
        {
            AdminAuthenticationService.getInstance(  ).loginUser( request, TEST_USER_ACCESS_CODE, TEST_USER_PASSWORD );
            _user = AdminUserService.getAdminUser( request );
        }
        catch ( FailedLoginException ex )
        {
            String strReturn = "../../" + AdminAuthenticationService.getInstance(  ).getLoginPageUrl(  );
        }
        catch ( LoginException ex )
        {
            String strReturn = "../../" + AdminAuthenticationService.getInstance(  ).getLoginPageUrl(  );
        }
    }
}
