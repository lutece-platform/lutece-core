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
package fr.paris.lutece.portal.service.template;

import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.date.DateUtil;

import java.util.Locale;


/**
 * Template service based on the Freemarker template engine
 */
public class FreeMarkerTemplateService extends AbstractFreeMarkerTemplateService
{
    public static final String BEAN_SERVICE = "freeMarkerTemplateService";
    private static final String PROPERTY_TEMPLATE_UPDATE_DELAY = "service.freemarker.templateUpdateDelay";
    private static final int TEMPLATE_UPDATE_DELAY = AppPropertiesService.getPropertyInt( PROPERTY_TEMPLATE_UPDATE_DELAY,
            5 );
    private static volatile IFreeMarkerTemplateService _singleton;

    /**
     * Get the instance of the freemarker template service
     * @return the instance of the freemarker template service
     */
    public static IFreeMarkerTemplateService getInstance(  )
    {
        if ( _singleton == null )
        {
            synchronized ( FreeMarkerTemplateService.class )
            {
                FreeMarkerTemplateService service = new FreeMarkerTemplateService(  );
                service.setTemplateUpdateDelay( TEMPLATE_UPDATE_DELAY );
                _singleton = service;
            }
        }

        return _singleton;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAbsolutePathFromRelativePath( String strPath )
    {
        return AppPathService.getAbsolutePathFromRelativePath( strPath );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultPattern( Locale locale )
    {
        return DateUtil.getDefaultPattern( locale );
    }
}
