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
package fr.paris.lutece.portal.service.filter;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;


/**
 *
 */
public class LuteceFilterConfig implements FilterConfig
{
    private String _strName;
    private ServletContext _context;
    private Map<String, String> _mapInitParameters;

    /**
     * Constructor
     * @param strName The name
     * @param context The servlet context
     * @param mapInitParameters Init parameters as a Map
     */
    public LuteceFilterConfig( String strName, ServletContext context, Map<String, String> mapInitParameters )
    {
        _strName = strName;
        _context = context;
        _mapInitParameters = mapInitParameters;
    }

    /**
     * {@inheritDoc}
     */
    public String getFilterName(  )
    {
        return _strName;
    }

    /**
     * {@inheritDoc}
     */
    public ServletContext getServletContext(  )
    {
        return _context;
    }

    /**
     * {@inheritDoc}
     */
    public String getInitParameter( String strKey )
    {
        return (String) _mapInitParameters.get( strKey );
    }

    /**
     * {@inheritDoc}
     * @return
     */
    public Enumeration<String> getInitParameterNames(  )
    {
        return Collections.enumeration( _mapInitParameters.keySet(  ) );
    }
}
