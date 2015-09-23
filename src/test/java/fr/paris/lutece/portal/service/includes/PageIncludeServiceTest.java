/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.portal.service.includes;

import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.test.LuteceTestCase;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public class PageIncludeServiceTest extends LuteceTestCase
{
    public void testPageIncludes(  )
    {
        List<PageInclude> listIncludes = PageIncludeService.getIncludes(  );
        // Assert default includes are loaded
        assertTrue( listIncludes.size(  ) > 2 );
    }

    public void testEnabledState(  ) throws LuteceInitException
    {
        PageIncludeEntry entry = new PageIncludeEntry(  );
        entry.setClassName( TestPageInclude.class.getName(  ) );
        entry.setId( "testEnablePageInclude" );
        entry.setPluginName( "core" ); // core is an always enabled plugin

        PageIncludeService.registerPageInclude( entry );

        List<PageInclude> includes = PageIncludeService.getIncludes(  );
        assertTrue( isTestPageIncludeActive( includes ) );

        entry.setEnabled( false );
        PageIncludeService.registerPageInclude( entry );
        includes = PageIncludeService.getIncludes(  );
        assertFalse( isTestPageIncludeActive( includes ) );

        entry.setPluginName( "bogus_inexistant_plugin" );
        PageIncludeService.registerPageInclude( entry );
        includes = PageIncludeService.getIncludes(  );
        assertFalse( isTestPageIncludeActive( includes ) );

        entry.setEnabled( true );
        PageIncludeService.registerPageInclude( entry );
        includes = PageIncludeService.getIncludes(  );
        assertFalse( isTestPageIncludeActive( includes ) );
    }

    private boolean isTestPageIncludeActive( List<PageInclude> includes )
    {
        boolean found = false;

        for ( PageInclude include : includes )
        {
            if ( include instanceof TestPageInclude )
            {
                found = true;

                break;
            }
        }

        return found;
    }

    static class TestPageInclude implements PageInclude
    {
        @Override
        public void fillTemplate( Map<String, Object> rootModel, PageData data, int nMode, HttpServletRequest request )
        {
        }
    }
}
