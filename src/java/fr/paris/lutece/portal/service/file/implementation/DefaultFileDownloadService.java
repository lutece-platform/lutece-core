/*
 * Copyright (c) 2002-2022, City of Paris
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
package fr.paris.lutece.portal.service.file.implementation;

import fr.paris.lutece.portal.service.file.ExpiredLinkException;
import fr.paris.lutece.portal.service.file.FileService;
import static fr.paris.lutece.portal.service.file.FileService.PARAMETER_VALIDITY_TIME;
import fr.paris.lutece.portal.service.file.IFileDownloadUrlService;
import fr.paris.lutece.portal.service.security.RsaService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.url.UrlItem;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * DatabaseBlobStoreService.
 * 
 */
public class DefaultFileDownloadService implements IFileDownloadUrlService
{
    private static final long serialVersionUID = 1L;

    // constants
    protected static final String URL_FO = "jsp/site/file/download";
    protected static final String URL_BO = "jsp/admin/file/download";
    private static final String SERVICE_NAME = "DefaultFileDownloadService";
    private static final String SEPARATOR = "/";

    // Keys
    public static final String KEY_LINK_VALIDITY_TIME = "link_validity_time";

    /**
     * Build the additionnel data map to provide encryption data
     * 
     * @param strFileId
     * @param strResourceId
     * @param strResourceType
     * @return the map
     */
    public static Map<String, String> buildAdditionnalDatas( String strFileId, String strResourceId, String strResourceType )
    {
        Map<String, String> map = new HashMap<>( );

        map.put( FileService.PARAMETER_FILE_ID, strFileId );
        map.put( FileService.PARAMETER_RESOURCE_ID, strResourceId );
        map.put( FileService.PARAMETER_RESOURCE_TYPE, strResourceType );

        return map;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileDownloadUrlFO( String strFileKey, String strFileStorageServiceProviderName )
    {

        return getFileDownloadUrlFO( strFileKey, null, strFileStorageServiceProviderName );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileDownloadUrlFO( String strFileKey, Map<String, String> additionnalData, String strFileStorageServiceProviderName )
    {
        StringBuilder sbUrl = new StringBuilder( );

        sbUrl.append( AppPathService.getBaseUrl( null ) );
        sbUrl.append( URL_FO );

        if ( additionnalData == null )
        {
            additionnalData = new HashMap<>( );
        }
        additionnalData.put( FileService.PARAMETER_FILE_ID, strFileKey );

        return getEncryptedUrl( sbUrl.toString( ), getDataToEncrypt( additionnalData ), strFileStorageServiceProviderName );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileDownloadUrlBO( String strFileKey, String strFileStorageServiceProviderName )
    {
        return getFileDownloadUrlBO( strFileKey, null, strFileStorageServiceProviderName );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileDownloadUrlBO( String strFileKey, Map<String, String> additionnalData, String strFileStorageServiceProviderName )
    {

        StringBuilder sbUrl = new StringBuilder( );

        sbUrl.append( AppPathService.getBaseUrl( null ) );
        sbUrl.append( URL_BO );

        if ( additionnalData == null )
        {
            additionnalData = new HashMap<>( );
        }
        additionnalData.put( FileService.PARAMETER_FILE_ID, strFileKey );

        return getEncryptedUrl( sbUrl.toString( ), getDataToEncrypt( additionnalData ), strFileStorageServiceProviderName );
    }

    /**
     * get encrypted url
     * 
     * @param strUrl
     * @param additionnalData
     * 
     * @return the url, null otherwise
     */
    protected String getEncryptedUrl( String strUrl, String dataToEncrypt, String strFileStorageServiceProviderName )
    {
        UrlItem item = new UrlItem( strUrl );

        try
        {
            String idEncrytped = RsaService.encryptRsa( dataToEncrypt );

            item.addParameter( FileService.PARAMETER_PROVIDER, strFileStorageServiceProviderName );
            item.addParameter( FileService.PARAMETER_DATA, idEncrytped );

            return item.getUrlWithEntity( );
        }
        catch( GeneralSecurityException e )
        {
            AppLogService.error( e.getMessage( ), e );
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName( )
    {
        return SERVICE_NAME;
    }

    /**
     * get data to encrypt
     * 
     * @param fileDownloadData
     * @return the map of datas to encrypt in the url
     */
    private String getDataToEncrypt( Map<String, String> additionnalData )
    {
        StringBuilder sb = new StringBuilder( );
        sb.append( StringUtils.defaultIfEmpty( additionnalData.get( FileService.PARAMETER_FILE_ID ), "" ) ).append( SEPARATOR );
        sb.append( StringUtils.defaultIfEmpty( additionnalData.get( FileService.PARAMETER_RESOURCE_ID ), "" ) ).append( SEPARATOR );
        sb.append( StringUtils.defaultIfEmpty( additionnalData.get( FileService.PARAMETER_RESOURCE_TYPE ), "" ) ).append( SEPARATOR );
        sb.append( calculateEndValidity( ) );

        return sb.toString( );
    }

    /**
     * get end validity time
     * 
     * @return the end time of url validity
     */
    protected long calculateEndValidity( )
    {
        LocalDateTime endValidity = LocalDateTime.MAX;
        if ( getValidityTime( ) > 0 )
        {
            endValidity = LocalDateTime.now( ).plusMinutes( LINK_VALIDITY_TIME );
        }
        return Timestamp.valueOf( endValidity ).getTime( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getRequestDataBO( HttpServletRequest request )
    {
        String strEncryptedData = request.getParameter( FileService.PARAMETER_DATA );

        try
        {
            String strDecryptedData = RsaService.decryptRsa( strEncryptedData );
            return getDecryptedData( strDecryptedData );
        }
        catch( GeneralSecurityException e )
        {
            AppLogService.error( e.getMessage( ), e );
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getRequestDataFO( HttpServletRequest request )
    {
        String strEncryptedData = request.getParameter( FileService.PARAMETER_DATA );

        try
        {
            String strDecryptedData = RsaService.decryptRsa( strEncryptedData );
            return getDecryptedData( strDecryptedData );
        }
        catch( GeneralSecurityException e )
        {
            AppLogService.error( e.getMessage( ), e );
            return null;
        }
    }

    /**
     * get map of datas from encrypted url data parameter
     * 
     * @param data
     * @return the map of
     */
    protected Map<String, String> getDecryptedData( String strData )
    {
        String [ ] data = strData.split( SEPARATOR );
        Map<String, String> fileData = buildAdditionnalDatas( data [0], data [1], data [2] );
        fileData.put( PARAMETER_VALIDITY_TIME, data [3] );

        return fileData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkLinkValidity( Map<String, String> fileData ) throws ExpiredLinkException
    {
        LocalDateTime validityTime = new Timestamp( Long.parseLong( fileData.get( FileService.PARAMETER_VALIDITY_TIME ) ) ).toLocalDateTime( );

        if ( LocalDateTime.now( ).isAfter( validityTime ) )
        {
            throw new ExpiredLinkException( "Link expired on : " + validityTime.toString( ) );
        }
    }
}
