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
package fr.paris.lutece.portal.service.security;

import org.apache.commons.lang.ObjectUtils;

import java.io.Serializable;

import java.security.Principal;

import java.sql.Timestamp;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * This Interface defines all methods required for a Lutece user implementation
 */
public abstract class LuteceUser implements Principal, Serializable, Cloneable
{
    /* These attribute names are derived from the Platform for Privacy
     * Preferences 1.0 (P3P 1.0) Specification by the
     * W3C (http://www.w3c.org/TR/P3P). The same attribute names are also being considered
     * by the OASIS Web Services for Remote Portlets Technical Committee.
     */
    public static final String BDATE = "user.bdate";
    public static final String GENDER = "user.gender";
    public static final String EMPLOYER = "user.employer";
    public static final String DEPARTMENT = "user.department";
    public static final String JOBTITLE = "user.jobtitle";
    public static final String PREFIX = "user.name.prefix";
    public static final String DATE_LAST_LOGIN = "user.lastLogin";
    public static final String NAME_GIVEN = "user.name.given";
    public static final String NAME_FAMILY = "user.name.family";
    public static final String NAME_MIDDLE = "user.name.middle";
    public static final String NAME_SUFFIX = "user.name.suffix";
    public static final String NAME_NICKNAME = "user.name.nickName";
    public static final String NAME_CIVILITY = "user.name.civility";
    public static final String HOME_INFO_POSTAL_NAME = "user.home-info.postal.name";
    public static final String HOME_INFO_POSTAL_STREET = "user.home-info.postal.street";
    public static final String HOME_INFO_POSTAL_STREET_NUMBER = "user.home-info.postal.street.number";
    public static final String HOME_INFO_POSTAL_STREET_SUFFIX = "user.home-info.postal.street.suffix";
    public static final String HOME_INFO_POSTAL_STREET_NAME = "user.home-info.postal.street.name";
    public static final String HOME_INFO_POSTAL_STREET_TYPE = "user.home-info.postal.street.type";
    public static final String HOME_INFO_POSTAL_STREET_URBAN_DISTRICT = "user.home-info.postal.street.urbandistrict";
    public static final String HOME_INFO_POSTAL_CITY = "user.home-info.postal.city";
    public static final String HOME_INFO_POSTAL_STATEPROV = "user.home-info.postal.stateprov";
    public static final String HOME_INFO_POSTAL_POSTALCODE = "user.home-info.postal.postalcode";
    public static final String HOME_INFO_POSTAL_COUNTRY = "user.home-info.postal.country";
    public static final String HOME_INFO_POSTAL_ORGANIZATION = "user.home-info.postal.organization";
    public static final String HOME_INFO_TELECOM_TELEPHONE_INTCODE = "user.home-info.telecom.telephone.intcode";
    public static final String HOME_INFO_TELECOM_TELEPHONE_LOCCODE = "user.home-info.telecom.telephone.loccode";
    public static final String HOME_INFO_TELECOM_TELEPHONE_NUMBER = "user.home-info.telecom.telephone.number";
    public static final String HOME_INFO_TELECOM_TELEPHONE_EXT = "user.home-info.telecom.telephone.ext";
    public static final String HOME_INFO_TELECOM_TELEPHONE_COMMENT = "user.home-info.telecom.telephone.comment";
    public static final String HOME_INFO_TELECOM_FAX_INT = "user.home-info.telecom.fax.intcode";
    public static final String HOME_INFO_TELECOM_FAX_LOCCODE = "user.home-info.telecom.fax.loccode";
    public static final String HOME_INFO_TELECOM_FAX_NUMBER = "user.home-info.telecom.fax.number";
    public static final String HOME_INFO_TELECOM_FAX_EXT = "user.home-info.telecom.fax.ext";
    public static final String HOME_INFO_TELECOM_FAX_COMMENT = "user.home-info.telecom.fax.comment";
    public static final String HOME_INFO_TELECOM_MOBILE_INTCODE = "user.home-info.telecom.mobile.intcode";
    public static final String HOME_INFO_TELECOM_MOBILE_LOCCODE = "user.home-info.telecom.mobile.loccode";
    public static final String HOME_INFO_TELECOM_MOBILE_NUMBER = "user.home-info.telecom.mobile.number";
    public static final String HOME_INFO_TELECOM_MOBILE_EXT = "user.home-info.telecom.mobile.ext";
    public static final String HOME_INFO_TELECOM_MOBILE_COMMENT = "user.home-info.telecom.mobile.comment";
    public static final String HOME_INFO_TELECOM_PAGER_INTCODE = "user.home-info.telecom.pager.intcode";
    public static final String HOME_INFO_TELECOM_PAGER_LOCCODE = "user.home-info.telecom.pager.loccode";
    public static final String HOME_INFO_TELECOM_PAGER_NUMBER = "user.home-info.telecom.pager.number";
    public static final String HOME_INFO_TELECOM_PAGER_EXT = "user.home-info.telecom.pager.ext";
    public static final String HOME_INFO_TELECOM_PAGER_COMMENT = "user.home-info.telecom.pager.comment";
    public static final String HOME_INFO_ONLINE_EMAIL = "user.home-info.online.email";
    public static final String HOME_INFO_ONLINE_URI = "user.home-info.online.uri";
    public static final String BUSINESS_INFO_POSTAL_NAME = "user.business-info.postal.name";
    public static final String BUSINESS_INFO_POSTAL_STREET = "user.business-info.postal.street";
    public static final String BUSINESS_INFO_POSTAL_CITY = "user.business-info.postal.city";
    public static final String BUSINESS_INFO_POSTAL_STATEPROV = "user.business-info.postal.stateprov";
    public static final String BUSINESS_INFO_POSTAL_POSTALCODE = "user.business-info.postal.postalcode";
    public static final String BUSINESS_INFO_POSTAL_COUNTRY = "user.business-info.postal.country";
    public static final String BUSINESS_INFO_POSTAL_ORGANIZATION = "user.business-info.postal.organization";
    public static final String BUSINESS_INFO_TELECOM_TELEPHONE_INTCODE = "user.business-info.telecom.telephone.intcode";
    public static final String BUSINESS_INFO_TELECOM_TELEPHONE_LOCCODE = "user.business-info.telecom.telephone.loccode";
    public static final String BUSINESS_INFO_TELECOM_TELEPHONE_NUMBER = "user.business-info.telecom.telephone.number";
    public static final String BUSINESS_INFO_TELECOM_TELEPHONE_EXT = "user.business-info.telecom.telephone.ext";
    public static final String BUSINESS_INFO_TELECOM_TELEPHONE_COMMENT = "user.business-info.telecom.telephone.comment";
    public static final String BUSINESS_INFO_TELECOM_FAX_INTCODE = "user.business-info.telecom.fax.intcode";
    public static final String BUSINESS_INFO_TELECOM_FAX_LOCCODE = "user.business-info.telecom.fax.loccode";
    public static final String BUSINESS_INFO_TELECOM_FAX_NUMBER = "user.business-info.telecom.fax.number";
    public static final String BUSINESS_INFO_TELECOM_FAX_EXT = "user.business-info.telecom.fax.ext";
    public static final String BUSINESS_INFO_TELECOM_FAX_COMMENT = "user.business-info.telecom.fax.comment";
    public static final String BUSINESS_INFO_TELECOM_MOBILE_INTCODE = "user.business-info.telecom.mobile.intcode";
    public static final String BUSINESS_INFO_TELECOM_MOBILE_LOCCODE = "user.business-info.telecom.mobile.loccode";
    public static final String BUSINESS_INFO_TELECOM_MOBILE_NUMBER = "user.business-info.telecom.mobile.number";
    public static final String BUSINESS_INFO_TELECOM_MOBILE_EXT = "user.business-info.telecom.mobile.ext";
    public static final String BUSINESS_INFO_TELECOM_MOBILE_COMMENT = "user.business-info.telecom.mobile.comment";
    public static final String BUSINESS_INFO_TELECOM_PAGER_INTCODE = "user.business-info.telecom.pager.intcode";
    public static final String BUSINESS_INFO_TELECOM_PAGER_LOCCODE = "user.business-info.telecom.pager.loccode";
    public static final String BUSINESS_INFO_TELECOM_PAGER_NUMBER = "user.business-info.telecom.pager.number";
    public static final String BUSINESS_INFO_TELECOM_PAGER_EXT = "user.business-info.telecom.pager.ext";
    public static final String BUSINESS_INFO_TELECOM_PAGER_COMMENT = "user.business-info.telecom.pager.comment";
    public static final String BUSINESS_INFO_ONLINE_EMAIL = "user.business-info.online.email";
    public static final String BUSINESS_INFO_ONLINE_URI = "user.business-info.online.uri";
    public static final String ANONYMOUS_USERNAME = "GUEST";
    public static final Timestamp DEFAULT_DATE_LAST_LOGIN = Timestamp.valueOf( "1980-01-01 00:00:00" );
    private static final long serialVersionUID = -8733640540563208835L;

    /** Map containing users info */
    private Map<String, String> _mapUserInfo = new HashMap<String, String>(  );

    /** User's name */
    private String _strUserName;

    /** User's roles */
    private String[] _roles;

    /** User's groups */
    private String[] _groups;

    /** Authentication Service */
    private String _strAuthenticationService;

    /** Authentication Service impl */
    private LuteceAuthentication _luteceAuthenticationService;

    /** Authentication Service */
    private String _strAuthenticationType;

    /**
     * Constructor
     * @param strUserName The User Name
     * @param authenticationService The PortalAuthentication object
     */
    public LuteceUser( String strUserName, LuteceAuthentication authenticationService )
    {
        _strUserName = strUserName;
        _strAuthenticationService = authenticationService.getAuthServiceName(  );
        _luteceAuthenticationService = authenticationService;
    }

    /**
     * Gets the user info map
     * @return The user info map
     */
    public final Map<String, String> getUserInfos(  )
    {
        return _mapUserInfo;
    }

    /**
     * Add an user's info
     * @param key The info key
     * @param value The info value
     */
    public final void setUserInfo( String key, String value )
    {
        _mapUserInfo.put( key, value );
    }

    /**
     * Gets a user's info
     * @param key The info key
     * @return The info value
     */
    public final String getUserInfo( String key )
    {
        String strInfo = _mapUserInfo.get( key );

        return ( strInfo == null ) ? "" : strInfo;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Principal Interface Implementation

    /**
     * equals implementation
     * @param object The object to compare
     * @return true if equal, otherwise false
     */
    @Override
    public boolean equals( Object object )
    {
        // FIXME : use LuteceUser property instead of object.toString()
        return ObjectUtils.equals( this.toString(  ), ObjectUtils.toString( object ) );
    }

    /**
     * toString implementation
     * @return The username
     */
    @Override
    public String toString(  )
    {
        return _strUserName;
    }

    /**
     * hashCode implementation
     * @return The hashcode
     */
    @Override
    public int hashCode(  )
    {
        return ( _strUserName == null ) ? 0 : _strUserName.hashCode(  );
    }

    /**
     * Return the user's name
     * @return The username
     */
    @Override
    public String getName(  )
    {
        return _strUserName;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Other user's info methods

    /**
     * Sets the user's name
     * @param strName The User name
     */
    public void setName( String strName )
    {
        _strUserName = strName;
    }

    /**
     * Returns user's roles
     * @return Returns user's roles
     */
    public String[] getRoles(  )
    {
        return _roles;
    }

    /**
     * add user's roles
     * @param roles The User roles
     */
    public void addRoles( Collection<String> roles )
    {
        _roles = addInArray( _roles, roles );
    }

    /**
     * Defines user's roles
     * @param roles The User roles
     */
    public void setRoles( Collection<String> roles )
    {
        _roles = getArray( roles );
    }

    /**
     * Returns user's groups
     * @return Returns user's groups
     */
    public String[] getGroups(  )
    {
        return _groups;
    }

    /**
     * add user's groups
     * @param groups The User groups
     */
    public void addGroups( Collection<String> groups )
    {
        _groups = addInArray( _groups, groups );
    }

    /**
     * Defines user's groups
     * @param groups The User groups
     */
    public void setGroups( Collection<String> groups )
    {
        _groups = getArray( groups );
    }

    /**
     * Add elements of a collection into an array
     *
     * @param array the array to fill
     * @param collection the collection containing the elements to add
     * @return The new array
     */
    private String[] addInArray( String[] array, Collection<String> collection )
    {
        String[] newArray;

        int j = 0;

        if ( array == null )
        {
            newArray = new String[collection.size(  )];
        }
        else
        {
            newArray = new String[collection.size(  ) + array.length];

            for ( j = 0; j < array.length; j++ )
            {
                newArray[j] = array[j];
            }
        }

        for ( String strItem : collection )
        {
            newArray[j++] = strItem;
        }

        return newArray;
    }

    /**
     * Set elements of a collection in array
     *
     * @param collection the collection containing the elements to add
     * @return An array
     */
    private String[] getArray( Collection<String> collection )
    {
        String[] newArray = new String[collection.size(  )];

        int j = 0;

        for ( String strItem : collection )
        {
            newArray[j++] = strItem;
        }

        return newArray;
    }

    // //////////////////////////////////////////////////////////////////////////
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
     * "Getter method" for {@link #_luteceAuthenticationService}
     * @return value of {@link #_luteceAuthenticationService}
     */
    public LuteceAuthentication getLuteceAuthenticationService(  )
    {
        return _luteceAuthenticationService;
    }

    /**
     * "Setter method" for {@link #_luteceAuthenticationService}.
     * @param authenticationService new value of {@link #_luteceAuthenticationService}
     */
    public void setLuteceAuthenticationService( LuteceAuthentication authenticationService )
    {
        _luteceAuthenticationService = authenticationService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object clone(  ) throws CloneNotSupportedException
    {
        return super.clone(  );
    }

    /**
     * Get the users email
     * @return The email
     */
    public String getEmail(  )
    {
        return null;
    }
}
