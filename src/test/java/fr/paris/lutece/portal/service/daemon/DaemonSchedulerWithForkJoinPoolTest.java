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

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.service.daemon.mocks.ExecutorServiceForDaemonTests;
import fr.paris.lutece.portal.service.daemon.mocks.TestExecutorService;
import fr.paris.lutece.test.LuteceTestCase;
import jakarta.enterprise.concurrent.ManagedScheduledExecutorService;
import jakarta.enterprise.concurrent.ManagedThreadFactory;
import jakarta.inject.Inject;

public class DaemonSchedulerWithForkJoinPoolTest extends LuteceTestCase
{
    private @Inject ManagedThreadFactory _managedThreadFactory;
    private @Inject ManagedScheduledExecutorService _managedScheduledExecutorService;
    
    @Test
    public void testShutdownWhileRunning()
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, BrokenBarrierException, TimeoutException
    {
        testShutdownWhileRunning(false);
    }

    @Test
    public void testShutdownWhileRunningThrows()
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, BrokenBarrierException, TimeoutException
    {
        testShutdownWhileRunning(true);
    }

    /**
     * test if the shutdown while running service does not fail when a daemon throws a RunTimeException
     *
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InterruptedException
     * @throws BrokenBarrierException
     * @throws TimeoutException
     */
    private void testShutdownWhileRunning(boolean shouldThrow)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, BrokenBarrierException, TimeoutException
    {
        String strMethodName = new Object()
        {
        }.getClass().getEnclosingMethod().getName();
        DaemonScheduler scheduler = buildDaemonScheduler();
        try
        {
            DaemonEntry entry = TestDaemon.makeDaemonEntry("JUNIT" + strMethodName + shouldThrow);
            TestDaemon testDaemon = (TestDaemon) entry.getDaemon();
            testDaemon.setStopThrows(shouldThrow);
            scheduler.schedule(entry, 0L, TimeUnit.MILLISECONDS);
            assertFalse(testDaemon.hasRun());
            testDaemon.go(250L, TimeUnit.MILLISECONDS);
            scheduler.shutdown();
            assertEquals(0, testDaemon.getStopCallNumber());
            testDaemon.waitForCompletion();
            assertTrue(testDaemon.hasRun());
            Thread.sleep(10L); // leave some time to the daemon to exit
                               // executing daemons
            assertEquals(1, testDaemon.getStopCallNumber());
        } catch (InterruptedException | BrokenBarrierException | TimeoutException | InvocationTargetException | NoSuchMethodException e  )
        {
            fail(e.getMessage());
        } finally
        {
            scheduler.shutdown();
        }
    }
    
    /**
     * Build DaemonScheduler
     * 
     * @return
     */
    private DaemonScheduler buildDaemonScheduler( )
    {
        BlockingQueue<DaemonEntry> queue = new LinkedBlockingQueue<>( );
        ExecutorService executor = new ExecutorServiceForDaemonTests(
                ( ) -> new TestExecutorService( runnable -> ForkJoinPool.commonPool( ).execute( runnable ) ) );
        DaemonScheduler scheduler = new DaemonScheduler( );
        scheduler.initDaemonScheduler( queue, executor, _managedThreadFactory, _managedScheduledExecutorService );
        return scheduler;
    }
}
