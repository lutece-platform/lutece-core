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
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNERS OR CONTRIBUTORS BE
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
package fr.paris.lutece.portal.service.search;

import java.util.List;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.service.util.ErrorLogCapture;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.inject.Inject;

/**
 * An empty query (search page opened without words) returns no result and logs no error: the Lucene parser was called on an
 * empty string, which throws, and the error was logged with its stack trace at each display of the search page.
 */
public class LuceneSearchEngineEmptyQueryTest extends LuteceTestCase
{
    private @Inject SearchEngine _engine;

    @Test
    public void testEmptyQueryLogsNoError( )
    {
        try ( ErrorLogCapture capture = new ErrorLogCapture( ) )
        {
            for ( String strQuery : new String [ ] { "", "   ", null } )
            {
                List<SearchResult> listResults = _engine.getSearchResults( strQuery, new MockHttpServletRequest( ) );
                assertTrue( listResults == null || listResults.isEmpty( ), "query <" + strQuery + ">" );
            }
            assertTrue( capture.getErrors( ).isEmpty( ), "errors logged: " + capture.getErrors( ) );
        }
    }
}
