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
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.service.daemon.mocks.ExecutorServiceForDaemonTests;
import fr.paris.lutece.test.LuteceTestCase;
import jakarta.enterprise.concurrent.ManagedScheduledExecutorService;
import jakarta.enterprise.concurrent.ManagedThreadFactory;
import jakarta.inject.Inject;

public class DaemonSchedulerWithSingleThreadExecutorTest extends LuteceTestCase
{
    private @Inject ManagedThreadFactory _managedThreadFactory;
    private @Inject ManagedScheduledExecutorService _managedScheduledExecutorService;
    
    @Test
    public void testEnqueue()
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, BrokenBarrierException, TimeoutException
    {
        testEnqueue(false);
    }

    @Test
    public void testEnqueueDaemonThrows()
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, BrokenBarrierException, TimeoutException
    {
        testEnqueue(true);
    }

    /**
     * test if the enqueue service does not fail when a daemon throws a RunTimeException
     *
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InterruptedException
     * @throws BrokenBarrierException
     * @throws TimeoutException
     */
    private void testEnqueue(boolean shouldThrow)
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
            testDaemon.setRunThrows(shouldThrow);
            scheduler.enqueue(entry, 0L, TimeUnit.MILLISECONDS);
            assertFalse(testDaemon.hasRun());
            testDaemon.go(250L, TimeUnit.MILLISECONDS);
            testDaemon.waitForCompletion();
            assertTrue(testDaemon.hasRun());
        } catch (InterruptedException | BrokenBarrierException | TimeoutException | InvocationTargetException | NoSuchMethodException e )
        {
            fail(e.getMessage());
        } finally
        {
            scheduler.shutdown();
        }
    }

    @Test
    public void testEnqueueDelay()
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, BrokenBarrierException, TimeoutException
    {
        testEnqueueDelay(false);
    }

    @Test
    public void testEnqueueDelayDaemonThrows()
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, BrokenBarrierException, TimeoutException
    {
        testEnqueueDelay(true);
    }

    /**
     * test if the enqueue delay service does not fail when a daemon throws a RunTimeException
     *
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InterruptedException
     * @throws BrokenBarrierException
     * @throws TimeoutException
     */
    private void testEnqueueDelay(boolean shouldThrow)
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
            testDaemon.setRunThrows(shouldThrow);
            Instant start = Instant.now();
            assertTrue(scheduler.enqueue(entry, 500L, TimeUnit.MILLISECONDS));
            assertFalse(testDaemon.hasRun());
            testDaemon.go();
            assertTrue(500L <= Duration.between(start, Instant.now()).toMillis());
            testDaemon.waitForCompletion();
            assertTrue(testDaemon.hasRun());
        } catch (InterruptedException | BrokenBarrierException | TimeoutException | InvocationTargetException | NoSuchMethodException e )
        {
            fail(e.getMessage());
        } finally
        {
            scheduler.shutdown();
        }
    }

    /**
     * test if the enqueue delay illegalstate exception service does not fail when a daemon throws a RunTimeException
     *
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InterruptedException
     * @throws BrokenBarrierException
     * @throws TimeoutException
     */
    @Test
    public void testEnqueueDelayIllegalState()
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, BrokenBarrierException, TimeoutException,
                   InvocationTargetException, NoSuchMethodException
    {
        String strMethodName = new Object()
        {
        }.getClass().getEnclosingMethod().getName();
        DaemonScheduler scheduler = buildDaemonScheduler();
        scheduler.shutdown();
        try
        {
            DaemonEntry entry = TestDaemon.makeDaemonEntry("JUNIT" + strMethodName);
            scheduler.enqueue(entry, 500L, TimeUnit.MILLISECONDS);
            fail("Should not be able to enqueue after shutdown");
        } catch (IllegalStateException e)
        {
            // ok
        } finally
        {
            scheduler.shutdown();
        }
    }

    @Test
    public void testSchedule()
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, BrokenBarrierException, TimeoutException
    {
        testSchedule(false);
    }

    @Test
    public void testSchedulDaemonThrows()
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, BrokenBarrierException, TimeoutException
    {
        testSchedule(true);
    }

    /**
     * test if the schedule service does not fail when a daemon throws a RunTimeException
     *
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InterruptedException
     * @throws BrokenBarrierException
     * @throws TimeoutException
     */
    private void testSchedule(boolean shouldThrow)
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
            testDaemon.setRunThrows(shouldThrow);
            Instant start = Instant.now();
            scheduler.schedule(entry, 0L, TimeUnit.MILLISECONDS);
            assertFalse(testDaemon.hasRun());
            testDaemon.go();
            System.out.println("Daemon took " + Duration.between(start, Instant.now()).toNanos() + "ns to execute");
            testDaemon.waitForCompletion();
            assertTrue(testDaemon.hasRun());
            testDaemon.go();
            assertTrue(9000L <= Duration.between(start, Instant.now()).toMillis());
            testDaemon.waitForCompletion();
            assertTrue(testDaemon.hasRun());
        } catch (InterruptedException | BrokenBarrierException | TimeoutException | InvocationTargetException | NoSuchMethodException e )
        {
            fail(e.getMessage());
        } finally
        {
            scheduler.shutdown();
        }
    }

    @Test
    public void testScheduleDelay()
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, BrokenBarrierException, TimeoutException
    {
        testScheduleDelay(false);
    }

    @Test
    public void testScheduleDelayDaemonThrows()
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, BrokenBarrierException, TimeoutException
    {
        testScheduleDelay(true);
    }

    /**
     * test if the schedule delay service does not fail when a daemon throws a RunTimeException
     *
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InterruptedException
     * @throws BrokenBarrierException
     * @throws TimeoutException
     */
    private void testScheduleDelay(boolean shouldThrow)
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
            testDaemon.setRunThrows(shouldThrow);
            Instant start = Instant.now();
            scheduler.schedule(entry, 500L, TimeUnit.MILLISECONDS);
            assertFalse(testDaemon.hasRun());
            testDaemon.go();
            assertTrue(500L <= Duration.between(start, Instant.now()).toMillis());
            testDaemon.waitForCompletion();
            assertTrue(testDaemon.hasRun());
        } catch (InterruptedException | BrokenBarrierException | TimeoutException | InvocationTargetException | NoSuchMethodException e )
        {
            fail(e.getMessage());
        } finally
        {
            scheduler.shutdown();
        }
    }

    @Test
    public void testScheduleTwice()
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, BrokenBarrierException, TimeoutException
    {
        testScheduleTwice(false);
    }

    @Test
    public void testScheduleTwiceDaemonThrows()
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, BrokenBarrierException, TimeoutException
    {
        testScheduleTwice(true);
    }

    /**
     * test if the schedule twice service does not fail when a daemon throws a RunTimeException
     *
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InterruptedException
     * @throws BrokenBarrierException
     * @throws TimeoutException
     */
    private void testScheduleTwice(boolean shouldThrow)
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
            testDaemon.setRunThrows(shouldThrow);
            Instant start = Instant.now();
            scheduler.schedule(entry, 0L, TimeUnit.MICROSECONDS);
            scheduler.schedule(entry, 500L, TimeUnit.MILLISECONDS);
            assertFalse(testDaemon.hasRun());
            testDaemon.go();
            testDaemon.waitForCompletion();
            assertTrue(testDaemon.hasRun());
            testDaemon.go();
            long timeForSecondRun = Duration.between(start, Instant.now()).toMillis();
            assertTrue(1000L <= timeForSecondRun, "Second run was " + timeForSecondRun + "ms after start");
            testDaemon.waitForCompletion();
            assertTrue(testDaemon.hasRun());
        } catch (InterruptedException | BrokenBarrierException | TimeoutException | InvocationTargetException | NoSuchMethodException e )
        {
            fail(e.getMessage());
        } finally
        {
            scheduler.shutdown();
        }
    }

    /**
     * test if the unschedule not scheduled service does not fail when a daemon throws a RunTimeException
     *
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InterruptedException
     * @throws BrokenBarrierException
     * @throws TimeoutException
     */
    @Test
    public void testUnScheduleNotScheduled() throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String strMethodName = new Object()
        {
        }.getClass().getEnclosingMethod().getName();
        DaemonScheduler scheduler = buildDaemonScheduler();
        try
        {
            DaemonEntry entry = TestDaemon.makeDaemonEntry("JUNIT" + strMethodName);
            scheduler.unSchedule(entry);
            // not sure how to assert something here
        } catch (Exception e)
        {
            fail();
        } finally
        {
            scheduler.shutdown();
        }
    }

    @Test
    public void testUnSchedule()
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, BrokenBarrierException, TimeoutException
    {
        testUnSchedule(false);
    }

    @Test
    public void testUnScheduleDaemonThrows()
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, BrokenBarrierException, TimeoutException
    {
        testUnSchedule(true);
    }

    /**
     * test if the unschedule not scheduled service does not fail when a daemon throws a RunTimeException
     *
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InterruptedException
     * @throws BrokenBarrierException
     * @throws TimeoutException
     */
    private void testUnSchedule(boolean shouldThrow)
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
            testDaemon.setRunThrows(shouldThrow);
            scheduler.schedule(entry, 0L, TimeUnit.MILLISECONDS);
            assertFalse(testDaemon.hasRun());
            testDaemon.go();
            testDaemon.waitForCompletion();
            assertTrue(testDaemon.hasRun());
            assertEquals(0, testDaemon.getStopCallNumber());
            Thread.sleep(10L); // leave some time to the daemon to exit executing daemons
            scheduler.unSchedule(entry);
            assertEquals(1, testDaemon.getStopCallNumber());
            try
            {
                testDaemon.go(1100L, TimeUnit.MILLISECONDS);
                fail("Daemon executed after unscheduling");
            } catch (TimeoutException e)
            {
                // OK
            }
        } catch (InterruptedException | BrokenBarrierException | TimeoutException | InvocationTargetException | NoSuchMethodException e )
        {
            fail(e.getMessage());
        } finally
        {
            scheduler.shutdown();
        }
    }

    @Test
    public void testUnScheduleWhileRunning()
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, BrokenBarrierException, TimeoutException
    {
        testUnScheduleWhileRunning(false);
    }

    @Test
    public void testUnScheduleWhileRunningDaemonThrows()
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException, BrokenBarrierException, TimeoutException
    {
        testUnScheduleWhileRunning(true);
    }

    /**
     * test if the unschedule not scheduled service does not fail when a daemon throws a RunTimeException
     *
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InterruptedException
     * @throws BrokenBarrierException
     * @throws TimeoutException
     */
    private void testUnScheduleWhileRunning(boolean shouldThrow)
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
            testDaemon.setRunThrows(shouldThrow);
            scheduler.schedule(entry, 0L, TimeUnit.MILLISECONDS);
            assertFalse(testDaemon.hasRun());
            testDaemon.go();
            assertEquals(0, testDaemon.getStopCallNumber());
            // unschedule while the daemon is executing
            scheduler.unSchedule(entry);
            Thread.sleep(10L); // leave some time to the daemon
            assertEquals(0, testDaemon.getStopCallNumber(), "Stopping should wait for execution end");
            testDaemon.waitForCompletion();
            assertTrue(testDaemon.hasRun());
            Thread.sleep(10L); // leave some time to the daemon to exit executing daemons
            assertEquals(1, testDaemon.getStopCallNumber());
            try
            {
                testDaemon.go(1100L, TimeUnit.MILLISECONDS);
                fail("Daemon executed after unscheduling");
            } catch (TimeoutException e)
            {
                // OK
            }
        } catch (InterruptedException | BrokenBarrierException | TimeoutException | InvocationTargetException | NoSuchMethodException e )
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
        ExecutorService executor = new ExecutorServiceForDaemonTests( ( ) -> Executors.newSingleThreadExecutor( ) );
        DaemonScheduler scheduler = new DaemonScheduler( );
        scheduler.initDaemonScheduler( queue, executor, _managedThreadFactory, _managedScheduledExecutorService );
        return scheduler;
    }

}
