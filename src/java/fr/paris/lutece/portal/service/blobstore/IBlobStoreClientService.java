/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
package fr.paris.lutece.portal.service.blobstore;

import org.apache.commons.fileupload.FileItem;


/**
 *
 * IBlobStoreClientService
 *
 */
public interface IBlobStoreClientService
{
    /**
    * Get the file name given an url
    * @param strUrl the url
    * @return the file name
    * @throws BlobStoreClientException exception if there is an issue
    */
    String getFileName( String strUrl ) throws BlobStoreClientException;

    /**
     * Do delete a file in the blobstore webapp
     * @param strBaseUrl the base url
     * @param strBlobStore the blobstore service name
     * @param strBlobKey the blob key
     * @return the deleted blob key
     * @throws BlobStoreClientException exception if there is an issue
     */
    String doDeleteFile( String strBaseUrl, String strBlobStore, String strBlobKey )
        throws BlobStoreClientException;

    /**
     * Do upload a file in the blobstore webapp
     * @param strBaseUrl the base url
     * @param fileItem the file to upload
     * @param strBlobStore the blobstore service name
     * @return the uploaded file blob key
     * @throws BlobStoreClientException exception if there is an issue
     */
    String doUploadFile( String strBaseUrl, FileItem fileItem, String strBlobStore )
        throws BlobStoreClientException;

    /**
     * Get the file url
     * @param strBaseUrl the base url
     * @param strBlobStore the blobstore service name
     * @param strBlobKey the blob key
     * @return the file url
     * @throws BlobStoreClientException exception if there is an issue
     */
    String getFileUrl( String strBaseUrl, String strBlobStore, String strBlobKey )
        throws BlobStoreClientException;

    /**
     * Download the file to the file path
     * @param strUrl the url of the file
     * @param strFilePath the file path
     * @throws BlobStoreClientException exception if there is an error
     */
    void doDownloadFile( String strUrl, String strFilePath )
        throws BlobStoreClientException;

    /**
     * Download the file
     * @param strUrl the url of the file
     * @return a {@link FileItem}
     * @throws BlobStoreClientException exception if there is an error
     */
    FileItem doDownloadFile( String strUrl ) throws BlobStoreClientException;
}
