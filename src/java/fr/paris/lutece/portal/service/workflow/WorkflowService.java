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
package fr.paris.lutece.portal.service.workflow;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.workflow.Action;
import fr.paris.lutece.portal.business.workflow.State;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 * WorkflowService
 */
public class WorkflowService
{
    private static final String PLUGIN_WORKFLOW_NAME = "workflow";
    private static WorkflowService _singleton;
    private boolean _bServiceAvailable = true;
    private IWorkflowService _service;

    /**
     * Private constructor
     */
    private WorkflowService(  )
    {
        try
        {
            _service = (IWorkflowService) SpringContextService.getPluginBean( PLUGIN_WORKFLOW_NAME, "workflowService" );
            _bServiceAvailable = _service != null;
        }
        catch ( BeanDefinitionStoreException e )
        {
            _bServiceAvailable = false;
        }
        catch ( NoSuchBeanDefinitionException e )
        {
            _bServiceAvailable = false;
        }
        catch ( CannotLoadBeanClassException e )
        {
            _bServiceAvailable = false;
        }
    }

    /**
    * Returns the unique instance of the service
    * @return The instance of the service
    */
    public static WorkflowService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = new WorkflowService(  );
        }

        return _singleton;
    }

    /**
    *
    * @return true if the workflow service is available
    */
    public boolean isAvailable(  )
    {
        return _bServiceAvailable && PluginService.isPluginEnable( PLUGIN_WORKFLOW_NAME );
    }

    /**
     * returns a list of actions possible for a given document based on the status
     * of the document in the workflow and the user role
     * @param nIdResource the document id
     * @param strResourceType the document type
     * @param user the adminUser
     * @param nIdWorkflow the workflow id
     * @return a list of Action
     */
    public Collection<Action> getActions( int nIdResource, String strResourceType, int nIdWorkflow, AdminUser user )
    {
        return isAvailable(  ) ? _service.getActions( nIdResource, strResourceType, nIdWorkflow, user ) : null;
    }
    
    /**
     * returns a list of actions possible for a given document based on the status
     * of the document in the workflow and the user role
     * @param listIdResssource the list of resource id
     * @param strResourceType the document type
     * @param user the adminUser
     * @param nIdWorkflow the workflow id
     * @return a list of Action
     */
    public HashMap<Integer, List<Action>> getActions( List<Integer> listIdResssource, String strResourceType, Integer nIdExternalParentId, int nIdWorkflow, AdminUser user )
    {
    	return isAvailable(  ) ? _service.getActions(listIdResssource, strResourceType, nIdExternalParentId, nIdWorkflow, user) : null;
    }

    /**
    * return true if a form is associate to the action
    *
    * @param nIdAction the action id
    * @param locale the loacle
    * @return true if a form is associate to the action
    */
    public boolean isDisplayTasksForm( int nIdAction, Locale locale )
    {
        return isAvailable(  ) ? _service.isDisplayTasksForm( nIdAction, locale ) : false;
    }

    /**
     * Proceed action given in parameter
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param request the request
     * @param nIdAction the action id
     * @param locale locale
     */
    @Deprecated
    public void doProcessAction( int nIdResource, String strResourceType, int nIdAction, HttpServletRequest request,
        Locale locale, boolean isAutomatic )
    {
    	this.doProcessAction(nIdResource, strResourceType, nIdAction, null, request, locale, isAutomatic);
    }

    /**
     * Proceed action given in parameter
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param nIdAction the action id
     * @param nExternalParentId the external parent id
     * @param request the request
     * @param locale locale
     */
    public void doProcessAction( int nIdResource, String strResourceType, int nIdAction, Integer nExternalParentId ,HttpServletRequest request,
            Locale locale, boolean isAutomatic )
    {
        if ( isAvailable(  ) )
        {
            _service.doProcessAction( nIdResource, strResourceType, nIdAction, nExternalParentId, request, locale, isAutomatic );
        }
    }

    /**
     * returns the  actions history performed on a resource
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param request the request
     * @param nIdWorkflow the workflow id
     * @param locale the locale
     * @return the history of actions performed on a resource
     */
    public String getDisplayDocumentHistory( int nIdResource, String strResourceType, int nIdWorkflow,
        HttpServletRequest request, Locale locale )
    {
        return isAvailable(  )
        ? _service.getDisplayDocumentHistory( nIdResource, strResourceType, nIdWorkflow, request, locale ) : null;
    }

    /**
     * returns a xml wich contains the  actions history performed on a resource
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param request the request
     * @param nIdWorkflow the workflow id
     * @param locale the locale
     * @return a xml wich contains  the history of actions performed on a resource
     */
    public String getDocumentHistoryXml( int nIdResource, String strResourceType, int nIdWorkflow,
        HttpServletRequest request, Locale locale )
    {
        return isAvailable(  )
        ? _service.getDocumentHistoryXml( nIdResource, strResourceType, nIdWorkflow, request, locale ) : null;
    }

    /**
     * Perform the information on the various tasks associated with the given action specified in parameter
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param request the request
     * @param nIdAction the action id
     * @param locale the locale
     * @return null if there is no error in the task form
     *                    else return the error message url
     */
    @Deprecated
    public String doSaveTasksForm( int nIdResource, String strResourceType, int nIdAction, HttpServletRequest request,
        Locale locale )
    {
    	return this.doSaveTasksForm(nIdResource, strResourceType, nIdAction, null, request, locale);
    }
    
    /**
     * Perform the information on the various tasks associated with the given action specified in parameter
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param nExternalParentId the external parent id
     * @param request the request
     * @param nIdAction the action id
     * @param locale the locale
     * @return null if there is no error in the task form
     *                    else return the error message url
     */    
    public String doSaveTasksForm( int nIdResource, String strResourceType, int nIdAction, Integer nExternalParentId,
    		HttpServletRequest request, Locale locale )
    {
        return isAvailable(  ) ? _service.doSaveTasksForm( nIdResource, strResourceType, nIdAction, nExternalParentId,
        		request, locale ): null;
    }

    /**
     * Remove in all workflows the resource specified in parameter
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     */
    public void doRemoveWorkFlowResource( int nIdResource, String strResourceType )
    {
        if ( isAvailable(  ) )
        {
            _service.doRemoveWorkFlowResource( nIdResource, strResourceType );
        }
    }
    
    /**
     * Remove list of resource workflow by list id
     * @param lListIdResource list of id resource
     * @param strResourceType the ressource type
     * @param nIdWorflow the workflow id
     */
    public void doRemoveWorkFlowResourceByListId( List<Integer> lListIdResource, String strResourceType, Integer nIdWorflow )
    {
        if ( isAvailable(  ) )
        {
            _service.doRemoveWorkFlowResourceByListId( lListIdResource, strResourceType, nIdWorflow );
        }
    }

    /**
     * returns the tasks form
     * @param nIdResource the document id
     * @param strResourceType the document type
     * @param request the request
     * @param nIdAction the action id
     * @param locale the locale
     * @return the tasks form associated to the action
     *
     */
    public String getDisplayTasksForm( int nIdResource, String strResourceType, int nIdAction,
        HttpServletRequest request, Locale locale )
    {
        return isAvailable(  )
        ? _service.getDisplayTasksForm( nIdResource, strResourceType, nIdAction, request, locale ) : null;
    }

    /**
     * Check that a given user is allowed to view a resource depending the state of the resource
     * @param nIdResource the document id
     * @param strResourceType the document type
     * @param  user the AdminUser
     * @param nIdWorkflow the workflow id
     * @return a list of Action
     */
    public boolean isAuthorized( int nIdResource, String strResourceType, int nIdWorkflow, AdminUser user )
    {
        return _bServiceAvailable && _service.isAuthorized( nIdResource, strResourceType, nIdWorkflow, user );
    }

    /**
     * Get all authorized resource Id
     * @param strResourceType the resource type
     * @param nIdWorkflow the workflow id
     * @param nIdWorkflowState The workflow state id or -1 for all workflow states
     * @param user the AdminUser
     * @return a list resource id
     */
    @Deprecated
    public List<Integer> getAuthorizedResourceList( String strResourceType, int nIdWorkflow, int nIdWorkflowState, AdminUser user )
    {
        return this.getAuthorizedResourceList(strResourceType, nIdWorkflow, nIdWorkflowState, null, user);
    }
    
    /**
     * Get all authorized resource Id
     * @param strResourceType the resource type
     * @param nIdWorkflow the workflow id
     * @param nIdWorkflowState The workflow state id or -1 for all workflow states
     * @param nExternalParentId The external parent id
     * @param user the AdminUser
     * @return a list resource id
     */
    public List<Integer> getAuthorizedResourceList( String strResourceType, int nIdWorkflow, int nIdWorkflowState, 
    		Integer nExternalParentId, AdminUser user )
	{
    	return isAvailable( )
    	? _service.getAuthorizedResourceList( strResourceType, nIdWorkflow, nIdWorkflowState, nExternalParentId, user ) : null;
	}
    
    /**
     * Get all authorized resource Id by list of state
     * @param strResourceType the resource type
     * @param nIdWorkflow the workflow id
     * @param lListIdWorkflowState The workflow state <b>id or null</b> for all workflow states
     * @param nExternalParentId
     * @param user the AdminUser
     * @return a list resource id
     */
    public List<Integer> getAuthorizedResourceList( String strResourceType, int nIdWorkflow, List<Integer> lListIdWorkflowState, 
    		Integer nExternalParentId, AdminUser user )
    {
        return isAvailable(  )
        ? _service.getAuthorizedResourceList( strResourceType, nIdWorkflow, lListIdWorkflowState, nExternalParentId, user ) : null;
    }
    

    /**
     * return a reference list which contains a list enabled workflow
     * @param user the AdminUser
     * @param locale the locale
     * @return a reference list which contains a list enabled workflow
     */
    public ReferenceList getWorkflowsEnabled( AdminUser user, Locale locale )
    {
        return isAvailable(  ) ? _service.getWorkflowsEnabled( user, locale ) : null;
    }

    /**
     * returns all state of a  given workflow
     * @param user the adminUser
     * @param nListIdWorkflow the workflow id
     * @return the state of a given document
     */
    public Collection<State> getAllStateByWorkflow( int nListIdWorkflow, AdminUser user )
    {
        return isAvailable(  ) ? _service.getAllStateByWorkflow( nListIdWorkflow, user ) : null;
    }

    /**
     * <b> WRONG : need to filter on workflow id</b>
     * Returns a list which contains idResource for a given filter
     * @param nIdState the id State
     * @param strResource the name of resource
     * @param user the AdminUser
     * @return a list which contains idRessource
     */
    @Deprecated
    public Collection<Integer> getListIdRessourceByState( int nIdState, String strResource, AdminUser user )
    {
        return isAvailable(  ) ? _service.getListIdRessourceByState( nIdState, strResource, user ) : null;
    }

    /**
     * returns the state of a  given document
     * of the document in the workflow and the user role
     * @param nIdResource the document id
     * @param strResourceType the document type
     * @param user the adminUser
     * @param nIdWorkflow the workflow id
     * @return the state of a given document
     */
    @Deprecated
    public State getState( int nIdResource, String strResourceType, int nIdWorkflow, AdminUser user )
    {
    	return this.getState(nIdResource, strResourceType, nIdWorkflow, null ,user);
    }
    
    /**
     * returns the state of a  given document
     * of the document in the workflow and the user role
     * @param nIdResource the document id
     * @param strResourceType the document type
     * @param nExternalParentId the external parent id
     * @param user the adminUser
     * @param nIdWorkflow the workflow id
     * @return the state of a given document
     */
    public State getState( int nIdResource, String strResourceType, int nIdWorkflow, Integer nIdExternalParentId, AdminUser user )
    {
    	return isAvailable(  ) ? _service.getState( nIdResource, strResourceType, nIdWorkflow, nIdExternalParentId, user ) : null;
    }
    
    /**
     * Execute action automatic
     * @param nIdResource the document id
     * @param strResourceType the document type
     * @param nIdWorkflow the workflow id
     * @param nExternalParentId the external parent id
     */
    public void executeActionAutomatic( int nIdResource, String strResourceType, int nIdWorkflow, Integer nExternalParentId )
    {
    	_service.executeActionAutomatic( nIdResource, strResourceType, nIdWorkflow , nExternalParentId);
    }

    /**
     * Execute action automatic
     * @param nIdResource the document id
     * @param strResourceType the document type
     * @param nIdWorkflow the workflow id
     */
    @Deprecated
    public void executeActionAutomatic( int nIdResource, String strResourceType, int nIdWorkflow )
    {
    	this.executeActionAutomatic(nIdResource, strResourceType, nIdWorkflow, null);
    }
}
