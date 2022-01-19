/*
 * Copyright (c) 2002-2022, City of Paris
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

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.rbac.RBACRole;
import fr.paris.lutece.portal.business.rbac.RBACRoleHome;
import fr.paris.lutece.portal.business.search.SearchParameterHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.web.dashboard.AdminDashboardJspBean;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.Utils;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

public class SearchJspBeanTest extends LuteceTestCase
{
    private static final String PARAMETER_DATE_FILTER = "date_filter";
    private static final String PARAMETER_DEFAULT_OPERATOR = "default_operator";
    private static final String PARAMETER_HELP_MESSAGE = "help_message";
    private static final String PARAMETER_TAGLIST = "taglist";
    private static final String PARAMETER_TAG_FILTER = "tag_filter";
    private static final String PARAMETER_TYPE_FILTER = "type_filter";
    private SearchJspBean _bean;
    private ReferenceList _origSearchParameters;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        _bean = new SearchJspBean( );
        _origSearchParameters = SearchParameterHome.findParametersList( );
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        for ( ReferenceItem param : _origSearchParameters )
        {
            SearchParameterHome.update( param );
        }
        super.tearDown( );
    }

    public void testDoModifyAdvancedParameters( ) throws AccessDeniedException
    {
        String strTypeFilter;
        if ( "none".equals( SearchParameterHome.findByKey( PARAMETER_TYPE_FILTER ).getName( ) ) )
        {
            strTypeFilter = "option";
        }
        else
        {
            strTypeFilter = "none";
        }

        String strDefaultOperator;
        if ( "OR".equals( SearchParameterHome.findByKey( PARAMETER_DEFAULT_OPERATOR ).getName( ) ) )
        {
            strDefaultOperator = "AND";
        }
        else
        {
            strDefaultOperator = "OR";
        }

        String strHelpMessage = getRandomName( );

        String strDateFilter;
        if ( "0".equals( SearchParameterHome.findByKey( PARAMETER_DATE_FILTER ).getName( ) ) )
        {
            strDateFilter = "1";
        }
        else
        {
            strDateFilter = "0";
        }

        String strTagFilter;
        if ( "0".equals( SearchParameterHome.findByKey( PARAMETER_TAG_FILTER ).getName( ) ) )
        {
            strTagFilter = "1";
        }
        else
        {
            strTagFilter = "0";
        }

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_TYPE_FILTER, strTypeFilter );
        request.addParameter( PARAMETER_DEFAULT_OPERATOR, strDefaultOperator );
        request.addParameter( PARAMETER_HELP_MESSAGE, strHelpMessage );
        request.addParameter( PARAMETER_DATE_FILTER, strDateFilter );
        request.addParameter( PARAMETER_TAG_FILTER, strTagFilter );
        AdminUser user = new AdminUser( );
        Map<String, RBACRole> roles = new HashMap<>( );
        for ( RBACRole role : RBACRoleHome.findAll( ) )
        {
            roles.put( role.getKey( ), role );
        }
        user.addRoles( roles );
        Utils.registerAdminUserWithRigth( request, user, "CORE_SEARCH_MANAGEMENT" );
        _bean.init( request, "CORE_SEARCH_MANAGEMENT" );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, AdminDashboardJspBean.TEMPLATE_MANAGE_DASHBOARDS ) );

        _bean.doModifyAdvancedParameters( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNull( message );
        for ( ReferenceItem param : _origSearchParameters )
        {
            if ( PARAMETER_TAGLIST.equals( param.getCode( ) ) )
            {
                continue;
            }
            assertFalse( "SearchParameter " + param.getCode( ) + " stayed equal to " + param.getName( ),
                    param.getName( ).equals( SearchParameterHome.findByKey( param.getCode( ) ).getName( ) ) );
        }
        assertEquals( strTypeFilter, SearchParameterHome.findByKey( PARAMETER_TYPE_FILTER ).getName( ) );
        assertEquals( strDefaultOperator, SearchParameterHome.findByKey( PARAMETER_DEFAULT_OPERATOR ).getName( ) );
        assertEquals( strHelpMessage, SearchParameterHome.findByKey( PARAMETER_HELP_MESSAGE ).getName( ) );
        assertEquals( strDateFilter, SearchParameterHome.findByKey( PARAMETER_DATE_FILTER ).getName( ) );
        assertEquals( strTagFilter, SearchParameterHome.findByKey( PARAMETER_TAG_FILTER ).getName( ) );
    }

    public void testDoModifyAdvancedParametersInvalidToken( ) throws AccessDeniedException
    {
        String strTypeFilter;
        if ( "none".equals( SearchParameterHome.findByKey( PARAMETER_TYPE_FILTER ).getName( ) ) )
        {
            strTypeFilter = "option";
        }
        else
        {
            strTypeFilter = "none";
        }

        String strDefaultOperator;
        if ( "OR".equals( SearchParameterHome.findByKey( PARAMETER_DEFAULT_OPERATOR ).getName( ) ) )
        {
            strDefaultOperator = "AND";
        }
        else
        {
            strDefaultOperator = "OR";
        }

        String strHelpMessage = getRandomName( );

        String strDateFilter;
        if ( "0".equals( SearchParameterHome.findByKey( PARAMETER_DATE_FILTER ).getName( ) ) )
        {
            strDateFilter = "1";
        }
        else
        {
            strDateFilter = "0";
        }

        String strTagFilter;
        if ( "0".equals( SearchParameterHome.findByKey( PARAMETER_TAG_FILTER ).getName( ) ) )
        {
            strTagFilter = "1";
        }
        else
        {
            strTagFilter = "0";
        }

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_TYPE_FILTER, strTypeFilter );
        request.addParameter( PARAMETER_DEFAULT_OPERATOR, strDefaultOperator );
        request.addParameter( PARAMETER_HELP_MESSAGE, strHelpMessage );
        request.addParameter( PARAMETER_DATE_FILTER, strDateFilter );
        request.addParameter( PARAMETER_TAG_FILTER, strTagFilter );
        AdminUser user = new AdminUser( );
        Map<String, RBACRole> roles = new HashMap<>( );
        for ( RBACRole role : RBACRoleHome.findAll( ) )
        {
            roles.put( role.getKey( ), role );
        }
        user.addRoles( roles );
        Utils.registerAdminUserWithRigth( request, user, "CORE_SEARCH_MANAGEMENT" );
        _bean.init( request, "CORE_SEARCH_MANAGEMENT" );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/search/manage_advanced_parameters.html" ) + "b" );

        try
        {
            _bean.doModifyAdvancedParameters( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            for ( ReferenceItem param : _origSearchParameters )
            {
                assertEquals( param.getName( ), SearchParameterHome.findByKey( param.getCode( ) ).getName( ) );
            }
        }
    }

    public void testDoModifyAdvancedParametersNoToken( ) throws AccessDeniedException
    {
        String strTypeFilter;
        if ( "none".equals( SearchParameterHome.findByKey( PARAMETER_TYPE_FILTER ).getName( ) ) )
        {
            strTypeFilter = "option";
        }
        else
        {
            strTypeFilter = "none";
        }

        String strDefaultOperator;
        if ( "OR".equals( SearchParameterHome.findByKey( PARAMETER_DEFAULT_OPERATOR ).getName( ) ) )
        {
            strDefaultOperator = "AND";
        }
        else
        {
            strDefaultOperator = "OR";
        }

        String strHelpMessage = getRandomName( );

        String strDateFilter;
        if ( "0".equals( SearchParameterHome.findByKey( PARAMETER_DATE_FILTER ).getName( ) ) )
        {
            strDateFilter = "1";
        }
        else
        {
            strDateFilter = "0";
        }

        String strTagFilter;
        if ( "0".equals( SearchParameterHome.findByKey( PARAMETER_TAG_FILTER ).getName( ) ) )
        {
            strTagFilter = "1";
        }
        else
        {
            strTagFilter = "0";
        }

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_TYPE_FILTER, strTypeFilter );
        request.addParameter( PARAMETER_DEFAULT_OPERATOR, strDefaultOperator );
        request.addParameter( PARAMETER_HELP_MESSAGE, strHelpMessage );
        request.addParameter( PARAMETER_DATE_FILTER, strDateFilter );
        request.addParameter( PARAMETER_TAG_FILTER, strTagFilter );
        AdminUser user = new AdminUser( );
        Map<String, RBACRole> roles = new HashMap<>( );
        for ( RBACRole role : RBACRoleHome.findAll( ) )
        {
            roles.put( role.getKey( ), role );
        }
        user.addRoles( roles );
        Utils.registerAdminUserWithRigth( request, user, "CORE_SEARCH_MANAGEMENT" );
        _bean.init( request, "CORE_SEARCH_MANAGEMENT" );

        try
        {
            _bean.doModifyAdvancedParameters( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            for ( ReferenceItem param : _origSearchParameters )
            {
                assertEquals( param.getName( ), SearchParameterHome.findByKey( param.getCode( ) ).getName( ) );
            }
        }
    }

    private String getRandomName( )
    {
        Random rand = new SecureRandom( );
        BigInteger bigInt = new BigInteger( 128, rand );
        return "junit" + bigInt.toString( 36 );
    }

}
