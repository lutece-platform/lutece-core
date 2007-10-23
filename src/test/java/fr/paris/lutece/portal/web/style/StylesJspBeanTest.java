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

import fr.paris.lutece.LuteceTestCase;
import fr.paris.lutece.MokeHttpServletRequest;
import fr.paris.lutece.portal.business.style.StyleHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.web.constants.Parameters;


/**
 * StylesJspBeanTest Test Class
 *
 */
public class StylesJspBeanTest extends LuteceTestCase
{
    /**
     * Test of getStylesManagement method, of class fr.paris.lutece.portal.web.style.StylesJspBean.
     */
    public void testGetStylesManagement(  ) throws AccessDeniedException
    {
        System.out.println( "getStylesManagement" );
        
        MokeHttpServletRequest request = new MokeHttpServletRequest(  );
        request.registerAdminUserWithRigth( new AdminUser(  ), StylesJspBean.RIGHT_MANAGE_STYLE );
        
        StylesJspBean instance = new StylesJspBean(  );
        instance.init( request, StylesJspBean.RIGHT_MANAGE_STYLE );
        instance.getStylesManagement( request );
    }
    
    /**
     * Test of getCreateStyle method, of class fr.paris.lutece.portal.web.style.StylesJspBean.
     */
    public void testGetCreateStyle(  ) throws AccessDeniedException
    {
        System.out.println( "getCreateStyle" );
        
        MokeHttpServletRequest request = new MokeHttpServletRequest(  );
        request.registerAdminUserWithRigth( new AdminUser(  ), StylesJspBean.RIGHT_MANAGE_STYLE );
        
        StylesJspBean instance = new StylesJspBean(  );
        instance.init( request, StylesJspBean.RIGHT_MANAGE_STYLE );
        instance.getCreateStyle( request );
    }
    
    /**
     * Test of doCreateStyle method, of fr.paris.lutece.portal.web.style.StylesJspBean.
     */
    public void testDoCreateStyle(  )
    {
        System.out.println( "doCreateStyle" );
        
        // Not implemented yet
    }
    
    /**
     * Test of getModifyStyle method, of class fr.paris.lutece.portal.web.style.StylesJspBean.
     */
    public void testGetModifyStyle(  ) throws AccessDeniedException
    {
        System.out.println( "getModifyStyle" );
        
        if( StyleHome.getStylesList().size() > 0 )
        {
            int nStyleId = StyleHome.getStylesList().iterator().next().getId();
            MokeHttpServletRequest request = new MokeHttpServletRequest(  );
            request.addMokeParameters( Parameters.STYLE_ID, "" + nStyleId );
            request.registerAdminUserWithRigth( new AdminUser(  ), StylesJspBean.RIGHT_MANAGE_STYLE );
            
            StylesJspBean instance = new StylesJspBean(  );
            instance.init( request, StylesJspBean.RIGHT_MANAGE_STYLE );
            instance.getModifyStyle( request );
        }
    }
    
    /**
     * Test of doModifyStyle method, of fr.paris.lutece.portal.web.style.StylesJspBean.
     */
    public void testDoModifyStyle(  )
    {
        System.out.println( "doModifyStyle" );
        
        // Not implemented yet
    }
    
    /**
     * Test of getConfirmRemoveStyle method, of class fr.paris.lutece.portal.web.style.StylesJspBean.
     */
    public void testGetConfirmRemoveStyle(  ) throws AccessDeniedException
    {
        System.out.println( "getConfirmRemoveStyle" );
        
        if( StyleHome.getStylesList().size() > 0 )
        {
            int nStyleId = StyleHome.getStylesList().iterator().next().getId();
            MokeHttpServletRequest request = new MokeHttpServletRequest(  );
            request.addMokeParameters( Parameters.STYLE_ID, "" + nStyleId );
            request.registerAdminUserWithRigth( new AdminUser(  ), StylesJspBean.RIGHT_MANAGE_STYLE );
            
            StylesJspBean instance = new StylesJspBean(  );
            instance.init( request, StylesJspBean.RIGHT_MANAGE_STYLE );
            instance.getConfirmRemoveStyle( request );
        }
    }
    
    /**
     * Test of doRemoveStyle method, of fr.paris.lutece.portal.web.style.StylesJspBean.
     */
    public void testDoRemoveStyle(  )
    {
        System.out.println( "doRemoveStyle" );
        
        // Not implemented yet
    }
}
