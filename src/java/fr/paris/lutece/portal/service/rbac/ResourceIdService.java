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
package fr.paris.lutece.portal.service.rbac;

import fr.paris.lutece.util.ReferenceList;

import java.util.Locale;


/**
 * This class gives the methods to implement in order to allow the restitution of the ids and labels
 * for a given resource type.
 */
public abstract class ResourceIdService
{
    // the plugin name
    private String _strPluginName;

    /**
     * This method should register permissions and resource type
     */
    public abstract void register(  );

    /**
     * Provide the list of all resources (id - label)
     *
     * @return the list of all resources available for restricted access
     * @param locale The locale
     */
    public abstract ReferenceList getResourceIdList( Locale locale );

    /**
     * Provide the title of a resource given its id
     *
     * @return the title the corresponding label
     * @param locale The locale
     * @param strId the id of the resource
     */
    public abstract String getTitle( String strId, Locale locale );

    /**
     * Sets the name of the plugin associated
     * @param strPluginName the plugin name
     */
    public void setPluginName( String strPluginName )
    {
        _strPluginName = strPluginName;
    }

    /**
     * Gets the name of the plugin associated
     * @return the plugin name
     */
    public String getPluginName(  )
    {
        return _strPluginName;
    }
}
