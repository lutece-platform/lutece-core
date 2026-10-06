/*
 * Copyright (c) 2002-2025, City of Paris
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
package fr.paris.lutece.portal.service.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.cdi.mvc.event.MvcEvent.ControllerInvocationType;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.inject.Inject;

/**
 * Tests which requests the security token filters check.
 */
public class SecurityTokenHandlerTest extends LuteceTestCase
{
    private static final String ADMIN_CONTROLLER = "/jsp/admin/system/ManageSecurityHeaders.jsp";
    private static final String ACTION_WITH_TOKEN = "removeSecurityHeader";
    private static final String ACTION_WITHOUT_TOKEN = "enableSecurityHeader";
    private static final String VIEW_DEFAULT = "defaultView";

    @Inject
    private SecurityTokenHandler _handler;

    /**
     * Registers the back office controllers as the application startup does.
     */
    @BeforeEach
    protected void registerControllers( )
    {
        _handler.registerAdminControllers( null );
    }

    /**
     * Builds a POST request to the given servlet path.
     *
     * @param strServletPath
     *            The servlet path
     * @param strAction
     *            The MVC action
     * @return The request
     */
    private MockHttpServletRequest post( String strServletPath, String strAction )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setMethod( "POST" );
        request.setServletPath( strServletPath );
        request.setParameter( "action", strAction );
        return request;
    }

    /**
     * An action of a back office controller is checked before the controller has ever been displayed.
     */
    @Test
    public void testAdminActionCheckedBeforeFirstDisplay( )
    {
        assertFalse( _handler.shouldNotFilter( post( ADMIN_CONTROLLER, ACTION_WITH_TOKEN ) ) );
    }

    /**
     * A page parameter does not move a back office request to the front office rules.
     */
    @Test
    public void testPageParameterOnAdminPathIsChecked( )
    {
        MockHttpServletRequest request = post( ADMIN_CONTROLLER, ACTION_WITH_TOKEN );
        request.setParameter( "page", "unknown" );
        assertFalse( _handler.shouldNotFilter( request ) );
    }

    /**
     * An action declared with the security token disabled is not checked.
     */
    @Test
    public void testDisabledActionNotChecked( )
    {
        assertTrue( _handler.shouldNotFilter( post( ADMIN_CONTROLLER, ACTION_WITHOUT_TOKEN ) ) );
    }

    /**
     * A front office request keeps the rules of the XPage it names.
     */
    @Test
    public void testUnknownXPageNotChecked( )
    {
        MockHttpServletRequest request = post( "/jsp/site/Portal.jsp", ACTION_WITH_TOKEN );
        request.setParameter( "page", "unknown" );
        assertTrue( _handler.shouldNotFilter( request ) );
    }

    /**
     * A default view displayed without the view parameter gets the token of its action.
     *
     * @throws NoSuchMethodException
     *             if the default view is missing
     */
    @Test
    public void testDefaultViewWithoutViewParameterGetsToken( ) throws NoSuchMethodException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setServletPath( ADMIN_CONTROLLER );
        _handler.handle( request );
        _handler.handleToken( request, ControllerInvocationType.DEFAULT_VIEW, SecurityTokenHandlerTest.class.getDeclaredMethod( VIEW_DEFAULT ) );

        String strToken = _handler.resolveTokenValue( request );
        assertNotNull( strToken );
        request.setParameter( SecurityTokenHandler.MARK_CSRF_TOKEN, strToken );
        assertTrue( _handler.validate( request, ACTION_WITH_TOKEN ) );
    }

    /**
     * Default view carrying the token of the action.
     *
     * @return nothing
     */
    @View( value = VIEW_DEFAULT, defaultView = true, securityTokenAction = ACTION_WITH_TOKEN )
    private String defaultView( )
    {
        return null;
    }
}
