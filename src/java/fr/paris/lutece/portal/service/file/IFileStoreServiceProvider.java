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
package fr.paris.lutece.portal.service.file;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.security.UserNotSignedException;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;

/**
 * File Store Service Provider Interface. InputStream methods should be used for very large blobs
 */
public interface IFileStoreServiceProvider extends Serializable
{

    /**
     * get the service name
     * 
     * @return the service name
     */
    public String getName( );

    /**
     * get the default
     * 
     * @return true if default
     */
    public boolean isDefault( );

    /**
     * Stores a file Lutece File
     * 
     * @param file
     *            the file
     * @return The key of the stored file
     */
    String storeFile( File file );

    /**
     * Stores a org.apache.commons.fileupload.FileItem
     * 
     * @param fileItem
     *            the fileItem
     * @return The key of the blob
     */
    String storeFileItem( FileItem fileItem );

    /**
     * Stores an input stream
     * 
     * @param inputStream
     *            the input stream
     * @return The key of the blob
     */
    String storeInputStream( InputStream inputStream );

    /**
     * Store a blob from a bytes array
     * 
     * @param blob
     *            The blob
     * @return The key of the blob
     */
    String storeBytes( byte [ ] blob );

    /**
     * Get a file
     * 
     * @param strKey
     *            The key of the file
     * @return The file
     */
    File getFile( String strKey );

    /**
     * Get a file object only filled with the meta data (name, size ...) 
     * without the physical file content
     * 
     * @param strKey
     *            The key of the file
     * @return The file
     */
    File getFileMetaData( String strKey );

    /**
     * Gets a blob as {@link InputStream}
     * 
     * @param strKey
     *            the key
     * @return the {@link InputStream}
     */
    InputStream getInputStream( String strKey );

    /**
     * Delete a blob
     * 
     * @param strKey
     *            The key of the blob
     */
    void delete( String strKey );

    /**
     * Gets the file download url for Front Office Lutece users
     * 
     * @param strKey
     *            the
     * @return the download link
     */
    String getFileDownloadUrlFO( String strKey );

    /**
     * Gets the file download url for Front Office Lutece users
     * 
     * @param strKey
     *            the
     * @param additionnalData
     * @return the download link
     */
    String getFileDownloadUrlFO( String strKey, Map<String, String> additionnalData );

    /**
     * Gets the file download url for Back Office admin Lutece users
     * 
     * @param strKey
     *            the
     * @return the download link
     */
    String getFileDownloadUrlBO( String strKey );

    /**
     * Gets the file download url for Back Office admin Lutece users
     * 
     * @param strKey
     *            the
     * @param additionnalData
     * @return the download link
     */
    String getFileDownloadUrlBO( String strKey, Map<String, String> additionnalData );

    /**
     * get requested file from BO
     * 
     * @param request
     * @return the file
     * @throws fr.paris.lutece.portal.service.admin.AccessDeniedException
     * @throws fr.paris.lutece.portal.service.file.ExpiredLinkException
     * @throws fr.paris.lutece.portal.service.security.UserNotSignedException
     */
    File getFileFromRequestBO( HttpServletRequest request ) throws AccessDeniedException, ExpiredLinkException, UserNotSignedException;

    /**
     * get requested file from FO
     * 
     * @param request
     * @return the file
     * @throws fr.paris.lutece.portal.service.admin.AccessDeniedException
     * @throws fr.paris.lutece.portal.service.file.ExpiredLinkException
     * @throws fr.paris.lutece.portal.service.security.UserNotSignedException
     */
    File getFileFromRequestFO( HttpServletRequest request ) throws AccessDeniedException, ExpiredLinkException, UserNotSignedException;

    /**
     * check if current user can access the file
     * 
     * @param fileData
     * @param user
     *            the current user
     * @throws fr.paris.lutece.portal.service.admin.AccessDeniedException
     * @throws fr.paris.lutece.portal.service.security.UserNotSignedException
     */
    void checkAccessRights( Map<String, String> fileData, User user ) throws AccessDeniedException, UserNotSignedException;

    /**
     * check if link is valid
     * 
     * @param fileData
     * @throws ExpiredLinkException
     */
    void checkLinkValidity( Map<String, String> fileData ) throws ExpiredLinkException;
}
