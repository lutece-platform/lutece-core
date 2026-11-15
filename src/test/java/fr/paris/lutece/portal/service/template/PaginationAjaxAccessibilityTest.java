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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * The navigation links of the Ajax pagination macro (@paginationAjax of paginationAdmin.ftl) must have an accessible
 * name: they are icon-only links built in JavaScript, so each carries a translated aria-label (first / previous / next /
 * last page) read from a data-* attribute, the icon is aria-hidden, the current page is aria-current and the disabled
 * links are aria-disabled. The labels come from the model as data-* attributes, never as concatenated HTML.
 */
public class PaginationAjaxAccessibilityTest extends LuteceTestCase
{
    private static final String SNIPPET = "<@paginationAjax paginator=paginator columns=cols ajaxUrl='Ajax.jsp' tableId='t' combo=0 showcount=0 />";

    private static String render( Locale locale )
    {
        Map<String, Object> paginator = new HashMap<>( );
        paginator.put( "itemsPerPage", 10 );
        paginator.put( "pageCurrent", 1 );
        paginator.put( "itemsCount", 30 );
        paginator.put( "pagesCount", 3 );
        paginator.put( "labelItemCount", "items" );

        List<Map<String, Object>> columns = new ArrayList<>( );
        Map<String, Object> column = new HashMap<>( );
        column.put( "name", "Name" );
        column.put( "property", "name" );
        columns.add( column );

        Map<String, Object> model = new HashMap<>( );
        model.put( "paginator", paginator );
        model.put( "cols", columns );

        return AppTemplateService.getTemplateFromStringFtl( SNIPPET, locale, model ).getHtml( );
    }

    @Test
    public void testNavigationLinksCarryAnAccessibleName( )
    {
        String strHtml = render( Locale.ENGLISH );

        // The four icon-only navigation links get a translated name from a data-* attribute.
        assertTrue( strHtml.contains( "data-label-first=\"First page\"" ), strHtml );
        assertTrue( strHtml.contains( "data-label-previous=\"Previous page\"" ), strHtml );
        assertTrue( strHtml.contains( "data-label-next=\"Next page\"" ), strHtml );
        assertTrue( strHtml.contains( "data-label-last=\"Last page\"" ), strHtml );

        // The JavaScript sets the accessible name on the link and hides the icon from the accessibility tree.
        assertTrue( strHtml.contains( "a.setAttribute('aria-label', ariaLabel)" ), strHtml );
        assertTrue( strHtml.contains( "ti ti-chevrons-left\" aria-hidden=\"true\"" ), strHtml );
        assertTrue( strHtml.contains( "ti ti-chevron-left\" aria-hidden=\"true\"" ), strHtml );
        assertTrue( strHtml.contains( "ti ti-chevron-right\" aria-hidden=\"true\"" ), strHtml );
        assertTrue( strHtml.contains( "ti ti-chevrons-right\" aria-hidden=\"true\"" ), strHtml );

        // The current page is marked, the disabled links are taken out of the tab order.
        assertTrue( strHtml.contains( "a.setAttribute('aria-current', 'page')" ), strHtml );
        assertTrue( strHtml.contains( "a.setAttribute('aria-disabled', 'true')" ), strHtml );
        assertTrue( strHtml.contains( "a.setAttribute('tabindex', '-1')" ), strHtml );
    }

    @Test
    public void testNavigationNamesAreTranslated( )
    {
        String strHtml = render( Locale.FRENCH );

        assertTrue( strHtml.contains( "data-label-first=\"Première page\"" ), strHtml );
        assertTrue( strHtml.contains( "data-label-previous=\"Page précédente\"" ), strHtml );
        assertTrue( strHtml.contains( "data-label-next=\"Page suivante\"" ), strHtml );
        assertTrue( strHtml.contains( "data-label-last=\"Dernière page\"" ), strHtml );
    }

    @Test
    public void testThePaginationLabelKeysAreDeclaredInBothLanguages( )
    {
        for ( Locale locale : new Locale [ ] {
                Locale.ENGLISH, Locale.FRENCH
        } )
        {
            for ( String strKey : new String [ ] {
                    "portal.util.labelFirstPage", "portal.util.labelPreviousPage", "portal.util.labelNextPage", "portal.util.labelLastPage"
            } )
            {
                assertTrue( !I18nService.getLocalizedString( strKey, locale ).isEmpty( ), strKey + " must be translated in " + locale );
            }
        }
    }
}
