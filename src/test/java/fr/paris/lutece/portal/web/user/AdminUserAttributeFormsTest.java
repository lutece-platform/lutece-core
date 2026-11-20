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
package fr.paris.lutece.portal.web.user;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.attribute.AbstractAttribute;
import fr.paris.lutece.portal.business.user.attribute.AdminUserFieldFilter;
import fr.paris.lutece.portal.business.user.attribute.AttributeCheckBox;
import fr.paris.lutece.portal.business.user.attribute.AttributeComboBox;
import fr.paris.lutece.portal.business.user.attribute.AttributeField;
import fr.paris.lutece.portal.business.user.attribute.AttributeFieldHome;
import fr.paris.lutece.portal.business.user.attribute.AttributeRadioButton;
import fr.paris.lutece.portal.business.user.attribute.AttributeText;
import fr.paris.lutece.portal.service.user.attribute.AttributeService;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.inject.Inject;

/**
 * The user screens render the forms of every type of user attribute
 */
public class AdminUserAttributeFormsTest extends LuteceTestCase
{
    private static final String RIGHT_CORE_USERS_MANAGEMENT = "CORE_USERS_MANAGEMENT";

    @Inject
    private AdminUserJspBean _bean;
    @Inject
    private AttributeService _attributeService;
    private final List<Integer> _listAttributes = new ArrayList<>( );

    @AfterEach
    public void removeAttributes( )
    {
        for ( int nIdAttribute : _listAttributes )
        {
            _attributeService.removeAttribute( nIdAttribute );
        }
    }

    private void create( AbstractAttribute attribute, String... options )
    {
        String strName = "junit" + new SecureRandom( ).nextLong( );
        attribute.setTitle( strName );
        attribute.setHelpMessage( strName );
        List<AttributeField> listFields = new ArrayList<>( );

        for ( String strOption : options )
        {
            AttributeField field = new AttributeField( );
            field.setTitle( strOption );
            field.setValue( strOption );
            listFields.add( field );
        }

        attribute.setListAttributeFields( listFields );
        _attributeService.createAttribute( attribute );
        _listAttributes.add( attribute.getIdAttribute( ) );
    }

    private MockHttpServletRequest getAdminRequest( ) throws Exception
    {
        // An administrator of level 0 in the session only: the state of the "admin" account of the test database does not matter
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser admin = new AdminUser( );
        admin.setAccessCode( "junit_attribute_forms" );
        admin.setUserLevel( 0 );
        admin.setLocale( Locale.FRENCH );
        AdminUserUtils.registerAdminUserWithRight( request, admin, RIGHT_CORE_USERS_MANAGEMENT );
        _bean.init( request, RIGHT_CORE_USERS_MANAGEMENT );

        return request;
    }

    /**
     * A radio button or check box attribute shown in the search is rendered in the search form of the users list
     */
    @Test
    public void testSearchFormWithRadioAndCheckBoxAttributes( ) throws Exception
    {
        AttributeRadioButton radio = new AttributeRadioButton( );
        radio.setShownInSearch( true );
        create( radio, "radioA", "radioB" );
        AttributeCheckBox checkBox = new AttributeCheckBox( );
        checkBox.setShownInSearch( true );
        checkBox.setFieldInLine( true );
        create( checkBox, "boxA" );

        String strHtml = _bean.getManageAdminUsers( getAdminRequest( ) );

        assertTrue( strHtml.contains( "radioB" ) );
        assertTrue( strHtml.contains( "boxA" ) );
    }

    /**
     * A mandatory check box attribute with one option is rendered in the user creation form
     */
    @Test
    public void testCreateFormWithOneOptionCheckBoxAttribute( ) throws Exception
    {
        AttributeCheckBox checkBox = new AttributeCheckBox( );
        checkBox.setMandatory( true );
        create( checkBox, "onlyOption" );

        String strHtml = _bean.getCreateAdminUser( getAdminRequest( ) );

        assertTrue( strHtml.contains( "onlyOption" ) );
    }

}
