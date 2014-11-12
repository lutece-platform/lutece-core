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
package fr.paris.lutece.portal.service.user.attribute;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.attribute.AdminUserField;
import fr.paris.lutece.portal.business.user.attribute.AdminUserFieldFilter;
import fr.paris.lutece.portal.business.user.attribute.AdminUserFieldHome;
import fr.paris.lutece.portal.business.user.attribute.IAttribute;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * AdminUserFieldService
 *
 */
public final class AdminUserFieldService
{
    // CONSTANTS
    private static final String CONSTANT_EMPTY_STRING = "";
    private static final String CONSTANT_UNDERSCORE = "_";

    // PARAMETERS
    private static final String PARAMETER_ATTRIBUTE = "attribute";
    private static final String PARAMETER_UPDATE_ATTRIBUTE = "update_attribute";
    private static final AttributeService _attributeService = AttributeService.getInstance(  );

    /**
     * Instantiates a new admin user field service.
     */
    private AdminUserFieldService(  )
    {
    }

    /**
     * Check if the user fields are correctly filled
     * @param request HttpServletRequest
     * @param locale locale
     * @return null if there are no problem
     */
    public static String checkUserFields( HttpServletRequest request, Locale locale )
    {
        // Specific attributes
        List<IAttribute> listAttributes = _attributeService.getAllAttributesWithoutFields( locale );

        for ( IAttribute attribute : listAttributes )
        {
            if ( attribute.isAttributeImage(  ) )
            {
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                FileItem fileItem = multipartRequest.getFile( PARAMETER_ATTRIBUTE + CONSTANT_UNDERSCORE +
                        attribute.getIdAttribute(  ) );
                String strUpdateAttribute = request.getParameter( PARAMETER_UPDATE_ATTRIBUTE + CONSTANT_UNDERSCORE +
                        attribute.getIdAttribute(  ) );

                if ( attribute.isMandatory(  ) && ( strUpdateAttribute != null ) &&
                        ( ( fileItem == null ) || ( fileItem.getSize(  ) == 0 ) ) )
                {
                    return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
                }
            }
            else
            {
                String value = request.getParameter( PARAMETER_ATTRIBUTE + CONSTANT_UNDERSCORE +
                        attribute.getIdAttribute(  ) );

                if ( attribute.isMandatory(  ) && ( ( value == null ) || value.equals( CONSTANT_EMPTY_STRING ) ) )
                {
                    return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
                }
            }
        }

        return null;
    }

    /**
     * Create the user fields
     * @param user Adminuser
     * @param request HttpServletRequest
     * @param locale locale
     */
    public static void doCreateUserFields( AdminUser user, HttpServletRequest request, Locale locale )
    {
        // Attributes created in the Back-Office
        List<IAttribute> listAttributes = _attributeService.getCoreAttributesWithoutFields( locale );

        for ( IAttribute attribute : listAttributes )
        {
            List<AdminUserField> listUserFields = attribute.getUserFieldsData( request, user );

            for ( AdminUserField userField : listUserFields )
            {
                if ( userField != null )
                {
                    AdminUserFieldHome.create( userField );
                }
            }
        }

        // Attributes associated to the plugins
        for ( AdminUserFieldListenerService adminUserFieldListenerService : SpringContextService.getBeansOfType( 
                AdminUserFieldListenerService.class ) )
        {
            adminUserFieldListenerService.doCreateUserFields( user, request, locale );
        }
    }

    /**
     * Modify the user fields
     * @param user AdminUser
     * @param request HttpServletRequest
     * @param locale locale
     * @param currentUser current user
     */
    public static void doModifyUserFields( AdminUser user, HttpServletRequest request, Locale locale,
        AdminUser currentUser )
    {
        // Attributes created in the Back-Office
        List<IAttribute> listAttributes = _attributeService.getCoreAttributesWithoutFields( locale );
        Map<Integer, List<AdminUserField>> map = new HashMap<Integer, List<AdminUserField>>(  );

        for ( IAttribute attribute : listAttributes )
        {
            List<AdminUserField> listUserFields = attribute.getUserFieldsData( request, user );

            map.put( attribute.getIdAttribute(  ), listUserFields );
        }

        // Remove all user fields
        AdminUserFieldFilter auFieldFilter = new AdminUserFieldFilter(  );
        auFieldFilter.setIdUser( user.getUserId(  ) );
        AdminUserFieldHome.removeByFilter( auFieldFilter );

        for ( int nIdAttribute : map.keySet(  ) )
        {
            for ( AdminUserField userField : map.get( nIdAttribute ) )
            {
                if ( userField != null )
                {
                    AdminUserFieldHome.create( userField );
                }
            }
        }

        // Attributes associated to the plugins
        for ( AdminUserFieldListenerService adminUserFieldListenerService : SpringContextService.getBeansOfType( 
                AdminUserFieldListenerService.class ) )
        {
            adminUserFieldListenerService.doModifyUserFields( user, request, locale, currentUser );
        }
    }

    /**
     * Remove the user fields
     * @param user Adminuser
     * @param request HttpServletRequest
     * @param locale locale
     */
    public static void doRemoveUserFields( AdminUser user, HttpServletRequest request, Locale locale )
    {
        AdminUserFieldFilter auFieldFilter = new AdminUserFieldFilter(  );
        auFieldFilter.setIdUser( user.getUserId(  ) );
        AdminUserFieldHome.removeByFilter( auFieldFilter );

        // Attributes associated to the plugins
        for ( AdminUserFieldListenerService adminUserFieldListenerService : SpringContextService.getBeansOfType( 
                AdminUserFieldListenerService.class ) )
        {
            adminUserFieldListenerService.doRemoveUserFields( user, request, locale );
        }
    }

    /**
     * Remove the user fields from a given ID attribute
     * @param nIdAttribute the ID attribute
     */
    public static void doRemoveUserFieldsByIdAttribute( int nIdAttribute )
    {
        AdminUserFieldFilter auFieldFilter = new AdminUserFieldFilter(  );
        auFieldFilter.setIdAttribute( nIdAttribute );
        AdminUserFieldHome.removeByFilter( auFieldFilter );
    }

    /**
     * Remove the user fields from a given ID attribute field
     * @param nIdAttributeField the attribute field ID
     */
    public static void doRemoveUserFieldsByIdField( int nIdAttributeField )
    {
        AdminUserFieldHome.removeUserFieldsFromIdField( nIdAttributeField );
    }

    /**
     * Get the user attribute fields
     * @param nUserId the user ID
     * @param locale the {@link Locale}
     * @return a Map of (ID Attribute, Object). The object could be either a File or a list of {@link AdminUserField}
     */
    public static Map<String, Object> getAdminUserFields( int nUserId, Locale locale )
    {
        List<IAttribute> listAttributes = _attributeService.getAllAttributesWithFields( locale );

        return getAdminUserFields( listAttributes, nUserId, locale );
    }

    /**
     * Get the user attribute fields
     * @param listAttributes the list of attributes
     * @param nUserId the user ID
     * @param locale the {@link Locale}
     * @return a Map of (ID Attribute, Object). The object could be either a File or a list of {@link AdminUserField}
     */
    public static Map<String, Object> getAdminUserFields( List<IAttribute> listAttributes, int nUserId, Locale locale )
    {
        Map<String, Object> map = new HashMap<String, Object>(  );

        for ( IAttribute attribute : listAttributes )
        {
            List<AdminUserField> listUserFields = AdminUserFieldHome.selectUserFieldsByIdUserIdAttribute( nUserId,
                    attribute.getIdAttribute(  ) );

            if ( attribute.isAttributeImage(  ) )
            {
                if ( listUserFields.size(  ) > 0 )
                {
                    AdminUserField userField = listUserFields.get( 0 );

                    if ( userField.getFile(  ) != null )
                    {
                        map.put( String.valueOf( attribute.getIdAttribute(  ) ), userField.getFile(  ) );
                    }
                }
            }
            else
            {
                if ( listUserFields.size(  ) == 0 )
                {
                    AdminUserField userField = new AdminUserField(  );
                    userField.setValue( StringUtils.EMPTY );
                    listUserFields.add( userField );
                }

                map.put( String.valueOf( attribute.getIdAttribute(  ) ), listUserFields );
            }
        }

        return map;
    }

    /**
     * Get the admin user field from a given attribute and a given user ID
     * @param attribute a {@link IAttribute}
     * @param nUserId the user ID
     * @param locale the {@link Locale}
     * @return either a File (if the attribute is a type img) or a list of {@link AdminUserField}
     */
    public Object getAdminUserField( IAttribute attribute, int nUserId, Locale locale )
    {
        List<AdminUserField> listUserFields = AdminUserFieldHome.selectUserFieldsByIdUserIdAttribute( nUserId,
                attribute.getIdAttribute(  ) );

        if ( attribute.isAttributeImage(  ) )
        {
            if ( listUserFields.size(  ) > 0 )
            {
                AdminUserField userField = listUserFields.get( 0 );

                if ( userField.getFile(  ) != null )
                {
                    return userField.getFile(  );
                }
            }
        }
        else
        {
            if ( listUserFields.size(  ) == 0 )
            {
                AdminUserField userField = new AdminUserField(  );
                userField.setValue( StringUtils.EMPTY );
                listUserFields.add( userField );
            }

            return listUserFields;
        }

        return null;
    }
}
