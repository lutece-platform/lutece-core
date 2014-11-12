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
package fr.paris.lutece.portal.service.database;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.util.sql.TransactionManager;

import org.apache.commons.lang.StringUtils;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionStatus;


/**
 * Lutece transaction manager. This TM use Lutece's specific pool transaction
 * manager with the class {@link TransactionManager}. It allow plugins to use
 * multi plugin transactions, and nested transactions. Note that nested
 * transactions does not create savepoints : if a nested transaction is roll
 * backed, then the whole transaction is roll backed.
 */
public class LuteceTransactionManager implements PlatformTransactionManager
{
    private String _strPluginName;
    private volatile Plugin _plugin;

    /**
     * Gets the plugin name
     * @return the plugin name
     */
    public String getPluginName(  )
    {
        return _strPluginName;
    }

    /**
     * Sets the plugin name
     * @param strPluginName the plugin name
     */
    public void setPluginName( String strPluginName )
    {
        _strPluginName = strPluginName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TransactionStatus getTransaction( TransactionDefinition definition )
        throws TransactionException
    {
        TransactionStatus trStatus = new DefaultTransactionStatus( null, true, false, false, false, null );
        TransactionManager.beginTransaction( getPlugin(  ) );

        return trStatus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit( TransactionStatus status ) throws TransactionException
    {
        TransactionManager.commitTransaction( getPlugin(  ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rollback( TransactionStatus status ) throws TransactionException
    {
        TransactionManager.rollBack( getPlugin(  ) );
    }

    private Plugin getPlugin(  )
    {
        if ( _plugin == null )
        {
            if ( StringUtils.isNotBlank( _strPluginName ) )
            {
                this._plugin = PluginService.getPlugin( _strPluginName );
            }
            else
            {
                _plugin = PluginService.getCore(  );
            }
        }

        return _plugin;
    }
}
