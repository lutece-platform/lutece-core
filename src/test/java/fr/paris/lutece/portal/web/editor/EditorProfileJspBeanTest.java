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
package fr.paris.lutece.portal.web.editor;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.editor.EditorProfile;
import fr.paris.lutece.portal.business.editor.EditorProfileHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.ISecurityTokenService;
import fr.paris.lutece.portal.service.security.SecurityTokenHandler;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;
import jakarta.inject.Inject;

/**
 * Removal of an editor profile
 */
public class EditorProfileJspBeanTest extends LuteceTestCase
{
    @Inject
    private EditorProfileJspBean _bean;
    @Inject
    private ISecurityTokenService _securityTokenService;

    /**
     * The confirmation message of the removal carries its own token: it still works after the confirmation page is reloaded
     */
    @Test
    public void testConfirmRemoveEditorProfileCarriesTheToken( ) throws Exception
    {
        EditorProfile profile = new EditorProfile( );
        profile.setName( "junit_profile" );
        profile.setDescription( "junit" );
        profile.setEditorName( "tinymce" );
        profile.setConfig( EditorProfile.EMPTY_CONFIG );
        EditorProfileHome.create( profile );

        try
        {
            MockHttpServletRequest request = new MockHttpServletRequest( );
            AdminUser user = new AdminUser( );
            user.setAccessCode( "test_editor_profile" );
            user.setLocale( Locale.FRENCH );
            AdminUserUtils.registerAdminUserWithRight( request, user, EditorChoiceLutecePanelJspBean.RIGHT_EDITORS_MANAGEMENT );
            request.setParameter( "view", "confirmRemoveEditorProfile" );
            request.setParameter( "profile_id", Integer.toString( profile.getId( ) ) );

            _bean.processController( request, new MockHttpServletResponse( ) );

            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( AdminMessage.TYPE_CONFIRMATION, message.getType( ) );
            Object token = message.getRequestParameters( ).get( SecurityTokenHandler.MARK_CSRF_TOKEN );
            assertNotNull( token, "the confirmation form posts the token of the removal" );

            request.setParameter( SecurityTokenService.PARAMETER_TOKEN, token.toString( ) );
            assertTrue( _securityTokenService.validate( request, "removeEditorProfile" ) );
        }
        finally
        {
            EditorProfileHome.remove( profile.getId( ) );
        }
    }
}
