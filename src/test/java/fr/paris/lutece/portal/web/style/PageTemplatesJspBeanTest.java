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
package fr.paris.lutece.portal.web.style;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.style.PageTemplate;
import fr.paris.lutece.portal.business.style.PageTemplateHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.ISecurityTokenService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;

/**
 * PageTemplatesJspBeanTest Test Class
 *
 */
public class PageTemplatesJspBeanTest extends LuteceTestCase
{
    private static final String TEST_PAGE_TEMPLATE_ID = "1"; // Page template one column
    private MockHttpServletRequest request;
    private @Inject ISecurityTokenService _securityTokenService;

    @BeforeEach
    protected void setUp( ) throws Exception
    {
        request = new MockHttpServletRequest( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), PageTemplatesJspBean.RIGHT_MANAGE_PAGE_TEMPLATES );
    }

    /**
     * Gets a CDI-managed instance of the controller under test.
     *
     * @return the PageTemplatesJspBean instance
     */
    private PageTemplatesJspBean getInstance( )
    {
        return CDI.current( ).select( PageTemplatesJspBean.class ).get( );
    }

    @Test
    public void testGetManagePageTemplate( ) throws AccessDeniedException
    {
        assertNotNull( getInstance( ).processController( request, new MockHttpServletResponse( ) ) );
    }

    @Test
    public void testGetModifyPageTemplate( ) throws AccessDeniedException
    {
        request.addParameter( MVCUtils.PARAMETER_VIEW, "modifyPageTemplate" );
        request.addParameter( Parameters.PAGE_TEMPLATE_ID, TEST_PAGE_TEMPLATE_ID );
        assertNotNull( getInstance( ).processController( request, new MockHttpServletResponse( ) ) );
    }

    @Test
    public void testDoModifyPageTemplate( ) throws AccessDeniedException
    {
        final String desc = getRandomName( );
        PageTemplate pageTemplate = new PageTemplate( );
        pageTemplate.setDescription( desc );
        PageTemplateHome.create( pageTemplate );

        MultipartHttpServletRequest multipartRequest = buildModifyRequest( pageTemplate.getId( ), desc + "mod",
                _securityTokenService.getToken( request, "admin/style/modify_page_template.html" ) );
        try
        {
            assertEquals( desc, PageTemplateHome.findByPrimaryKey( pageTemplate.getId( ) ).getDescription( ) );
            getInstance( ).processController( multipartRequest, new MockHttpServletResponse( ) );
            assertEquals( desc + "mod", PageTemplateHome.findByPrimaryKey( pageTemplate.getId( ) ).getDescription( ) );
        }
        finally
        {
            PageTemplateHome.remove( pageTemplate.getId( ) );
        }
    }

    @Test
    public void testDoModifyPageTemplateInvalidToken( ) throws AccessDeniedException
    {
        final String desc = getRandomName( );
        PageTemplate pageTemplate = new PageTemplate( );
        pageTemplate.setDescription( desc );
        PageTemplateHome.create( pageTemplate );

        MultipartHttpServletRequest multipartRequest = buildModifyRequest( pageTemplate.getId( ), desc + "mod",
                _securityTokenService.getToken( request, "admin/style/modify_page_template.html" ) + "b" );
        try
        {
            assertEquals( desc, PageTemplateHome.findByPrimaryKey( pageTemplate.getId( ) ).getDescription( ) );
            getInstance( ).processController( multipartRequest, new MockHttpServletResponse( ) );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( desc, PageTemplateHome.findByPrimaryKey( pageTemplate.getId( ) ).getDescription( ) );
        }
        finally
        {
            PageTemplateHome.remove( pageTemplate.getId( ) );
        }
    }

    @Test
    public void testDoModifyPageTemplateNoToken( ) throws AccessDeniedException
    {
        final String desc = getRandomName( );
        PageTemplate pageTemplate = new PageTemplate( );
        pageTemplate.setDescription( desc );
        PageTemplateHome.create( pageTemplate );

        MultipartHttpServletRequest multipartRequest = buildModifyRequest( pageTemplate.getId( ), desc + "mod", null );
        try
        {
            assertEquals( desc, PageTemplateHome.findByPrimaryKey( pageTemplate.getId( ) ).getDescription( ) );
            getInstance( ).processController( multipartRequest, new MockHttpServletResponse( ) );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( desc, PageTemplateHome.findByPrimaryKey( pageTemplate.getId( ) ).getDescription( ) );
        }
        finally
        {
            PageTemplateHome.remove( pageTemplate.getId( ) );
        }
    }

    /**
     * Builds the multipart request used to drive the modifyPageTemplate action.
     *
     * @param nId
     *            the page template id
     * @param strDescription
     *            the new description
     * @param strToken
     *            the security token, or null to omit it
     * @return the multipart request
     */
    private MultipartHttpServletRequest buildModifyRequest( int nId, String strDescription, String strToken )
    {
        Map<String, String [ ]> parameters = new HashMap<>( );
        parameters.put( MVCUtils.PARAMETER_ACTION, new String [ ] {
                "modifyPageTemplate"
        } );
        parameters.put( Parameters.PAGE_TEMPLATE_ID, new String [ ] {
                Integer.toString( nId )
        } );
        parameters.put( Parameters.PAGE_TEMPLATE_DESCRIPTION, new String [ ] {
                strDescription
        } );
        if ( strToken != null )
        {
            parameters.put( SecurityTokenService.PARAMETER_TOKEN, new String [ ] {
                    strToken
            } );
        }
        return new MultipartHttpServletRequest( request, Collections.emptyMap( ), parameters );
    }

    @Test
    public void testGetConfirmRemovePageTemplate( ) throws AccessDeniedException
    {
        request.addParameter( MVCUtils.PARAMETER_VIEW, "confirmRemovePageTemplate" );
        request.addParameter( Parameters.PAGE_TEMPLATE_ID, TEST_PAGE_TEMPLATE_ID );
        getInstance( ).processController( request, new MockHttpServletResponse( ) );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        if ( message.getRequestParameters( ) != null )
        {
            assertNotNull( message.getRequestParameters( ).get( SecurityTokenService.PARAMETER_TOKEN ) );
            assertEquals( TEST_PAGE_TEMPLATE_ID, message.getRequestParameters( ).get( Parameters.PAGE_TEMPLATE_ID ) );
        }
    }

    @Test
    public void testDoRemovePageTemplate( ) throws AccessDeniedException
    {
        final String desc = getRandomName( );
        PageTemplate pageTemplate = new PageTemplate( );
        pageTemplate.setDescription( desc );
        pageTemplate.setFile( "junit" );
        pageTemplate.setPicture( "junit" );
        PageTemplateHome.create( pageTemplate );

        request.addParameter( MVCUtils.PARAMETER_ACTION, "removePageTemplate" );
        request.addParameter( Parameters.PAGE_TEMPLATE_ID, Integer.toString( pageTemplate.getId( ) ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, "jsp/admin/style/DoRemovePageTemplate.jsp" ) );
        try
        {
            getInstance( ).processController( request, new MockHttpServletResponse( ) );
            assertNull( PageTemplateHome.findByPrimaryKey( pageTemplate.getId( ) ) );
        }
        finally
        {
            PageTemplateHome.remove( pageTemplate.getId( ) );
        }
    }

    @Test
    public void testDoRemovePageTemplateInvalidToken( ) throws AccessDeniedException
    {
        final String desc = getRandomName( );
        PageTemplate pageTemplate = new PageTemplate( );
        pageTemplate.setDescription( desc );
        pageTemplate.setFile( "junit" );
        pageTemplate.setPicture( "junit" );
        PageTemplateHome.create( pageTemplate );

        request.addParameter( MVCUtils.PARAMETER_ACTION, "removePageTemplate" );
        request.addParameter( Parameters.PAGE_TEMPLATE_ID, Integer.toString( pageTemplate.getId( ) ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, "jsp/admin/style/DoRemovePageTemplate.jsp" ) + "b" );
        try
        {
            getInstance( ).processController( request, new MockHttpServletResponse( ) );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertNotNull( PageTemplateHome.findByPrimaryKey( pageTemplate.getId( ) ) );
        }
        finally
        {
            PageTemplateHome.remove( pageTemplate.getId( ) );
        }
    }

    @Test
    public void testDoRemovePageTemplateNoToken( ) throws AccessDeniedException
    {
        final String desc = getRandomName( );
        PageTemplate pageTemplate = new PageTemplate( );
        pageTemplate.setDescription( desc );
        pageTemplate.setFile( "junit" );
        pageTemplate.setPicture( "junit" );
        PageTemplateHome.create( pageTemplate );

        request.addParameter( MVCUtils.PARAMETER_ACTION, "removePageTemplate" );
        request.addParameter( Parameters.PAGE_TEMPLATE_ID, Integer.toString( pageTemplate.getId( ) ) );
        try
        {
            getInstance( ).processController( request, new MockHttpServletResponse( ) );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertNotNull( PageTemplateHome.findByPrimaryKey( pageTemplate.getId( ) ) );
        }
        finally
        {
            PageTemplateHome.remove( pageTemplate.getId( ) );
        }
    }

    private String getRandomName( )
    {
        Random rand = new SecureRandom( );
        BigInteger bigInt = new BigInteger( 128, rand );
        return "junit" + bigInt.toString( 36 );
    }
}
