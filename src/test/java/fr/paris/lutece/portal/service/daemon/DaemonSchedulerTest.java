/*
 * Copyright (c) 2002-2017, Mairie de Paris
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

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import fr.paris.lutece.test.LuteceTestCase;

public class DaemonSchedulerTest extends LuteceTestCase
{
    private static final class TestExecutorService extends AbstractExecutorService
    {
        private final Consumer<Runnable> _executor;

        public TestExecutorService( Consumer<Runnable> executor )
        {
            _executor = executor;
        }

        @Override
        public void execute( Runnable command )
        {
            _executor.accept( command );
        }

        @Override
        public List<Runnable> shutdownNow( )
        {
            return null;
        }

        @Override
        public void shutdown( )
        {
        }

        @Override
        public boolean isTerminated( )
        {
            return false;
        }

        @Override
        public boolean isShutdown( )
        {
            return false;
        }

        @Override
        public boolean awaitTermination( long timeout, TimeUnit unit ) throws InterruptedException
        {
            return true;
        }
    }

    public void testEnqueue( ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, BrokenBarrierException,
            TimeoutException
    {
        testEnqueue( false );
    }

    public void testEnqueueDaemonThrows( ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException,
            BrokenBarrierException, TimeoutException
    {
        testEnqueue( true );
    }

    private void testEnqueue( boolean shouldThrow ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException,
            BrokenBarrierException, TimeoutException
    {
        BlockingQueue<DaemonEntry> queue = new LinkedBlockingQueue<>( );
        ExecutorService executor = Executors.newSingleThreadExecutor( );
        DaemonScheduler scheduler = new DaemonScheduler( queue, executor );
        try
        {
            DaemonEntry entry = getDaemonEntry( "JUNIT" );
            TestDaemon testDaemon = (TestDaemon) entry.getDaemon( );
            testDaemon.setRunThrows( shouldThrow );
            scheduler.enqueue( entry, 0L, TimeUnit.MILLISECONDS );
            assertFalse( testDaemon.hasRun( ) );
            testDaemon.go( 250L, TimeUnit.MILLISECONDS );
            testDaemon.waitForCompletion( );
            assertTrue( testDaemon.hasRun( ) );
        }
        finally
        {
            scheduler.shutdown( );
        }
    }

    public void testEnqueueDelay( ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException,
            BrokenBarrierException, TimeoutException
    {
        testEnqueueDelay( false );
    }

    public void testEnqueueDelayDaemonThrows( ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException,
            BrokenBarrierException, TimeoutException
    {
        testEnqueueDelay( true );
    }

    private void testEnqueueDelay( boolean shouldThrow ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException,
            BrokenBarrierException, TimeoutException
    {
        BlockingQueue<DaemonEntry> queue = new LinkedBlockingQueue<>( );
        ExecutorService executor = Executors.newSingleThreadExecutor( );
        DaemonScheduler scheduler = new DaemonScheduler( queue, executor );
        try
        {
            DaemonEntry entry = getDaemonEntry( "JUNIT" );
            TestDaemon testDaemon = (TestDaemon) entry.getDaemon( );
            testDaemon.setRunThrows( shouldThrow );
            Instant start = Instant.now( );
            assertTrue( scheduler.enqueue( entry, 500L, TimeUnit.MILLISECONDS ) );
            assertFalse( testDaemon.hasRun( ) );
            testDaemon.go( );
            assertTrue( 500L <= Duration.between( start, Instant.now( ) ).toMillis( ) );
            testDaemon.waitForCompletion( );
            assertTrue( testDaemon.hasRun( ) );
        }
        finally
        {
            scheduler.shutdown( );
        }
    }

    public void testEnqueueDelayIllegalState( ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException,
            BrokenBarrierException, TimeoutException
    {
        BlockingQueue<DaemonEntry> queue = new LinkedBlockingQueue<>( );
        ExecutorService executor = Executors.newSingleThreadExecutor( );
        DaemonScheduler scheduler = new DaemonScheduler( queue, executor );
        scheduler.shutdown( );
        try
        {
            DaemonEntry entry = getDaemonEntry( "JUNIT" );
            scheduler.enqueue( entry, 500L, TimeUnit.MILLISECONDS );
            fail( "Should not be able to enqueue after shutdown" );
        }
        catch ( IllegalStateException e )
        {
            // ok
        }
        finally
        {
            scheduler.shutdown( );
        }
    }

    private DaemonEntry getDaemonEntry( String name ) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        DaemonEntry entry = new DaemonEntry( );
        entry.setId( name );
        entry.setIsRunning( true );
        entry.setPluginName( "core" );
        entry.setClassName( TestDaemon.class.getName( ) );
        entry.loadDaemon( );
        entry.setInterval( 1 );
        TestDaemon testDaemon = (TestDaemon) entry.getDaemon( );
        testDaemon.setPluginName( "core" );
        return entry;
    }

    public void testEnqueueFull( ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, BrokenBarrierException,
            TimeoutException
    {
        testEnqueueFull( false );
    }

    public void testEnqueueFullDaemonThrows( ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException,
            BrokenBarrierException, TimeoutException
    {
        testEnqueueFull( true );
    }

    private void testEnqueueFull( boolean shouldThrow ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException,
            BrokenBarrierException, TimeoutException
    {
        BlockingQueue<DaemonEntry> queue = new LinkedBlockingQueue<>( 1 );
        ExecutorService executor = new TestExecutorService( runnable -> runnable.run( ) );
        DaemonScheduler scheduler = new DaemonScheduler( queue, executor );
        try
        {
            DaemonEntry executing = getDaemonEntry( "JUNIT-executing" );
            TestDaemon executingDaemon = (TestDaemon) executing.getDaemon( );
            executingDaemon.setRunThrows( shouldThrow );
            assertTrue( scheduler.enqueue( executing, 0L, TimeUnit.MILLISECONDS ) );
            executingDaemon.go( );
            DaemonEntry inqueue = getDaemonEntry( "JUNIT-inqueue" );
            TestDaemon inqueueDaemon = (TestDaemon) inqueue.getDaemon( );
            inqueueDaemon.setRunThrows( shouldThrow );
            assertTrue( scheduler.enqueue( inqueue, 0L, TimeUnit.MILLISECONDS ) );
            DaemonEntry refused = getDaemonEntry( "JUNIT-refused" );
            assertFalse( scheduler.enqueue( refused, 0L, TimeUnit.MILLISECONDS ) );
            executingDaemon.waitForCompletion( );
            inqueueDaemon.go( );
            inqueueDaemon.waitForCompletion( );
        }
        finally
        {
            scheduler.shutdown( );
        }
    }

    public void testSchedule( ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, BrokenBarrierException,
            TimeoutException
    {
        testSchedule( false );
    }

    public void testSchedulDaemonThrows( ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException,
            BrokenBarrierException, TimeoutException
    {
        testSchedule( true );
    }

    private void testSchedule( boolean shouldThrow ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException,
            BrokenBarrierException, TimeoutException
    {
        BlockingQueue<DaemonEntry> queue = new LinkedBlockingQueue<>( );
        ExecutorService executor = Executors.newSingleThreadExecutor( );
        DaemonScheduler scheduler = new DaemonScheduler( queue, executor );
        try
        {
            DaemonEntry entry = getDaemonEntry( "JUNIT" );
            TestDaemon testDaemon = (TestDaemon) entry.getDaemon( );
            testDaemon.setRunThrows( shouldThrow );
            Instant start = Instant.now( );
            scheduler.schedule( entry, 0L, TimeUnit.MILLISECONDS );
            assertFalse( testDaemon.hasRun( ) );
            testDaemon.go( );
            System.out.println( "Daemon took " + Duration.between( start, Instant.now( ) ).toNanos( ) + "ns to execute" );
            testDaemon.waitForCompletion( );
            assertTrue( testDaemon.hasRun( ) );
            testDaemon.go( );
            assertTrue( 1000L <= Duration.between( start, Instant.now( ) ).toMillis( ) );
            testDaemon.waitForCompletion( );
            assertTrue( testDaemon.hasRun( ) );
        }
        finally
        {
            scheduler.shutdown( );
        }
    }

    public void testScheduleDelay( ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException,
            BrokenBarrierException, TimeoutException
    {
        testScheduleDelay( false );
    }

    public void testScheduleDelayDaemonThrows( ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException,
            BrokenBarrierException, TimeoutException
    {
        testScheduleDelay( true );
    }

    private void testScheduleDelay( boolean shouldThrow ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException,
            BrokenBarrierException, TimeoutException
    {
        BlockingQueue<DaemonEntry> queue = new LinkedBlockingQueue<>( );
        ExecutorService executor = Executors.newSingleThreadExecutor( );
        DaemonScheduler scheduler = new DaemonScheduler( queue, executor );
        try
        {
            DaemonEntry entry = getDaemonEntry( "JUNIT" );
            TestDaemon testDaemon = (TestDaemon) entry.getDaemon( );
            testDaemon.setRunThrows( shouldThrow );
            Instant start = Instant.now( );
            scheduler.schedule( entry, 500L, TimeUnit.MILLISECONDS );
            assertFalse( testDaemon.hasRun( ) );
            testDaemon.go( );
            assertTrue( 500L <= Duration.between( start, Instant.now( ) ).toMillis( ) );
            testDaemon.waitForCompletion( );
            assertTrue( testDaemon.hasRun( ) );
        }
        finally
        {
            scheduler.shutdown( );
        }
    }

    public void testScheduleTwice( ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException,
            BrokenBarrierException, TimeoutException
    {
        testScheduleTwice( false );
    }

    public void testScheduleTwiceDaemonThrows( ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException,
            BrokenBarrierException, TimeoutException
    {
        testScheduleTwice( true );
    }

    private void testScheduleTwice( boolean shouldThrow ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException,
            BrokenBarrierException, TimeoutException
    {
        BlockingQueue<DaemonEntry> queue = new LinkedBlockingQueue<>( );
        ExecutorService executor = Executors.newSingleThreadExecutor( );
        DaemonScheduler scheduler = new DaemonScheduler( queue, executor );
        try
        {
            DaemonEntry entry = getDaemonEntry( "JUNIT" );
            TestDaemon testDaemon = (TestDaemon) entry.getDaemon( );
            testDaemon.setRunThrows( shouldThrow );
            Instant start = Instant.now( );
            scheduler.schedule( entry, 0L, TimeUnit.MICROSECONDS );
            scheduler.schedule( entry, 500L, TimeUnit.MILLISECONDS );
            assertFalse( testDaemon.hasRun( ) );
            testDaemon.go( );
            testDaemon.waitForCompletion( );
            assertTrue( testDaemon.hasRun( ) );
            testDaemon.go( );
            long timeForSecondRun = Duration.between( start, Instant.now( ) ).toMillis( );
            assertTrue( "Second run was " + timeForSecondRun + "ms after start", 1000L <= timeForSecondRun );
            testDaemon.waitForCompletion( );
            assertTrue( testDaemon.hasRun( ) );
        }
        finally
        {
            scheduler.shutdown( );
        }
    }

    public void testUnScheduleNotScheduled( ) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        BlockingQueue<DaemonEntry> queue = new LinkedBlockingQueue<>( );
        ExecutorService executor = Executors.newSingleThreadExecutor( );
        DaemonScheduler scheduler = new DaemonScheduler( queue, executor );
        try
        {
            DaemonEntry entry = getDaemonEntry( "JUNIT" );
            scheduler.unSchedule( entry );
            // not sure how to assert something here
        }
        catch (Exception e) {
            fail( );
        }
        finally
        {
            scheduler.shutdown( );
        }
    }

    public void testUnSchedule( ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, BrokenBarrierException,
            TimeoutException
    {
        testUnSchedule( false );
    }

    public void testUnScheduleDaemonThrows( ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException,
            BrokenBarrierException, TimeoutException
    {
        testUnSchedule( true );
    }

    private void testUnSchedule( boolean shouldThrow ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException,
            BrokenBarrierException, TimeoutException
    {
        BlockingQueue<DaemonEntry> queue = new LinkedBlockingQueue<>( );
        ExecutorService executor = Executors.newSingleThreadExecutor( );
        DaemonScheduler scheduler = new DaemonScheduler( queue, executor );
        try
        {
            DaemonEntry entry = getDaemonEntry( "JUNIT" );
            TestDaemon testDaemon = (TestDaemon) entry.getDaemon( );
            testDaemon.setRunThrows( shouldThrow );
            scheduler.schedule( entry, 0L, TimeUnit.MILLISECONDS );
            assertFalse( testDaemon.hasRun( ) );
            testDaemon.go( );
            testDaemon.waitForCompletion( );
            assertTrue( testDaemon.hasRun( ) );
            assertEquals(0, testDaemon.getStopCallNumber( ) );
            Thread.sleep( 10L ); // leave some time to the daemon to exit executing daemons
            scheduler.unSchedule( entry );
            assertEquals(1, testDaemon.getStopCallNumber( ) );
            try
            {
                testDaemon.go( 1100L, TimeUnit.MILLISECONDS );
                fail( "Daemon executed after unscheduling" );
            }
            catch( TimeoutException e )
            {
                // OK
            }
        }
        finally
        {
            scheduler.shutdown( );
        }
    }

    public void testUnScheduleWhileRunning( ) throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            InterruptedException, BrokenBarrierException, TimeoutException
    {
        testUnScheduleWhileRunning( false );
    }

    public void testUnScheduleWhileRunningDaemonThrows( ) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, InterruptedException, BrokenBarrierException, TimeoutException
    {
        testUnScheduleWhileRunning( true );
    }

    private void testUnScheduleWhileRunning( boolean shouldThrow ) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, InterruptedException, BrokenBarrierException, TimeoutException
    {
        BlockingQueue<DaemonEntry> queue = new LinkedBlockingQueue<>( );
        ExecutorService executor = Executors.newSingleThreadExecutor( );
        DaemonScheduler scheduler = new DaemonScheduler( queue, executor );
        try
        {
            DaemonEntry entry = getDaemonEntry( "JUNIT" );
            TestDaemon testDaemon = ( TestDaemon ) entry.getDaemon( );
            testDaemon.setRunThrows( shouldThrow );
            scheduler.schedule( entry, 0L, TimeUnit.MILLISECONDS );
            assertFalse( testDaemon.hasRun( ) );
            testDaemon.go( );
            assertEquals(0, testDaemon.getStopCallNumber( ) );
            // unschedule while the daemon is executing
            scheduler.unSchedule( entry );
            Thread.sleep( 10L ); // leave some time to the daemon
            assertEquals("Stopping should wait for execution end", 0, testDaemon.getStopCallNumber( ) );
            testDaemon.waitForCompletion( );
            assertTrue( testDaemon.hasRun( ) );
            Thread.sleep( 10L ); // leave some time to the daemon to exit executing daemons
            assertEquals(1, testDaemon.getStopCallNumber( ) );
            try
            {
                testDaemon.go( 1100L, TimeUnit.MILLISECONDS );
                fail( "Daemon executed after unscheduling" );
            }
            catch ( TimeoutException e )
            {
                // OK
            }
        }
        finally
        {
            scheduler.shutdown( );
        }
    }

    public void testEnqueueWhileRunning( ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException,
            BrokenBarrierException, TimeoutException
    {
        testEnqueueWhileRunning( false );
    }

    public void testEnqueueWhileRunningDaemonThrows( ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException,
            BrokenBarrierException, TimeoutException
    {
        testEnqueueWhileRunning( true );
    }

    private void testEnqueueWhileRunning( boolean shouldThrow ) throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            InterruptedException, BrokenBarrierException, TimeoutException
    {
        BlockingQueue<DaemonEntry> queue = new LinkedBlockingQueue<>( );
        ExecutorService executor = Executors.newCachedThreadPool( );
        DaemonScheduler scheduler = new DaemonScheduler( queue, executor );
        try
        {
            DaemonEntry executing = getDaemonEntry( "JUNIT-executing" );
            TestDaemon executingDaemon = (TestDaemon) executing.getDaemon( );
            executingDaemon.setRunThrows( shouldThrow );
            assertTrue( scheduler.enqueue( executing, 0L, TimeUnit.MILLISECONDS ) );
            executingDaemon.go( );
            assertTrue( scheduler.enqueue( executing, 0L, TimeUnit.MILLISECONDS ) );
            try
            {
                executingDaemon.go( 200, TimeUnit.MILLISECONDS );
                fail( "Daemon started execution while already running" );
            }
            catch( TimeoutException e )
            {
                executingDaemon.resetGo( );
            }
            executingDaemon.waitForCompletion( );
            executingDaemon.go( );
            executingDaemon.waitForCompletion( );
        }
        finally
        {
            scheduler.shutdown( );
        }
    }

    public void testEnqueueCoalesce( ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException,
            BrokenBarrierException, TimeoutException
    {
        testEnqueueCoalesce( false );
    }

    public void testEnqueueCoalesceDaemonThrows( ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException,
            BrokenBarrierException, TimeoutException
    {
        testEnqueueCoalesce( true );
    }

    private void testEnqueueCoalesce( boolean shouldThrow ) throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            InterruptedException, BrokenBarrierException, TimeoutException
    {
        BlockingQueue<DaemonEntry> queue = new LinkedBlockingQueue<>( );
        ExecutorService executor = new TestExecutorService( runnable -> runnable.run( ) );
        DaemonScheduler scheduler = new DaemonScheduler( queue, executor );
        try
        {
            DaemonEntry executing = getDaemonEntry( "JUNIT-executing" );
            TestDaemon executingDaemon = (TestDaemon) executing.getDaemon( );
            executingDaemon.setRunThrows( shouldThrow );
            assertTrue( scheduler.enqueue( executing, 0L, TimeUnit.MILLISECONDS ) );
            executingDaemon.go( );
            DaemonEntry inqueue = getDaemonEntry( "JUNIT-inqueue" );
            TestDaemon inqueueDaemon = (TestDaemon) inqueue.getDaemon( );
            inqueueDaemon.setRunThrows( shouldThrow );
            assertTrue( scheduler.enqueue( inqueue, 0L, TimeUnit.MILLISECONDS ) );
            assertTrue( scheduler.enqueue( inqueue, 0L, TimeUnit.MILLISECONDS ) );
            executingDaemon.waitForCompletion( );
            inqueueDaemon.go( );
            inqueueDaemon.waitForCompletion( );
            try
            {
                inqueueDaemon.go( 200, TimeUnit.MILLISECONDS );
                fail( "Daemon started twice but should have been coalesced" );
            }
            catch( TimeoutException e )
            {

            }
        }
        finally
        {
            scheduler.shutdown( );
        }
    }

    public void testShutdown( ) throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            InterruptedException, BrokenBarrierException, TimeoutException
    {
        testShutdown( false );
    }

    public void testShutdownThrows( ) throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            InterruptedException, BrokenBarrierException, TimeoutException
    {
        testShutdown( true );
    }

    private void testShutdown( boolean shouldThrow ) throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            InterruptedException, BrokenBarrierException, TimeoutException
    {
        BlockingQueue<DaemonEntry> queue = new LinkedBlockingQueue<>( );
        ExecutorService executor = Executors.newCachedThreadPool( );
        DaemonScheduler scheduler = new DaemonScheduler( queue, executor );
        try
        {
            DaemonEntry entry = getDaemonEntry( "JUNIT" );
            TestDaemon testDaemon = ( TestDaemon ) entry.getDaemon( );
            testDaemon.setStopThrows( shouldThrow );
            scheduler.schedule( entry, 0L, TimeUnit.MILLISECONDS );
            DaemonEntry entry2 = getDaemonEntry( "JUNIT2" );
            TestDaemon testDaemon2 = ( TestDaemon ) entry2.getDaemon( );
            testDaemon2.setStopThrows( shouldThrow );
            scheduler.schedule( entry2, 0L, TimeUnit.MILLISECONDS );
            assertFalse( testDaemon.hasRun( ) );
            assertFalse( testDaemon2.hasRun( ) );
            testDaemon.go( 250L, TimeUnit.MILLISECONDS );
            testDaemon2.go( 250L, TimeUnit.MILLISECONDS );
            testDaemon.waitForCompletion( );
            testDaemon2.waitForCompletion( );
            assertTrue( testDaemon.hasRun( ) );
            assertTrue( testDaemon2.hasRun( ) );
            Thread.sleep( 10L ); // leave some time to the daemons to exit
                                 // executing daemons
            scheduler.shutdown( );
            assertEquals( 1, testDaemon.getStopCallNumber( ) );
            assertEquals( 1, testDaemon2.getStopCallNumber( ) );
        }
        finally
        {
            scheduler.shutdown( );
        }
    }

    public void testShutdownWhileRunning( ) throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            InterruptedException, BrokenBarrierException, TimeoutException
    {
        testShutdownWhileRunning( false );
    }

    public void testShutdownWhileRunningThrows( ) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, InterruptedException, BrokenBarrierException, TimeoutException
    {
        testShutdownWhileRunning( true );
    }

    private void testShutdownWhileRunning( boolean shouldThrow ) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, InterruptedException, BrokenBarrierException, TimeoutException
    {
        BlockingQueue<DaemonEntry> queue = new LinkedBlockingQueue<>( );
        ExecutorService executor = new TestExecutorService(
                runnable -> ForkJoinPool.commonPool( ).execute( runnable ) );
        DaemonScheduler scheduler = new DaemonScheduler( queue, executor );
        try
        {
            DaemonEntry entry = getDaemonEntry( "JUNIT" );
            TestDaemon testDaemon = ( TestDaemon ) entry.getDaemon( );
            testDaemon.setStopThrows( shouldThrow );
            scheduler.schedule( entry, 0L, TimeUnit.MILLISECONDS );
            assertFalse( testDaemon.hasRun( ) );
            testDaemon.go( 250L, TimeUnit.MILLISECONDS );
            scheduler.shutdown( );
            assertEquals( 0, testDaemon.getStopCallNumber( ) );
            testDaemon.waitForCompletion( );
            assertTrue( testDaemon.hasRun( ) );
            Thread.sleep( 10L ); // leave some time to the daemon to exit
                                 // executing daemons
            assertEquals( 1, testDaemon.getStopCallNumber( ) );
        }
        finally
        {
            scheduler.shutdown( );
        }
    }
}
