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
package fr.paris.lutece.portal.web.search;

import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.search.SearchParameterHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.search.SearchResourceIdService;
import fr.paris.lutece.portal.service.search.SearchService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.ReferenceItem;

import org.apache.commons.lang.StringUtils;


import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage app search features ( manage filters )
 */
public class SearchJspBean extends AdminFeaturesPageJspBean
{
    /** Unique name for the right to manage search parameters */
    public static final String RIGHT_SEARCH_MANAGEMENT = "CORE_SEARCH_MANAGEMENT";

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -2095709285081142039L;

    // //////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String EMPTY_STRING = "";

    // Jsp url
    private static final String JSP_MANAGE_SEARCH = "../AdminTechnicalMenu.jsp?#search";

    // Parameters
    private static final String PARAMETER_CANCEL = "cancel";
    private static final String PARAMETER_DATE_FILTER = "date_filter";
    private static final String PARAMETER_DEFAULT_OPERATOR = "default_operator";
    private static final String PARAMETER_HELP_MESSAGE = "help_message";
    private static final String PARAMETER_TAG_FILTER = "tag_filter";
    private static final String PARAMETER_TYPE_FILTER = "type_filter";


    // Template
    private static final String TEMPLATE_ADMIN_DASHBOARD = "admin/search/search_admindashboard.html";


    /**
     * Processes the data capture form of advanced parameters
     * 
     * @param request
     *            the HTTP request
     * @return the jsp URL of the process result
     * @throws AccessDeniedException
     *             if permission to manage advanced parameters on search has not been granted to the user
     */
    public String doModifyAdvancedParameters( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !RBACService.isAuthorized( SearchService.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, SearchResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS,
                getUser( ) ) )
        {
            throw new AccessDeniedException( "User " + getUser( ) + " is not authorized to permission "
                    + SearchResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS );
        }
        if ( !SecurityTokenService.getInstance( ).validate( request, TEMPLATE_ADMIN_DASHBOARD ) )
        {
            throw new AccessDeniedException( "Invalid security token" );
        }

        if ( request.getParameter( PARAMETER_CANCEL ) == null )
        {
            String strTypeFilter = request.getParameter( PARAMETER_TYPE_FILTER );
            String strDefaultOperator = request.getParameter( PARAMETER_DEFAULT_OPERATOR );
            String strHelpMessage = request.getParameter( PARAMETER_HELP_MESSAGE );
            String strDateFilter = request.getParameter( PARAMETER_DATE_FILTER );
            String strTagFilter = request.getParameter( PARAMETER_TAG_FILTER );

            // mandatory field
            if ( StringUtils.isBlank( strTypeFilter ) || StringUtils.isBlank( strDefaultOperator ) || StringUtils.isBlank( strDateFilter )
                    || StringUtils.isBlank( strTagFilter ) )
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }

            ReferenceItem param = new ReferenceItem( );
            param.setCode( PARAMETER_TYPE_FILTER );
            param.setName( strTypeFilter );
            SearchParameterHome.update( param );

            param = new ReferenceItem( );
            param.setCode( PARAMETER_DEFAULT_OPERATOR );
            param.setName( strDefaultOperator );
            SearchParameterHome.update( param );

            param = new ReferenceItem( );
            param.setCode( PARAMETER_HELP_MESSAGE );
            param.setName( StringUtils.isNotBlank( strHelpMessage ) ? strHelpMessage : EMPTY_STRING );
            SearchParameterHome.update( param );

            param = new ReferenceItem( );
            param.setCode( PARAMETER_DATE_FILTER );
            param.setName( strDateFilter );
            SearchParameterHome.update( param );

            param = new ReferenceItem( );
            param.setCode( PARAMETER_TAG_FILTER );
            param.setName( strTagFilter );
            SearchParameterHome.update( param );
        }

        return JSP_MANAGE_SEARCH;
    }
}
