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
package fr.paris.lutece.util.annotation;

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;

import org.scannotation.AnnotationDB;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import java.lang.annotation.Annotation;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Uses {@link AnnotationDB} from scannotation.
 * @see #init()
 */
public class ScannotationDB implements IAnnotationDB
{
    private static final String CONSTANT_WEB_INF_LIB = "/WEB-INF/lib/";
    private static final String CONSTANT_WEB_INF_CLASS = "/WEB-INF/classes/";

    /**
     * Default filename filter matches <strong>ONLY</strong> lutece jars.
     */
    private static final String CONSTANT_DEFAULT_FILENAME_FILTER = "(plugin-.*\\.jar)|(module-.*\\.jar)|(lutece-core-.*\\.jar)|(library-.*\\.jar)";
    private final AnnotationDB _db;
    private String _strFileFilter;

    /**
     * Constructor.
     */
    public ScannotationDB(  )
    {
        _db = new AnnotationDB(  );
    }

    /**
     * Gets the filename filter
     * @return the filename filter
     */
    public String getFileFilter(  )
    {
        return _strFileFilter;
    }

    /**
     * Sets the filename filter
     * @param strFileFilter the filename filter
     */
    public void setFileFilter( String strFileFilter )
    {
        _strFileFilter = strFileFilter;
    }

    /**
     * Scans the WEB-INF/classes and WEB-INF/lib using {@link #getFileFilter()}
     * to filter jars.
     */
    @Override
    public void init(  )
    {
        AppLogService.info( "ScannotationDB Scanning classpath..." );

        if ( getFileFilter(  ) == null )
        {
            setFileFilter( CONSTANT_DEFAULT_FILENAME_FILTER );
            AppLogService.info( "Using default filename filter" );
        }
        else
        {
            AppLogService.info( "Using " + getFileFilter(  ) + " as filename filter" );
        }

        Date start = new Date(  );
        File libDirectory = new File( AppPathService.getWebAppPath(  ) + CONSTANT_WEB_INF_LIB );

        String[] allJars = libDirectory.list( new FilenameFilter(  )
                {
                    /**
                     * {@inheritDoc}
                     */
                    @Override
                    public boolean accept( File dir, String name )
                    {
                        return name.matches( _strFileFilter );
                    }
                } );

        for ( String strJar : allJars )
        {
            try
            {
                if ( AppLogService.isDebugEnabled(  ) )
                {
                    AppLogService.debug( "Scanning " + strJar );
                }

                _db.scanArchives( new URL( "file:///" + AppPathService.getWebAppPath(  ) + CONSTANT_WEB_INF_LIB +
                        strJar ) );
            }
            catch ( MalformedURLException e )
            {
                AppLogService.error( e.getMessage(  ), e );
            }
            catch ( IOException e )
            {
                AppLogService.error( e.getMessage(  ), e );
            }
        }

        AppLogService.info( "ScannotationDB WEB-INF/lib scanned" );

        try
        {
            _db.scanArchives( new URL( "file:///" + AppPathService.getWebAppPath(  ) + CONSTANT_WEB_INF_CLASS ) );
        }
        catch ( MalformedURLException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
        catch ( IOException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }

        AppLogService.info( "ScannotationDB Classpath scanned in " + ( new Date(  ).getTime(  ) - start.getTime(  ) ) +
            "ms" );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getClassesName( Class<?extends Annotation> annotationType )
    {
        return getClassesName( annotationType.getName(  ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getClassesName( String strAnnotationType )
    {
        Map<String, Set<String>> index = _db.getAnnotationIndex(  );

        Set<String> setClasses = index.get( strAnnotationType );

        if ( setClasses == null )
        {
            setClasses = new HashSet<String>(  );
        }

        return setClasses;
    }
}
