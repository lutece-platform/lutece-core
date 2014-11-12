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
package fr.paris.lutece.portal.business.user.authentication;

import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.Timestamp;


/**
 * This class provides Data Access methods for LuteceDefaultAdminUser objects
 */
public class LuteceDefaultAdminUserDAO implements ILuteceDefaultAdminUserDAO
{
    public static final int USER_NOTFOUND = -1;
    public static final int INVALID_PASSWORD = -2;
    public static final int USER_OK = 0;
    private static final String SQL_QUERY_CHECK_PASSWORD = "SELECT password FROM core_admin_user WHERE  access_code = ? ";
    private static final String SQL_QUERY_LOAD_USER = " SELECT access_code, id_user, password_max_valid_date, account_max_valid_date, email FROM core_admin_user WHERE access_code = ? ";
    private static final String SQL_QUERY_UPDATE_PASSWORD_RESET = "UPDATE core_admin_user set reset_password = ? WHERE id_user = ? ";

    // /////////////////////////////////////////////////////////////////////////////////////
    // Access methods to data

    /**
     * Check the password of a given user into the table provided by the database authentication module
     * @param strAccessCode The name of the user
     * @param strPassword the user password
     * @return the the error number
     */
    public int checkPassword( String strAccessCode, String strPassword )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CHECK_PASSWORD );
        daoUtil.setString( 1, strAccessCode );
        daoUtil.executeQuery(  );

        if ( !daoUtil.next(  ) )
        {
            daoUtil.free(  );

            return USER_NOTFOUND;
        }

        String strStoredPassword = daoUtil.getString( 1 );
        daoUtil.free(  );

        if ( !strStoredPassword.equals( strPassword ) )
        {
            daoUtil.free(  );

            return INVALID_PASSWORD;
        }

        return USER_OK;
    }

    /**
     * load the data of an user from the table provided by the database authentication module This only provides data specific to the database authentication module.
     *
     * @param strAccessCode The access code of user
     * @param authenticationService The AdminAuthentication
     * @return user The instance of an LuteceDefaultAdminUser's object
     */
    public LuteceDefaultAdminUser load( String strAccessCode, AdminAuthentication authenticationService )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_LOAD_USER );
        daoUtil.setString( 1, strAccessCode );
        daoUtil.executeQuery(  );

        if ( !daoUtil.next(  ) )
        {
            daoUtil.free(  );
            throw new AppException( "The line doesn't exist " );
        }

        String strUserName = daoUtil.getString( 1 );
        LuteceDefaultAdminUser user = new LuteceDefaultAdminUser( strUserName, authenticationService );
        user.setUserId( daoUtil.getInt( 2 ) );
        user.setPasswordMaxValidDate( daoUtil.getTimestamp( 3 ) );

        long accountMaxValidDate = daoUtil.getLong( 4 );

        if ( accountMaxValidDate > 0 )
        {
            user.setAccountMaxValidDate( new Timestamp( accountMaxValidDate ) );
        }

        user.setEmail( daoUtil.getString( 5 ) );
        daoUtil.free(  );

        return user;
    }

    /**
     * Set the reset password attribute of the user
     * @param user User to update
     * @param bIsPasswordReset New value of the reset password attribute
     */
    public void updateResetPassword( LuteceDefaultAdminUser user, boolean bIsPasswordReset )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_PASSWORD_RESET );
        daoUtil.setBoolean( 1, bIsPasswordReset );
        daoUtil.setInt( 2, user.getUserId(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}
