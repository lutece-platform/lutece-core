/*
 * Copyright (c) 2002-2009, Mairie de Paris
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
package fr.paris.lutece.util.date;

import junit.framework.TestCase;

import java.text.SimpleDateFormat;

import java.util.Locale;


/**
 * This class tests the class fr.paris.lutece.util.date.DateUtil.
 */
public class DateUtilTest extends TestCase
{
    /**
     * Constructor for DateUtilTest.
     * @param strName the name of the test
     */
    public DateUtilTest( String strName )
    {
        super( strName );
    }

    /**
     * Tests the getCurrentDateString method
     */
    public void testGetCurrentDateString(  )
    {
        SimpleDateFormat formatter = new SimpleDateFormat( "dd'/'MM'/'yyyy", Locale.FRANCE );

        // true if the date does not change between the two calls
        assertEquals( formatter.format( new java.util.Date(  ) ), DateUtil.getCurrentDateString(  ) );
    }

    /**
     * Tests the getDateSql method
     */
    public void testGetDateSql(  )
    {
        final String DATE = "22/09/1978";
        assertEquals( DATE, DateUtil.getDateString( DateUtil.getDateSql( DATE ) ) );
    }

    /**
     * Tests the getDate method
     */
    public void testGetDate(  )
    {
        final String DATE = "22/09/1978";
        assertEquals( DATE, DateUtil.getDateString( DateUtil.getDate( DATE ) ) );
    }

    /**
     * Tests the getTimestamp method
     */
    public void testGetTimestamp(  )
    {
        final String DATE = "22/09/1978";
        assertEquals( DATE, DateUtil.getDateString( DateUtil.getTimestamp( DATE ) ) );
    }

    /**
     * Tests the getDateStringDate method
     */
    public void testGetDateStringDate(  )
    {
        final int TIME = 5000;
        final java.sql.Date DATE = new java.sql.Date( TIME );

        // loss precision ==> three conversion
        assertEquals( DateUtil.getDateString( DATE ),
            DateUtil.getDateString( DateUtil.getDateSql( DateUtil.getDateString( DATE ) ) ) );
    }

    /**
     * Tests the getDateStringTimestamp method
     */
    public void testGetDateStringTimestamp(  )
    {
        final int TIME = 5000;
        final java.sql.Timestamp DATE = new java.sql.Timestamp( TIME );
        assertEquals( DateUtil.getDateString( DATE ),
            DateUtil.getDateString( DateUtil.getTimestamp( DateUtil.getDateString( DATE ) ) ) );
    }
}
