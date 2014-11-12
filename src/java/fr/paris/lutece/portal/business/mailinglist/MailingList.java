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
package fr.paris.lutece.portal.business.mailinglist;

import fr.paris.lutece.portal.service.workgroup.WorkgroupRemovalListenerService;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class represents a mailing list composed by admin users. All members of those
 * mailing lists are dynamically retreived by filters based on roles and workgroups.
 */
public class MailingList
{
    private static MailingListWorkgroupRemovalListener _listenerWorkgroup;

    // Variables declarations
    private int _nId;
    private String _strName;
    private String _strDescription;
    private String _strWorkgroup;
    private Collection<MailingListUsersFilter> _listFilters = new ArrayList<MailingListUsersFilter>(  );

    /**
     * Initialize the rule
     */
    public static synchronized void init(  )
    {
        // Create removal listeners and register them
        if ( _listenerWorkgroup == null )
        {
            _listenerWorkgroup = new MailingListWorkgroupRemovalListener(  );
            WorkgroupRemovalListenerService.getService(  ).registerListener( _listenerWorkgroup );
        }
    }

    /**
     * Returns the Id
     *
     * @return The Id
     */
    public int getId(  )
    {
        return _nId;
    }

    /**
     * Sets the Id
     *
     * @param nId The Id
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the Name
     *
     * @return The Name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the Name
     *
     * @param strName The Name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Returns the Description
     *
     * @return The Description
     */
    public String getDescription(  )
    {
        return _strDescription;
    }

    /**
     * Sets the Description
     *
     * @param strDescription The Description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * Returns the Workgroup
     *
     * @return The Workgroup
     */
    public String getWorkgroup(  )
    {
        return _strWorkgroup;
    }

    /**
     * Sets the Workgroup
     *
     * @param strWorkgroup The Workgroup
     */
    public void setWorkgroup( String strWorkgroup )
    {
        _strWorkgroup = strWorkgroup;
    }

    /**
     * Returns filters of the mailing list
     * @return A collection of users filters
     */
    public Collection<MailingListUsersFilter> getFilters(  )
    {
        return _listFilters;
    }
}
