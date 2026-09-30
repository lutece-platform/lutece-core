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
package fr.paris.lutece.portal.service.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests the loading order of the properties files: the last file loaded wins.
 */
public class FileSorterUtilTest
{
    /**
     * override/plugins is loaded after override, whatever the file names.
     */
    @Test
    public void testOverridePluginsWinsOverOverride( )
    {
        assertEquals( Arrays.asList( "WEB-INF/conf/override/zz.properties", "WEB-INF/conf/override/plugins/aa.properties" ),
                FileSorterUtil.sortByPropertiesPrecedence( Arrays.asList( "WEB-INF/conf/override/plugins/aa.properties", "WEB-INF/conf/override/zz.properties" ) ) );
        assertEquals( Arrays.asList( "WEB-INF/conf/override/aa.properties", "WEB-INF/conf/override/plugins/zz.properties" ),
                FileSorterUtil.sortByPropertiesPrecedence( Arrays.asList( "WEB-INF/conf/override/plugins/zz.properties", "WEB-INF/conf/override/aa.properties" ) ) );
    }

    /**
     * The themes are loaded after the plugins, the override directories after both.
     */
    @Test
    public void testDirectoryOrder( )
    {
        List<String> paths = Arrays.asList( "WEB-INF/conf/override/a.properties", "WEB-INF/conf/themes/a.properties", "WEB-INF/conf/plugins/z.properties",
                "WEB-INF/conf/override/plugins/a.properties" );
        assertEquals( Arrays.asList( "WEB-INF/conf/plugins/z.properties", "WEB-INF/conf/themes/a.properties", "WEB-INF/conf/override/a.properties",
                "WEB-INF/conf/override/plugins/a.properties" ), FileSorterUtil.sortByPropertiesPrecedence( paths ) );
    }

    /**
     * Inside one directory the files are loaded in alphabetical order.
     */
    @Test
    public void testAlphabeticalInsideADirectory( )
    {
        assertEquals( Arrays.asList( "WEB-INF/conf/plugins/a.properties", "WEB-INF/conf/plugins/b.properties" ),
                FileSorterUtil.sortByPropertiesPrecedence( Arrays.asList( "WEB-INF/conf/plugins/b.properties", "WEB-INF/conf/plugins/a.properties" ) ) );
    }

    /**
     * A path starting with a slash is ranked like the relative one.
     */
    @Test
    public void testLeadingSlash( )
    {
        assertEquals( Arrays.asList( "/WEB-INF/conf/override/zz.properties", "/WEB-INF/conf/override/plugins/aa.properties" ),
                FileSorterUtil.sortByPropertiesPrecedence( Arrays.asList( "/WEB-INF/conf/override/plugins/aa.properties", "/WEB-INF/conf/override/zz.properties" ) ) );
    }

    /**
     * A file of the override directory whose name starts like the plugins directory stays in the override directory.
     */
    @Test
    public void testOverrideFileNamedLikeThePluginsDirectory( )
    {
        assertEquals( Arrays.asList( "WEB-INF/conf/override/plugins-local.properties", "WEB-INF/conf/override/plugins/a.properties" ),
                FileSorterUtil.sortByPropertiesPrecedence( Arrays.asList( "WEB-INF/conf/override/plugins/a.properties", "WEB-INF/conf/override/plugins-local.properties" ) ) );
    }
}
