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
package fr.paris.lutece.portal.web.xpages;

import fr.paris.lutece.portal.service.content.XPageAppService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


/**
 * XPageApplication Entry
 */
public class XPageApplicationEntry
{
    // Variables declarations
    private String _strId;
    private String _strClassName;
    private String _strPluginName;
    private List<String> _listRoles = new ArrayList<String>(  );
    private boolean _bEnabled = true; // defaults to enabled

    /**
     * Returns the Id
     *
     * @return The Id
     */
    public String getId(  )
    {
        return _strId;
    }

    /**
     * Sets the Id
     *
     * @param strId The Id
     */
    public void setId( String strId )
    {
        _strId = strId;
    }

    /**
     * Returns the ClassName
     *
     * @return The ClassName
     */
    public String getClassName(  )
    {
        return _strClassName;
    }

    /**
     * Sets the ClassName
     *
     * @param strClassName The ClassName
     */
    public void setClassName( String strClassName )
    {
        _strClassName = strClassName;
    }

    /**
     * Returns the Roles
     *
     * @return The Roles
     */
    public List<String> getRoles(  )
    {
        return _listRoles;
    }

    /**
     * Sets the Roles
     *
     * @param strRoles The Roles
     */
    public void setRoles( String strRoles )
    {
        // extracts each role (separated by a comma) from the String
        if ( strRoles != null )
        {
            StringTokenizer strTokens = new StringTokenizer( strRoles, "," );

            while ( strTokens.hasMoreTokens(  ) )
            {
                _listRoles.add( strTokens.nextToken(  ) );
            }
        }
    }

    /**
     * Return the name of the plugin
     *
     * @return The name of the plugin
     */
    public String getPluginName(  )
    {
        return _strPluginName;
    }

    /**
     * Set the plugin name of the insert service
     *
     * @param strPluginName the plugin name
     */
    public void setPluginName( String strPluginName )
    {
        _strPluginName = strPluginName;
    }

    /**
     * Returns the Application
     *
     * @deprecated use {@link XPageAppService#getApplicationInstance(XPageApplicationEntry)} instead
     * @return The Application
     */
    public XPageApplication getApplication(  )
    {
        return XPageAppService.getApplicationInstance( this );
    }

    /**
     * Sets the Application
     *
     * @deprecated should not be used anymore
     * @param application The Application
     */
    public void setApplication( XPageApplication application )
    {
    }

    /**
     * Tells if the application is enable (plugin enabled)
     *
     * @return True if the application is enable, otherwise false
     */
    public boolean isEnable(  )
    {
        return _bEnabled && PluginService.isPluginEnable( _strPluginName );
    }

    /**
     * Tells if the XPageApplication is enabled, independently of the plugin's status
     * @return <code>true</code> if this XPageApplication is enabled, <code>false</code> otherwise
     * @since 5.1
     */
    public boolean isEnabled(  )
    {
        return _bEnabled;
    }

    /**
     * Sets the enabled state of this XPageApplication
     * @param enabled <code>true</code> if this XPageApplication is enabled, <code>false</code> otherwise
     * @since 5.1
     */
    public void setEnabled( boolean bEnabled )
    {
        _bEnabled = bEnabled;
    }

    /**
     * Gets the plugin instance associated to the application
     *
     * @return the plugin
     */
    public Plugin getPlugin(  )
    {
        return PluginService.getPlugin( _strPluginName );
    }
}
