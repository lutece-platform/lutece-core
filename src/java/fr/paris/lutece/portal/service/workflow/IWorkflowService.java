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
package fr.paris.lutece.portal.service.workflow;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.workflow.Action;
import fr.paris.lutece.portal.business.workflow.State;
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 * WorkflowService
 */
public interface IWorkflowService
{
    /**
     * returns a list of actions possible for a given document based on the status
     * of the document in the workflow and the user role
     * @param nIdResource the document id
     * @param strResourceType the document type
     * @param user the adminUser
     * @param nIdWorkflow the workflow id
     * @return a list of Action
     */
    Collection<Action> getActions( int nIdResource, String strResourceType, int nIdWorkflow, AdminUser user );

    /**
     * returns a list of actions possible for a given document based on the status
     * of the document in the workflow and the user role
     * @param listIdResource the list of resource id
     * @param strResourceType the document type
     * @param nIdExternalParentId the external parent identifier
     * @param nIdWorkflow the workflow id
     * @param user the adminUser
     * @return a list of Action
     */
    HashMap<Integer, List<Action>> getActions( List<Integer> listIdResource, String strResourceType,
        Integer nIdExternalParentId, int nIdWorkflow, AdminUser user );

    /**
     * returns the state of a  given document
     * of the document in the workflow and the user role
     * @param nIdResource the document id
     * @param strResourceType the document type
     * @param nIdWorkflow the workflow id
     * @param user the adminUser
     * @return the state of a given document
     */
    @Deprecated
    State getState( int nIdResource, String strResourceType, int nIdWorkflow, AdminUser user );

    /**
     * returns the state of a  given document
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param nIdWorkflow the workflow id
     * @param nIdExternalParentId the external parent id
     * @param user The user
     * @return the state of a given document
     */
    State getState( int nIdResource, String strResourceType, int nIdWorkflow, Integer nIdExternalParentId,
        AdminUser user );

    /**
     * return true if a form is associate to the action
     *
     * @param nIdAction the action id
     * @param locale the loacle
     * @return true if a form is associate to the action
     */
    boolean isDisplayTasksForm( int nIdAction, Locale locale );

    /**
     * Proceed action given in parameter
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param nIdAction the action id
     * @param request the request
     * @param locale locale
     * @param isAutomatic true if action is automatic
     */
    @Deprecated
    void doProcessAction( int nIdResource, String strResourceType, int nIdAction, HttpServletRequest request,
        Locale locale, boolean isAutomatic );

    /**
     * Proceed action given in parameter
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param nIdAction the action id
     * @param nExternalParentId the external parent id*
     * @param request the request
     * @param locale locale
     * @param isAutomatic true if action is automatic
     */
    void doProcessAction( int nIdResource, String strResourceType, int nIdAction, Integer nExternalParentId,
        HttpServletRequest request, Locale locale, boolean isAutomatic );

    /**
     * returns the  actions history performed on a resource
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param nIdWorkflow the workflow id
     * @param request the request
     * @param locale the locale
     * @return the history of actions performed on a resource
     */
    String getDisplayDocumentHistory( int nIdResource, String strResourceType, int nIdWorkflow,
        HttpServletRequest request, Locale locale );

    /**
     * returns a xml wich contains the  actions history performed on a resource
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param nIdWorkflow the workflow id
     * @param request the request
     * @param locale the locale
     * @return a xml wich contains  the history of actions performed on a resource
     */
    String getDocumentHistoryXml( int nIdResource, String strResourceType, int nIdWorkflow, HttpServletRequest request,
        Locale locale );

    /**
     * Perform the information on the various tasks associated with the given action specified in parameter
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param nIdAction the action id
     * @param request the request
     * @param locale the locale
     * @return null if there is no error in the task form
     *                    else return the error message url
     */
    @Deprecated
    String doSaveTasksForm( int nIdResource, String strResourceType, int nIdAction, HttpServletRequest request,
        Locale locale );

    /**
     * Perform the information on the various tasks associated with the given action specified in parameter
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param nIdAction the action id
     * @param nExternalParentId the external parent id
     * @param request the request
     * @param locale the locale
     * @return null if there is no error in the task form
     *                    else return the error message url
     */
    String doSaveTasksForm( int nIdResource, String strResourceType, int nIdAction, Integer nExternalParentId,
        HttpServletRequest request, Locale locale );

    /**
     * Remove in all workflows the resource specified in parameter
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     */
    void doRemoveWorkFlowResource( int nIdResource, String strResourceType );

    /**
     * Remove list of resource by list of id resource
     * @param lListIdResource the list of resource id
     * @param strResourceType  the resource type
     * @param nIdWorflow the workflow id
     */
    void doRemoveWorkFlowResourceByListId( List<Integer> lListIdResource, String strResourceType, Integer nIdWorflow );

    /**
     * returns the tasks form
     * @param nIdResource the document id
     * @param strResourceType the document type
     * @param nIdAction the action id
     * @param request the request
     * @param locale the locale
     * @return the tasks form associated to the action
     */
    String getDisplayTasksForm( int nIdResource, String strResourceType, int nIdAction, HttpServletRequest request,
        Locale locale );

    /**
     * Check that a given user is allowed to view a resource depending the state of the resource
     * @param nIdResource the document id
     * @param strResourceType the document type
     * @param nIdWorkflow the workflow id*
     * @param  user the AdminUser
     * @return a list of Action
     */
    boolean isAuthorized( int nIdResource, String strResourceType, int nIdWorkflow, AdminUser user );

    /**
     * Get all authorized resource Id
     * @param strResourceType the resource type
     * @param nIdWorkflow the workflow id
     * @param nIdWorkflowState The workflow state id
     * @param user the AdminUser
     * @return a list resource id
     */
    @Deprecated
    List<Integer> getAuthorizedResourceList( String strResourceType, int nIdWorkflow, int nIdWorkflowState,
        AdminUser user );

    /**
     * Get all authorized resource Id
     * @param strResourceType the resource type
     * @param nIdWorkflow the workflow id
     * @param nIdWorkflowState The workflow state id
     * @param nExternalParentId The external parent id
     * @param user the AdminUser
     * @return
     */
    List<Integer> getAuthorizedResourceList( String strResourceType, int nIdWorkflow, int nIdWorkflowState,
        Integer nExternalParentId, AdminUser user );

    /**
     * Get all authorized resource Id
     * @param strResourceType the resource type
     * @param nIdWorkflow the workflow id
     * @param lListIdWorkflowState The list workflow state id
     * @param nExternalParentId he external parent id
     * @param user the AdminUser
     * @return
     */
    List<Integer> getAuthorizedResourceList( String strResourceType, int nIdWorkflow,
        List<Integer> lListIdWorkflowState, Integer nExternalParentId, AdminUser user );

    /**
     * return a referencelist wich contains a list enabled workflow
     * @param user the AdminUser
     * @param locale the locale
     * @return a referencelist wich contains a list enabled workflow
     */
    ReferenceList getWorkflowsEnabled( AdminUser user, Locale locale );

    /**
     * returns all state of a  given workflow
     * @param nIdWorkflow the workflow id
     * @param user the adminUser
     * @return the state of a given document
     */
    Collection<State> getAllStateByWorkflow( int nIdWorkflow, AdminUser user );

    /**
     * <b> WRONG : need to filter on workflow id</b>
     * return a list witch contains idRessource for a given state
     * @param nIdState the id State
     * @param strResourceType the resource type
     * @param user the AdminUser
     * @return a list witch contains idRessource
     */
    @Deprecated
    Collection<Integer> getListIdRessourceByState( int nIdState, String strResourceType, AdminUser user );

    /**
     * Execute action automatic
     * @param nIdResource the document id
     * @param strResourceType the document type
     * @param nIdWorkflow the workflow id
     */
    @Deprecated
    void executeActionAutomatic( int nIdResource, String strResourceType, int nIdWorkflow );

    /**
     * Execute action automatic
     * @param nIdResource the document id
     * @param strResourceType the document type
     * @param nIdWorkflow the workflow id
     * @param nExternalParentId the external parent id
     */
    void executeActionAutomatic( int nIdResource, String strResourceType, int nIdWorkflow, Integer nExternalParentId );
}
