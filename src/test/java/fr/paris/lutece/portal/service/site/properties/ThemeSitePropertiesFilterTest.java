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
package fr.paris.lutece.portal.service.site.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.paris.lutece.portal.service.datastore.LocalizedData;

/**
 * Tests the rules of the theme configuration file that hides theme site properties
 */
public class ThemeSitePropertiesFilterTest
{
    private static final String PREFIX = "portal.theme.site_property.";

    private static LocalizedData data( String strKey, String strGroup )
    {
        LocalizedData data = new LocalizedData( );
        data.setKey( strKey );
        data.setGroup( strGroup );
        return data;
    }

    private static List<LocalizedData> properties( )
    {
        List<LocalizedData> list = new ArrayList<>( );
        list.add( data( PREFIX + "layout.darkmode.checkbox", "layout" ) );
        list.add( data( PREFIX + "layout.width.select", "layout" ) );
        list.add( data( PREFIX + "layout.width.select.options", "" ) );
        list.add( data( PREFIX + "consent.text", "consent" ) );
        list.add( data( PREFIX + "consent.mode.radio", "consent" ) );
        list.add( data( PREFIX + "consent.mode.radio.options", "" ) );
        list.add( data( "portal.site.site_property.name", "consent" ) );
        return list;
    }

    private static List<String> keysAfter( String strJson ) throws Exception
    {
        JsonNode config = new ObjectMapper( ).readTree( strJson );
        List<LocalizedData> list = properties( );
        ThemeSitePropertiesFilter.filter( list, config );
        return list.stream( ).map( LocalizedData::getKey ).toList( );
    }

    @Test
    public void testEmptyConfigKeepsEverything( ) throws Exception
    {
        assertEquals( 7, keysAfter( "{}" ).size( ) );
    }

    @Test
    public void testTrueKeepsTheProperty( ) throws Exception
    {
        assertEquals( 7, keysAfter( "{\"siteProperties\":{\"groups\":{\"layout\":true},\"keys\":{\"" + PREFIX + "consent.text\":true}}}" ).size( ) );
    }

    @Test
    public void testFalseKeyHidesThePropertyAndItsOptions( ) throws Exception
    {
        List<String> keys = keysAfter( "{\"siteProperties\":{\"keys\":{\"" + PREFIX + "layout.width.select\":false}}}" );
        assertEquals( List.of( PREFIX + "layout.darkmode.checkbox", PREFIX + "consent.text", PREFIX + "consent.mode.radio",
                PREFIX + "consent.mode.radio.options", "portal.site.site_property.name" ), keys );
    }

    @Test
    public void testKeyPrefixDoesNotHideALongerName( ) throws Exception
    {
        // "consent.mode" hides consent.mode.radio but "consent.te" must not hide consent.text
        List<String> keys = keysAfter( "{\"siteProperties\":{\"keys\":{\"" + PREFIX + "consent.te\":false,\"" + PREFIX + "consent.mode\":false}}}" );
        assertEquals( 5, keys.size( ) );
        assertEquals( true, keys.contains( PREFIX + "consent.text" ) );
    }

    @Test
    public void testFalseGroupHidesItsPropertiesAndTheirOptions( ) throws Exception
    {
        List<String> keys = keysAfter( "{\"siteProperties\":{\"groups\":{\"consent\":false}}}" );
        // A property outside the theme prefix is never hidden, even in a group of the same name
        assertEquals( List.of( PREFIX + "layout.darkmode.checkbox", PREFIX + "layout.width.select", PREFIX + "layout.width.select.options",
                "portal.site.site_property.name" ), keys );
    }
}
