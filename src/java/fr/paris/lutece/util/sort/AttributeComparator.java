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
package fr.paris.lutece.util.sort;

import fr.paris.lutece.portal.service.util.AppLogService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.Comparator;


public class AttributeComparator implements Comparator<Object>
{
    private String _strSortedAttribute;
    private boolean _bIsASC;

    /**
     * Constructor
     * @param strSortedAttribute the name of the attribute on which the sort will be made
     * @param bIsAsc true for the ASC order, false for the DESC order
     */
    public AttributeComparator( String strSortedAttribute, boolean bIsASC )
    {
        this._strSortedAttribute = strSortedAttribute;
        this._bIsASC = bIsASC;
    }

    /**
     * Constructor
     * @param strSortedAttribute the name of the attribute on which the sort will be made
     * @param bIsAsc true for the ASC order, false for the DESC order
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
     * @return < 0 if o1 is before o2 in the alphabetical order
     *           0 if o1 equals o2
     *         > 0 if o1 is after o2
     */
    public int compare( Object o1, Object o2 )
    {
        int nStatus = 0;

        Method method1 = getMethod( o1 );
        Method method2 = getMethod( o2 );

        if ( ( method1 != null ) && ( method2 != null ) && ( method1.getReturnType(  ) == method2.getReturnType(  ) ) )
        {
            try
            {
                String strReturnType = method1.getReturnType(  ).getName(  ).toString(  );

                if ( strReturnType.equals( "java.lang.String" ) )
                {
                    nStatus = ( (String) method1.invoke( o1 ) ).toLowerCase(  )
                                .compareTo( ( (String) method2.invoke( o2 ) ).toLowerCase(  ) );
                }
                else if ( strReturnType.equals( "int" ) )
                {
                    nStatus = ( (Integer) method1.invoke( o1 ) ).compareTo( (Integer) method2.invoke( o2 ) );
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
}
