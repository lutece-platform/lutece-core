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
package fr.paris.lutece.portal.web.xss;

import fr.paris.lutece.util.http.SecurityUtil;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * A rewrite of the HttpServletRequestWrapper for escaping xss characters which
 * could be contained inside the request
 */
public abstract class SafeRequestFilter implements Filter
{
    private static final String PROPERTY_TITLE_REQUEST_PARAMETERS_CONTAINS_XSS_CHARACTERS = "portal.util.message.titleDefault";
    private static final String PROPERTY_REQUEST_PARAMETERS_CONTAINS_XSS_CHARACTERS = "portal.util.message.requestParametersContainsXssCharacters";
    private static final String PARAM_FILTER_XSS_CHARATERS = "xssCharacters";
    private static final String ACTIVATE_XSS_FILTER = "activateXssFilter";
    private FilterConfig _filterConfig;
    private String _strXssCharacters;
    private boolean _bActivateXssFilter;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init( FilterConfig config ) throws ServletException
    {
        _filterConfig = config;

        String strParamValue = _filterConfig.getInitParameter( PARAM_FILTER_XSS_CHARATERS );
        _strXssCharacters = strParamValue;
        strParamValue = _filterConfig.getInitParameter( ACTIVATE_XSS_FILTER );

        if ( strParamValue != null )
        {
            _bActivateXssFilter = Boolean.valueOf( strParamValue );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy(  )
    {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
        throws IOException, ServletException
    {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if ( _bActivateXssFilter && ( _strXssCharacters != null ) && !_strXssCharacters.trim(  ).equals( "" ) &&
                !SecurityUtil.containsCleanParameters( httpRequest, _strXssCharacters ) )
        {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.sendRedirect( getMessageUrl( httpRequest,
                    PROPERTY_REQUEST_PARAMETERS_CONTAINS_XSS_CHARACTERS, null,
                    PROPERTY_TITLE_REQUEST_PARAMETERS_CONTAINS_XSS_CHARACTERS ) );
        }
        else
        {
            chain.doFilter( request, response );
        }
    }

    /**
     * Forward the error message url depends site or admin implementation
     * @param request @HttpServletRequest
     * @param strMessageKey the message key
     * @param messageArgs the message args
     * @param strTitleKey the title of the message
     * @return url
     */
    protected abstract String getMessageUrl( HttpServletRequest request, String strMessageKey, Object[] messageArgs,
        String strTitleKey );
}
