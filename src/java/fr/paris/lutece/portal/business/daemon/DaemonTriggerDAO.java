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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides Data Access methods for DaemonTrigger objects
 */
public final class DaemonTriggerDAO implements IDaemonTriggerDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_daemon_trigger, trigger_key, trigger_group, cron_expression, daemon_key FROM core_daemon_trigger WHERE id_daemon_trigger = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO core_daemon_trigger ( trigger_key, trigger_group, cron_expression, daemon_key ) VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM core_daemon_trigger WHERE id_daemon_trigger = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE core_daemon_trigger SET id_daemon_trigger = ?, trigger_key = ?, trigger_group = ?, cron_expression = ?, daemon_key = ? WHERE id_daemon_trigger = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_daemon_trigger, trigger_key, trigger_group, cron_expression, daemon_key FROM core_daemon_trigger";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( DaemonTrigger daemonTrigger, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin );
        try
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++ , daemonTrigger.getKey( ) );
            daoUtil.setString( nIndex++ , daemonTrigger.getGroup( ) );
            daoUtil.setString( nIndex++ , daemonTrigger.getCronExpression( ) );
            daoUtil.setString( nIndex++ , daemonTrigger.getDaemonKey( ) );

            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) )
            {
                daemonTrigger.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
        finally
        {
            daoUtil.free( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public DaemonTrigger load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1 , nKey );
        daoUtil.executeQuery( );
        DaemonTrigger daemonTrigger = null;

        if ( daoUtil.next( ) )
        {
            daemonTrigger = new DaemonTrigger();
            int nIndex = 1;

            daemonTrigger.setId( daoUtil.getInt( nIndex++ ) );
            daemonTrigger.setKey( daoUtil.getString( nIndex++ ) );
            daemonTrigger.setGroup( daoUtil.getString( nIndex++ ) );
            daemonTrigger.setCronExpression( daoUtil.getString( nIndex++ ) );
            daemonTrigger.setDaemonKey( daoUtil.getString( nIndex++ ) );
        }

        daoUtil.free( );
        return daemonTrigger;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1 , nKey );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( DaemonTrigger daemonTrigger, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        int nIndex = 1;

        daoUtil.setInt( nIndex++ , daemonTrigger.getId( ) );
        daoUtil.setString( nIndex++ , daemonTrigger.getKey( ) );
        daoUtil.setString( nIndex++ , daemonTrigger.getGroup( ) );
        daoUtil.setString( nIndex++ , daemonTrigger.getCronExpression( ) );
        daoUtil.setString( nIndex++ , daemonTrigger.getDaemonKey( ) );
        daoUtil.setInt( nIndex , daemonTrigger.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<DaemonTrigger> selectDaemonTriggersList( Plugin plugin )
    {
        List<DaemonTrigger> daemonTriggerList = new ArrayList<DaemonTrigger>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            DaemonTrigger daemonTrigger = new DaemonTrigger(  );
            int nIndex = 1;

            daemonTrigger.setId( daoUtil.getInt( nIndex++ ) );
            daemonTrigger.setKey( daoUtil.getString( nIndex++ ) );
            daemonTrigger.setGroup( daoUtil.getString( nIndex++ ) );
            daemonTrigger.setCronExpression( daoUtil.getString( nIndex++ ) );
            daemonTrigger.setDaemonKey( daoUtil.getString( nIndex++ ) );

            daemonTriggerList.add( daemonTrigger );
        }

        daoUtil.free( );
        return daemonTriggerList;
    }
}
