/*
 * Copyright (c) 2002-2014, Mairie de Paris
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

import fr.paris.lutece.portal.service.util.AppLogService;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;

import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;


/**
 * JobSchedulerService
 */
public final class JobSchedulerService
{
    private static volatile JobSchedulerService _singleton;
    private static Scheduler _scheduler;

    /** Creates a new instance of JobSchedulerService */
    private JobSchedulerService(  )
    {
    }

    /**
     * Gets the unique instance of the service
     * @return The service's instance
     */
    public static JobSchedulerService getInstance(  )
    {
        if ( _singleton == null )
        {
            synchronized ( JobSchedulerService.class )
            {
                JobSchedulerService service = new JobSchedulerService(  );
                service.init(  );
                _singleton = service;
            }
        }

        return _singleton;
    }

    /**
     * Initialize the service.
     */
    private void init(  )
    {
        SchedulerFactory factory = new StdSchedulerFactory(  );

        try
        {
            _scheduler = factory.getScheduler(  );
            _scheduler.start(  );
            AppLogService.info( "Lutece job scheduler started." );
        }
        catch ( SchedulerException e )
        {
            AppLogService.error( "Error starting the Lutece job scheduler ", e );
        }
    }

    /**
     * Schedule a job according cron information
     * @param job The Job to schedule
     * @param trigger The Cron trigger
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
                AppLogService.info( "New job scheduled : " + job.getName(  ) );
            }
            catch ( SchedulerException e )
            {
                AppLogService.error( "Error scheduling job " + job.getName(  ), e );
            }
        }

        return date;
    }

    /**
     * Shutdown the service (Called by the core while the webapp is destroyed)
     */
    public static void shutdown(  )
    {
        if ( _scheduler != null )
        {
            try
            {
                _scheduler.shutdown(  );
                AppLogService.info( "Lutece job scheduler stopped." );
            }
            catch ( SchedulerException e )
            {
                AppLogService.error( "Error shuting down the Lutece job scheduler ", e );
            }
        }
    }
}
