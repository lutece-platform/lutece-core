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
package fr.paris.lutece.portal.service.template;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.test.LuteceTestCase;

/**
 * Front office macro cMainNav: the main menu is written once, whether it comes from the core or from a custom menu
 */
public class CMainNavTemplateTest extends LuteceTestCase
{
    private static final String SNIPPET = "<@cMainNav title='Site' hasMenu=true isSidebar=false isFixed=false hasSearchMenu=false searchUrl='' isOnlyHome=false hasUserThemeSwitch=false />";
    private static final String CORE_MENU = "<li class=\"nav-item\">CORE_MENU_ITEM</li>";
    private static final String MAIN_MENU = "<li class=\"nav-item\">CUSTOM_MAIN_ITEM</li>";
    private static final String INTERNAL_MENU = "<li class=\"nav-item\">CUSTOM_INTERNAL_ITEM</li>";

    private static String render( Map<String, Object> model )
    {
        return AppTemplateService.getTemplateFromStringFtl( SNIPPET, Locale.FRENCH, model ).getHtml( );
    }

    private static int count( String strHtml, String strText )
    {
        return strHtml.split( strText, -1 ).length - 1;
    }

    /**
     * Without custom menu, the menu of the core is written once
     */
    @Test
    public void testCoreMenuOnly( )
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "page_main_menu_html", HtmlMarkup.of( CORE_MENU ) );

        assertEquals( 1, count( render( model ), "CORE_MENU_ITEM" ) );
    }

    /**
     * A custom main menu without internal menu (plugin-menus puts it in customMenuMainPage, customMenuInternalPage and
     * page_main_menu_html) is written once, in place of the menu of the core
     */
    @Test
    public void testCustomMainMenuWrittenOnce( )
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "page_main_menu_html", HtmlMarkup.of( MAIN_MENU ) );
        model.put( "customMenuMainPage", HtmlMarkup.of( MAIN_MENU ) );
        model.put( "customMenuInternalPage", HtmlMarkup.of( MAIN_MENU ) );

        assertEquals( 1, count( render( model ), "CUSTOM_MAIN_ITEM" ) );
    }

    /**
     * A custom main menu replaces the menu of the core
     */
    @Test
    public void testCustomMainMenuReplacesCoreMenu( )
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "page_main_menu_html", HtmlMarkup.of( CORE_MENU ) );
        model.put( "customMenuMainPage", HtmlMarkup.of( MAIN_MENU ) );

        String strHtml = render( model );

        assertEquals( 1, count( strHtml, "CUSTOM_MAIN_ITEM" ) );
        assertEquals( 0, count( strHtml, "CORE_MENU_ITEM" ) );
    }

    /**
     * An internal custom menu different from the main one is written once, after it
     */
    @Test
    public void testInternalMenuWrittenWhenDifferent( )
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "page_main_menu_html", HtmlMarkup.of( MAIN_MENU ) );
        model.put( "customMenuMainPage", HtmlMarkup.of( MAIN_MENU ) );
        model.put( "customMenuInternalPage", HtmlMarkup.of( INTERNAL_MENU ) );

        String strHtml = render( model );

        assertEquals( 1, count( strHtml, "CUSTOM_MAIN_ITEM" ) );
        assertEquals( 1, count( strHtml, "CUSTOM_INTERNAL_ITEM" ) );
        assertTrue( strHtml.indexOf( "CUSTOM_MAIN_ITEM" ) < strHtml.indexOf( "CUSTOM_INTERNAL_ITEM" ), strHtml );
    }

    /**
     * An internal custom menu alone is written after the menu of the core
     */
    @Test
    public void testInternalMenuWithoutMainMenu( )
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "page_main_menu_html", HtmlMarkup.of( CORE_MENU ) );
        model.put( "customMenuInternalPage", HtmlMarkup.of( INTERNAL_MENU ) );

        String strHtml = render( model );

        assertEquals( 1, count( strHtml, "CORE_MENU_ITEM" ) );
        assertEquals( 1, count( strHtml, "CUSTOM_INTERNAL_ITEM" ) );
    }

    /**
     * showDefaultMenu=false hides the menu of the core, not a custom main menu
     */
    @Test
    public void testShowDefaultMenuFalse( )
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "page_main_menu_html", HtmlMarkup.of( CORE_MENU ) );
        String strHtml = AppTemplateService.getTemplateFromStringFtl( SNIPPET.replace( "/>", "showDefaultMenu=false />" ), Locale.FRENCH, model ).getHtml( );
        assertEquals( 0, count( strHtml, "CORE_MENU_ITEM" ) );

        model.put( "customMenuMainPage", HtmlMarkup.of( MAIN_MENU ) );
        strHtml = AppTemplateService.getTemplateFromStringFtl( SNIPPET.replace( "/>", "showDefaultMenu=false />" ), Locale.FRENCH, model ).getHtml( );
        assertEquals( 1, count( strHtml, "CUSTOM_MAIN_ITEM" ) );
    }
}
