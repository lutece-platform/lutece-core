/*
 * Copyright (c) 2002-2024, City of Paris
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
package fr.paris.lutece.portal.service.datastore;

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.test.LuteceTestCase;

/**
 * Labels of the properties of a localized data group
 */
public class LocalizedDataGroupTest extends LuteceTestCase
{
    private static final String PREFIX = "localizeddatagrouptest.site_property.";
    private static final String KEY_TEXT = PREFIX + "welcome_title";
    private static final String KEY_TEXTBLOCK = PREFIX + "footer_text.textblock";

    /**
     * Removes the properties created by the tests
     */
    @AfterEach
    protected void removeProperties( )
    {
        DatastoreService.removeDataByPrefix( PREFIX );
    }

    /**
     * A property without i18n resource, such as a label created by plugin-sitelabels, is displayed with its key without the prefix of the group nor
     * the suffix of its input type, instead of an empty label
     */
    @Test
    public void testPropertyWithoutI18nGetsItsKeyAsLabel( )
    {
        DatastoreService.setDataValue( KEY_TEXT, "Welcome" );
        DatastoreService.setDataValue( KEY_TEXTBLOCK, "Footer" );

        LocalizedDataGroup group = new LocalizedDataGroup( new TestGroup( ), Locale.FRENCH );

        assertEquals( 2, group.getLocalizedDataList( ).size( ) );
        for ( LocalizedData data : group.getLocalizedDataList( ) )
        {
            String strExpected = KEY_TEXT.equals( data.getKey( ) ) ? "welcome_title" : "footer_text";
            assertEquals( strExpected, data.getLabel( ) );
        }
    }

    /**
     * The default label keeps the key when nothing remains after the prefix and the suffix
     */
    @Test
    public void testDefaultLabel( )
    {
        assertEquals( "my_label", LocalizedDataGroup.getDefaultLabel( PREFIX + "my_label.htmlblock", PREFIX ) );
        assertEquals( "my_label", LocalizedDataGroup.getDefaultLabel( "group.my_label", "group" ) );
        assertEquals( "other.key", LocalizedDataGroup.getDefaultLabel( "other.key", PREFIX ) );
        assertEquals( PREFIX, LocalizedDataGroup.getDefaultLabel( PREFIX, PREFIX ) );
    }

    /**
     * Group of the tests
     */
    private static final class TestGroup implements ILocalizedDataGroup
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getNameKey( )
        {
            return "portal.site.site_property.name";
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getDescriptionKey( )
        {
            return "portal.site.site_property.name";
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getDatastoreKeysPrefix( )
        {
            return PREFIX;
        }
    }
}
