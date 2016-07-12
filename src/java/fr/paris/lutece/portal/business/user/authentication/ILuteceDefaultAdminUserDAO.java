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


/**
 *
 * @author lenaini
 */
public interface ILuteceDefaultAdminUserDAO
{
    /**
     * Check the password of a given user into the table provided by the database authentication module
     * @param strAccessCode The name of the user
     * @param strPassword the user password
     * @return the the error number
     */
    int checkPassword( String strAccessCode, String strPassword );

    /**
     * load the data of an user from the table provided by the database authentication module
     * This only provides data specific to the database authentication module.
     * @param strAccessCode The access code of user
     * @param authenticationService The AdminAuthentication
     * @return user The instance of an LuteceDefaultAdminUser's object
     */
    LuteceDefaultAdminUser load( String strAccessCode, AdminAuthentication authenticationService );

    /**
     * Set the reset password attribute of the user
     * @param user User to update
     * @param bIsPasswordReset New value of the reset password attribute
     */
    void updateResetPassword( LuteceDefaultAdminUser user, boolean bIsPasswordReset );
}
