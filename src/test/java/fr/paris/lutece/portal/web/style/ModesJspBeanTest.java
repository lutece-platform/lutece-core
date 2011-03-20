/*
 * Copyright (c) 2002-2009, Mairie de Paris
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

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.MokeHttpServletRequest;


/**
 * ModeJspBeanTest Test Class
 *
 */
public class ModesJspBeanTest extends LuteceTestCase
{
    private static final String TEST_MODE_ID = "0"; // normal mode 

    /**
     * Test of getManageModes method, of class fr.paris.lutece.portal.web.style.ModesJspBean.
     */
    public void testGetManageModes(  ) throws AccessDeniedException
    {
        System.out.println( "getManageModes" );

        MokeHttpServletRequest request = new MokeHttpServletRequest(  );
        request.registerAdminUserWithRigth( new AdminUser(  ), ModesJspBean.RIGHT_MANAGE_MODES );

        ModesJspBean instance = new ModesJspBean(  );
        instance.init( request, ModesJspBean.RIGHT_MANAGE_MODES );
        instance.getManageModes( request );
    }

    /**
     * Test of getCreateMode method, of class fr.paris.lutece.portal.web.style.ModesJspBean.
     */
    public void testGetCreateMode(  ) throws AccessDeniedException
    {
        System.out.println( "getCreateMode" );

        MokeHttpServletRequest request = new MokeHttpServletRequest(  );
        request.registerAdminUserWithRigth( new AdminUser(  ), ModesJspBean.RIGHT_MANAGE_MODES );

        ModesJspBean instance = new ModesJspBean(  );
        instance.init( request, ModesJspBean.RIGHT_MANAGE_MODES );
        instance.getCreateMode( request );
    }

    /**
     * Test of doCreateMode method, of fr.paris.lutece.portal.web.style.ModesJspBean.
     */
    public void testDoCreateMode(  )
    {
        System.out.println( "doCreateMode" );

        // Not implemented yet
    }

    /**
     * Test of testGetModifyMode method, of class fr.paris.lutece.portal.web.style.ModesJspBean.
     */
    public void testGetModifyMode(  ) throws AccessDeniedException
    {
        System.out.println( "testGetModifyMode" );

        MokeHttpServletRequest request = new MokeHttpServletRequest(  );
        request.addMokeParameters( Parameters.MODE_ID, TEST_MODE_ID );
        request.registerAdminUserWithRigth( new AdminUser(  ), ModesJspBean.RIGHT_MANAGE_MODES );

        ModesJspBean instance = new ModesJspBean(  );
        instance.init( request, ModesJspBean.RIGHT_MANAGE_MODES );
        instance.getModifyMode( request );
    }

    /**
     * Test of doModifyMode method, of fr.paris.lutece.portal.web.style.ModesJspBean.
     */
    public void testDoModifyMode(  )
    {
        System.out.println( "doModifyMode" );

        // Not implemented yet
    }

    /**
     * Test of testGetModeView method, of class fr.paris.lutece.portal.web.style.ModesJspBean.
     */
    public void testGetModeView(  ) throws AccessDeniedException
    {
        System.out.println( "testGetModeView" );

        MokeHttpServletRequest request = new MokeHttpServletRequest(  );
        request.addMokeParameters( Parameters.MODE_ID, TEST_MODE_ID );
        request.registerAdminUserWithRigth( new AdminUser(  ), ModesJspBean.RIGHT_MANAGE_MODES );

        ModesJspBean instance = new ModesJspBean(  );
        instance.init( request, ModesJspBean.RIGHT_MANAGE_MODES );
        instance.getModeView( request );
    }
}
