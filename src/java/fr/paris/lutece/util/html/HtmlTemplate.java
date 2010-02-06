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

////////////////////////////////////////////////////////////////////////
// HtmlTemplate.java
package fr.paris.lutece.util.html;

import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.string.StringUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * This class represents an HTML template that may include bookmarks that can be substitute by values.
 *
 * @version 1.2.5
 */
public class HtmlTemplate
{
    private String _strTemplate;

    /**
     * Constructor 1
     */
    public HtmlTemplate(  )
    {
    }

    /**
     * Constructor 2
     *
     * @param strTemplate The template as a string
     */
    public HtmlTemplate( String strTemplate )
    {
        _strTemplate = strTemplate;
    }

    /**
     * Constructor 3
     *
     * @param template Copy constructor based on another template.
     */
    public HtmlTemplate( HtmlTemplate template )
    {
        _strTemplate = template.getHtml(  );
    }

    /**
     * Load the template from a file
     *
     * @param strFilename The file name to load
     * @throws IOException  If an error occured
     */
    public void load( String strFilename ) throws IOException
    {
        FileReader fr = new FileReader( strFilename );
        BufferedReader in = new BufferedReader( fr );
        String strLine;
        _strTemplate = "";

        while ( ( strLine = in.readLine(  ) ) != null )
        {
            _strTemplate += ( strLine + "\r\n" );
        }

        in.close(  );
    }

    /**
     * Load the template from an InputStream
     *
     * @param is The open InputStream that point on the template
     * @throws IOException If an error occured
     */
    public void load( InputStream is ) throws IOException
    {
        InputStreamReader fr = new InputStreamReader( is );
        BufferedReader in = new BufferedReader( fr );
        String strLine;
        _strTemplate = "";

        while ( ( strLine = in.readLine(  ) ) != null )
        {
            _strTemplate += ( strLine + "\r\n" );
        }

        in.close(  );
        is.close(  );
    }

    /**
     * Returns the template.
     *
     * @return The template as a string.
     */
    public String getHtml(  )
    {
        return _strTemplate;
    }

    /**
     * Substitute each appearance of a bookmark by a given value.
     *
     * @param strBookmark The bookmark that must be present in the template.
     * @param strValue The value to substitute as a String.
     */
    public void substitute( String strBookmark, String strValue )
    {
        _strTemplate = StringUtil.substitute( _strTemplate, strValue, strBookmark );
    }

    /**
     * Substitute each appearance of a bookmark by a given value.
     *
     * @param strBookmark The bookmark that must be present in the template.
     * @param nValue The value to substitute as an integer.
     */
    public void substitute( String strBookmark, int nValue )
    {
        String strValue = String.valueOf( nValue );
        substitute( strBookmark, strValue );
    }

    /**
     * Substitute each appearance of a bookmark by a given value.
     *
     * @param strBookmark The bookmark that must be present in the template.
     * @param date The value to substitute as a Date.
     */
    public void substitute( String strBookmark, java.sql.Date date )
    {
        String strValue = DateUtil.getDateString( date );
        substitute( strBookmark, strValue );
    }

    /**
     * Substitute each occurence of a bookmark by a given value.
     *
     * @param strBookmark The bookmark that must be present in the template.
     * @param date The value to substitute as a Timestamp.
     */
    public void substitute( String strBookmark, java.sql.Timestamp date )
    {
        String strValue = DateUtil.getDateString( date );
        substitute( strBookmark, strValue );
    }
}
