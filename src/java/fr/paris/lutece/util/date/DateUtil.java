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
package fr.paris.lutece.util.date;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.web.l10n.LocaleService;

import java.sql.Timestamp;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;


/**
 * This class provides date utils.
 */
public final class DateUtil
{
    @Deprecated
    private static SimpleDateFormat _formatter = new SimpleDateFormat( "dd'/'MM'/'yyyy", Locale.FRANCE );
    @Deprecated
    private static SimpleDateFormat _formatterDateTime = new SimpleDateFormat( "dd'/'MM'/'yyyy' 'HH':'mm", Locale.FRANCE );
    private static final String CONSTANTE_PATTERN_DATE = "dd/MM/yyyy";
    private static final long CONSTANT_NUMBER_MILISECONDS_IN_DAY = 86400000;

    /**
     * Creates a new DateUtil object
     */
    private DateUtil(  )
    {
    }

    /**
     * Returns the date of the day in form of a String
     *
     * @return The Date of the day in a  "JJ/MM/AAAA" format
     * @deprecated Use getCurrentDateString( Locale locale )
     */
    public static synchronized String getCurrentDateString(  )
    {
        return _formatter.format( new java.util.Date(  ) );
    }

    /**
     * Converts a String date in a "jj/mm/aaaa" format in a java.sql.Date type date
     *
     * @param strDate The String Date to convert, in a date in the "jj/mm/aaaa" format
     * @return The date in form of a java.sql.Date type date
     * @deprecated Use formatDateSql( String strDate, Locale locale )
     */
    public static synchronized java.sql.Date getDateSql( String strDate )
    {
        ParsePosition pos = new ParsePosition( 0 );
        java.util.Date date = _formatter.parse( strDate, pos );

        if ( date != null )
        {
            return new java.sql.Date( date.getTime(  ) );
        }

        return null;
    }

    /**
     * Converts a String date in a "jj/mm/aaaa" format in a java.util.Date type date
     *
     * @param strDate The String Date to convert, in a date in the "jj/mm/aaaa" format
     * @return The date in form of a java.sql.Date tyep date
     * @deprecated Use formatDate( String strDate, Locale locale )
     */
    public static synchronized java.util.Date getDate( String strDate )
    {
        ParsePosition pos = new ParsePosition( 0 );
        java.util.Date date = _formatter.parse( strDate, pos );

        return date;
    }

    /**
     * Converts a String date in a "jj/mm/aaaa" format in a java.sql.Timestamp type date
     *
     * @param strDate The String Date to convert, in a date in the "jj/mm/aaaa" format
     * @return The date in form of a java.sql.Date tyep date
     * @deprecated Use formatTimestamp( String strDate, Locale locale )
     */
    public static synchronized java.sql.Timestamp getTimestamp( String strDate )
    {
        ParsePosition pos = new ParsePosition( 0 );
        java.util.Date date = _formatter.parse( strDate, pos );

        if ( date != null )
        {
            return ( new java.sql.Timestamp( date.getTime(  ) ) );
        }

        return null;
    }

    /**
     * Converts a java.sql.Date type date in a String date with a "jj/mm/aaaa" format
     *
     * @param date java.sql.Date date to convert
     * @return strDate The date converted to String in a "jj/mm/aaaa" format or an empty String if the date is null
     * @deprecated Use getDateString( Date date, Locale locale )
     */
    public static synchronized String getDateString( java.sql.Date date )
    {
        if ( date != null )
        {
            StringBuffer strDate = new StringBuffer(  );
            _formatter.format( date, strDate, new FieldPosition( 0 ) );

            return strDate.toString(  );
        }

        return "";
    }

    ///////////////////////////////////////////////////////////////////////////
    // methodes using the java.sql.Timestamp type

    /**
     * Converts une java.sql.Timestamp date in a String date in a "jj/mm/aaaa" format
     *
     * @param date java.sql.Timestamp date to convert
     * @return strDate The String date in a "jj/mm/aaaa" format or the emmpty String if the date is null
     * @deprecated Use getDateString( java.sql.Timestamp date, Locale locale )
     */
    public static synchronized String getDateString( java.sql.Timestamp date )
    {
        if ( date != null )
        {
            StringBuffer strDate = new StringBuffer(  );
            _formatter.format( date, strDate, new FieldPosition( 0 ) );

            return strDate.toString(  );
        }

        return "";
    }

    ///////////////////////////////////////////////////////////////////////////
    // methodes using the java.util.Date type

    /**
     * Converts a java.util.Date date in a String date in a "jj/mm/aaaa" format
     *
     * @param date java.util.Date date to convert
     * @return strDate A String date in a "jj/mm/aaaa" format or an empty String if the date is null
     * @deprecated Use formatDate( String strDate, Locale locale )
     */
    public static synchronized String getDateString( java.util.Date date )
    {
        if ( date != null )
        {
            StringBuffer strDate = new StringBuffer(  );
            _formatter.format( date, strDate, new FieldPosition( 0 ) );

            return strDate.toString(  );
        }

        return "";
    }

    ///////////////////////////////////////////////////////////////////////////
    // methods using a long value

    //TODO : Create a new getDateTimeString(  ) function with locale
    /**
     * Converts a long value to a String date in a "jj/mm/aaaa hh:mm" format
     *
     * @param lTime The long value to convert
     * @return The formatted string
     */
    public static synchronized String getDateTimeString( long lTime )
    {
        StringBuffer strDate = new StringBuffer(  );
        _formatterDateTime.format( new java.util.Date( lTime ), strDate, new FieldPosition( 0 ) );

        return strDate.toString(  );
    }

    /* -------------- Added in 2.2.1 ----------------- */

    /**
     * Get the date from String date
     * The format pattern is specified internaly
     * @param strDate the date to format
     * @param locale The Locale
     * @return The Date or null else
     */
    public static Date formatDate( String strDate, Locale locale )
    {
        Date date = null;

        if ( strDate != null )
        {
            DateFormat dateFormat = null;

            if ( locale != null )
            {
                String strLocalizedDateFormat = I18nService.getDateFormatShortPattern( locale );

                if ( ( strLocalizedDateFormat != null ) && !strLocalizedDateFormat.equals( "" ) )
                {
                    dateFormat = new SimpleDateFormat( strLocalizedDateFormat );
                }
                else
                {
                    dateFormat = DateFormat.getDateInstance( DateFormat.SHORT, locale );
                }
            }
            else
            {
                dateFormat = DateFormat.getDateInstance( DateFormat.SHORT, LocaleService.getDefault(  ) );
            }

            dateFormat.setLenient( false );

            try
            {
                date = dateFormat.parse( strDate );
            }
            catch ( ParseException e )
            {
                return null;
            }
        }

        return date;
    }

    /**
     * Get the date from String date
     * @param strDate the date to format
     * @param locale The Locale
     * @return The Date or null else
     */
    public static Date formatDateLongYear( String strDate, Locale locale )
    {
        Date date = null;

        if ( ( strDate != null ) && ( strDate.trim(  ).length(  ) == CONSTANTE_PATTERN_DATE.length(  ) ) )
        {
            date = formatDate( strDate, locale );
        }

        return date;
    }

    /**
     * Get the {@link Timestamp} from String date
     * The format pattern is specified internaly
     * @param strDate the date to format
     * @param locale The Locale
     * @return The {@link Timestamp} or null else
     */
    public static Timestamp formatTimestamp( String strDate, Locale locale )
    {
        Date date = formatDate( strDate, locale );

        if ( date == null )
        {
            return null;
        }

        return new Timestamp( date.getTime(  ) );
    }

    /**
     * Get the {@link java.sql.Date} from String date
     * The format pattern is specified internaly
     * @param strDate the date to format
     * @param locale The Locale
     * @return The {@link java.sql.Date} or null else
     */
    public static java.sql.Date formatDateSql( String strDate, Locale locale )
    {
        Date date = formatDate( strDate, locale );

        if ( date == null )
        {
            return null;
        }

        return new java.sql.Date( date.getTime(  ) );
    }

    /**
     * Convert the date to String with a standard pattern
     * @param date The date to convert
     * @param locale The Locale
     * @return The formated String
     */
    public static String getDateString( Date date, Locale locale )
    {
        DateFormat dateFormat = null;

        if ( locale != null )
        {
            String strLocalizedDateFormat = I18nService.getDateFormatShortPattern( locale );

            if ( ( strLocalizedDateFormat != null ) && !strLocalizedDateFormat.equals( "" ) )
            {
                dateFormat = new SimpleDateFormat( strLocalizedDateFormat );
            }
            else
            {
                dateFormat = DateFormat.getDateInstance( DateFormat.SHORT, locale );
            }
        }
        else
        {
            dateFormat = DateFormat.getDateInstance( DateFormat.SHORT, LocaleService.getDefault(  ) );
        }

        return dateFormat.format( date );
    }

    /**
     * Converts a long value to a String date
     *
     * @param lTime The long value to convert
     * @param locale the locale
     * @return The formatted string
     */
    public static String getDateString( long lTime, Locale locale )
    {
        return getDateString( new Date( lTime ), locale );
    }

    /**
     * Converts a Timestamp value to a String date
     *
     * @param date The date
     * @param locale the locale
     * @return The formatted string
     */
    public static String getDateString( java.sql.Timestamp date, Locale locale )
    {
        if ( date == null )
        {
            return "";
        }

        return getDateString( new Date( date.getTime(  ) ), locale );
    }

    /**
     * Converts the current Date to a String date
     *
     * @param locale the locale
     * @return The formatted string
     */
    public static String getCurrentDateString( Locale locale )
    {
        return getDateString( new Date(  ), locale );
    }

    /**
     * Return the pattern for date
     * @param locale the Locale
     * @return The pattern as a String
     */
    public static String getDefaultPattern( Locale locale )
    {
        if ( locale != null )
        {
            String strLocalizedDateFormat = I18nService.getDateFormatShortPattern( locale );

            if ( ( strLocalizedDateFormat != null ) && !strLocalizedDateFormat.equals( "" ) )
            {
                return strLocalizedDateFormat;
            }

            DateFormat df = DateFormat.getDateInstance( DateFormat.SHORT, locale );

            if ( df instanceof SimpleDateFormat )
            {
                SimpleDateFormat sdf = (SimpleDateFormat) df;

                return sdf.toPattern(  );
            }
        }

        DateFormat df = DateFormat.getDateInstance( DateFormat.SHORT, LocaleService.getDefault(  ) );

        if ( df instanceof SimpleDateFormat )
        {
            SimpleDateFormat sdf = (SimpleDateFormat) df;

            return sdf.toPattern(  );
        }

        return null;
    }

    /**
     * Get the number of milliseconds in a given number of days
     * @param lDays The number of days
     * @return The number of milliseconds in the given number of days
     */
    public static long convertDaysInMiliseconds( long lDays )
    {
        return CONSTANT_NUMBER_MILISECONDS_IN_DAY * lDays;
    }
}
