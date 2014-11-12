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
package fr.paris.lutece.portal.web.system;

import fr.paris.lutece.portal.service.daemon.AppDaemonService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.admin.AdminPageJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 *
 */
public class DaemonsJspBean extends AdminPageJspBean
{
    public static final String RIGHT_DAEMONS_MANAGEMENT = "CORE_DAEMONS_MANAGEMENT";
    private static final long serialVersionUID = 3636973660388119199L;
    private static final String TEMPLATE_MANAGE_DAEMONS = "admin/system/manage_daemons.html";
    private static final String MARK_DAEMONS_LIST = "daemons_list";
    private static final String PARAMETER_DAEMON = "daemon";
    private static final String PARAMETER_ACTION = "action";
    private static final String PARAMETER_INTERVAL = "interval";
    private static final String ACTION_START = "START";
    private static final String ACTION_STOP = "STOP";
    private static final String ACTION_UPDATE_INTERVAL = "UPDATE_INTERVAL";
    private static final String PROPERTY_FIELD_INTERVAL = "portal.system.manage_daemons.columnTitleInterval";
    private static final String MESSAGE_MANDATORY_FIELD = "portal.util.message.mandatoryField";
    private static final String MESSAGE_NUMERIC_FIELD = "portal.util.message.errorNumericField";

    /**
     * Build the manage daemon page
     * @param request The HTTP request
     * @return The Manage daemon page
     */
    public String getManageDaemons( HttpServletRequest request )
    {
        HashMap<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_DAEMONS_LIST, AppDaemonService.getDaemonEntries(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_DAEMONS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the daemon action
     * @param request The HTTP request
     * @return The forward URL
     */
    public String doDaemonAction( HttpServletRequest request )
    {
        String strAction = request.getParameter( PARAMETER_ACTION );
        String strDaemonKey = request.getParameter( PARAMETER_DAEMON );

        if ( strAction.equalsIgnoreCase( ACTION_START ) )
        {
            AppDaemonService.startDaemon( strDaemonKey );
        }
        else if ( strAction.equalsIgnoreCase( ACTION_STOP ) )
        {
            AppDaemonService.stopDaemon( strDaemonKey );
        }

        else if ( strAction.equalsIgnoreCase( ACTION_UPDATE_INTERVAL ) )
        {
            String strErrorMessage = null;
            String strDaemonInterval = request.getParameter( PARAMETER_INTERVAL );

            Object[] tabFieldInterval = { I18nService.getLocalizedString( PROPERTY_FIELD_INTERVAL, getLocale(  ) ) };

            if ( StringUtils.isEmpty( strDaemonInterval ) )
            {
                strErrorMessage = MESSAGE_MANDATORY_FIELD;
            }
            else if ( !StringUtils.isNumeric( strDaemonInterval ) )
            {
                strErrorMessage = MESSAGE_NUMERIC_FIELD;
            }

            if ( strErrorMessage != null )
            {
                return AdminMessageService.getMessageUrl( request, strErrorMessage, tabFieldInterval,
                    AdminMessage.TYPE_STOP );
            }

            AppDaemonService.modifyDaemonInterval( strDaemonKey, strDaemonInterval );
        }

        return getHomeUrl( request );
    }
}
