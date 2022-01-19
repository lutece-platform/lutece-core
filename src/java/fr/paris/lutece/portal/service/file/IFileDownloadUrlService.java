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

import java.io.Serializable;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 *
 * Builds download Url. Useful when we want to share a http link, ftp link, or fylesystem link.
 * 
 * @see JSPBlobStoreDownloadUrlService
 */
public interface IFileDownloadUrlService extends Serializable
{
    static final int LINK_VALIDITY_TIME = AppPropertiesService.getPropertyInt( "lutece.file.download.validity", 0 );

    /**
     * get Front Office user download URL
     * 
     * @param strFileKey
     *            the file key
     * @return the url
     */
    String getFileDownloadUrlFO( String strFileKey, String strFileStorageServiceProviderName );

    /**
     * get Front Office user download URL
     * 
     * @param strFileKey
     *            the file key
     * @param additionnalData
     *            the data used to build the url
     * @return the url
     */
    String getFileDownloadUrlFO( String strFileKey, Map<String, String> additionnalData, String strFileStorageServiceProviderName );

    /**
     * get Back Office user download URL
     * 
     * @param strFileKey
     *            the file key
     * @return the url
     */
    String getFileDownloadUrlBO( String strFileKey, String strFileStorageServiceProviderName );

    /**
     * get Back Office user download URL
     * 
     * @param strFileKey
     *            the file key
     * @param additionnalData
     *            the data used to build the url
     * @return the url
     */
    String getFileDownloadUrlBO( String strFileKey, Map<String, String> additionnalData, String strFileStorageServiceProviderName );

    /**
     * get service name
     * 
     * @return the service name
     */
    String getName( );

    /**
     * get BO file data from request
     * 
     * @param request
     * @return the map of file data
     */
    Map<String, String> getRequestDataBO( HttpServletRequest request );

    /**
     * get FO file data from request
     * 
     * @param request
     * @return the map of file data
     */
    Map<String, String> getRequestDataFO( HttpServletRequest request );

    /**
     * check link validity
     * 
     * @param fileData
     * @throws ExpiredLinkException
     *             if link is invadid
     */
    void checkLinkValidity( Map<String, String> fileData ) throws ExpiredLinkException;

    /**
     * Return the validity duration of a link (in minutes) <br />
     * if equal to 0, there is no limit
     * 
     * @return
     */
    default int getValidityTime( )
    {
        return LINK_VALIDITY_TIME;
    }
}
