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

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class TestDaemon extends Daemon
{
    CyclicBarrier startBarrier = new CyclicBarrier( 2 );
    CyclicBarrier completionBarrier = new CyclicBarrier( 2 );
    boolean hasRun;
    boolean shouldThrow;

    public void setRunThrows( boolean shouldThrow )
    {
        this.shouldThrow = shouldThrow;
    }

    @Override
    public void run( )
    {
        try
        {
            hasRun = false;
            startBarrier.await( 10, TimeUnit.SECONDS );
            hasRun = true;
            completionBarrier.await( 10, TimeUnit.SECONDS );
        }
        catch( InterruptedException | BrokenBarrierException | TimeoutException e )
        {
            e.printStackTrace( );
        }
        if ( shouldThrow )
        {
            throw new RuntimeException( "I'm a bad daemon" );
        }
    }

    public boolean hasRun( )
    {
        return hasRun;
    }

    public void resetGo( )
    {
        startBarrier.reset( );
    }

    public void go( ) throws InterruptedException, BrokenBarrierException, TimeoutException
    {
        this.go( 10, TimeUnit.SECONDS );
    }

    public void go( long timeout, TimeUnit unit ) throws InterruptedException, BrokenBarrierException, TimeoutException
    {
        startBarrier.await( timeout, unit );
    }

    public void waitForCompletion( ) throws InterruptedException, BrokenBarrierException, TimeoutException
    {
        this.waitForCompletion( 10, TimeUnit.SECONDS );
    }

    public void waitForCompletion( long timeout, TimeUnit unit ) throws InterruptedException, BrokenBarrierException, TimeoutException
    {
        completionBarrier.await( timeout, unit );
    }

}
