/*
 * Copyright (c) 2002-2020, City of Paris
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

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * IWorkflowProvider.
 */
public interface IWorkflowProvider
{
    // GET
	

    /**
     * returns a list of actions possible for a given document based on the status of the document in the workflow and the user role.
     *
     * @param nIdResource
     * 			the id of the document
     * @param strResourceType
     * 			the type of the document
     * @param listActions
     *            the list actions
     * @param user
     *            the User
     * @return a list of Action
     */
    Collection<Action> getActions( int nIdResource, String strResourceType, Collection<Action> listActions, User user );
    
    
    
    /**
     * returns a list of actions possible for a given document based on the status of the document in the workflow and the user role.
     * 
     * @param strResourceType
     * 			the type of the document
     * @param mapActions
     *            the map actions
     * @param user
     *            the nUser
     * @return a list of Action
     */
    Map<Integer, List<Action>> getActions( String strResourceType, Map<Integer, List<Action>> mapActions, User user );


    /**
     * Returns authorized action list
     * 
     * @param listActions
     * @param nIdWorkflowState
     * @param user
     * @return authorized action list
     */
    Collection<Action> getAuthorizedActions( Collection<Action> listActions, User user ) ;

    /**
     * returns the actions history performed on a resource.
     *
     * @param nIdResource
     *            the resource id
     * @param strResourceType
     *            the resource type
     * @param nIdWorkflow
     *            the workflow id
     * @param request
     *            the request
     * @param locale
     *            the locale
     * @param user
     *            the User
     * @return the history of actions performed on a resource
     */
    String getDisplayDocumentHistory( int nIdResource, String strResourceType, int nIdWorkflow, HttpServletRequest request, Locale locale, User user);

    
    /**
     * returns the actions history performed on a resource.
     *
     * @param nIdResource
     *            the resource id
     * @param strResourceType
     *            the resource type
     * @param nIdWorkflow
     *            the workflow id
     * @param request
     *            the request
     * @param locale
     *            the locale
     * @param model
     *            The model to add to the default model
     * @param strTemplate
     *            The template
     * @param user 
     * 			  the User        
     * @return the history of actions performed on a resource
     */
    
    String getDisplayDocumentHistory( int nIdResource, String strResourceType, int nIdWorkflow, HttpServletRequest request, Locale locale,
            Map<String, Object> model, String strTemplate,User user );

     
    /**
     * returns a xml wich contains the actions history performed on a resource.
     *
     * @param nIdResource
     *            the resource id
     * @param strResourceType
     *            the resource type
     * @param nIdWorkflow
     *            the workflow id
     * @param request
     *            the request
     * @param locale
     *            the locale
     * @param user
     * @return a xml wich contains the history of actions performed on a resource
     */
    String getDocumentHistoryXml( int nIdResource, String strResourceType, int nIdWorkflow, HttpServletRequest request, Locale locale,  User user );

   
    /**
     * returns the tasks form.
     *
     * @param nIdResource
     *            the resource id
     * @param strResourceType
     *            the resource type
     * @param nIdAction
     *            the action id
     * @param request
     *            the request
     * @param locale
     *            the locale
     * @param user 
     * 			  the User        
 
     * @return the tasks form associated to the action
     */
    String getDisplayTasksForm( int nIdResource, String strResourceType, int nIdAction, HttpServletRequest request, Locale locale, User user);

   
    /**
     * Get all authorized resource Id.
     *
     * @param strResourceType
     *            the resource type
     * @param nIdWorkflow
     *            the workflow id
     * @param nIdWorkflowState
     *            The workflow state id
     * @param nExternalParentId
     *            The external parent id
     * @param user
     *            the User
     * @return The list
     */
    
    List<Integer> getAuthorizedResourceList( String strResourceType, int nIdWorkflow, int nIdWorkflowState, Integer nExternalParentId, User user );

   
    /**
     * Get all authorized resource Id.
     *
     * @param strResourceType
     *            the resource type
     * @param nIdWorkflow
     *            the workflow id
     * @param lListIdWorkflowState
     *            The list workflow state id
     * @param nExternalParentId
     *            he external parent id
     * @param user
     *            the User
     * @return The list
     */
    List<Integer> getAuthorizedResourceList( String strResourceType, int nIdWorkflow, List<Integer> lListIdWorkflowState, Integer nExternalParentId,
            User user );

   
    /**
     * return a referencelist wich contains a list enabled workflow.
     *
     * @param user
     *            the User
     * @param locale
     *            the locale
     * @return a referencelist wich contains a list enabled workflow
     */
    ReferenceList getWorkflowsEnabled( User user, Locale locale );


    /**
     * returns all state of a given workflow.
     *
     * @param listStates
     *            the list states
     * @param user
     *            the User
     * @return the state of a given document
     */
    Collection<State> getAllStateByWorkflow( Collection<State> listStates, User user );


    /**
     * The user access code.
     *
     * @param request
     *            the HTTP request
     * @param user  the RBACUser             
     * @return the user access code
     */
    String getUserAccessCode( HttpServletRequest request, User user );


    // CHECK

    /**
     * Check that a given user is allowed to view a resource depending the state of the resource.
     *
     * @param nIdResource
     *            the document id
     * @param strResourceType
     *            the document type
     * @param nIdWorkflow
     *            the workflow id*
     * @param user
     *            the user
     * @return a list of Action
     */
    boolean isAuthorized( int nIdResource, String strResourceType, int nIdWorkflow, User user );


    /**
     * Check if the action can be proceed for the given resource.
     *
     * @param nIdResource
     * 			the id of the resource
     * @param strResourceType
     * 			the type of the resource
     * @param nIdAction
     *            the id action
     * @param request
     *            the HTTP request
     * @param user the User          
     * @return true if the action can proceed, false otherwise
     */
    boolean canProcessAction( int nIdResource, String strResourceType, int nIdAction, HttpServletRequest request, User user );
    
    
    // DO

    /**
     * Test if the information relating to various tasks associated with action are validated.
     *
     * @param nIdResource
     *            the resource id
     * @param strResourceType
     *            the resource type
     * @param nIdAction
     *            the action id
     * @param request
     *            the request
     * @param locale
     *            the locale
     * @param user the user
     * @return null if there is no error in the tasks form, return the error message otherwise
     */
    String doValidateTasksForm( int nIdResource, String strResourceType, int nIdAction, HttpServletRequest request, Locale locale , User user );

}
