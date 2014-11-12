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
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * PluginActionManager.
 * <ul>
 * <li>
 * Use {@link #getPluginAction(HttpServletRequest, Class)} to find the invoked action
 * <li>
 * Use {@link #fillModel(HttpServletRequest, AdminUser, Map, Class, String)} to fill you model
 * </ul>
 * @see #getListPluginAction(Class)
 * @see #getPluginAction(HttpServletRequest, Class)
 * @see #fillModel(HttpServletRequest, AdminUser, Map, Class, String)
 *
 */
public final class PluginActionManager
{
    /**
     * Empty constructor
     */
    private PluginActionManager(  )
    {
        // nothing
    }

    /**
     * Gets the list of {@link IDirectoryAction}.
     *
     * @param <A> the generic type
     * @param pluginActionClass the plugin action class
     * @return the list
     */
    public static <A extends IPluginAction<?>> List<A> getListPluginAction( Class<A> pluginActionClass )
    {
        return SpringContextService.getBeansOfType( pluginActionClass );
    }

    /**
     * Gets the {@link IPluginAction} for the request.
     *
     * @param <A> the generic type
     * @param request the request
     * @param pluginActionClass the plugin action class
     * @return the invoked {@link IPluginAction}, <code>null</code> otherwise.
     * @see IPluginAction#isInvoked(HttpServletRequest)
     */
    public static <A extends IPluginAction<?>> A getPluginAction( HttpServletRequest request, Class<A> pluginActionClass )
    {
        for ( A action : getListPluginAction( pluginActionClass ) )
        {
            if ( action.isInvoked( request ) )
            {
                return action;
            }
        }

        return null;
    }

    /**
     * Fills the model with all actions and adds the list to the given marker
     * @param request the request
     * @param adminUser the admin user
     * @param model the model
     * @param pluginActionClass the action class (usually the interface)
     * @param strMark the marker to put for the actions list
     * @param <A> the action type (usually the interface)
     */
    public static <A extends IPluginAction<?>> void fillModel( HttpServletRequest request, AdminUser adminUser,
        Map<String, Object> model, Class<A> pluginActionClass, String strMark )
    {
        for ( A action : SpringContextService.getBeansOfType( pluginActionClass ) )
        {
            action.fillModel( request, adminUser, model );
        }

        // add the action list
        model.put( strMark, getListPluginAction( pluginActionClass ) );
    }
}
