/*
 * Copyright (c) 2002-2019, Mairie de Paris
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

import java.util.concurrent.TimeUnit;

/**
 * Daemon scheduler. Responsible for ensuring on demand or timely daemon execution.
 */
public interface IDaemonScheduler
{

    /** The bean name */
    String BEAN_NAME = "daemonScheduler";

    /**
     * Enqueue a daemon for execution in the immediate future
     * 
     * @param entry
     *            the daemon entry
     * @param unit
     *            the delay before execution
     * @param nDelay
     *            the unit of <code>nDelay</code> argument
     * @return <code>true</code> if the daemon was successfully queued, <code>false</code> otherwise, for instance if the underlying queue is over capacity
     */
    boolean enqueue( DaemonEntry entry, long nDelay, TimeUnit unit );

    /**
     * Schedule a daemon execution.
     * 
     * @see DaemonEntry#getInterval()
     * @param entry
     *            the daemon entry
     * @param nInitialDelay
     *            the initial delay before the first execution
     * @param unit
     *            the unit of <code>nInitialDelay</code> argument
     */
    void schedule( DaemonEntry entry, long nInitialDelay, TimeUnit unit );

    /**
     * Unschedule a daemon
     * 
     * @param entry
     *            the daemon entry
     */
    void unSchedule( DaemonEntry daemonEntry );

    /**
     * Perform an orderly shutdown, stopping daemon execution
     */
    void shutdown( );

}
