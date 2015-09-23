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
package fr.paris.lutece.portal.web.upload;

import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.PortalJspBean;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;


/**
 * A rewrite of the multipart filter from the com.oreilly.servlet package. The
 * rewrite allows us to use initialization parameters specified in the Lutece
 * configuration files.
 * This filter concern site (front office) jsp pages
 */
public class UploadFilterSite extends UploadFilter
{
    /**
     * {@inheritDoc}
     */
    @Override
    protected String getMessageRelativeUrl( HttpServletRequest request, String strMessageKey, Object[] messageArgs,
        String strTitleKey )
    {
        // Fetch the upload filter site next url
        String strNextUrl = PortalJspBean.getUploadFilterSiteNextUrl( request );

        try
        {
            if ( StringUtils.isNotBlank( strNextUrl ) )
            {
                // If there is a next url from the session, then use this site message
                PortalJspBean.removeUploadFilterSiteNextUrl( request );
                SiteMessageService.setMessage( request, strMessageKey, messageArgs, strTitleKey, null, "",
                    SiteMessage.TYPE_STOP, null, strNextUrl );
            }
            else
            {
                // Otherwise, use the old site message
                SiteMessageService.setMessage( request, strMessageKey, messageArgs, strTitleKey, null, "",
                    SiteMessage.TYPE_STOP );
            }
        }
        catch ( SiteMessageException lme )
        {
            return AppPathService.getSiteMessageUrl( request );
        }

        return null;
    }
}
