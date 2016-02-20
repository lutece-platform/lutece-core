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
package fr.paris.lutece.portal.web.admin;

import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.business.portlet.PortletType;
import fr.paris.lutece.portal.business.portlet.PortletTypeHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.portlet.PortletRemovalListenerService;
import fr.paris.lutece.portal.service.portlet.PortletResourceIdService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.util.url.UrlItem;

import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * This class provides the admin interface to manage administration of portlet on the pages
 */
public class AdminPagePortletJspBean extends AdminFeaturesPageJspBean
{
    // Right
    public static final String RIGHT_MANAGE_ADMIN_SITE = "CORE_ADMIN_SITE";
    private static final String PROPERTY_MESSAGE_WARNING_PORTLET_ALIAS = "portal.site.message.warningPortletAlias";
    private static final String PROPERTY_MESSAGE_CONFIRM_REMOVE_PORTLET = "portal.site.message.confirmRemovePortlet";
    private static final String MESSAGE_CANNOT_REMOVE_PORTLET = "portal.site.message.cannotRemovePortlet";
    private static final String MESSAGE_CANNOT_REMOVE_PORTLET_TITLE = "portal.site.message.cannotRemovePortlet.title";
    private static final String PROPERTY_MESSAGE_CONFIRM_MODIFY_STATUS = "portal.site.message.confirmModifyStatus";
    private static final String PORTLET_STATUS = "status";
    private static final String JSP_REMOVE_PORTLET = "jsp/admin/site/DoRemovePortlet.jsp";
    private static final String JSP_DO_MODIFY_STATUS = "jsp/admin/site/DoModifyPortletStatus.jsp";
    private static final String JSP_ADMIN_SITE = "AdminSite.jsp";

    /**
     * Processes the modification of a portlet whose identifier is stored in the http request
     *
     * @param request The http request
     * @return The jsp url of the process result
     */
    public String doModifyPortlet( HttpServletRequest request )
    {
        String strUrl = null;
        String strPortletId = request.getParameter( Parameters.PORTLET_ID );

        int nPortletId = Integer.parseInt( strPortletId );
        Portlet portlet = PortletHome.findByPrimaryKey( nPortletId );

        for ( PortletType portletType : PortletTypeHome.getPortletTypesList( getLocale(  ) ) )
        {
            if ( portletType.getId(  ).equals( portlet.getPortletTypeId(  ) ) )
            {
                UrlItem url = new UrlItem( portletType.getUrlUpdate(  ) );
                url.addParameter( Parameters.PORTLET_ID, nPortletId );
                strUrl = url.getUrl(  );

                break;
            }
        }

        return strUrl;
    }

    /**
     * Redirects towards the url of the process creation of a portlet according to its type or null if the portlet type
     * doesn't exist.
     *
     * @param request The http request
     * @return The jsp url of the portlet type process creation
     */
    public String doCreatePortlet( HttpServletRequest request )
    {
        String strUrl = null;
        String strPortletTypeId = request.getParameter( Parameters.PORTLET_TYPE_ID );

        for ( PortletType portletType : PortletTypeHome.getPortletTypesList( getLocale(  ) ) )
        {
            if ( portletType.getId(  ).equals( strPortletTypeId ) )
            {
                strUrl = portletType.getUrlCreation(  );

                break;
            }
        }

        return strUrl;
    }

    /**
     * Displays the confirm message for deleting the portlet
     *
     * @param request The http request
     * @return The confirm page
     * @throws AccessDeniedException if the user is not authorized to manage the portlet
     */
    public String getRemovePortlet( HttpServletRequest request ) throws AccessDeniedException
    {
        String strPortletId = request.getParameter( Parameters.PORTLET_ID );
        if ( !StringUtils.isNumeric( strPortletId ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_ERROR);
        }
        int nPortletId = Integer.parseInt( strPortletId );
        Portlet portlet = null;
        try
        {
            portlet = PortletHome.findByPrimaryKey( nPortletId );
        } catch (NullPointerException e)
        {
            AppLogService.error( "Error looking for portlet with id " + nPortletId, e );
        }
        if ( portlet == null || portlet.getId( ) != nPortletId )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MESSAGE_INVALID_ENTRY, new Object[] { nPortletId }, AdminMessage.TYPE_ERROR);
        }
        AdminUser user = AdminUserService.getAdminUser( request );
        if ( !RBACService.isAuthorized( PortletType.RESOURCE_TYPE, portlet.getPortletTypeId(  ),
                PortletResourceIdService.PERMISSION_MANAGE, user ) )
        {
            throw new AccessDeniedException( "User " + user + " is not authorized to permission " + PortletResourceIdService.PERMISSION_MANAGE
                    + " on portlet " + nPortletId );
        }
        String strUrl = JSP_REMOVE_PORTLET + "?portlet_id=" + strPortletId;
        String strTarget = "_top";
        if ( PortletHome.hasAlias( nPortletId ) )
        {
            return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_WARNING_PORTLET_ALIAS,
                    new Object[] { portlet.getName( ) },
                    null, strUrl, strTarget, AdminMessage.TYPE_CONFIRMATION );
        }

        ArrayList<String> listErrors = new ArrayList<String>(  );
        Locale locale = AdminUserService.getLocale( request );
        if ( !PortletRemovalListenerService.getService(  ).checkForRemoval( strPortletId, listErrors, locale ) )
        {
            String strCause = AdminMessageService.getFormattedList( listErrors, locale );
            Object[] args = { strCause, portlet.getName( ) };

            return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_REMOVE_PORTLET, args,
                MESSAGE_CANNOT_REMOVE_PORTLET_TITLE, strUrl, strTarget, AdminMessage.TYPE_STOP );
        }

        return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_CONFIRM_REMOVE_PORTLET,
                new Object[] { portlet.getName( ) }, null, strUrl, strTarget,
                AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Processes the removal of the portlet
     *
     * @param request The http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException if the user is not authorized to manage the portlet
     */
    public String doRemovePortlet( HttpServletRequest request ) throws AccessDeniedException
    {
        String strPortletId = request.getParameter( Parameters.PORTLET_ID );
        if ( !StringUtils.isNumeric( strPortletId ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_ERROR);
        }
        int nPortletId = Integer.parseInt( strPortletId );
        Portlet portlet = null;
        try
        {
            portlet = PortletHome.findByPrimaryKey( nPortletId );
        } catch (NullPointerException e)
        {
            AppLogService.error( "Error looking for portlet with id " + nPortletId, e );
        }
        if ( portlet == null || portlet.getId( ) != nPortletId )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MESSAGE_INVALID_ENTRY, new Object[] { nPortletId }, AdminMessage.TYPE_ERROR);
        }
        AdminUser user = AdminUserService.getAdminUser( request );
        if ( !RBACService.isAuthorized( PortletType.RESOURCE_TYPE, portlet.getPortletTypeId(  ),
                PortletResourceIdService.PERMISSION_MANAGE, user ) )
        {
            throw new AccessDeniedException( "User " + user + " is not authorized to permission " + PortletResourceIdService.PERMISSION_MANAGE
                    + " on portlet " + nPortletId );
        }
        ArrayList<String> listErrors = new ArrayList<String>(  );
        Locale locale = AdminUserService.getLocale( request );

        if ( PortletRemovalListenerService.getService(  ).checkForRemoval( strPortletId, listErrors, locale ) )
        {
            portlet.remove(  );
        }

        String strUrl = JSP_ADMIN_SITE + "?" + Parameters.PAGE_ID + "=" + portlet.getPageId(  );
        return strUrl;
    }

    /**
     * Displays the portlet status modification page
     *
     * @param request The http request
     * @return The confirm page
     * @throws AccessDeniedException if the user is not authorized to manage the portlet
     */
    public String getModifyPortletStatus( HttpServletRequest request ) throws AccessDeniedException
    {
        String strPortletId = request.getParameter( Parameters.PORTLET_ID );
        String strStatus = request.getParameter( PORTLET_STATUS );
        if ( !StringUtils.isNumeric( strPortletId ) || !StringUtils.isNumeric( strStatus ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_ERROR);
        }
        int nPortletId = Integer.parseInt( strPortletId );
        Portlet portlet = null;
        try
        {
            portlet = PortletHome.findByPrimaryKey( nPortletId );
        } catch (NullPointerException e)
        {
            AppLogService.error( "Error looking for portlet with id " + nPortletId, e );
        }
        if ( portlet == null || portlet.getId( ) != nPortletId )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MESSAGE_INVALID_ENTRY, new Object[] { nPortletId }, AdminMessage.TYPE_ERROR);
        }
        int nStatus = Integer.parseInt( strStatus );
        if ( nStatus != Portlet.STATUS_PUBLISHED && nStatus != Portlet.STATUS_UNPUBLISHED )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MESSAGE_INVALID_ENTRY, new Object[] { nStatus }, AdminMessage.TYPE_ERROR);
        }
        AdminUser user = AdminUserService.getAdminUser( request );
        if ( !RBACService.isAuthorized( PortletType.RESOURCE_TYPE, portlet.getPortletTypeId(  ),
                PortletResourceIdService.PERMISSION_MANAGE, user ) )
        {
            throw new AccessDeniedException( "User " + user + " is not authorized to permission " + PortletResourceIdService.PERMISSION_MANAGE
                    + " on portlet " + nPortletId );
        }
        String strUrl = JSP_DO_MODIFY_STATUS + "?portlet_id=" + strPortletId + "&status=" + strStatus;
        String strTarget = "_top";

        return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_CONFIRM_MODIFY_STATUS, strUrl, strTarget,
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Processes the status definition for portlet : suspended or activated
     *
     * @param request The http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException if the user is not authorized to manage the portlet
     */
    public String doModifyPortletStatus( HttpServletRequest request ) throws AccessDeniedException
    {
        String strPortletId = request.getParameter( Parameters.PORTLET_ID );
        String strStatus = request.getParameter( PORTLET_STATUS );
        if ( !StringUtils.isNumeric( strPortletId ) || !StringUtils.isNumeric( strStatus ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_ERROR);
        }
        int nPortletId = Integer.parseInt( strPortletId );
        Portlet portlet = null;
        try
        {
            portlet = PortletHome.findByPrimaryKey( nPortletId );
        } catch (NullPointerException e)
        {
            AppLogService.error( "Error looking for portlet with id " + nPortletId, e );
        }
        if ( portlet == null || portlet.getId( ) != nPortletId )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MESSAGE_INVALID_ENTRY, new Object[] { nPortletId }, AdminMessage.TYPE_ERROR);
        }
        int nStatus = Integer.parseInt( strStatus );
        if ( nStatus != Portlet.STATUS_PUBLISHED && nStatus != Portlet.STATUS_UNPUBLISHED )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MESSAGE_INVALID_ENTRY, new Object[] { nStatus }, AdminMessage.TYPE_ERROR);
        }
        AdminUser user = AdminUserService.getAdminUser( request );
        if ( !RBACService.isAuthorized( PortletType.RESOURCE_TYPE, portlet.getPortletTypeId(  ),
                PortletResourceIdService.PERMISSION_MANAGE, user ) )
        {
            throw new AccessDeniedException( "User " + user + " is not authorized to permission " + PortletResourceIdService.PERMISSION_MANAGE
                    + " on portlet " + nPortletId );
        }

        PortletHome.updateStatus( portlet, nStatus );

        return JSP_ADMIN_SITE + "?" + Parameters.PAGE_ID + "=" + portlet.getPageId(  );
    }
}
