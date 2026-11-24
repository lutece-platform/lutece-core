/*
 * Copyright (c) 2002-2022, City of Paris
 * All rights reserved.
 * License 1.0
 */
package fr.paris.lutece.portal.util.mvc.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;
import jakarta.inject.Inject;

/**
 * Tests that {@link MVCAdminJspBean#processController} ends the response by itself when it has already written it
 * (a {@code @ResponseBody} method, a download), so that the admin controller JSP (header, content, footer) which
 * runs afterwards writes into a closed writer and is ignored, and that it marks the request accordingly. A plain
 * view and a view that returns {@code null} leave the response open and unmarked.
 */
public class MvcResponseWrittenTest extends LuteceTestCase
{
    private static final String PARAMETER_VIEW = "view";

    @Inject
    private TestResponseController _bean;

    private MockHttpServletRequest _request;
    private String _returned;

    /**
     * Registers an admin user holding the controller right, then runs the controller for the given view.
     *
     * @param strView
     *            the value of the {@code view} request parameter
     * @return the response the controller wrote into
     */
    private MockHttpServletResponse process( String strView ) throws Exception
    {
        _request = new MockHttpServletRequest( );
        AdminUser user = new AdminUser( );
        user.setAccessCode( "test_mvc" );
        user.setLocale( Locale.FRENCH );
        AdminUserUtils.registerAdminUserWithRight( _request, user, TestResponseController.RIGHT );
        _request.setParameter( PARAMETER_VIEW, strView );

        MockHttpServletResponse response = new MockHttpServletResponse( );
        _returned = _bean.processController( _request, response );
        return response;
    }

    @Test
    public void testResponseBodyWritesOnlyItsContentAndIsMarked( ) throws Exception
    {
        MockHttpServletResponse response = process( TestResponseController.VIEW_JSON );

        assertNull( _returned, "a @ResponseBody view returns no page to the JSP" );
        assertTrue( MVCUtils.isResponseWritten( _request ), "the request must be marked as already written" );
        assertEquals( TestResponseController.JSON_BODY, response.getContentAsString( ) );

        // What the controller JSP would write afterwards goes into the closed writer and is ignored.
        response.getWriter( ).print( "<html>CHROME</html>" );
        assertEquals( TestResponseController.JSON_BODY, response.getContentAsString( ),
                "nothing must be appended after the @ResponseBody content" );
    }

    @Test
    public void testResponseBodyWithAccentsGivesItsLengthInBytes( ) throws Exception
    {
        MockHttpServletResponse response = process( TestResponseController.VIEW_JSON_ACCENTS );

        assertEquals( StandardCharsets.UTF_8.name( ), response.getCharacterEncoding( ), "the body is declared in UTF-8" );
        assertEquals( String.valueOf( TestResponseController.JSON_ACCENTS_BODY.getBytes( StandardCharsets.UTF_8 ).length ), response.getHeader( "Content-Length" ),
                "the length counts the bytes of the body, an accent taking two" );
        assertEquals( TestResponseController.JSON_ACCENTS_BODY, response.getContentAsString( ), "the whole body is written" );
    }

    @Test
    public void testDownloadWritesOnlyItsDataAndIsMarked( ) throws Exception
    {
        MockHttpServletResponse response = process( TestResponseController.VIEW_DOWNLOAD );

        assertNull( _returned, "a download view returns no page to the JSP" );
        assertTrue( MVCUtils.isResponseWritten( _request ), "the request must be marked as already written" );
        assertEquals( TestResponseController.DOWNLOAD_DATA, response.getContentAsString( ) );

        response.getWriter( ).print( "<html>CHROME</html>" );
        assertEquals( TestResponseController.DOWNLOAD_DATA, response.getContentAsString( ),
                "nothing must be appended after the downloaded data" );
    }

    @Test
    public void testRedirectIsMarked( ) throws Exception
    {
        process( TestResponseController.VIEW_REDIRECT );

        assertNull( _returned, "a redirect returns no page to the JSP" );
        assertTrue( MVCUtils.isResponseWritten( _request ), "a redirect must mark the request as already written" );
    }

    @Test
    public void testPlainViewIsNotMarkedAndLeavesTheResponseOpen( ) throws Exception
    {
        MockHttpServletResponse response = process( TestResponseController.VIEW_NORMAL );

        assertEquals( TestResponseController.PAGE_CONTENT, _returned, "a plain view returns its page to the JSP" );
        assertFalse( MVCUtils.isResponseWritten( _request ), "a plain view must not mark the request" );
        assertEquals( "", response.getContentAsString( ), "a plain view writes nothing to the response itself" );

        // The response is still open: the JSP that follows writes the page normally.
        response.getWriter( ).print( "PAGE-HTML" );
        assertEquals( "PAGE-HTML", response.getContentAsString( ), "the controller JSP must still be able to write" );
    }

    @Test
    public void testNullViewIsNotMarked( ) throws Exception
    {
        process( TestResponseController.VIEW_NULL );

        assertNull( _returned, "the view legitimately returns null" );
        assertFalse( MVCUtils.isResponseWritten( _request ), "a view that returns null must not be marked as written" );
    }
}
