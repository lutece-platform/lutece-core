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

import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;
import java.util.List;

/**
 *
 * @author SLE
 */
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

    private IFileStoreServiceProvider _currentFileStoreServiceProvider;
    private static FileService _instance = new FileService( );

    /**
     * init
     */
    private FileService( )
    {
        _currentFileStoreServiceProvider = getDefaultServiceProvider( );
    }

    /**
     * getter
     * 
     * @return the instance
     */
    public static FileService getInstance( )
    {
        return _instance;
    }

    /**
     * get the current FileStoreService provider
     * 
     * @return the current FileStoreService provider
     */
    public IFileStoreServiceProvider getFileStoreServiceProvider( )
    {
        return _currentFileStoreServiceProvider;
    }

    /**
     * get the FileStoreService provider
     * 
     * @param strFileStoreServiceProviderName
     * @return the current FileStoreService provider
     */
    public IFileStoreServiceProvider getFileStoreServiceProvider( String strFileStoreServiceProviderName )
    {
    	List<IFileStoreServiceProvider> fileStoreServiceProviderList = SpringContextService.getBeansOfType( IFileStoreServiceProvider.class );

        // search file service
        if ( !fileStoreServiceProviderList.isEmpty( ) )
        {
            for ( IFileStoreServiceProvider fss : fileStoreServiceProviderList )
            {
                if ( strFileStoreServiceProviderName.equals( fss.getName( ) ) )
                {
                      return fss;
                }
            }
        }

        // otherwise
        throw new AppException( MSG_NO_FILE_SERVICE );
    }

    /**
     * get the current FileStoreService provider
     * 
     * @param strFileStoreServiceProviderName
     */
    public void setFileStoreServiceProvider( String strFileStoreServiceProviderName )
    {
        List<IFileStoreServiceProvider> fileStoreServiceProviderList = SpringContextService.getBeansOfType( IFileStoreServiceProvider.class );

        // search file service
        if ( !fileStoreServiceProviderList.isEmpty( ) )
        {
            for ( IFileStoreServiceProvider fss : fileStoreServiceProviderList )
            {
                if ( strFileStoreServiceProviderName.equals( fss.getName( ) ) )
                {
                    _currentFileStoreServiceProvider = fss;
                    return;
                }
            }
        }

        // otherwise
        throw new AppException( MSG_NO_FILE_SERVICE );
    }

    /**
     * get default File Store Service Provider
     * 
     * @return the provider
     */
    private IFileStoreServiceProvider getDefaultServiceProvider( )
    {
        List<IFileStoreServiceProvider> fileStoreServiceProviderList = SpringContextService.getBeansOfType( IFileStoreServiceProvider.class );

        // search default file service
        if ( !fileStoreServiceProviderList.isEmpty( ) )
        {
            for ( IFileStoreServiceProvider fss : fileStoreServiceProviderList )
            {
                if ( fss.isDefault( ) )
                {
                    return fss;
                }
            }

            // return the first one otherwise
            return fileStoreServiceProviderList.get( 0 ) ;
        }

        // otherwise
        throw new AppException( MSG_NO_FILE_SERVICE );
    }
}
