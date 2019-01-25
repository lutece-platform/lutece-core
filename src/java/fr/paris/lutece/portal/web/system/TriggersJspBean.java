/*
 * Copyright (c) 2002-2018, Mairie de Paris
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

import fr.paris.lutece.portal.business.daemon.DaemonTrigger;
import fr.paris.lutece.portal.business.daemon.DaemonTriggerHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.daemon.AppDaemonService;
import static fr.paris.lutece.portal.service.daemon.AppDaemonService.getDaemonEntries;
import fr.paris.lutece.portal.service.daemon.DaemonEntry;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.scheduler.JobSchedulerService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;
import java.util.Collection;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.quartz.CronExpression;

/**
 *
 */
@Controller( controllerJsp = "ManageTriggers.jsp", controllerPath = "jsp/admin/system/", right = "CORE_DAEMONS_MANAGEMENT" )
public class TriggersJspBean extends MVCAdminJspBean
{
    // Templates
    private static final String TEMPLATE_MANAGE_TRIGGERS = "admin/trigger/manage_triggers.html";
    private static final String TEMPLATE_CREATE_TRIGGER = "admin/trigger/create_trigger.html";
    private static final String TEMPLATE_MODIFY_TRIGGER = "admin/trigger/modify_trigger.html";

    // Parameters
    private static final String PARAMETER_DAEMON_TRIGGER_ID = "id";
    private static final String PARAMETER_TRIGGER_KEY = "key";
    private static final String PARAMETER_TRIGGER_GROUP = "group";
    private static final String PARAMETER_CRON_EXPRESSION = "cron_expression";
    private static final String PARAMETER_DAEMON_KEY = "daemon_key";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_TRIGGERS = "portal.system.manage_triggers.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_TRIGGER = "portal.system.modify_trigger.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_TRIGGER = "portal.system.create_trigger.pageTitle";

    // Markers
    private static final String MARK_DAEMON_TRIGGER_LIST = "daemon_triggers_list";
    private static final String MARK_DAEMON_TRIGGER = "daemon_trigger";
    private static final String MARK_DAEMONS_LIST = "daemons_list";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";

    // JSP
    private static final String JSP_MANAGE_TRIGGERS = "jsp/admin/system/ManageTriggers.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_TRIGGER = "portal.system.message.confirmRemoveTrigger";
    private static final String MESSAGE_TRIGGER_EXIST_ERROR = "portal.system.message.triggerExistsError";
    private static final String MESSAGE_INVALID_CRON_EXPRESSION_ERROR = "portal.system.message.invalidCronExpressionError";
    private static final String MESSAGE_INVALID_DAEMON_KEY_ERROR = "portal.system.message.invalidDaemonKeyError";
    private static final String PROPERTY_DEFAULT_LIST_ITEM_PER_PAGE = "daemon.daemontrigger.listItems.itemsPerPage";
    private static final String EMPTY_STRING = "";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "portal.system.model.entity.daemontrigger.attribute.";

    // Views
    private static final String VIEW_MANAGE_TRIGGERS = "manageTriggers";
    private static final String VIEW_CREATE_TRIGGER = "createTrigger";
    private static final String VIEW_MODIFY_TRIGGER = "modifyTrigger";

    // Actions
    private static final String ACTION_CREATE_TRIGGER = "createTrigger";
    private static final String ACTION_MODIFY_TRIGGER = "modifyTrigger";
    private static final String ACTION_REMOVE_TRIGGER = "removeTrigger";
    private static final String ACTION_CONFIRM_REMOVE_TRIGGER = "confirmRemoveTrigger";

    // Infos
    private static final String INFO_TRIGGER_CREATED = "portal.system.job.info.trigger.created";
    private static final String INFO_TRIGGER_UPDATED = "portal.system.job.info.trigger.updated";
    private static final String INFO_TRIGGER_REMOVED = "portal.system.job.info.trigger.removed";

    //Variables
    private int _nDefaultItemsPerPage;
    private String _strCurrentPageIndex;
    private int _nItemsPerPage;


    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_TRIGGERS, defaultView = true )
    public String getManageTriggers( HttpServletRequest request )
    {
        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_LIST_ITEM_PER_PAGE, 50 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage, _nDefaultItemsPerPage );

        UrlItem url = new UrlItem( JSP_MANAGE_TRIGGERS );
        String strUrl = url.getUrl(  );

        List<DaemonTrigger> listDaemonTriggers = DaemonTriggerHome.getDaemonTriggersList( );

        // PAGINATOR
        LocalizedPaginator<DaemonTrigger> paginator = new LocalizedPaginator<DaemonTrigger>( listDaemonTriggers, _nItemsPerPage, strUrl, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex, getLocale(  ) );

        Map<String, Object> model = new HashMap<String, Object>( );

        model.put( MARK_NB_ITEMS_PER_PAGE, String.valueOf( _nItemsPerPage ) );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_DAEMON_TRIGGER_LIST, paginator.getPageItems(  ) );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_TRIGGERS, TEMPLATE_MANAGE_TRIGGERS, model );
    }

    /**
     * Returns the form to create a trigger
     *
     * @param request The Http request
     * @return the html code of the trigger form
     */
    @View( VIEW_CREATE_TRIGGER )
    public String getCreateTrigger( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_DAEMONS_LIST, getListDaemons( true ) );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_CREATE_TRIGGER ) );
        return getPage( PROPERTY_PAGE_TITLE_CREATE_TRIGGER, TEMPLATE_CREATE_TRIGGER, model );
    }

    /**
     * Process the data capture form of a new trigger
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Action( ACTION_CREATE_TRIGGER )
    public String doCreateTrigger( HttpServletRequest request ) throws AccessDeniedException
    {
        String strTriggerKey = request.getParameter( PARAMETER_TRIGGER_KEY );
        String strTriggerGroup = request.getParameter( PARAMETER_TRIGGER_GROUP );
        String strCronExpression = request.getParameter( PARAMETER_CRON_EXPRESSION );
        String strDaemonKey = request.getParameter( PARAMETER_DAEMON_KEY );

        DaemonTrigger daemonTrigger = new DaemonTrigger( );
        populate( daemonTrigger, request );

        if ( !validateBean( daemonTrigger, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_CREATE_TRIGGER );
        }
        if ( JobSchedulerService.getInstance( ).findTriggerByKey( strTriggerKey, strTriggerGroup ) != null )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_TRIGGER_EXIST_ERROR, AdminMessage.TYPE_STOP ) );
        }
        if ( !CronExpression.isValidExpression( strCronExpression ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_INVALID_CRON_EXPRESSION_ERROR, AdminMessage.TYPE_STOP ) );
        }
        if ( !AppDaemonService.checkDaemonKey( strDaemonKey ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_INVALID_DAEMON_KEY_ERROR, AdminMessage.TYPE_STOP ) );
        }
        assertSecurityToken( request, TEMPLATE_CREATE_TRIGGER );

        JobSchedulerService.getInstance( ).createTrigger( daemonTrigger );
        DaemonTriggerHome.create( daemonTrigger );
        addInfo( INFO_TRIGGER_CREATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_TRIGGERS );
    }

    /**
     * Manages the removal form of a trigger whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_TRIGGER )
    public String getConfirmRemoveTrigger( HttpServletRequest request )
    {
        String strDaemonTriggerKey = request.getParameter( PARAMETER_DAEMON_TRIGGER_ID );

        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_TRIGGER ) );
        url.addParameter( PARAMETER_DAEMON_TRIGGER_ID, strDaemonTriggerKey );
        url.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, JSP_MANAGE_TRIGGERS ) );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_TRIGGER, url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a trigger
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage triggers
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Action( ACTION_REMOVE_TRIGGER )
    public String doRemoveTrigger( HttpServletRequest request ) throws AccessDeniedException
    {
        String strDaemonTriggerId = request.getParameter( PARAMETER_DAEMON_TRIGGER_ID );
        int nDaemonTriggerId = Integer.parseInt( strDaemonTriggerId );

        assertSecurityToken( request, JSP_MANAGE_TRIGGERS );

        DaemonTrigger daemonTrigger = DaemonTriggerHome.findByPrimaryKey( nDaemonTriggerId );

        JobSchedulerService.getInstance( ).removeTrigger( daemonTrigger );
        DaemonTriggerHome.remove( nDaemonTriggerId );
        addInfo( INFO_TRIGGER_REMOVED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_TRIGGERS );
    }

    /**
     * Returns the form to update info about a trigger
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_TRIGGER )
    public String getModifyTrigger( HttpServletRequest request )
    {
        String strDaemonTriggerId = request.getParameter( PARAMETER_DAEMON_TRIGGER_ID );
        int nDaemonTriggerId = Integer.parseInt( strDaemonTriggerId );

        DaemonTrigger daemonTrigger = DaemonTriggerHome.findByPrimaryKey( nDaemonTriggerId );

        Map<String, Object> model = getModel(  );
        model.put( MARK_DAEMON_TRIGGER, daemonTrigger );
        model.put( MARK_DAEMONS_LIST, getListDaemons( false ) );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_MODIFY_TRIGGER ) );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_TRIGGER, TEMPLATE_MODIFY_TRIGGER, model );
    }

    /**
     * Process the change form of a trigger
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Action( ACTION_MODIFY_TRIGGER )
    public String doModifyTrigger( HttpServletRequest request ) throws AccessDeniedException
    {
        String strDaemonTriggerId = request.getParameter( PARAMETER_DAEMON_TRIGGER_ID );
        int nDaemonTriggerId = Integer.parseInt( strDaemonTriggerId );
        String strTriggerKey = request.getParameter( PARAMETER_TRIGGER_KEY );
        String strTriggerGroup = request.getParameter( PARAMETER_TRIGGER_GROUP );
        String strCronExpression = request.getParameter( PARAMETER_CRON_EXPRESSION );
        String strDaemonKey = request.getParameter( PARAMETER_DAEMON_KEY );

        DaemonTrigger oldDaemonTrigger = DaemonTriggerHome.findByPrimaryKey( nDaemonTriggerId );
        DaemonTrigger newDaemonTrigger = new DaemonTrigger( );
        populate( newDaemonTrigger, request );

        if ( !validateBean( newDaemonTrigger, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_TRIGGER, PARAMETER_DAEMON_TRIGGER_ID, newDaemonTrigger.getId( ) );
        }
        if ( !CronExpression.isValidExpression( strCronExpression ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_INVALID_CRON_EXPRESSION_ERROR, AdminMessage.TYPE_STOP ) );
        }
        if ( !AppDaemonService.checkDaemonKey( strDaemonKey ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_INVALID_DAEMON_KEY_ERROR, AdminMessage.TYPE_STOP ) );
        }
        if ( JobSchedulerService.getInstance( ).findTriggerByKey( strTriggerKey, strTriggerGroup ) != null 
                && ( !oldDaemonTrigger.getKey( ).equals( strTriggerKey ) || !oldDaemonTrigger.getGroup( ).equals( strTriggerGroup ) ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_TRIGGER_EXIST_ERROR, AdminMessage.TYPE_STOP ) );
        }
        assertSecurityToken( request, TEMPLATE_MODIFY_TRIGGER );

        JobSchedulerService.getInstance( ).updateTrigger( oldDaemonTrigger, newDaemonTrigger );
        DaemonTriggerHome.update( newDaemonTrigger );
        addInfo( INFO_TRIGGER_UPDATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_TRIGGERS );
    }

    /**
     * Returns the list of daemons
     * 
     * @param bEmptyString True to add an empty string in the list
     * @return the list of daemons
     */
    private ReferenceList getListDaemons( boolean bEmptyString )
    {
        ReferenceList refList = new ReferenceList( );

        if ( bEmptyString )
        {
            // add empty item
            refList.addItem( EMPTY_STRING, EMPTY_STRING );
        }

        Collection<DaemonEntry> listDaemonEntries = getDaemonEntries( );

        for ( DaemonEntry daemonEntry : listDaemonEntries )
        {
            refList.addItem( daemonEntry.getId( ), I18nService.getLocalizedString( daemonEntry.getNameKey( ), getLocale( ) ) );
        }

        return refList;
    }

    /**
     * Asserts that the security token is valid
     * 
     * @param request
     *            the request
     * @param strTemplate
     *            the template of the page to access
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    private void assertSecurityToken( HttpServletRequest request, String strTemplate ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, strTemplate ) )
        {
            throw new AccessDeniedException( "Invalid security token" );
        }
    }
}
