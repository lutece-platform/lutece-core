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
package fr.paris.lutece.portal.business.portlet;

import fr.paris.lutece.portal.service.rbac.RBACResource;

/**
 * A FreeMarker template available to render the portlets of a given portlet type. It replaces the XSL styles of the former versions : a portlet chooses
 * one of the templates registered for its type, and renders its content through it.
 */
public class PortletTemplate implements RBACResource
{
    /** The RBAC resource type of the portlet templates */
    public static final String RESOURCE_TYPE = "PORTLET_TEMPLATE";

    private int _nId;
    private String _strPortletTypeId;
    private String _strDescription;
    private String _strTemplatePath;

    /**
     * Returns the template identifier
     *
     * @return the template identifier
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the template identifier
     *
     * @param nId
     *            the template identifier
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the identifier of the portlet type the template applies to
     *
     * @return the portlet type identifier
     */
    public String getPortletTypeId( )
    {
        return _strPortletTypeId;
    }

    /**
     * Sets the identifier of the portlet type the template applies to
     *
     * @param strPortletTypeId
     *            the portlet type identifier
     */
    public void setPortletTypeId( String strPortletTypeId )
    {
        _strPortletTypeId = strPortletTypeId;
    }

    /**
     * Returns the label displayed to the administrator
     *
     * @return the description
     */
    public String getDescription( )
    {
        return _strDescription;
    }

    /**
     * Sets the label displayed to the administrator
     *
     * @param strDescription
     *            the description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * Returns the template path, relative to the WEB-INF/templates directory
     *
     * @return the template path
     */
    public String getTemplatePath( )
    {
        return _strTemplatePath;
    }

    /**
     * Sets the template path, relative to the WEB-INF/templates directory
     *
     * @param strTemplatePath
     *            the template path
     */
    public void setTemplatePath( String strTemplatePath )
    {
        _strTemplatePath = strTemplatePath;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getResourceTypeCode( )
    {
        return RESOURCE_TYPE;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getResourceId( )
    {
        return String.valueOf( _nId );
    }
}
