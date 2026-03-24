/*
 * Copyright (c) 2002-2025, City of Paris
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
package fr.paris.lutece.portal.business.user;

import fr.paris.lutece.util.string.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

public class AdminUserWrapper implements Serializable
{

    private final AdminUser _adminUser;
    private List<String> _roles;
    private List<String> _rights;
    private List<String> _workgroups;
    private List<String> _attributes;

    /**
     *
     * @param adminUser the admin user
     */
    public AdminUserWrapper( AdminUser adminUser)
    {
        _adminUser = adminUser;
    }


    /**
     * Returns the user access code.
     *
     * @return the user access code
     */
    public String getAccessCode()
    {
        return _adminUser.getAccessCode( );
    }

    /**
     * Returns the user last name.
     *
     * @return the user last name
     */
    public String getLastName()
    {
        return _adminUser.getLastName( );
    }

    /**
     * Returns the user first name.
     *
     * @return the user first name
     */
    public String getFirstName()
    {
        return _adminUser.getFirstName( );
    }

    /**
     * Returns the user email address.
     *
     * @return the user email address
     */
    public String getEmail()
    {
        return _adminUser.getEmail( );
    }

    /**
     * Returns the user status.
     *
     * @return the user status
     */
    public int getStatus()
    {
        return _adminUser.getRealStatus( );
    }

    /**
     * Returns the user locale.
     *
     * @return the user locale
     */
    public String getLocale()
    {
        return _adminUser.getLocale( ).toString( );
    }

    /**
     * Returns the user level.
     *
     * @return the user level
     */
    public int getLevel()
    {
        return _adminUser.getUserLevel( );
    }

    /**
     * Returns whether the user must reset the password.
     *
     * @return the password reset flag
     */
    public String getPasswordReset() {
        return Boolean.toString( _adminUser.isPasswordReset( ) );
    }

    /**
     * Returns the user accessibility mode.
     *
     * @return the accessibility mode
     */
    public String getAccessibilityMode() {
        return Boolean.toString( _adminUser.getAccessibilityMode( ) );
    }

    /**
     * Returns the maximum password validity date.
     *
     * @return the maximum password validity date
     */
    public String getPasswordMaxValidDate() {
        DateFormat dateFormat = new SimpleDateFormat( );
        return Optional.ofNullable( _adminUser.getPasswordMaxValidDate( ) ).map( dateFormat::format ).orElse( StringUtils.EMPTY );
    }

    /**
     * Returns the maximum account validity date.
     *
     * @return the maximum account validity date
     */
    public String getAccountMaxValidDate() {
        DateFormat dateFormat = new SimpleDateFormat( );
        return Optional.ofNullable( _adminUser.getAccountMaxValidDate( ) ).map( dateFormat::format ).orElse( StringUtils.EMPTY );
    }

    /**
     * Returns the date of the last login.
     *
     * @return the date of the last login
     */
    public String getDateLastLogin() {
        DateFormat dateFormat = new SimpleDateFormat( );
        return Optional.ofNullable( _adminUser.getDateLastLogin( ) ).map( dateFormat::format ).orElse( StringUtils.EMPTY );
    }

    /**
     * Returns the list of roles assigned to the user.
     *
     * @return the list of roles
     */
    public List<String> getRoles() {
        return _roles;
    }

    /**
     * Sets the list of roles assigned to the user.
     *
     * @param roles the list of roles to set
     */
    public void setRoles(List<String> roles) {
        this._roles = roles;
    }

    /**
     * Returns the list of rights assigned to the user.
     *
     * @return the list of rights
     */
    public List<String> getRights() {
        return _rights;
    }

    /**
     * Sets the list of rights assigned to the user.
     *
     * @param rights the list of rights to set
     */
    public void setRights(List<String> rights) {
        this._rights = rights;
    }

    /**
     * Returns the list of workgroups the user belongs to.
     *
     * @return the list of workgroups
     */
    public List<String> getWorkgroups() {
        return _workgroups;
    }

    /**
     * Sets the list of workgroups the user belongs to.
     *
     * @param listWorkgroups the list of workgroups to set
     */
    public void setWorkgroups(List<String> listWorkgroups) {
        this._workgroups = listWorkgroups;
    }

    /**
     * Returns the list of user attributes.
     *
     * @return the list of attributes
     */
    public List<String> getAttributes() {
        return _attributes;
    }

    /**
     * Sets the list of user attributes.
     *
     * @param attributes the list of attributes to set
     */
    public void setAttributes(List<String> attributes) {
        this._attributes = attributes;
    }
}
