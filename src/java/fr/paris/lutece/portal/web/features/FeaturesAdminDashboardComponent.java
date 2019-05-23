/*
 * Copyright (c) 2002-2019, Mairie de Paris
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
import fr.paris.lutece.portal.business.right.RightHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.dashboard.admin.AdminDashboardComponent;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.dashboard.AdminDashboardJspBean;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * FeaturesAdminDashboardComponent
 */
public class FeaturesAdminDashboardComponent extends AdminDashboardComponent
{
    private static final String TEMPLATE_FEATURES_ADMINDASHBOARD = "admin/dashboard/admin/features_admindashboard.html";
    private static final String MARK_FEATURE_NO_GROUP = "no_group";
    private static final String MARK_FEATURE_GROUP_LIST = "feature_group_list";
    private static final String MARK_FEATURE_GROUP = "feature_group";
    private static final String MARK_GROUPS_LIST = "groups_list";
    private static final String MARK_ORDER_LIST = "order_list";
    private static final String MARK_EXTERNAL_FEATURES_LIST = "external_features_list";
    private static final String MARK_RIGHT_LIST = "feature_list";
    private static final String MARK_ORDER_IS_OK = "order_list_state";
    
    private static final String NO_GROUP_DESCRIPTION = "portal.features.nogroup.description";
    private static final String NO_GROUP_LABEL = "portal.features.nogroup.label";
    private static final int NO_GROUP_ORDER = 0;
    private static final String NO_GROUP_ID = null;


    /**
     * {@inheritDoc}
     */
    @Override
    public String getDashboardData( AdminUser user, HttpServletRequest request )
    {
        List<FeatureGroup> listGroups = FeatureGroupHome.getFeatureGroupsList( );
        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_FEATURE_NO_GROUP, getNoGroup( user.getLocale( ) ) );
        model.put( MARK_FEATURE_GROUP_LIST, getRefListFeatureGroups( user.getLocale( ) ) );
        model.put( MARK_ORDER_LIST, getOrderRefList( ) );
        model.put( MARK_GROUPS_LIST, listGroups );
        model.put( MARK_EXTERNAL_FEATURES_LIST, RightHome.getExternalRightList( ) );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, AdminDashboardJspBean.TEMPLATE_MANAGE_DASHBOARDS ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_FEATURES_ADMINDASHBOARD, user.getLocale( ), model );

        return template.getHtml( );

    }

    /**
     * Generate an HTML combo of available group order
     * 
     * @return The reference list of orders
     */
    private ReferenceList getOrderRefList( )
    {
        int nGroupsCount = FeatureGroupHome.getFeatureGroupsCount( );
        ReferenceList listOrders = new ReferenceList( );

        for ( int i = 0; i < nGroupsCount; i++ )
        {
            listOrders.addItem( i + 1, Integer.toString( i + 1 ) );
        }

        return listOrders;
    }
    /**
     * Generate a combo containing all available groups
     *
     * @param locale
     *            The locale
     * @return the reference list of feature groups
     */
    private Map<String, Object> getNoGroup( Locale locale )
    {
        FeatureGroup noGroup = new FeatureGroup( );
        noGroup.setLocale( locale );
        noGroup.setId( NO_GROUP_ID );
        noGroup.setOrder( NO_GROUP_ORDER );
        noGroup.setDescriptionKey( NO_GROUP_DESCRIPTION );
        noGroup.setLabelKey( NO_GROUP_LABEL );

        Map<String, Object> groupMap = new HashMap<String, Object>( );
        groupMap.put( MARK_FEATURE_GROUP, noGroup );
        groupMap.put( MARK_RIGHT_LIST, I18nService.localizeCollection( RightHome.getRightsList( noGroup.getId( ) ), locale ) );
        groupMap.put( MARK_ORDER_IS_OK, RightHome.checkFeatureOrders( noGroup.getId( ) ) );

        return groupMap;
    }

    /**
     * Generate a combo containing all available groups
     *
     * @param locale
     *            The locale
     * @return the reference list of feature groups
     */
    private Collection<HashMap<String, Object>> getRefListFeatureGroups( Locale locale )
    {
        Collection<HashMap<String, Object>> colGroupMap = new ArrayList<HashMap<String, Object>>( );
        Collection<FeatureGroup> colGroups = FeatureGroupHome.getFeatureGroupsList( );

        for ( FeatureGroup fg : colGroups )
        {
            HashMap<String, Object> groupMap = new HashMap<String, Object>( );
            groupMap.put( MARK_FEATURE_GROUP, fg );
            groupMap.put( MARK_RIGHT_LIST, I18nService.localizeCollection( RightHome.getRightsList( fg.getId( ) ), locale ) );
            groupMap.put( MARK_ORDER_IS_OK, RightHome.checkFeatureOrders( fg.getId( ) ) );
            colGroupMap.add( groupMap );
        }

        return colGroupMap;
    }


}
