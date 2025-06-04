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
package fr.paris.lutece.util.bean;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.util.date.DateUtil;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.SuppressPropertiesBeanIntrospector;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.SqlTimeConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Bean Utils
 */
public final class BeanUtil
{
    private static final char UNDERSCORE = '_';
    private static final String PROXY_CLASS_PROXY_TOKEN = "Proxy";
    private static final String PROXY_CLASS_DOLLAR_TOKEN = "$$";
    /** Suffix for standard configuration instances */
    private static final String STANDARD_SUFFIX = "_standard";
    
    /** Suffix for enhanced configuration instances */
    private static final String ENHANCED_SUFFIX = "_enhanced";

    private static Logger _logger = MVCUtils.getLogger();

    static
    {
        PropertyUtils.addBeanIntrospector( SuppressPropertiesBeanIntrospector.SUPPRESS_CLASS );
    }

    private static Map<String, BeanUtilsBean> _mapBeanUtilsBeans;

    /**
     * BeanUtil initialization, considering Lutèce availables locales and date format properties
     */
    public static void init( )
    {    	
        _mapBeanUtilsBeans = new HashMap<>( );
        _logger.debug("Initializing BeanUtilsManager...");
        
        final Collection<Locale> availableLocales = I18nService.getAdminAvailableLocales();
        _logger.debug("Found {} available locales", availableLocales.size());
        
        for (Locale locale : availableLocales) {
            createAndStoreBeanInstances(locale);
        }
        _logger.debug("BeanUtilsManager initialized successfully with {} instances", 
        		_mapBeanUtilsBeans.size());
        
     /*   for ( Locale locale : I18nService.getAdminAvailableLocales( ) )
        {
            BeanUtilsBean beanUtilsBean = new BeanUtilsBean( );
            beanUtilsBean.getPropertyUtils( ).addBeanIntrospector( SuppressPropertiesBeanIntrospector.SUPPRESS_CLASS );

            DateConverter dateConverter = new DateConverter( null );
            dateConverter.setPatterns( new String[] { DateUtil.ISO_PATTERN_DATE, I18nService.getDateFormatShortPattern( locale ) } );
            beanUtilsBean.getConvertUtils( ).register( dateConverter, Date.class );

            SqlTimeConverter sqlTimeConverter = new SqlTimeConverter( null );
            beanUtilsBean.getConvertUtils( ).register( sqlTimeConverter, Timestamp.class );

            _mapBeanUtilsBeans.put( locale.getLanguage( ), beanUtilsBean );
        }
        */
    }

    /** Private constructor */
    private BeanUtil( )
    {
    }

    /**
     * Populate a bean using parameters in http request
     * 
     * @param bean
     * @param request
     */
    public static void populate( Object bean, HttpServletRequest request )
    {
        populate( bean, request, null );
    }

    /**
     * Populate a bean using parameters in http request, with locale date format controls
     *
     * @param bean
     *            bean to populate
     * @param request
     *            http request
     * @param locale
     */
    public static void populate( Object bean, HttpServletRequest request, Locale locale )
    {
        Field [ ] declaredFields = bean.getClass( ).getDeclaredFields( );
        if ( isProxiedBean( bean ) )
        {
            declaredFields = bean.getClass( ).getSuperclass( ).getDeclaredFields( );
        }
        
        for ( Field field : declaredFields )
        {
            try
            {
                // for all boolean field, init to false
                if ( Boolean.class.isAssignableFrom( field.getType( ) ) || boolean.class.isAssignableFrom( field.getType( ) ) )
                {
                    field.setAccessible( true );
                    field.set( bean, false );
                }
            }
            catch( Exception e )
            {
                String error = "La valeur du champ " + field.getName( ) + " de la classe " + bean.getClass( ).getName( ) + " n'a pas pu être récupéré ";
                _logger.error( error, e );
                throw new AppException( error, e );
            }
        }

        try
        {
            BeanUtilsBean beanUtilsBean =getStandardBean( locale );
            if ( beanUtilsBean == null )
            {
                beanUtilsBean = BeanUtilsBean.getInstance( );
            }

            beanUtilsBean.populate( bean, convertMap( request.getParameterMap( ) ) );
        }
        catch( InvocationTargetException | IllegalAccessException e )
        {
        	_logger.error( "Unable to fetch data from request", e );
        }
    }

    /**
     * Convert map by casifying parameters names.
     * 
     * @param mapInput
     *            The input map
     * @return The output map
     */
    public static Map<String, String [ ]> convertMap( Map<String, String [ ]> mapInput )
    {
        Map<String, String [ ]> mapOutput = new HashMap<>( );

        for ( Entry<String, String [ ]> entry : mapInput.entrySet( ) )
        {
            mapOutput.put( convertUnderscores( entry.getKey( ) ), entry.getValue( ) );
        }

        return mapOutput;
    }

    /**
     * Remove underscore and set the next letter in caps
     * 
     * @param strSource
     *            The source
     * @return The converted string
     */
    public static String convertUnderscores( String strSource )
    {
        StringBuilder sb = new StringBuilder( );
        boolean bCapitalizeNext = false;

        for ( char c : strSource.toCharArray( ) )
        {
            if ( c == UNDERSCORE )
            {
                bCapitalizeNext = true;
            }
            else
            {
                if ( bCapitalizeNext )
                {
                    sb.append( Character.toUpperCase( c ) );
                    bCapitalizeNext = false;
                }
                else
                {
                    sb.append( c );
                }
            }
        }

        return sb.toString( );
    }
    
    /**
     * Checks if the bean is a proxy class
     * 
     * @param bean
     *            The bean
     * @return True if the bean is a proxy class
     */
    private static boolean isProxiedBean( Object bean )
    {
        return bean.toString( ).contains( PROXY_CLASS_PROXY_TOKEN ) || bean.toString( ).contains( PROXY_CLASS_DOLLAR_TOKEN );
    }
    // ==================== PRIVATE HELPER METHODS ====================
    /**
     * Retrieves a BeanUtilsBean instance for a specific locale and configuration type.
     * 
     * @param locale The locale for which to retrieve the BeanUtilsBean
     * @param enhanced Whether to retrieve the enhanced configuration instance (true) 
     *                 or standard configuration instance (false)
     * @return The BeanUtilsBean instance
     * @throws IllegalStateException if manager is not initialized
     */
    public static BeanUtilsBean getBeanUtilsBean(Locale locale, boolean enhanced) {
        if (locale == null || _mapBeanUtilsBeans == null) {
        	BeanUtilsBean.getInstance( );
        }
        
        String key = buildKey(locale, enhanced);
        return getBeanUtilsBean(key);
    }
    /**
     * Retrieves a BeanUtilsBean instance by its key.
     * 
     * @param key The key identifying the BeanUtilsBean instance
     * @return The BeanUtilsBean instance associated with the key, or null if not found
     * @throws IllegalStateException if manager is not initialized
     */
    public static BeanUtilsBean getBeanUtilsBean(String key) {
                
        return _mapBeanUtilsBeans.get(key);
    }
    /**
     * Retrieves the enhanced configuration BeanUtilsBean for the given locale.
     * 
     * @param locale The locale for which to retrieve the BeanUtilsBean
     * @return The enhanced BeanUtilsBean instance, or null if not found
     * @throws IllegalStateException if manager is not initialized
     */
    public static BeanUtilsBean getEnhancedBean(Locale locale) {
        return getBeanUtilsBean(locale, true);
    }
    /**
     * Retrieves the standard configuration BeanUtilsBean for the given locale.
     * 
     * @param locale The locale for which to retrieve the BeanUtilsBean
     * @return The standard BeanUtilsBean instance, or null if not found
     * @throws IllegalStateException if manager is not initialized
     */
    public static BeanUtilsBean getStandardBean(Locale locale) {
        return getBeanUtilsBean(locale, false);
    }
    /**
     * Creates and stores both standard and enhanced BeanUtilsBean instances for the given locale.
     * 
     * @param locale The locale to configure
     */
    private static void createAndStoreBeanInstances(Locale locale) {
        final String languageKey = locale.getLanguage();
        
        // Create and store enhanced configuration instance
        BeanUtilsBean enhancedBean = createBeanUtilsBean(locale, true);
        _mapBeanUtilsBeans.put(languageKey + ENHANCED_SUFFIX, enhancedBean);
        
        // Create and store standard configuration instance
        BeanUtilsBean standardBean = createBeanUtilsBean(locale, false);
        _mapBeanUtilsBeans.put(languageKey + STANDARD_SUFFIX, standardBean);
        
        _logger.debug("Created BeanUtilsBean instances for locale: {}", locale);
    }
    
    /**
     * Creates and configures a BeanUtilsBean instance for the specified locale.
     * 
     * @param locale The locale for date format configuration
     * @param enhanced Whether to register enhanced converter configuration
     * @return A fully configured BeanUtilsBean instance
     * @throws IllegalArgumentException if locale is null
     */
    private static BeanUtilsBean createBeanUtilsBean(Locale locale, boolean enhanced) {
        if (locale == null) {
            throw new IllegalArgumentException("Locale cannot be null");
        }
        
        BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
        
        // Configure bean introspection
        configureBeanIntrospection(beanUtilsBean);
        
        // Configure converters
        configureDateConverter(beanUtilsBean, locale);
        configureSqlTimeConverter(beanUtilsBean);
        
        // Apply enhanced converter configuration if requested
        if (enhanced) {
            applyEnhancedConfiguration(beanUtilsBean);
        }
        
        return beanUtilsBean;
    }
    
    /**
     * Configures bean introspection for the given BeanUtilsBean.
     * 
     * @param beanUtilsBean The BeanUtilsBean to configure
     */
    private static void configureBeanIntrospection(BeanUtilsBean beanUtilsBean) {
        beanUtilsBean.getPropertyUtils()
                    .addBeanIntrospector(SuppressPropertiesBeanIntrospector.SUPPRESS_CLASS);
    }
    
    /**
     * Configures the DateConverter for the given BeanUtilsBean and locale.
     * 
     * @param beanUtilsBean The BeanUtilsBean to configure
     * @param locale The locale for date formatting
     */
    private static void configureDateConverter(BeanUtilsBean beanUtilsBean, Locale locale) {
        DateConverter dateConverter = new DateConverter(null);
        
        String[] datePatterns = {
            DateUtil.ISO_PATTERN_DATE,
            I18nService.getDateFormatShortPattern(locale)
        };
        dateConverter.setPatterns(datePatterns);
        
        beanUtilsBean.getConvertUtils().register(dateConverter, Date.class);
    }
    
    /**
     * Configures the SqlTimeConverter for the given BeanUtilsBean.
     * 
     * @param beanUtilsBean The BeanUtilsBean to configure
     */
    private static void configureSqlTimeConverter(BeanUtilsBean beanUtilsBean) {
        SqlTimeConverter sqlTimeConverter = new SqlTimeConverter(null);
        beanUtilsBean.getConvertUtils().register(sqlTimeConverter, Timestamp.class);
    }
    
    /**
     * Applies enhanced converter configuration to the BeanUtilsBean.
     * 
     * @param beanUtilsBean The BeanUtilsBean to enhance
     */
    private static void applyEnhancedConfiguration(BeanUtilsBean beanUtilsBean) {
        beanUtilsBean.getConvertUtils().register(true, true, 0);
    }
    
    /**
     * Builds a key for the given locale and configuration type.
     * 
     * @param locale The locale
     * @param enhanced Whether this is for enhanced configuration
     * @return The constructed key
     */
    private static String buildKey(Locale locale, boolean enhanced) {
        String suffix = enhanced ? ENHANCED_SUFFIX : STANDARD_SUFFIX;
        return locale.getLanguage() + suffix;
    }
}
