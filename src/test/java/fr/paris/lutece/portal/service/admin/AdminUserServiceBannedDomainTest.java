/*
 * Copyright (c) 2002-2026, City of Paris
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

import org.junit.jupiter.api.Test;

import fr.paris.lutece.test.LuteceTestCase;

/**
 * Banned e-mail domains of the admin users
 */
public class AdminUserServiceBannedDomainTest extends LuteceTestCase
{
    @Test
    public void testBannedDomainsAreTrimmedAndCaseInsensitive( )
    {
        String strBanned = AdminUserService.getLargeSecurityParameter( AdminUserService.DSKEY_BANNED_DOMAIN_NAMES );
        try
        {
            AdminUserService.updateLargeSecurityParameter( AdminUserService.DSKEY_BANNED_DOMAIN_NAMES, "example.org; Yopmail.com\nmailinator.com" );

            assertFalse( AdminUserService.checkEmail( "bh@example.org" ) );
            assertFalse( AdminUserService.checkEmail( "bh@yopmail.COM" ) );
            assertFalse( AdminUserService.checkEmail( "bh@mailinator.com" ) );
            assertTrue( AdminUserService.checkEmail( "bh@lutece.fr" ) );
        }
        finally
        {
            AdminUserService.updateLargeSecurityParameter( AdminUserService.DSKEY_BANNED_DOMAIN_NAMES, strBanned );
        }
    }
}
