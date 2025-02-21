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
package fr.paris.lutece.portal.service.admin;

import java.security.SecureRandom;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminUser;
import fr.paris.lutece.portal.service.csv.CSVMessageDescriptor;
import fr.paris.lutece.portal.service.user.attribute.AttributeService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.inject.Inject;
public class ImportAdminUserServiceTest extends LuteceTestCase
{
	@Inject
	private AttributeService _attributeService;
	
	@Test
    public void testReadLineOfCSVFileForPassword( )
    {
        String [ ] lineData = new String [ ] {
                "user" + new SecureRandom( ).nextLong( ), // accessCode
                "lastName", "firstName", "email", Integer.toString( AdminUser.ACTIVE_CODE ), "fr", Integer.toString( 3 ), // level
                "", // resetpassword
                "false", // accessibility mode
                "", // passwordmaxvaliddate
                "", // accountmaxvaliddate
                "", // datelastlogin

        };
        ImportAdminUserService importAdminuser = new DefaultImportAdminUserService( _attributeService );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        List<CSVMessageDescriptor> messages = importAdminuser.readLineOfCSVFile( lineData, 1, AdminUserService.getLocale( request ),
                AppPathService.getBaseUrl( request ) );
        assertNotNull( messages );
        assertTrue( messages.isEmpty( ) );
        AdminUser user = AdminUserHome.findUserByLogin( lineData [0] );
        assertNotNull( user );
        try
        {
            LuteceDefaultAdminUser luteceDefaultAdminUser = AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( user.getUserId( ) );
            assertNotNull( luteceDefaultAdminUser );
            assertFalse( luteceDefaultAdminUser.getPassword( ).isLegacy( ) );
        }
        finally
        {
            AdminUserHome.remove( user.getUserId( ) );
        }
    }
}
