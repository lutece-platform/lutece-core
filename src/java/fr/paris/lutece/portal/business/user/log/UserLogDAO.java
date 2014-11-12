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
package fr.paris.lutece.portal.business.user.log;

import fr.paris.lutece.util.sql.DAOUtil;


/**
 * This class provides Data Access methods for AppUser objects
 */
public final class UserLogDAO implements IUserLogDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT_LOGIN_ERRORS = " SELECT COUNT(*) FROM core_connections_log  WHERE ip_address = ?  " +
        " AND date_login > ? AND date_login < ? ";
    private static final String SQL_QUERY_INSERT_LOGS = " INSERT INTO core_connections_log ( access_code, ip_address, date_login, login_status ) " +
        " VALUES ( ?, ?, ?, ? )";

    /**
     * Calculate the number of connections with a given ip_address by a determinate time
     * @param userLog The Log of connection
     * @param nIntervalMinutes The number of minutes since the last connection
     * @return int
     */
    public int selectLoginErrors( UserLog userLog, int nIntervalMinutes )
    {
        int nCount = 0;
        java.sql.Timestamp dateEnd = new java.sql.Timestamp( new java.util.Date(  ).getTime(  ) );
        java.sql.Timestamp dateBegin = new java.sql.Timestamp( dateEnd.getTime(  ) - ( nIntervalMinutes * 1000L * 60L ) );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_LOGIN_ERRORS );

        daoUtil.setString( 1, userLog.getIpAddress(  ) );
        daoUtil.setTimestamp( 2, dateBegin );
        daoUtil.setTimestamp( 3, dateEnd );

        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            nCount = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nCount;
    }

    /**
     * Insert a new record in the table of connections
     * @param userLog the UserLog Object
     */
    public void insertLog( UserLog userLog )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_LOGS );
        daoUtil.setString( 1, userLog.getAccessCode(  ) );
        daoUtil.setString( 2, userLog.getIpAddress(  ) );
        daoUtil.setTimestamp( 3, userLog.getDateLogin(  ) );
        daoUtil.setInt( 4, userLog.getLoginStatus(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}
