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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppLogService;


/**
 * Represents a runnable waiting to be launched by the
 * {@link ThreadLauncherDaemon}
 */
public class RunnableQueueItem implements Runnable
{
    private Runnable _runnable;
    private String _strKey;
    private Plugin _plugin;

    /**
     * Creates a new {@link RunnableQueueItem}
     * @param runnable The runnable waiting to bet launched
     * @param strKey The key associated with the runnable. Runnables of a given
     *            plugin are ensured that they will not be executed at the same
     *            time if they have the same key.
     * @param plugin The plugin associated with the runnable queue item
     */
    public RunnableQueueItem( Runnable runnable, String strKey, Plugin plugin )
    {
        this._runnable = runnable;
        this._strKey = strKey;
        this._plugin = plugin;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(  )
    {
        try
        {
            _runnable.run(  );
        }
        catch ( Exception e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
    }

    /**
     * Get the runnable that is waiting for its execution
     * @return The runnable
     */
    public Runnable getRunnable(  )
    {
        return _runnable;
    }

    /**
     * Get the key that identifies the runnable.
     * @return the key of the runnable
     */
    public String getKey(  )
    {
        return _strKey;
    }

    /**
     * Get the plugin that created the runnable
     * @return The plugin that created the runnable
     */
    public Plugin getPlugin(  )
    {
        return _plugin;
    }

    /**
     * Compute the key of the item from its plugin name and the declared key.
     * The difference between the computed key and the defined key is that the
     * computed key is unique, whereas the defined key is unique within a
     * plugin.
     * @return The computed key of this runnable item
     */
    protected String computeKey(  )
    {
        if ( ( _strKey != null ) && ( _plugin != null ) )
        {
            return _plugin.getName(  ) + _strKey;
        }

        return null;
    }
}
