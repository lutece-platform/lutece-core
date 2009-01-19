/*
 * Copyright (c) 2002-2009, Mairie de Paris
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
package fr.paris.lutece.portal.web.upload;

import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.util.AppPathService;

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;


/**
 * A rewrite of the multipart filter from the com.oreilly.servlet package. The
 * rewrite allows us to use initialization parameters specified in the Lutece
 * configuration files.
 * This filter concern site (front office) jsp pages
 */
public class UploadFilterSite extends UploadFilter
{
    private static final String PROPERTY_TITLE_FILE_SIZE_LIMIT_EXCEEDED = "portal.util.message.titleDefault";
    private static final String PROPERTY_MESSAGE_FILE_SIZE_LIMIT_EXCEEDED = "portal.util.message.fileSizeLimitExceeded";
    private static final int KILO_BYTE = 1024;

    /**
     * Get the error message url when file is bigger than the max size authorized
     * @param request The http request
     * @return The Message URL
     */
    protected String getMessageRelativeUrl( HttpServletRequest request )
    {
        long lSizeMax = getRequestSizeMax(  );

        try
        {
            DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance(  );
            decimalFormat.applyPattern( "#" );

            String strMessage = ( lSizeMax >= KILO_BYTE ) ? ( String.valueOf( lSizeMax / KILO_BYTE ) )
                                                          : ( decimalFormat.format( lSizeMax / KILO_BYTE ) );
            Object[] args = { strMessage };
            SiteMessageService.setMessage( request, PROPERTY_MESSAGE_FILE_SIZE_LIMIT_EXCEEDED, args,
                PROPERTY_TITLE_FILE_SIZE_LIMIT_EXCEEDED, null, "", SiteMessage.TYPE_STOP );
        }
        catch ( SiteMessageException lme )
        {
            return AppPathService.getPortalUrl(  );
        }

        return null;
    }
}
