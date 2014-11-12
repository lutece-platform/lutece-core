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
package fr.paris.lutece.portal.service.insert;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.i18n.Localizable;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.rbac.RBACResource;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.web.insert.InsertServiceSelectionBean;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 * The insert Service
 */
public class InsertService implements RBACResource, Localizable
{
    public static final String RESOURCE_TYPE = "INSERT_SERVICE";

    /** Unique ID of the Service */
    private String _strId;

    /** Name used in the UI  */
    private String _strNameKey;

    /** Label used in the UI  */
    private String _strLabelKey;

    /** Name of the Bean to use to select a link */
    private String _strActionBean;

    /** Name of the plugin */
    private String _strPluginName;
    private Locale _locale;

    /**
     * Returns the insert service Id
     *
     * @return The Id as a String
     */
    public String getId(  )
    {
        return _strId;
    }

    /**
     * Set the Id of the insert service
     *
     * @param strId the id
     */
    public void setId( String strId )
    {
        _strId = strId;
    }

    /**
     * Returns the insert service name
     *
     * @return The name key as a String
     */
    public String getNameKey(  )
    {
        return _strNameKey;
    }

    /**
     * Returns the insert service name
     *
     * @return The name as a String
     */
    public String getName(  )
    {
        return I18nService.getLocalizedString( _strNameKey, _locale );
    }

    /**
     * Set the name of the insert service
     *
     * @param strNameKey the label
     */
    public void setNameKey( String strNameKey )
    {
        _strNameKey = strNameKey;
    }

    /**
     * Returns the insert service label
     *
     * @return The label key as a String
     */
    public String getLabelKey(  )
    {
        return _strLabelKey;
    }

    /**
     * Returns the insert service label
     *
     * @return The label as a String
     */
    public String getLabel(  )
    {
        return I18nService.getLocalizedString( _strLabelKey, _locale );
    }

    /**
     * Set the name of the insert service
     *
     * @param strLabelKey the label
     */
    public void setLabelKey( String strLabelKey )
    {
        _strLabelKey = strLabelKey;
    }

    /**
     * Returns the ActionBean
     *
     * @return The ActionBean as a String
     */
    public String getActionBeanString(  )
    {
        return _strActionBean;
    }

    /**
     * Set the ActionBean of the insert service
     *
     * @param strActionBean the ActionBean
     */
    public void setActionBeanString( String strActionBean )
    {
        _strActionBean = strActionBean;
    }

    /**
     * Register the name of the Action Bean associated with the service
     *
     * @param strSelectionBean The ActionBean to use
     */
    public void setActionBeanClassName( String strSelectionBean )
    {
        _strActionBean = strSelectionBean;
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
     * Returns the name of the Bean to select an object
     *
     * @return an Instance of the Bean
     * @see fr.paris.lutece.portal.service.LinkService#getActionBean()
     */
    public InsertServiceSelectionBean getSelectionActionBean(  )
    {
        try
        {
            return (InsertServiceSelectionBean) Class.forName( getActionBeanString(  ) ).newInstance(  );
        }
        catch ( InstantiationException e )
        {
            throw new AppException( "Error instantiating a LinkService Selection Bean : " + e.getMessage(  ), e );
        }
        catch ( IllegalAccessException e )
        {
            throw new AppException( "Error instantiating a LinkService Selection Bean : " + e.getMessage(  ), e );
        }
        catch ( ClassNotFoundException e )
        {
            throw new AppException( "Error instantiating a LinkService Selection Bean : " + e.getMessage(  ), e );
        }
    }

    /**
     * Get the UI to select an object
     *
     * @param request The Http request.
     * @return HTML code of the page as string
     */
    public String getSelectorUI( HttpServletRequest request )
    {
        return getSelectionActionBean(  ).getInsertServiceSelectorUI( request );
    }

    /**
     * Tells if the insertservice is enable (plugin activated).
     * @return Returns the state of the insert service : enable or disable
     */
    public boolean isEnabled(  )
    {
        Plugin plugin = PluginService.getPlugin( _strPluginName );

        return plugin.isInstalled(  );
    }

    ////////////////////////////////////////////////////////////////////////////
    // RBAC Resource implementation

    /**
     * Returns the Resource Type Code that identify the resource type
     * @return The Resource Type Code
     */
    public String getResourceTypeCode(  )
    {
        return RESOURCE_TYPE;
    }

    /**
     * Returns the resource Id of the current object
     * @return The resource Id of the current object
     */
    public String getResourceId(  )
    {
        return "" + getId(  );
    }

    /**
     * Implements Localizable
     * @param locale The current locale
     */
    public void setLocale( Locale locale )
    {
        _locale = locale;
    }
}
