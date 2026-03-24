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

import java.io.Serializable;
import java.util.List;

public class AdminUserExport implements Serializable
{

    private String _strAccessCode;
    private String _strLastName;
    private String _strFirstName;
    private String _strEmail;
    private String _strStatus;
    private String _strLocale;
    private String _strLevel;
    private String _strPasswordReset;
    private String _strAccessibilityMode;
    private String _strPasswordMaxValidDate;
    private String _strAccountMaxValidDate;
    private String _strDateLastLogin;

    private List<String> _roles;
    private List<String> _rights;
    private List<String> _workgroups;
    private List<AdminUserAttributeExport> _attributes;

    /**
     * Returns the user access code.
     *
     * @return the user access code
     */
    public String getAccessCode() {
        return _strAccessCode;
    }

    /**
     * Sets the user access code.
     *
     * @param accessCode the user access code to set
     */
    public void setAccessCode(String accessCode) {
        this._strAccessCode = accessCode;
    }

    /**
     * Returns the user last name.
     *
     * @return the user last name
     */
    public String getLastName() {
        return _strLastName;
    }

    /**
     * Sets the user last name.
     *
     * @param lastName the user last name to set
     */
    public void setLastName(String lastName) {
        this._strLastName = lastName;
    }

    /**
     * Returns the user first name.
     *
     * @return the user first name
     */
    public String getFirstName() {
        return _strFirstName;
    }

    /**
     * Sets the user first name.
     *
     * @param firstName the user first name to set
     */
    public void setFirstName(String firstName) {
        this._strFirstName = firstName;
    }

    /**
     * Returns the user email address.
     *
     * @return the user email address
     */
    public String getEmail() {
        return _strEmail;
    }

    /**
     * Sets the user email address.
     *
     * @param email the user email address to set
     */
    public void setEmail(String email) {
        this._strEmail = email;
    }

    /**
     * Returns the user status.
     *
     * @return the user status
     */
    public String getStatus() {
        return _strStatus;
    }

    /**
     * Sets the user status.
     *
     * @param status the user status to set
     */
    public void setStatus(String status) {
        this._strStatus = status;
    }

    /**
     * Returns the user locale.
     *
     * @return the user locale
     */
    public String getLocale() {
        return _strLocale;
    }

    /**
     * Sets the user locale.
     *
     * @param locale the user locale to set
     */
    public void setLocale(String locale) {
        this._strLocale = locale;
    }

    /**
     * Returns the user level.
     *
     * @return the user level
     */
    public String getLevel() {
        return _strLevel;
    }

    /**
     * Sets the user level.
     *
     * @param level the user level to set
     */
    public void setLevel(String level) {
        this._strLevel = level;
    }

    /**
     * Returns whether the user must reset the password.
     *
     * @return the password reset flag
     */
    public String getPasswordReset() {
        return _strPasswordReset;
    }

    /**
     * Sets whether the user must reset the password.
     *
     * @param passwordReset the password reset flag to set
     */
    public void setPasswordReset(String passwordReset) {
        this._strPasswordReset = passwordReset;
    }

    /**
     * Returns the user accessibility mode.
     *
     * @return the accessibility mode
     */
    public String getAccessibilityMode() {
        return _strAccessibilityMode;
    }

    /**
     * Sets the user accessibility mode.
     *
     * @param accessibilityMode the accessibility mode to set
     */
    public void setAccessibilityMode(String accessibilityMode) {
        this._strAccessibilityMode = accessibilityMode;
    }

    /**
     * Returns the maximum password validity date.
     *
     * @return the maximum password validity date
     */
    public String getPasswordMaxValidDate() {
        return _strPasswordMaxValidDate;
    }

    /**
     * Sets the maximum password validity date.
     *
     * @param passwordMaxValidDate the maximum password validity date to set
     */
    public void setPasswordMaxValidDate(String passwordMaxValidDate) {
        this._strPasswordMaxValidDate = passwordMaxValidDate;
    }

    /**
     * Returns the maximum account validity date.
     *
     * @return the maximum account validity date
     */
    public String getAccountMaxValidDate() {
        return _strAccountMaxValidDate;
    }

    /**
     * Sets the maximum account validity date.
     *
     * @param accountMaxValidDate the maximum account validity date to set
     */
    public void setAccountMaxValidDate(String accountMaxValidDate) {
        this._strAccountMaxValidDate = accountMaxValidDate;
    }

    /**
     * Returns the date of the last login.
     *
     * @return the date of the last login
     */
    public String getDateLastLogin() {
        return _strDateLastLogin;
    }

    /**
     * Sets the date of the last login.
     *
     * @param dateLastLogin the date of the last login to set
     */
    public void setDateLastLogin(String dateLastLogin) {
        this._strDateLastLogin = dateLastLogin;
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
    public List<AdminUserAttributeExport> getAttributes() {
        return _attributes;
    }

    /**
     * Sets the list of user attributes.
     *
     * @param attributes the list of attributes to set
     */
    public void setAttributes(List<AdminUserAttributeExport> attributes) {
        this._attributes = attributes;
    }

}
