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
package fr.paris.lutece.util.method;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * MethodUtils
 *
 */
public final class MethodUtil
{
    private static final String PREFIX_GET = "get";
    private static final String PREFIX_SET = "set";

    /**
     * Instantiates a new method utils.
     */
    private MethodUtil( )
    {
    }

    /**
     * Sets the attribute. <br>
     * <strong>Warning :</strong> This method does not handle setter that :
     * <ul>
     * <li>has no parameter or has more than one parameter</li>
     * <li>has array parameter (ie : String[] or int[] ...)</li>
     * </ul>
     *
     * @param <A>
     *            the generic type of the instance
     * @param <B>
     *            the generic type of the value to set
     * @param instance
     *            the instance to set
     * @param strAttributeName
     *            the attribute name
     * @param value
     *            the value of the attribute to set
     * @throws SecurityException
     *             the security exception
     * @throws NoSuchMethodException
     *             the no such method exception
     * @throws IllegalArgumentException
     *             the illegal argument exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws InvocationTargetException
     *             the invocation target exception
     */
    public static <A, B> void set( A instance, String strAttributeName, B value )
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        if ( StringUtils.isNotBlank( strAttributeName ) && ( instance != null ) && ( value != null ) )
        {
            Method methodSetter = getSetter( instance, strAttributeName, value.getClass( ) );

            if ( methodSetter != null )
            {
                methodSetter.invoke( instance, value );
            }
            else
            {
                throw new NoSuchMethodException( );
            }
        }
        else
        {
            throw new IllegalArgumentException( "One on the parameters is null/blank." );
        }
    }

    /**
     * Gets the method.
     *
     * @param <A>
     *            the generic type of the instance
     * @param strMethodPrefix
     *            the str method prefix
     * @param instance
     *            the instance
     * @param strAttributeName
     *            the str attribute name
     * @param clazz
     *            the clazz
     * @return the method
     * @throws SecurityException
     *             the security exception
     * @throws NoSuchMethodException
     *             the no such method exception
     */
    public static <A> Method getMethod( String strMethodPrefix, A instance, String strAttributeName, Class<?> clazz ) throws NoSuchMethodException
    {
        String strFirstLetter = strAttributeName.substring( 0, 1 ).toUpperCase( );

        String strMethodName = strMethodPrefix + strFirstLetter + strAttributeName.substring( 1, strAttributeName.length( ) );

        try
        {
            return instance.getClass( ).getMethod( strMethodName, clazz );
        }
        catch( NoSuchMethodException e )
        {
            return getPrimitiveMethod( strMethodName, instance, clazz );
        }
    }

    /**
     * Gets the primitive method.
     *
     * @param <A>
     *            the generic type of the instance
     * @param strMethodName
     *            the str method name
     * @param instance
     *            the instance
     * @param clazz
     *            the clazz
     * @return the primitive method
     * @throws SecurityException
     *             the security exception
     * @throws NoSuchMethodException
     *             the no such method exception
     */
    public static <A> Method getPrimitiveMethod( String strMethodName, A instance, Class<?> clazz ) throws NoSuchMethodException
    {
        if ( clazz.equals( Integer.class ) )
        {
            return instance.getClass( ).getMethod( strMethodName, int.class );
        }
        else
            if ( clazz.equals( Long.class ) )
            {
                return instance.getClass( ).getMethod( strMethodName, long.class );
            }
            else
                if ( clazz.equals( Double.class ) )
                {
                    return instance.getClass( ).getMethod( strMethodName, double.class );
                }
                else
                    if ( clazz.equals( Short.class ) )
                    {
                        return instance.getClass( ).getMethod( strMethodName, short.class );
                    }
                    else
                        if ( clazz.equals( Byte.class ) )
                        {
                            return instance.getClass( ).getMethod( strMethodName, byte.class );
                        }
                        else
                            if ( clazz.equals( Float.class ) )
                            {
                                return instance.getClass( ).getMethod( strMethodName, float.class );
                            }
                            else
                                if ( clazz.equals( Character.class ) )
                                {
                                    return instance.getClass( ).getMethod( strMethodName, char.class );
                                }
                                else
                                    if ( clazz.equals( Boolean.class ) )
                                    {
                                        return instance.getClass( ).getMethod( strMethodName, boolean.class );
                                    }

        throw new NoSuchMethodException( );
    }

    /**
     * Gets the setter.
     *
     * @param <A>
     *            the generic type
     * @param instance
     *            the instance
     * @param strAttributeName
     *            the str attribute name
     * @param clazz
     *            the clazz
     * @return the setter
     * @throws SecurityException
     *             the security exception
     * @throws NoSuchMethodException
     *             the no such method exception
     */
    public static <A> Method getSetter( A instance, String strAttributeName, Class<?> clazz ) throws NoSuchMethodException
    {
        return getMethod( PREFIX_SET, instance, strAttributeName, clazz );
    }

    /**
     * Gets the setter.
     *
     * @param <A>
     *            the generic type of the instance
     * @param instance
     *            the instance
     * @param strAttributeName
     *            the str attribute name
     * @param clazz
     *            the clazz
     * @return the setter
     * @throws SecurityException
     *             the security exception
     * @throws NoSuchMethodException
     *             the no such method exception
     */
    public static <A> Method getGetter( A instance, String strAttributeName, Class<?> clazz ) throws NoSuchMethodException
    {
        return getMethod( PREFIX_GET, instance, strAttributeName, clazz );
    }
}
