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
package fr.paris.lutece.portal.service.download;

import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.business.file.FileHome;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFileHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.security.RsaService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.url.UrlItem;

/**
 * Abstract implementation for {@link IFileDownloadProvider}
 */
public abstract class AbstractFileDownloadProvider implements IFileDownloadProvider
{
    private static final String SEPARATOR = "/";
    private static final String MESSAGE_LINK_EXPIRED = "portal.file.download.link.expired";
    private static final String MESSAGE_ACCESS_DENIED = "portal.file.download.access.denied";
    private static final String PARAM_DATA = "data";
    public static final String PARAM_PROVIDER = "provider";
    private static final String UNAUTHORIZED = "Unauthorized";

    @Override
    public final String getDownloadUrl( FileDownloadData fileDownloadData, boolean urlBo )
    {
        try
        {
            StringBuilder urlSb = new StringBuilder( );

            if ( urlBo )
            {
                urlSb.append( AppPathService.getBaseUrl( null ) );
                urlSb.append( "jsp/admin/" );
            }
            else
            {
                urlSb.append( AppPathService.getProdUrl( "" ) );
                urlSb.append( "jsp/site/" );
            }
            urlSb.append( "file/download" );

            UrlItem item = new UrlItem( urlSb.toString( ) );
            String toEncrypt = getDataToEncrypt( fileDownloadData );
            String idEncrytped = RsaService.encryptRsa( toEncrypt );
            item.addParameter( PARAM_PROVIDER, getProviderName( ) );
            item.addParameter( PARAM_DATA, idEncrytped );
            return item.getUrlWithEntity( );
        }
        catch( GeneralSecurityException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
        return null;
    }

    @Override
    public final File getFile( User user, HttpServletRequest request, boolean bo ) throws SiteMessageException, UserNotSignedException
    {
        File file = null;
        try
        {
            String data = request.getParameter( PARAM_DATA );
            String decrypted = RsaService.decryptRsa( data );
            FileDownloadData fileDownloadData = getDecryptedData( decrypted );
            checkLinkValidity( user, bo, fileDownloadData );

            file = FileHome.findByPrimaryKey( fileDownloadData.getIdFile( ) );

            if ( file != null && file.getPhysicalFile( ) != null )
            {
                file.setPhysicalFile( PhysicalFileHome.findByPrimaryKey( file.getPhysicalFile( ).getIdPhysicalFile( ) ) );
            }
        }
        catch( GeneralSecurityException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
        catch( ExpiredLinkException e )
        {
            AppLogService.error( e.getMessage( ), e );
            SiteMessageService.setMessage( request, MESSAGE_LINK_EXPIRED );
        }
        catch( AccessDeniedException e )
        {
            SiteMessageService.setMessage( request, MESSAGE_ACCESS_DENIED );
        }
        return file;
    }

    /**
     * Find provider by name
     * 
     * @param providerName
     * @return
     */
    public static IFileDownloadProvider findProvider( String providerName )
    {
        if ( StringUtils.isEmpty( providerName ) )
        {
            return null;
        }
        for ( IFileDownloadProvider p : SpringContextService.getBeansOfType( IFileDownloadProvider.class ) )
        {
            if ( providerName.equals( p.getProviderName( ) ) )
            {
                return p;
            }
        }
        return null;
    }

    private String getDataToEncrypt( FileDownloadData fileDownloadData )
    {
        StringBuilder sb = new StringBuilder( );
        sb.append( fileDownloadData.getIdFile( ) ).append( SEPARATOR );
        sb.append( fileDownloadData.getIdResource( ) ).append( SEPARATOR );
        sb.append( fileDownloadData.getResourceType( ) ).append( SEPARATOR );
        sb.append( calculateEndValidity( ) );
        return sb.toString( );
    }

    private FileDownloadData getDecryptedData( String data )
    {
        String [ ] dataArray = data.split( SEPARATOR );
        FileDownloadData fileDownloadData = new FileDownloadData( Integer.parseInt( dataArray [1] ), dataArray [2], Integer.parseInt( dataArray [0] ) );
        fileDownloadData.setEndValidity( new Timestamp( Long.parseLong( dataArray [3] ) ).toLocalDateTime( ) );

        return fileDownloadData;
    }

    private void checkLinkValidity( User user, boolean bo, FileDownloadData fileDownloadData )
            throws ExpiredLinkException, AccessDeniedException, UserNotSignedException
    {
        if ( LocalDateTime.now( ).isAfter( fileDownloadData.getEndValidity( ) ) )
        {
            throw new ExpiredLinkException( "Link expired on : " + fileDownloadData.getEndValidity( ).toString( ) );
        }
        if ( bo && user == null )
        {
            throw new AccessDeniedException( UNAUTHORIZED );
        }
        checkUserDownloadRight( user, bo, fileDownloadData );
    }

    private long calculateEndValidity( )
    {
        LocalDateTime endValidity = LocalDateTime.MAX;
        if ( getLinkValidityTime( ) > 0 )
        {
            endValidity = LocalDateTime.now( ).plusMinutes( getLinkValidityTime( ) );
        }
        return Timestamp.valueOf( endValidity ).getTime( );
    }

    protected abstract void checkUserDownloadRight( User user, boolean bo, FileDownloadData fileDownloadData )
            throws AccessDeniedException, UserNotSignedException;
}
