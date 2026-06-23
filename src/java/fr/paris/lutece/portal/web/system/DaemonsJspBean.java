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
package fr.paris.lutece.portal.web.system;

import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.daemon.AppDaemonService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.apache.commons.lang3.StringUtils;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage daemons through the admin MVC front-controller.
 */
@RequestScoped
@Named
@Controller( name = "daemons", right = "CORE_DAEMONS_MANAGEMENT" )
public class DaemonsJspBean extends MVCAdminJspBean
{
    public static final String RIGHT_DAEMONS_MANAGEMENT = "CORE_DAEMONS_MANAGEMENT";
    private static final long serialVersionUID = 3636973660388119199L;
    private static final String TEMPLATE_MANAGE_DAEMONS = "admin/system/manage_daemons.html";
    private static final String MARK_DAEMONS_LIST = "daemons_list";
    private static final String MARK_LOG_MAX_SIZE = "log_max_size";
    private static final String PARAMETER_DAEMON = "daemon";
    private static final String PARAMETER_INTERVAL = "interval";
    private static final String PARAMETER_CRON = "cron";

    // Views
    private static final String VIEW_MANAGE_DAEMONS = "manageDaemons";

    // Actions
    private static final String ACTION_START = "START";
    private static final String ACTION_STOP = "STOP";
    private static final String ACTION_RUN = "RUN";
    private static final String ACTION_UPDATE_INTERVAL = "UPDATE_INTERVAL";
    private static final String ACTION_UPDATE_CRON = "UPDATE_CRON";

    private static final String PROPERTY_FIELD_INTERVAL = "portal.system.manage_daemons.columnTitleInterval";
    private static final String PROPERTY_FIELD_CRON = "portal.system.manage_daemons.columnTitleIntervalOrCron";
    private static final String PROPERTY_DAEMON_LASTRUNLOG_MAX_SIZE = "portal.system.manage_daemons.lastrunlog.maxsize";
    private static final String MESSAGE_MANDATORY_FIELD = "portal.util.message.mandatoryField";
    private static final String MESSAGE_NUMERIC_FIELD = "portal.util.message.errorNumericField";
    private static final String MESSAGE_INVALID_CRON = "portal.util.message.invalidEntry";

    @Inject
    private Models _models;

    /**
     * Build the manage daemon page.
     *
     * @param request
     *            The HTTP request
     * @return The Manage daemon page
     */
    @View( value = VIEW_MANAGE_DAEMONS, defaultView = true )
    public String getManageDaemons( HttpServletRequest request )
    {
        _models.put( MARK_LOG_MAX_SIZE, AppPropertiesService.getPropertyInt( PROPERTY_DAEMON_LASTRUNLOG_MAX_SIZE, 30000 ) );
        _models.put( MARK_DAEMONS_LIST, AppDaemonService.getDaemonEntries( ) );
        _models.put( SecurityTokenService.MARK_TOKEN, getSecurityTokenService( ).getToken( request, TEMPLATE_MANAGE_DAEMONS ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_DAEMONS, getLocale( ), _models );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Starts a daemon.
     *
     * @param request
     *            The HTTP request
     * @return The redirection to the admin home
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Action( ACTION_START )
    public String doStartDaemon( HttpServletRequest request ) throws AccessDeniedException
    {
        assertSecurityToken( request );
        AppDaemonService.startDaemon( request.getParameter( PARAMETER_DAEMON ) );

        return redirect( request, getHomeUrl( request ) );
    }

    /**
     * Stops a daemon.
     *
     * @param request
     *            The HTTP request
     * @return The redirection to the admin home
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Action( ACTION_STOP )
    public String doStopDaemon( HttpServletRequest request ) throws AccessDeniedException
    {
        assertSecurityToken( request );
        AppDaemonService.stopDaemon( request.getParameter( PARAMETER_DAEMON ) );

        return redirect( request, getHomeUrl( request ) );
    }

    /**
     * Signals a daemon for an immediate run.
     *
     * @param request
     *            The HTTP request
     * @return The redirection to the admin home
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Action( ACTION_RUN )
    public String doRunDaemon( HttpServletRequest request ) throws AccessDeniedException
    {
        assertSecurityToken( request );
        AppDaemonService.signalDaemon( request.getParameter( PARAMETER_DAEMON ) );

        return redirect( request, getHomeUrl( request ) );
    }

    /**
     * Updates the interval of a daemon.
     *
     * @param request
     *            The HTTP request
     * @return The redirection to the admin home, or an error message
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Action( ACTION_UPDATE_INTERVAL )
    public String doUpdateInterval( HttpServletRequest request ) throws AccessDeniedException
    {
        String strDaemonKey = request.getParameter( PARAMETER_DAEMON );
        String strDaemonInterval = request.getParameter( PARAMETER_INTERVAL );

        Object [ ] tabFieldInterval = {
                I18nService.getLocalizedString( PROPERTY_FIELD_INTERVAL, getLocale( ) )
        };

        String strErrorMessage = null;

        if ( StringUtils.isEmpty( strDaemonInterval ) )
        {
            strErrorMessage = MESSAGE_MANDATORY_FIELD;
        }
        else
            if ( !StringUtils.isNumeric( strDaemonInterval ) )
            {
                strErrorMessage = MESSAGE_NUMERIC_FIELD;
            }

        if ( strErrorMessage != null )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, strErrorMessage, tabFieldInterval, AdminMessage.TYPE_STOP ) );
        }

        assertSecurityToken( request );
        AppDaemonService.modifyDaemonInterval( strDaemonKey, strDaemonInterval );

        return redirect( request, getHomeUrl( request ) );
    }

    /**
     * Updates the cron expression of a daemon.
     *
     * @param request
     *            The HTTP request
     * @return The redirection to the admin home, or an error message
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Action( ACTION_UPDATE_CRON )
    public String doUpdateCron( HttpServletRequest request ) throws AccessDeniedException
    {
        String strDaemonKey = request.getParameter( PARAMETER_DAEMON );
        String strDaemonCron = request.getParameter( PARAMETER_CRON );

        Object [ ] tabFieldCron = {
                I18nService.getLocalizedString( PROPERTY_FIELD_CRON, getLocale( ) )
        };

        String strCronErrorMessage = null;

        if ( StringUtils.isEmpty( strDaemonCron ) )
        {
            strCronErrorMessage = MESSAGE_MANDATORY_FIELD;
        }
        else
            if ( !AppDaemonService.isValidCronExpression( strDaemonCron ) )
            {
                strCronErrorMessage = MESSAGE_INVALID_CRON;
            }

        if ( strCronErrorMessage != null )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, strCronErrorMessage, tabFieldCron, AdminMessage.TYPE_STOP ) );
        }

        assertSecurityToken( request );
        AppDaemonService.modifyDaemonCron( strDaemonKey, strDaemonCron );

        return redirect( request, getHomeUrl( request ) );
    }

    /**
     * Asserts that the security token is valid.
     *
     * @param request
     *            the request
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    private void assertSecurityToken( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !getSecurityTokenService( ).validate( request, TEMPLATE_MANAGE_DAEMONS ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
    }
}
