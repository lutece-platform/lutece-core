/*
 * Copyright (c) 2002-2010, Mairie de Paris
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

import fr.paris.lutece.portal.business.rbac.AdminRole;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.user.authentication.AdminAuthentication;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * This Interface defines all methods required for an admin user implementation
 */
public class AdminUser implements Serializable
{
	public static String RESOURCE_TYPE = "ADMIN_USER";
	
    private static final String EMPTY_STRING = "";
    private static final int ACTIVE_CODE = 0;
    private int _nUserId;
    private String _strAccessCode;
    private String _strLastName;
    private String _strFirstName;
    private String _strEmail;
    private int _nStatus;
    private int _nUserLevel;

    /** User's rights */
    private Map<String, Right> _rights = new HashMap<String, Right>(  );

    /** User's roles */
    private Map<String, AdminRole> _roles = new HashMap<String, AdminRole>(  );

    /** Authentication Service */
    private String _strAuthenticationService;

    /** Authentication Service */
    private String _strAuthenticationType;

    /** the user's locale */
    private Locale _locale;

    /**
     * Constructor
     */
    public AdminUser(  )
    {
    }

    /**
     * Constructor
     * @param stAccessCode The User Name
     * @param authenticationService The PortalAuthentication object
     */
    public AdminUser( String stAccessCode, AdminAuthentication authenticationService )
    {
        _strAccessCode = stAccessCode;
        _strAuthenticationService = authenticationService.getAuthServiceName(  );
    }

    /**
     * Get the user's Locale
     * @return The user's locale
     */
    public Locale getLocale(  )
    {
        return ( _locale == null ) ? Locale.getDefault(  ) : _locale;
    }

    /**
     * Set the user Locale
     * @param locale The locale
     */
    public void setLocale( Locale locale )
    {
        _locale = locale;
    }

    /**
     * Return the user's id
     * @return The user id
     */
    public int getUserId(  )
    {
        return _nUserId;
    }

    /**
     * Sets the user's id
     * @param nUserId The User id
     */
    public void setUserId( int nUserId )
    {
        _nUserId = nUserId;
    }

    /**
     * @return Returns the _nStatus.
     */
    public int getStatus(  )
    {
        return _nStatus;
    }

    /**
     * @param nStatus The _nStatus to set.
     */
    public void setStatus( int nStatus )
    {
        _nStatus = nStatus;
    }

    /**
     * Tells whether the current user is active or not
     * @return true if active, false otherwise
     */
    public boolean isStatusActive(  )
    {
        return ( _nStatus == ACTIVE_CODE );
    }

    /**
     * Returns the last name of this user.
     *
     * @return the user last name
     */
    public String getLastName(  )
    {
        return _strLastName;
    }

    /**
     * Sets the last name of the user to the specified string.
     *
     * @param strLastName the new last name
     */
    public void setLastName( String strLastName )
    {
        _strLastName = ( strLastName == null ) ? EMPTY_STRING : strLastName;
    }

    /**
     * Returns the first name of this user.
     *
     * @return the user first name
     */
    public String getFirstName(  )
    {
        return _strFirstName;
    }

    /**
     * Sets the first name of the user to the specified string.
     *
     * @param strFirstName the new first name
     */
    public void setFirstName( String strFirstName )
    {
        _strFirstName = ( strFirstName == null ) ? EMPTY_STRING : strFirstName;
    }

    /**
     * Returns the email of this user.
     *
     * @return the user email
     */
    public String getEmail(  )
    {
        return _strEmail;
    }

    /**
     * Sets the email of the user to the specified string.
     *
     * @param strEmail the new email
     */
    public void setEmail( String strEmail )
    {
        _strEmail = ( strEmail == null ) ? EMPTY_STRING : strEmail;
    }

    /**
     * @return Returns the _strAccessCode.
     */
    public String getAccessCode(  )
    {
        return _strAccessCode;
    }

    /**
     * @param strAccessCode The _strAccessCode to set.
     */
    public void setAccessCode( String strAccessCode )
    {
        _strAccessCode = strAccessCode;
    }

    /**
     * Returns user's roles
     * @return Returns user's roles
     */
    public Map<String, AdminRole> getRoles(  )
    {
        return _roles;
    }

    /**
     * add user's roles
     * @param roles The User roles
     */
    public void addRoles( Map<String, AdminRole> roles )
    {
        _roles.putAll( roles );
    }

    /**
     * Defines user's roles
     * @param roles The User roles
     */
    public void setRoles( Map<String, AdminRole> roles )
    {
        _roles.clear(  );
        _roles.putAll( roles );
    }

    /**
     * Returns user's rights
     * @return Returns user's rights
     */
    public Map<String, Right> getRights(  )
    {
        return _rights;
    }

    /**
     * Verify user rights on a given functionality
     *
     * @param strRightCode right code which corresponding to the functionality
     * @return true if user have this authorisation and false otherwise
     */
    public boolean checkRight( String strRightCode )
    {
        return _rights.containsKey( strRightCode );
    }

    /**
     * Defines user's rights
     * @param rights The User rights
     */
    public void setRights( Map<String, Right> rights )
    {
        _rights.clear(  );
        _rights.putAll( rights );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Authentication infos

    /**
     * Defines the authentification service that had authentified the user
     * @param strAuthenticationService The authentification service
     */
    public void setAuthenticationService( String strAuthenticationService )
    {
        _strAuthenticationService = strAuthenticationService;
    }

    /**
     * Returns the authentification service that had authentified the user
     * @return the authentification service that had authentified the user
     */
    public String getAuthenticationService(  )
    {
        return _strAuthenticationService;
    }

    /**
     * Defines the authentification type that had authentified the user
     * @param strAuthenticationType The authentification type
     */
    public void setAuthenticationType( String strAuthenticationType )
    {
        _strAuthenticationType = strAuthenticationType;
    }

    /**
     * Returns the authentification type that had authentified the user
     * @return the authentification type that had authentified the user
     */
    public String getAuthenticationType(  )
    {
        return _strAuthenticationType;
    }

    /**
     * Defines the user level
     * @param nUserLevel the user level
     */
    public void setUserLevel( int nUserLevel )
    {
        _nUserLevel = nUserLevel;
    }

    /**
     * Returns the user level
     * @return the user level
     */
    public int getUserLevel(  )
    {
        return _nUserLevel;
    }

    /**
     * Check if current user has rights over user
     * @param user the user to check
     * @return true if current user has higher level than user
     */
    public boolean isParent( AdminUser user )
    {
        return _nUserLevel < user.getUserLevel(  );
    }

    /**
     * Check if current user has rights depending on level
     * @param level a level id
     * @return true if current user has higher level than level
     */
    public boolean hasRights( int level )
    {
        return _nUserLevel < level;
    }

    /**
     * Check if this user has admin rights
     * @return true if user has admin rights
     */
    public boolean isAdmin(  )
    {
        return _nUserLevel == 0;
    }

    /**
     * Check if this user has a given role
     * @param strRole The role key
     * @return true if user has the role
     */
    public boolean isInRole( String strRole )
    {
        // Reload roles because roles are only load by the bind and should not be accessible
        // through users list for security reasons
        Map<String, AdminRole> roles = AdminUserHome.getRolesListForUser( getUserId(  ) );

        return roles.containsKey( strRole );
    }
}
