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

import fr.paris.lutece.portal.service.util.AppException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;


/**
 *
 * @author SLE
 */
@ApplicationScoped
public class FileService
{

    // parameters
    public static final String PARAMETER_FILE_ID = "file_id";
    public static final String PARAMETER_RESOURCE_ID = "resource_id";
    public static final String PARAMETER_RESOURCE_TYPE = "resource_type";
    public static final String PARAMETER_VALIDITY_TIME = "validity_time";
    public static final String PARAMETER_DATA = "data";
    public static final String PARAMETER_BO = "is_bo";
    public static final String PARAMETER_PROVIDER = "provider";

    // constants
    public static final String PERMISSION_VIEW = "VIEW";

    // messages
    private static final String MSG_NO_FILE_SERVICE = "No file service Available";


    @Inject
    private IFileStoreServiceProvider _defaulFileStoreServiceProvider;
    @Inject
    private Instance<IFileStoreServiceProvider> _fileStoreServiceProviderList;


    /**
     * init
     */
    @PostConstruct
    void initFileService( )
    {
        _defaulFileStoreServiceProvider = getDefaultServiceProvider( );
    }

    /**
     * Returns the unique instance of the {@link FileService} service.
     * 
     * <p>This method is deprecated and is provided for backward compatibility only. 
     * For new code, use dependency injection with {@code @Inject} to obtain the 
     * {@link FileService} instance instead.</p>
     * 
     * @return The unique instance of {@link FileService}.
     * 
     * @deprecated Use {@code @Inject} to obtain the {@link FileService} 
     * instance. This method will be removed in future versions.
     */
    @Deprecated( since = "8.0", forRemoval = true )
    public static FileService getInstance( )
    {
        return CDI.current( ).select( FileService.class ).get( );
    }

   /**
     * get the current FileStoreService provider
     * 
     * @return the current FileStoreService provider
     */
    public IFileStoreServiceProvider getFileStoreServiceProvider( )
    {
        return getFileStoreServiceProvider( null) ;
    }

    /**
     * get the FileStoreService provider
     * 
     * @param strFileStoreServiceProviderName
     * @return the current FileStoreService provider
     */
    public IFileStoreServiceProvider getFileStoreServiceProvider( String strFileStoreServiceProviderName )
    {

    	if ( strFileStoreServiceProviderName == null && _defaulFileStoreServiceProvider != null )
    	{
    		return _defaulFileStoreServiceProvider;
    	}
    	
        // search file service
        if ( !_fileStoreServiceProviderList.isUnsatisfied() && strFileStoreServiceProviderName != null )
        {
            for ( IFileStoreServiceProvider fss : _fileStoreServiceProviderList )
            {
                if ( strFileStoreServiceProviderName.equals( fss.getName( ) ) )
                {
                      return fss;
                }
            }
            return _fileStoreServiceProviderList.stream()
                    .filter(fss -> strFileStoreServiceProviderName.equals(fss.getName()))
                    .findFirst().orElseThrow(()-> new AppException( MSG_NO_FILE_SERVICE ));
        }

        // otherwise
        throw new AppException( MSG_NO_FILE_SERVICE );
    }
    /** get default File Store Service Provider
     * 
     * @return the provider
     */
    private IFileStoreServiceProvider getDefaultServiceProvider( )
    {
        // search default file service
        if ( !_fileStoreServiceProviderList.isUnsatisfied() )
        {
           return  _fileStoreServiceProviderList.stream()
            .filter(fss -> fss.isDefault())
            .findFirst()
            .orElseGet(() -> _fileStoreServiceProviderList.stream().findFirst()
                    .orElseThrow(() -> new AppException(MSG_NO_FILE_SERVICE)));
        }
        // otherwise
        throw new AppException( MSG_NO_FILE_SERVICE );
    }
}
