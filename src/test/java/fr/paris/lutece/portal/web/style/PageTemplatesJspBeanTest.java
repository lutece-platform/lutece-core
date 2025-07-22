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
import java.util.NoSuchElementException;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.style.IPageTemplateRepository;
import fr.paris.lutece.portal.business.style.PageTemplate;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.ISecurityTokenService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.web.admin.AdminUserUtils;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.inject.Inject;

/**
 * PageTemplatesJspBeanTest Test Class
 *
 */
public class PageTemplatesJspBeanTest extends LuteceTestCase
{
    private static final String TEST_PAGE_TEMPLATE_ID = "1"; // Page template one column
    private MockHttpServletRequest request;
    @Inject
    private PageTemplatesJspBean instance;
    @Inject
    private ISecurityTokenService _securityTokenService;
    @Inject
    private IPageTemplateRepository _repository;
    
    @BeforeEach
    protected void setUp( ) throws Exception
    {
        request = new MockHttpServletRequest( );
        AdminUserUtils.registerAdminUserWithRigth( request, new AdminUser( ), PageTemplatesJspBean.RIGHT_MANAGE_PAGE_TEMPLATES );
        instance.init( request, PageTemplatesJspBean.RIGHT_MANAGE_PAGE_TEMPLATES );
    }

    /**
     * Test of getManagePageTemplate method, of class fr.paris.lutece.portal.web.style.PageTemplatesJspBean.
     */
    @Test
    public void testGetManagePageTemplate( ) throws AccessDeniedException
    {
        assertTrue( StringUtils.isNotEmpty( instance.getManagePageTemplate( request ) ) );
    }

    /**
     * Test of getModifyPageTemplate method, of class fr.paris.lutece.portal.web.style.PageTemplatesJspBean.
     */
    @Test
    public void testGetModifyPageTemplate( ) throws AccessDeniedException
    {
        request.addParameter( Parameters.PAGE_TEMPLATE_ID, TEST_PAGE_TEMPLATE_ID );
        assertTrue( StringUtils.isNotEmpty( instance.getModifyPageTemplate( request ) ) );
    }

    /**
     * Test of doModifyPageTemplate method, of fr.paris.lutece.portal.web.style.PageTemplatesJspBean.
     * 
     * @throws AccessDeniedException
     */
    @Test
    public void testDoModifyPageTemplate( ) throws AccessDeniedException
    {
        final String desc = getRandomName( );
        PageTemplate pageTemplate = new PageTemplate( );
        pageTemplate.setDescription( desc );
        _repository.create( pageTemplate );

        Map<String, String [ ]> parameters = new HashMap<>( );
        parameters.put( Parameters.PAGE_TEMPLATE_ID, new String [ ] {
                Integer.toString( pageTemplate.getId( ) )
        } );
        parameters.put( Parameters.PAGE_TEMPLATE_DESCRIPTION, new String [ ] {
                desc + "mod"
        } );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, new String [ ] {
                _securityTokenService.getToken( request, "admin/style/modify_page_template.html" )
        } );
        MultipartHttpServletRequest multipartRequest = new MultipartHttpServletRequest( request, Collections.emptyMap( ), parameters );
        try
        {
            assertEquals( desc, _repository.load( pageTemplate.getId( ) ).get( ).getDescription( ) );
            instance.doModifyPageTemplate( multipartRequest );
            assertEquals( desc + "mod", _repository.load( pageTemplate.getId( ) ).get( ).getDescription( ) );
        }
        finally
        {
            _repository.remove( pageTemplate.getId( ) );
        }
    }

    @Test
    public void testDoModifyPageTemplateInvalidToken( ) throws AccessDeniedException
    {
        final String desc = getRandomName( );
        PageTemplate pageTemplate = new PageTemplate( );
        pageTemplate.setDescription( desc );
        _repository.create( pageTemplate );

        Map<String, String [ ]> parameters = new HashMap<>( );
        parameters.put( Parameters.PAGE_TEMPLATE_ID, new String [ ] {
                Integer.toString( pageTemplate.getId( ) )
        } );
        parameters.put( Parameters.PAGE_TEMPLATE_DESCRIPTION, new String [ ] {
                desc + "mod"
        } );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, new String [ ] {
                _securityTokenService.getToken( request, "admin/style/modify_page_template.html" ) + "b"
        } );
        MultipartHttpServletRequest multipartRequest = new MultipartHttpServletRequest( request, Collections.emptyMap( ), parameters );
        try
        {
            assertEquals( desc, _repository.load( pageTemplate.getId( ) ).get( ).getDescription( ) );
            instance.doModifyPageTemplate( multipartRequest );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( desc, _repository.load( pageTemplate.getId( ) ).get( ).getDescription( ) );
        }
        finally
        {
            _repository.remove( pageTemplate.getId( ) );
        }
    }

    @Test
    public void testDoModifyPageTemplateNoToken( ) throws AccessDeniedException
    {
        final String desc = getRandomName( );
        PageTemplate pageTemplate = new PageTemplate( );
        pageTemplate.setDescription( desc );
        _repository.create( pageTemplate );

        Map<String, String [ ]> parameters = new HashMap<>( );
        parameters.put( Parameters.PAGE_TEMPLATE_ID, new String [ ] {
                Integer.toString( pageTemplate.getId( ) )
        } );
        parameters.put( Parameters.PAGE_TEMPLATE_DESCRIPTION, new String [ ] {
                desc + "mod"
        } );
        MultipartHttpServletRequest multipartRequest = new MultipartHttpServletRequest( request, Collections.emptyMap( ), parameters );
        try
        {
            assertEquals( desc, _repository.load( pageTemplate.getId( ) ).get( ).getDescription( ) );
            instance.doModifyPageTemplate( multipartRequest );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( desc, _repository.load( pageTemplate.getId( ) ).get( ).getDescription( ) );
        }
        finally
        {
            _repository.remove( pageTemplate.getId( ) );
        }
    }

    @Test
    public void testGetConfirmRemovePageTemplate( )
    {
        request.addParameter( Parameters.PAGE_TEMPLATE_ID, TEST_PAGE_TEMPLATE_ID );
        instance.getConfirmRemovePageTemplate( request );
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
        _repository.create( pageTemplate );

        request.addParameter( Parameters.PAGE_TEMPLATE_ID, Integer.toString( pageTemplate.getId( ) ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, "jsp/admin/style/DoRemovePageTemplate.jsp" ) );
        try
        {
            instance.doRemovePageTemplate( request );
            assertThrows( NoSuchElementException.class, ( ) -> _repository.load( pageTemplate.getId( ) ).get( ) );
        }
        finally
        {
            _repository.remove( pageTemplate.getId( ) );
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
        _repository.create( pageTemplate );

        request.addParameter( Parameters.PAGE_TEMPLATE_ID, Integer.toString( pageTemplate.getId( ) ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, "jsp/admin/style/DoRemovePageTemplate.jsp" ) + "b" );
        try
        {
            instance.doRemovePageTemplate( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertNotNull( _repository.load( pageTemplate.getId( ) ).get( ) );
        }
        finally
        {
            _repository.remove( pageTemplate.getId( ) );
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
        _repository.create( pageTemplate );

        request.addParameter( Parameters.PAGE_TEMPLATE_ID, Integer.toString( pageTemplate.getId( ) ) );
        try
        {
            instance.doRemovePageTemplate( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertNotNull( _repository.load( pageTemplate.getId( ) ).get( ) );
        }
        finally
        {
            _repository.remove( pageTemplate.getId( ) );
        }
    }

    private String getRandomName( )
    {
        Random rand = new SecureRandom( );
        BigInteger bigInt = new BigInteger( 128, rand );
        return "junit" + bigInt.toString( 36 );
    }
}
