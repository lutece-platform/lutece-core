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

import java.io.InputStream;
import java.io.Serializable;


/**
 * Blob Store Service Interface. <i>*InputStream</i> methods should be used for very large blobs.
 */
public interface BlobStoreService extends Serializable
{
    /**
     * Store a blob
     * @param blob The blob
     * @return The key of the blob
     */
    String store( byte[] blob );

    /**
     * Stores an input stream
     * @param inputStream the input stream
     * @return The key of the blob
     */
    String storeInputStream( InputStream inputStream );

    /**
     * Get a blob
     * @param strKey The key of the blob
     * @return  The blob
     */
    byte[] getBlob( String strKey );

    /**
     * Gets a blob as {@link InputStream}
     * @param strKey the key
     * @return the {@link InputStream}
     */
    InputStream getBlobInputStream( String strKey );

    /**
     * Update a blob
     * @param strKey The key of the blob
     * @param blob The new blob
     */
    void update( String strKey, byte[] blob );

    /**
     * Updates a blob key with the inputstream
     * @param strKey the blob key
     * @param inputStream the input stream
     */
    void updateInputStream( String strKey, InputStream inputStream );

    /**
     * Delete a blob
     * @param strKey The key of the blob
     */
    void delete( String strKey );

    /**
     * Gets the blob URL
     * @param strKey
     * @return the blob url
     */
    String getBlobUrl( String strKey );

    /**
     * Gets the file download url (for {@link BlobStoreFileItem})
     * @param strKey the
     * @return the download link
     */
    String getFileUrl( String strKey );

    /**
     * Gets the blobstore name
     * @return the blobstore name
     */
    String getName(  );

    /**
     * Sets the blobstore name
     * @param strName the name
     */
    void setName( String strName );
}
