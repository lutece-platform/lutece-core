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
package fr.paris.lutece.portal.web.xss;

import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;

/**
 * This class extends SafeRequestFilter and use AdminMessageService for display error Message
 * 
 * @author merlinfe
 *
 */
public class SafeRequestFilterAdmin extends SafeRequestFilter
{
    private static final String PROPERTY_ACTIVATE_XSS_FILTER = "lutece.safe.request.admin.activateXssFilter";
    private static final String PROPERTY_SANITIZE_FILTER_MODE = "lutece.safe.request.admin.sanitizeFilterMode";
    private static final String PROPERTY_XSS_CHARACTERS = "lutece.safe.request.admin.xssCharacters";

    /**
     * {@inheritDoc}
     */
    @Override
    public void init( FilterConfig config ) throws ServletException
    {
        String xssFilter = AppPropertiesService.getProperty( PROPERTY_ACTIVATE_XSS_FILTER );
        String sanitizeFilterMode = AppPropertiesService.getProperty( PROPERTY_SANITIZE_FILTER_MODE );
        String xssCharacters = AppPropertiesService.getProperty( PROPERTY_XSS_CHARACTERS );

        if( xssFilter == null && sanitizeFilterMode==null && xssCharacters==null )
        {
            initFromFilterConfig( config );
        }
        else
        {
            initFilter( Boolean.parseBoolean(xssFilter), Boolean.parseBoolean(sanitizeFilterMode), xssCharacters );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getMessageUrl( HttpServletRequest request, String strMessageKey, Object [ ] messageArgs, String strTitleKey )
    {
        return AdminMessageService.getMessageUrl( request, strMessageKey, messageArgs, AdminMessage.TYPE_STOP );
    }
}
