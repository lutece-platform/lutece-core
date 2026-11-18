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
package fr.paris.lutece.portal.web.system;

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.securityheader.SecurityHeader;
import fr.paris.lutece.portal.business.securityheader.SecurityHeaderConfigHome;
import fr.paris.lutece.portal.business.securityheader.SecurityHeaderConfigItem;
import fr.paris.lutece.portal.business.securityheader.SecurityHeaderHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;
import jakarta.inject.Inject;

/**
 * Tests of {@link SecurityHeaderConfigJspBean}
 */
public class SecurityHeaderConfigJspBeanTest extends LuteceTestCase
{
    @Inject
    private SecurityHeaderConfigJspBean _bean;
    @Inject
    private Models _models;

    private SecurityHeader _securityHeader;
    private SecurityHeaderConfigItem _configItem;

    @BeforeEach
    public void createSecurityHeader( )
    {
        _securityHeader = new SecurityHeader( );
        _securityHeader.setName( "X-Junit-Config" );
        _securityHeader.setValue( "junit" );
        _securityHeader.setDescription( "junit" );
        _securityHeader.setType( "page" );
        _securityHeader.setPageCategory( "all" );
        _securityHeader.setActive( false );
        SecurityHeaderHome.create( _securityHeader );

        _configItem = new SecurityHeaderConfigItem( );
        _configItem.setIdSecurityHeader( _securityHeader.getId( ) );
        _configItem.setUrlPattern( "/jsp/site/Portal.jsp?page=junit*" );
        _configItem.setHeaderCustomValue( "junit-custom" );
        SecurityHeaderConfigHome.create( _configItem );
    }

    @AfterEach
    public void removeSecurityHeader( )
    {
        SecurityHeaderConfigHome.remove( _configItem.getIdConfigItem( ) );
        SecurityHeaderHome.remove( _securityHeader.getId( ) );
    }

    private MockHttpServletRequest newRequest( Locale locale ) throws Exception
    {
        AdminUser user = new AdminUser( );
        user.setLocale( locale );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUserUtils.registerAdminUserWithRight( request, user, SecurityHeaderConfigJspBean.RIGHT_SECURITY_HEADER_MANAGEMENT );
        _bean.processController( request, new MockHttpServletResponse( ) );
        // The request scope of the tests spans several calls: the errors of a previous call are dropped
        _models.put( "errors", null );
        request.setParameter( "id_securityheader", Integer.toString( _securityHeader.getId( ) ) );

        return request;
    }

    /**
     * A config item without custom value gets the message of the validation instead of failing
     */
    @Test
    public void testCreateWithoutValueShowsTheError( ) throws Exception
    {
        MockHttpServletRequest request = newRequest( Locale.FRENCH );
        request.setParameter( "urlPattern", "/jsp/site/Portal.jsp?page=other*" );
        request.setParameter( "headerCustomValue", "" );

        _bean.doCreateSecurityHeaderConfigItem( request );

        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_STOP, message.getType( ) );
        assertEquals( 1, SecurityHeaderConfigHome.findBySecurityHeaderId( _securityHeader.getId( ) ).size( ) );
    }

    /**
     * A config item modified without URL pattern gets the message of the validation instead of failing
     */
    @Test
    public void testModifyWithoutPatternShowsTheError( ) throws Exception
    {
        MockHttpServletRequest request = newRequest( Locale.FRENCH );
        request.setParameter( "id_config_item", Integer.toString( _configItem.getIdConfigItem( ) ) );
        request.setParameter( "urlPattern", "" );
        request.setParameter( "headerCustomValue", "changed" );

        _bean.doModifySecurityHeaderConfigItem( request );

        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_STOP, message.getType( ) );
        assertEquals( "junit-custom", SecurityHeaderConfigHome.findByPrimaryKey( _configItem.getIdConfigItem( ) ).getHeaderCustomValue( ) );
    }

    /**
     * A config item modified with the URL pattern of another item: the message is shown by a redirection, the page is not the URL of the
     * message
     */
    @Test
    public void testModifyWithDuplicatePatternRedirectsToTheMessage( ) throws Exception
    {
        SecurityHeaderConfigItem other = new SecurityHeaderConfigItem( );
        other.setIdSecurityHeader( _securityHeader.getId( ) );
        other.setUrlPattern( "/jsp/site/Portal.jsp?page=other*" );
        other.setHeaderCustomValue( "other" );
        SecurityHeaderConfigHome.create( other );

        try
        {
            MockHttpServletRequest request = newRequest( Locale.FRENCH );
            request.setParameter( "id_config_item", Integer.toString( _configItem.getIdConfigItem( ) ) );
            request.setParameter( "urlPattern", other.getUrlPattern( ) );
            request.setParameter( "headerCustomValue", "changed" );

            // The response is a redirection: no content to display
            assertNull( _bean.doModifySecurityHeaderConfigItem( request ) );

            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( AdminMessage.TYPE_STOP, message.getType( ) );
            assertEquals( "junit-custom", SecurityHeaderConfigHome.findByPrimaryKey( _configItem.getIdConfigItem( ) ).getHeaderCustomValue( ) );
        }
        finally
        {
            SecurityHeaderConfigHome.remove( other.getIdConfigItem( ) );
        }
    }

    /**
     * The validation messages of a config item exist in English
     */
    @Test
    public void testValidationMessagesInEnglish( ) throws Exception
    {
        MockHttpServletRequest request = newRequest( Locale.ENGLISH );
        request.setParameter( "urlPattern", "/jsp/site/Portal.jsp?page=other*" );
        request.setParameter( "headerCustomValue", "" );

        _bean.doCreateSecurityHeaderConfigItem( request );

        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( "The field Value cannot be empty. Please fill it.", message.getText( Locale.ENGLISH ) );

        request = newRequest( Locale.ENGLISH );
        request.setParameter( "id_config_item", Integer.toString( _configItem.getIdConfigItem( ) ) );
        request.setParameter( "urlPattern", "" );
        request.setParameter( "headerCustomValue", "changed" );

        _bean.doModifySecurityHeaderConfigItem( request );

        message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( "The field URL pattern cannot be empty. Please fill it.", message.getText( Locale.ENGLISH ) );
    }
}
