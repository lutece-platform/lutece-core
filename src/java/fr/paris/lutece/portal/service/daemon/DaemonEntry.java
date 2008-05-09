/*
 * Copyright (c) 2002-2008, Mairie de Paris
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

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.date.DateUtil;

import java.util.Date;


/**
 *  this class is used to manage daemons declaration
 */
public class DaemonEntry
{
    private String _strId;
    private String _strNameKey;
    private String _strDescriptionKey;
    private String _strClassName;
    private Date _dateLastRunDate;
    private String _strLastRunLogs;
    private long _lInterval;
    private boolean _bOnStartup;
    private boolean _bIsRunning;
    private Daemon _daemon;
    private DaemonThread _thread;
    private String _strPluginName;

    // Variables declarations

    /**
     * Returns the Id
     *
     * @return The Id
     */
    public String getId(  )
    {
        return _strId;
    }

    /**
     * Sets the Id
     *
     * @param strId The Id
     */
    public void setId( String strId )
    {
        _strId = strId;
    }

    /**
     * Returns the NameKey
     *
     * @return The NameKey
     */
    public String getNameKey(  )
    {
        return _strNameKey;
    }

    /**
     * Sets the NameKey
     *
     * @param strNameKey The NameKey
     */
    public void setNameKey( String strNameKey )
    {
        _strNameKey = strNameKey;
    }

    /**
     * Returns the DescriptionKey
     *
     * @return The DescriptionKey
     */
    public String getDescriptionKey(  )
    {
        return _strDescriptionKey;
    }

    /**
     * Sets the DescriptionKey
     *
     * @param strDescriptionKey The DescriptionKey
     */
    public void setDescriptionKey( String strDescriptionKey )
    {
        _strDescriptionKey = strDescriptionKey;
    }

    /**
     * Returns the ClassName
     *
     * @return The ClassName
     */
    public String getClassName(  )
    {
        return _strClassName;
    }

    /**
     * Sets the ClassName
     *
     * @param strClassName The ClassName
     */
    public void setClassName( String strClassName )
    {
        _strClassName = strClassName;
    }

    /**
     * Load the daemon
     *
     * @throws ClassNotFoundException If an error occured
     * @throws InstantiationException If an error occured
     * @throws IllegalAccessException If an error occured
     */
    void loadDaemon(  ) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        _daemon = (Daemon) Class.forName( _strClassName ).newInstance(  );
    }

    /**
     * Returns the daemon associated to the entry
     *
     * @return The daemon
     */
    Daemon getDaemon(  )
    {
        return _daemon;
    }

    /**
     * Returns the Interval of time in seconds between two executions
     * @return _lInterval the interval of time
     */
    public long getInterval(  )
    {
        return _lInterval;
    }

    /**
     * Indicates if the daemon must be process on system startup
     * @return _bOnStartup
     */
    public boolean onStartup(  )
    {
        return _bOnStartup;
    }

    /**
     * Starts the thread
     */
    void startThread(  )
    {
        _thread = new DaemonThread( this );
        _thread.start(  );
        _bIsRunning = true;
        AppLogService.info( "Starting daemon '" + getId(  ) + "'" );
    }

    /**
     * Stops the thread
     */
    void stopThread(  )
    {
        if ( _thread != null )
        {
            _thread.interrupt(  );
            _thread = null;
            _bIsRunning = false;
            AppLogService.info( "Stopping daemon '" + getId(  ) + "'" );
        }
    }

    /**
     * Checks if the daemon is running
     * @return True if the thread is running, otherwise false
     */
    public boolean isRunning(  )
    {
        return _bIsRunning;
    }

    /**
     * Returns the PluginName
     *
     * @return The PluginName
     */
    public String getPluginName(  )
    {
        return _strPluginName;
    }

    /**
     * Sets the PluginName
     *
     * @param strPluginName The PluginName
     */
    public void setPluginName( String strPluginName )
    {
        _strPluginName = strPluginName;
    }

    /**
     * Returns the LastRunDate
     *
     * @return The LastRunDate
     */
    public String getLastRunDate(  )
    {
        return ( _dateLastRunDate != null ) ? DateUtil.getDateTimeString( _dateLastRunDate.getTime(  ) ) : "";
    }

    /**
     * Sets the LastRunDate
     *
     * @param dateLastRunDate The LastRunDate
     */
    public void setLastRunDate( Date dateLastRunDate )
    {
        _dateLastRunDate = dateLastRunDate;
    }

    /**
     * Returns the LastRunLogs
     *
     * @return The LastRunLogs
     */
    public String getLastRunLogs(  )
    {
        return ( _strLastRunLogs != null ) ? _strLastRunLogs : "";
    }

    /**
     * Sets the LastRunLogs
     *
     * @param strLastRunLogs The LastRunLogs
     */
    public void setLastRunLogs( String strLastRunLogs )
    {
        _strLastRunLogs = strLastRunLogs;
    }

    /**
     * Sets the interval
     * @param lInterval The interval
     */
    void setInterval( long lInterval )
    {
        _lInterval = lInterval;
    }

    /**
     * Sets the OnStartUp property
     * @param bOnStartup True if the daemon should be launched on startup, otherwise false
     */
    void setOnStartUp( boolean bOnStartup )
    {
        _bOnStartup = bOnStartup;
    }
}
