/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.portal.service.resource;

import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;


/**
 * This abstract Class provides a standard way for application to deliver resources
 * using multiple loaders and an optional cache.
 * @since v1.4.1
 */
public abstract class ResourceService extends AbstractCacheableService
{
    // Constants
    private static final String DELIMITER = ",";
    private static final String UNDEFINED_SERVICE_NAME = "Undefined Service Name";

    // Variables
    private String _strName = UNDEFINED_SERVICE_NAME;
    private List<ResourceLoader> _listLoaders = new ArrayList<ResourceLoader>(  );

    /**
     *
     */
    protected ResourceService(  )
    {
        String strLoadersProperty = getLoadersProperty(  );

        if ( ( strLoadersProperty != null ) && ( !strLoadersProperty.equals( "" ) ) )
        {
            initLoaders( strLoadersProperty );

            //            initCache( getName() );
        }
        else
        {
            AppLogService.error( "Resource service : Loaders property key is missing" );
        }
    }

    // Methods to overide
    /**
     * Gets the name of the property that list all loaders
     * @return The property key
     */
    protected abstract String getLoadersProperty(  );

    /**
     * Initialize loaders
     * @param strKey The property key that contains all loaders class
     */
    protected void initLoaders( String strKey )
    {
        String strLoaders = AppPropertiesService.getProperty( strKey );
        StringTokenizer st = new StringTokenizer( strLoaders, DELIMITER );

        while ( st.hasMoreTokens(  ) )
        {
            String strLoaderClassName = st.nextToken(  );
            addLoader( strLoaderClassName );
        }
    }

    /**
     * Set the service name
     * @param strName The service name
     */
    protected void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Get the service name
     * @return The service name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Set the service name by reading a property
     * @param strKey The name key
     */
    protected void setNameKey( String strKey )
    {
        setName( AppPropertiesService.getProperty( strKey, UNDEFINED_SERVICE_NAME ) );
    }

    /**
     * Defines whether the cache is enable or disable reading a property
     * @param strKey The key name of the cache
     */
    protected void setCacheKey( String strKey )
    {
        String strCache = AppPropertiesService.getProperty( strKey, "false" );

        if ( strCache.equals( "true" ) )
        {
            initCache( getName(  ) );
        }
    }

    /**
     * Add a new loader to the service
     * @param strLoaderClassName The loader class name
     */
    protected void addLoader( String strLoaderClassName )
    {
        try
        {
            ResourceLoader loader = (ResourceLoader) Class.forName( strLoaderClassName ).newInstance(  );
            _listLoaders.add( loader );
        }
        catch ( IllegalAccessException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
        catch ( InstantiationException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
        catch ( ClassNotFoundException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
    }

    /**
     * Returns a resource by its Id
     * @param strId The resource Id
     * @return A resource
     */
    protected Resource getResource( String strId )
    {
        Resource resource = null;

        if ( isCacheEnable(  ) )
        {
            resource = (Resource) getFromCache( strId );

            if ( resource == null )
            {
                resource = loadResource( strId );

                if ( resource != null )
                {
                    putInCache( strId, resource );
                }
            }
        }
        else
        {
            resource = loadResource( strId );
        }

        return resource;
    }

    /**
     * Load a resource by its Id
     * @param strId The resource Id
     * @return A resource
     */
    private Resource loadResource( String strId )
    {
        Resource resource = null;
        Iterator<ResourceLoader> i = _listLoaders.iterator(  );

        while ( i.hasNext(  ) && ( resource == null ) )
        {
            ResourceLoader loader = (ResourceLoader) i.next(  );
            resource = loader.getResource( strId );
        }

        return resource;
    }

    /**
     * Load all resources
     * @return A collection of resources
     */
    protected Collection<Resource> getResources(  )
    {
        List<Resource> listResources = new ArrayList<Resource>(  );
        Iterator<ResourceLoader> i = _listLoaders.iterator(  );

        while ( i.hasNext(  ) )
        {
            ResourceLoader loader = (ResourceLoader) i.next(  );
            Collection<Resource> colResources = loader.getResources(  );
            Iterator<Resource> j = colResources.iterator(  );

            while ( j.hasNext(  ) )
            {
                Resource resource = (Resource) j.next(  );
                listResources.add( resource );
            }
        }

        return listResources;
    }
}
