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
package fr.paris.lutece.portal.service.search;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.page.PageHome;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.page.IPageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;


/**
 * Indexer service for pages
 */
public class PageIndexer implements SearchIndexer
{
    public static final String INDEX_TYPE_PAGE = "Page";
    public static final String INDEXER_NAME = "PageIndexer";
    protected static final String PROPERTY_PAGE_BASE_URL = "search.pageIndexer.baseUrl";
    protected static final String PROPERTY_SEARCH_PAGE_URL = "search.pageSearch.baseUrl";
    protected static final String PROPERTY_INDEXER_ENABLE = "search.pageIndexer.enable";
    protected static final String PARAMETER_PAGE_ID = "page_id";
    protected static IPageService _pageService = (IPageService) SpringContextService.getBean( "pageService" );
    private static final String INDEXER_DESCRIPTION = "Indexer service for pages";
    private static final String INDEXER_VERSION = "1.0.0";

    /**
     * {@inheritDoc}
     */
    @Override
    public void indexDocuments(  ) throws IOException, InterruptedException, SiteMessageException
    {
        String strPageBaseUrl = AppPropertiesService.getProperty( PROPERTY_PAGE_BASE_URL );
        List<Page> listPages = PageHome.getAllPages(  );

        for ( Page page : listPages )
        {
            UrlItem url = new UrlItem( strPageBaseUrl );
            url.addParameter( PARAMETER_PAGE_ID, page.getId(  ) );

            Document doc = null;

            try
            {
                doc = getDocument( page, url.getUrl(  ) );
            }
            catch ( Exception e )
            {
                String strMessage = "Page ID : " + page.getId(  );
                IndexationService.error( this, e, strMessage );
            }

            if ( doc != null )
            {
                IndexationService.write( doc );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Document> getDocuments( String nIdDocument )
        throws IOException, InterruptedException, SiteMessageException
    {
        ArrayList<Document> listDocuments = new ArrayList<Document>(  );
        String strPageBaseUrl = AppPropertiesService.getProperty( PROPERTY_PAGE_BASE_URL );

        Page page = PageHome.getPage( Integer.parseInt( nIdDocument ) );

        if ( ( page != null ) && ( page.getId(  ) != 0 ) )
        {
            UrlItem url = new UrlItem( strPageBaseUrl );
            url.addParameter( PARAMETER_PAGE_ID, page.getId(  ) );

            Document doc = getDocument( page, url.getUrl(  ) );
            listDocuments.add( doc );
        }

        return listDocuments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName(  )
    {
        return INDEXER_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion(  )
    {
        return INDEXER_VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription(  )
    {
        return INDEXER_DESCRIPTION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnable(  )
    {
        String strEnable = AppPropertiesService.getProperty( PROPERTY_INDEXER_ENABLE, Boolean.TRUE.toString(  ) );

        return ( strEnable.equalsIgnoreCase( Boolean.TRUE.toString(  ) ) );
    }

    /**
     * Builds a document which will be used by Lucene during the indexing of the
     * pages of the site with the following
     * fields : summary, uid, url, contents, title and description.
     * @return the built Document
     * @param strUrl The base URL for documents
     * @param page the page to index
     * @throws IOException The IO Exception
     * @throws InterruptedException The InterruptedException
     * @throws SiteMessageException occurs when a site message need to be
     *             displayed
     */
    protected Document getDocument( Page page, String strUrl )
        throws IOException, InterruptedException, SiteMessageException
    {
        FieldType ft = new FieldType( StringField.TYPE_STORED );
        ft.setOmitNorms( false );

        FieldType ftNotStored = new FieldType( StringField.TYPE_NOT_STORED );
        ftNotStored.setOmitNorms( false );
        ftNotStored.setTokenized( false );

        // make a new, empty document
        Document doc = new Document(  );

        // Add the url as a field named "url".  Use an UnIndexed field, so
        // that the url is just stored with the document, but is not searchable.
        doc.add( new Field( SearchItem.FIELD_URL, strUrl, ft ) );

        // Add the last modified date of the file a field named "modified".
        // Use a field that is indexed (i.e. searchable), but don't tokenize
        // the field into words.
        String strDate = DateTools.dateToString( page.getDateUpdate(  ), DateTools.Resolution.DAY );
        doc.add( new Field( SearchItem.FIELD_DATE, strDate, ft ) );

        // Add the uid as a field, so that index can be incrementally maintained.
        // This field is not stored with document, it is indexed, but it is not
        // tokenized prior to indexing.
        String strIdPage = String.valueOf( page.getId(  ) );
        doc.add( new Field( SearchItem.FIELD_UID, strIdPage, ftNotStored ) );

        String strPageContent = _pageService.getPageContent( page.getId(  ), 0, null );
        ContentHandler handler = new BodyContentHandler(  );
        Metadata metadata = new Metadata(  );

        try
        {
            new HtmlParser(  ).parse( new ByteArrayInputStream( strPageContent.getBytes(  ) ), handler, metadata,
                new ParseContext(  ) );
        }
        catch ( SAXException e )
        {
            throw new AppException( "Error during page parsing." );
        }
        catch ( TikaException e )
        {
            throw new AppException( "Error during page parsing." );
        }

        //the content of the article is recovered in the parser because this one
        //had replaced the encoded caracters (as &eacute;) by the corresponding special caracter (as ?)
        StringBuilder sb = new StringBuilder( handler.toString(  ) );

        // Add the tag-stripped contents as a Reader-valued Text field so it will
        // get tokenized and indexed.
        StringBuilder sbFieldContent = new StringBuilder(  );
        StringBuilder sbFieldMetadata = new StringBuilder(  );
        sbFieldContent.append( page.getName(  ) ).append( " " ).append( sb.toString(  ) );

        // Add the metadata description of the page if it exists
        if ( page.getDescription(  ) != null )
        {
            sbFieldContent.append( " " ).append( page.getDescription(  ) );
        }

        // Add the metadata keywords of the page if it exists
        String strMetaKeywords = page.getMetaKeywords(  );

        if ( StringUtils.isNotBlank( strMetaKeywords ) )
        {
            sbFieldContent.append( " " ).append( strMetaKeywords );
            sbFieldMetadata.append( strMetaKeywords );
        }

        doc.add( new Field( SearchItem.FIELD_CONTENTS, sbFieldContent.toString(  ), TextField.TYPE_NOT_STORED ) );

        if ( StringUtils.isNotBlank( page.getMetaDescription(  ) ) )
        {
            if ( sbFieldMetadata.length(  ) > 0 )
            {
                sbFieldMetadata.append( " " );
            }

            sbFieldMetadata.append( page.getMetaDescription(  ) );
        }

        if ( sbFieldMetadata.length(  ) > 0 )
        {
            doc.add( new StringField( SearchItem.FIELD_METADATA, sbFieldMetadata.toString(  ), Field.Store.NO ) );
        }

        // Add the title as a separate Text field, so that it can be searched
        // separately.
        doc.add( new Field( SearchItem.FIELD_TITLE, page.getName(  ), ft ) );

        if ( StringUtils.isNotBlank( page.getDescription(  ) ) )
        {
            // Add the summary as an UnIndexed field, so that it is stored and returned
            // with hit documents for display.
            doc.add( new StoredField( SearchItem.FIELD_SUMMARY, page.getDescription(  ) ) );
        }

        doc.add( new Field( SearchItem.FIELD_TYPE, INDEX_TYPE_PAGE, ft ) );
        doc.add( new Field( SearchItem.FIELD_ROLE, page.getRole(  ), ft ) );

        // return the document
        return doc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getListType(  )
    {
        List<String> listType = new ArrayList<String>(  );
        listType.add( INDEX_TYPE_PAGE );

        return listType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSpecificSearchAppUrl(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_SEARCH_PAGE_URL );
    }
}
