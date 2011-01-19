/*
 * Copyright (c) 2002-2010, Mairie de Paris
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

import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.io.File;
import java.io.FilenameFilter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * This class provides a way to use Spring Framework ligthweight containers
 * offering IoC (Inversion of Control) features.
 * @see http://www.springframework.org
 */
public final class SpringContextService
{
    //private static final String CORE = "core";
    //private static Map<String, ApplicationContext> _mapContext = new HashMap<String, ApplicationContext>(  );
    private static final String PATH_CONF = "/WEB-INF/conf/";
    private static final String DIR_PLUGINS = "plugins/";
    private static final String SUFFIX_CONTEXT_FILE = "_context.xml";
    private static final String FILE_CORE_CONTEXT = "core_context.xml";
    private static ApplicationContext _context;

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
        //        ApplicationContext context = getContext( CORE );
        return _context.getBean( strName );
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
        return _context.getBean( strName );
    }

    /**
     * Gets a Spring Application context from a given name
     * @param strContextName The context's name
     * @return The context
     * @deprecated
     */

    /*private static ApplicationContext getContext( String strContextName )
    {
        // Try to get the context from the cache
        ApplicationContext context = (ApplicationContext) _mapContext.get( strContextName );
    
        if ( context == null )
        {
            // If not found then load the context from the XML file
            String strContextFilePath = AppPathService.getAbsolutePathFromRelativePath( PATH_CONF );
    
            if ( !strContextName.equals( CORE ) )
            {
                strContextFilePath += DIR_PLUGINS;
            }
    
            String strContextFile = strContextFilePath + strContextName + SUFFIX_CONTEXT_FILE;
    
            try
            {
                context = new FileSystemXmlApplicationContext( "file:" + strContextFile );
            }
            catch ( Exception e )
            {
                AppLogService.debug( "Error retrieving context file : " + e.getMessage(  ) );
            }
            finally
            {
                _mapContext.put( strContextName, context );
            }
    
            _mapContext.put( strContextName, context );
        }
    
        return context;
    }*/

    /**
     * Initialize a global Application Context containing all beans (core + plugins)
     * Now uses GenericApplicationContext for better performances. A wrong formatted file
     * will not block block context to be built (without the file), but a wrong bean (i.e. cannot
     * be instantiated) will cause a full context failure. Context is less "failure-friendly"
     * @throws LuteceInitException The lutece init exception
     * @since 2.4
     */
    public static void init(  ) throws LuteceInitException
    {
        try
        {
            // timing
            Date dateBegin = new Date(  );

            // Load the core context file : core_context.xml
            String strConfPath = AppPathService.getAbsolutePathFromRelativePath( PATH_CONF );
            String strContextFile = "file:" + strConfPath + FILE_CORE_CONTEXT;

            GenericApplicationContext gap = new GenericApplicationContext(  );
            XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader( gap );

            xmlReader.loadBeanDefinitions( strContextFile );

            // _context = new ClassPathXmlApplicationContext( strContextFile );
            AppLogService.info( "Context file loaded : " + FILE_CORE_CONTEXT );

            // Load all context files found in the conf/plugins directory
            // Files are loaded separatly with an individual try/catch block
            // to avoid stopping the process in case of a failure
            // The global context generation will fail if a bean in any file cannot be built.
            String strConfPluginsPath = strConfPath + DIR_PLUGINS;
            File dirConfPlugins = new File( strConfPluginsPath );
            FilenameFilter filterContext = new ContextFileFilter(  );
            String[] filesContext = dirConfPlugins.list( filterContext );

            for ( String fileContext : filesContext )
            {
                String[] file = { "file:" + strConfPluginsPath + fileContext };

                // Safe loading of plugin context file
                try
                {
                    //_context = new ClassPathXmlApplicationContext( file, _context );
                    xmlReader.loadBeanDefinitions( file );
                    AppLogService.info( "Context file loaded : " + fileContext );
                }
                catch ( Exception e )
                {
                    AppLogService.error( "Unable to load Spring context file : " + fileContext + " - cause : " +
                        e.getMessage(  ), e );
                }
            }

            gap.refresh(  );

            _context = gap;

            AppLogService.info( "Spring context loaded in " + ( new Date(  ).getTime(  ) - dateBegin.getTime(  ) ) +
                "ms" );
        }
        catch ( Exception e )
        {
            AppLogService.error( "Error initializing Spring Context Service", e );
            throw new LuteceInitException( "Error initializing Spring Context Service", e );
        }
    }

    /**
     * Gets the application context
     *
     * @return The application context
     */
    public static ApplicationContext getContext(  )
    {
        return _context;
    }

    /**
     * Returns a list of bean among all that implements a given interface or extends a given class
     * @param <T> The class type
     * @param classDef The class type
     * @return A list of beans
     */
    public static <T> List<T> getBeansOfType( Class<T> classDef )
    {
        List<T> list = new ArrayList<T>(  );
        Map<String, T> map = BeanFactoryUtils.beansOfTypeIncludingAncestors( _context, classDef );
        String[] sBeanNames = map.keySet(  ).toArray( new String[0] );

        for ( String strBeanName : sBeanNames )
        {
            String strPluginPrefix = getPrefix( strBeanName );

            if ( ( strPluginPrefix == null ) || ( ( strPluginPrefix != null ) && isEnabled( strPluginPrefix ) ) )
            {
                list.add( map.get( strBeanName ) );
            }
        }

        return list;
    }

    /**
     * Gets the prefix of the bean (supposed to be the plugin name)
     * @param strBeanName The bean name
     * @return The prefix
     */
    private static String getPrefix( String strBeanName )
    {
        int nPos = strBeanName.indexOf( "." );

        if ( nPos > 0 )
        {
            return strBeanName.substring( 0, nPos );
        }

        return null;
    }

    /**
     * Analyze a bean prefix to tell if it matchs an activated plugin
     * @param strPrefix The prefix of a bean
     * @return True if the prefix matchs an activated plugin
     */
    private static boolean isEnabled( String strPrefix )
    {
        Plugin plugin = PluginService.getPlugin( strPrefix );

        if ( ( plugin != null ) && plugin.isInstalled(  ) )
        {
            return true;
        }

        return false;
    }

    /**
     * Utils filename filter to identify context files
     */
    static class ContextFileFilter implements FilenameFilter
    {
        /**
         * Filter filename
         * @param file The current file
         * @param strName The file name
         * @return true if the file is a context file otherwise false
         */
        public boolean accept( File file, String strName )
        {
            return strName.endsWith( SUFFIX_CONTEXT_FILE );
        }
    }
}
