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
import fr.paris.lutece.portal.business.daemon.DaemonTriggerHome;
import fr.paris.lutece.portal.service.daemon.DaemonJob;
import fr.paris.lutece.portal.service.util.AppLogService;
import java.util.ArrayList;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;

import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;
import java.util.List;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import static org.quartz.TriggerKey.triggerKey;
import static org.quartz.impl.matchers.GroupMatcher.groupEquals;

/**
 * JobSchedulerService
 */
public final class JobSchedulerService
{
    private static volatile JobSchedulerService _singleton;
    private static Scheduler _scheduler;

    /** Creates a new instance of JobSchedulerService */
    private JobSchedulerService( )
    {
    }

    /**
     * Gets the unique instance of the service
     * 
     * @return The service's instance
     */
    public static JobSchedulerService getInstance( )
    {
        if ( _singleton == null )
        {
            synchronized( JobSchedulerService.class )
            {
                JobSchedulerService service = new JobSchedulerService( );
                service.init( );
                _singleton = service;

                loadTriggers( );
            }
        }

        return _singleton;
    }

    /**
     * Initialize the service.
     */
    private void init( )
    {
        SchedulerFactory factory = new StdSchedulerFactory( );

        try
        {
            _scheduler = factory.getScheduler( );
            _scheduler.start( );
            AppLogService.info( "Lutece job scheduler started." );
        }
        catch( SchedulerException e )
        {
            AppLogService.error( "Error starting the Lutece job scheduler ", e );
        }
    }

    /**
     * Schedule a job according cron information
     * 
     * @param job
     *            The Job to schedule
     * @param trigger
     *            The Cron trigger
     * @return Date
     */
    public Date scheduleJob( JobDetail job, CronTrigger trigger )
    {
        Date date = null;

        if ( _scheduler != null )
        {
            try
            {
                date = _scheduler.scheduleJob( job, trigger );
                AppLogService.info( "New job scheduled : " + job.getKey( ).getName( ) );
            }
            catch( SchedulerException e )
            {
                AppLogService.error( "Error scheduling job " + job.getKey( ).getName( ), e );
            }
        }

        return date;
    }

    /**
     * Register a trigger
     * 
     * @param daemonTrigger
     *            A daemon trigger representing the new trigger
     * @return Date
     */
    public Date createTrigger( DaemonTrigger daemonTrigger )
    {
        Date date = null;

        String strTriggerKey = daemonTrigger.getKey( );
        String strTriggerGroup = daemonTrigger.getGroup( );
        String strCronExpression = daemonTrigger.getCronExpression( );
        String strDaemonKey = daemonTrigger.getDaemonKey( );

        if ( _scheduler != null )
        {
            try
            {
                JobDetail jobDetail;

                if ( !_scheduler.checkExists( new JobKey( "default", "default" ) ) )
                {
                    jobDetail = JobBuilder.newJob( DaemonJob.class )
                        .withIdentity( "default", "default" )
                        .storeDurably( )
                        .build( );
                    _scheduler.addJob( jobDetail, true );
                }
                else
                {
                    jobDetail = _scheduler.getJobDetail( new JobKey( "default", "default" ) );
                }

                CronTrigger cronTrigger = (CronTrigger) TriggerBuilder.newTrigger( )
                    .withIdentity( strTriggerKey, strTriggerGroup )
                    .withSchedule( CronScheduleBuilder.cronSchedule( strCronExpression ) )
                    .usingJobData( "DaemonKey" , strDaemonKey )
                    .forJob( jobDetail )
                    .build( );

                date = _scheduler.scheduleJob( cronTrigger );
                AppLogService.info( "New trigger registered : " + strTriggerKey + " - " + strTriggerGroup );
            }
            catch( SchedulerException e )
            {
                AppLogService.error( "Error registering trigger " + strTriggerKey + " - " + strTriggerGroup, e );
            }
        }

        return date;
    }

    /**
     * Reregister a trigger
     * 
     * @param oldDaemonTrigger
     *            A daemon trigger representing the old trigger
     * @param newDaemonTrigger
     *            A daemon trigger representing the new trigger
     * @return Date
     */
    public Date updateTrigger( DaemonTrigger oldDaemonTrigger, DaemonTrigger newDaemonTrigger )
    {
        Date date = null;

        String strOldTriggerKey = oldDaemonTrigger.getKey( );
        String strOldTriggerGroup = oldDaemonTrigger.getGroup( );
        String strTriggerKey = newDaemonTrigger.getKey( );
        String strTriggerGroup = newDaemonTrigger.getGroup( );
        String strCronExpression = newDaemonTrigger.getCronExpression( );
        String strDaemonKey = newDaemonTrigger.getDaemonKey( );

        CronTrigger cronTrigger = (CronTrigger) getInstance( ).findTriggerByKey( strOldTriggerKey, strOldTriggerGroup );

        TriggerBuilder triggerBuilder = cronTrigger.getTriggerBuilder( );

        Trigger newTrigger = triggerBuilder.withIdentity( strTriggerKey, strTriggerGroup )
            .withSchedule( CronScheduleBuilder.cronSchedule( strCronExpression ) )
            .usingJobData( "DaemonKey", strDaemonKey )
            .build( );

        if ( _scheduler != null )
        {
            try
            {
                date = _scheduler.rescheduleJob( cronTrigger.getKey( ), newTrigger );
                AppLogService.info( "Trigger reregistered : " + strTriggerKey + " - " + strTriggerGroup );
            }
            catch( SchedulerException e )
            {
                AppLogService.error( "Error reregistering trigger " + strTriggerKey + " - " + strTriggerGroup, e );
            }
        }

        return date;
    }

    /**
     * Unregister a trigger
     * 
     * @param daemonTrigger
     *            A daemon trigger representing the trigger to remove
     */
    public void removeTrigger( DaemonTrigger daemonTrigger )
    {
        String strTriggerKey = daemonTrigger.getKey( );
        String strTriggerGroup = daemonTrigger.getGroup( );

        if ( _scheduler != null )
        {
            try
            {
                _scheduler.unscheduleJob( triggerKey( strTriggerKey, strTriggerGroup ) );
                AppLogService.info( "Trigger unregistered : " + strTriggerKey + " - " + strTriggerGroup );
            }
            catch( SchedulerException e )
            {
                AppLogService.error( "Error unregistering trigger " + strTriggerKey + " - " + strTriggerGroup, e );
            }
        }
    }

    /**
     * Returns an instance of a trigger whose key is specified in parameter
     * 
     * @param strTriggerKey
     *            The trigger key
     * @param strTriggerGroup
     *            The trigger group
     * @return the trigger
     */
    public Trigger findTriggerByKey( String strTriggerKey, String strTriggerGroup )
    {
        Trigger trigger = null;
        try
        {
            trigger = _scheduler.getTrigger( triggerKey( strTriggerKey, strTriggerGroup ) );
        }
        catch( SchedulerException e )
        {
            AppLogService.error( "Error getting trigger " + strTriggerKey + " - " + strTriggerGroup, e );
        }

        return trigger;
    }

    /**
     * Shutdown the service (Called by the core while the webapp is destroyed)
     */
    public static void shutdown( )
    {
        if ( _scheduler != null )
        {
            try
            {
                _scheduler.shutdown( );
                AppLogService.info( "Lutece job scheduler stopped." );
            }
            catch( SchedulerException e )
            {
                AppLogService.error( "Error shuting down the Lutece job scheduler ", e );
            }
        }
    }

    /**
     * get the trigger list
     * 
     * @return the trigger list
     */
    public static List<Trigger> getTriggersList( )
    {
        List<Trigger> listTrigger = new ArrayList<>( );
        try
        {
            for( String triggerGroup : _scheduler.getTriggerGroupNames( ) )
            {
                for( TriggerKey triggerKey : _scheduler.getTriggerKeys( groupEquals( triggerGroup ) ) )
                {
                    listTrigger.add( _scheduler.getTrigger( triggerKey ) );
                }
            }
        }
        catch( SchedulerException e )
        {
            AppLogService.error( "Error listing the triggers ", e );
        }

        return listTrigger;
    }

    /**
     * load the triggers save in database
     * 
     */
    private static void loadTriggers( )
    {
        List<DaemonTrigger> listDaemonTriggers = DaemonTriggerHome.getDaemonTriggersList( );

        for ( DaemonTrigger daemonTrigger : listDaemonTriggers )
        {
            getInstance( ).createTrigger( daemonTrigger );
        }
    }
}
