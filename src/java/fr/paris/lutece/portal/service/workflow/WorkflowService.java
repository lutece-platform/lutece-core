/*
 * Copyright (c) 2002-2022, City of Paris
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

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.plugins.workflowcore.service.workflow.IWorkflowService;
import fr.paris.lutece.portal.business.event.ResourceEvent;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.event.ResourceEventManager;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.TransactionManager;

/**
 *
 * WorkflowService
 *
 */
public final class WorkflowService
{
    private static final String PLUGIN_WORKFLOW_NAME = "workflow";
    private static final String BEAN_WORKFLOW_PROVIDER = "workflow.workflowProvider";
    private static WorkflowService _singleton;
    private boolean _bServiceAvailable = true;
    private IWorkflowService _service;
    private IWorkflowProvider _provider;

    /**
     * Private constructor
     */
    private WorkflowService( )
    {
        try
        {
            _service = SpringContextService.getBean( fr.paris.lutece.plugins.workflowcore.service.workflow.WorkflowService.BEAN_SERVICE );
            _provider = SpringContextService.getBean( BEAN_WORKFLOW_PROVIDER );
            _bServiceAvailable = ( _service != null ) && ( _provider != null );
        }
        catch( CannotLoadBeanClassException | NoSuchBeanDefinitionException | BeanDefinitionStoreException e )
        {
            _bServiceAvailable = false;
        }
    }

    /**
     * Returns the unique instance of the service
     * 
     * @return The instance of the service
     */
    public static synchronized WorkflowService getInstance( )
    {
        if ( _singleton == null )
        {
            _singleton = new WorkflowService( );
        }
        return _singleton;
    }

    /**
     * Check if the workflow service is available. To be available, the following conditions must be verified :
     * <ul>
     * <li>the Bean service is not null</li>
     * <li>the plugin-workflow must be enable</li>
     * </ul>
     * 
     * @return true if the workflow service is available
     */
    public boolean isAvailable( )
    {
        // LUTECE-1273 : Condition ( _service != null && _provider != null ) in case the
        // plugin-workflow is removed from a webapp
        return _bServiceAvailable && ( _service != null ) && ( _provider != null ) && PluginService.isPluginEnable( PLUGIN_WORKFLOW_NAME );
    }

    /**
     * returns a list of actions possible for a given document based on the status of the document in the workflow and the user role
     * 
     * @param nIdResource
     *            the document id
     * @param strResourceType
     *            the document type
     * @param user
     *            the adminUser
     * @param nIdWorkflow
     *            the workflow id
     * @return a list of Action
     */
    public Collection<Action> getActions( int nIdResource, String strResourceType, int nIdWorkflow, User user )
    {
        if ( isAvailable( ) )
        {
            Collection<Action> listActions = _service.getActions( nIdResource, strResourceType, nIdWorkflow );

            return _provider.getActions( nIdResource, strResourceType, listActions, user );
        }

        return null;
    }

    /**
     * returns a list of actions possible for a given document based on the status of the document in the workflow and the user role
     * 
     * @param nIdResource
     *            the document id
     * @param strResourceType
     *            the document type
     * @param user
     *            the adminUser
     * @param nIdWorkflow
     *            the workflow id
     * @return a list of Action
     * @deprecated use getActions( int, String, int, User )
     */
    @Deprecated
    public Collection<Action> getActions( int nIdResource, String strResourceType, int nIdWorkflow, AdminUser user )
    {
        return getActions( nIdResource, strResourceType, nIdWorkflow, (User) user );
    }

    /**
     * returns a list of actions possible for a given document based on the status of the document in the workflow and the user role
     * 
     * @param listIdResource
     *            the list of resource id
     * @param strResourceType
     *            the document type
     * @param nIdExternalParentId
     *            the external parent identifier
     * @param nIdWorkflow
     *            the workflow id
     * @param user
     *            the User
     * @return a list of Action
     */
    public Map<Integer, List<Action>> getActions( List<Integer> listIdResource, String strResourceType, Integer nIdExternalParentId, int nIdWorkflow,
            User user )
    {
        if ( isAvailable( ) )
        {
            Map<Integer, List<Action>> mapActions = _service.getActions( listIdResource, strResourceType, nIdExternalParentId, nIdWorkflow );

            return _provider.getActions( strResourceType, mapActions, user );
        }

        return null;
    }

    /**
     * returns a list of actions possible for a given document based on the status of the document in the workflow and the user role
     * 
     * @param listIdResource
     *            the list of resource id
     * @param strResourceType
     *            the document type
     * @param nIdExternalParentId
     *            the external parent identifier
     * @param nIdWorkflow
     *            the workflow id
     * @param user
     *            the User
     * @return a list of Action
     * @deprecated getActions( List, String, Integer, int, User )
     */
    @Deprecated
    public Map<Integer, List<Action>> getActions( List<Integer> listIdResource, String strResourceType, Integer nIdExternalParentId, int nIdWorkflow,
            AdminUser user )
    {
        return getActions( listIdResource, strResourceType, nIdExternalParentId, nIdWorkflow, (User) user );
    }

    /**
     * return true if a form is associate to the action
     *
     * @param nIdAction
     *            the action id
     * @param locale
     *            the loacle
     * @return true if a form is associate to the action
     */
    public boolean isDisplayTasksForm( int nIdAction, Locale locale )
    {
        return isAvailable( ) && _service.isDisplayTasksForm( nIdAction, locale );
    }

    /**
     * Proceed action given in parameter
     * 
     * @param nIdResource
     *            the resource id
     * @param strResourceType
     *            the resource type
     * @param nIdAction
     *            the action id
     * @param nExternalParentId
     *            the external parent id
     * @param request
     *            the request
     * @param locale
     *            locale
     * @param bIsAutomatic
     *            Is automatic
     * @deprecated use doProcessAction( int, String, int, Integer, HttpServletRequest, Locale, boolean, User )
     */
    @Deprecated
    public void doProcessAction( int nIdResource, String strResourceType, int nIdAction, Integer nExternalParentId, HttpServletRequest request, Locale locale,
            boolean bIsAutomatic )
    {
        doProcessAction( nIdResource, strResourceType, nIdAction, nExternalParentId, request, locale, bIsAutomatic, null );
    }

    /**
     * Proceed action given in parameter
     * 
     * @param nIdResource
     *            the resource id
     * @param strResourceType
     *            the resource type
     * @param nIdAction
     *            the action id
     * @param nExternalParentId
     *            the external parent id
     * @param request
     *            the request
     * @param locale
     *            locale
     * @param bIsAutomatic
     *            Is automatic
     * @param user
     *            The User
     */
    public void doProcessAction( int nIdResource, String strResourceType, int nIdAction, Integer nExternalParentId, HttpServletRequest request, Locale locale,
            boolean bIsAutomatic, User user )
    {
        if ( isAvailable( ) && canProcessAction( nIdResource, strResourceType, nIdAction, nExternalParentId, request, bIsAutomatic, user ) )
        {
            TransactionManager.beginTransaction( null );

            try
            {
                String strUserAccessCode = bIsAutomatic ? null : _provider.getUserAccessCode( request, user );
                _service.doProcessAction( nIdResource, strResourceType, nIdAction, nExternalParentId, request, locale, bIsAutomatic, strUserAccessCode, user );
                TransactionManager.commitTransaction( null );

                registerResourceEvent( nIdResource, strResourceType );
            }
            catch( Exception e )
            {
                TransactionManager.rollBack( null );
                throw new AppException( e.getMessage( ), e );
            }
        }
    }

    /**
     * returns the actions history performed on a resource
     * 
     * @param nIdResource
     *            the resource id
     * @param strResourceType
     *            the resource type
     * @param request
     *            the request
     * @param nIdWorkflow
     *            the workflow id
     * @param locale
     *            the locale
     * @return the history of actions performed on a resource
     * @deprecated use getDisplayDocumentHistory( int, String, int, HttpServletRequest, Locale, User )
     */
    @Deprecated
    public String getDisplayDocumentHistory( int nIdResource, String strResourceType, int nIdWorkflow, HttpServletRequest request, Locale locale )
    {
        return getDisplayDocumentHistory( nIdResource, strResourceType, nIdWorkflow, request, locale, null );
    }

    /**
     * returns the actions history performed on a resource
     * 
     * @param nIdResource
     *            the resource id
     * @param strResourceType
     *            the resource type
     * @param request
     *            the request
     * @param nIdWorkflow
     *            the workflow id
     * @param locale
     *            the locale
     * @param user
     *            The User
     * @return the history of actions performed on a resource
     */
    public String getDisplayDocumentHistory( int nIdResource, String strResourceType, int nIdWorkflow, HttpServletRequest request, Locale locale, User user )
    {
        return isAvailable( ) ? _provider.getDisplayDocumentHistory( nIdResource, strResourceType, nIdWorkflow, request, locale, user ) : null;
    }

    /**
     * returns the actions history performed on a resource
     * 
     * @param nIdResource
     *            the resource id
     * @param strResourceType
     *            the resource type
     * @param request
     *            the request
     * @param nIdWorkflow
     *            the workflow id
     * @param locale
     *            the locale
     * @param model
     *            The model to add to the default model
     * @param strTemplate
     *            The template
     * @return the history of actions performed on a resource
     * @deprecated use getDisplayDocumentHistory( int, String, int, HttpServletRequest, Locale, Map, String, User )
     */
    @Deprecated
    public String getDisplayDocumentHistory( int nIdResource, String strResourceType, int nIdWorkflow, HttpServletRequest request, Locale locale,
            Map<String, Object> model, String strTemplate )
    {
        return getDisplayDocumentHistory( nIdResource, strResourceType, nIdWorkflow, request, locale, model, strTemplate, null );
    }

    /**
     * returns the actions history performed on a resource
     * 
     * @param nIdResource
     *            the resource id
     * @param strResourceType
     *            the resource type
     * @param request
     *            the request
     * @param nIdWorkflow
     *            the workflow id
     * @param locale
     *            the locale
     * @param model
     *            The model to add to the default model
     * @param strTemplate
     *            The template
     * @param user
     *            The User
     * @return the history of actions performed on a resource
     */
    public String getDisplayDocumentHistory( int nIdResource, String strResourceType, int nIdWorkflow, HttpServletRequest request, Locale locale,
            Map<String, Object> model, String strTemplate, User user )
    {
        if ( !isAvailable( ) )
        {
            return null;
        }
        try
        {
            return _provider.getDisplayDocumentHistory( nIdResource, strResourceType, nIdWorkflow, request, locale, model, strTemplate, user );
        }
        catch( NoSuchMethodError ex )
        {
            AppLogService.error( "You are using a too old Workflow provider version. Please upgrade." );
            return _provider.getDisplayDocumentHistory( nIdResource, strResourceType, nIdWorkflow, request, locale, user );
        }
    }

    /**
     * Perform the information on the various tasks associated with the given action specified in parameter
     * 
     * @param nIdResource
     *            the resource id
     * @param strResourceType
     *            the resource type
     * @param nExternalParentId
     *            the external parent id
     * @param request
     *            the request
     * @param nIdAction
     *            the action id
     * @param locale
     *            the locale
     * @return null if there is no error in the task form else return the error message url
     * @deprecated use doSaveTasksForm( int, String, int, Integer, HttpServletRequest, Locale, User )
     */
    @Deprecated
    public String doSaveTasksForm( int nIdResource, String strResourceType, int nIdAction, Integer nExternalParentId, HttpServletRequest request,
            Locale locale )
    {
        return doSaveTasksForm( nIdResource, strResourceType, nIdAction, nExternalParentId, request, locale, null );
    }

    /**
     * Perform the information on the various tasks associated with the given action specified in parameter
     * 
     * @param nIdResource
     *            the resource id
     * @param strResourceType
     *            the resource type
     * @param nExternalParentId
     *            the external parent id
     * @param request
     *            the request
     * @param nIdAction
     *            the action id
     * @param locale
     *            the locale
     * @param user
     *            the user
     * @return null if there is no error in the task form else return the error message url
     */
    public String doSaveTasksForm( int nIdResource, String strResourceType, int nIdAction, Integer nExternalParentId, HttpServletRequest request, Locale locale,
            User user )
    {
        if ( isAvailable( ) )
        {
            String strError = _provider.doValidateTasksForm( nIdResource, strResourceType, nIdAction, request, locale, user );

            if ( StringUtils.isNotBlank( strError ) )
            {
                return strError;
            }

            doProcessAction( nIdResource, strResourceType, nIdAction, nExternalParentId, request, locale, false, user );
        }

        return null;
    }

    /**
     * Get the list of ids of resources of a given type that are in a given state
     * 
     * @param nIdState
     *            The id of the state of resources to get
     * @param strResourceType
     *            The type of resources to get
     * @return The list of resources matching both given state id and resource given. Return an empty list if no resource was found, or if the state does not
     *         exist.
     */
    public List<Integer> getResourceIdListByIdState( int nIdState, String strResourceType )
    {
        if ( isAvailable( ) )
        {
            return _service.getResourceIdListByIdState( nIdState, strResourceType );
        }

        return null;
    }

    /**
     * Remove in every workflows the resource specified in parameter
     * 
     * @param nIdResource
     *            the resource id
     * @param strResourceType
     *            the resource type
     */
    public void doRemoveWorkFlowResource( int nIdResource, String strResourceType )
    {
        if ( isAvailable( ) )
        {
            TransactionManager.beginTransaction( null );

            try
            {
                _service.doRemoveWorkFlowResource( nIdResource, strResourceType );
                TransactionManager.commitTransaction( null );
            }
            catch( Exception e )
            {
                TransactionManager.rollBack( null );
                throw new AppException( e.getMessage( ), e );
            }
        }
    }

    /**
     * Remove list of resource workflow by list id
     * 
     * @param lListIdResource
     *            list of id resource
     * @param strResourceType
     *            the ressource type
     * @param nIdWorflow
     *            the workflow id
     */
    public void doRemoveWorkFlowResourceByListId( List<Integer> lListIdResource, String strResourceType, Integer nIdWorflow )
    {
        if ( isAvailable( ) )
        {
            TransactionManager.beginTransaction( null );

            try
            {
                _service.doRemoveWorkFlowResourceByListId( lListIdResource, strResourceType, nIdWorflow );
                TransactionManager.commitTransaction( null );
            }
            catch( Exception e )
            {
                TransactionManager.rollBack( null );
                throw new AppException( e.getMessage( ), e );
            }
        }
    }

    /**
     * returns the tasks form
     * 
     * @param nIdResource
     *            the document id
     * @param strResourceType
     *            the document type
     * @param request
     *            the request
     * @param nIdAction
     *            the action id
     * @param locale
     *            the locale
     * @return the tasks form associated to the action
     *
     * @deprecated use getDisplayTasksForm( int, String, int, HttpServletRequest, Locale, User )
     */
    @Deprecated
    public String getDisplayTasksForm( int nIdResource, String strResourceType, int nIdAction, HttpServletRequest request, Locale locale )
    {
        return getDisplayTasksForm( nIdResource, strResourceType, nIdAction, request, locale, null );

    }

    /**
     * returns the tasks form
     * 
     * @param nIdResource
     *            the document id
     * @param strResourceType
     *            the document type
     * @param request
     *            the request
     * @param nIdAction
     *            the action id
     * @param locale
     *            the locale
     * @param user
     *            the user
     * @return the tasks form associated to the action
     *
     */
    public String getDisplayTasksForm( int nIdResource, String strResourceType, int nIdAction, HttpServletRequest request, Locale locale, User user )
    {
        return isAvailable( ) ? _provider.getDisplayTasksForm( nIdResource, strResourceType, nIdAction, request, locale, user ) : null;
    }

    /**
     * Check that a given user is allowed to view a resource depending the state of the resource
     * 
     * @param nIdResource
     *            the document id
     * @param strResourceType
     *            the document type
     * @param user
     *            the User
     * @param nIdWorkflow
     *            the workflow id
     * @return a list of Action
     */
    public boolean isAuthorized( int nIdResource, String strResourceType, int nIdWorkflow, User user )
    {
        return isAvailable( ) && _provider.isAuthorized( nIdResource, strResourceType, nIdWorkflow, user );
    }

    /**
     * Check that a given user is allowed to view a resource depending the state of the resource
     * 
     * @param nIdResource
     *            the document id
     * @param strResourceType
     *            the document type
     * @param user
     *            the User
     * @param nIdWorkflow
     *            the workflow id
     * @return a list of Action
     * @deprecated use isAuthorized( int, String, int, User )
     */
    @Deprecated
    public boolean isAuthorized( int nIdResource, String strResourceType, int nIdWorkflow, AdminUser user )
    {
        return isAuthorized( nIdResource, strResourceType, nIdWorkflow, (User) user );
    }

    /**
     * Get all authorized resource Id
     * 
     * @param strResourceType
     *            the resource type
     * @param nIdWorkflow
     *            the workflow id
     * @param nIdWorkflowState
     *            The workflow state id or -1 for all workflow states
     * @param nExternalParentId
     *            The external parent id
     * @param user
     *            the User
     * @return a list resource id
     */
    public List<Integer> getAuthorizedResourceList( String strResourceType, int nIdWorkflow, int nIdWorkflowState, Integer nExternalParentId, User user )
    {
        return isAvailable( ) ? _provider.getAuthorizedResourceList( strResourceType, nIdWorkflow, nIdWorkflowState, nExternalParentId, user ) : null;
    }

    /**
     * Get all authorized resource Id
     * 
     * @param strResourceType
     *            the resource type
     * @param nIdWorkflow
     *            the workflow id
     * @param nIdWorkflowState
     *            The workflow state id or -1 for all workflow states
     * @param nExternalParentId
     *            The external parent id
     * @param user
     *            the User
     * @return a list resource id
     * @deprecated use getAuthorizedResourceList( String, int, int, Integer, User )
     */
    @Deprecated
    public List<Integer> getAuthorizedResourceList( String strResourceType, int nIdWorkflow, int nIdWorkflowState, Integer nExternalParentId, AdminUser user )
    {
        return getAuthorizedResourceList( strResourceType, nIdWorkflow, nIdWorkflowState, nExternalParentId, (User) user );

    }

    /**
     * Get all authorized resource Id by list of state
     * 
     * @param strResourceType
     *            the resource type
     * @param nIdWorkflow
     *            the workflow id
     * @param lListIdWorkflowState
     *            The workflow state <b>id or null</b> for all workflow states
     * @param nExternalParentId
     *            the externbal parent identifier
     * @param user
     *            the User
     * @return a list resource id
     */
    public List<Integer> getAuthorizedResourceList( String strResourceType, int nIdWorkflow, List<Integer> lListIdWorkflowState, Integer nExternalParentId,
            User user )
    {
        return isAvailable( ) ? _provider.getAuthorizedResourceList( strResourceType, nIdWorkflow, lListIdWorkflowState, nExternalParentId, user ) : null;
    }

    /**
     * Get all authorized resource Id by list of state
     * 
     * @param strResourceType
     *            the resource type
     * @param nIdWorkflow
     *            the workflow id
     * @param lListIdWorkflowState
     *            The workflow state <b>id or null</b> for all workflow states
     * @param nExternalParentId
     *            the externbal parent identifier
     * @param user
     *            the User
     * @return a list resource id
     * @deprecated use getAuthorizedResourceList( String, int, List, Integer, User )
     */
    @Deprecated
    public List<Integer> getAuthorizedResourceList( String strResourceType, int nIdWorkflow, List<Integer> lListIdWorkflowState, Integer nExternalParentId,
            AdminUser user )
    {
        return getAuthorizedResourceList( strResourceType, nIdWorkflow, lListIdWorkflowState, nExternalParentId, (User) user );
    }

    /**
     * return a reference list which contains a list enabled workflow
     * 
     * @param user
     *            the User
     * @param locale
     *            the locale
     * @return a reference list which contains a list enabled workflow
     */
    public ReferenceList getWorkflowsEnabled( User user, Locale locale )
    {
        return isAvailable( ) ? _provider.getWorkflowsEnabled( user, locale ) : null;
    }

    /**
     * return a reference list which contains a list enabled workflow
     * 
     * @param user
     *            the User
     * @param locale
     *            the locale
     * @return a reference list which contains a list enabled workflow
     * @deprecated use getWorkflowsEnabled( User, Locale )
     */
    @Deprecated
    public ReferenceList getWorkflowsEnabled( AdminUser user, Locale locale )
    {
        return getWorkflowsEnabled( (User) user, locale );
    }

    /**
     * returns all state of a given workflow
     * 
     * @param user
     *            the user
     * @param nIdWorkflow
     *            the workflow id
     * @return the state of a given document
     */
    public Collection<State> getAllStateByWorkflow( int nIdWorkflow, User user )
    {
        if ( isAvailable( ) )
        {
            Collection<State> listStates = _service.getAllStateByWorkflow( nIdWorkflow );

            return _provider.getAllStateByWorkflow( listStates, user );
        }

        return null;
    }

    /**
     * returns all state of a given workflow
     * 
     * @param user
     *            the adminUser
     * @param nIdWorkflow
     *            the workflow id
     * @return the state of a given document
     * @deprecated use getAllStateByWorkflow( int, User )
     */
    @Deprecated
    public Collection<State> getAllStateByWorkflow( int nIdWorkflow, AdminUser user )
    {
        return getAllStateByWorkflow( nIdWorkflow, (User) user );
    }

    /**
     * returns the state of a given document of the document in the workflow and the user role
     * 
     * @param nIdResource
     *            the document id
     * @param strResourceType
     *            the document type
     * @param nIdWorkflow
     *            the workflow id
     * @param nIdExternalParentId
     *            the external parent id
     * @return the state of a given document
     */
    public State getState( int nIdResource, String strResourceType, int nIdWorkflow, Integer nIdExternalParentId )
    {
        if ( isAvailable( ) )
        {
            State state = null;
            TransactionManager.beginTransaction( null );

            try
            {
                state = _service.getState( nIdResource, strResourceType, nIdWorkflow, nIdExternalParentId );
                TransactionManager.commitTransaction( null );
            }
            catch( Exception e )
            {
                TransactionManager.rollBack( null );
                throw new AppException( e.getMessage( ), e );
            }

            return state;
        }

        return null;
    }

    /**
     * Execute action automatic
     * 
     * @param nIdResource
     *            the document id
     * @param strResourceType
     *            the document type
     * @param nIdWorkflow
     *            the workflow id
     * @param nExternalParentId
     *            the external parent id
     * @deprecated use executeActionAutomatic( int, String, int, Integer, User )
     */
    @Deprecated
    public void executeActionAutomatic( int nIdResource, String strResourceType, int nIdWorkflow, Integer nExternalParentId )
    {
        executeActionAutomatic( nIdResource, strResourceType, nIdWorkflow, nExternalParentId, null );
    }

    /**
     * Execute action automatic
     * 
     * @param nIdResource
     *            the document id
     * @param strResourceType
     *            the document type
     * @param nIdWorkflow
     *            the workflow id
     * @param nExternalParentId
     *            the external parent id
     * @param user
     *            the user
     */
    public void executeActionAutomatic( int nIdResource, String strResourceType, int nIdWorkflow, Integer nExternalParentId, User user )
    {
        if ( isAvailable( ) )
        {
            TransactionManager.beginTransaction( null );

            try
            {
                _service.executeActionAutomatic( nIdResource, strResourceType, nIdWorkflow, nExternalParentId, user );
                TransactionManager.commitTransaction( null );

                registerResourceEvent( nIdResource, strResourceType );
            }
            catch( Exception e )
            {
                TransactionManager.rollBack( null );
                throw new AppException( e.getMessage( ), e );
            }
        }
    }

    /**
     * Get the list of mass actions from a given id workflow
     * 
     * @param nIdWorkflow
     *            the id workflow
     * @return the list of mass actions
     */
    public List<Action> getMassActions( int nIdWorkflow )
    {
        return isAvailable( ) ? _service.getMassActions( nIdWorkflow ) : null;
    }

    /**
     * Get the list of mass actions from a given id workflow
     * 
     * @param nIdWorkflow
     *            the id workflow
     * @param nIdState
     * @param user
     * @return the list of mass actions
     */
    public Collection<Action> getMassActions( int nIdWorkflow, int nIdState, User user )
    {
        if ( !isAvailable( ) )
        {
            return null;
        }

        Collection<Action> listActions = _service.getMassActions( nIdWorkflow, nIdState );

        return _provider.getAuthorizedActions( listActions, user );
    }

    /**
     * Check if the action can be proceed for the given resource
     * 
     * @param nIdResource
     *            the id resource
     * @param strResourceType
     *            the resource type
     * @param nIdAction
     *            the id action
     * @param nExternalParentId
     *            the external parent id
     * @param request
     *            the HTTP request
     * @param bIsAutomatic
     *            is automatic action
     * @return true if the action can proceed, false otherwise
     * @deprecated use canProcessAction( int, String, int, Integer, HttpServletRequest, User )
     */
    @Deprecated
    public boolean canProcessAction( int nIdResource, String strResourceType, int nIdAction, Integer nExternalParentId, HttpServletRequest request,
            boolean bIsAutomatic )
    {
        return canProcessAction( nIdResource, strResourceType, nIdAction, nExternalParentId, request, bIsAutomatic, null );
    }

    /**
     * Check if the action can be proceed for the given resource
     * 
     * @param nIdResource
     *            the id resource
     * @param strResourceType
     *            the resource type
     * @param nIdAction
     *            the id action
     * @param nExternalParentId
     *            the external parent id
     * @param request
     *            the HTTP request
     * @param bIsAutomatic
     *            is automatic action
     * @param user
     *            the RBACUser
     * @return true if the action can proceed, false otherwise
     */
    public boolean canProcessAction( int nIdResource, String strResourceType, int nIdAction, Integer nExternalParentId, HttpServletRequest request,
            boolean bIsAutomatic, User user )
    {
        if ( isAvailable( ) && _service.canProcessAction( nIdResource, strResourceType, nIdAction, nExternalParentId ) )
        {
            if ( bIsAutomatic )
            {
                return true;
            }

            return _provider.canProcessAction( nIdResource, strResourceType, nIdAction, request, user );
        }

        return false;
    }

    /**
     * Proceed automatic reflexive actions of state given in parameter. This method should be called anytime a service changed the state of a resource without
     * proceeding a workflow action
     * 
     * @param nIdResource
     *            the resource id
     * @param strResourceType
     *            the resource type
     * @param nIdState
     *            the state of the resource id
     * @param nIdExternalParent
     *            the external parent id*
     * @param locale
     *            locale
     * @deprecated use doProcessAutomaticReflexiveActions( int, String, int, Integer, Locale, User )
     */
    @Deprecated
    public void doProcessAutomaticReflexiveActions( int nIdResource, String strResourceType, int nIdState, Integer nIdExternalParent, Locale locale )
    {
        doProcessAutomaticReflexiveActions( nIdResource, strResourceType, nIdState, nIdExternalParent, locale, null );
    }

    /**
     * Proceed automatic reflexive actions of state given in parameter. This method should be called anytime a service changed the state of a resource without
     * proceeding a workflow action
     * 
     * @param nIdResource
     *            the resource id
     * @param strResourceType
     *            the resource type
     * @param nIdState
     *            the state of the resource id
     * @param nIdExternalParent
     *            the external parent id*
     * @param locale
     *            locale
     * @param user
     *            the user
     */
    public void doProcessAutomaticReflexiveActions( int nIdResource, String strResourceType, int nIdState, Integer nIdExternalParent, Locale locale, User user )
    {
        if ( isAvailable( ) )
        {
            TransactionManager.beginTransaction( null );

            try
            {
                _service.doProcessAutomaticReflexiveActions( nIdResource, strResourceType, nIdState, nIdExternalParent, locale, user );
                TransactionManager.commitTransaction( null );

                registerResourceEvent( nIdResource, strResourceType );
            }
            catch( Exception e )
            {
                TransactionManager.rollBack( null );
                throw new AppException( e.getMessage( ), e );
            }
        }
    }

    /**
     * Create and process a ResourceEvent.
     * 
     * @param nIdResource
     * @param strResourceType
     */
    private void registerResourceEvent( int nIdResource, String strResourceType )
    {
        ResourceEvent formResponseEvent = new ResourceEvent( );
        formResponseEvent.setIdResource( String.valueOf( nIdResource ) );
        formResponseEvent.setTypeResource( strResourceType );

        ResourceEventManager.fireUpdatedResource( formResponseEvent );
    }
}
