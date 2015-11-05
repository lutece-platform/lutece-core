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
package fr.paris.lutece.portal.service.rbac;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.i18n.Localizable;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * This class provides the methods for resource types.
 * It gives the key and label of the resource type
 * It allows to define and access to the permissions available.
 * It links with the resourceIdService class that retrieves specific data to be managed.
 */
public class ResourceType implements Localizable
{
    private String _strResourceTypeKey;
    private String _strResourceTypeLabel;
    private Map<String, Permission> _mapPermissions;
    private String _strResourceIdServiceClass;
    private String _strPluginName;
    private Locale _locale;

    /**
     *
     */
    public ResourceType(  )
    {
        _mapPermissions = new HashMap<String, Permission>(  );
    }

    /**
     * Implements Localizable
     * @param locale The current locale
     */
    public void setLocale( Locale locale )
    {
        _locale = locale;
    }

    /**
     * Registers a permission
     *
     * @param permission the permission object to register
     */
    public void registerPermission( Permission permission )
    {
        _mapPermissions.put( permission.getPermissionKey(  ), permission );
    }

    /**
     * Returns all registered permissions
     *
     * @return A collection containing all registered permissions
     */
    public Collection<Permission> getPermissionList(  )
    {
        return _mapPermissions.values(  );
    }

    /**
     * Get a particular permission
     *
     * @param strPermissionId Identifier of the seeked permission
     * @return the selected resource type
     */
    public Permission getPermission( String strPermissionId )
    {
        return _mapPermissions.get( strPermissionId );
    }

    /**
     * Returns the resource type Key
     * @return Returns the _strResourceTypeKey.
     */
    public String getResourceTypeKey(  )
    {
        return _strResourceTypeKey;
    }

    /**
     *  Sets the resource type Key
     * @param strResourceTypeCode The _strResourceTypeKey to set.
     */
    public void setResourceTypeKey( String strResourceTypeCode )
    {
        _strResourceTypeKey = strResourceTypeCode;
    }

    /**
     *  Returns the resource type label
     * @return Returns the _strResourceTypeLabel.
     */
    public String getResourceTypeLabel(  )
    {
        return I18nService.getLocalizedString( _strResourceTypeLabel, _locale );
    }

    /**
     *  Sets the resource type label
     * @param strResourceTypeLabel The _strResourceTypeLabel to set.
     */
    public void setResourceTypeLabelKey( String strResourceTypeLabel )
    {
        _strResourceTypeLabel = strResourceTypeLabel;
    }

    /**
     *  Returns the name of the resourceIdService class
     * @return Returns the _strResourceIdServiceClass.
     */
    public String getResourceIdServiceClass(  )
    {
        return _strResourceIdServiceClass;
    }

    /**
     * Sets the name of the resourceIdService class
     * @param strResourceIdServiceClass The _strResourceIdServiceClass to set.
     */
    public void setResourceIdServiceClass( String strResourceIdServiceClass )
    {
        _strResourceIdServiceClass = strResourceIdServiceClass;
    }

    /**
     * Returns an instance of the resourceIdService class
     * @return a ResourceIdService object with the plugin name initialised
     */
    public ResourceIdService getResourceIdService(  )
    {
        try
        {
            ResourceIdService service = (ResourceIdService) Class.forName( getResourceIdServiceClass(  ) ).newInstance(  );
            if ( service != null )
            {
            	service.setPluginName( getPluginName(  ) );
            	return service;
            }
        }
        catch ( InstantiationException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
        catch ( IllegalAccessException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
        catch ( ClassNotFoundException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }

        return null;
    }

    /**
     * Sets the plugin name
     * @param strPluginName The __strPluginName to set.
     */
    public void setPluginName( String strPluginName )
    {
        _strPluginName = strPluginName;
    }

    /**
     * Returns the plugin name
     * @return Returns the __strPluginName.
     */
    public String getPluginName(  )
    {
        return _strPluginName;
    }
}