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
package fr.paris.lutece.portal.business.workflow;

import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupResource;

import java.sql.Timestamp;

import java.util.List;


/**
 * This is the business class for the object Workflow
 */
public class Workflow implements AdminWorkgroupResource, IReferenceItem
{
    // Variables declarations
    private int _nId;
    private String _strName;
    private String _strDescription;
    private List<Action> _listActions;
    private List<State> _listStates;
    private Timestamp _tCreationDate;
    private String _strWorkgroupKey;
    private boolean _bEnabled;

    /**
     * Initialize the workflow
     */
    public static void init(  )
    {
    }

    /**
     * Returns the IdWorkflow
     * @return The IdWorkflow
     */
    public int getId(  )
    {
        return _nId;
    }

    /**
     * Sets the IdWorkflow
     * @param nIdWorkflow The IdWorkflow
     */
    public void setId( int nIdWorkflow )
    {
        _nId = nIdWorkflow;
    }

    /**
     * Returns the Name
     * @return The Name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the Name
     * @param strName The Name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Returns the Description
     * @return The Description
     */
    public String getDescription(  )
    {
        return _strDescription;
    }

    /**
     * Sets the Description
     * @param strDescription The Description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     *return the list of all actions associated to the workflow
     * @return the list of all actions associated to the workflow
     */
    public List<Action> getAllActions(  )
    {
        return _listActions;
    }

    /**
     *set the list of all actions associated to the workflow
     * @param listActions the list of all actions associated to the workflow
     */
    public void setAllActions( List<Action> listActions )
    {
        _listActions = listActions;
    }

    /**
     * return the list of all states associated to the workflow
     * @return the list of all states associated to the workflow
     */
    public List<State> getAllStates(  )
    {
        return _listStates;
    }

    /**
     *set the list of all states associated to the workflow
     * @param listStates the list of all states associated to the workflow
     */
    public void setAllStates( List<State> listStates )
    {
        _listStates = listStates;
    }

    /**
    *
    * @return the work group associate to the workflow
    */
    public String getWorkgroup(  )
    {
        return _strWorkgroupKey;
    }

    /**
     * set  the work group associate to the workflow
     * @param workGroup  the work group associate to the workflow
     */
    public void setWorkgroup( String workGroup )
    {
        _strWorkgroupKey = workGroup;
    }

    /**
    *
    * @return the creation date
    */
    public Timestamp getCreationDate(  )
    {
        return _tCreationDate;
    }

    /**
     * set the creation date
     * @param dateCreation the creation date
     */
    public void setCreationDate( Timestamp dateCreation )
    {
        _tCreationDate = dateCreation;
    }

    /**
    *
    * @return true if the workflow is enabled
    */
    public boolean isEnabled(  )
    {
        return _bEnabled;
    }

    /**
     * set true if the workflow is enabled
     * @param enable true if the workflow is enabled
     */
    public void setEnabled( boolean enable )
    {
        _bEnabled = enable;
    }
}
