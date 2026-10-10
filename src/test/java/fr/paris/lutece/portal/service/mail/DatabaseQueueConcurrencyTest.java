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
package fr.paris.lutece.portal.service.mail;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.test.LuteceTestCase;

/**
 * Database mail queue consumed concurrently (several daemons, several instances): each mail is taken once
 */
public class DatabaseQueueConcurrencyTest extends LuteceTestCase
{
    private static final int MAILS = 60;
    private static final int CONSUMERS = 8;

    @Test
    public void testEachMailIsConsumedOnce( ) throws Exception
    {
        DatabaseQueue queue = new DatabaseQueue( );
        while ( queue.consume( ) != null )
        {
            // empty the queue
        }
        for ( int i = 0; i < MAILS; i++ )
        {
            MailItem item = new MailItem( );
            item.setSubject( "concurrency" + i );
            queue.send( item );
        }

        Set<String> setConsumed = ConcurrentHashMap.newKeySet( );
        AtomicInteger nDuplicates = new AtomicInteger( );
        ExecutorService executor = Executors.newFixedThreadPool( CONSUMERS );
        try
        {
            List<Future<?>> futures = new ArrayList<>( );
            for ( int c = 0; c < CONSUMERS; c++ )
            {
                // One queue per consumer, as on separate instances
                DatabaseQueue queueOfInstance = new DatabaseQueue( );
                futures.add( executor.submit( ( ) -> {
                    MailItem item;
                    while ( ( item = queueOfInstance.consume( ) ) != null )
                    {
                        if ( !setConsumed.add( item.getSubject( ) ) )
                        {
                            nDuplicates.incrementAndGet( );
                        }
                    }
                } ) );
            }
            for ( Future<?> future : futures )
            {
                future.get( );
            }
        }
        finally
        {
            executor.shutdown( );
        }

        assertEquals( 0, nDuplicates.get( ), "mails sent twice" );
        assertEquals( MAILS, setConsumed.size( ) );
    }
}
