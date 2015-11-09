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
package fr.paris.lutece.portal.business.user.authentication;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.password.IPassword;

public class LuteceDefaultAdminUserDAOTest extends LuteceTestCase
{
    private LuteceDefaultAdminUserDAO getLuteceDefaultAdminUserDAO(  )
    {
        LuteceDefaultAdminUserDAO dao = new LuteceDefaultAdminUserDAO(  );
        AutowireCapableBeanFactory beanFactory = SpringContextService.getContext( ).getAutowireCapableBeanFactory( );
        beanFactory.autowireBean( dao );
        return dao;
    }

    public void testLoadPassword(  )
    {
        LuteceDefaultAdminUserDAO dao = getLuteceDefaultAdminUserDAO(  );

        IPassword password = dao.loadPassword( "admin" );
        assertNotNull( password );
        assertTrue( password.check( "adminadmin" ) );

        password = dao.loadPassword( "adminDOES_NOT_EXIST" );
        assertNotNull( password );
        assertFalse( password.check( "adminadmin" ) );
    }

    public void testStore(  )
    {
        LuteceDefaultAdminUserDAO dao = getLuteceDefaultAdminUserDAO(  );
        PasswordFactory passwordFactory = new PasswordFactory( );

        IPassword password = passwordFactory.getPassword( "PLAINTEXT:PASSWORD" );
        try
        {
            dao.store( "DOES_NOT_EXIST", password );
            fail( );
        } catch ( IllegalArgumentException e )
        {
        }
        password = passwordFactory.getPassword( "MD5:319f4d26e3c536b5dd871bb2c52e3178" );
        try
        {
            dao.store( "DOES_NOT_EXIST", password );
            fail( );
        } catch ( IllegalArgumentException e )
        {
        }
        password = passwordFactory.getPassword( "SHA-1:112bb791304791ddcf692e29fd5cf149b35fea37" );
        try
        {
            dao.store( "DOES_NOT_EXIST", password );
            fail( );
        } catch ( IllegalArgumentException e )
        {
        }
        password = passwordFactory.getPassword( "SHA-256:0be64ae89ddd24e225434de95d501711339baeee18f009ba9b4369af27d30d60" );
        try
        {
            dao.store( "DOES_NOT_EXIST", password );
            fail( );
        } catch ( IllegalArgumentException e )
        {
        }
        password = passwordFactory.getDummyPassword(  );
        try
        {
            dao.store( "DOES_NOT_EXIST", password );
            fail( );
        } catch ( UnsupportedOperationException e )
        {
        }
        password = passwordFactory.getPasswordFromCleartext( "PASSWORD" );
        dao.store( "DOES_NOT_EXIST", password );
    }
}
