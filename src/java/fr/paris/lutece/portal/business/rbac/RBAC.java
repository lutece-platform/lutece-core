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
package fr.paris.lutece.portal.business.rbac;

import fr.paris.lutece.portal.service.i18n.Localizable;
import fr.paris.lutece.portal.service.rbac.Permission;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.rbac.ResourceType;
import fr.paris.lutece.portal.service.rbac.ResourceTypeManager;

import org.apache.commons.lang.StringUtils;

import java.util.Locale;


/**
 * This class is used for rbac control configuration.
 * A role is associated to controls (given by the permission key) on resources
 * (identified by a resource type and a resource id).
 * Wilcards can be used for resource ids and permission keys.
 */
public class RBAC implements Localizable
{
    public static final String WILDCARD_RESOURCES_ID = "*";
    public static final String WILDCARD_PERMISSIONS_KEY = "*";
    private int _nRBACId;
    private String _strRoleKey;
    private String _strResourceTypeKey;
    private String _strResourceId;
    private String _strPermissionKey;
    private Locale _locale;

    /**
     * Implements Localizable
     * @param locale The current locale
     */
    public void setLocale( Locale locale )
    {
        _locale = locale;
    }

    /**
     * Returns the RBAC ID
     * @return The RBAC ID
     */
    public int getRBACId(  )
    {
        return _nRBACId;
    }

    /**
     * Sets the RBAC ID
     * @param nRBACId The RBAC Id to set
     */
    public void setRBACId( int nRBACId )
    {
        _nRBACId = nRBACId;
    }

    /**
     * Returns the Permission Key
     * @return The Permission Key
     */
    public String getPermissionKey(  )
    {
        return _strPermissionKey;
    }

    /**
     * Sets the Permission Key
     * @param strPermissionKey The Permission Key to set
     */
    public void setPermissionKey( String strPermissionKey )
    {
        _strPermissionKey = strPermissionKey;
    }

    /**
     * Returns the Resource Id
     * @return The Resource Id
     */
    public String getResourceId(  )
    {
        return _strResourceId;
    }

    /**
     * Sets the Resource Id
     * @param strResourceId The Resource Id to set
     */
    public void setResourceId( String strResourceId )
    {
        _strResourceId = strResourceId;
    }

    /**
     * Returns the Resource Type Key
     * @return The Resource Type Key.
     */
    public String getResourceTypeKey(  )
    {
        return _strResourceTypeKey;
    }

    /**
     * Sets the Resource Type Key
     * @param strResourceTypeKey The Resource Type Key to set.
     */
    public void setResourceTypeKey( String strResourceTypeKey )
    {
        _strResourceTypeKey = strResourceTypeKey;
    }

    /**
     * Returns the Role Key.
     * @return The Role Key.
     */
    public String getRoleKey(  )
    {
        return _strRoleKey;
    }

    /**
     * Sets the Role Key
     * @param strRoleKey The Role Key to set.
     */
    public void setRoleKey( String strRoleKey )
    {
        _strRoleKey = strRoleKey;
    }

    /**
     * Retrieve the label of the resource type from the resource type key
     * @return the label of the resource type
     */
    public String getResourceTypeLabel(  )
    {
        ResourceType resourceType = ResourceTypeManager.getResourceType( getResourceTypeKey(  ) );

        if ( resourceType != null )
        {
            return resourceType.getResourceTypeLabel(  );
        }

        return StringUtils.EMPTY;
    }

    /**
     * Retrieve the label of the resource from the resource id
     * @return the label of the resource
     */
    public String getResourceIdLabel(  )
    {
        if ( getResourceId(  ).equals( WILDCARD_RESOURCES_ID ) )
        {
            return WILDCARD_RESOURCES_ID;
        }
        else
        {
            ResourceType resourceType = ResourceTypeManager.getResourceType( getResourceTypeKey(  ) );

            if ( resourceType != null )
            {
                ResourceIdService resourceManagerService = resourceType.getResourceIdService(  );
                String strTitle = StringUtils.EMPTY ;
                
                if ( resourceManagerService!= null )
                {
                	strTitle = resourceManagerService.getTitle( getResourceId(  ), _locale );
                }
                if ( strTitle != null && StringUtils.isNotEmpty( strTitle ) )
                {
                    return strTitle;
                }
                else
                {
                    return getResourceId(  );
                }
            }

            return StringUtils.EMPTY;
        }
    }

    /**
     * Retrieve the label of the permission from the permission key
     * @return the label of the permission
     */
    public String getPermissionLabel(  )
    {
        if ( getPermissionKey(  ).equals( WILDCARD_PERMISSIONS_KEY ) )
        {
            return WILDCARD_PERMISSIONS_KEY;
        }
        else
        {
            ResourceType resourceType = ResourceTypeManager.getResourceType( getResourceTypeKey(  ) );
            if ( resourceType != null )
            {
            	Permission permission = resourceType.getPermission( getPermissionKey(  ) );
            	if ( permission != null )
            	{
            		permission.setLocale( _locale );
            		return  permission.getPermissionTitle(  );
            	}
            }
        }
        return StringUtils.EMPTY ;
    }
}