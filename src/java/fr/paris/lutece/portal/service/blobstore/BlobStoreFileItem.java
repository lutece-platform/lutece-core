/*
 * Copyright (c) 2002-2011, Mairie de Paris
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

import fr.paris.lutece.portal.service.util.AppLogService;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;


/**
 * Builds a fileItem from blobstore implementing {@link FileItem}. <br>
 * Metadata is stored in one blob, and content in another one.
 * get() method is lazy preventing blob to be stored in-memory.
 * Use {@link #buildFileMetadata(String, long, String)} to build the FileMetadata.
 * @see #buildFileMetadata(String, long, String)
 * @see #BlobStoreFileItem(String, BlobStoreService)
 *
 */
public class BlobStoreFileItem implements FileItem
{
    public static final String JSON_KEY_FILE_SIZE = "fileSize";
    public static final String JSON_KEY_FILE_NAME = "fileName";
    public static final String JSON_KEY_FILE_CONTENT_TYPE = "fileContentType";
    public static final String JSON_KEY_FILE_BLOB_ID = "fileBlobId";
    public static final String JSON_KEY_FILE_METADATA_BLOB_ID = "fileMetadata";
    private static final long serialVersionUID = 1L;
    private BlobStoreService _blobstoreService;
    private String _strBlobId;
    private String _strFileName;
    private long _lFileSize;
    private String _strFileBlobId;
    private String _strContentType;

    /**
     * Builds a fileItem from blobstore. get() method is lazy.
     * The {@link BlobStoreService} is here to prevent specific usage for the fileItem so it can be used as any other FileItem.
     * @param strBlobId the blob id
     * @param blobstoreService the blob service
     * @throws NoSuchBlobException if blob cannot be parsed
     */
    public BlobStoreFileItem( String strBlobId, BlobStoreService blobstoreService )
        throws NoSuchBlobException
    {
        _strBlobId = strBlobId;
        _blobstoreService = blobstoreService;

        // first, get the metadata
        byte[] blob = _blobstoreService.getBlob( _strBlobId );

        if ( blob == null )
        {
            throw new NoSuchBlobException( "No blob found for id " + strBlobId );
        }

        JSONObject jsonObject = parseBlob( blob );

        if ( jsonObject != null )
        {
            String strSize = (String) jsonObject.get( JSON_KEY_FILE_SIZE );
            _lFileSize = Long.parseLong( strSize );
            _strFileName = (String) jsonObject.get( JSON_KEY_FILE_NAME );
            // store the real blob id - file will be fetch on demand (#get)
            _strFileBlobId = (String) jsonObject.get( JSON_KEY_FILE_BLOB_ID );
            _strContentType = (String) jsonObject.getString( JSON_KEY_FILE_CONTENT_TYPE );
        }
        else
        {
            throw new NoSuchBlobException( strBlobId );
        }
    }

    /**
     * Gets the metadata blob id
     * @return the metadata blob id
     */
    public String getBlobId(  )
    {
        return _strBlobId;
    }

    /**
     * Gets the file blob id
     * @return the file blob id
     */
    public String getFileBlobId(  )
    {
        return _strFileBlobId;
    }

    /**
     * Deletes both blobs : metadata <strong>AND</strong> content.
     */
    public void delete(  )
    {
        _blobstoreService.delete( _strFileBlobId );
        _blobstoreService.delete( _strBlobId );
    }

    /**
     * {@inheritDoc}
     */
    public byte[] get(  )
    {
        return _blobstoreService.getBlob( _strFileBlobId );
    }

    /**
     * {@inheritDoc}
     */
    public String getContentType(  )
    {
        return _strContentType;
    }

    /**
     * Not supported
     * @return null
     */
    public String getFieldName(  )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * @throws IOException ioexception
     */
    public InputStream getInputStream(  ) throws IOException
    {
        return _blobstoreService.getBlobInputStream( _strFileBlobId );
    }

    /**
     * {@inheritDoc}
     */
    public String getName(  )
    {
        return _strFileName;
    }

    /**
     * Not supported - throws UnsupportedOperationException exception
     * @return nothing
     * @throws IOException ioe
     */
    public OutputStream getOutputStream(  ) throws IOException
    {
        throw new UnsupportedOperationException(  );
    }

    /**
     * {@inheritDoc}
     */
    public long getSize(  )
    {
        return _lFileSize;
    }

    /**
     * {@inheritDoc}
     */
    public String getString(  )
    {
        return new String( get(  ) );
    }

    /**
     * {@inheritDoc}
     */
    public String getString( String encoding ) throws UnsupportedEncodingException
    {
        return new String( get(  ), encoding );
    }

    /**
     * Not supported
     * @return false
     */
    public boolean isFormField(  )
    {
        return false;
    }

    /**
     * Always false.
     * @return false
     */
    public boolean isInMemory(  )
    {
        return false;
    }

    /**
     * Not supported
     * @param name -
     */
    public void setFieldName( String name )
    {
        // nothing
    }

    /**
     * Not supported
     * @param state -
     */
    public void setFormField( boolean state )
    {
        // nothing
    }

    /**
     * Not supported
     * @param file -
     * @throws Exception ex
     */
    public void write( File file ) throws Exception
    {
        throw new UnsupportedOperationException(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString(  )
    {
        return "BlobId:" + _strBlobId + " FileBlobId:" + _strFileBlobId + " FileName:" + _strFileName;
    }

    /**
     * Parses a blob to a JSONObject
     * @param blob the blob
     * @return the {@link JSONObject}, <code>null</code> if blob is null or an exception occur
     */
    private static JSONObject parseBlob( byte[] blob )
    {
        if ( blob == null )
        {
            return null;
        }

        try
        {
            return JSONObject.fromObject( new String( blob ) );
        }
        catch ( JSONException je )
        {
            AppLogService.error( je.getMessage(  ), je );
        }

        return null;
    }

    /**
         * Builds the json value of a file metadata.
         * @param strFileName filename
         * @param lSize size
         * @param strFileBlobId the blob id
         * @param strContentType the content type
         * @return the json of the fileMetadata to store in BlobStore
         */
    public static final String buildFileMetadata( String strFileName, long lSize, String strFileBlobId,
        String strContentType )
    {
        JSONObject json = new JSONObject(  );
        json.accumulate( BlobStoreFileItem.JSON_KEY_FILE_SIZE, Long.toString( lSize ) );
        json.accumulate( BlobStoreFileItem.JSON_KEY_FILE_NAME, strFileName );
        json.accumulate( BlobStoreFileItem.JSON_KEY_FILE_BLOB_ID, strFileBlobId );
        json.accumulate( BlobStoreFileItem.JSON_KEY_FILE_CONTENT_TYPE, strContentType );

        return json.toString(  );
    }
}
