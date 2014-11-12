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
package fr.paris.lutece.portal.business.role;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;


/**
 * This class provides instances management methods (create, find, ...) for Role right objects
 */

//TODO : change Role management (deletion of role 'none', manage role in mylutece exclusively)
public final class RoleHome
{
    //Properties
    private static final String PROPERTY_DEFAULT_ROLE_CODE = "defaultRole.code";
    private static final String PROPERTY_DEFAULT_ROLE_DESCRIPTION = "defaultRole.description";

    // Static variable pointed at the DAO instance
    private static IRoleDAO _dao = (IRoleDAO) SpringContextService.getBean( "roleDAO" );

    /**
     * Creates a new RoleHome object.
     */
    private RoleHome(  )
    {
    }

    /**
     * Creation of an instance of a mode
     *
     * @param role An instance of a role which contains the informations to create
     * @return The instance of a mode which has been created with its primary key.
     */
    public static Role create( Role role )
    {
        if ( !role.getRole(  ).equals( getDefaultRole(  ).getRole(  ) ) )
        {
            _dao.insert( role );
        }

        return role;
    }

    /**
     * Update of the mode which is specified
     *
     * @param role The instance of the role which contains the data to store
     * @return The instance of the mode which has been updated
     */
    public static Role update( Role role )
    {
        if ( !role.getRole(  ).equals( getDefaultRole(  ).getRole(  ) ) )
        {
            _dao.store( role );
        }

        return role;
    }

    /**
     * Remove the mode whose identifier is specified in parameter
     *
     * @param strRole The identifier of the role to remove
     */
    public static void remove( String strRole )
    {
        _dao.delete( strRole );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of an role whose identifier is specified in parameter
     *
     * @param strRole The mode primary key
     * @return an instance of a role
     */
    public static Role findByPrimaryKey( String strRole )
    {
        Role role = getDefaultRole(  );

        if ( !strRole.equals( role.getRole(  ) ) )
        {
            return _dao.load( strRole );
        }

        return role;
    }

    /**
     * Return the list of all roles
     *
     * @return A ReferenceList of roles
     */
    public static ReferenceList getRolesList(  )
    {
        ReferenceList roleList = _dao.selectRolesList(  );
        Role defaultRole = getDefaultRole(  );
        roleList.addItem( defaultRole.getRole(  ), defaultRole.getRoleDescription(  ) );

        return roleList;
    }

    /**
     * Returns the roles list
     *
     * @return Collection of Role
     */
    public static Collection<Role> findAll(  )
    {
        Collection<Role> roleList = _dao.selectAll(  );
        roleList.add( getDefaultRole(  ) );

        return roleList;
    }

    /**
     * Test if the role exists
     * @param strRole The role name
     * @return true if the role exists, otherwise false
     */
    public static boolean findExistRole( String strRole )
    {
        if ( strRole.equals( getDefaultRole(  ).getRole(  ) ) )
        {
            return true;
        }

        return ( findByPrimaryKey( strRole ) == null ) ? false : true;
    }

    /**
     * Return default role
     *
     * @return the default role
     */
    private static Role getDefaultRole(  )
    {
        Role role = new Role(  );
        role.setRole( AppPropertiesService.getProperty( PROPERTY_DEFAULT_ROLE_CODE ) );
        role.setRoleDescription( AppPropertiesService.getProperty( PROPERTY_DEFAULT_ROLE_DESCRIPTION ) );

        return role;
    }

    /**
     * Return the list of all roles
     * @param user The Admin User
     * @return A ReferenceList of roles
     */
    public static ReferenceList getRolesList( AdminUser user )
    {
        Collection<Role> listRoles = RoleHome.findAll(  );
        listRoles = AdminWorkgroupService.getAuthorizedCollection( listRoles, user );

        ReferenceList roleList = new ReferenceList(  );

        for ( Role role : listRoles )
        {
            roleList.addItem( role.getRole(  ), role.getRoleDescription(  ) );
        }

        return roleList;
    }
}
