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
package fr.paris.lutece.portal.web.features;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.MokeHttpServletRequest;


/**
 * LevelsJspBeanTest Test Class
 *
 */
public class LevelsJspBeanTest extends LuteceTestCase
{
    private static final String TEST_LEVEL_ID = "0"; // administrator level_right 

    /**
     * Test of getManageLevels method, of class fr.paris.lutece.portal.web.features.LevelsJspBean.
     */
    public void testGetManageLevels(  ) throws AccessDeniedException
    {
        System.out.println( "getManageLevels" );

        MokeHttpServletRequest request = new MokeHttpServletRequest(  );
        request.registerAdminUserWithRigth( new AdminUser(  ), LevelsJspBean.RIGHT_MANAGE_LEVELS );

        LevelsJspBean instance = new LevelsJspBean(  );
        instance.init( request, LevelsJspBean.RIGHT_MANAGE_LEVELS );
        instance.getManageLevels( request );
    }

    /**
     * Test of getCreateLevel method, of class fr.paris.lutece.portal.web.features.LevelsJspBean.
     */
    public void testGetCreateLevel(  ) throws AccessDeniedException
    {
        System.out.println( "getCreateLevel" );

        MokeHttpServletRequest request = new MokeHttpServletRequest(  );
        request.registerAdminUserWithRigth( new AdminUser(  ), LevelsJspBean.RIGHT_MANAGE_LEVELS );

        LevelsJspBean instance = new LevelsJspBean(  );
        instance.init( request, LevelsJspBean.RIGHT_MANAGE_LEVELS );
        instance.getCreateLevel( request );
    }

    /**
     * Test of doCreateLevel method, of class fr.paris.lutece.portal.web.features.LevelsJspBean.
     */
    public void testDoCreateLevel(  )
    {
        System.out.println( "doCreateLevel" );

        // Not implemented yet
    }

    /**
     * Test of getModifyLevel method, of class fr.paris.lutece.portal.web.features.LevelsJspBean.
     */
    public void testGetModifyLevel(  ) throws AccessDeniedException
    {
        System.out.println( "getModifyLevel" );

        MokeHttpServletRequest request = new MokeHttpServletRequest(  );
        request.addMokeParameters( Parameters.LEVEL_ID, TEST_LEVEL_ID );
        request.registerAdminUserWithRigth( new AdminUser(  ), LevelsJspBean.RIGHT_MANAGE_LEVELS );

        LevelsJspBean instance = new LevelsJspBean(  );
        instance.init( request, LevelsJspBean.RIGHT_MANAGE_LEVELS );
        instance.getModifyLevel( request );
    }

    /**
     * Test of doModifyLevel method, of class fr.paris.lutece.portal.web.features.LevelsJspBean.
     */
    public void testDoModifyLevel(  )
    {
        System.out.println( "doModifyLevel" );

        // Not implemented yet
    }
}
