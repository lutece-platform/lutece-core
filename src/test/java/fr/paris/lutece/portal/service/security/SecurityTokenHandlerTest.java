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

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
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
    private static final String SHARED_CONTROLLER = "/jsp/admin/test/ManageSharedToken.jsp";
    // Name registered by a @Controller: controllerPath + controllerJsp, without leading slash
    private static final String SHARED_CONTROLLER_NAME = "jsp/admin/test/ManageSharedToken.jsp";
    private static final String VIEW_SHARED = "sharedView";
    private static final String ACTION_SAVE_STEP = "doSaveStep";
    private static final String ACTION_SUMMARY = "formResponseSummary";
    private static final String ACTION_OWN_TOKEN = "doOwnToken";
    private static final String SHARED_TOKEN_KEY = "formResponse";
    private static final String TOKEN = "token-value";
    private static final String TOKEN_FIELD = "<input type=\"hidden\" name=\"_csrftoken\" value=\"" + TOKEN + "\" >";

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
     * An action called through GET or HEAD (a plain link) is checked too: the action runs whatever the method.
     */
    @Test
    public void testAdminActionCheckedOnGet( )
    {
        for ( String strMethod : new String [ ] {
                "GET", "HEAD"
        } )
        {
            MockHttpServletRequest request = post( ADMIN_CONTROLLER, ACTION_WITH_TOKEN );
            request.setMethod( strMethod );
            assertFalse( _handler.shouldNotFilter( request ) );
        }
    }

    /**
     * A view displayed through GET is not checked, nor an action declared with the security token disabled.
     */
    @Test
    public void testGetWithoutActionOrDisabledActionNotChecked( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setMethod( "GET" );
        request.setServletPath( ADMIN_CONTROLLER );
        request.setParameter( "view", "confirmRemoveSecurityHeader" );
        assertTrue( _handler.shouldNotFilter( request ) );

        MockHttpServletRequest requestDisabled = post( ADMIN_CONTROLLER, ACTION_WITHOUT_TOKEN );
        requestDisabled.setMethod( "GET" );
        assertTrue( _handler.shouldNotFilter( requestDisabled ) );
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

    /**
     * A form whose first attribute follows several spaces or a line break still receives the token field.
     */
    @Test
    public void testTokenAddedAfterAnyWhitespace( )
    {
        HashMap<String, Object> model = new HashMap<>( );
        model.put( SecurityTokenHandler.MARK_CSRF_TOKEN, TOKEN );

        assertEquals( "<form  id=\"f\">" + TOKEN_FIELD + "</form>", SecurityTokenHandler.addSecurityToken( "<form  id=\"f\"></form>", model ) );
        assertEquals( "<form\n  method=\"post\">" + TOKEN_FIELD + "</form>",
                SecurityTokenHandler.addSecurityToken( "<form\n  method=\"post\"></form>", model ) );
    }

    /**
     * Several actions posted from the same form accept the token of the view that displays it when they share its securityTokenAction.
     *
     * @throws NoSuchMethodException
     *             if the view is missing
     */
    @Test
    public void testActionsSharingTokenKeyAcceptViewToken( ) throws NoSuchMethodException
    {
        _handler.registerActions( SHARED_CONTROLLER_NAME, SharedTokenController.class.getDeclaredMethods( ) );

        for ( String strAction : new String [ ] {
                ACTION_SAVE_STEP, ACTION_SUMMARY
        } )
        {
            MockHttpServletRequest request = post( SHARED_CONTROLLER, strAction );
            assertFalse( _handler.shouldNotFilter( request ) );
            assertEquals( SHARED_TOKEN_KEY, _handler.resolveTokenAction( request, strAction ) );

            _handler.handle( request );
            _handler.handleToken( request, ControllerInvocationType.VIEW, SharedTokenController.class.getDeclaredMethod( VIEW_SHARED ) );
            request.setParameter( SecurityTokenHandler.MARK_CSRF_TOKEN, _handler.resolveTokenValue( request ) );
            assertTrue( _handler.validate( request, _handler.resolveTokenAction( request, strAction ) ) );
        }
    }

    /**
     * An action without securityTokenAction keeps its own name as token key.
     */
    @Test
    public void testActionWithoutTokenKeyUsesItsName( )
    {
        _handler.registerActions( SHARED_CONTROLLER_NAME, SharedTokenController.class.getDeclaredMethods( ) );

        MockHttpServletRequest request = post( SHARED_CONTROLLER, ACTION_OWN_TOKEN );
        assertEquals( ACTION_OWN_TOKEN, _handler.resolveTokenAction( request, ACTION_OWN_TOKEN ) );
        assertEquals( ACTION_WITH_TOKEN, _handler.resolveTokenAction( post( ADMIN_CONTROLLER, ACTION_WITH_TOKEN ), ACTION_WITH_TOKEN ) );
    }

    /**
     * Controller whose step form posts several actions with one token.
     */
    private static final class SharedTokenController
    {
        @View( value = VIEW_SHARED, securityTokenAction = SHARED_TOKEN_KEY )
        private String sharedView( )
        {
            return null;
        }

        @Action( value = ACTION_SAVE_STEP, securityTokenAction = SHARED_TOKEN_KEY )
        private String doSaveStep( )
        {
            return null;
        }

        @Action( value = ACTION_SUMMARY, securityTokenAction = SHARED_TOKEN_KEY )
        private String formResponseSummary( )
        {
            return null;
        }

        @Action( value = ACTION_OWN_TOKEN )
        private String doOwnToken( )
        {
            return null;
        }
    }
}
