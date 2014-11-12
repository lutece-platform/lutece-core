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
package fr.paris.lutece.portal.business.user.attribute;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.user.attribute.AttributeFieldService;
import fr.paris.lutece.portal.web.constants.Messages;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * AttributeComboBox
 *
 */
public class AttributeCheckBox extends AbstractAttribute implements ISimpleValuesAttributes
{
    // Constants
    private static final String CONSTANT_UNDERSCORE = "_";

    // Parameters
    private static final String PARAMETER_TITLE = "title";
    private static final String PARAMETER_HELP_MESSAGE = "help_message";
    private static final String PARAMETER_MANDATORY = "mandatory";
    private static final String PARAMETER_IS_SHOWN_IN_SEARCH = "is_shown_in_search";
    private static final String PARAMETER_IS_SHOWN_IN_RESULT_LIST = "is_shown_in_result_list";
    private static final String PARAMETER_ATTRIBUTE = "attribute";
    private static final String PARAMETER_IS_FIELD_IN_LINE = "is_field_in_line";

    // Properties
    private static final String PROPERTY_TYPE_CHECKBOX = "portal.users.attribute.type.checkBox";
    private static final String PROPERTY_CREATE_CHECKBOX_PAGETITLE = "portal.users.create_attribute.pageTitleAttributeCheckBox";
    private static final String PROPERTY_MODIFY_CHECKBOX_PAGETITLE = "portal.users.modify_attribute.pageTitleAttributeCheckBox";

    // Templates
    private static final String TEMPLATE_CREATE_ATTRIBUTE = "admin/user/attribute/checkbox/create_attribute_checkbox.html";
    private static final String TEMPLATE_MODIFY_ATTRIBUTE = "admin/user/attribute/checkbox/modify_attribute_checkbox.html";
    private static final String TEMPLATE_HTML_FORM_ATTRIBUTE = "admin/user/attribute/checkbox/html_code_form_attribute_checkbox.html";
    private static final String TEMPLATE_HTML_FORM_SEARCH_ATTRIBUTE = "admin/user/attribute/checkbox/html_code_form_search_attribute_checkbox.html";
    private static final String TEMPLATE_HTML_VALUE = "admin/user/attribute/checkbox/html_code_value_attribute_checkbox.html";

    /**
     * Constructor
     */
    public AttributeCheckBox(  )
    {
    }

    /**
     * Get the template create an attribute
     * @return The URL of the template
     */
    @Override
    public String getTemplateCreateAttribute(  )
    {
        return TEMPLATE_CREATE_ATTRIBUTE;
    }

    /**
     * Get the template modify an attribute
     * @return The URL of the template
     */
    @Override
    public String getTemplateModifyAttribute(  )
    {
        return TEMPLATE_MODIFY_ATTRIBUTE;
    }

    /**
     * Get the template html form attribute
     * @return the template
     */
    @Override
    public String getTemplateHtmlFormAttribute(  )
    {
        return TEMPLATE_HTML_FORM_ATTRIBUTE;
    }

    /**
     * Get the template html form search attribute
     * @return the template
     */
    @Override
    public String getTemplateHtmlFormSearchAttribute(  )
    {
        return TEMPLATE_HTML_FORM_SEARCH_ATTRIBUTE;
    }

    /**
     * Get the template html for the value of the attribute
     * @return the template
     */
    @Override
    public String getTemplateHtmlValue(  )
    {
        return TEMPLATE_HTML_VALUE;
    }

    /**
     * Get page title for create page
     * @return page title
     */
    @Override
    public String getPropertyCreatePageTitle(  )
    {
        return PROPERTY_CREATE_CHECKBOX_PAGETITLE;
    }

    /**
     * Get page title for modify page
     * @return page title
     */
    @Override
    public String getPropertyModifyPageTitle(  )
    {
        return PROPERTY_MODIFY_CHECKBOX_PAGETITLE;
    }

    /**
     * Set the data of the attribute
     * @param request HttpServletRequest
     * @return null if there are no errors
     */
    @Override
    public String setAttributeData( HttpServletRequest request )
    {
        String strTitle = request.getParameter( PARAMETER_TITLE );
        String strHelpMessage = ( request.getParameter( PARAMETER_HELP_MESSAGE ) != null )
            ? request.getParameter( PARAMETER_HELP_MESSAGE ).trim(  ) : null;
        String strIsShownInSearch = request.getParameter( PARAMETER_IS_SHOWN_IN_SEARCH );
        String strIsShownInResultList = request.getParameter( PARAMETER_IS_SHOWN_IN_RESULT_LIST );
        String strMandatory = request.getParameter( PARAMETER_MANDATORY );
        String strFieldInLine = request.getParameter( PARAMETER_IS_FIELD_IN_LINE );

        if ( StringUtils.isNotBlank( strTitle ) )
        {
            setTitle( strTitle );
            setHelpMessage( strHelpMessage );
            setMandatory( strMandatory != null );
            setShownInSearch( strIsShownInSearch != null );
            setShownInResultList( strIsShownInResultList != null );
            setFieldInLine( strFieldInLine != null );

            return null;
        }

        return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
    }

    /**
     * Set attribute type
     * @param locale locale
     */
    @Override
    public void setAttributeType( Locale locale )
    {
        AttributeType attributeType = new AttributeType(  );
        attributeType.setLocale( locale );
        attributeType.setClassName( this.getClass(  ).getName(  ) );
        attributeType.setLabelType( PROPERTY_TYPE_CHECKBOX );
        setAttributeType( attributeType );
    }

    /**
     * Get the data of the user fields
     * @param request HttpServletRequest
     * @param user user
     * @return user field data
     */
    @Override
    public List<AdminUserField> getUserFieldsData( HttpServletRequest request, AdminUser user )
    {
        String[] strValues = request.getParameterValues( PARAMETER_ATTRIBUTE + CONSTANT_UNDERSCORE +
                getIdAttribute(  ) );

        return getUserFieldsData( strValues, user );
    }

    /**
     * Get the data of the user fields
     * @param strValues Values
     * @param user user
     * @return user field data
     */
    @Override
    public List<AdminUserField> getUserFieldsData( String[] strValues, AdminUser user )
    {
        List<AdminUserField> listUserFields = new ArrayList<AdminUserField>(  );

        if ( strValues != null )
        {
            for ( String strValue : strValues )
            {
                AdminUserField userField = new AdminUserField(  );
                AttributeField attributeField;

                if ( StringUtils.isNotBlank( strValue ) && StringUtils.isNumeric( strValue ) )
                {
                    int nIdField = Integer.parseInt( strValue );
                    attributeField = AttributeFieldService.getInstance(  ).getAttributeField( nIdField );
                }
                else
                {
                    attributeField = new AttributeField(  );
                    attributeField.setAttribute( this );
                    attributeField.setTitle( strValue );
                    attributeField.setValue( strValue );
                }

                userField.setUser( user );
                userField.setAttribute( this );
                userField.setAttributeField( attributeField );
                userField.setValue( attributeField.getTitle(  ) );

                listUserFields.add( userField );
            }
        }

        return listUserFields;
    }

    /**
     * Get whether the attribute is anonymizable.
     * @return True if the attribute can be anonymized, false otherwise.
     */
    @Override
    public boolean isAnonymizable(  )
    {
        return false;
    }
}
