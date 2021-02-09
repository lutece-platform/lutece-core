/*
 * Copyright (c) 2002-2021, City of Paris
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
import fr.paris.lutece.portal.service.util.AppLogService;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.SuppressPropertiesBeanIntrospector;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.SqlTimeConverter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

/**
 * Bean Utils
 */
public final class BeanUtil
{
    private static final char UNDERSCORE = '_';

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

        for ( Locale locale : I18nService.getAdminAvailableLocales( ) )
        {
            BeanUtilsBean beanUtilsBean = new BeanUtilsBean( );
            beanUtilsBean.getPropertyUtils( ).addBeanIntrospector( SuppressPropertiesBeanIntrospector.SUPPRESS_CLASS );

            DateConverter dateConverter = new DateConverter( null );
            dateConverter.setPattern( I18nService.getDateFormatShortPattern( locale ) );
            beanUtilsBean.getConvertUtils( ).register( dateConverter, Date.class );
            
            SqlTimeConverter sqlTimeConverter = new SqlTimeConverter( null );
            beanUtilsBean.getConvertUtils( ).register( sqlTimeConverter, Timestamp.class );
            
            _mapBeanUtilsBeans.put( locale.getLanguage( ), beanUtilsBean );
        }
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
        for ( Field field : bean.getClass( ).getDeclaredFields( ) )
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
                AppLogService.error( error );
                throw new AppException( error, e );
            }
        }

        try
        {
            BeanUtilsBean beanUtilsBean;
            if ( locale != null && _mapBeanUtilsBeans != null )
            {
                beanUtilsBean = _mapBeanUtilsBeans.get( locale.getLanguage( ) );
            }
            else
            {
                beanUtilsBean = BeanUtilsBean.getInstance( );
            }

            beanUtilsBean.populate( bean, convertMap( request.getParameterMap( ) ) );
        }
        catch( InvocationTargetException | IllegalAccessException e )
        {
            AppLogService.error( "Unable to fetch data from request", e );
        }
    }

    /**
     * Convert map by casifying parameters names.
     * 
     * @param mapInput
     *            The input map
     * @return The output map
     */
    public static Map<String, Object> convertMap( Map<String, String [ ]> mapInput )
    {
        Map<String, Object> mapOutput = new HashMap<>( );

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
}
