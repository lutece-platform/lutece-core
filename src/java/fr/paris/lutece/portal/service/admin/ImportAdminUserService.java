/*
 * Copyright (c) 2002-2016, Mairie de Paris
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

import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.service.csv.CSVMessageDescriptor;
import fr.paris.lutece.portal.service.csv.CSVMessageLevel;
import fr.paris.lutece.portal.service.csv.CSVReaderService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Class to import Admin Users from CSV files.
 */
public abstract class ImportAdminUserService extends CSVReaderService
{
    private static final String MESSAGE_ACCESS_CODE_ALREADY_USED = "portal.users.message.user.accessCodeAlreadyUsed";
    private static final String MESSAGE_EMAIL_ALREADY_USED = "portal.users.message.user.accessEmailUsed";
    private static final String MESSAGE_ERROR_MIN_NUMBER_COLUMNS = "portal.users.import_users_from_file.messageErrorMinColumnNumber";
    private static final String PROPERTY_IMPORT_EXPORT_USER_SEPARATOR = "lutece.importExportUser.defaultSeparator";
    private static final String CONSTANT_DEFAULT_IMPORT_EXPORT_USER_SEPARATOR = ":";
    private static final String MESSAGE_USERS_IMPORTED = "portal.users.import_users_from_file.usersImported";
    
    private Character _strAttributesSeparator;
    private boolean _bUpdateExistingUsers;



    /**
     * {@inheritDoc}
     */
    @Override
    protected List<CSVMessageDescriptor> checkLineOfCSVFile( String [ ] strLineDataArray, int nLineNumber, Locale locale )
    {
        List<CSVMessageDescriptor> listMessages = new ArrayList<CSVMessageDescriptor>( );
        
        int nbMinColumns = getNbMinColumns( );
        if ( ( strLineDataArray == null ) || ( strLineDataArray.length < nbMinColumns ) )
        {
            int nNbCol;

            if ( strLineDataArray == null )
            {
                nNbCol = 0;
            }
            else
            {
                nNbCol = strLineDataArray.length;
            }

            Object [ ] args = {
                    nNbCol, nbMinColumns
            };
            String strErrorMessage = I18nService.getLocalizedString( MESSAGE_ERROR_MIN_NUMBER_COLUMNS, args, locale );
            CSVMessageDescriptor error = new CSVMessageDescriptor( CSVMessageLevel.ERROR, nLineNumber, strErrorMessage );
            listMessages.add( error );

            return listMessages;
        }

        if ( !getUpdateExistingUsers( ) )
        {
            String strAccessCode = getAccessCode( strLineDataArray );
            String strEmail = getEmail( strLineDataArray );

            if ( AdminUserHome.checkAccessCodeAlreadyInUse( strAccessCode ) > 0 )
            {
                String strMessage = I18nService.getLocalizedString( MESSAGE_ACCESS_CODE_ALREADY_USED, locale );
                CSVMessageDescriptor error = new CSVMessageDescriptor( CSVMessageLevel.ERROR, nLineNumber, strMessage );
                listMessages.add( error );
            }
            else
            {
                if ( AdminUserHome.checkEmailAlreadyInUse( strEmail ) > 0 )
                {
                    String strMessage = I18nService.getLocalizedString( MESSAGE_EMAIL_ALREADY_USED, locale );
                    CSVMessageDescriptor error = new CSVMessageDescriptor( CSVMessageLevel.ERROR, nLineNumber, strMessage );
                    listMessages.add( error );
                }
            }
        }

        return listMessages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<CSVMessageDescriptor> getEndOfProcessMessages( int nNbLineParses, int nNbLinesWithoutErrors, Locale locale )
    {
        List<CSVMessageDescriptor> listMessages = new ArrayList<CSVMessageDescriptor>( );
        Object [ ] args = {
                nNbLineParses, nNbLinesWithoutErrors
        };
        String strMessageContent = I18nService.getLocalizedString( MESSAGE_USERS_IMPORTED, args, locale );
        CSVMessageDescriptor message = new CSVMessageDescriptor( CSVMessageLevel.INFO, 0, strMessageContent );
        listMessages.add( message );

        return listMessages;
    }
    
    /**
     * Get the separator used for attributes of admin users.
     * 
     * @return The separator
     */
    public Character getAttributesSeparator( )
    {
        if ( _strAttributesSeparator == null )
        {
            _strAttributesSeparator = AppPropertiesService.getProperty( PROPERTY_IMPORT_EXPORT_USER_SEPARATOR, CONSTANT_DEFAULT_IMPORT_EXPORT_USER_SEPARATOR )
                    .charAt( 0 );
        }

        return _strAttributesSeparator;
    }

    /**
     * Get the update users flag
     * 
     * @return True if existing users should be updated, false if they should be ignored.
     */
    public boolean getUpdateExistingUsers( )
    {
        return _bUpdateExistingUsers;
    }

    /**
     * Set the update users flag
     * 
     * @param bUpdateExistingUsers
     *            True if existing users should be updated, false if they should be ignored.
     */
    public void setUpdateExistingUsers( boolean bUpdateExistingUsers )
    {
        _bUpdateExistingUsers = bUpdateExistingUsers;
    }
    
    /**
     * Get the HTML template for importing users from file 
     * 
     * @return the template path
     */
    public abstract String getImportFromFileTemplate( );
    
    /**
     * Get the number min of CSV columns
     * 
     * @return the min number of columns
     */
    public abstract int getNbMinColumns( );
    
    /**
     * Get the access code from data array
     * 
     * @param strLineDataArray the data array
     * @return the access code
     */
    public abstract String getAccessCode( String [ ] strLineDataArray );
    
    /**
     * Get the email from CSV columns
     * 
     * @param strLineDataArray the data array
     * @return the email
     */
    public abstract String getEmail( String [ ] strLineDataArray );

    /**
     * {@inheritDoc}
     */
    //Reexport this method for visibility in this package
    @Override
    protected abstract List<CSVMessageDescriptor> readLineOfCSVFile( String [ ] strLineDataArray, int nLineNumber, Locale locale, String strBaseUrl );

}
