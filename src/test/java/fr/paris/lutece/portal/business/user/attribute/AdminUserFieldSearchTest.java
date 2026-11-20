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
package fr.paris.lutece.portal.business.user.attribute;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.service.user.attribute.AttributeService;
import fr.paris.lutece.test.LuteceTestCase;
import jakarta.inject.Inject;

/**
 * Search of the admin users by the option of a list attribute
 */
public class AdminUserFieldSearchTest extends LuteceTestCase
{
    @Inject
    private AttributeService _attributeService;

    /**
     * A user who chose an option is still found by it after the option is renamed
     */
    @Test
    public void testSearchByRenamedOption( )
    {
        String strName = "junit" + new SecureRandom( ).nextLong( );
        AttributeComboBox attribute = new AttributeComboBox( );
        attribute.setTitle( strName );
        attribute.setHelpMessage( strName );
        List<AttributeField> listFields = new ArrayList<>( );
        AttributeField option = new AttributeField( );
        option.setTitle( "Alpha" );
        option.setValue( "a" );
        listFields.add( option );
        attribute.setListAttributeFields( listFields );
        _attributeService.createAttribute( attribute );

        AdminUser user = new AdminUser( );
        user.setAccessCode( strName );
        user.setLastName( strName );
        user.setFirstName( strName );
        user.setEmail( strName + "@lutece.fr" );
        AdminUserHome.create( user );
        try
        {
            AttributeField storedOption = AttributeFieldHome.selectAttributeFieldsByIdAttribute( attribute.getIdAttribute( ) ).stream( )
                    .filter( f -> "Alpha".equals( f.getTitle( ) ) ).findFirst( ).orElseThrow( );
            AdminUserField userField = new AdminUserField( );
            userField.setUser( user );
            userField.setAttribute( attribute );
            userField.setAttributeField( storedOption );
            userField.setValue( "Alpha" );
            AdminUserFieldHome.create( userField );

            storedOption.setTitle( "Beta" );
            AttributeFieldHome.update( storedOption );

            AdminUserField searched = new AdminUserField( );
            searched.setAttribute( attribute );
            searched.setAttributeField( storedOption );
            searched.setValue( "Beta" );
            AdminUserFieldFilter filter = new AdminUserFieldFilter( );
            List<AdminUserField> listSearched = new ArrayList<>( );
            listSearched.add( searched );
            filter.setListUserFields( listSearched );

            assertTrue( AdminUserFieldHome.findUsersByFilter( filter ).stream( ).anyMatch( u -> u.getUserId( ) == user.getUserId( ) ) );
        }
        finally
        {
            AdminUserFieldHome.removeUserFieldsFromIdUser( user.getUserId( ) );
            AdminUserHome.remove( user.getUserId( ) );
            _attributeService.removeAttribute( attribute.getIdAttribute( ) );
        }
    }
}
