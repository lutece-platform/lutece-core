/*
 * Copyright (c) 2002-2021, City of Paris
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
package fr.paris.lutece.portal.web.user.attribute;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.portal.business.user.attribute.IAttribute;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.user.attribute.AttributeService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.dashboard.AdminDashboardJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 *
 * AttributeJspBean
 *
 */
public class AttributeJspBean extends AdminFeaturesPageJspBean
{
    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = 183073111112521149L;

    // CONSTANTS
    private static final String QUESTION_MARK = "?";
    private static final String EQUAL = "=";

    // PARAMETERS
    private static final String PARAMETER_ATTRIBUTE_TYPE_CLASS_NAME = "attribute_type_class_name";
    private static final String PARAMETER_CANCEL = "cancel";
    private static final String PARAMETER_APPLY = "apply";
    private static final String PARAMETER_ID_ATTRIBUTE = "id_attribute";

    // MARKS
    private static final String MARK_ATTRIBUTE_TYPE = "attribute_type";
    private static final String MARK_ATTRIBUTE = "attribute";
    private static final String MARK_ATTRIBUTE_FIELDS_LIST = "attribute_fields_list";

    // PROPERTIES
    private static final String PROPERTY_MESSAGE_CONFIRM_REMOVE_ATTRIBUTE = "portal.users.manage_attributes.message.confirmRemoveAttribute";

    // JSP
    private static final String JSP_URL_REMOVE_ATTRIBUTE = "jsp/admin/user/attribute/DoRemoveAttribute.jsp";
    private static final String ANCHOR_ADMIN_DASHBOARDS = "attributes_management";
    private static final String JSP_MODIFY_ATTRIBUTE = "ModifyAttribute.jsp";
    private static final AttributeService _attributeService = AttributeService.getInstance( );

    /**
     * Get user attribute creation interface
     * 
     * @param request
     *            HttpServletRequest
     * @return the Html form
     */
    public String getCreateAttribute( HttpServletRequest request )
    {
        String strAttributeTypeClassName = request.getParameter( PARAMETER_ATTRIBUTE_TYPE_CLASS_NAME );

        IAttribute attribute = null;

        try
        {
            attribute = (IAttribute) Class.forName( strAttributeTypeClassName ).newInstance( );
        }
        catch( IllegalAccessException | InstantiationException | ClassNotFoundException e )
        {
            AppLogService.error( e );
        }

        if ( attribute == null )
        {
            return getAdminDashboardsUrl( request, ANCHOR_ADMIN_DASHBOARDS );
        }

        setPageTitleProperty( attribute.getPropertyCreatePageTitle( ) );

        attribute.setAttributeType( getLocale( ) );

        HtmlTemplate template;
        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_ATTRIBUTE_TYPE, attribute.getAttributeType( ) );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, attribute.getTemplateCreateAttribute( ) ) );

        template = AppTemplateService.getTemplate( attribute.getTemplateCreateAttribute( ), getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Create an user attribute
     * 
     * @param request
     *            HttpServletRequest
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doCreateAttribute( HttpServletRequest request ) throws AccessDeniedException
    {
        String strAttributeTypeClassName = request.getParameter( PARAMETER_ATTRIBUTE_TYPE_CLASS_NAME );
        String strActionCancel = request.getParameter( PARAMETER_CANCEL );
        String strActionApply = request.getParameter( PARAMETER_APPLY );

        if ( StringUtils.isEmpty( strActionCancel ) )
        {
            IAttribute attribute = null;

            try
            {
                attribute = (IAttribute) Class.forName( strAttributeTypeClassName ).newInstance( );
            }
            catch( IllegalAccessException | InstantiationException | ClassNotFoundException e )
            {
                AppLogService.error( e );
            }

            if ( attribute == null )
            {
                getAdminDashboardsUrl( request, ANCHOR_ADMIN_DASHBOARDS );
            }
            else
            {
                String strError = attribute.setAttributeData( request );

                if ( StringUtils.isNotBlank( strError ) )
                {
                    return strError;
                }
                if ( !SecurityTokenService.getInstance( ).validate( request, attribute.getTemplateCreateAttribute( ) ) )
                {
                    throw new AccessDeniedException( ERROR_INVALID_TOKEN );
                }
                _attributeService.createAttribute( attribute );

                if ( strActionApply != null )
                {
                    return JSP_MODIFY_ATTRIBUTE + QUESTION_MARK + PARAMETER_ID_ATTRIBUTE + EQUAL + attribute.getIdAttribute( );
                }
            }
        }

        return getAdminDashboardsUrl( request, ANCHOR_ADMIN_DASHBOARDS );
    }

    /**
     * Get the user attribute modification interface
     * 
     * @param request
     *            HttpServletRequest
     * @return the html form
     */
    public String getModifyAttribute( HttpServletRequest request )
    {
        String strIdAttribute = request.getParameter( PARAMETER_ID_ATTRIBUTE );

        if ( StringUtils.isNotBlank( strIdAttribute ) && StringUtils.isNumeric( strIdAttribute ) )
        {
            // Check if the ID attribute is correct
            int nIdAttribute = Integer.parseInt( strIdAttribute );

            IAttribute attribute = _attributeService.getAttributeWithFields( nIdAttribute, getLocale( ) );

            setPageTitleProperty( attribute.getPropertyModifyPageTitle( ) );

            HtmlTemplate template;
            Map<String, Object> model = new HashMap<>( );
            model.put( MARK_ATTRIBUTE, attribute );
            model.put( MARK_ATTRIBUTE_FIELDS_LIST, attribute.getListAttributeFields( ) );
            model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, attribute.getTemplateModifyAttribute( ) ) );

            template = AppTemplateService.getTemplate( attribute.getTemplateModifyAttribute( ), getLocale( ), model );

            return getAdminPage( template.getHtml( ) );
        }

        // Otherwise, we redirect the user to the attribute management interface
        return getAdminDashboardsUrl( request, ANCHOR_ADMIN_DASHBOARDS );
    }

    /**
     * Modify the attribute
     * 
     * @param request
     *            HttpServletRequest
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doModifyAttribute( HttpServletRequest request ) throws AccessDeniedException
    {
        String strIdAttribute = request.getParameter( PARAMETER_ID_ATTRIBUTE );
        int nIdAttribute = Integer.parseInt( strIdAttribute );
        String strActionCancel = request.getParameter( PARAMETER_CANCEL );
        String strActionApply = request.getParameter( PARAMETER_APPLY );

        if ( StringUtils.isEmpty( strActionCancel ) )
        {
            IAttribute attribute = _attributeService.getAttributeWithFields( nIdAttribute, getLocale( ) );

            if ( attribute != null )
            {
                String strError = attribute.setAttributeData( request );

                if ( strError != null )
                {
                    return strError;
                }
                if ( !SecurityTokenService.getInstance( ).validate( request, attribute.getTemplateModifyAttribute( ) ) )
                {
                    throw new AccessDeniedException( ERROR_INVALID_TOKEN );
                }

                _attributeService.updateAttribute( attribute );

                if ( strActionApply != null )
                {
                    return JSP_MODIFY_ATTRIBUTE + QUESTION_MARK + PARAMETER_ID_ATTRIBUTE + EQUAL + attribute.getIdAttribute( );
                }
            }
        }

        return getAdminDashboardsUrl( request, ANCHOR_ADMIN_DASHBOARDS );
    }

    /**
     * Get the confirmation to remove an user attribute
     * 
     * @param request
     *            HttpServletRequest
     * @return The Jsp URL of the confirmation window
     */
    public String doConfirmRemoveAttribute( HttpServletRequest request )
    {
        String strIdAttribute = request.getParameter( PARAMETER_ID_ATTRIBUTE );

        Map<String, String> parameters = new HashMap<>( );
        parameters.put( PARAMETER_ID_ATTRIBUTE, strIdAttribute );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, JSP_URL_REMOVE_ATTRIBUTE ) );

        return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_CONFIRM_REMOVE_ATTRIBUTE, JSP_URL_REMOVE_ATTRIBUTE, AdminMessage.TYPE_CONFIRMATION,
                parameters );
    }

    /**
     * Remove an user attribute
     * 
     * @param request
     *            HttpServletRequest
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doRemoveAttribute( HttpServletRequest request ) throws AccessDeniedException
    {
        String strIdAttribute = request.getParameter( PARAMETER_ID_ATTRIBUTE );

        if ( StringUtils.isNotBlank( strIdAttribute ) && StringUtils.isNumeric( strIdAttribute ) )
        {
            if ( !SecurityTokenService.getInstance( ).validate( request, JSP_URL_REMOVE_ATTRIBUTE ) )
            {
                throw new AccessDeniedException( ERROR_INVALID_TOKEN );
            }
            int nIdAttribute = Integer.parseInt( strIdAttribute );
            _attributeService.removeAttribute( nIdAttribute );
        }

        return getAdminDashboardsUrl( request, ANCHOR_ADMIN_DASHBOARDS );
    }

    /**
     * Move up the position of the attribute field
     * 
     * @param request
     *            HttpServletRequest
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doMoveUpAttribute( HttpServletRequest request ) throws AccessDeniedException
    {
        String strIdAttribute = request.getParameter( PARAMETER_ID_ATTRIBUTE );

        if ( StringUtils.isNotBlank( strIdAttribute ) && StringUtils.isNumeric( strIdAttribute ) )
        {
            if ( !SecurityTokenService.getInstance( ).validate( request, AdminDashboardJspBean.TEMPLATE_MANAGE_DASHBOARDS ) )
            {
                throw new AccessDeniedException( ERROR_INVALID_TOKEN );
            }
            int nIdAttribute = Integer.parseInt( strIdAttribute );

            List<IAttribute> listAttributes = _attributeService.getAllAttributesWithoutFields( getLocale( ) );
            IAttribute previousAttribute;
            IAttribute currentAttribute;

            Iterator<IAttribute> it = listAttributes.iterator( );
            previousAttribute = it.next( );
            currentAttribute = it.next( );

            while ( it.hasNext( ) && ( currentAttribute.getIdAttribute( ) != nIdAttribute ) )
            {
                previousAttribute = currentAttribute;
                currentAttribute = it.next( );
            }

            int previousAttributePosition = previousAttribute.getPosition( );
            int currentAttributePosition = currentAttribute.getPosition( );
            previousAttribute.setPosition( currentAttributePosition );
            currentAttribute.setPosition( previousAttributePosition );

            _attributeService.updateAttribute( previousAttribute );
            _attributeService.updateAttribute( currentAttribute );
        }

        return getAdminDashboardsUrl( request, ANCHOR_ADMIN_DASHBOARDS );
    }

    /**
     * Move down the position of the attribute field
     * 
     * @param request
     *            HttpServletRequest
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doMoveDownAttribute( HttpServletRequest request ) throws AccessDeniedException
    {
        String strIdAttribute = request.getParameter( PARAMETER_ID_ATTRIBUTE );

        if ( StringUtils.isNotBlank( strIdAttribute ) && StringUtils.isNumeric( strIdAttribute ) )
        {
            if ( !SecurityTokenService.getInstance( ).validate( request, AdminDashboardJspBean.TEMPLATE_MANAGE_DASHBOARDS ) )
            {
                throw new AccessDeniedException( ERROR_INVALID_TOKEN );
            }
            int nIdAttribute = Integer.parseInt( strIdAttribute );

            List<IAttribute> listAttributes = _attributeService.getAllAttributesWithoutFields( getLocale( ) );
            IAttribute nextAttribute = null;
            IAttribute currentAttribute = null;

            Iterator<IAttribute> it = listAttributes.iterator( );
            currentAttribute = it.next( );
            nextAttribute = it.next( );

            while ( it.hasNext( ) && ( currentAttribute.getIdAttribute( ) != nIdAttribute ) )
            {
                currentAttribute = nextAttribute;
                nextAttribute = it.next( );
            }

            int nextAttributePosition = nextAttribute.getPosition( );
            int currentAttributePosition = currentAttribute.getPosition( );
            nextAttribute.setPosition( currentAttributePosition );
            currentAttribute.setPosition( nextAttributePosition );

            _attributeService.updateAttribute( nextAttribute );
            _attributeService.updateAttribute( currentAttribute );
        }

        return getAdminDashboardsUrl( request, ANCHOR_ADMIN_DASHBOARDS );
    }
}
