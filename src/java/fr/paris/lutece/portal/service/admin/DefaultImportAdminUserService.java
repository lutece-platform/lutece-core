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
package fr.paris.lutece.portal.service.admin;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.attribute.AdminUserField;
import fr.paris.lutece.portal.business.user.attribute.AdminUserFieldFilter;
import fr.paris.lutece.portal.business.user.attribute.AdminUserFieldHome;
import fr.paris.lutece.portal.business.user.attribute.IAttribute;
import fr.paris.lutece.portal.business.user.attribute.ISimpleValuesAttributes;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminUser;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroupHome;
import fr.paris.lutece.portal.service.csv.CSVMessageDescriptor;
import fr.paris.lutece.portal.service.csv.CSVMessageLevel;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.user.attribute.AdminUserFieldListenerService;
import fr.paris.lutece.portal.service.user.attribute.AttributeService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;

/**
 * Class to import Admin Users from CSV files.
 */
public class DefaultImportAdminUserService extends ImportAdminUserService
{

    private static final String CONSTANT_RIGHT = "right";
    private static final String CONSTANT_ROLE = "role";
    private static final String CONSTANT_WORKGROUP = "workgroup";
    private static final String PROPERTY_MESSAGE_EMAIL_SUBJECT_NOTIFY_USER = "portal.users.notify_user.email.subject";
    private static final String TEMPLATE_NOTIFY_USER = "admin/user/notify_user_account_created.html";
    private static final String MESSAGE_ERROR_IMPORTING_ATTRIBUTES = "portal.users.import_users_from_file.errorImportingAttributes";
    private static final String MESSAGE_NO_LEVEL = "portal.users.import_users_from_file.importNoLevel";
    private static final String MESSAGE_NO_STATUS = "portal.users.import_users_from_file.importNoStatus";
    private static final int CONSTANT_MINIMUM_COLUMNS_PER_LINE = 12;

    // Template
    private static final String TEMPLATE_DEFAULT_IMPORT_USERS_FROM_FILE = "admin/user/import_users_from_file.html";
    private static final AttributeService _attributeService = AttributeService.getInstance( );

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<CSVMessageDescriptor> readLineOfCSVFile( String [ ] strLineDataArray, int nLineNumber, Locale locale, String strBaseUrl )
    {
        List<CSVMessageDescriptor> listMessages = new ArrayList<>( );
        int nIndex = 0;

        String strAccessCode = strLineDataArray [nIndex++];
        String strLastName = strLineDataArray [nIndex++];
        String strFirstName = strLineDataArray [nIndex++];
        String strEmail = strLineDataArray [nIndex++];

        boolean bUpdateUser = getUpdateExistingUsers( );
        int nUserId = 0;

        if ( bUpdateUser )
        {
            int nAccessCodeUserId = AdminUserHome.checkAccessCodeAlreadyInUse( strAccessCode );
            int nEmailUserId = AdminUserHome.checkEmailAlreadyInUse( strEmail );

            if ( nAccessCodeUserId > 0 )
            {
                nUserId = nAccessCodeUserId;
            }
            else if ( nEmailUserId > 0 )
            {
                nUserId = nEmailUserId;
            }
            bUpdateUser = nUserId > 0;
        }

        String strStatus = strLineDataArray [nIndex++];
        int nStatus = getStatus( strStatus, strLastName, strFirstName, nLineNumber, listMessages, locale );

        String strLocale = strLineDataArray [nIndex++];
        String strLevelUser = strLineDataArray [nIndex++];
        int nLevelUser = getLevel( strLevelUser, strLastName, strFirstName, nLineNumber, listMessages, locale );

        nIndex++;

        String strAccessibilityMode = strLineDataArray [nIndex++];
        boolean bAccessibilityMode = Boolean.parseBoolean( strAccessibilityMode );
        nIndex++;
        nIndex++;

        AdminUser user = null;

        if ( bUpdateUser )
        {
            user = AdminUserHome.findByPrimaryKey( nUserId );
        }
        else
        {
            user = new LuteceDefaultAdminUser( );
        }

        user.setAccessCode( strAccessCode );
        user.setLastName( strLastName );
        user.setFirstName( strFirstName );
        user.setEmail( strEmail );
        user.setStatus( nStatus );
        user.setUserLevel( nLevelUser );
        user.setLocale( new Locale( strLocale ) );
        user.setAccessibilityMode( bAccessibilityMode );
        
        String strDateLastLogin = strLineDataArray [nIndex++];
        user = saveOrUpdateUser( user, bUpdateUser, nUserId, strDateLastLogin, strBaseUrl );

        // We remove any previous right, roles, workgroup and attributes of the user
        AdminUserHome.removeAllRightsForUser( user.getUserId( ) );
        AdminUserHome.removeAllRolesForUser( user.getUserId( ) );

        AdminUserFieldFilter auFieldFilter = new AdminUserFieldFilter( );
        auFieldFilter.setIdUser( user.getUserId( ) );
        AdminUserFieldHome.removeByFilter( auFieldFilter );

        // We get every attribute, role, right and workgroup of the user
        Map<Integer, List<String>> mapAttributesValues = new HashMap<>( );
        List<String> listAdminRights = new ArrayList<>( );
        List<String> listAdminRoles = new ArrayList<>( );
        List<String> listAdminWorkgroups = new ArrayList<>( );

        while ( nIndex < strLineDataArray.length )
        {
            String strValue = strLineDataArray [nIndex];
            readAttribute( strValue, listAdminRights, listAdminRoles, listAdminWorkgroups, mapAttributesValues );
            nIndex++;
        }

        // We create rights
        for ( String strRight : listAdminRights )
        {
            AdminUserHome.createRightForUser( user.getUserId( ), strRight );
        }

        // We create roles
        for ( String strRole : listAdminRoles )
        {
            AdminUserHome.createRoleForUser( user.getUserId( ), strRole );
        }

        // We create workgroups
        for ( String strWorkgoup : listAdminWorkgroups )
        {
            AdminWorkgroupHome.addUserForWorkgroup( user, strWorkgoup );
        }

        List<IAttribute> listAttributes = _attributeService.getAllAttributesWithoutFields( locale );

        // We save the attributes found
        saveAttributes( listAttributes, user, nLineNumber, mapAttributesValues, listMessages, locale );

        return listMessages;
    }
    
    private int getLevel( String strLevelUser, String strLastName, String strFirstName, int nLineNumber, List<CSVMessageDescriptor> listMessages, Locale locale )
    {
        int nLevelUser = 3;

        if ( StringUtils.isNotEmpty( strLevelUser ) && StringUtils.isNumeric( strLevelUser ) )
        {
            nLevelUser = Integer.parseInt( strLevelUser );
        }
        else
        {
            Object [ ] args = {
                    strLastName, strFirstName, nLevelUser
            };
            String strMessage = I18nService.getLocalizedString( MESSAGE_NO_LEVEL, args, locale );
            CSVMessageDescriptor message = new CSVMessageDescriptor( CSVMessageLevel.INFO, nLineNumber, strMessage );
            listMessages.add( message );
        }
        return nLevelUser;
    }
    
    private int getStatus( String strStatus, String strLastName, String strFirstName, int nLineNumber, List<CSVMessageDescriptor> listMessages, Locale locale )
    {
        int nStatus = 0;

        if ( StringUtils.isNotEmpty( strStatus ) && StringUtils.isNumeric( strStatus ) )
        {
            nStatus = Integer.parseInt( strStatus );
        }
        else
        {
            Object [ ] args = {
                    strLastName, strFirstName, nStatus
            };
            String strMessage = I18nService.getLocalizedString( MESSAGE_NO_STATUS, args, locale );
            CSVMessageDescriptor message = new CSVMessageDescriptor( CSVMessageLevel.INFO, nLineNumber, strMessage );
            listMessages.add( message );
        }
        return nStatus;
    }
    
    private void readAttribute( String strValue, List<String> listAdminRights, List<String> listAdminRoles, List<String> listAdminWorkgroups,  Map<Integer, List<String>> mapAttributesValues )
    {
        int nSeparatorIndex = strValue.indexOf( getAttributesSeparator( ) );
        if ( StringUtils.isNotBlank( strValue ) && nSeparatorIndex >= 0 )
        {
            String strLineId = strValue.substring( 0, nSeparatorIndex );

            if ( StringUtils.isNotBlank( strLineId ) )
            {
                if ( StringUtils.equalsIgnoreCase( strLineId, CONSTANT_RIGHT ) )
                {
                    listAdminRights.add( strValue.substring( nSeparatorIndex + 1 ) );
                }
                else if ( StringUtils.equalsIgnoreCase( strLineId, CONSTANT_ROLE ) )
                {
                    listAdminRoles.add( strValue.substring( nSeparatorIndex + 1 ) );
                }
                else if ( StringUtils.equalsIgnoreCase( strLineId, CONSTANT_WORKGROUP ) )
                {
                    listAdminWorkgroups.add( strValue.substring( nSeparatorIndex + 1 ) );
                }
                else
                {
                    int nAttributeId = Integer.parseInt( strLineId );

                    String strAttributeValue = strValue.substring( nSeparatorIndex + 1 );
                    List<String> listValues = mapAttributesValues.get( nAttributeId );

                    if ( listValues == null )
                    {
                        listValues = new ArrayList<>( );
                    }

                    listValues.add( strAttributeValue );
                    mapAttributesValues.put( nAttributeId, listValues );
                }
            }
        }
    }
    
    private void saveAttributes( List<IAttribute> listAttributes, AdminUser user, int nLineNumber, Map<Integer, List<String>> mapAttributesValues, List<CSVMessageDescriptor> listMessages, Locale locale )
    {
        listAttributes = listAttributes.stream( )
                .filter( a -> a instanceof ISimpleValuesAttributes )
                .collect( Collectors.toList( ) );
        for ( IAttribute attribute : listAttributes )
        {
            List<String> listValues = mapAttributesValues.get( attribute.getIdAttribute( ) );

            if ( CollectionUtils.isEmpty( listValues ) )
            {
                continue;
            }

            int nIdField = 0;
            for ( String strValue : listValues )
            {
                int nSeparatorIndex = strValue.indexOf( getAttributesSeparator( ) );

                if ( nSeparatorIndex >= 0 )
                {
                    nIdField = 0;

                    try
                    {
                        nIdField = Integer.parseInt( strValue.substring( 0, nSeparatorIndex ) );
                    }
                    catch ( NumberFormatException e )
                    {
                        nIdField = 0;
                    }

                    strValue = strValue.substring( nSeparatorIndex + 1 );
                }
                else
                {
                    nIdField = 0;
                }

                createFields( attribute, user, strValue, nIdField, nLineNumber, listMessages, locale );
            }
        }
    }
    
    private void createFields( IAttribute attribute, AdminUser user, String strValue, int nIdField, int nLineNumber, List<CSVMessageDescriptor> listMessages, Locale locale )
    {
        Plugin pluginCore = PluginService.getCore( );
        boolean bCoreAttribute = ( attribute.getPlugin( ) == null )
                || StringUtils.equals( pluginCore.getName( ), attribute.getPlugin( ).getName( ) );
        try
        {
            List<AdminUserField> listUserFields = ( (ISimpleValuesAttributes) attribute )
                    .getUserFieldsData( new String[] {strValue}, user );

            for ( AdminUserField userField : listUserFields )
            {
                if ( userField != null )
                {
                    userField.getAttributeField( ).setIdField( nIdField );
                    AdminUserFieldHome.create( userField );
                }
            }

            if ( !bCoreAttribute )
            {
                for ( AdminUserFieldListenerService adminUserFieldListenerService : SpringContextService
                        .getBeansOfType( AdminUserFieldListenerService.class ) )
                {
                    adminUserFieldListenerService.doCreateUserFields( user, listUserFields, locale );
                }
            }
        }
        catch ( Exception e )
        {
            AppLogService.error( e.getMessage( ), e );

            String strErrorMessage = I18nService.getLocalizedString( MESSAGE_ERROR_IMPORTING_ATTRIBUTES,
                    locale );
            CSVMessageDescriptor error = new CSVMessageDescriptor( CSVMessageLevel.ERROR, nLineNumber,
                    strErrorMessage );
            listMessages.add( error );
        }
    }
    
    private AdminUser saveOrUpdateUser( AdminUser user, boolean bUpdateUser, int nUserId, String strDateLastLogin, String strBaseUrl )
    {
        if ( bUpdateUser )
        {
            user.setUserId( nUserId );
            // We update the user
            AdminUserHome.update( user );
        }
        else
        {
            Timestamp dateLastLogin = getDateLastLogin( strDateLastLogin );
            // We create the user
            user.setPasswordReset( true );
            user.setPasswordMaxValidDate( null );
            user.setAccountMaxValidDate( AdminUserService.getAccountMaxValidDate( ) );
            user.setDateLastLogin( dateLastLogin );

            if ( AdminAuthenticationService.getInstance( ).isDefaultModuleUsed( ) )
            {
                LuteceDefaultAdminUser defaultAdminUser = (LuteceDefaultAdminUser) user;
                String strPassword = AdminUserService.makePassword( );
                defaultAdminUser.setPassword( AdminUserService.encryptPassword( strPassword ) );
                AdminUserHome.create( defaultAdminUser );
                AdminUserService.notifyUser( AppPathService.getProdUrl( strBaseUrl ), user, strPassword, PROPERTY_MESSAGE_EMAIL_SUBJECT_NOTIFY_USER,
                        TEMPLATE_NOTIFY_USER );
            }
            else
            {
                AdminUserHome.create( user );
            }
        }
        return user;
    }
    
    private Timestamp getDateLastLogin( String strDateLastLogin )
    {
        Timestamp dateLastLogin = AdminUser.getDefaultDateLastLogin( );

        if ( StringUtils.isNotBlank( strDateLastLogin ) )
        {
            DateFormat dateFormat = new SimpleDateFormat( );
            Date dateParsed;

            try
            {
                dateParsed = dateFormat.parse( strDateLastLogin );
            }
            catch( ParseException e )
            {
                AppLogService.error( e.getMessage( ), e );
                dateParsed = null;
            }

            if ( dateParsed != null )
            {
                dateLastLogin = new Timestamp( dateParsed.getTime( ) );
            }
        }
        return dateLastLogin;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getImportFromFileTemplate( )
    {
        return TEMPLATE_DEFAULT_IMPORT_USERS_FROM_FILE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNbMinColumns( )
    {
        return CONSTANT_MINIMUM_COLUMNS_PER_LINE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAccessCode( String [ ] strLineDataArray )
    {
        return strLineDataArray [0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEmail( String [ ] strLineDataArray )
    {
        return strLineDataArray [3];
    }
}
