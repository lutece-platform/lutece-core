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
package fr.paris.lutece.portal.service.search;

import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

//import org.xml.sax.XMLReader;
import java.io.IOException;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class provides management methods for indexing
 */
public final class IndexationService
{
    // Constants corresponding to the variables defined in the lutece.properties file
    public static final String PATH_INDEX = "search.lucene.indexPath";
    public static final String PARAM_FORCING = "forcing";
    private static final String PROPERTY_WRITER_MERGE_FACTOR = "search.lucene.writer.mergeFactor";
    private static final String PROPERTY_WRITER_MAX_FIELD_LENGTH = "search.lucene.writer.maxFieldLength";
    private static final String PROPERTY_ANALYSER_CLASS_NAME = "search.lucene.analyser.className";
    private static final int DEFAULT_WRITER_MERGE_FACTOR = 20;
    private static final int DEFAULT_WRITER_MAX_FIELD_LENGTH = 1000000;
    private static String _strIndex;
    private static int _nWriterMergeFactor;
    private static int _nWriterMaxFieldLength;
    private static Analyzer _analyzer;
    private static Map<String, SearchIndexer> _mapIndexers = new HashMap<String, SearchIndexer>(  );

    /**
     * The private constructor
     */
    private IndexationService(  )
    {
    }

    /**
     * Initalizes the service
     * @throws LuteceInitException If an error occured
     */
    public static void init(  ) throws LuteceInitException
    {
        // Read configuration properties
        _strIndex = AppPathService.getPath( PATH_INDEX );

        if ( ( _strIndex == null ) || ( _strIndex.equals( "" ) ) )
        {
            throw new LuteceInitException( "Lucene index path not found in lucene.properties", null );
        }

        _nWriterMergeFactor = AppPropertiesService.getPropertyInt( PROPERTY_WRITER_MERGE_FACTOR,
                DEFAULT_WRITER_MERGE_FACTOR );
        _nWriterMaxFieldLength = AppPropertiesService.getPropertyInt( PROPERTY_WRITER_MAX_FIELD_LENGTH,
                DEFAULT_WRITER_MAX_FIELD_LENGTH );

        String strAnalyserClassName = AppPropertiesService.getProperty( PROPERTY_ANALYSER_CLASS_NAME );

        if ( ( _strIndex == null ) || ( _strIndex.equals( "" ) ) )
        {
            throw new LuteceInitException( "Analyser class name not found in lucene.properties", null );
        }

        try
        {
            _analyzer = (Analyzer) Class.forName( strAnalyserClassName ).newInstance(  );
        }
        catch ( Exception e )
        {
            throw new LuteceInitException( "Failed to load Lucene Analyzer class", e );
        }
    }

    /**
     * Process the indexing
     * @param bCreate Force creating the index
     * @return the result log of the indexing
     */
    public static synchronized String processIndexing( boolean bCreate )
    {
        // String buffer for building the response page;
        StringBuffer sbLogs = new StringBuffer(  );

        IndexWriter writer = null;

        try
        {
            sbLogs.append( "\r\nIndexing all contents ...\r\n" );

            Date start = new Date(  );
            writer = new IndexWriter( _strIndex, _analyzer, bCreate );
            writer.setMergeFactor( _nWriterMergeFactor );
            writer.setMaxFieldLength( _nWriterMaxFieldLength );

            for ( SearchIndexer indexer : _mapIndexers.values(  ) )
            {
                if ( indexer.isEnable(  ) )
                {
                    sbLogs.append( "\r\n<strong>Indexer : " );
                    sbLogs.append( indexer.getName(  ) );
                    sbLogs.append( " - " );
                    sbLogs.append( indexer.getDescription(  ) );
                    sbLogs.append( "</strong>\r\n" );

                    List<Document> listDocuments = indexer.getDocuments(  );

                    for ( Document doc : listDocuments )
                    {
                        writer.addDocument( doc );
                        sbLogs.append( "Indexing " );
                        sbLogs.append( doc.get( SearchItem.FIELD_TYPE ) );
                        sbLogs.append( " #" );
                        sbLogs.append( doc.get( SearchItem.FIELD_UID ) );
                        sbLogs.append( " - " );
                        sbLogs.append( doc.get( SearchItem.FIELD_TITLE ) );
                        sbLogs.append( "\r\n" );
                    }
                }
            }

            sbLogs.append( "\r\nOptimization of the index for the current site...\r\n\r\n" );
            writer.optimize(  );

            Date end = new Date(  );
            sbLogs.append( "Duration of the treatment : " );
            sbLogs.append( end.getTime(  ) - start.getTime(  ) );
            sbLogs.append( " milliseconds\r\n" );
        }
        catch ( Exception e )
        {
            sbLogs.append( " caught a " );
            sbLogs.append( e.getClass(  ) );
            sbLogs.append( "\n with message: " );
            sbLogs.append( e.getMessage(  ) );
            sbLogs.append( "\r\n" );
            AppLogService.error( "Indexing error : " + e.getMessage(  ), e );
        }
        finally
        {
            try
            {
                if ( writer != null )
                {
                    writer.close(  );
                }
            }
            catch ( IOException e )
            {
                AppLogService.error( e.getMessage(  ), e );
            }
        }

        return sbLogs.toString(  );
    }

    /**
     * Register an indexer
     * @param indexer The indexer to add to the registry
     */
    public static void registerIndexer( SearchIndexer indexer )
    {
        _mapIndexers.put( indexer.getName(  ), indexer );
        AppLogService.info( "New search indexer registered : " + indexer.getName(  ) );
    }

    /**
     * Gets the current index
     * @return The index
     */
    public static String getIndex(  )
    {
        return _strIndex;
    }

    /**
     * Gets the current analyser
     * @return The analyser
     */
    public static Analyzer getAnalyser(  )
    {
        return _analyzer;
    }

    /**
     * Returns all search indexers
     * @return A collection of indexers
     */
    public static Collection<SearchIndexer> getIndexers(  )
    {
        return _mapIndexers.values(  );
    }
}
