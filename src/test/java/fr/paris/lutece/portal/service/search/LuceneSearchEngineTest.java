package fr.paris.lutece.portal.service.search;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.MokeLuteceAuthentication;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * Test the LuceneSearchEngine class
 */
public class LuceneSearchEngineTest extends LuteceTestCase
{

    private static final String BEAN_SEARCH_ENGINE = "searchEngine";

    private static boolean firstRun = true;
    private static SearchEngine _engine;

    /* mimic initialization in IndexationService.processIndexing */
    private IndexWriter getIndexWriter() throws Exception {
            Directory dir = IndexationService.getDirectoryIndex( );
            IndexWriterConfig conf = new IndexWriterConfig( Version.LUCENE_46, IndexationService.getAnalyser() );
            conf.setOpenMode( OpenMode.CREATE );
            return new IndexWriter(dir, conf);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        if(firstRun) {
            firstRun = false;

            _engine = SpringContextService.getBean( BEAN_SEARCH_ENGINE);

            FieldType ft = new FieldType( StringField.TYPE_STORED );
            ft.setOmitNorms( false );
            Document doc = new Document( );
            doc.add( new Field( SearchItem.FIELD_DATE, "2014-06-06", ft ) );
            doc.add( new Field( SearchItem.FIELD_CONTENTS, "lutecefoo lutecebar", TextField.TYPE_NOT_STORED ) );
            doc.add( new StringField( SearchItem.FIELD_METADATA, "lutecetag", Field.Store.NO ) );

            doc.add( new Field( SearchItem.FIELD_TYPE, "lutecetype", ft ) );
            doc.add( new Field( SearchItem.FIELD_ROLE, "role1", ft ) );

            //Not using IndexationService.write(doc) because it needs to be
            //called by IndexationService.processIndexing() (or else it throws null pointer exception)
            IndexWriter indexWriter = getIndexWriter();
            indexWriter.addDocument(doc);
            indexWriter.close();
        }
    }

    public void testSearchSimpleMatch() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        List<SearchResult> listResults = _engine.getSearchResults( "lutecefoo", request );
        assertTrue("The search results list should have one element. Got : " + listResults, listResults != null && listResults.size() == 1);
    }

    public void testSearchSimpleNoMatch() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        List<SearchResult> listResults = _engine.getSearchResults( "lutecebadfoo", request );
        assertTrue("The search results list should have no elements. Got : " + listResults, listResults != null && listResults.size() == 0);
    }

    public void testSearchDateMatch() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("date_after","01/01/2014");
        request.setParameter("date_before","01/10/2015");
        List<SearchResult> listResults = _engine.getSearchResults( "lutecefoo", request );
        assertTrue("The search results list should have one element. Got : " + listResults, listResults != null && listResults.size() == 1);
    }

    public void testSearchDateNoMatch() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("date_after","01/01/2010");
        request.setParameter("date_before","01/10/2011");
        List<SearchResult> listResults = _engine.getSearchResults( "lutecefoo", request );
        assertTrue("The search results list should have no elements. Got : " + listResults, listResults != null && listResults.size() == 0);
    }

    public void testSearchTypeMatch() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("type_filter","lutecetype");
        List<SearchResult> listResults = _engine.getSearchResults( "lutecefoo", request );
        assertTrue("The search results list should have one element. Got : " + listResults, listResults != null && listResults.size() == 1);
    }

    public void testSearchTypeNoMatch() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("type_filter","lutecebadtype");
        List<SearchResult> listResults = _engine.getSearchResults( "lutecefoo", request );
        assertTrue("The search results list should have no elements. Got : " + listResults, listResults != null && listResults.size() == 0);
    }

    public void testSearchTagMatch() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("tag_filter","lutecetag");
        List<SearchResult> listResults = _engine.getSearchResults( "lutecetag", request );
        assertTrue("The search results list should have one element. Got : " + listResults, listResults != null && listResults.size() == 1);
    }

    public void testSearchTagNoMatch() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("tag_filter","lutecetag");
        List<SearchResult> listResults = _engine.getSearchResults( "lutecebadtag", request );
        assertTrue("The search results list should have no elements. Got : " + listResults, listResults != null && listResults.size() == 0);
    }

    public void testSearchUserMatch() throws Exception {

        //XXX copy pasted from PortalMenuServiceTest
        boolean authStatus;
        authStatus = enableAuthentication();
        try {
            LuteceUser user = new LuteceUser( "junit", SecurityService.getInstance( ).getAuthenticationService( ) ) {};

            user.setRoles( Arrays.asList( "role1" ) );
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.getSession( ).setAttribute( "lutece_user", user );

            List<SearchResult> listResults = _engine.getSearchResults( "lutecefoo", request );
            assertTrue("The search results list should have one element. Got : " + listResults, listResults != null && listResults.size() == 1);
        } finally {
            restoreAuthentication( authStatus );
        }
    }

    public void testSearchUserNoMatch() throws Exception {

        //XXX copy pasted from PortalMenuServiceTest
        boolean authStatus;
        authStatus = enableAuthentication();
        try {
            LuteceUser user = new LuteceUser( "junit", SecurityService.getInstance( ).getAuthenticationService( ) ) {};

            user.setRoles( Arrays.asList( "role2" ) );
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.getSession( ).setAttribute( "lutece_user", user );

            List<SearchResult> listResults = _engine.getSearchResults( "lutecefoo", request );
            assertTrue("The search results list should have no elements. Got : " + listResults, listResults != null && listResults.size() == 0);
        } finally {
            restoreAuthentication( authStatus );
        }
    }

    ///XXX refactor, this is copy pasted from PortalMenuServiceTest
    private void restoreAuthentication( boolean status ) throws IOException, LuteceInitException
    {
        if ( !status )
        {
            File luteceProperties = new File( getResourcesDir( ), "WEB-INF/conf/lutece.properties" );
            Properties props = new Properties( );
            InputStream is = new FileInputStream( luteceProperties );
            props.load( is );
            is.close( );
            props.remove( "mylutece.authentication.enable" );
            props.remove( "mylutece.authentication.class" );

            OutputStream os = new FileOutputStream( luteceProperties );
            props.store( os, "saved for junit " + this.getClass( ).getCanonicalName( ) );
            os.close( );
            AppPropertiesService.reloadAll( );
            SecurityService.init( );
        }
    }

    ///XXX refactor, this is copy pasted from PortalMenuServiceTest
    private boolean enableAuthentication( ) throws IOException, LuteceInitException
    {
        boolean status = SecurityService.isAuthenticationEnable( );

        if ( !status )
        {
            File luteceProperties = new File( getResourcesDir( ), "WEB-INF/conf/lutece.properties" );
            Properties props = new Properties( );
            InputStream is = new FileInputStream( luteceProperties );
            props.load( is );
            is.close( );
            props.setProperty( "mylutece.authentication.enable", "true" );
            props.setProperty( "mylutece.authentication.class", MokeLuteceAuthentication.class.getName( ) );

            OutputStream os = new FileOutputStream( luteceProperties );
            props.store( os, "saved for junit " + this.getClass( ).getCanonicalName( ) );
            os.close( );
            AppPropertiesService.reloadAll( );
            SecurityService.init( );
        }

        return status;
    }
}
