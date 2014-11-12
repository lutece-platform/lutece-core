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
package fr.paris.lutece.portal.web.resource;

import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.pluginaction.DefaultPluginActionResult;
import fr.paris.lutece.portal.web.pluginaction.IPluginActionResult;
import fr.paris.lutece.portal.web.pluginaction.PluginActionManager;
import fr.paris.lutece.util.url.UrlItem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * This JSP bean is used to execute the {@link IExtendableResourcePluginAction}
 *
 */
public class ExtendableResourceJspBean extends AdminFeaturesPageJspBean
{
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -2783981113148281852L;

    /**
     * Do process extendable resource action.
     *
     * @param request the request
     * @param response the response
     * @return the i plugin action result
     * @throws AccessDeniedException the access denied exception
     */
    public IPluginActionResult doProcessExtendableResourceAction( HttpServletRequest request,
        HttpServletResponse response ) throws AccessDeniedException
    {
        IExtendableResourcePluginAction action = PluginActionManager.getPluginAction( request,
                IExtendableResourcePluginAction.class );

        if ( action != null )
        {
            AppLogService.debug( "Processing resource action " + action.getName(  ) );

            return action.process( request, response, getUser(  ), null );
        }

        // If no action, then redirect the user to the home page
        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + AppPathService.getAdminMenuUrl(  ) );
        DefaultPluginActionResult result = new DefaultPluginActionResult(  );
        result.setRedirect( url.getUrl(  ) );

        return result;
    }
}
