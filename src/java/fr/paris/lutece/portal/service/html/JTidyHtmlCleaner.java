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
package fr.paris.lutece.portal.service.html;

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import org.w3c.tidy.Tidy;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;


/**
 *
 * This class is an implementation of IHtmlCleaner using the JTidy library
 *
 */
public class JTidyHtmlCleaner implements IHtmlCleaner
{
    private static final String PROPERTY_JTIDY_FILE_PATH = "file.jtidy.properties";
    private static String _strContent;
    private static Tidy _tidy;

    /**
     * {@inheritDoc}
     */
    public String clean( String strSource ) throws HtmlCleanerException
    {
        String strCleanedSource = strSource;

        String strOutput = "";

        StringReader sr = new StringReader( strCleanedSource );
        StringWriter sw = new StringWriter(  );
        //      Convert to XHTML using Tidy
        _tidy.parse( sr, sw );

        _strContent = strCleanedSource;
        strOutput = sw.toString(  );

        // Verify the content of html editor after using tidy
        if ( _strContent.length(  ) != strOutput.length(  ) )
        {
            if ( strOutput.length(  ) == 0 )
            {
                throw new HtmlCleanerException(  );
            }
        }

        sr.close(  );
        sw.flush(  );

        try
        {
            sw.close(  );
        }
        catch ( IOException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }

        return strOutput;
    }

    /**
     * {@inheritDoc}
     */
    public void init(  )
    {
        _tidy = new Tidy(  );
        _tidy.setConfigurationFromFile( AppPropertiesService.getProperty( PROPERTY_JTIDY_FILE_PATH ) );
    }
}
