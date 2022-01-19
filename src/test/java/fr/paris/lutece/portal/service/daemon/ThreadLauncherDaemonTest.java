/*
 * Copyright (c) 2002-2022, City of Paris
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

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.test.LuteceTestCase;

public class ThreadLauncherDaemonTest extends LuteceTestCase
{
    private static final long TIMEOUT_DURATION = 10L;
    private static final TimeUnit TIMEOUT_TIMEUNIT = TimeUnit.SECONDS;
    private boolean _runnableTimedOut;
    private Boolean _bThreadLauncherDaemonInitialState;
    private DaemonEntry _threadLauncherDaemonEntry;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        // we ensure the ThreadLauncherDeamon is started
        AppLogService.info( "Ensure ThreadLauncherDeamon is started" );
        for ( DaemonEntry daemonEntry : AppDaemonService.getDaemonEntries( ) )
        {
            if ( daemonEntry.getId( ).equals( "threadLauncherDaemon" ) )
            {
                _threadLauncherDaemonEntry = daemonEntry;
                _bThreadLauncherDaemonInitialState = daemonEntry.isRunning( );
                break;
            }
        }
        assertNotNull( "Did not find threadLauncherDaemon daemon", _bThreadLauncherDaemonInitialState );
        AppDaemonService.startDaemon( "threadLauncherDaemon" );
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        // restore threadLauncherDaemon state
        AppLogService.info( "restore threadLauncherDaemon state ( {} )", _bThreadLauncherDaemonInitialState );
        if ( !_bThreadLauncherDaemonInitialState.booleanValue( ) )
        {
            AppDaemonService.stopDaemon( "threadLauncherDaemon" );
        }
        super.tearDown( );
    }

    public void testAddItemToQueue( ) throws InterruptedException, BrokenBarrierException, TimeoutException
    {
        CyclicBarrier barrier = new CyclicBarrier( 2 );
        _runnableTimedOut = false;

        dumpStateWhileWaiting( 0L ); // for debugging test failure

        Instant start = Instant.now( );
        ThreadLauncherDaemon.addItemToQueue( ( ) -> {
            try
            {
                AppLogService.info( "testAddItemToQueue: Inside the task, going to await" );
                barrier.await( TIMEOUT_DURATION, TIMEOUT_TIMEUNIT );
            }
            catch( InterruptedException | BrokenBarrierException | TimeoutException e )
            {
                _runnableTimedOut = true;
            }
        }, "key", PluginService.getCore( ) );

        dumpStateWhileWaiting( 500L ); // for debugging test failure

        barrier.await( TIMEOUT_DURATION, TIMEOUT_TIMEUNIT );
        AppLogService.info( "ThreadLauncherDaemonTest#testAddItemToQueue : task executed after {} ms",
                ( ) -> Duration.between( start, Instant.now( ) ).toMillis( ) );
        AppLogService.info( "Last Run Logs : {}", _threadLauncherDaemonEntry.getLastRunLogs( ) );
        assertFalse( _runnableTimedOut );
    }

    private void dumpStateWhileWaiting( long lWait ) throws InterruptedException
    {
        // wait for the daemon to have a chance to try running
        Thread.sleep( lWait );
        final StringBuilder dump = new StringBuilder( );
        final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean( );
        final ThreadInfo [ ] threadInfos = threadMXBean.getThreadInfo( threadMXBean.getAllThreadIds( ), 100 );
        for ( ThreadInfo threadInfo : threadInfos )
        {
            dump.append( '"' );
            dump.append( threadInfo.getThreadName( ) );
            dump.append( "\" " );
            final Thread.State state = threadInfo.getThreadState( );
            dump.append( "\n   java.lang.Thread.State: " );
            dump.append( state );
            final StackTraceElement [ ] stackTraceElements = threadInfo.getStackTrace( );
            for ( final StackTraceElement stackTraceElement : stackTraceElements )
            {
                dump.append( "\n        at " );
                dump.append( stackTraceElement );
            }
            dump.append( "\n\n" );
        }
        AppLogService.info( "Current state : {}", dump );
    }
}
