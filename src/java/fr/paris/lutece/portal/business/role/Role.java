/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.portal.business.role;

import fr.paris.lutece.portal.service.rbac.RBACResource;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupResource;


/**
 * This class represents business objects role
 */
public class Role implements RBACResource, AdminWorkgroupResource
{
    public static final String RESOURCE_TYPE = "ROLE_TYPE";
    private String _strRole;
    private String _strRoleDescription;
    private String _strWorkgroupKey;

    /**
     * Gets the role name
     * @return the role name
     */
    public String getRole(  )
    {
        return _strRole;
    }

    /**
     * Sets the role name
     * @param strRole the role name
     */
    public void setRole( String strRole )
    {
        _strRole = ( strRole != null ) ? strRole : "";
    }

    /**
     * Gets the role description
     * @return the role description
     */
    public String getRoleDescription(  )
    {
        return _strRoleDescription;
    }

    /**
     * Sets the role description
     * @param strRoleDescription the role description
     */
    public void setRoleDescription( String strRoleDescription )
    {
        _strRoleDescription = strRoleDescription;
    }

    /**
     * RBAC resource implmentation
     * @return The resource type code
     */
    public String getResourceTypeCode(  )
    {
        return RESOURCE_TYPE;
    }

    /**
     * RBAC resource implmentation
     * @return The resourceId
     */
    public String getResourceId(  )
    {
        return _strRole;
    }

    /**
     * Get workgroup key
     * @return workgroup key
     */
    public String getWorkgroup(  )
    {
        return _strWorkgroupKey;
    }

    /**
     * Set workgroup key
     * @param strWorkgroupKey workgroup key
     */
    public void setWorkgroup( String strWorkgroupKey )
    {
        _strWorkgroupKey = strWorkgroupKey;
    }
}
