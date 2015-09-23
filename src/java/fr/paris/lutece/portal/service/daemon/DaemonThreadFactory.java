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
package fr.paris.lutece.portal.service.daemon;

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


/**
 * This class provides a method that creates new threads on demand to run daemon task
 */
public final class DaemonThreadFactory implements ThreadFactory
{
    private static final String PROPERTY_RUN_THREAD_AS_DAEMON = "daemon.runThreadAsDaemon";
    private static final boolean RUN_THREAD_AS_DAEMON = Boolean.valueOf( AppPropertiesService.getProperty( 
                PROPERTY_RUN_THREAD_AS_DAEMON, "0" ) );
    private static final ThreadFactory _defaultThreadFactory = Executors.defaultThreadFactory(  );
    private static final String DAEMONS_NAME_PREFIX = "Lutece-DaemonsPool-Thread-";
    private static int _nIndex = 1;

    /**
     * Constructs a new <tt>Thread</tt> with priority and daemon status
     *
    * @param runnable a runnable to be executed by new thread instance
    * @return constructed thread
     */
    @Override
    public Thread newThread( Runnable runnable )
    {
        Thread thread = _defaultThreadFactory.newThread( runnable );
        thread.setDaemon( RUN_THREAD_AS_DAEMON );
        thread.setPriority( Thread.MIN_PRIORITY );
        thread.setName( DAEMONS_NAME_PREFIX + _nIndex );
        AppLogService.info( "New Scheduled Thread added to the pool for daemons. Index:" + _nIndex );
        _nIndex++;

        return thread;
    }
}
