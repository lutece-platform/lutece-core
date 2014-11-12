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
package fr.paris.lutece.portal.business.portlet;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.i18n.Localizable;
import fr.paris.lutece.portal.service.rbac.RBACResource;

import java.util.Locale;


/**
 * This class represents business objects PortletType
 */
public class PortletType implements RBACResource, Localizable
{
    /////////////////////////////////////////////////////////////////////////////////
    // Constants
    public static final String RESOURCE_TYPE = "PORTLET_TYPE";
    private static final String EMPTY_STRING = "";
    private String _strId;
    private String _strNameKey;
    private String _strUrlCreation;
    private String _strUrlUpdate;
    private String _strHomeClass;
    private String _strPluginName;
    private String _strDoCreateUrl;
    private String _strCreateScriptTemplate;
    private String _strCreateSpecificTemplate;
    private String _strCreateSpecificFormTemplate;
    private String _strDoModifyUrl;
    private String _strModifyScriptTemplate;
    private String _strModifySpecificTemplate;
    private String _strModifySpecificFormTemplate;
    private Locale _locale;

    /**
     * Returns the identifier of the Portlet type
     *
     * @return the Portlet Type identifier
     */
    public String getId(  )
    {
        return _strId;
    }

    /**
     * Sets the identifier of the portlet type
     *
     * @param strId the Portlet Type identifier
     */
    public void setId( String strId )
    {
        _strId = ( strId == null ) ? EMPTY_STRING : strId;
    }

    /**
     * Sets the name key of the Portlet Type
     *
     * @param strNameKey the Portlet type Name key
     */
    public void setNameKey( String strNameKey )
    {
        _strNameKey = strNameKey;
    }

    /**
     * Returns the name key of the Portlet Type
     *
     * @return the Portlet type name key
     */
    public String getNameKey(  )
    {
        return _strNameKey;
    }

    /**
     * Returns the name of the Portlet Type
     *
     * @return the Portlet type name
     */
    public String getName(  )
    {
        return I18nService.getLocalizedString( _strNameKey, _locale );
    }

    /**
     * Sets the url of the program which manages the creation of this portlet type
     *
     * @param strUrlCreation the url to create the current portlet type
     */
    public void setUrlCreation( String strUrlCreation )
    {
        _strUrlCreation = strUrlCreation;
    }

    /**
     * Returns the url of the program which manages the creation of this portlet type
     *
     * @return the url of creation
     */
    public String getUrlCreation(  )
    {
        return _strUrlCreation;
    }

    /**
     * Sets the url of the program which manages the update of this portlet type
     *
     * @param strUrlUpdate the url to update the current portlet type
     */
    public void setUrlUpdate( String strUrlUpdate )
    {
        _strUrlUpdate = strUrlUpdate;
    }

    /**
     * Returns the url of the program which manages the update of this portlet type
     *
     * @return the url of update
     */
    public String getUrlUpdate(  )
    {
        return _strUrlUpdate;
    }

    /**
     * Sets the home class path of the program which manages the update of this portlet type
     *
     * @param strHomeClass the path to update the current portlet type
     */
    public void setHomeClass( String strHomeClass )
    {
        _strHomeClass = strHomeClass;
    }

    /**
     * Returns the path of the home class which manages the update of this portlet type
     *
     * @return the home class path of update
     */
    public String getHomeClass(  )
    {
        return _strHomeClass;
    }

    /**
     * Sets the plugin name of the Portlet Type
     *
     * @param strPluginName the Portlet type Plugin Name
     */
    public void setPluginName( String strPluginName )
    {
        _strPluginName = strPluginName;
    }

    /**
     * Returns the plugin name of the Portlet Type
     *
     * @return the Portlet type  plugin name
     */
    public String getPluginName(  )
    {
        return _strPluginName;
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
     * Return the DoCreate Url
     * @return The DoCreate Url
     */
    public String getDoCreateUrl(  )
    {
        return _strDoCreateUrl;
    }

    /**
     * Sets the DoCreate Url
     * @param strDoCreateUrl The DoCreate Url
     */
    public void setDoCreateUrl( String strDoCreateUrl )
    {
        _strDoCreateUrl = strDoCreateUrl;
    }

    /**
     * Sets the CreateScript
     * @param strCreateScript The CreateScript
     */
    public void setCreateScriptTemplate( String strCreateScript )
    {
        _strCreateScriptTemplate = strCreateScript;
    }

    /**
     * Sets the CreateSpecific
     * @param strCreateSpecific The CreateSpecific
     */
    public void setCreateSpecificTemplate( String strCreateSpecific )
    {
        _strCreateSpecificTemplate = strCreateSpecific;
    }

    /**
     * Sets the CreateSpecificForm
     * @param strCreateSpecificForm The CreateSpecificForm
     */
    public void setCreateSpecificFormTemplate( String strCreateSpecificForm )
    {
        _strCreateSpecificFormTemplate = strCreateSpecificForm;
    }

    /**
     * Return a script to include in the create form
     * @return the script to include in the create form
     */
    public String getCreateScriptTemplate(  )
    {
        return _strCreateScriptTemplate;
    }

    /**
     * Return html code to include in the create form
     * @return html code to include in the create form
     */
    public String getCreateSpecificTemplate(  )
    {
        return _strCreateSpecificTemplate;
    }

    /**
     * Return html code to add another form to the create page
     * @return html code to add another form to the create page
     */
    public String getCreateSpecificFormTemplate(  )
    {
        return _strCreateSpecificFormTemplate;
    }

    /**
     * Return the Modify Url
     * @return The Modify Url
     */
    public String getDoModifyUrl(  )
    {
        return _strDoModifyUrl;
    }

    /**
     * Return a script to include in the modify form
     * @return the script to include in the modify form
     */
    public String getModifyScriptTemplate(  )
    {
        return _strModifyScriptTemplate;
    }

    /**
     * Return html code to include in the modify form
     * @return html code to include in the modify form
     */
    public String getModifySpecificTemplate(  )
    {
        return _strModifySpecificTemplate;
    }

    /**
     * Return html code to add another form to the modify page
     * @return html code to add another form to the modify page
     */
    public String getModifySpecificFormTemplate(  )
    {
        return _strModifySpecificFormTemplate;
    }

    /**
     * Sets the DoModify Url
     * @param strDoModifyUrl The DoModify Url
     */
    public void setDoModifyUrl( String strDoModifyUrl )
    {
        _strDoModifyUrl = strDoModifyUrl;
    }

    /**
     * Sets the ModifyScript
     * @param strModifyScript The ModifyScript
     */
    public void setModifyScriptTemplate( String strModifyScript )
    {
        _strModifyScriptTemplate = strModifyScript;
    }

    /**
     * Sets the ModifySpecific
     * @param strModifySpecific The ModifySpecific
     */
    public void setModifySpecificTemplate( String strModifySpecific )
    {
        _strModifySpecificTemplate = strModifySpecific;
    }

    /**
     * Sets the ModifySpecificForm
     * @param strModifySpecificForm The ModifySpecificForm
     */
    public void setModifySpecificFormTemplate( String strModifySpecificForm )
    {
        _strModifySpecificFormTemplate = strModifySpecificForm;
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
