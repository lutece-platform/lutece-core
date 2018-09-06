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
package fr.paris.lutece.portal.service.scheduler;

import fr.paris.lutece.portal.business.daemon.DaemonTrigger;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.test.LuteceTestCase;

import java.util.List;
import org.quartz.CronTrigger;

public class JobSchedulerServiceTest extends LuteceTestCase
{
    private final static String KEY_1 = "key1";
    private final static String KEY_2 = "key2";
    private final static String GROUP_1 = "group1";
    private final static String GROUP_2 = "group2";
    private final static String CRON_EXPRESSION_1 = "0 0 0 * * ?";
    private final static String CRON_EXPRESSION_2 = "0 0 1 * * ?";
    private final static String DAEMON_KEY_1 = "daemonKey1";
    private final static String DAEMON_KEY_2 = "daemonKey2";

    public void testServiceTrigger( )
    {
        // Initialize an object
        DaemonTrigger daemonTrigger = new DaemonTrigger( );
        daemonTrigger.setKey( KEY_1 );
        daemonTrigger.setGroup( GROUP_1 );
        daemonTrigger.setCronExpression( CRON_EXPRESSION_1 );
        daemonTrigger.setDaemonKey( DAEMON_KEY_1 );

        // Create test
        JobSchedulerService.getInstance( ).createTrigger( daemonTrigger );

        CronTrigger triggerStored = (CronTrigger) JobSchedulerService.getInstance( ).findTriggerByKey( daemonTrigger.getKey( ), daemonTrigger.getGroup( ) );
        assertEquals( triggerStored.getKey( ).getName( ), daemonTrigger.getKey( ) );
        assertEquals( triggerStored.getKey( ).getGroup( ), daemonTrigger.getGroup( ) );
        assertEquals( triggerStored.getCronExpression( ), daemonTrigger.getCronExpression( ) );
        assertEquals( triggerStored.getJobDataMap( ).get( "DaemonKey" ), daemonTrigger.getDaemonKey( ) );

        // Update test
        DaemonTrigger newDaemonTrigger = new DaemonTrigger( );
        newDaemonTrigger.setKey( KEY_2 );
        newDaemonTrigger.setGroup( GROUP_2 );
        newDaemonTrigger.setCronExpression( CRON_EXPRESSION_2 );
        newDaemonTrigger.setDaemonKey( DAEMON_KEY_2 );
        JobSchedulerService.getInstance( ).updateTrigger( daemonTrigger, newDaemonTrigger );

        triggerStored = (CronTrigger) JobSchedulerService.getInstance( ).findTriggerByKey( newDaemonTrigger.getKey( ), newDaemonTrigger.getGroup( ) );
        assertEquals( triggerStored.getKey( ).getName( ), newDaemonTrigger.getKey( ) );
        assertEquals( triggerStored.getKey( ).getGroup( ), newDaemonTrigger.getGroup( ) );
        assertEquals( triggerStored.getCronExpression( ), newDaemonTrigger.getCronExpression( ) );
        assertEquals( triggerStored.getJobDataMap( ).get( "DaemonKey" ), newDaemonTrigger.getDaemonKey( ) );

        // List test
        List listTriggers = JobSchedulerService.getInstance( ).getTriggersList( );
        assertTrue( listTriggers.size( ) > 0 );

        // Delete test
        JobSchedulerService.getInstance( ).removeTrigger( newDaemonTrigger );
        triggerStored = (CronTrigger) JobSchedulerService.getInstance( ).findTriggerByKey( newDaemonTrigger.getKey( ), newDaemonTrigger.getGroup( ) );
        assertNull( triggerStored );
    }
}
