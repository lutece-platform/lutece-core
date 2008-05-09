/*
 * Copyright (c) 2002-2008, Mairie de Paris
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
package fr.paris.lutece.portal.service.spring;

import fr.paris.lutece.portal.service.util.AppPathService;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.util.HashMap;
import java.util.Map;


/**
 * This class provides a way to use Spring Framework ligthweight containers
 * offering IoC (Inversion of Control) features.
 * @see http://www.springframework.org
 */
public final class SpringContextService
{
    private static final String CORE = "core";
    private static Map<String, BeanFactory> _mapBeanFactories = new HashMap<String, BeanFactory>(  );
    private static final String PATH_CONF = "/WEB-INF/conf/";
    private static final String DIR_PLUGINS = "plugins/";
    private static final String SUFFIX_CONTEXT_FILE = "_context.xml";

    /** Creates a new instance of SpringContextService */
    private SpringContextService(  )
    {
    }

    /**
     * Return an instance, which may be shared or independent, of the given bean name.
     * This method allows a Spring BeanFactory to be used as a replacement for
     * the Singleton or Prototype design pattern.<br />
     * The bean is retreived from the main context defined in the WEB-INF/conf/core_context.xml.
     * @param strName The bean's name
     * @return The instance of the bean
     */
    public static Object getBean( String strName )
    {
        BeanFactory factory = getBeanFactory( CORE );

        return factory.getBean( strName );
    }

    /**
     * Return an instance of the given bean name loaded by the a Spring BeanFactory.
     * The bean is retreived from a plugin context defined in the WEB-INF/conf/plugins/[plugin_name]_context.xml.
     * @param strPluginName The Plugin's name
     * @param strName The bean's name
     * @return The instance of the bean
     */
    public static Object getPluginBean( String strPluginName, String strName )
    {
        BeanFactory factory = getBeanFactory( strPluginName );

        return factory.getBean( strName );
    }

    /**
     * Gets a Spring Application context from a given name
     * @param strContextName The context's name
     * @return The context
     */
    private static BeanFactory getBeanFactory( String strContextName )
    {
        // Try to get the context from the cache
        BeanFactory factory = (BeanFactory) _mapBeanFactories.get( strContextName );

        if ( factory == null )
        {
            // If not found then load the context from the XML file
            String strContextFilePath = AppPathService.getAbsolutePathFromRelativePath( PATH_CONF );

            if ( !strContextName.equals( CORE ) )
            {
                strContextFilePath += DIR_PLUGINS;
            }

            String strContextFile = strContextFilePath + strContextName + SUFFIX_CONTEXT_FILE;
            Resource resource = new FileSystemResource( strContextFile );
            factory = new XmlBeanFactory( resource );
            _mapBeanFactories.put( strContextName, factory );
        }

        return factory;
    }
}
