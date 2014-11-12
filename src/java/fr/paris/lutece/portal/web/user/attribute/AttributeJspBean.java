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
package fr.paris.lutece.portal.web.user.attribute;

import fr.paris.lutece.portal.business.user.attribute.AttributeType;
import fr.paris.lutece.portal.business.user.attribute.IAttribute;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.user.attribute.AttributeService;
import fr.paris.lutece.portal.service.user.attribute.AttributeTypeService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


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
    private static final String MARK_ATTRIBUTE_TYPES_LIST = "attribute_types_list";
    private static final String MARK_ATTRIBUTE_TYPE = "attribute_type";
    private static final String MARK_ATTRIBUTES_LIST = "attributes_list";
    private static final String MARK_ATTRIBUTE = "attribute";
    private static final String MARK_ATTRIBUTE_FIELDS_LIST = "attribute_fields_list";

    // PROPERTIES
    private static final String PROPERTY_MANAGE_ATTRIBUTES_PAGETITLE = "portal.users.manage_attributes.pageTitle";
    private static final String PROPERTY_MESSAGE_CONFIRM_REMOVE_ATTRIBUTE = "portal.users.manage_attributes.message.confirmRemoveAttribute";

    // TEMPLATES
    private static final String TEMPLATE_MANAGE_ATTRIBUTES = "admin/user/attribute/manage_attributes.html";

    // JSP
    private static final String JSP_URL_REMOVE_ATTRIBUTE = "jsp/admin/user/attribute/DoRemoveAttribute.jsp";
    private static final String JSP_MANAGE_ATTRIBUTES = "ManageAttributes.jsp";
    private static final String JSP_MODIFY_ATTRIBUTE = "ModifyAttribute.jsp";
    private static final AttributeService _attributeService = AttributeService.getInstance(  );
    private static final AttributeTypeService _attributeTypeService = AttributeTypeService.getInstance(  );

    /**
     * Get list of user attributes
     * @param request HttpServletRequest
     * @return list of attributes
     */
    public String getManageAttributes( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_MANAGE_ATTRIBUTES_PAGETITLE );

        List<IAttribute> listAttributes = _attributeService.getAllAttributesWithoutFields( getLocale(  ) );

        // ATTRIBUTE TYPES
        List<AttributeType> listAttributeTypes = _attributeTypeService.getAttributeTypes( getLocale(  ) );

        HtmlTemplate template;
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_ATTRIBUTES_LIST, listAttributes );
        model.put( MARK_ATTRIBUTE_TYPES_LIST, listAttributeTypes );

        template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_ATTRIBUTES, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Get user attribute creation interface
     * @param request HttpServletRequest
     * @return the Html form
     */
    public String getCreateAttribute( HttpServletRequest request )
    {
        String strAttributeTypeClassName = request.getParameter( PARAMETER_ATTRIBUTE_TYPE_CLASS_NAME );

        IAttribute attribute = null;

        try
        {
            attribute = (IAttribute) Class.forName( strAttributeTypeClassName ).newInstance(  );
        }
        catch ( ClassNotFoundException e )
        {
            // class doesn't exist
            AppLogService.error( e );
        }
        catch ( InstantiationException e )
        {
            // Class is abstract or is an interface or haven't accessible
            // builder
            AppLogService.error( e );
        }
        catch ( IllegalAccessException e )
        {
            // can't access to the class
            AppLogService.error( e );
        }

        if ( attribute == null )
        {
            return getManageAttributes( request );
        }

        setPageTitleProperty( attribute.getPropertyCreatePageTitle(  ) );

        attribute.setAttributeType( getLocale(  ) );

        HtmlTemplate template;
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_ATTRIBUTE_TYPE, attribute.getAttributeType(  ) );

        template = AppTemplateService.getTemplate( attribute.getTemplateCreateAttribute(  ), getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Create an user attribute
     * @param request HttpServletRequest
     * @return The Jsp URL of the process result
     */
    public String doCreateAttribute( HttpServletRequest request )
    {
        String strAttributeTypeClassName = request.getParameter( PARAMETER_ATTRIBUTE_TYPE_CLASS_NAME );
        String strActionCancel = request.getParameter( PARAMETER_CANCEL );
        String strActionApply = request.getParameter( PARAMETER_APPLY );

        if ( StringUtils.isEmpty( strActionCancel ) )
        {
            IAttribute attribute = null;

            try
            {
                attribute = (IAttribute) Class.forName( strAttributeTypeClassName ).newInstance(  );
            }
            catch ( ClassNotFoundException e )
            {
                // class doesn't exist
                AppLogService.error( e );
            }
            catch ( InstantiationException e )
            {
                // Class is abstract or is an interface or haven't accessible
                // builder
                AppLogService.error( e );
            }
            catch ( IllegalAccessException e )
            {
                // can't access to the class
                AppLogService.error( e );
            }

            if ( attribute == null )
            {
                return getManageAttributes( request );
            }

            String strError = attribute.setAttributeData( request );

            if ( StringUtils.isNotBlank( strError ) )
            {
                return strError;
            }

            _attributeService.createAttribute( attribute );

            if ( strActionApply != null )
            {
                return JSP_MODIFY_ATTRIBUTE + QUESTION_MARK + PARAMETER_ID_ATTRIBUTE + EQUAL +
                attribute.getIdAttribute(  );
            }
        }

        return JSP_MANAGE_ATTRIBUTES;
    }

    /**
     * Get the user attribute modification interface
     * @param request HttpServletRequest
     * @return the html form
     */
    public String getModifyAttribute( HttpServletRequest request )
    {
        String strIdAttribute = request.getParameter( PARAMETER_ID_ATTRIBUTE );

        if ( StringUtils.isNotBlank( strIdAttribute ) && StringUtils.isNumeric( strIdAttribute ) )
        {
            // Check if the ID attribute is correct
            int nIdAttribute = Integer.parseInt( strIdAttribute );

            IAttribute attribute = _attributeService.getAttributeWithFields( nIdAttribute, getLocale(  ) );

            setPageTitleProperty( attribute.getPropertyModifyPageTitle(  ) );

            HtmlTemplate template;
            Map<String, Object> model = new HashMap<String, Object>(  );
            model.put( MARK_ATTRIBUTE, attribute );
            model.put( MARK_ATTRIBUTE_FIELDS_LIST, attribute.getListAttributeFields(  ) );

            template = AppTemplateService.getTemplate( attribute.getTemplateModifyAttribute(  ), getLocale(  ), model );

            return getAdminPage( template.getHtml(  ) );
        }

        // Otherwise, we redirect the user to the attribute management interface
        return getManageAttributes( request );
    }

    /**
     * Modify the attribute
     * @param request HttpServletRequest
     * @return The Jsp URL of the process result
     */
    public String doModifyAttribute( HttpServletRequest request )
    {
        String strIdAttribute = request.getParameter( PARAMETER_ID_ATTRIBUTE );
        int nIdAttribute = Integer.parseInt( strIdAttribute );
        String strActionCancel = request.getParameter( PARAMETER_CANCEL );
        String strActionApply = request.getParameter( PARAMETER_APPLY );

        if ( StringUtils.isEmpty( strActionCancel ) )
        {
            IAttribute attribute = _attributeService.getAttributeWithFields( nIdAttribute, getLocale(  ) );

            if ( attribute != null )
            {
                String strError = attribute.setAttributeData( request );

                if ( strError != null )
                {
                    return strError;
                }

                _attributeService.updateAttribute( attribute );

                if ( strActionApply != null )
                {
                    return JSP_MODIFY_ATTRIBUTE + QUESTION_MARK + PARAMETER_ID_ATTRIBUTE + EQUAL +
                    attribute.getIdAttribute(  );
                }
            }
        }

        return JSP_MANAGE_ATTRIBUTES;
    }

    /**
     * Get the confirmation to remove an user attribute
     * @param request HttpServletRequest
     * @return The Jsp URL of the confirmation window
     */
    public String doConfirmRemoveAttribute( HttpServletRequest request )
    {
        String strIdAttribute = request.getParameter( PARAMETER_ID_ATTRIBUTE );
        String strUrlRemove = JSP_URL_REMOVE_ATTRIBUTE + QUESTION_MARK + PARAMETER_ID_ATTRIBUTE + EQUAL +
            strIdAttribute;

        String strUrl = AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_CONFIRM_REMOVE_ATTRIBUTE,
                strUrlRemove, AdminMessage.TYPE_CONFIRMATION );

        return strUrl;
    }

    /**
     * Remove an user attribute
     * @param request HttpServletRequest
     * @return The Jsp URL of the process result
     */
    public String doRemoveAttribute( HttpServletRequest request )
    {
        String strIdAttribute = request.getParameter( PARAMETER_ID_ATTRIBUTE );

        if ( StringUtils.isNotBlank( strIdAttribute ) && StringUtils.isNumeric( strIdAttribute ) )
        {
            int nIdAttribute = Integer.parseInt( strIdAttribute );
            _attributeService.removeAttribute( nIdAttribute );
        }

        return JSP_MANAGE_ATTRIBUTES;
    }

    /**
     * Move up the position of the attribute field
     * @param request HttpServletRequest
     * @return The Jsp URL of the process result
     */
    public String doMoveUpAttribute( HttpServletRequest request )
    {
        String strIdAttribute = request.getParameter( PARAMETER_ID_ATTRIBUTE );

        if ( StringUtils.isNotBlank( strIdAttribute ) && StringUtils.isNumeric( strIdAttribute ) )
        {
            int nIdAttribute = Integer.parseInt( strIdAttribute );

            List<IAttribute> listAttributes = _attributeService.getAllAttributesWithoutFields( getLocale(  ) );
            IAttribute previousAttribute = null;
            IAttribute currentAttribute = null;

            Iterator<IAttribute> it = listAttributes.iterator(  );
            previousAttribute = it.next(  );
            currentAttribute = it.next(  );

            while ( it.hasNext(  ) && ( currentAttribute.getIdAttribute(  ) != nIdAttribute ) )
            {
                previousAttribute = currentAttribute;
                currentAttribute = it.next(  );
            }

            int previousAttributePosition = previousAttribute.getPosition(  );
            int currentAttributePosition = currentAttribute.getPosition(  );
            previousAttribute.setPosition( currentAttributePosition );
            currentAttribute.setPosition( previousAttributePosition );

            _attributeService.updateAttribute( previousAttribute );
            _attributeService.updateAttribute( currentAttribute );
        }

        return JSP_MANAGE_ATTRIBUTES;
    }

    /**
     * Move down the position of the attribute field
     * @param request HttpServletRequest
     * @return The Jsp URL of the process result
     */
    public String doMoveDownAttribute( HttpServletRequest request )
    {
        String strIdAttribute = request.getParameter( PARAMETER_ID_ATTRIBUTE );

        if ( StringUtils.isNotBlank( strIdAttribute ) && StringUtils.isNumeric( strIdAttribute ) )
        {
            int nIdAttribute = Integer.parseInt( strIdAttribute );

            List<IAttribute> listAttributes = _attributeService.getAllAttributesWithoutFields( getLocale(  ) );
            IAttribute nextAttribute = null;
            IAttribute currentAttribute = null;

            Iterator<IAttribute> it = listAttributes.iterator(  );
            currentAttribute = it.next(  );
            nextAttribute = it.next(  );

            while ( it.hasNext(  ) && ( currentAttribute.getIdAttribute(  ) != nIdAttribute ) )
            {
                currentAttribute = nextAttribute;
                nextAttribute = it.next(  );
            }

            int nextAttributePosition = nextAttribute.getPosition(  );
            int currentAttributePosition = currentAttribute.getPosition(  );
            nextAttribute.setPosition( currentAttributePosition );
            currentAttribute.setPosition( nextAttributePosition );

            _attributeService.updateAttribute( nextAttribute );
            _attributeService.updateAttribute( currentAttribute );
        }

        return JSP_MANAGE_ATTRIBUTES;
    }
}
