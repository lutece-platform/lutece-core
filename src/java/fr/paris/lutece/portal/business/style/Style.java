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
package fr.paris.lutece.portal.business.style;


/**
 * This class represents style business objects
 */
public class Style
{
    private static final String EMPTY_STRING = "";
    private int _nId;
    private int _nPortalComponentId;
    private String _strPortletTypeId;
    private String _strDescription;
    private String _strPortletTypeName;
    private String _strPortalComponentName;

    /**
     * Returns the identifier of this style
     *
     * @return the identifier of the style
     */
    public int getId(  )
    {
        return _nId;
    }

    /**
     * Sets the identifier of this style with the int value specified in parameter
     *
     * @param nId the new identifier
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the identifier of the portlet type of this style
     *
     * @return the identifier of the portlet type style
     */
    public String getPortletTypeId(  )
    {
        return _strPortletTypeId;
    }

    /**
     * Sets the identifier of the portlet type of this style with the int value specified in parameter
     *
     * @param strPortletTypeId the identifier of portlet type of the style
     */
    public void setPortletTypeId( String strPortletTypeId )
    {
        _strPortletTypeId = strPortletTypeId;
    }

    /**
     * Returns the identifier of the portal component of this style
     *
     * @return the identifier of the portal component's style
     */
    public int getPortalComponentId(  )
    {
        return _nPortalComponentId;
    }

    /**
     * Sets the identifier of the portal component of this style with the int value specified in parameter
     *
     * @param nPortalComponentId the identifier of portal component of the style
     */
    public void setPortalComponentId( int nPortalComponentId )
    {
        _nPortalComponentId = nPortalComponentId;
    }

    /**
     * Returns the description of this style
     *
     * @return the description of this style
     */
    public String getDescription(  )
    {
        return _strDescription;
    }

    /**
     * Sets the description of this style with the String value specified in parameter or "" otherwise
     *
     * @param strDescription the identifier of portlet type of the style
     */
    public void setDescription( String strDescription )
    {
        _strDescription = ( strDescription == null ) ? EMPTY_STRING : strDescription;
    }

    /**
     * Returns the portlet type name of this style
     *
     * @return the portlet type name of this style
     */
    public String getPortletTypeName(  )
    {
        return _strPortletTypeName;
    }

    /**
     * Sets the portlet type name of this style with the String value specified in parameter
     *
     * @param strPortletTypeName the new portlet type name
     */
    public void setPortletTypeName( String strPortletTypeName )
    {
        _strPortletTypeName = ( strPortletTypeName == null ) ? EMPTY_STRING : strPortletTypeName;
    }

    /**
     * Returns the portal component name of this style
     *
     * @return the portal component name of this style
     */
    public String getPortalComponentName(  )
    {
        return _strPortalComponentName;
    }

    /**
     * Sets the portlet type name of this style with the String value specified in parameter
     *
     * @param strPortalComponentName the new portlet type name
     */
    public void setPortalComponentName( String strPortalComponentName )
    {
        _strPortalComponentName = ( strPortalComponentName == null ) ? EMPTY_STRING : strPortalComponentName;
    }
}
