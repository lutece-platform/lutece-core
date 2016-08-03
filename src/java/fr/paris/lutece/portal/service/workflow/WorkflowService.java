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
package fr.paris.lutece.portal.service.workflow;

import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.plugins.workflowcore.service.workflow.IWorkflowService;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.TransactionManager;

import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * WorkflowService
 *
 */
public final class WorkflowService
{
    private static final String PLUGIN_WORKFLOW_NAME = "workflow";
    private static final String BEAN_WORKFLOW_PROVIDER = "workflow.workflowProvider";
    private static volatile WorkflowService _singleton;
    private boolean _bServiceAvailable = true;
    private IWorkflowService _service;
    private IWorkflowProvider _provider;

    /**
     * Private constructor
     */
    private WorkflowService(  )
    {
        try
        {
            _service = SpringContextService.getBean( fr.paris.lutece.plugins.workflowcore.service.workflow.WorkflowService.BEAN_SERVICE );
            _provider = SpringContextService.getBean( BEAN_WORKFLOW_PROVIDER );
            _bServiceAvailable = ( _service != null ) && ( _provider != null );
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
     * Check if the workflow service is available.
     * To be available, the following conditions must be verified :
     * <ul>
     * <li>the Bean service is not null</li>
     * <li>the plugin-workflow must be enable</li>
     * </ul>
     * @return true if the workflow service is available
     */
    public boolean isAvailable(  )
    {
        // LUTECE-1273 : Condition ( _service != null && _provider != null ) in case the plugin-workflow is removed from a webapp  
        return _bServiceAvailable && ( _service != null ) && ( _provider != null ) &&
        PluginService.isPluginEnable( PLUGIN_WORKFLOW_NAME );
    }

    /**
     * returns a list of actions possible for a given document based on the
     * status
     * of the document in the workflow and the user role
     * @param nIdResource the document id
     * @param strResourceType the document type
     * @param user the adminUser
     * @param nIdWorkflow the workflow id
     * @return a list of Action
     */
    public Collection<Action> getActions( int nIdResource, String strResourceType, int nIdWorkflow, AdminUser user )
    {
        if ( isAvailable(  ) )
        {
            Collection<Action> listActions = _service.getActions( nIdResource, strResourceType, nIdWorkflow );

            return _provider.getActions( listActions, user );
        }

        return null;
    }

    /**
     * returns a list of actions possible for a given document based on the
     * status
     * of the document in the workflow and the user role
     * @param listIdResource the list of resource id
     * @param strResourceType the document type
     * @param nIdExternalParentId the external parent identifier
     * @param nIdWorkflow the workflow id
     * @param user the adminUser
     * @return a list of Action
     */
    public Map<Integer, List<Action>> getActions( List<Integer> listIdResource, String strResourceType,
        Integer nIdExternalParentId, int nIdWorkflow, AdminUser user )
    {
        if ( isAvailable(  ) )
        {
            Map<Integer, List<Action>> mapActions = _service.getActions( listIdResource, strResourceType,
                    nIdExternalParentId, nIdWorkflow );

            return _provider.getActions( mapActions, user );
        }

        return null;
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
     * @param nIdAction the action id
     * @param nExternalParentId the external parent id
     * @param request the request
     * @param locale locale
     * @param bIsAutomatic Is automatic
     */
    public void doProcessAction( int nIdResource, String strResourceType, int nIdAction, Integer nExternalParentId,
        HttpServletRequest request, Locale locale, boolean bIsAutomatic )
    {
        if ( isAvailable(  ) &&
                canProcessAction( nIdResource, strResourceType, nIdAction, nExternalParentId, request, bIsAutomatic ) )
        {
            TransactionManager.beginTransaction( null );

            try
            {
                String strUserAccessCode = bIsAutomatic ? null : _provider.getUserAccessCode( request );
                _service.doProcessAction( nIdResource, strResourceType, nIdAction, nExternalParentId, request, locale,
                    bIsAutomatic, strUserAccessCode );
                TransactionManager.commitTransaction( null );
            }
            catch ( Exception e )
            {
                TransactionManager.rollBack( null );
                throw new AppException( e.getMessage(  ), e );
            }
        }
    }

    /**
     * returns the actions history performed on a resource
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
        ? _provider.getDisplayDocumentHistory( nIdResource, strResourceType, nIdWorkflow, request, locale ) : null;
    }

    /**
     * returns the actions history performed on a resource
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param request the request
     * @param nIdWorkflow the workflow id
     * @param locale the locale
     * @param strTemplate The template
     * @return the history of actions performed on a resource
     */
    @Deprecated   
    public String getDisplayDocumentHistory( int nIdResource, String strResourceType, int nIdWorkflow,
        HttpServletRequest request, Locale locale, String strTemplate )
    {
        if( ! isAvailable() )
        {
            return null;
        }
        try
        {
            return _provider.getDisplayDocumentHistory( nIdResource, strResourceType, nIdWorkflow, request, locale, strTemplate );
        }
        catch( NoSuchMethodError ex )
        {
            AppLogService.error( "You are using a too old Workflow provider version. Please upgrade.");
            return _provider.getDisplayDocumentHistory( nIdResource, strResourceType, nIdWorkflow, request, locale );
        }
    }

    /**
     * returns the actions history performed on a resource
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param request the request
     * @param nIdWorkflow the workflow id
     * @param locale the locale
     * @param model The model to add to the default model
     * @param strTemplate The template
     * @return the history of actions performed on a resource
     */
    public String getDisplayDocumentHistory( int nIdResource, String strResourceType, int nIdWorkflow,
        HttpServletRequest request, Locale locale, Map<String, Object> model, String strTemplate )
    {
        if( ! isAvailable() )
        {
            return null;
        }
        try
        {
            return _provider.getDisplayDocumentHistory( nIdResource, strResourceType, nIdWorkflow, request, locale, model, strTemplate );
        }
        catch( NoSuchMethodError ex )
        {
            AppLogService.error( "You are using a too old Workflow provider version. Please upgrade.");
            return _provider.getDisplayDocumentHistory( nIdResource, strResourceType, nIdWorkflow, request, locale );
        }
    }

    /**
     * returns a xml wich contains the actions history performed on a resource
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param request the request
     * @param nIdWorkflow the workflow id
     * @param locale the locale
     * @return a xml wich contains the history of actions performed on a
     *         resource
     */
    public String getDocumentHistoryXml( int nIdResource, String strResourceType, int nIdWorkflow,
        HttpServletRequest request, Locale locale )
    {
        return isAvailable(  )
        ? _provider.getDocumentHistoryXml( nIdResource, strResourceType, nIdWorkflow, request, locale ) : null;
    }

    /**
     * Perform the information on the various tasks associated with the given
     * action specified in parameter
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param nExternalParentId the external parent id
     * @param request the request
     * @param nIdAction the action id
     * @param locale the locale
     * @return null if there is no error in the task form
     *         else return the error message url
     */
    public String doSaveTasksForm( int nIdResource, String strResourceType, int nIdAction, Integer nExternalParentId,
        HttpServletRequest request, Locale locale )
    {
        if ( isAvailable(  ) )
        {
            String strError = _provider.doValidateTasksForm( nIdResource, strResourceType, nIdAction, request, locale );

            if ( StringUtils.isNotBlank( strError ) )
            {
                return strError;
            }

            doProcessAction( nIdResource, strResourceType, nIdAction, nExternalParentId, request, locale, false );
        }

        return null;
    }

    /**
     * Get the list of ids of resources of a given type that are in a given
     * state
     * @param nIdState The id of the state of resources to get
     * @param strResourceType The type of resources to get
     * @return The list of resources matching both given state id and resource
     *         given. Return an empty list if no resource was found, or if the
     *         state does not exist.
     */
    public List<Integer> getResourceIdListByIdState( int nIdState, String strResourceType )
    {
        if ( isAvailable(  ) )
        {
            return _service.getResourceIdListByIdState( nIdState, strResourceType );
        }

        return null;
    }

    /**
     * Remove in every workflows the resource specified in parameter
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     */
    public void doRemoveWorkFlowResource( int nIdResource, String strResourceType )
    {
        if ( isAvailable(  ) )
        {
            TransactionManager.beginTransaction( null );

            try
            {
                _service.doRemoveWorkFlowResource( nIdResource, strResourceType );
                TransactionManager.commitTransaction( null );
            }
            catch ( Exception e )
            {
                TransactionManager.rollBack( null );
                throw new AppException( e.getMessage(  ), e );
            }
        }
    }

    /**
     * Remove list of resource workflow by list id
     * @param lListIdResource list of id resource
     * @param strResourceType the ressource type
     * @param nIdWorflow the workflow id
     */
    public void doRemoveWorkFlowResourceByListId( List<Integer> lListIdResource, String strResourceType,
        Integer nIdWorflow )
    {
        if ( isAvailable(  ) )
        {
            TransactionManager.beginTransaction( null );

            try
            {
                _service.doRemoveWorkFlowResourceByListId( lListIdResource, strResourceType, nIdWorflow );
                TransactionManager.commitTransaction( null );
            }
            catch ( Exception e )
            {
                TransactionManager.rollBack( null );
                throw new AppException( e.getMessage(  ), e );
            }
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
        ? _provider.getDisplayTasksForm( nIdResource, strResourceType, nIdAction, request, locale ) : null;
    }

    /**
     * Check that a given user is allowed to view a resource depending the state
     * of the resource
     * @param nIdResource the document id
     * @param strResourceType the document type
     * @param user the AdminUser
     * @param nIdWorkflow the workflow id
     * @return a list of Action
     */
    public boolean isAuthorized( int nIdResource, String strResourceType, int nIdWorkflow, AdminUser user )
    {
        return isAvailable(  ) && _provider.isAuthorized( nIdResource, strResourceType, nIdWorkflow, user );
    }

    /**
     * Get all authorized resource Id
     * @param strResourceType the resource type
     * @param nIdWorkflow the workflow id
     * @param nIdWorkflowState The workflow state id or -1 for all workflow
     *            states
     * @param nExternalParentId The external parent id
     * @param user the AdminUser
     * @return a list resource id
     */
    public List<Integer> getAuthorizedResourceList( String strResourceType, int nIdWorkflow, int nIdWorkflowState,
        Integer nExternalParentId, AdminUser user )
    {
        return isAvailable(  )
        ? _provider.getAuthorizedResourceList( strResourceType, nIdWorkflow, nIdWorkflowState, nExternalParentId, user )
        : null;
    }

    /**
     * Get all authorized resource Id by list of state
     * @param strResourceType the resource type
     * @param nIdWorkflow the workflow id
     * @param lListIdWorkflowState The workflow state <b>id or null</b> for all
     *            workflow states
     * @param nExternalParentId the externbal parent identifier
     * @param user the AdminUser
     * @return a list resource id
     */
    public List<Integer> getAuthorizedResourceList( String strResourceType, int nIdWorkflow,
        List<Integer> lListIdWorkflowState, Integer nExternalParentId, AdminUser user )
    {
        return isAvailable(  )
        ? _provider.getAuthorizedResourceList( strResourceType, nIdWorkflow, lListIdWorkflowState, nExternalParentId,
            user ) : null;
    }

    /**
     * return a reference list which contains a list enabled workflow
     * @param user the AdminUser
     * @param locale the locale
     * @return a reference list which contains a list enabled workflow
     */
    public ReferenceList getWorkflowsEnabled( AdminUser user, Locale locale )
    {
        return isAvailable(  ) ? _provider.getWorkflowsEnabled( user, locale ) : null;
    }

    /**
     * returns all state of a given workflow
     * @param user the adminUser
     * @param nIdWorkflow the workflow id
     * @return the state of a given document
     */
    public Collection<State> getAllStateByWorkflow( int nIdWorkflow, AdminUser user )
    {
        if ( isAvailable(  ) )
        {
            Collection<State> listStates = _service.getAllStateByWorkflow( nIdWorkflow );

            return _provider.getAllStateByWorkflow( listStates, user );
        }

        return null;
    }

    /**
     * returns the state of a given document
     * of the document in the workflow and the user role
     * @param nIdResource the document id
     * @param strResourceType the document type
     * @param nIdWorkflow the workflow id
     * @param nIdExternalParentId the external parent id
     * @return the state of a given document
     */
    public State getState( int nIdResource, String strResourceType, int nIdWorkflow, Integer nIdExternalParentId )
    {
        if ( isAvailable(  ) )
        {
            State state = null;
            TransactionManager.beginTransaction( null );

            try
            {
                state = _service.getState( nIdResource, strResourceType, nIdWorkflow, nIdExternalParentId );
                TransactionManager.commitTransaction( null );
            }
            catch ( Exception e )
            {
                TransactionManager.rollBack( null );
                throw new AppException( e.getMessage(  ), e );
            }

            return state;
        }

        return null;
    }

    /**
     * Execute action automatic
     * @param nIdResource the document id
     * @param strResourceType the document type
     * @param nIdWorkflow the workflow id
     * @param nExternalParentId the external parent id
     */
    public void executeActionAutomatic( int nIdResource, String strResourceType, int nIdWorkflow,
        Integer nExternalParentId )
    {
        if ( isAvailable(  ) )
        {
            TransactionManager.beginTransaction( null );

            try
            {
                _service.executeActionAutomatic( nIdResource, strResourceType, nIdWorkflow, nExternalParentId );
                TransactionManager.commitTransaction( null );
            }
            catch ( Exception e )
            {
                TransactionManager.rollBack( null );
                throw new AppException( e.getMessage(  ), e );
            }
        }
    }

    /**
     * Get the list of mass actions from a given id workflow
     * @param nIdWorkflow the id workflow
     * @return the list of mass actions
     */
    public List<Action> getMassActions( int nIdWorkflow )
    {
        return isAvailable(  ) ? _service.getMassActions( nIdWorkflow ) : null;
    }

    /**
     * Check if the action can be proceed for the given resource
     * @param nIdResource the id resource
     * @param strResourceType the resource type
     * @param nIdAction the id action
     * @param nExternalParentId the external parent id
     * @param request the HTTP request
     * @param bIsAutomatic is automatic action
     * @return true if the action can proceed, false otherwise
     */
    public boolean canProcessAction( int nIdResource, String strResourceType, int nIdAction, Integer nExternalParentId,
        HttpServletRequest request, boolean bIsAutomatic )
    {
        if ( isAvailable(  ) )
        {
            if ( _service.canProcessAction( nIdResource, strResourceType, nIdAction, nExternalParentId ) )
            {
                if ( bIsAutomatic )
                {
                    return true;
                }

                return _provider.canProcessAction( nIdAction, request );
            }
        }

        return false;
    }

    /**
     * Proceed automatic reflexive actions of state given in parameter. This
     * method should be called anytime a service changed the state of a resource
     * without proceeding a workflow action
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param nIdState the state of the resource id
     * @param nIdExternalParent the external parent id*
     * @param locale locale
     */
    public void doProcessAutomaticReflexiveActions( int nIdResource, String strResourceType, int nIdState,
        Integer nIdExternalParent, Locale locale )
    {
        if ( isAvailable(  ) )
        {
            TransactionManager.beginTransaction( null );

            try
            {
                _service.doProcessAutomaticReflexiveActions( nIdResource, strResourceType, nIdState, nIdExternalParent,
                    locale );
                TransactionManager.commitTransaction( null );
            }
            catch ( Exception e )
            {
                TransactionManager.rollBack( null );
                throw new AppException( e.getMessage(  ), e );
            }
        }
    }
}
