/*
 * Copyright (c) 2002-2026, City of Paris
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
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNERS OR CONTRIBUTORS BE
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
package fr.paris.lutece.portal.service.util;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.Property;

/**
 * Test helper: records the ERROR (and more severe) events written to the lutece.application logger while it is open.
 */
public final class ErrorLogCapture implements AutoCloseable
{
    private static final String NAME = "errorLogCapture";
    private final List<String> _listErrors = new CopyOnWriteArrayList<>( );
    private final AbstractAppender _appender;
    private final LoggerContext _context;
    private final LoggerConfig _loggerConfig;

    /**
     * Starts the capture
     */
    public ErrorLogCapture( )
    {
        _appender = new AbstractAppender( NAME, null, null, true, Property.EMPTY_ARRAY )
        {
            @Override
            public void append( LogEvent event )
            {
                if ( event.getLevel( ).isMoreSpecificThan( Level.ERROR ) )
                {
                    _listErrors.add( event.getMessage( ).getFormattedMessage( ) );
                }
            }
        };
        _appender.start( );
        _context = (LoggerContext) LogManager.getContext( false );
        _loggerConfig = _context.getConfiguration( ).getLoggerConfig( "lutece.application" );
        _loggerConfig.addAppender( _appender, Level.ALL, null );
        _context.updateLoggers( );
    }

    /**
     * @return the error messages recorded so far
     */
    public List<String> getErrors( )
    {
        return _listErrors;
    }

    @Override
    public void close( )
    {
        _loggerConfig.removeAppender( NAME );
        _context.updateLoggers( );
        _appender.stop( );
    }
}
