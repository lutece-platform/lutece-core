/*
 * Copyright (c) 2002-2008, Mairie de Paris
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
package fr.paris.lutece.portal.service.i18n;

import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.ReferenceList;

import java.text.DateFormat;

import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * I18nService Test Class
 *
 */
public class I18nServiceTest extends LuteceTestCase
{
    /**
     * Test of localize method, of class fr.paris.lutece.portal.service.i18n.I18nService.
     */
    public void testLocalize(  )
    {
        System.out.println( "localize" );

        String strSource = "#i18n{portal.util.labelCancel}";
        Locale locale = Locale.FRENCH;

        String expResult = "Annuler";
        String result = fr.paris.lutece.portal.service.i18n.I18nService.localize( strSource, locale );
        assertEquals( expResult, result );
    }

    /**
     * Test of getLocalizedString method, of class fr.paris.lutece.portal.service.i18n.I18nService.
     */
    public void testGetLocalizedString(  )
    {
        System.out.println( "getLocalizedString" );

        String strKey = "portal.util.labelCancel";
        Locale locale = Locale.FRENCH;

        String expResult = "Annuler";
        String result = fr.paris.lutece.portal.service.i18n.I18nService.getLocalizedString( strKey, locale );
        assertEquals( expResult, result );
    }

    /**
     * Test of getLocalizedDate method, of class fr.paris.lutece.portal.service.i18n.I18nService.
     */
    public void testGetLocalizedDate(  )
    {
        System.out.println( "getLocalizedDate" );

        Date date = new Date( 0 );
        Locale locale = Locale.FRENCH;
        int nDateFormat = DateFormat.SHORT;

        String expResult = "01/01/70";
        String result = fr.paris.lutece.portal.service.i18n.I18nService.getLocalizedDate( date, locale, nDateFormat );
        assertEquals( expResult, result );
    }

    /**
     * Test of getLocalizedDateTime method, of class fr.paris.lutece.portal.service.i18n.I18nService.
     */
    public void testGetLocalizedDateTime(  )
    {
        System.out.println( "getLocalizedDateTime" );

        Date date = new Date( 0 );
        Locale locale = Locale.FRENCH;
        int nDateFormat = DateFormat.SHORT;
        int nTimeFormat = DateFormat.SHORT;

        String expResult = "01/01/70 01:00";
        String result = fr.paris.lutece.portal.service.i18n.I18nService.getLocalizedDateTime( date, locale,
                nDateFormat, nTimeFormat );
        assertEquals( expResult, result );
    }

    /**
     * Test of getAdminAvailableLocales method, of class fr.paris.lutece.portal.service.i18n.I18nService.
     */
    public void testGetAdminAvailableLocales(  )
    {
        System.out.println( "getAdminAvailableLocales" );

        List<Locale> result = fr.paris.lutece.portal.service.i18n.I18nService.getAdminAvailableLocales(  );
        assertTrue( result.size(  ) >= 2 );
    }

    /**
     * Test of getAdminLocales method, of class fr.paris.lutece.portal.service.i18n.I18nService.
     */
    public void testGetAdminLocales(  )
    {
        System.out.println( "getAdminLocales" );

        Locale locale = Locale.FRENCH;

        ReferenceList result = fr.paris.lutece.portal.service.i18n.I18nService.getAdminLocales( locale );
        assertTrue( result.size(  ) >= 2 );
    }
}
