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
package fr.paris.lutece.util.sort;

import fr.paris.lutece.portal.service.util.AppLogService;

import java.io.Serializable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.Comparator;


/**
 * This class provide Attribute Comparator
 */
public class AttributeComparator implements Comparator<Object>, Serializable
{
    private static final long serialVersionUID = 8552197766086300259L;
    private String _strSortedAttribute;
    private boolean _bIsASC;

    /**
     * Constructor
     * @param strSortedAttribute the name of the attribute on which the sort will be made
     * @param bIsASC true for the ASC order, false for the DESC order
     */
    public AttributeComparator( String strSortedAttribute, boolean bIsASC )
    {
        this._strSortedAttribute = strSortedAttribute;
        this._bIsASC = bIsASC;
    }

    /**
     * Constructor
     * @param strSortedAttribute the name of the attribute on which the sort will be made
     */
    public AttributeComparator( String strSortedAttribute )
    {
        this._strSortedAttribute = strSortedAttribute;
        this._bIsASC = true;
    }

    /**
     * Compare two objects o1 and o2.
     * @param o1 Object
     * @param o2 Object
     * @return < 0 if o1 is before o2 in the alphabetical order 0 if o1 equals o2 > 0 if o1 is after o2
     */
    @Override
    public int compare( Object o1, Object o2 )
    {
        int nStatus = 0;

        Method method1 = getMethod( o1 );
        Method method2 = getMethod( o2 );

        if ( ( method1 != null ) && ( method2 != null ) && ( method1.getReturnType(  ) == method2.getReturnType(  ) ) )
        {
            try
            {
                Object oRet1 = method1.invoke( o1 );
                Object oRet2 = method2.invoke( o2 );

                String strReturnType = method1.getReturnType(  ).getName(  ).toString(  );
                Class<?> returnType = method1.getReturnType(  );

                if ( oRet1 == null )
                {
                    if ( oRet2 == null )
                    {
                        nStatus = 0;
                    }
                    else
                    {
                        nStatus = -1;
                    }
                }
                else
                {
                    if ( oRet2 == null )
                    {
                        nStatus = 1;
                    }
                    else
                    {
                        if ( strReturnType.equals( "java.lang.String" ) )
                        {
                            nStatus = ( (String) oRet1 ).toLowerCase(  ).compareTo( ( (String) oRet2 ).toLowerCase(  ) );
                        }
                        else if ( returnType.isPrimitive(  ) || isComparable( returnType ) )
                        {
                            nStatus = ( (Comparable) oRet1 ).compareTo( (Comparable) oRet2 );
                        }
                        else if ( returnType.isEnum(  ) )
                        {
                            nStatus = oRet1.toString(  ).compareTo( oRet2.toString(  ) );
                        }
                    }
                }
            }
            catch ( IllegalArgumentException e )
            {
                AppLogService.error( e );
            }
            catch ( IllegalAccessException e )
            {
                AppLogService.error( e );
            }
            catch ( InvocationTargetException e )
            {
                AppLogService.error( e );
            }
        }

        if ( !_bIsASC )
        {
            nStatus = nStatus * ( -1 );
        }

        return nStatus;
    }

    /**
     * Return the getter method of the object obj for the attribute _strSortedAttribute
     * @param obj the object
     * @return method Method of the object obj for the attribute _strSortedAttribute
     */
    private Method getMethod( Object obj )
    {
        Method method = null;
        String strFirstLetter = _strSortedAttribute.substring( 0, 1 ).toUpperCase(  );

        String strMethodName = "get" + strFirstLetter +
            _strSortedAttribute.substring( 1, _strSortedAttribute.length(  ) );

        try
        {
            method = obj.getClass(  ).getMethod( strMethodName );
        }
        catch ( Exception e )
        {
            AppLogService.error( e );
        }

        return method;
    }

    /**
     * Returns <code>true</code> if the class implements {@link Comparable} or extends a super class that
     * implements {@link Comparable}, <code>false</code> otherwise.
     * @param clazz the class
     * @return <code>true</code> if the class implements {@link Comparable}, <code>false</code> otherwise.
     */
    private boolean isComparable( Class<?> clazz )
    {
        for ( Class<?> interfac : clazz.getInterfaces(  ) )
        {
            if ( interfac.equals( Comparable.class ) )
            {
                return true;
            }
        }

        // The class might be extending a super class that implements {@link Comparable}
        Class<?> superClass = clazz.getSuperclass(  );

        if ( superClass != null )
        {
            return isComparable( superClass );
        }

        return false;
    }
}
