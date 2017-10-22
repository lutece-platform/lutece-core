package fr.paris.lutece.portal.web.search;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.search.SearchParameterHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.PasswordResetException;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.Utils;
import fr.paris.lutece.util.ReferenceItem;

public class SearchJspBeanTest extends LuteceTestCase
{
    private static final String PARAMETER_TAGLIST = "taglist";
    private SearchJspBean _bean;
    private String _strOrigTagList;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        _bean = new SearchJspBean( );
        _strOrigTagList = SearchParameterHome.findByKey( PARAMETER_TAGLIST ).getName( );
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        ReferenceItem param = new ReferenceItem( );
        param.setCode( PARAMETER_TAGLIST );

        param.setName( StringUtils.isNotBlank( _strOrigTagList ) ? _strOrigTagList : "" );
        SearchParameterHome.update( param );
        super.tearDown( );
    }

    public void testGetModifyTagList( ) throws PasswordResetException, AccessDeniedException
    {
        HttpServletRequest request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), "CORE_SEARCH_MANAGEMENT" );
        _bean.init( request, "CORE_SEARCH_MANAGEMENT" );
        assertNotNull( _bean.getModifyTagList( request ) );
    }

    public void testDoModifyTagList( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        String taglist = getRandomName( );
        request.addParameter( PARAMETER_TAGLIST, taglist );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/search/modify_taglist.html" ) );

        assertFalse( taglist.equals( SearchParameterHome.findByKey( PARAMETER_TAGLIST ).getName( ) ) );

        _bean.doModifyTagList( request );

        assertEquals( taglist, SearchParameterHome.findByKey( PARAMETER_TAGLIST ).getName( ) );
    }

    public void testDoModifyTagListInvalidToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        String taglist = getRandomName( );
        request.addParameter( PARAMETER_TAGLIST, taglist );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/search/modify_taglist.html" ) + "b" );

        assertFalse( taglist.equals( SearchParameterHome.findByKey( PARAMETER_TAGLIST ).getName( ) ) );
        try
        {
            _bean.doModifyTagList( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertFalse( taglist.equals( SearchParameterHome.findByKey( PARAMETER_TAGLIST ).getName( ) ) );
        }
    }

    public void testDoModifyTagListNoToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        String taglist = getRandomName( );
        request.addParameter( PARAMETER_TAGLIST, taglist );

        assertFalse( taglist.equals( SearchParameterHome.findByKey( PARAMETER_TAGLIST ).getName( ) ) );
        try
        {
            _bean.doModifyTagList( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertFalse( taglist.equals( SearchParameterHome.findByKey( PARAMETER_TAGLIST ).getName( ) ) );
        }
    }

    private String getRandomName( )
    {
        Random rand = new SecureRandom( );
        BigInteger bigInt = new BigInteger( 128, rand );
        return "junit" + bigInt.toString( 36 );
    }

}
