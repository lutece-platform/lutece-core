/*
 * Copyright (c) 2015, Mairie de Paris
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
package fr.paris.lutece.portal.web.role;

import java.security.SecureRandom;
import java.util.Locale;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.role.Role;
import fr.paris.lutece.portal.business.role.RoleHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

public class RoleJspBeanTest extends LuteceTestCase
{
    private static final String PARAMETER_PAGE_ROLE = "role";

    public void testGetRemovePageRole( )
    {
        RoleJspBean bean = new RoleJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        // no args
        bean.getRemovePageRole( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        ReferenceList listLanguages = I18nService.getAdminLocales( Locale.FRANCE );
        for ( ReferenceItem lang : listLanguages )
        {
            assertTrue( message.getText( new Locale( lang.getCode( ) ) ).contains( PARAMETER_PAGE_ROLE ) );
        }
        // invalid arg
        String randomRoleName = "role" + new SecureRandom(  ).nextLong(  );
        request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_PAGE_ROLE, randomRoleName );
        bean.getRemovePageRole( request );
        message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        for ( ReferenceItem lang : listLanguages )
        {
            assertTrue( message.getText( new Locale( lang.getCode( ) ) ).contains( randomRoleName ) );
        }
        // valid arg
        Role role = new Role( );
        role.setRole( randomRoleName );
        role.setRoleDescription( randomRoleName );
        role.setWorkgroup( AdminWorkgroupService.ALL_GROUPS );
        RoleHome.create( role );
        try
        {
            request = new MockHttpServletRequest( );
            request.addParameter( PARAMETER_PAGE_ROLE, randomRoleName );
            bean.getRemovePageRole( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            for ( ReferenceItem lang : listLanguages )
            {
                assertTrue( message.getText( new Locale( lang.getCode( ) ) ).contains( randomRoleName ) );
            }
        } finally
        {
            RoleHome.remove( randomRoleName );
        }
    }
}
