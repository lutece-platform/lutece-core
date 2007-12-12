/*
 * Copyright (c) 2002-2006, Mairie de Paris
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

import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.MokeHttpServletRequest;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.web.constants.Parameters;


/**
 * PageTemplatesJspBeanTest Test Class
 *
 */
public class PageTemplatesJspBeanTest extends LuteceTestCase
{
    private static final String TEST_PAGE_TEMPLATE_ID = "1"; // Page template one column 

    /**
     * Test of getManagePageTemplate method, of class fr.paris.lutece.portal.web.style.PageTemplatesJspBean.
     */
    public void testGetManagePageTemplate(  ) throws AccessDeniedException
    {
        System.out.println( "getManagePageTemplate" );

        MokeHttpServletRequest request = new MokeHttpServletRequest(  );
        request.registerAdminUserWithRigth( new AdminUser(  ), PageTemplatesJspBean.RIGHT_MANAGE_PAGE_TEMPLATES );

        PageTemplatesJspBean instance = new PageTemplatesJspBean(  );
        instance.init( request, PageTemplatesJspBean.RIGHT_MANAGE_PAGE_TEMPLATES );
        instance.getManagePageTemplate( request );
    }

    /**
     * Test of getCreatePageTemplate method, of class fr.paris.lutece.portal.web.style.PageTemplatesJspBean.
     */
    public void testGetCreatePageTemplate(  ) throws AccessDeniedException
    {
        System.out.println( "getCreatePageTemplate" );

        MokeHttpServletRequest request = new MokeHttpServletRequest(  );
        request.registerAdminUserWithRigth( new AdminUser(  ), PageTemplatesJspBean.RIGHT_MANAGE_PAGE_TEMPLATES );

        PageTemplatesJspBean instance = new PageTemplatesJspBean(  );
        instance.init( request, PageTemplatesJspBean.RIGHT_MANAGE_PAGE_TEMPLATES );
        instance.getCreatePageTemplate( request );
    }

    /**
     * Test of doCreatePageTemplate method, of fr.paris.lutece.portal.web.style.PageTemplatesJspBean.
     */
    public void testDoCreatePageTemplate(  )
    {
        System.out.println( "doCreatePageTemplate" );

        // Not implemented yet
    }

    /**
     * Test of getModifyPageTemplate method, of class fr.paris.lutece.portal.web.style.PageTemplatesJspBean.
     */
    public void testGetModifyPageTemplate(  ) throws AccessDeniedException
    {
        System.out.println( "getModifyPageTemplate" );

        MokeHttpServletRequest request = new MokeHttpServletRequest(  );
        request.addMokeParameters( Parameters.PAGE_TEMPLATE_ID, TEST_PAGE_TEMPLATE_ID );
        request.registerAdminUserWithRigth( new AdminUser(  ), PageTemplatesJspBean.RIGHT_MANAGE_PAGE_TEMPLATES );

        PageTemplatesJspBean instance = new PageTemplatesJspBean(  );
        instance.init( request, PageTemplatesJspBean.RIGHT_MANAGE_PAGE_TEMPLATES );
        instance.getModifyPageTemplate( request );
    }

    /**
     * Test of doModifyPageTemplate method, of fr.paris.lutece.portal.web.style.PageTemplatesJspBean.
     */
    public void testDoModifyPageTemplate(  )
    {
        System.out.println( "doModifyPageTemplate" );

        // Not implemented yet
    }
}
