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
import fr.paris.lutece.portal.business.user.AdminUser;
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
     * returns a list of actions possible for a given document based on the status
     * of the document in the workflow and the user role.
     *
     * @param listActions the list actions
     * @param user the adminUser
     * @return a list of Action
     */
    Collection<Action> getActions( Collection<Action> listActions, AdminUser user );

    /**
     * returns a list of actions possible for a given document based on the status
     * of the document in the workflow and the user role.
     *
     * @param mapActions the map actions
     * @param user the adminUser
     * @return a list of Action
     */
    Map<Integer, List<Action>> getActions( Map<Integer, List<Action>> mapActions, AdminUser user );

    /**
     * returns the  actions history performed on a resource.
     *
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
     * returns the  actions history performed on a resource.
     *
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param nIdWorkflow the workflow id
     * @param request the request
     * @param locale the locale
     * @param strTemplate The template
     * @return the history of actions performed on a resource
     */
    @Deprecated
    String getDisplayDocumentHistory( int nIdResource, String strResourceType, int nIdWorkflow,
        HttpServletRequest request, Locale locale, String strTemplate );

    /**
     * returns the  actions history performed on a resource.
     *
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param nIdWorkflow the workflow id
     * @param request the request
     * @param locale the locale
     * @param model The model to add to the default model
     * @param strTemplate The template
     * @return the history of actions performed on a resource
     */
    String getDisplayDocumentHistory( int nIdResource, String strResourceType, int nIdWorkflow,
        HttpServletRequest request, Locale locale, Map<String, Object> model, String strTemplate );

    /**
     * returns a xml wich contains the  actions history performed on a resource.
     *
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
     * returns the tasks form.
     *
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param nIdAction the action id
     * @param request the request
     * @param locale the locale
     * @return the tasks form associated to the action
     */
    String getDisplayTasksForm( int nIdResource, String strResourceType, int nIdAction, HttpServletRequest request,
        Locale locale );

    /**
     * Get all authorized resource Id.
     *
     * @param strResourceType the resource type
     * @param nIdWorkflow the workflow id
     * @param nIdWorkflowState The workflow state id
     * @param nExternalParentId The external parent id
     * @param user the AdminUser
     * @return The list
     */
    List<Integer> getAuthorizedResourceList( String strResourceType, int nIdWorkflow, int nIdWorkflowState,
        Integer nExternalParentId, AdminUser user );

    /**
     * Get all authorized resource Id.
     *
     * @param strResourceType the resource type
     * @param nIdWorkflow the workflow id
     * @param lListIdWorkflowState The list workflow state id
     * @param nExternalParentId he external parent id
     * @param user the AdminUser
     * @return The list
     */
    List<Integer> getAuthorizedResourceList( String strResourceType, int nIdWorkflow,
        List<Integer> lListIdWorkflowState, Integer nExternalParentId, AdminUser user );

    /**
     * return a referencelist wich contains a list enabled workflow.
     *
     * @param user the AdminUser
     * @param locale the locale
     * @return a referencelist wich contains a list enabled workflow
     */
    ReferenceList getWorkflowsEnabled( AdminUser user, Locale locale );

    /**
     * returns all state of a  given workflow.
     *
     * @param listStates the list states
     * @param user the adminUser
     * @return the state of a given document
     */
    Collection<State> getAllStateByWorkflow( Collection<State> listStates, AdminUser user );

    /**
     * The user access code.
     *
     * @param request the HTTP request
     * @return the user access code
     */
    String getUserAccessCode( HttpServletRequest request );

    // CHECK

    /**
     * Check that a given user is allowed to view a resource depending the state of the resource.
     *
     * @param nIdResource the document id
     * @param strResourceType the document type
     * @param nIdWorkflow the workflow id*
     * @param user the user
     * @return a list of Action
     */
    boolean isAuthorized( int nIdResource, String strResourceType, int nIdWorkflow, AdminUser user );

    /**
     * Check if the action can be proceed for the given resource.
     *
     * @param nIdAction the id action
     * @param request the HTTP request
     * @return true if the action can proceed, false otherwise
     */
    boolean canProcessAction( int nIdAction, HttpServletRequest request );

    // DO

    /**
     * Test if the information relating to various tasks associated with action are validated.
     *
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param nIdAction the action id
     * @param request the request
     * @param locale the locale
     * @return null if there is no error in the tasks form, return the error message otherwise
     */
    String doValidateTasksForm( int nIdResource, String strResourceType, int nIdAction, HttpServletRequest request,
        Locale locale );

    /**
     * Perform the information on the various tasks associated with the given
     * action specified in parameter.
     *
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param nIdAction the action id
     * @param nExternalParentId the external parent id
     * @param request the request
     * @param locale the locale
     * @param strUserAccessCode the user access code
     * @deprecated This method should not be used. Use
     *             {@link WorkflowService#doProcessAction(int, String, int, Integer, HttpServletRequest, Locale, boolean)
     *             WorkflowService.doProcessAction } instead.
     */
    @Deprecated
    void doSaveTasksForm( int nIdResource, String strResourceType, int nIdAction, Integer nExternalParentId,
        HttpServletRequest request, Locale locale, String strUserAccessCode );
}
