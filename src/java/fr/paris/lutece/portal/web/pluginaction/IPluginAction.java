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
package fr.paris.lutece.portal.web.pluginaction;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * A plugin Action. <br>
 * The action should rely on a service instead of implementing the business rules itself if possible.
 * <ul>
 * <li>{@link #fillModel(HttpServletRequest, AdminUser, Map)} is called by the JspBean to add additionnal data in the main model
 * <li>{@link #getButtonTemplate()} is used to display buttons.
 * <li>{@link #isInvoked(HttpServletRequest)} is used by a JspBean to detect the invoked action.
 * <li>{@link #process(HttpServletRequest, HttpServletResponse, AdminUser, IPluginActionFields)} is called by the JspBean to process the invoked action.
 * The {@link IPluginActionResult} may contain html content or a redirect url or a noop as a result of the action processing.
 * <li>{@link #getName()} is mainly for debbuging purpose
 * </ul>
 * @param <FieldsDTO> the dto used by the process method, might be session variables or any useful data for the action processing.
 */
public interface IPluginAction<FieldsDTO>
{
    /**
     * Returns <code>true</code> if the action is invoked, <code>false</code> otherwise. <br>
     * Uses one or several button names to detect if the action is called or note.
     * @param request the request
     * @return <code>true</code> if the action is invoked, <code>false</code> otherwise.
     */
    boolean isInvoked( HttpServletRequest request );

    /**
     * Processes the request
     * @param request the request
     * @param response the response
     * @param adminUser the user
     * @param sessionFields the session fields
     * @return the action result
     * @throws AccessDeniedException if the user can't access the feature
     */
    IPluginActionResult process( HttpServletRequest request, HttpServletResponse response, AdminUser adminUser,
        FieldsDTO sessionFields ) throws AccessDeniedException;

    /**
     * Fills the model to provide necessary data to fill the button template
     * @param request the request
     * @param adminUser the admin user to filter features
     * @param model the model
     */
    void fillModel( HttpServletRequest request, AdminUser adminUser, Map<String, Object> model );

    /**
     * Gets the template to display the action (typically a button - but also check box, select...)
     * Return an empty string if no display is needed (for default directory actions : create, search...)
     * @return the template
     */
    String getButtonTemplate(  );

    /**
     * The action name
     * @return the name
     */
    String getName(  );
}
