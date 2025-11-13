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
package fr.paris.lutece.portal.service.html;

import fr.paris.lutece.portal.service.cache.CacheConfigUtil;
import fr.paris.lutece.portal.service.cache.CacheService;
import fr.paris.lutece.portal.service.cache.CacheableService;
import fr.paris.lutece.util.xml.XmlTransformer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * XmlTransformerCacheService
 * 
 * @since v 3.0
 */
@ApplicationScoped
public class XmlTransformerCacheService implements CacheableService<String,String>
{
    private static final String SERVICE_NAME = "XML Transformer Cache Service (XSLT)";
    private static final String MSG_KEYS_NOT_AVAILABLE = "Keys not available";
    
    private static final Logger logger = LogManager.getLogger(CacheConfigUtil.CACHE_LOGGER_NAME);

    @Inject 
    XmlTransformerCacheService xmlTransformerCacheService;
    /**
     * {@inheritDoc }
     */
    @Override
    public String getName( )
    {
        return SERVICE_NAME;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isCacheEnable( )
    {
        return true;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int getCacheSize( )
    {
        return XmlTransformer.getTransformersCount( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void resetCache( )
    {
        XmlTransformer.cleanTransformerList( );
        logger.debug( "XmlTransformer cache service cache has been reset" );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void enableCache( boolean bEnable )
    {
        // Always enable
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<String> getKeys( )
    {
        List<String> list = new ArrayList<>( );
        list.add( MSG_KEYS_NOT_AVAILABLE );

        return list;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getInfos( )
    {
        return "This cache can't be disabled - Poolsize = " + XmlTransformer.TRANSFORMER_POOL_SIZE;
    }

    /**
    * This method observes the initialization of the {@link ApplicationScoped} context
    * and registers an instance of {@link XmlTransformerCacheService} with the {@link CacheService}.
    *
    * <p>This method is triggered automatically by CDI when the {@link ApplicationScoped} context is initialized,
    * which typically occurs during the startup of the application server.</p>
    *
    * @param context the {@link ServletContext} that is initialized. This parameter is observed
    *                and injected automatically by CDI when the {@link ApplicationScoped} context is initialized.
    */
    public void initializedService(@Observes @Initialized(ApplicationScoped.class) 
   	ServletContext context){		
        CacheService.registerCacheableService( xmlTransformerCacheService );     
   	}

    @Override
    public boolean isPreventGlobalReset( )
    {
        return false;
    }

}
