/*
 * Copyright (c) 2002-2009, Mairie de Paris
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

import fr.paris.lutece.portal.service.rbac.RBACResource;

import java.util.List;


/**
 *
 */
public class State implements IReferenceItem, RBACResource
{
    public static final String RESOURCE_TYPE = "WORKFLOW_STATE_TYPE";

    // Variables declarations
    private int _nId;
    private Workflow _workflow;
    private String _strName;
    private String _strDescription;
    private List<Action> _listActions;
    private Boolean _bInitialState;
    private Boolean _bRequiredWorkgroupAssigned;
    

    /**
     *
     * @return the id of the workflow state
     */
    public int getId(  )
    {
        return _nId;
    }

    /**
     * set the id of the workflow state
     * @param idState the id of the workflow state
     */
    public void setId( int idState )
    {
        _nId = idState;
    }

    /**
    *
    * @return the workflow associated
    */
    public Workflow getWorkflow(  )
    {
        return _workflow;
    }

    /**
     * set the the workflow associated
     * @param workflow the workflow associated
     */
    public void setWorkflow( Workflow workflow )
    {
        _workflow = workflow;
    }

    /**
     * Returns the action name
     *
     * @return the action name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Set the action name
     *
     * @param strName the action name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Returns the action description
     *
     * @return  the action description
     */
    public String getDescription(  )
    {
        return _strDescription;
    }

    /**
     * Set the action description
     *
     * @param strDescription The Description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     *return the list of all actions associated to the state
     * @return the list of all actions associated to the state
     */
    public List<Action> getAllActions(  )
    {
        return _listActions;
    }

    /**
     *set the list of all actions associated to the state
     * @param listActions the list of all actions associated to the state
     */
    public void setAllActions( List<Action> listActions )
    {
        _listActions = listActions;
    }

    /**
     *
     * @return true if the state is the initial state of the workflow
     */
    public Boolean isInitialState(  )
    {
        return _bInitialState;
    }

    /**
     *  set true if the state is the initial state of the workflow
     * @param isInitialState true if the state is the initial state of the workflow
     */
    public void setInitialState( Boolean isInitialState )
    {
        _bInitialState = isInitialState;
    }

    /**
     * RBAC resource implementation
     * @return The resource type code
     */
    public String getResourceTypeCode(  )
    {
        return RESOURCE_TYPE;
    }

    /**
     * RBAC resource implmentation
     * @return The resourceId
     */
    public String getResourceId(  )
    {
        return Integer.toString( _nId );
    }

    /**
     * 
     * @return return true if the resources of this state shall be assigned to a workgroup
     */
	public Boolean isRequiredWorkgroupAssigned() {
		return _bRequiredWorkgroupAssigned;
	}
	/**
	 * set true return true if the resources of this state shall be assigned to a workgroup
	 * @param requireUserAssigned true return true if the resources of this state shall be assigned to to a workgroup
	 */
	public void setRequiredWorkgroupAssigned(Boolean requireUserAssociated) {
		_bRequiredWorkgroupAssigned = requireUserAssociated;
	}
}
