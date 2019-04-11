/*
 * Copyright (c) 2002-2017, Mairie de Paris
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
package fr.paris.lutece.portal.web.globalmanagement;

import java.util.Locale;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.globalmanagement.RichTextEditorService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.test.LuteceTestCase;

public class EditorChoiceLutecePanelJspBeanTest extends LuteceTestCase
{
    private EditorChoiceLutecePanelJspBean _instance;
    private String _boDefaultEditor;
    private String _foDefaultEditor;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        _instance = new EditorChoiceLutecePanelJspBean( );
        _instance.setRequest( new MockHttpServletRequest( ) );
        _boDefaultEditor = RichTextEditorService.getBackOfficeDefaultEditor( );
        _foDefaultEditor = RichTextEditorService.getFrontOfficeDefaultEditor( );
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        RichTextEditorService.updateBackOfficeDefaultEditor( _boDefaultEditor );
        RichTextEditorService.updateFrontOfficeDefaultEditor( _foDefaultEditor );
        super.tearDown( );
    }

    public void testGetPanelContent( )
    {
        assertNotNull( _instance.getPanelContent( ) );
    }

    public void testDoUpdateBackOfficeEditor( ) throws AccessDeniedException
    {
        String strBOEditor = RichTextEditorService.getListEditorsForBackOffice( Locale.FRANCE ).stream( )
                .filter( ref -> !ref.getCode( ).equals( _boDefaultEditor ) ).findFirst( ).orElseThrow( IllegalStateException::new ).getCode( );
        assertFalse( strBOEditor.equals( RichTextEditorService.getBackOfficeDefaultEditor( ) ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "editor_back_office", strBOEditor );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/globalmanagement/panel/editor_choice_panel.html" ) );
        _instance.doUpdateBackOfficeEditor( request );

        assertEquals( strBOEditor, RichTextEditorService.getBackOfficeDefaultEditor( ) );
    }

    public void testDoUpdateBackOfficeEditorInvalidToken( ) throws AccessDeniedException
    {
        String strBOEditor = RichTextEditorService.getListEditorsForBackOffice( Locale.FRANCE ).stream( )
                .filter( ref -> !ref.getCode( ).equals( _boDefaultEditor ) ).findFirst( ).orElseThrow( IllegalStateException::new ).getCode( );
        assertFalse( strBOEditor.equals( RichTextEditorService.getBackOfficeDefaultEditor( ) ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "editor_back_office", strBOEditor );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/globalmanagement/panel/editor_choice_panel.html" ) + "b" );
        try
        {
            _instance.doUpdateBackOfficeEditor( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( _boDefaultEditor, RichTextEditorService.getBackOfficeDefaultEditor( ) );
        }
    }

    public void testDoUpdateBackOfficeEditorNoToken( ) throws AccessDeniedException
    {
        String strBOEditor = RichTextEditorService.getListEditorsForBackOffice( Locale.FRANCE ).stream( )
                .filter( ref -> !ref.getCode( ).equals( _boDefaultEditor ) ).findFirst( ).orElseThrow( IllegalStateException::new ).getCode( );
        assertFalse( strBOEditor.equals( RichTextEditorService.getBackOfficeDefaultEditor( ) ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "editor_back_office", strBOEditor );

        try
        {
            _instance.doUpdateBackOfficeEditor( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( _boDefaultEditor, RichTextEditorService.getBackOfficeDefaultEditor( ) );
        }
    }

    public void testDoUpdateFrontOfficeEditor( ) throws AccessDeniedException
    {
        String strFOEditor = RichTextEditorService.getListEditorsForFrontOffice( Locale.FRANCE ).stream( )
                .filter( ref -> !ref.getCode( ).equals( _foDefaultEditor ) ).findFirst( ).orElseThrow( IllegalStateException::new ).getCode( );
        assertFalse( strFOEditor.equals( RichTextEditorService.getFrontOfficeDefaultEditor( ) ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "editor_front_office", strFOEditor );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/globalmanagement/panel/editor_choice_panel.html" ) );
        _instance.doUpdateFrontOfficeEditor( request );

        assertEquals( strFOEditor, RichTextEditorService.getFrontOfficeDefaultEditor( ) );
    }

    public void testDoUpdateFrontOfficeEditorInvalidToken( ) throws AccessDeniedException
    {
        String strFOEditor = RichTextEditorService.getListEditorsForFrontOffice( Locale.FRANCE ).stream( )
                .filter( ref -> !ref.getCode( ).equals( _foDefaultEditor ) ).findFirst( ).orElseThrow( IllegalStateException::new ).getCode( );
        assertFalse( strFOEditor.equals( RichTextEditorService.getFrontOfficeDefaultEditor( ) ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "editor_front_office", strFOEditor );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/globalmanagement/panel/editor_choice_panel.html" ) + "b" );
        try
        {
            _instance.doUpdateFrontOfficeEditor( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( _foDefaultEditor, RichTextEditorService.getFrontOfficeDefaultEditor( ) );
        }
    }

    public void testDoUpdateFrontOfficeEditorNoToken( ) throws AccessDeniedException
    {
        String strFOEditor = RichTextEditorService.getListEditorsForFrontOffice( Locale.FRANCE ).stream( )
                .filter( ref -> !ref.getCode( ).equals( _foDefaultEditor ) ).findFirst( ).orElseThrow( IllegalStateException::new ).getCode( );
        assertFalse( strFOEditor.equals( RichTextEditorService.getFrontOfficeDefaultEditor( ) ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "editor_front_office", strFOEditor );

        try
        {
            _instance.doUpdateFrontOfficeEditor( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( _foDefaultEditor, RichTextEditorService.getFrontOfficeDefaultEditor( ) );
        }
    }
}
