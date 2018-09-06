/*
 * Copyright (c) 2002-2018, Mairie de Paris
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
package fr.paris.lutece.portal.business.daemon;

import fr.paris.lutece.test.LuteceTestCase;

import java.util.List;

public class DaemonTriggerHomeTest extends LuteceTestCase
{
    private final static String KEY_1 = "key1";
    private final static String KEY_2 = "key2";
    private final static String GROUP_1 = "group1";
    private final static String GROUP_2 = "group2";
    private final static String CRON_EXPRESSION_1 = "cronExpression1";
    private final static String CRON_EXPRESSION_2 = "cronExpression2";
    private final static String DAEMON_KEY_1 = "daemonKey1";
    private final static String DAEMON_KEY_2 = "daemonKey2";

    public void testBusinessDaemonTrigger( )
    {
        // Initialize an object
        DaemonTrigger daemonTrigger = new DaemonTrigger( );
        daemonTrigger.setKey( KEY_1 );
        daemonTrigger.setGroup( GROUP_1 );
        daemonTrigger.setCronExpression( CRON_EXPRESSION_1 );
        daemonTrigger.setDaemonKey( DAEMON_KEY_1 );

        // Create test
        DaemonTriggerHome.create( daemonTrigger );

        DaemonTrigger daemonTriggerStored = DaemonTriggerHome.findByPrimaryKey( daemonTrigger.getId( ) );
        assertEquals( daemonTriggerStored.getKey( ), daemonTrigger.getKey( ) );
        assertEquals( daemonTriggerStored.getGroup( ), daemonTrigger.getGroup( ) );
        assertEquals( daemonTriggerStored.getCronExpression( ), daemonTrigger.getCronExpression( ) );
        assertEquals( daemonTriggerStored.getDaemonKey( ), daemonTrigger.getDaemonKey( ) );

        // Update test
        daemonTrigger.setKey( KEY_2 );
        daemonTrigger.setGroup( GROUP_2 );
        daemonTrigger.setCronExpression( CRON_EXPRESSION_2 );
        daemonTrigger.setDaemonKey( DAEMON_KEY_2 );
        DaemonTriggerHome.update( daemonTrigger );

        daemonTriggerStored = DaemonTriggerHome.findByPrimaryKey( daemonTrigger.getId( ) );
        assertEquals( daemonTriggerStored.getKey( ), daemonTrigger.getKey( ) );
        assertEquals( daemonTriggerStored.getGroup( ), daemonTrigger.getGroup( ) );
        assertEquals( daemonTriggerStored.getCronExpression( ), daemonTrigger.getCronExpression( ) );
        assertEquals( daemonTriggerStored.getDaemonKey( ), daemonTrigger.getDaemonKey( ) );

        // List test
        List listDaemonTriggers = DaemonTriggerHome.getDaemonTriggersList( );
        assertTrue( listDaemonTriggers.size( ) > 0 );

        // Delete test
        DaemonTriggerHome.remove( daemonTrigger.getId( ) );
        daemonTriggerStored = DaemonTriggerHome.findByPrimaryKey( daemonTrigger.getId( ) );
        assertNull( daemonTriggerStored );
    }
}
