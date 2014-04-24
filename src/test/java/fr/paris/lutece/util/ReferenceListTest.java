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

import fr.paris.lutece.test.LuteceTestCase;

import java.util.ArrayList;
import java.util.Collection;


/**
 * ReferenceList Test Class
 *
 */
public class ReferenceListTest extends LuteceTestCase
{
    /**
     * Test of addItem method, of class fr.paris.lutece.util.ReferenceList.
     */
    public void testAddItem(  )
    {
        System.out.println( "addItem" );

        String strCode = "code";
        String strName = "name";
        ReferenceList instance = new ReferenceList(  );

        instance.addItem( strCode, strName );
    }

    /**
     * Test of convert method, of class fr.paris.lutece.util.ReferenceList.
     */
    public void testConvert(  )
    {
        System.out.println( "convert" );

        Collection collection = new ArrayList(  );
        ReferenceItem item = new ReferenceItem(  );
        String strCode = "code 1";
        String strName = "name 1";
        item.setCode( strCode );
        item.setName( strName );
        item.setChecked( true );

        boolean bNumericCode = false;
        collection.add( item );

        // Convert the ArrayList into a ReferenceList
        String strCodeAttribute = "code";
        String strNameAttribute = "name";
        ReferenceList result = ReferenceList.convert( collection, strCodeAttribute, strNameAttribute, bNumericCode );
        assertEquals( strCode, result.get( 0 ).getCode(  ) );
        assertEquals( strName, result.get( 0 ).getName(  ) );
    }

    /**
     * Test of checkItems method, of class fr.paris.lutece.util.ReferenceList.
     */
    public void testCheckItems(  )
    {
        System.out.println( "checkItems" );

        String[] valuesToCheck = { "code1", "code3" };
        ReferenceList instance = new ReferenceList(  );
        instance.addItem( "code1", "name1" );
        instance.addItem( "code2", "name2" );
        instance.addItem( "code3", "name3" );

        instance.checkItems( valuesToCheck );
        assertTrue( instance.get( 0 ).isChecked(  ) );
        assertFalse( instance.get( 1 ).isChecked(  ) );
        assertTrue( instance.get( 2 ).isChecked(  ) );
    }
}
