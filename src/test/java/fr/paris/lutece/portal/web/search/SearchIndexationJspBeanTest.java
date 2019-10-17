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
package fr.paris.lutece.portal.web.search;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.daemon.AppDaemonService;
import fr.paris.lutece.portal.service.daemon.DaemonEntry;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.search.IndexationService;
import fr.paris.lutece.portal.service.search.SearchIndexer;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.Utils;

/**
 * SearchIndexationJspBean Test Class
 *
 */
public class SearchIndexationJspBeanTest extends LuteceTestCase
{
    private TestSearchIndexer testIndexer;
    private Boolean bIndexDaemonInitialState;

    private static final class TestSearchIndexer implements SearchIndexer
    {
        private boolean _bIndexDocumentsCalled;

        @Override
        public boolean isEnable( )
        {
            return true;
        }

        @Override
        public void indexDocuments( ) throws IOException, InterruptedException, SiteMessageException
        {
            _bIndexDocumentsCalled = true;
        }

        @Override
        public String getVersion( )
        {
            return "1.0.0";
        }

        @Override
        public String getSpecificSearchAppUrl( )
        {
            return null;
        }

        @Override
        public String getName( )
        {
            return this.getClass( ).getCanonicalName( );
        }

        @Override
        public List<String> getListType( )
        {
            return null;
        }

        @Override
        public List<Document> getDocuments( String strIdDocument ) throws IOException, InterruptedException, SiteMessageException
        {
            return null;
        }

        @Override
        public String getDescription( )
        {
            return "junit test indexer";
        }
    }

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        // we stop the indexer daemon so that it does not interfere with the
        // tests
        for ( DaemonEntry daemonEntry : AppDaemonService.getDaemonEntries( ) )
        {
            if ( daemonEntry.getId( ).equals( "indexer" ) )
            {
                bIndexDaemonInitialState = daemonEntry.isRunning( );
                break;
            }
        }
        assertNotNull( "Did not find indexer daemon", bIndexDaemonInitialState );
        AppDaemonService.stopDaemon( "indexer" );
        testIndexer = new TestSearchIndexer( );
        IndexationService.registerIndexer( testIndexer );

    }

    @Override
    protected void tearDown( ) throws Exception
    {
        IndexationService.unregisterIndexer( testIndexer );
        if ( bIndexDaemonInitialState.booleanValue( ) )
        {
            AppDaemonService.startDaemon( "indexer" );
        }
        super.tearDown( );
    }

    /**
     * Test of getIndexingProperties method, of class fr.paris.lutece.portal.web.search.SearchIndexationJspBean.
     */
    public void testGetIndexingProperties( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), SearchIndexationJspBean.RIGHT_INDEXER );

        SearchIndexationJspBean instance = new SearchIndexationJspBean( );
        instance.init( request, SearchIndexationJspBean.RIGHT_INDEXER );
        assertNotNull( instance.getIndexingProperties( request ) );
    }

    /**
     * Test of doIndexing method, of class fr.paris.lutece.portal.web.search.SearchIndexationJspBean.
     */
    public void testDoIndexing( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        SearchIndexationJspBean instance = new SearchIndexationJspBean( );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/search/manage_search_indexation.html" ) );

        Utils.registerAdminUserWithRigth( request, new AdminUser( ), SearchIndexationJspBean.RIGHT_INDEXER );
        instance.init( request, SearchIndexationJspBean.RIGHT_INDEXER );
        instance.doIndexing( request );
        assertTrue( testIndexer._bIndexDocumentsCalled );
    }

    public void testDoIndexingInvalidToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        SearchIndexationJspBean instance = new SearchIndexationJspBean( );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/search/manage_search_indexation.html" ) + "b" );

        Utils.registerAdminUserWithRigth( request, new AdminUser( ), SearchIndexationJspBean.RIGHT_INDEXER );
        instance.init( request, SearchIndexationJspBean.RIGHT_INDEXER );
        try
        {
            instance.doIndexing( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertFalse( testIndexer._bIndexDocumentsCalled );
        }
    }

    public void testDoIndexingNoToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        SearchIndexationJspBean instance = new SearchIndexationJspBean( );

        Utils.registerAdminUserWithRigth( request, new AdminUser( ), SearchIndexationJspBean.RIGHT_INDEXER );
        instance.init( request, SearchIndexationJspBean.RIGHT_INDEXER );
        try
        {
            instance.doIndexing( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertFalse( testIndexer._bIndexDocumentsCalled );
        }
    }
}
