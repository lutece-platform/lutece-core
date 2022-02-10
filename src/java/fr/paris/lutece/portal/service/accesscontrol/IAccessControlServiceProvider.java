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

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.portal.business.accesscontrol.AccessControlSessionData;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.util.ReferenceList;

/**
 * Provide for access control
 */
public interface IAccessControlServiceProvider
{

    /**
     * return a referencelist which contains a list enabled AccessControl.
     *
     * @param user
     *            the User
     * @param locale
     *            the locale
     * @return a referencelist which contains a list enabled AccessControl
     */
    ReferenceList getAccessControlsEnabled( User user, Locale locale );

    /**
     * Find the access control used by a resource.
     * 
     * @param idResource
     * @param resourceType
     * @return the id of the access control, -1 if none
     */
    int findAccessControlForResource( int idResource, String resourceType );

    /**
     * Create or update an AccessControlResource mapping
     * 
     * @param idResource
     * @param resourceType
     * @param idAccessControl
     */
    void createOrUpdateAccessControlResource( int idResource, String resourceType, int idAccessControl );

    /**
     * Redirect to the accessControl XPage
     * 
     * @param idAccessControl
     * @return
     */
    XPage redirectToAccessControlXPage( HttpServletRequest request, int idResource, String resourceType, int idAccessControl );

    /**
     * Get the {@link AccessControlSessionData} associated to the resource.
     * 
     * @param request
     * @param idResource
     * @param resourceType
     * @return
     */
    AccessControlSessionData getSessionDataForResource( HttpServletRequest request, int idResource, String resourceType );

    /**
     * Delete the {@link AccessControlSessionData} associated to the resource.
     * 
     * @param request
     * @param idResource
     * @param resourceType
     */
    void deleteSessionDataForResource( HttpServletRequest request, int idResource, String resourceType );
    
    /**
     * Apply the session data to the destination object
     * @param sessionData
     * @param destination
     */
    void applyPersistentData( AccessControlSessionData sessionData, Object destination );
}
