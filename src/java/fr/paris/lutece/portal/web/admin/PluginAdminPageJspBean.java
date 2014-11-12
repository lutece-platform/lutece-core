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
package fr.paris.lutece.portal.web.admin;

import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;

import javax.servlet.http.HttpServletRequest;


/**
 * Provides generic methods for plugin jspBeans
 */
public abstract class PluginAdminPageJspBean extends AdminFeaturesPageJspBean
{
    private static final long serialVersionUID = 4578248013299079234L;

    // Parameters
    private static final String PARAMETER_PLUGIN_NAME = "plugin_name";
    private transient Plugin _plugin;

    /**
     * Initialize the jspbean data
     * Allows to set the feature url and feature title associated
     * @param request the HTTP request
     * @param strRight The right
     * @throws fr.paris.lutece.portal.service.admin.AccessDeniedException Access denied exception
     */
    @Override
    public void init( HttpServletRequest request, String strRight )
        throws AccessDeniedException
    {
        super.init( request, strRight );

        // initialize the plugin
        String strPluginName = request.getParameter( PARAMETER_PLUGIN_NAME );

        if ( strPluginName != null )
        {
            if ( ( _plugin == null ) || ( !_plugin.getName(  ).equals( strPluginName ) ) )
            {
                _plugin = PluginService.getPlugin( strPluginName );
            }
        }

        // if no icon is provided for the feature, try to use the plugin's one
        if ( ( _plugin != null ) && ( ( getFeatureIcon(  ) == null ) || ( getFeatureIcon(  ).equals( "" ) ) ) )
        {
            setFeatureIcon( _plugin.getIconUrl(  ) );
        }
    }

    /**
     * Returns the Plugin
     *
     * @return The Plugin
     */
    public Plugin getPlugin(  )
    {
        return _plugin;
    }
}
