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
package fr.paris.lutece.portal.web.stylesheet;

import fr.paris.lutece.LuteceTestCase;
import fr.paris.lutece.MokeHttpServletRequest;
import fr.paris.lutece.portal.business.stylesheet.StyleSheetHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.web.constants.Parameters;


/**
 * StyleSheetJspBean Test Class
 *
 */
public class StyleSheetJspBeanTest extends LuteceTestCase
{
    /**
     * Test of getManageStyleSheet method, of class fr.paris.lutece.portal.web.stylesheet.StyleSheetJspBean.
     */
    public void testGetStyleSheetManagement(  ) throws AccessDeniedException
    {
        System.out.println( "getManageStyleSheet" );

        MokeHttpServletRequest request = new MokeHttpServletRequest(  );
        request.registerAdminUserWithRigth( new AdminUser(  ), StyleSheetJspBean.RIGHT_MANAGE_STYLESHEET );

        StyleSheetJspBean instance = new StyleSheetJspBean(  );
        instance.init( request, StyleSheetJspBean.RIGHT_MANAGE_STYLESHEET );
        instance.getManageStyleSheet( request );
    }

    /**
     * Test of getCreateStyleSheet method, of class fr.paris.lutece.portal.web.stylesheet.StyleSheetJspBean.
     */
    public void testGetCreateStyleSheet(  ) throws AccessDeniedException
    {
        System.out.println( "getCreateStyleSheet" );

        MokeHttpServletRequest request = new MokeHttpServletRequest(  );
        request.addMokeParameters( Parameters.MODE_ID, "0" );
        request.registerAdminUserWithRigth( new AdminUser(  ), StyleSheetJspBean.RIGHT_MANAGE_STYLESHEET );

        StyleSheetJspBean instance = new StyleSheetJspBean(  );
        instance.init( request, StyleSheetJspBean.RIGHT_MANAGE_STYLESHEET );
        instance.getCreateStyleSheet( request );
    }

    /**
     * Test of doCreateStyleSheet method, of class fr.paris.lutece.portal.web.stylesheet.StyleSheetJspBean.
     */
    public void testDoCreateStyleSheet(  )
    {
        System.out.println( "doCreateStyleSheet" );

        // Not implemented yet
    }

    /**
     * Test of getModifyStyleSheet method, of class fr.paris.lutece.portal.web.stylesheet.StyleSheetJspBean.
     */
    public void testGetModifyStyleSheet(  ) throws AccessDeniedException
    {
        System.out.println( "getModifyStyleSheet" );

        if ( StyleSheetHome.getStyleSheetList( 0 ).size(  ) > 0 )
        {
            int nStyleSheetId = StyleSheetHome.getStyleSheetList( 0 ).iterator(  ).next(  ).getId(  );
            MokeHttpServletRequest request = new MokeHttpServletRequest(  );
            request.addMokeParameters( Parameters.STYLESHEET_ID, "" + nStyleSheetId );
            System.out.println( "-> using stylesheet ID : " + nStyleSheetId );
            request.registerAdminUserWithRigth( new AdminUser(  ), StyleSheetJspBean.RIGHT_MANAGE_STYLESHEET );

            StyleSheetJspBean instance = new StyleSheetJspBean(  );
            instance.init( request, StyleSheetJspBean.RIGHT_MANAGE_STYLESHEET );
            instance.getModifyStyleSheet( request );
        }
    }

    /**
     * Test of doModifyStyleSheet method, of class fr.paris.lutece.portal.web.stylesheet.StyleSheetJspBean.
     */
    public void testDoModifyStyleSheet(  )
    {
        System.out.println( "doModifyStyleSheet" );

        // Not implemented yet
    }

    /**
     * Test of getConfirmRemoveStyleSheet method, of class fr.paris.lutece.portal.web.stylesheet.StyleSheetJspBean.
     */
    public void testGetConfirmRemoveStyleSheet(  ) throws AccessDeniedException
    {
        System.out.println( "getConfirmRemoveStyleSheet" );

        if ( StyleSheetHome.getStyleSheetList( 0 ).size(  ) > 0 )
        {
            int nStyleSheetId = StyleSheetHome.getStyleSheetList( 0 ).iterator(  ).next(  ).getId(  );
            MokeHttpServletRequest request = new MokeHttpServletRequest(  );
            request.addMokeParameters( Parameters.STYLESHEET_ID, "" + nStyleSheetId );
            System.out.println( "-> using stylesheet ID : " + nStyleSheetId );
            request.registerAdminUserWithRigth( new AdminUser(  ), StyleSheetJspBean.RIGHT_MANAGE_STYLESHEET );

            StyleSheetJspBean instance = new StyleSheetJspBean(  );
            instance.init( request, StyleSheetJspBean.RIGHT_MANAGE_STYLESHEET );
            instance.getRemoveStyleSheet( request );
        }
    }

    /**
     * Test of doRemoveStyleSheet method, of class fr.paris.lutece.portal.web.stylesheet.StyleSheetJspBean.
     */
    public void testDoRemoveStyleSheet(  )
    {
        System.out.println( "doRemoveStyleSheet" );

        // Not implemented yet
    }
}
