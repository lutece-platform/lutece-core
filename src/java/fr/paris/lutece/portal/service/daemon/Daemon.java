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
package fr.paris.lutece.portal.service.daemon;


/**
 * Interfaces for Daemons Service.
 */
public abstract class Daemon implements Runnable
{
    ////////////////////////////////////////////////////////////////////////////////
    // Constants
    public static final int STATUS_STOPPED = 0;
    public static final int STATUS_RUNNING = 1;

    /** The result for this daemon is not yet known. */
    public static final int RESULT_UNKNOWN = 0;

    /** A daemon has processed and it was successful */
    public static final int RESULT_SUCCESS = 1;

    /** A daemon has processed but it has failed */
    public static final int RESULT_FAILED = 2;

    /** A daemon is processing so its result is not yet known */
    public static final int RESULT_PROCESSING = 3;
    private String _strLastRunLogs;
    private int _nResult;
    private String _strPluginName;

    /**
     * Gets the plugin name
     *
     * @return the plugin name
     */
    public String getPluginName(  )
    {
        return _strPluginName;
    }

    /**
     * Sets the plugin name
     *
     * @param pluginName The plugin name
     */
    public void setPluginName( String pluginName )
    {
        _strPluginName = pluginName;
    }

    /**
     * Returns the LastRunLogs
     *
     * @return The LastRunLogs
     */
    public String getLastRunLogs(  )
    {
        return _strLastRunLogs;
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
     * Returns the Result
     *
     * @return The Result
     */
    public int getResult(  )
    {
        return _nResult;
    }

    /**
     * Sets the Result
     *
     * @param nResult The Result
     */
    public void setResult( int nResult )
    {
        _nResult = nResult;
    }
}
