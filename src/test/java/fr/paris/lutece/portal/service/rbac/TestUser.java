/*
 * Copyright (c) 2002-2023, City of Paris
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
package fr.paris.lutece.portal.service.rbac;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.api.user.UserRole;

final class TestUser implements User
{

    static final class TestRole implements UserRole
    {

        private final String _strKey;

        TestRole( String strKey )
        {
            _strKey = strKey;
        }

        @Override
        public void setKey( String strKey )
        {
            throw new UnsupportedOperationException( );

        }

        @Override
        public void setDescription( String strDescription )
        {
            throw new UnsupportedOperationException( );

        }

        @Override
        public String getKey( )
        {
            return _strKey;
        }

        @Override
        public String getDescription( )
        {
            return _strKey;
        }
    }

    private final Map<String, UserRole> _userRoles;

    TestUser( String... roles )
    {
        _userRoles = new HashMap<>( );
        for ( String role : roles )
        {
            _userRoles.put( role, new TestRole( role ) );
        }
    }

    @Override
    public List<String> getUserWorkgroups( )
    {
        throw new UnsupportedOperationException( );
    }

    @Override
    public Map<String, UserRole> getUserRoles( )
    {
        return _userRoles;
    }

    @Override
    public String getRealm( )
    {
        throw new UnsupportedOperationException( );
    }

    @Override
    public String getLastName( )
    {
        throw new UnsupportedOperationException( );
    }

    @Override
    public String getFirstName( )
    {
        throw new UnsupportedOperationException( );
    }

    @Override
    public String getEmail( )
    {
        throw new UnsupportedOperationException( );
    }

    @Override
    public String getAccessCode( )
    {
        throw new UnsupportedOperationException( );
    }
}
