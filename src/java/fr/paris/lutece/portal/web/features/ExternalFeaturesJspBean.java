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
import fr.paris.lutece.portal.business.right.Level;
import fr.paris.lutece.portal.business.right.LevelHome;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.right.RightHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author closea
 */
public class ExternalFeaturesJspBean extends AdminFeaturesPageJspBean
{

    // Properties
    private static final String PROPERTY_CREATE_EXTERNAL_FEATURE_PAGETITLE = "portal.features.create_external_feature.pageTitle";
    private static final String PROPERTY_MODIFY_EXTERNAL_FEATURE_PAGETITLE = "portal.features.modify_external_feature.pageTitle";

    // Templates
    private static final String TEMPLATE_CREATE_EXTERNAL_FEATURE = "admin/features/create_external_feature.html";
    private static final String TEMPLATE_MODIFY_EXTERNAL_FEATURE = "admin/features/modify_external_feature.html";

    // Messages
    private static final String MESSAGE_CONFIRM_DELETE = "portal.features.delete_external_feature.confirmDeleteExternalFeature";

    // Parameters
    private static final String PARAMETER_ID_FEATURE_GROUP = "feature_group_id";
    private static final String PARAMETER_ID_EXTERNAL_FEATURE = "external_feature_id";
    private static final String PARAMETER_ID_LEVEL = "level_id";

    // JSP
    private static final String JSP_DELETE_EXTERNAL_FEATURE = "jsp/admin/features/DoRemoveExternalFeature.jsp";

    // Rights
    public static final String RIGHT_EXTERNAL_FEATURES_MANAGEMENT = "CORE_FEATURES_MANAGEMENT";

    // Markers
    private static final String MARK_EXTERNAL_FEATURE = "external_feature";
    private static final String MARK_FEATURES_GROUPS_REFERENCE_LIST = "features_groups_labels_list";
    private static final String MARK_RIGHT_LEVELS_REFERENCE_LIST = "right_levels_labels_list";
    
    private static final String ANCHOR_ADMIN_DASHBOARDS = "features_management";

    private Right _externalFeature;

    /**
     * Returns the list of rights
     *
     * @param request
     *            The Http request
     * @return the html code for display the rights list
     */
//    public String getManageExternalFeatures( HttpServletRequest request )
//    {
//        setPageTitleProperty( PROPERTY_MANAGE_EXTERNAL_FEATURES_PAGETITLE );
//
//        Map<String, Object> model = new HashMap<>( );
//        model.put( MARK_EXTERNAL_FEATURES_LIST, RightHome.getExternalRightList( ) );
//
//        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_EXTERNAL_FEATURES, getLocale( ), model );
//
//        return getAdminPage( template.getHtml( ) );
//    }

    public String getCreateExternalFeature( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_CREATE_EXTERNAL_FEATURE_PAGETITLE );

        Map<String, Object> model = new HashMap<>( );

        Collection<FeatureGroup> featureGroups = FeatureGroupHome.getFeatureGroupsList( );
        ReferenceList featureGroupsReferenceList = new ReferenceList( );
        for ( FeatureGroup featureGroup : featureGroups )
        {
            featureGroup.setLocale( getUser( ).getLocale( ) );
            featureGroupsReferenceList.add( featureGroup.getReferenceItem( ) );
        }
        model.put( MARK_FEATURES_GROUPS_REFERENCE_LIST, featureGroupsReferenceList );

        Collection<Level> rightLevels = LevelHome.getLevelsList( );
        ReferenceList rightLevelsReferenceList = new ReferenceList( );
        for ( Level rightLevel : rightLevels )
        {
            rightLevelsReferenceList.add( rightLevel.getReferenceItem( ) );
        }
        model.put( MARK_RIGHT_LEVELS_REFERENCE_LIST, rightLevelsReferenceList );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_CREATE_EXTERNAL_FEATURE ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_EXTERNAL_FEATURE, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    public String doCreateExternalFeature( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, TEMPLATE_CREATE_EXTERNAL_FEATURE ) )
        {
            throw new AccessDeniedException( "Invalid security token" );
        }
        _externalFeature = new Right( );
        populate( _externalFeature, request );
        _externalFeature.setFeatureGroup( FeatureGroupHome.findByPrimaryKey( request.getParameter( PARAMETER_ID_FEATURE_GROUP ) ).getId( ) );
        _externalFeature.setExternalFeature( true );
        _externalFeature.setLevel( Integer.parseInt( request.getParameter( PARAMETER_ID_LEVEL ) ) );

        RightHome.create( _externalFeature );
        return getAdminDashboardsUrl( request , ANCHOR_ADMIN_DASHBOARDS );
    }

    public String getRemoveExternalFeature( HttpServletRequest request )
    {

        String strExternalFeatureId = request.getParameter( PARAMETER_ID_EXTERNAL_FEATURE );

        _externalFeature = RightHome.findByPrimaryKey( strExternalFeatureId );
        _externalFeature.setLocale( getUser( ).getLocale( ) );

        Object [ ] messageArgs = {
            _externalFeature.getName( )
        };

        Map<String, Object> parameters = new HashMap<>( );
        parameters.put( PARAMETER_ID_EXTERNAL_FEATURE, strExternalFeatureId );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, JSP_DELETE_EXTERNAL_FEATURE ) );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE, messageArgs, null, JSP_DELETE_EXTERNAL_FEATURE, "",
                AdminMessage.TYPE_CONFIRMATION, parameters );
    }

    public String doRemoveExternalFeature( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, JSP_DELETE_EXTERNAL_FEATURE ) )
        {
            throw new AccessDeniedException( "Invalid security token" );
        }
        RightHome.remove( _externalFeature.getId( ) );

        return getAdminDashboardsUrl( request , ANCHOR_ADMIN_DASHBOARDS );
    }

    public String getModifyExternalFeature( HttpServletRequest request )
    {

        setPageTitleProperty( PROPERTY_MODIFY_EXTERNAL_FEATURE_PAGETITLE );
        Map<String, Object> model = new HashMap<>( );

        String strExternalFeatureId = request.getParameter( PARAMETER_ID_EXTERNAL_FEATURE );
        _externalFeature = RightHome.findByPrimaryKey( strExternalFeatureId );
        model.put( MARK_EXTERNAL_FEATURE, _externalFeature );

        Collection<FeatureGroup> featureGroups = FeatureGroupHome.getFeatureGroupsList( );
        ReferenceList featureGroupsReferenceList = new ReferenceList( );
        for ( FeatureGroup featureGroup : featureGroups )
        {
            featureGroup.setLocale( getUser( ).getLocale( ) );
            featureGroupsReferenceList.add( featureGroup.getReferenceItem( ) );
        }
        model.put( MARK_FEATURES_GROUPS_REFERENCE_LIST, featureGroupsReferenceList );

        Collection<Level> rightLevels = LevelHome.getLevelsList( );
        ReferenceList rightLevelsReferenceList = new ReferenceList( );
        for ( Level rightLevel : rightLevels )
        {
            rightLevelsReferenceList.add( rightLevel.getReferenceItem( ) );
        }
        model.put( MARK_RIGHT_LEVELS_REFERENCE_LIST, rightLevelsReferenceList );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_MODIFY_EXTERNAL_FEATURE ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_EXTERNAL_FEATURE, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    public String doModifyExternalFeature( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, TEMPLATE_MODIFY_EXTERNAL_FEATURE ) )
        {
            throw new AccessDeniedException( "Invalid security token" );
        }
        int nIdOrder = _externalFeature.getOrder( );
        String strIdExternalFeature = _externalFeature.getId( );

        _externalFeature = new Right( );
        populate( _externalFeature, request );

        _externalFeature.setId( strIdExternalFeature );
        _externalFeature.setFeatureGroup( FeatureGroupHome.findByPrimaryKey( request.getParameter( PARAMETER_ID_FEATURE_GROUP ) ).getId( ) );
        _externalFeature.setExternalFeature( true );
        _externalFeature.setOrder( nIdOrder );
        _externalFeature.setLevel( Integer.parseInt( request.getParameter( PARAMETER_ID_LEVEL ) ) );

        RightHome.update( _externalFeature );

        // update this right for user if he or she already have it
        AdminUser user = AdminUserService.getAdminUser( request );
        if ( user.checkRight( _externalFeature.getId( ) ) )
        {
            user.updateRight( _externalFeature );
        }

        return getAdminDashboardsUrl( request , ANCHOR_ADMIN_DASHBOARDS );
    }
}
