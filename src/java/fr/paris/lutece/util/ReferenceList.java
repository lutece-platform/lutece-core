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
package fr.paris.lutece.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


/**
 * This class provides the structure for a list of references values Each
 * element includes a code and a name
 */
public class ReferenceList extends ArrayList<ReferenceItem>
{
    /**
     * Generated serialVersionUID
     */
    private static final long serialVersionUID = 5456351278712947650L;

    /**
     * Default constructor.
     */
    public ReferenceList(  )
    {
        super(  );
    }

    /**
     * Creates a new reference list with a specified initial capacity.
     * @param nInitialCapacity The initial capacity of the collection
     */
    public ReferenceList( int nInitialCapacity )
    {
        super( nInitialCapacity );
    }

    /**
     * This method adds a new element (code, name) to the list
     *
     * @param strCode The code as a String
     * @param strName The name corresponding to the code as a String
     */
    public void addItem( String strCode, String strName )
    {
        ReferenceItem item = new ReferenceItem(  );
        item.setCode( strCode );
        item.setName( strName );
        add( item );
    }

    /**
     * This method converts the int code specified in parameter as a String and
     * adds a new element (code, name) to the
     * list
     *
     * @param nCode The code to convert an add
     * @param strName The name corresponding to the code as a String
     */
    public void addItem( int nCode, String strName )
    {
        ReferenceItem item = new ReferenceItem(  );
        String strCode = String.valueOf( nCode );
        item.setCode( strCode );
        item.setName( strName );
        add( item );
    }

    /**
     * Converts a collection to a ReferenceList
     * @param collection The collection to convert
     * @param strCodeAttribute The name of the code attribute. Ex : "id" to call
     *            getId()
     * @param strNameAttribute The name of the code attribute. Ex : "name" to
     *            call getName()
     * @param bNumericCode true if the code getter returns an Integer, false
     *            otherwise
     * @return The ReferenceList filled
     * @since v1.1
     */
    public static ReferenceList convert( Collection collection, String strCodeAttribute, String strNameAttribute,
        boolean bNumericCode )
    {
        ReferenceList list = new ReferenceList(  );
        String strCodeGetter = "get" + Character.toUpperCase( strCodeAttribute.charAt( 0 ) ) +
            strCodeAttribute.substring( 1 );
        String strNameGetter = "get" + Character.toUpperCase( strNameAttribute.charAt( 0 ) ) +
            strNameAttribute.substring( 1 );
        String strCode;
        String strName;

        try
        {
            for ( Object o : collection )
            {
                // build getter method name
                if ( bNumericCode )
                {
                    Integer nCode = (Integer) o.getClass(  ).getMethod( strCodeGetter, (Class[]) null )
                                               .invoke( o, (Object[]) null );
                    strCode = nCode.toString(  );
                }
                else
                {
                    strCode = (String) o.getClass(  ).getMethod( strCodeGetter, (Class[]) null )
                                        .invoke( o, (Object[]) null );
                }

                strName = (String) o.getClass(  ).getMethod( strNameGetter, (Class[]) null ).invoke( o, (Object[]) null );
                list.addItem( strCode, strName );
            }
        }
        catch ( Exception ex )
        {
            return null;
        }

        return list;
    }

    /**
     * Convert a Map<String, String> into a ReferenceList
     * @param map the map to convert
     * @return the converted ReferenceList
     */
    public static ReferenceList convert( Map<String, String> map )
    {
        if ( map != null )
        {
            ReferenceList list = new ReferenceList(  );

            for ( Entry<String, String> param : map.entrySet(  ) )
            {
                list.addItem( param.getKey(  ), param.getValue(  ) );
            }

            return list;
        }

        return null;
    }

    /**
     * Convert the ReferenceList into a map
     * @return the converted map
     */
    public Map<String, String> toMap(  )
    {
        Map<String, String> map = new HashMap<String, String>(  );

        if ( !this.isEmpty(  ) )
        {
            for ( ReferenceItem item : this )
            {
                map.put( item.getCode(  ), item.getName(  ) );
            }
        }

        return map;
    }

    /**
     * CheckItems when are selected
     * @param valuesToCheck The list of string
     */
    public void checkItems( String[] valuesToCheck )
    {
        for ( int i = 0; i < valuesToCheck.length; i++ )
        {
            for ( ReferenceItem item : this )
            {
                if ( item.getCode(  ).equals( valuesToCheck[i] ) )
                {
                    item.setChecked( true );
                }
            }
        }
    }
}
