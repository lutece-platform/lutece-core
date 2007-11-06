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
package fr.paris.lutece.portal.web.rbac;

import fr.paris.lutece.LuteceTestCase;
import fr.paris.lutece.MokeHttpServletRequest;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;


/**
 * StyleSheetJspBean Test Class
 *
 */
public class RoleManagementJspBeanTest extends LuteceTestCase
{
    private static final String PARAMETER_ROLE_KEY = "role_key";
    private static final String PARAMETER_RBAC_ID = "rbac_id";

    /**
     * Test of getManageRoles method, of class fr.paris.lutece.portal.web.rbac.RoleManagementJspBean.
     */
    public void testGetManageRoles(  ) throws AccessDeniedException
    {
        System.out.println( "getManageRoles" );

        MokeHttpServletRequest request = new MokeHttpServletRequest(  );
        request.registerAdminUserWithRigth( new AdminUser(  ), RoleManagementJspBean.RIGHT_MANAGE_ROLES );

        RoleManagementJspBean instance = new RoleManagementJspBean(  );
        instance.init( request, RoleManagementJspBean.RIGHT_MANAGE_ROLES );
        instance.getManageRoles( request );
    }

    /**
     * Test of getCreateRole method, of class fr.paris.lutece.portal.web.rbac.RoleManagementJspBean.
     */
    public void testGetCreateRole(  ) throws AccessDeniedException
    {
        System.out.println( "getCreateRole" );

        MokeHttpServletRequest request = new MokeHttpServletRequest(  );

        request.registerAdminUserWithRigth( new AdminUser(  ), RoleManagementJspBean.RIGHT_MANAGE_ROLES );

        RoleManagementJspBean instance = new RoleManagementJspBean(  );
        instance.init( request, RoleManagementJspBean.RIGHT_MANAGE_ROLES );
        instance.getCreateRole( request );
    }

    /**
     * Test of doCreateRole method, of class fr.paris.lutece.portal.web.rbac.RoleManagementJspBean.
     */
    public void testDoCreateRole(  )
    {
        System.out.println( "doCreateRole" );

        // Not implemented yet
    }

    /**
     * Test of getModifyRole method, of class fr.paris.lutece.portal.web.rbac.RoleManagementJspBean.
     */
    public void testGetModifyRole(  ) throws AccessDeniedException
    {
        System.out.println( "getModifyRole" );

        // Not implemented yet
    }

    /**
     * Test of doModifyRole method, of class fr.paris.lutece.portal.web.rbac.RoleManagementJspBean.
     */
    public void testDoModifyRole(  )
    {
        System.out.println( "doModifyRole" );

        // Not implemented yet
    }

    /**
     * Test of doConfirmRemoveRole method, of class fr.paris.lutece.portal.web.rbac.RoleManagementJspBean.
     */
    public void testDoConfirmRemoveRole(  ) throws AccessDeniedException
    {
        System.out.println( "doConfirmRemoveRole" );

        // Not implemented yet
    }

    /**
     * Test of doRemoveRole method, of class fr.paris.lutece.portal.web.rbac.RoleManagementJspBean.
     */
    public void testDoRemoveRole(  )
    {
        System.out.println( "doRemoveRole" );

        // Not implemented yet
    }

    /**
     * Test of getViewRoleDescription method, of class fr.paris.lutece.portal.web.rbac.RoleManagementJspBean.
     */
    public void testgetViewRoleDescription(  ) throws AccessDeniedException
    {
        System.out.println( "getViewRoleDescription" );

        // Not implemented yet
    }

    /**
     * Test of doConfirmRemoveControlFromRole method, of class fr.paris.lutece.portal.web.rbac.RoleManagementJspBean.
     */
    public void testDoConfirmRemoveControlFromRole(  )
        throws AccessDeniedException
    {
        System.out.println( "doConfirmRemoveControlFromRole" );

        // Not implemented yet
    }

    /**
     * Test of doRemoveControlFromRole method, of class fr.paris.lutece.portal.web.rbac.RoleManagementJspBean.
     */
    public void testDoRemoveControlFromRole(  )
    {
        System.out.println( "doRemoveControlFromRole" );

        // Not implemented yet
    }
}
