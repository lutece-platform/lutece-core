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
package fr.paris.lutece.portal.service.servlet;

import java.util.HashMap;
import java.util.Map;


/**
 * Servlet Entry used to load servlet from the plugin XML file
 * @since v 3.0
 */
public class ServletEntry
{
    private String _strName;
    private String _strServletClass;
    private String _strMapping;
    private Map<String, String> _mapInitParameters = new HashMap<String, String>(  );

    /**
     * Returns the Name
     *
     * @return The Name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the Name
     *
     * @param strName The Name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Returns the servlet
     *
     * @return The servlet
     */
    public String getServletClass(  )
    {
        return _strServletClass;
    }

    /**
     * Sets the servlet.
     *
     * @param strServletClass the new servlet class
     */
    public void setServletClass( String strServletClass )
    {
        _strServletClass = strServletClass;
    }

    /**
     * Returns the Mapping
     *
     * @return The Mapping
     */
    public String getMappingUrlPattern(  )
    {
        return _strMapping;
    }

    /**
     * Sets the Mapping
     *
     * @param strMapping The Mapping
     */
    public void setMappingUrlPattern( String strMapping )
    {
        _strMapping = strMapping;
    }

    /**
     * Add an init parameter
     * @param strName The parameter name
     * @param strValue The parameter value
     */
    public void addParameter( String strName, String strValue )
    {
        _mapInitParameters.put( strName, strValue );
    }

    /**
     * Returns init parameters
     * @return Init parameters in a map object
     */
    public Map<String, String> getInitParameters(  )
    {
        return _mapInitParameters;
    }
}
