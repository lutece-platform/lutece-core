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
package fr.paris.lutece.portal.service.accesscontrol;

import java.util.Locale;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.portal.business.accesscontrol.AccessControlSessionData;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.util.ReferenceList;

/**
 * AccessControlService
 */
@ApplicationScoped
public class AccessControlService
{
    private IAccessControlServiceProvider _provider;

    
    public AccessControlService() {
    }
    
    @Inject
    public AccessControlService(
            @Named("accesscontrol.accessControlServiceProvider")
            Instance<IAccessControlServiceProvider> providerInstance) {    	
        this._provider = providerInstance.stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * Check if the access control service is available. To be available, the following conditions must be verified :
     * <ul>
     * <li>the Bean service is not null</li>
     * <li>the plugin-accesscontrol must be enable</li>
     * </ul>
     * 
     * @return true if the workflow service is available
     */
    public boolean isAvailable( )
    {
        return   _provider != null  && PluginService.isPluginEnable( "accesscontrol" );
    }

    /**
     * Returns the unique instance of the {@link AccessControlService} service.
     * 
     * <p>This method is deprecated and is provided for backward compatibility only. 
     * For new code, use dependency injection with {@code @Inject} to obtain the 
     * {@link AccessControlService} instance instead.</p>
     * 
     * @return The unique instance of {@link AccessControlService}.
     * 
     * @deprecated Use {@code @Inject} to obtain the {@link AccessControlService} 
     * instance. This method will be removed in future versions.
     */
    @Deprecated( since = "8.0", forRemoval = true )
    public static AccessControlService getInstance( )
    {
        return CDI.current( ).select( AccessControlService.class ).get( );
    }

    /**
     * return a reference list which contains a list enabled AccessControl
     * 
     * @param user
     *            the User
     * @param locale
     *            the locale
     * @return a reference list which contains a list enabled AccessControl
     */
    public ReferenceList getAccessControlsEnabled( User user, Locale locale )
    {
        return isAvailable( ) ? _provider.getAccessControlsEnabled( user, locale ) : null;
    }

    /**
     * Find the access control used by a resource.
     * 
     * @param idResource
     * @param resourceType
     * @return the id of the access control, -1 if none
     */
    public int findAccessControlForResource( int idResource, String resourceType )
    {
        return isAvailable( ) ? _provider.findAccessControlForResource( idResource, resourceType ) : -1;
    }

    /**
     * Links the given resource to the given access control. <br />
     * if idAccessControl = -1, deletes the link between the resource and any access control.
     * 
     * @param idResource
     * @param resourceType
     * @param idAccessControl
     */
    public void linkResourceToAccessControl( int idResource, String resourceType, int idAccessControl )
    {
        if ( isAvailable( ) )
        {
            _provider.createOrUpdateAccessControlResource( idResource, resourceType, idAccessControl );
        }
    }

    /**
     * Redirects to the Access Control exists if the resource has an AccesControl tha has not already been validated
     * 
     * @param request
     * @param idResource
     * @param resourceType
     * @return
     */
    public XPage doExecuteAccessControl( HttpServletRequest request, int idResource, String resourceType, Object destination )
    {
        if ( isAvailable( ) )
        {
            int idAccessControl = findAccessControlForResource( idResource, resourceType );
            if ( idAccessControl != -1 )
            {
                AccessControlSessionData sessionData = _provider.getSessionDataForResource( request, idResource, resourceType );
                if ( sessionData == null || !sessionData.isAccessControlResult( ) )
                {
                    return _provider.redirectToAccessControlXPage( request, idResource, resourceType, idAccessControl );
                }
                if ( sessionData.isAccessControlResult( ) )
                {
                    _provider.applyPersistentData( sessionData, destination ); 
                }
            }
        }
        return null;
    }

    /**
     * Remove the Session Data for the give Data
     * 
     * @param request
     * @param idResource
     * @param resourceType
     */
    public void cleanSessionData( HttpServletRequest request, int idResource, String resourceType )
    {
        if ( isAvailable( ) )
        {
            _provider.deleteSessionDataForResource( request, idResource, resourceType );
        }
    }
}
