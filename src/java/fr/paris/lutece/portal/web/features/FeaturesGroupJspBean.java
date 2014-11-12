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
package fr.paris.lutece.portal.web.features;

import fr.paris.lutece.portal.business.right.FeatureGroup;
import fr.paris.lutece.portal.business.right.FeatureGroupHome;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.right.RightHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * FeaturesGroupJspBean
 */
public class FeaturesGroupJspBean extends AdminFeaturesPageJspBean
{
    public static final String RIGHT_FEATURES_MANAGEMENT = "CORE_FEATURES_MANAGEMENT";
    private static final long serialVersionUID = -8573499137269541850L;
    private static final String TEMPLATE_MANAGE_FEATURES = "admin/features/manage_features.html";
    private static final String TEMPLATE_MANAGE_GROUPS = "admin/features/manage_groups.html";
    private static final String TEMPLATE_DISPATCH_FEATURES = "admin/features/dispatch_features.html";
    private static final String TEMPLATE_CREATE_GROUP = "admin/features/create_group.html";
    private static final String TEMPLATE_MODIFY_GROUP = "admin/features/modify_group.html";
    private static final String PARAMETER_GROUP_ID = "group_id";
    private static final String PARAMETER_GROUP_NAME = "group_name";
    private static final String PARAMETER_GROUP_DESCRIPTION = "group_description";
    private static final String PARAMETER_GROUP_ORDER = "group_order";
    private static final String PARAMETER_ORDER_ID = "order_id";
    private static final String PARAMETER_RIGHT_ID = "right_id";
    private static final String JSP_DISPATCH_FEATURES = "DispatchFeatures.jsp";
    private static final String JSP_MANAGE_GROUPS = "ManageGroups.jsp";
    private static final String MESSAGE_CONFIRM_DELETE = "portal.features.message.confirmDeleteGroup";
    private static final String MESSAGE_RIGHT_ALREADY_ASSIGN = "portal.features.message.rightAlreadyAssign";
    private static final String MARK_GROUPS_LIST = "groups_list";
    private static final String MARK_RIGHT_LIST = "feature_list";
    private static final String MARK_ORDER_IS_OK = "order_list_state";
    private static final String MARK_FEATURE_NO_GROUP = "no_group";
    private static final String MARK_FEATURE_GROUP_LIST = "feature_group_list";
    private static final String MARK_ORDER_LIST = "order_list";
    private static final String MARK_FEATURE_GROUP = "feature_group";
    private static final String MARK_DEFAULT_ORDER = "order_default";
    private static final String NO_GROUP_DESCRIPTION = "portal.features.nogroup.description";
    private static final String NO_GROUP_LABEL = "portal.features.nogroup.label";
    private static final int NO_GROUP_ORDER = 0;
    private static final String NO_GROUP_ID = null;
    private static final String REGEX_ID = "^[\\d]+$";

    /**
     * Returns the Manage Features page
     * @param request The HTTP request
     * @return The HTML page
     */
    public String getManageFeatures( HttpServletRequest request )
    {
        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MANAGE_FEATURES );

        return getAdminPage( t.getHtml(  ) );
    }

    /**
     * Returns the Manage Groups page
     * @param request The HTTP request
     * @return The HTML page
     */
    public String getManageGroups( HttpServletRequest request )
    {
        List<FeatureGroup> listGroups = FeatureGroupHome.getFeatureGroupsList(  );
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_ORDER_LIST, getOrderRefList(  ) );
        model.put( MARK_GROUPS_LIST, listGroups );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_GROUPS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns the Dispatch Features page
     * @param request The HTTP request
     * @return The HTML page
     */
    public String getDispatchFeatures( HttpServletRequest request )
    {
        Locale locale = getLocale(  );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_FEATURE_NO_GROUP, getNoGroup( locale ) );
        model.put( MARK_FEATURE_GROUP_LIST, getRefListFeatureGroups( locale ) );

        HtmlTemplate tPage = AppTemplateService.getTemplate( TEMPLATE_DISPATCH_FEATURES, locale, model );

        return getAdminPage( tPage.getHtml(  ) );
    }

    /**
     * Generate a combo containing all available groups
     *
     * @param locale The locale
     * @return the reference list of feature groups
     */
    private Map<String, Object> getNoGroup( Locale locale )
    {
        FeatureGroup noGroup = new FeatureGroup(  );
        noGroup.setLocale( locale );
        noGroup.setId( NO_GROUP_ID );
        noGroup.setOrder( NO_GROUP_ORDER );
        noGroup.setDescriptionKey( NO_GROUP_DESCRIPTION );
        noGroup.setLabelKey( NO_GROUP_LABEL );

        Map<String, Object> groupMap = new HashMap<String, Object>(  );
        groupMap.put( MARK_FEATURE_GROUP, noGroup );
        groupMap.put( MARK_RIGHT_LIST,
            I18nService.localizeCollection( RightHome.getRightsList( noGroup.getId(  ) ), locale ) );
        groupMap.put( MARK_ORDER_IS_OK, RightHome.checkFeatureOrders( noGroup.getId(  ) ) );

        return groupMap;
    }

    /**
     * Generate a combo containing all available groups
     *
     * @param locale The locale
     * @return the reference list of feature groups
     */
    private Collection<HashMap<String, Object>> getRefListFeatureGroups( Locale locale )
    {
        Collection<HashMap<String, Object>> colGroupMap = new ArrayList<HashMap<String, Object>>(  );
        Collection<FeatureGroup> colGroups = FeatureGroupHome.getFeatureGroupsList(  );

        for ( FeatureGroup fg : colGroups )
        {
            HashMap<String, Object> groupMap = new HashMap<String, Object>(  );
            groupMap.put( MARK_FEATURE_GROUP, fg );
            groupMap.put( MARK_RIGHT_LIST,
                I18nService.localizeCollection( RightHome.getRightsList( fg.getId(  ) ), locale ) );
            groupMap.put( MARK_ORDER_IS_OK, RightHome.checkFeatureOrders( fg.getId(  ) ) );
            colGroupMap.add( groupMap );
        }

        return colGroupMap;
    }

    /**
     * Dispatch a feature to a given group
     * @param request The HTTP request
     * @return The next URL to redirect after processing
     */
    public String doDispatchFeature( HttpServletRequest request )
    {
        String strRightId = request.getParameter( PARAMETER_RIGHT_ID );
        String strGroupName = request.getParameter( PARAMETER_GROUP_NAME );
        String strOrderId = request.getParameter( PARAMETER_ORDER_ID );
        Right right = RightHome.findByPrimaryKey( strRightId );
        UrlItem url = new UrlItem( JSP_DISPATCH_FEATURES );

        if ( ( strGroupName != null ) )
        {
            //Set the old group as anchor
            url.setAnchor( right.getFeatureGroup(  ) );
            right.setFeatureGroup( strGroupName.equals( "" ) ? null : strGroupName );
        }

        if ( ( strOrderId != null ) && strOrderId.matches( REGEX_ID ) )
        {
            right.setOrder( Integer.parseInt( strOrderId ) );
        }

        RightHome.update( right );

        return url.getUrl(  );
    }

    /**
     * Dispatch a feature group
     *
     * @param request The HTTP request
     * @return The next URL to redirect after processing
     */
    public String doDispatchFeatureGroup( HttpServletRequest request )
    {
        String strGroupId = request.getParameter( PARAMETER_GROUP_ID );
        String strOrderId = request.getParameter( PARAMETER_ORDER_ID );
        FeatureGroup featureGroup = FeatureGroupHome.findByPrimaryKey( strGroupId );
        UrlItem url = new UrlItem( JSP_MANAGE_GROUPS );

        if ( ( strOrderId != null ) && strOrderId.matches( REGEX_ID ) )
        {
            featureGroup.setOrder( Integer.parseInt( strOrderId ) );
        }

        FeatureGroupHome.update( featureGroup );

        return url.getUrl(  );
    }

    /**
     * Reinitialize feature orders
     * @param request The {@link HttpServletRequest}
     * @return The next URL to redirect after processing
     */
    public String doReinitFeatures( HttpServletRequest request )
    {
        String strGroupId = request.getParameter( PARAMETER_GROUP_ID );
        RightHome.reinitFeatureOrders( strGroupId );

        UrlItem url = new UrlItem( JSP_DISPATCH_FEATURES );

        if ( ( strGroupId != null ) )
        {
            url.setAnchor( strGroupId );
        }

        return url.getUrl(  );
    }

    /**
     * Returns the Create Group page
     * @param request The HTTP request
     * @return The HTML page
     */
    public String getCreateGroup( HttpServletRequest request )
    {
        int nCount = FeatureGroupHome.getFeatureGroupsCount(  ) + 1;

        Map<String, Serializable> model = new HashMap<String, Serializable>(  );
        model.put( MARK_ORDER_LIST, getOrderRefList(  ) );
        model.put( MARK_DEFAULT_ORDER, String.valueOf( nCount ) );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_CREATE_GROUP, getLocale(  ), model );

        return getAdminPage( t.getHtml(  ) );
    }

    /**
     * Returns the Modify Group page
     * @param request The HTTP request
     * @return The HTML page
     */
    public String getModifyGroup( HttpServletRequest request )
    {
        String strGroupId = request.getParameter( PARAMETER_GROUP_ID );

        FeatureGroup group = FeatureGroupHome.findByPrimaryKey( strGroupId );

        if ( group == null )
        {
            return getManageGroups( request );
        }

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_ORDER_LIST, getOrderRefList(  ) );
        model.put( MARK_FEATURE_GROUP, group );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MODIFY_GROUP, getLocale(  ), model );

        return getAdminPage( t.getHtml(  ) );
    }

    /**
     * Create the group
     * @param request The HTTP request
     * @return The next URL to redirect after processing
     */
    public String doCreateGroup( HttpServletRequest request )
    {
        String strGroupId = request.getParameter( PARAMETER_GROUP_ID );
        String strGroupName = request.getParameter( PARAMETER_GROUP_NAME );
        String strGroupDescription = request.getParameter( PARAMETER_GROUP_DESCRIPTION );
        String strGroupOrder = request.getParameter( PARAMETER_GROUP_ORDER );

        // Mandatory fields
        if ( strGroupId.equals( "" ) || strGroupName.equals( "" ) || strGroupDescription.equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        FeatureGroup group = new FeatureGroup(  );
        group.setId( strGroupId );
        group.setLabelKey( strGroupName );
        group.setDescriptionKey( strGroupDescription );

        FeatureGroupHome.create( group );
        group.setOrder( Integer.parseInt( strGroupOrder ) );
        FeatureGroupHome.update( group );

        return JSP_MANAGE_GROUPS;
    }

    /**
     * Modify the group
     * @param request The HTTP request
     * @return The next URL to redirect after processing
     */
    public String doModifyGroup( HttpServletRequest request )
    {
        String strGroupId = request.getParameter( PARAMETER_GROUP_ID );
        String strGroupName = request.getParameter( PARAMETER_GROUP_NAME );
        String strGroupDescription = request.getParameter( PARAMETER_GROUP_DESCRIPTION );
        String strGroupOrder = request.getParameter( PARAMETER_GROUP_ORDER );

        // Mandatory fields
        if ( strGroupId.equals( "" ) || strGroupName.equals( "" ) || strGroupDescription.equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        FeatureGroup group = FeatureGroupHome.findByPrimaryKey( strGroupId );
        group.setLabelKey( strGroupName );
        group.setDescriptionKey( strGroupDescription );
        group.setOrder( Integer.parseInt( strGroupOrder ) );
        FeatureGroupHome.update( group );

        return JSP_MANAGE_GROUPS;
    }

    /**
     * Generate an HTML combo of available group order
     * @return The reference list of orders
     */
    private ReferenceList getOrderRefList(  )
    {
        int nGroupsCount = FeatureGroupHome.getFeatureGroupsCount(  );
        ReferenceList listOrders = new ReferenceList(  );

        for ( int i = 0; i < nGroupsCount; i++ )
        {
            listOrders.addItem( i + 1, Integer.toString( i + 1 ) );
        }

        return listOrders;
    }

    /**
     * Returns the Remove page
     * @param request The HTTP request
     * @return The HTML page
     */
    public String getRemoveGroup( HttpServletRequest request )
    {
        String strGroupId = request.getParameter( PARAMETER_GROUP_ID );

        String strUrl = "jsp/admin/features/DoRemoveGroup.jsp?" + PARAMETER_GROUP_ID + "=" + strGroupId;
        FeatureGroup group = FeatureGroupHome.findByPrimaryKey( strGroupId );
        group.setLocale( getUser(  ).getLocale(  ) );

        Object[] messageArgs = { group.getLabel(  ) };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE, messageArgs, null, strUrl, "",
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Remove the group
     * @param request The HTTP request
     * @return The next URL to redirect after processing
     */
    public String doRemoveGroup( HttpServletRequest request )
    {
        String strGroupId = request.getParameter( PARAMETER_GROUP_ID );

        if ( RightHome.getRightsList( strGroupId ).size(  ) > 0 )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_RIGHT_ALREADY_ASSIGN, AdminMessage.TYPE_STOP );
        }

        FeatureGroupHome.remove( strGroupId );

        return JSP_MANAGE_GROUPS;
    }
}
